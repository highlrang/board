package com.myproject.myweb.controller.api;

import com.myproject.myweb.domain.Like;
import com.myproject.myweb.domain.Post;
import com.myproject.myweb.domain.user.User;
import com.myproject.myweb.dto.like.LikeRequestDto;
import com.myproject.myweb.dto.like.LikeResponseDto;
import com.myproject.myweb.repository.like.LikeRepository;
import com.myproject.myweb.repository.like.query.LikeQueryRepository;
import com.myproject.myweb.service.LikeService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class LikeApiController {

    private final LikeRepository likeRepository;
    private final LikeQueryRepository likeQueryRepository;
    private final LikeService likeService;

    @Data
    @AllArgsConstructor
    static class Result<T>{
        private String title;
        private T data;
    }

    // save는 return 값 있지만 push는 없게
    @PostMapping("/api/v1/likes")
    public LikeResponseDto pushV1(@RequestBody LikeRequestDto likeRequestDto){
        likeService.push(likeRequestDto);
        return new LikeResponseDto(); // 대충 빈객체 return
    }

    // 특정 게시글에 좋아요 한 사용자들 리스트
    @GetMapping("/api/v1/likes/post/{postId}")
    public Result<LikeDtoByPost> likeUsersByPostIdV1(@PathVariable(value="postId") Long postId){
        // likeQueryRepository.findAllLikesByPost(postId)
        List<Like> entity = likeRepository.findAllByPost_Id(postId);

        List<LikeDtoByPost> result = entity.stream()
                .map(e -> new LikeDtoByPost(e.getUser()))
                .collect(Collectors.toList());

        return new Result(entity.get(0).getPost().getTitle(), result);
    }


    // 특정 사용자가 좋아요한 게시글 리스트
    @GetMapping("/api/v1/likes/user/{userId}")
    public Result<LikeDtoByUser> likePostsByUserIdV1(@PathVariable(value="userId") Long userId){
        List<Like> entity = likeRepository.findAllByUser_Id(userId);

        List<LikeDtoByUser> result = entity.stream()
                .map(e -> new LikeDtoByUser(e.getPost()))
                .collect(Collectors.toList());

        List<Long> postIds = result.stream().map(p -> p.getPostId()).collect(Collectors.toList());
        Map<Long, Long> likeCount = likeQueryRepository.findAllLikesByPostsIds(postIds); // native query
        result.forEach(r -> r.addTotalLike(likeCount.get(r.getPostId())));

        return new Result(entity.get(0).getUser().getName(), result);
    }

    @Getter
    static class LikeDtoByUser{

        private Long postId;
        private String postCategory;
        private String writerEmail;
        private String writerName;
        private String postTitle;
        private String postContent;
        private Boolean postIsComplete;
        private Long postTotalLike;

        public LikeDtoByUser(Post p) {
            this.postId = p.getId();
            this.postCategory = p.getCategory().getName();
            this.writerEmail = p.getWriter().getEmail();
            this.writerName = p.getWriter().getName();
            this.postTitle = p.getTitle();
            this.postContent = p.getContent();
            this.postIsComplete = p.getIsComplete();
        }

        public void addTotalLike(Long totalLike){
            this.postTotalLike = totalLike;
        }

    }

    @Getter
    static class LikeDtoByPost{
        private Long userId;
        private Long userEmail;
        private String userName;
        private String userRole;

        public LikeDtoByPost(User u) {
            this.userId = u.getId();
            this.userEmail = u.getId();
            this.userName = u.getName();
            this.userRole = u.getRole().getTitle();

        }

    }

}
