package com.myproject.myweb.service;

import com.myproject.myweb.domain.Category;
import com.myproject.myweb.domain.user.User;
import com.myproject.myweb.dto.post.PostDetailResponseDto;
import com.myproject.myweb.repository.CategoryRepository;
import com.myproject.myweb.repository.post.PostRepository;
import com.myproject.myweb.repository.user.UserRepository;
import com.myproject.myweb.dto.post.PostRequestDto;
import com.myproject.myweb.dto.post.PostResponseDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

//@SpringBootTest
//@WebMvcTest
//@DataJpaTest
//@RestClientTest
//@JsonTest

//@AutoConfigureTestDatabase

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional // 또는 AfterEach로 clear하기
public class PostServiceTest {

    // repository nullpointerexception 남 (impl은 에러 안 나는 듯)
    @Autowired PostRepository postRepository;
    @Autowired PostService postService;            // 단위에서는 new로 ??
    @Autowired UserRepository userRepository;
    @Autowired CategoryRepository categoryRepository;

    @Test @Commit
    public void 게시글_저장() {
        String title = "게시글 제목";
        String content = "게시글 내용";
        Boolean isPublic = true;

        User user = userRepository.findByEmail("jhw127@naver.com").get();
        Category category = categoryRepository.findByName("테스트 카테고리").get();


        PostRequestDto postRequestDto = PostRequestDto.builder()
                .categoryId(category.getId())
                .writerId(user.getId())
                .title(title)
                .content(content)
                .isPublic(isPublic)
                .build();

        Long id = postService.save(postRequestDto);

        PostDetailResponseDto postDetailResponseDto = postService.findById(id);

        assertThat(postDetailResponseDto.getTitle()).isEqualTo(title);
        assertThat(postDetailResponseDto.getIsComplete()).isNotEqualTo(isPublic);


    }

    @Test
    public void  게시글_수정() {
        // 기존꺼
        Category category = categoryRepository.findByName("테스트 카테고리").get();
        User writer = userRepository.findByEmail("jhw127@naver.com").get();
        Boolean isPublic = Boolean.TRUE;
        PostRequestDto post = PostRequestDto.builder()
                .categoryId(category.getId())
                .writerId(writer.getId())
                .isPublic(isPublic)
                .build();
        // 이후에 title 필수 인자로 지정하기
        Long id = postService.save(post);



        // 수정
        Boolean isPublic2 = !isPublic;
        PostRequestDto dto = PostRequestDto.builder()
                .title("수정")
                .content("수정")
                .isPublic(isPublic2)
                .build();

        postService.update(id, dto);


        // then
        PostDetailResponseDto result = postService.findById(id);
        assertThat(result.getIsPublic()).isEqualTo(isPublic2);



    }


    @Test
    public void 게시글_삭제() { // 삭제 연관관계 메서드

        PostResponseDto postResponseDto = postService.findAll().get(0);
        Long categoryId = postResponseDto.getCategoryId();
        Category category = categoryRepository.findById(categoryId).get();
        Long userId = postResponseDto.getWriterId();
        User writer = userRepository.findById(userId).get();
        int writerSize = writer.getPostList().size();
        int postSize = category.getPostList().size();
        Long id = postResponseDto.getId();
        postService.delete(id);


        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> postService.findById(id));

        assertThat(writerSize).isEqualTo(writer.getPostList().size()+1);
        assertThat(postSize).isEqualTo(category.getPostList().size()+1);

        assertThat(e.getMessage()).isEqualTo("PostNotFoundException");
    }

    @Test
    public void 게시글_완료여부_수정() {

        PostResponseDto post = postService.findAll().get(0);
        Long id = post.getId();
        Boolean isComplete = post.getIsComplete();

        postService.postCompleteUpdate(id);

        Boolean result = postService.findById(id).getIsComplete();

        assertThat(result).isEqualTo(!isComplete);
    }


}