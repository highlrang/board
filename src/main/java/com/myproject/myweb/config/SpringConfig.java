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
import com.myproject.myweb.repository.post.PostRepository;
import com.myproject.myweb.repository.user.UserRepository;
import com.myproject.myweb.service.CategoryService;
import com.myproject.myweb.service.LikeService;
import com.myproject.myweb.service.PostService;
import com.myproject.myweb.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /*@Bean // 자동 등록
    public UserService userService(){ return new UserService(userRepository, passwordEncoder()); }
    */

    @Bean
    public CategoryService categoryService(){
        return new CategoryService(categoryRepository);
    }

    @Bean
    public PostService postService(){
        return new PostService(postRepository, categoryRepository, userRepository, likeRepository);
    }

    @Bean
    public LikeService likeService(){
        return new LikeService(postRepository, userRepository, likeRepository);
    }


}