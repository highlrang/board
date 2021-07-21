package com.myproject.myweb.config;

/*
@Configuration
public class SpringConfig {


    @Bean
    public PostService postService(){
        return new PostService(postRepository());
    }

    @Bean
    public PostRepository postRepository(){
        return new PostRepository(); // ??
    }
}
*/

import com.myproject.myweb.repository.CategoryRepository;
import com.myproject.myweb.repository.like.LikeRepository;
import com.myproject.myweb.repository.like.query.LikeQueryRepository;
import com.myproject.myweb.repository.post.PostRepository;
import com.myproject.myweb.repository.post.query.PostQueryRepository;
import com.myproject.myweb.repository.post.query.PostQuerydslRepository;
import com.myproject.myweb.repository.user.UserRepository;
import com.myproject.myweb.repository.user.query.UserQueryRepository;
import com.myproject.myweb.service.CategoryService;
import com.myproject.myweb.service.LikeService;
import com.myproject.myweb.service.PostService;
import com.myproject.myweb.service.user.UserService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;

@RequiredArgsConstructor
@Configuration
public class SpringConfig{
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    // JpaRepository 인터페이스 상속한 구현체를 자동으로 빈 등록 + 생성자 주입
    /*
    원래라면
    @Bean
    public UserRepository userRepository(){
        return new UserRepository();
    }
    &&
    return new UserService(userRepository());
     */

    private final EntityManager em;

    @Bean
    public JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(em);
    }

    @Bean
    public PostQuerydslRepository postQuerydslRepository(){
        return new PostQuerydslRepository(jpaQueryFactory());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean // 자동등록
    public UserService userService(){ return new UserService(userRepository, passwordEncoder()); }

    @Bean
    public CategoryService categoryService(){
        return new CategoryService(categoryRepository);
    }

    @Bean // 자동 등록 아님?
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public PostService postService(){
        return new PostService(postRepository, postQuerydslRepository(), categoryRepository, userRepository, likeRepository, restTemplate());
    }

    @Bean
    public LikeService likeService(){
        return new LikeService(postRepository, userRepository, likeRepository);
    }


}