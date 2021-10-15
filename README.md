<br/>
## 게시판 프로젝트
<br/>

#### 프로젝트 설명

일반적인 게시판 프로젝트로 게시글 등록, 수정, 삭제가 가능하고,
좋아요 기능을 통해 일정 수의 좋아요를 받은 게시글은 베스트글이 되고
베스트글을 일정 수준 소유한 사용자는 회원 등급을 상승시키는 구조의 프로젝트입니다.


-----------------------------------------------------------
<br/>

#### JPA, Querydsl을 활용한 동적 쿼리 활용 및 쿼리 최적화

+ Querydsl
```java
public Long countBestPosts(Long cateId){
    return (long) jpaQueryFactory.selectFrom(post)
            .innerJoin(post.category, category)
            .innerJoin(post.likeList, like)
            .where(eqCategoryId(cateId))
            .groupBy(like.post)
            .having(like.count().goe(5))
            .fetch()
            .size();
}

private BooleanExpression eqCategoryId(Long cateId){
    if(cateId == null) return null;
    return category.id.eq(cateId);
}
```

-----------------------------------------------------------
<br/>

#### REST API


+ Rest Controller
```java
@GetMapping("/api/v1/posts/category/{cateId}/writer/{writerId}")
public Result<PostListDto> postsByCategoryAndWriterV1(@PathVariable("cateId") Long cateId,
                                                      @PathVariable("writerId") Long writerId,
                                                      @RequestParam(value = "offset", defaultValue = "0") int offset){
    List<Post> entity = postQuerydslRepository.findAllPaging(cateId, writerId, null, offset);
    List<PostListDto> posts = toPostListDtos(entity);

    Long count = postRepository.countByCategory_IdAndWriter_Id(cateId, writerId);
    return new Result(count, posts);
}
```
<br/>

+ javascript
```java

function(){
    var cateId=$('#cateId').val();
    var userId=$('#userId').val();
    var offset=$('#offset').val();

    $.ajax({
        type:'GET',
        url:'/api/v1/posts/category/'+cateId+'/writer/'+userId+"?offset="+offset,
        dataType:'json'

    }).done(function(data){
        console.log(data);
        listAfter(data);

    }).fail(function(request, error){
        console.log(error);
        window.location.href='/error';
    });
}
```

-----------------------------------------------------------
<br/>

#### Spring Batch와 Scheduler를 이용한 일괄 작업 자동화

+ Spring Batch (JpaPagingItemReader)
```java
@Bean
public Job jpaPagingJob(){
    return jobBuilderFactory.get(JOB_NAME)
        .start(jpaPagingStep())
        .build();
}

@Bean
public Step jpaPagingStep() {
    return stepBuilderFactory.get("jpaPagingItemReaderStep")
            .<User, User>chunk(chunkSize)
            .reader(itemReader())
            .processor(itemProcessor())
            .writer(itemWriter())
            .build();
}

@Bean
public JpaPagingItemReader<User> itemReader() {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("role", Role.NORMAL_USER);
    return new JpaPagingItemReaderBuilder<User>()
            .name("jpaCursorItemReader")
            .entityManagerFactory(entityManagerFactory)
            .queryString("select w" +
                    " from Like l" +
                    " join l.post p" +
                    " join p.writer w" +
                    " where w.role =:role" +
                    " group by l.post.writer" +
                    " having count(l.post.writer) >= 100" +
                    " order by count(l.post.writer) desc")
            .parameterValues(parameters)
            .build();
}

@Bean
public ItemProcessor<User, User> itemProcessor() {
    return user -> {
        user.roleUpdate(Role.SILVAL_USER);
        return user;
    };
}

@Bean
public JpaItemWriter<User> itemWriter() {
    JpaItemWriter<User> jpaItemWriter = new JpaItemWriter<>();
    jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
    return jpaItemWriter;
}
```


-----------------------------------------------------------
<br/>

#### 통합 테스트

```java
@Test
public void  게시글_수정() {
    // given
    Category category = categoryRepository.findByName("Free").get();
    User writer = userRepository.findByEmail("jhw127@naver.com").get();
    Boolean isPublic = Boolean.TRUE;
    PostRequestDto post = PostRequestDto.builder()
                                        .categoryId(category.getId())
                                        .writerId(writer.getId())
                                        .title("게시글 수정 전")
                                        .isPublic(isPublic)
                                        .build();
    Long id = postService.save(post);

    // given
    Boolean isPublic2 = false;
    PostRequestDto dto = PostRequestDto.builder()
                                       .title("수정 후")
                                       .isPublic(isPublic2)
                                       .build();
    postService.update(id, dto, new UserResponseDto(writer));

    // when
    PostDetailResponseDto result = postService.findById(id);
    
    // then 
    assertThat(result.getIsPublic()).isEqualTo(isPublic2);
}
```
<br/><br/>


