package com.myproject.myweb;

import com.myproject.myweb.domain.Category;
import com.myproject.myweb.domain.Like;
import com.myproject.myweb.domain.Post;
import com.myproject.myweb.domain.user.User;
import com.myproject.myweb.dto.post.query.PostByLikeCountQueryDto;
import com.myproject.myweb.dto.user.UserRequestDto;
import com.myproject.myweb.repository.CategoryRepository;
import com.myproject.myweb.repository.like.LikeRepository;
import com.myproject.myweb.repository.post.PostRepository;
import com.myproject.myweb.repository.post.query.PostQueryRepository;
import com.myproject.myweb.repository.post.query.PostQuerydslRepository;
import com.myproject.myweb.repository.user.UserRepository;
import com.myproject.myweb.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class DataInsertTest {
    @Autowired CategoryRepository categoryRepository;
    @Autowired BCryptPasswordEncoder passwordEncoder;
    @Autowired UserRepository userRepository;
    @Autowired PostRepository postRepository;
    @Autowired PostQuerydslRepository postQuerydslRepository;
    @Autowired LikeRepository likeRepository;

    @Test // @Commit
    public void 카테고리(){
        Category category = categoryRepository.findByName("Free").get();
        // category.update("free");
    }

    @Test
    public void 사용자insert(){

        String email = "joah@naver.com";
        String name = "조아";
        String password = passwordEncoder.encode("xhxh5314");
        // userRepository.save(User.builder().email(email).name(name).password(password).build());

        email = "spring@naver.com";
        name = "봄봄봄";

        email = "anything@naver.com";
        name = "아무개";

        // 232(joah) 233(spring) 234(anything)
        // 228(127) 10(126-정사원) 9(125-김이름)
    }


    @Test
    public void 게시글insert(){
        // Post post = Post.createPost("title", "content", true, Category, User);
    }

    @Test
    public void 좋아요insert(){
        // 140 best글 (joah - 고민글)
        // 141 best글 (spring - 공부)
        // 142 best글 + 상세페이지 좋아요와 채택 캡쳐 (126 - 식물)
        // 143 비공개게시글 + 수정 화면 캡쳐 + 미채택 미공개 상세캡쳐 (127 - 일기)

        Post post = postRepository.findById(139l).get();
        List<Like> likes = new ArrayList<>();

        // User user = userRepository.findByEmail("anything@naver.com").get();
        // likes.add(Like.builder().post(post).user(user).build());

        User user = userRepository.findByEmail("joah@naver.com").get();
        likes.add(Like.builder().post(post).user(user).build());

        user = userRepository.findByEmail("spring@naver.com").get();
        likes.add(Like.builder().post(post).user(user).build());

        user = userRepository.findByEmail("jhw127@naver.com").get();
        likes.add(Like.builder().post(post).user(user).build());

        user = userRepository.findByEmail("jhw126@naver.com").get();
        likes.add(Like.builder().post(post).user(user).build());

        user = userRepository.findByEmail("jhw125@naver.com").get();
        likes.add(Like.builder().post(post).user(user).build());

        likeRepository.saveAll(likes);
    }

    @Test
    public void 카테고리정렬미해결게시글(){
        List<PostByLikeCountQueryDto> allPostsByLikeAndNotComplete =
                postQuerydslRepository.findAllPostsByLike(22L, false, -1);
        // 20
        Map<String, List<PostByLikeCountQueryDto>> posts = allPostsByLikeAndNotComplete.stream()
                .sorted(Comparator.comparing(PostByLikeCountQueryDto::getLikeCount))
                .collect(Collectors.groupingBy(PostByLikeCountQueryDto::getCategory));

        for(Map.Entry<String, List<PostByLikeCountQueryDto>> entry: posts.entrySet()){
            for(PostByLikeCountQueryDto p: entry.getValue()) {
                System.out.println("카테고리 : " + entry.getKey() +
                        " 제목 : " + p.getTitle() +
                        " 작성자 : " + p.getWriter() +
                        " 좋아요수 : " + p.getLikeCount());
            }
        }
    }
}
