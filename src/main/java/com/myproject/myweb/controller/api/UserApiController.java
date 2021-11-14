package com.myproject.myweb.controller.api;

import com.myproject.myweb.domain.Post;
import com.myproject.myweb.domain.user.Role;
import com.myproject.myweb.domain.user.User;
import com.myproject.myweb.dto.post.query.PostByLikeCountQueryDto;
import com.myproject.myweb.dto.user.query.WriterByLikeCountQueryDto;
import com.myproject.myweb.exception.ArgumentException;
import com.myproject.myweb.repository.like.LikeRepository;
import com.myproject.myweb.repository.like.query.LikeQueryRepository;
import com.myproject.myweb.repository.like.query.LikeQuerydslRepository;
import com.myproject.myweb.repository.user.UserRepository;
import com.myproject.myweb.repository.user.query.UserQueryRepository;
import com.myproject.myweb.repository.user.query.UserQuerydslRepository;
import com.myproject.myweb.security.JwtTokenProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserQuerydslRepository userQuerydslRepository;
    private final UserRepository userRepository;
    private final LikeQuerydslRepository likeQuerydslRepository;
    private final LikeRepository likeRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public String login(HttpServletResponse response,
                        @RequestBody Map<String, String> userBody) {
        User user = userRepository.findByEmail(userBody.get("email"))
                .orElseThrow(() -> new IllegalArgumentException("UserNotFoundException"));
        if(!passwordEncoder.matches(userBody.get("password"), user.getPassword())){
            throw new IllegalArgumentException("UserNotMatchedException");
        }

        String jwt = jwtTokenProvider.createToken(user.getEmail(), Arrays.asList(user.getRole().getTitle()));
        Cookie cookie = new Cookie("token", jwt);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 7);
        response.addCookie(cookie);

        return "login-success";
    }

    @GetMapping("/api/v1/users")
    public List<UserListDto> usersV1(){
        List<User> users = userRepository.findAll();
        List<UserListDto> result = users.stream()
                .map(UserListDto::new)
                .collect(Collectors.toList());

        Map<Long, Long> likeCounts = getLikeCountsByUserIds(getUserIds(result));
        result.forEach(u -> u.addTotalLike(likeCounts.get(u.getUserId())));

        return result;
    }

    private List<Long> getUserIds(List<UserListDto> result) {
        List<Long> userIds = result.stream().map(UserListDto::getUserId).collect(Collectors.toList());
        return userIds;
    }

    private Map<Long, Long> getLikeCountsByUserIds(List<Long> userIds) {
        Map<Long, Long> likeCounts = likeQuerydslRepository.findAllLikesByPostsWriters(userIds);
        return likeCounts;
    }

    @GetMapping("/api/v1/users/role/{role}") // NORMAL_USER
    public List<UserListDto> usersByRoleV1(@PathVariable(value = "role") String role){

        Role validRole;
        try{
            validRole = Role.valueOf(role);
            List<User> users = userRepository.findByRole(validRole);
            List<UserListDto> result = users.stream()
                    .map(UserListDto::new)
                    .collect(Collectors.toList());

            Map<Long, Long> likeCounts = getLikeCountsByUserIds(getUserIds(result));
            result.forEach(u -> u.addTotalLike(likeCounts.get(u.getUserId())));

            return result;

        }catch(IllegalArgumentException e){
            Map<String, List<String>> messages = new HashMap<>();
            List<String> args = new ArrayList<>();
            args.add(role);
            messages.put("ArgumentNotValidException", args);
            throw new ArgumentException(messages);
        }
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
                    .map(UserPostDto::new)
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
        private String likedPostWriter;
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

    @GetMapping("/api/v1/user")
    public ResponseEntity<String> userByCookie(HttpServletRequest request){
        String token = jwtTokenProvider.resolveToken(request);
        if(!jwtTokenProvider.validateToken(token)) return new ResponseEntity<>("UserTokenFail", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(jwtTokenProvider.getUserPk(token), HttpStatus.OK);
    }

    @GetMapping("/api/v1/users/{id}")
    public UserDto userByIdV1(@PathVariable(value = "id") Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("UserNotFoundException"));
        Long likeCount = likeRepository.countAllByPost_Writer(user);

        UserDto userDto = new UserDto(user);
        userDto.addTotalLike(likeCount);
        return userDto;
    }

    @GetMapping("/api/v1/users/likes/{count}")
    public List<WriterByLikeCountQueryDto> writersByTotalLikeV1(@PathVariable(value="count") long count){
        return userQuerydslRepository.findAllWritersByLikeCount(count);
    }
}
