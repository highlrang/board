package com.myproject.myweb.controller.api;

import com.myproject.myweb.domain.Like;
import com.myproject.myweb.domain.Post;
import com.myproject.myweb.dto.post.PostDetailResponseDto;
import com.myproject.myweb.dto.post.PostRequestDto;
import com.myproject.myweb.dto.post.query.PostQueryDto;
import com.myproject.myweb.dto.post.query.admin.PostAdminMatchDto;
import com.myproject.myweb.dto.user.UserResponseDto;
import com.myproject.myweb.repository.like.LikeRepository;
import com.myproject.myweb.dto.post.query.PostByLikeCountQueryDto;
import com.myproject.myweb.repository.like.query.LikeQueryRepository;
import com.myproject.myweb.repository.post.PostRepository;
import com.myproject.myweb.repository.post.query.PostQueryRepository;
import com.myproject.myweb.service.PostService;
import com.myproject.myweb.dto.post.PostResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class PostApiController {

    // service는 basic한 비즈니스 로직이니,
    // 기존의 repository 또는 api스펙에 맞게 별도의 repository에 메서드 추가하여 불러와야함
    // toMany가 아니라면 fetch join과 dto로 받는 것만으로 완료됨

    private final PostRepository postRepository;
    private final PostQueryRepository postQueryRepository;
    private final PostService postService;
    private final LikeRepository likeRepository;
    private HttpSession session;

    @GetMapping("/api/v1/posts")
    // 카테고리 상관없이 모든 게시글
    public List<PostListDto> postsV1(){
        List<Post> entity = postRepository.findAllFetch();

        List<PostListDto> posts = toPostListDtos(entity);

        /* 리스트에서는 상세정보 필요없음, 사용한다면 method분리해서 사용
        List<Long> postIds = posts.stream()
                .map(p -> p.getId())
                .collect(Collectors.toList());
        ap<Long, Long> likeCount = likeQueryRepository.findAllLikesByPostsIds(postIds); // in-queryM
        posts.forEach(p -> p.addTotalLike(likeCount.get(p.getId())));
         */

        return posts;

        // toMany 관계는 따로 v5 방식(dto, 1+1(+1))으로 적용해보기
        // (v3은 fetch join + distinct)
        // 최종적으로는 v3.1(@batch fetch size) 방식으로 적용하고 모두 포폴에 넣기

    }

    private List<PostListDto> toPostListDtos(List<Post> entity) {
        return entity.stream()
                    .map(e -> new PostListDto(e))
                    .collect(Collectors.toList());
    }

    @Getter
    @AllArgsConstructor
    static class Result<T>{
        private Long count;
        private T list;
    }

    @GetMapping("/api/v1/posts/category/{cateId}")
    public Result<PostListDto> postsByCategoryV1(@PathVariable("cateId") Long cateId,
                                               @RequestParam(value = "offset", defaultValue = "0") int offset){
        List<Post> entity = postQueryRepository.findAllWithCategoryAndPublicAndPagingByFetch(cateId, offset);
        List<PostListDto> posts = toPostListDtos(entity);

        Long count = postRepository.countByCategory_Id(cateId);

        return new Result(count, posts);
    }

    @GetMapping("/api/v1/posts/category/{cateId}/writer/{writerId}")
    public List<PostListDto> postsByCategoryAndWriterV1(@PathVariable("cateId") Long cateId,
                                                    @PathVariable("writerId") Long writerId){
        List<Post> entity = postRepository.findAllByCategory_IdAndWriter_Id(cateId, writerId);
        List<PostListDto> posts = toPostListDtos(entity);

        return posts;
    }

    @GetMapping("/api/v1/posts/writer/{writerId}")
    // 카테고리 상관없이 자신의 전체 게시글 모음
    public List<PostListDto> PostsByWriterV1(@PathVariable("writerId") Long writerId){

        UserResponseDto user = (UserResponseDto)session.getAttribute("user");
        if(user.getId() != writerId){
            // 요청자 작성자 일치 확인
        }

        List<Post> entity = postRepository.findByWriterFetch(writerId);
        List<PostListDto> posts = toPostListDtos(entity);

        return posts;
    }

    @Getter
    // why static?? A) dto가 너무 많아지고, Inner class하면 각 클래스에서 사용하는 것만 볼 수 있음
    // "어떤 메소드가 인스턴스가 생성되지 않았더라도, 호출 할 것인가?"  => "그렇다." == static(basic한)
    static class PostListDto{
        private Long postCnt;
        private Long id;
        private String title;
        private Boolean isComplete;
        private String category;
        private String writer;

        public PostListDto(Post p) {
            this.id = p.getId();
            this.title = p.getTitle();
            this.isComplete = p.getIsComplete();
            this.category = p.getCategory().getName();
            this.writer = p.getWriter().getName();
        }

        public void addPostCnt(Long cnt){
            this.postCnt = postCnt;
        }
    }

    @Getter
    static class PostDto{
        private Long id;
        private String title;
        private String content;
        private Boolean isComplete;
        private Boolean isPublic;
        private Long categoryId;
        private Long writerId;
        private String categoryName;
        private String writerName;
        private Long totalLike;
        private List<PostLikeDto> likes;

        public PostDto(Post p){
            this.id = p.getId();
            this.title = p.getTitle();
            this.content = p.getContent();
            this.isComplete = p.getIsComplete();
            this.isPublic = p.getIsPublic();
            this.categoryId = p.getCategory().getId();
            this.categoryName = p.getCategory().getName();
            this.writerId = p.getWriter().getId();
            this.writerName = p.getWriter().getName();

            this.likes = p.getLikeList()
                    .stream()
                        .map(like -> new PostLikeDto(like))
                    .collect(Collectors.toList());
        }

        public void addTotalLike(Long totalLike){
            this.totalLike = totalLike;
        }

    }

    @Getter
    static class PostLikeDto{
        private Long userId;
        private String userName;

        public PostLikeDto(Like l){
            this.userId = l.getUser().getId();
            this.userName = l.getUser().getName();
        }
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostDto postDetailV1(@PathVariable(value="id") Long id){
        // fetch join 안 한 likes만 stream()으로 lazy 초기화 필요 // 데이터 하나니까 1 + n 걱정 안하기

        Post entity = postRepository.findByIdFetch(id)
                .orElseThrow(() -> new IllegalStateException());

        PostDto post = new PostDto(entity); // repository에서 해야 영속성 유지되는 거 아님??!
        post.addTotalLike(likeRepository.countAllByPost_Id(post.getId()).get());

        return post;
    }

    @PostMapping("/api/v1/posts")
    public PostDetailResponseDto savePostV1(@RequestBody @Valid PostRequestDto postRequestDto){
        Long id = postService.save(postRequestDto);
        return postService.findById(id);
    }

    @PutMapping("/api/v1/posts/{id}")
    public PostDetailResponseDto updatePostV1(@PathVariable("id") Long id,
                                        @RequestBody @Valid PostRequestDto postRequestDto){
        postService.update(id, postRequestDto); // findById(영속성 컨텍스트)는 service에서 다룸

        PostDetailResponseDto postDetailResponseDto = postService.findById(id);
        postDetailResponseDto.addTotalLike(likeRepository.countAllByPost_Id(postDetailResponseDto.getId()).get());
        return postDetailResponseDto;
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public void deletePostV1(@PathVariable Long id){
        postService.delete(id);
    }

    @GetMapping("/api/v1/posts/likes/category/{cateId}")
    public List<PostByLikeCountQueryDto> postsByLikeAndCategoryV1(@PathVariable(value="cateId") Long cateId){
        return postQueryRepository.findAllPostsByLikeAndCategory(cateId);
    }

    @GetMapping("/api/v1/posts/uncomplete/likes") // 20
    public List<PostByLikeCountQueryDto> postsByLikeAndCompleteV1(@RequestParam(value="count", defaultValue = "5") Long count){
        return postQueryRepository.findAllPostsByLikeAndComplete(count);
    }

    @GetMapping("/api/v1/posts/admin/matching")
    public List<PostAdminMatchDto> postAdminMatch(){
        return postService.postMatchingAdmin();
    }
}
