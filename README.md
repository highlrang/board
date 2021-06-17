# 게시판 웹 프로젝트 코드 프리뷰 (게시글 조회 기준)

## Controller

+ contoller

``` java
@GetMapping("/list")
public String postList(@RequestParam("cateId") Long cateId, Model model){
    model.addAttribute("cateId", cateId);
    UserResponseDto user = (UserResponseDto)session.getAttribute("user");
    model.addAttribute("userId", user.getId());
    return "post/list";
}
```

+ api restcontroller
```java
@GetMapping("/api/v1/posts/category/{cateId}")
public Result<PostListDto> postsByCategoryV1(@PathVariable("cateId") Long cateId,
                                           @RequestParam(value = "offset", defaultValue = "0") int offset){
    List<Post> entity = postQueryRepository.findAllWithCategoryAndPublicAndPagingByFetch(cateId, offset);
    List<PostListDto> posts = toPostListDtos(entity);

    Long count = postRepository.countByCategory_Id(cateId);

    return new Result(count, posts);
}
```

------------------------------------------------------

## Service

``` java
@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    public PostDetailResponseDto findById(Long id){
        Post entity = postRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("PostNotFoundException"));
        PostDetailResponseDto post = new PostDetailResponseDto(entity);

        # 총 좋아요 수
        Long totalLike = likeRepository.countAllByPost_Id(post.getId())
                .orElseThrow(() -> new IllegalStateException());
        post.addTotalLike(totalLike);
        
        # 좋아요한 계정
        List<LikeResponseDto> likes = likeRepository.findAllByPost_Id(post.getId())
                .stream()
                .map(l -> new LikeResponseDto(l))
                .collect(Collectors.toList());
        post.addLikeList(likes);

        return post;
    }
}

```
+ 카테고리 기준으로 전체 게시글 조회는 페이징 처리를 위해 api controller에서 postQueryRepository로 직접 조회

------------------------------------------------------

## DTO

```java
@Getter
@NoArgsConstructor
public class PostDetailResponseDto {
    private Long id;
    private Long categoryId;
    private String categoryName;
    private Long writerId;
    private String writerName;
    private String title;
    private String content;
    private Boolean isPublic;
    private Boolean isComplete;
    private Long totalLike;
    private List<LikeResponseDto> likes;

    public PostDetailResponseDto(Post entity){
        this.id = entity.getId();
        this.categoryId = entity.getCategory().getId();
        this.categoryName = entity.getCategory().getName();
        this.writerId = entity.getWriter().getId();
        this.writerName = entity.getWriter().getName();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.isPublic = entity.getIsPublic();
        this.isComplete = entity.getIsComplete();
    }

    public void addTotalLike(Long totalLike){
        this.totalLike = totalLike;
    }

    public void addLikeList(List<LikeResponseDto> likeList){
        this.likes = likeList;
    }

}

```

-------------------------------------------------------------------

## Repository

+ JPA

```java
@Repository
@RequiredArgsConstructor
public class PostQueryRepository {

    private final EntityManager em;

    # 게시글 조회 페이징 처리
    public List<Post> findAllWithCategoryAndPublicAndPagingByFetch(Long cateId, int offset) {
        return em.createQuery(
                "select p from Post p" +
                        " join fetch p.category c" +
                        " join fetch p.writer w" +
                        " where p.category.id =: cateId and p.isPublic = true", Post.class)
                .setParameter("cateId", cateId)
                .setFirstResult(offset)
                .setMaxResults(10)
                .getResultList();
    }

    # 베스트 게시글 조회(좋아요 수 특정 개수 이상인 게시글 조회)
    public List<PostByLikeCountQueryDto> findAllPostsByLikeAndCategory(Long cateId){
        List<PostByLikeCountQueryDto> result = em.createQuery("select new com.myproject.myweb.dto.post.query.PostByLikeCountQueryDto(p.id, c.name, p.title, w.name, p.isComplete, count(l))" +
                " from Like l" +
                " join l.post p" +
                " join p.category c" +
                " join p.writer w" +
                " where p.category.id =: cateId" +
                " group by l.post" +
                " having count(l.post) >= 1" + // 이후에는 100개 이상 best글
                " order by count(l.post) desc", PostByLikeCountQueryDto.class)
                .setParameter("cateId", cateId)
                .getResultList();

        return result;
    }

}
```


+ Spring Data JPA

```java
public interface PostRepository extends JpaRepository<Post, Long>{
    # 쿼리 자동 
    List<Post> findByWriter(User writer);

    Long countByCategory_Id(Long cateId);

    List<Post> findAllByCategory_IdAndIsPublic(Long cateId, Boolean isPublic);
    List<Post> findAllByCategory_IdAndWriter_Id(Long cateId, Long writerId);
    
    
    # 다대일 관계에 있는 외래키 @Query로 fetch join 사용해서 가져오기
    @Query("select p from Post p join fetch p.writer w join fetch p.category c where p.isPublic = true")
    List<Post> findAllFetch();

    @Query("select p from Post p join fetch p.writer w join fetch p.category c where p.writer.id = :writerId")
    List<Post> findByWriterFetch(@Param(value="writerId") Long writerId);

    @Query("select p from Post p join fetch p.writer w join fetch p.category c join fetch p.likeList l where p.id = :id")
    Optional<Post> findByIdFetch(@Param(value="id") Long id);
}    

```

-------------------------------------------------------------------

## domain

```java
@Getter
@NoArgsConstructor
@Entity
@DynamicInsert
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="writer_id")
    private User writer;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    private Category category;

    @NotNull
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Boolean isPublic;

    @Column(columnDefinition = "boolean default false")
    private Boolean isComplete;

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Like> likeList = new ArrayList<>();

    @Builder
    public Post(String title, String content, Boolean isPublic){
        this.title = title;
        this.content = content;
        this.isPublic = isPublic;
    }

    public void addCategory(Category category){
        this.category = category;
    }

    public void addWriter(User writer){
        this.writer = writer;
    }

    public void likeDelete(Like l){
        this.likeList.remove(l);
    }

    public void update(String title, String content, Boolean isPublic){
        this.title = title;
        this.content = content;
        this.isPublic = isPublic;
    }

    public void completeUpdate(Boolean isComplete){
        this.isComplete = isComplete;
    }
}
```

--------------------------------------------------------

## Ajax

```jquery

var main = {
        init : function () {
            var _this = this;
            
            $(document).on('click', '#post-list', function(){
                _this.list();
            });
        },

        list : function(){
            var id = $('#cateId').val();
            var offset = $('#offset').val();

            $.ajax({
                type: 'GET',
                url: '/api/v1/posts/category/'+id+"?offset="+offset,
                dataType: 'json',
                contentType: 'application/json; charset=utf-8'

            }).done(function(data){
            
                // 테이블로 결과 반환
            
                var table = "<h3>전체 게시글</h3><table><thead><tr>";
                
                table += "<th>제목</th><th>작성자</th><th>열람</th></tr><tbody>";

                $.each(data["list"], function(index, post){
                    table += "<tr><td>"+post.title+"</td><td>"+post.writer+
                    "</td><td><a href='#' onclick='goDetail(" + post.id +
                    ")'>열람</a></td></tr>";
                });

                table += "</tbody></thead></table><span>";

                var cnt;
                if(data["count"] % 10 == 0){
                    cnt = data["count"] / 10;
                }else{
                    cnt = parseInt(data["count"] / 10) + 1;
                }

                for(var i = 1; i < cnt + 1; i++){
                    table += "<input type='button' name='page' value='" + i + "'/>";
                }

                table += "</span>";

                $("#allTable").html(table);




            }).fail(function(error){
                alert(JSON.stringify(error));
            });

        }
};

main.init();

```
