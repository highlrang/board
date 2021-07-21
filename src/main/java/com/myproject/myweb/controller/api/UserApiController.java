package com.myproject.myweb.controller.api;

import com.myproject.myweb.domain.Post;
import com.myproject.myweb.domain.user.Role;
import com.myproject.myweb.domain.user.User;
import com.myproject.myweb.dto.post.query.PostByLikeCountQueryDto;
import com.myproject.myweb.dto.user.query.WriterByLikeCountQueryDto;
import com.myproject.myweb.repository.like.LikeRepository;
import com.myproject.myweb.repository.like.query.LikeQueryRepository;
import com.myproject.myweb.repository.like.query.LikeQuerydslRepository;
import com.myproject.myweb.repository.user.UserRepository;
import com.myproject.myweb.repository.user.query.UserQueryRepository;
import com.myproject.myweb.repository.user.query.UserQuerydslRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserQuerydslRepository userQuerydslRepository;
    private final UserRepository userRepository;
    private final LikeQuerydslRepository likeQuerydslRepository;
    private final LikeRepository likeRepository;

    @GetMapping("/api/v1/users")
    public List<UserListDto> usersV1(){
        List<User> users = userRepository.findAll();
        List<UserListDto> result = users.stream()
                .map(u -> new UserListDto(u))
                .collect(Collectors.toList());

        Map<Long, Long> likeCounts = getLikeCountsByUserIds(getUserIds(result));
        result.forEach(u -> u.addTotalLike(likeCounts.get(u.getUserId())));

        return result;
        // return userQueryRepository.findAllUsersByDto();
    }

    private List<Long> getUserIds(List<UserListDto> result) {
        List<Long> userIds = result.stream().map(u -> u.getUserId()).collect(Collectors.toList());
        return userIds;
    }

    private Map<Long, Long> getLikeCountsByUserIds(List<Long> userIds) {
        Map<Long, Long> likeCounts = likeQuerydslRepository.findAllLikesByPostsWriters(userIds);
        return likeCounts;
    }

    @GetMapping("/api/v1/users/role/{role}") // ?NORMAL_USER
    public List<UserListDto> usersByRoleV1(@PathVariable(value = "role") String role){

        // Role에 있는지 valid 필요
        Role validRole = Role.valueOf(role);

        List<User> users = userRepository.findByRole(validRole);
        List<UserListDto> result = users.stream()
                .map(u -> new UserListDto(u))
                .collect(Collectors.toList());

        Map<Long, Long> likeCounts = getLikeCountsByUserIds(getUserIds(result));
        result.forEach(u -> u.addTotalLike(likeCounts.get(u.getUserId())));

        return result;

        // return userQueryRepository.findAllUsersByDto(Role.valueOf(role));
    }

    @Getter
    static class UserListDto{
        private Long userId;
        private String userEmail;
        private String userName;
        private String userRole;
        private Long userTotalLike;

        public UserListDto(User u){
            this.userId = u.getId();
            this.userEmail = u.getEmail();
            this.userName = u.getName();
            this.userRole = u.getRole().getTitle();
        }

        public void addTotalLike(Long totalLike){
            this.userTotalLike = totalLike;
        }

    }

    @Getter
    static class UserDto{
        private Long userId;
        private String userEmail;
        private String userName;
        private Long userTotalLike; // user가 작성한 모든 게시글의 좋아요 수
        private List<UserPostDto> userPosts;
        private List<UserLikeDto> userLikes;

        public UserDto(User u){
            this.userId = u.getId();
            this.userEmail = u.getEmail();
            this.userName = u.getName();
            this.userPosts = u.getPostList()
                    .stream()
                    .map(p -> new UserPostDto(p))
                    .collect(Collectors.toList());
            this.userLikes = u.getLikeList()
                    .stream()
                    .map(l -> new UserLikeDto(l.getPost()))
                    .collect(Collectors.toList());
        }

        public void addTotalLike(Long totalLike){
            this.userTotalLike = totalLike;
        }
    }

    @Getter
    static class UserPostDto{

        private Long myPostId;
        private String myPostCategory;
        private String myPostTitle;
        private String myPostContent;

        private Boolean myPostIsPublic;
        private Boolean myPostIsComplete;

        public UserPostDto(Post p){
            this.myPostId = p.getId();
            this.myPostCategory = p.getCategory().getName();
            this.myPostTitle = p.getTitle();
            this.myPostContent = p.getContent();
            this.myPostIsPublic = p.getIsPublic();
            this.myPostIsComplete = p.getIsComplete();
        }

    }

    @Getter
    static class UserLikeDto{
        private Long likedPostId;
        private String likedPostWriter; // name
        private String likedPostCategory;

        private String likedPostTitle;
        private Boolean likedPostComplete;

        public UserLikeDto(Post p){
            this.likedPostId = p.getId();
            this.likedPostWriter = p.getWriter().getName();
            this.likedPostCategory = p.getCategory().getName();
            this.likedPostTitle = p.getTitle();
            this.likedPostComplete = p.getIsComplete();
        }

    }


    @GetMapping("/api/v1/users/{id}")
    public UserDto usersByIdV1(@PathVariable(value = "id") Long id){
        UserDto user = new UserDto(
                userRepository.findById(id)
                        .orElseThrow(IllegalStateException::new)
                // userQueryRepository.findUserById(id)
        );

        // 임시
        Long likeCount = likeRepository.countAllByPost_Writer(userRepository.findById(id).get()).get(); // user로 보내기
        user.addTotalLike(likeCount);

        return user;
    }

    @GetMapping("/api/v1/users/likes/{count}")
    public List<WriterByLikeCountQueryDto> writersByTotalLikeV1(@PathVariable(value="count") long count){
        return userQuerydslRepository.findAllWritersByLikeCount(count);
    }
}
