package com.myproject.myweb;

import com.myproject.myweb.domain.Category;
import com.myproject.myweb.domain.Like;
import com.myproject.myweb.domain.Post;
import com.myproject.myweb.domain.user.Role;
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
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;

import static com.myproject.myweb.domain.QCategory.category;
import static com.myproject.myweb.domain.QLike.like;
import static com.myproject.myweb.domain.QPost.post;
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
    @Autowired EntityManager em;
    @Autowired JPAQueryFactory jpaQueryFactory;

    @Test
    public void querydsl(){
        Category cate = em.createQuery("select c From Category c", Category.class)
                .getResultList().get(0);

        jpaQueryFactory.selectFrom(post)
                .innerJoin(post.category, category)
                .fetchJoin()
                .innerJoin(post.likeList, like)
                .fetchJoin()
                .where(category.id.eq(cate.getId()))
                .groupBy(like.post)
                .having(like.count().goe(5))
                .fetch()
                .size();
    }

    @Test // @Commit
    public void ????????????(){
        Category category = categoryRepository.findByName("Free").get();
        // category.update("free");
    }

    @Test
    public void ?????????insert(){

        String email = "joah@naver.com";
        String name = "??????";
        String password = passwordEncoder.encode("xhxh5314");
        // userRepository.save(User.builder().email(email).name(name).password(password).build());

        email = "spring@naver.com";
        name = "?????????";

        email = "anything@naver.com";
        name = "?????????";

        // 232(joah) 233(spring) 234(anything)
        // 228(127) 10(126-?????????) 9(125-?????????)
    }


    @Test
    public void ?????????insert(){
        // Post post = Post.createPost("title", "content", true, Category, User);
    }

    @Test
    public void ?????????insert(){
        // 140 best??? (joah - ?????????)
        // 141 best??? (spring - ??????)
        // 142 best??? + ??????????????? ???????????? ?????? ?????? (126 - ??????)
        // 143 ?????????????????? + ?????? ?????? ?????? + ????????? ????????? ???????????? (127 - ??????)

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
    public void ????????????????????????????????????(){
        List<PostByLikeCountQueryDto> allPostsByLikeAndNotComplete =
                postQuerydslRepository.findAllPostsByLike(22L, false, -1);
        // 20
        Map<String, List<PostByLikeCountQueryDto>> posts = allPostsByLikeAndNotComplete.stream()
                .sorted(Comparator.comparing(PostByLikeCountQueryDto::getLikeCount))
                .collect(Collectors.groupingBy(PostByLikeCountQueryDto::getCategory));

        for(Map.Entry<String, List<PostByLikeCountQueryDto>> entry: posts.entrySet()){
            for(PostByLikeCountQueryDto p: entry.getValue()) {
                System.out.println("???????????? : " + entry.getKey() +
                        " ?????? : " + p.getTitle() +
                        " ????????? : " + p.getWriter() +
                        " ???????????? : " + p.getLikeCount());
            }
        }
    }
}
