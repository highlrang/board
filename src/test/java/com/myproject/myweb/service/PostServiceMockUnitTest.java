package com.myproject.myweb.service;




import com.myproject.myweb.domain.Category;
import com.myproject.myweb.domain.Post;
import com.myproject.myweb.domain.user.User;
import com.myproject.myweb.dto.post.PostDetailResponseDto;
import com.myproject.myweb.dto.post.PostRequestDto;
import com.myproject.myweb.dto.post.PostResponseDto;
import com.myproject.myweb.repository.CategoryRepository;
import com.myproject.myweb.repository.like.LikeRepository;
import com.myproject.myweb.repository.post.PostRepository;
import com.myproject.myweb.repository.post.query.PostQueryRepository;
import com.myproject.myweb.repository.user.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // RunWith하면 init 안 해도됨?
public class PostServiceMockUnitTest {

    @Mock private PostRepository postRepository;
    @Mock private PostQueryRepository postQueryRepository;
    @Mock private UserRepository userRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private LikeRepository likeRepository;
    @Mock private RestTemplate restTemplate;
    @InjectMocks private PostService postService;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        postService = new PostService(postRepository, postQueryRepository, categoryRepository, userRepository, likeRepository, restTemplate);
    }

    @Test
    public void findAllTest(){
        // Optional.of()

        List<PostResponseDto> mockList = new ArrayList<>();
        when(postService.findAll()).thenReturn(mockList);
        // doThrow().when().method();

        List<PostResponseDto> posts = postService.findAll();
        // verify(postRepository, atLeastOnce()).findAll();

        assertThat(posts.size()).isEqualTo(0);
    }

}
