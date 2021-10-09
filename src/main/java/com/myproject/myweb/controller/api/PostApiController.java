package com.myproject.myweb.controller.api;

import com.myproject.myweb.domain.Like;
import com.myproject.myweb.domain.Post;
import com.myproject.myweb.dto.post.PostDetailResponseDto;
import com.myproject.myweb.dto.post.PostRequestDto;
import com.myproject.myweb.dto.post.query.admin.PostAdminDto;
import com.myproject.myweb.dto.user.UserResponseDto;
import com.myproject.myweb.dto.post.query.PostByLikeCountQueryDto;
import com.myproject.myweb.repository.post.PostRepository;
import com.myproject.myweb.repository.post.query.PostQuerydslRepository;
import com.myproject.myweb.service.PostService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PostApiController {
    private final PostRepository postRepository;
    private final PostQuerydslRepository postQuerydslRepository;
    private final PostService postService;

    private List<PostListDto> toPostListDtos(List<Post> entity) {
        return entity.stream()
                    .map(PostListDto::new)
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
        List<Post> entity = postQuerydslRepository.findAllPaging(cateId, null, true, offset);
        List<PostListDto> posts = toPostListDtos(entity);

        Long count = postRepository.countByCategory_Id(cateId); // 페이징을 위해 총 게시글 개수

        return new Result(count, posts);
    }

    @GetMapping("/api/v1/posts/category/{cateId}/writer/{writerId}")
    public Result<PostListDto> postsByCategoryAndWriterV1(@PathVariable("cateId") Long cateId,
                                                        @PathVariable("writerId") Long writerId,
                                                        @RequestParam(value = "offset", defaultValue = "0") int offset){
        List<Post> entity = postQuerydslRepository.findAllPaging(cateId, writerId, null, offset);
        List<PostListDto> posts = toPostListDtos(entity);

        Long count = postRepository.countByCategory_IdAndWriter_Id(cateId, writerId);
        return new Result(count, posts);
    }

    @Getter
    // why static A) dto가 너무 많아지고, Inner class하면 각 클래스에서 사용하는 것만 볼 수 있음
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
                    .map(PostLikeDto::new)
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

    @PostMapping("/api/v1/posts")
    public PostDetailResponseDto savePostV1(@RequestBody @Valid PostRequestDto postRequestDto){
        Long id = postService.save(postRequestDto);
        return postService.findById(id);
    }

    @PutMapping("/api/v1/posts/{id}")
    public PostDetailResponseDto updatePostV1(@PathVariable("id") Long id,
                                              @RequestBody @Valid PostRequestDto postRequestDto,
                                              HttpSession session){
        UserResponseDto user = (UserResponseDto) session.getAttribute("user");
        postService.update(id, postRequestDto, user);
        return postService.findById(id);
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public void deletePostV1(@PathVariable Long id, HttpSession session){
        UserResponseDto user = (UserResponseDto) session.getAttribute("user");
        postService.delete(id, user);
    }



    /* ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ */


    @GetMapping("/api/v1/posts/best-likes/category/{cateId}") // 이용자에게 보여질 목록(페이징 처리)
    public Result postsByLikeAndCategoryV1(@PathVariable(value="cateId") Long cateId,
                                           @RequestParam(value="offset", defaultValue = "0") int offset
    ){
        List<PostByLikeCountQueryDto> bestPosts = postQuerydslRepository.findAllPostsByLike(cateId, null, offset);
        Long count = postQuerydslRepository.countBestPosts(cateId);
        return new Result(count, bestPosts);

    }

    @GetMapping("/api/v1/posts/best-likes/category/{cateId}/for-complete") // 관리자에 전달할 전체 목록
    public List<PostByLikeCountQueryDto> postsByLikeAndCompleteV1(@PathVariable(value="cateId") Long cateId){
        return postQuerydslRepository.findAllPostsByLike(cateId, false, -1);
    }

    @GetMapping("/api/v1/posts/admin/matching") // 미사용 api
    public List<PostAdminDto> postAdminMatch(){
        List<PostAdminDto> postAdminDtos = new ArrayList<>();
        try{
            postAdminDtos = postService.postMatchingAdmin();
        }catch(WebClientResponseException e){
            log.error("<에러> 게시글 관리자 불러오지 못함. 원인 -> " + e.getResponseBodyAsString());
        }
        return postAdminDtos;
    }
}
