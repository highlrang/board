package com.myproject.myweb.service;




import com.myproject.myweb.dto.post.PostDetailResponseDto;
import com.myproject.myweb.dto.post.PostRequestDto;
import com.myproject.myweb.dto.post.PostResponseDto;
import com.myproject.myweb.repository.CategoryRepository;
import com.myproject.myweb.repository.like.LikeRepository;
import com.myproject.myweb.repository.post.PostRepository;
import com.myproject.myweb.repository.user.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class) //?
public class PostServiceUnitTest {

    @Mock private PostRepository postRepository;
    @Mock private UserRepository userRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private LikeRepository likeRepository;
    @InjectMocks private PostService postService; // spring context에 등록하려면 MockBean 등록 후 Autowired

    @Test
    public void postCreateTest(){

        Long cate = categoryRepository.findAll().get(0).getId();
        Long user = userRepository.findAll().get(0).getId();

        PostRequestDto post = new PostRequestDto(cate, user, "Mock", "Mockito testing", true);
        Long id = postService.save(post);

        PostDetailResponseDto result = postService.findById(id);

        assertThat("Mock").isEqualTo(result.getTitle());

    }


    /*
    @RunWith(SpringRunner.class)
    @WebMvcTest(controllers = HelloController.class) || @SpringBootTest(webEnvironment = WebEnvironment.MOCK)
    // @AutoConfiureMockMvc
    public class HelloControllerTest {
        @Autowired
        private MockMvc mvc;

        @Test
        public void hello가_리턴된다() throws Exception {
            String hello = "hello";

            mvc.perform(get("/hello"))
                    .andExpect(status().isOk())
                    .andExpect(content().string(hello))
                    .andDo(print());
        }
    }

    @RunWith(SpringRunner.class)
    @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
    @AutoConfigureMockMvc
    public class SampleControllerTest {

        @Autowired
        TestRestTemplate testRestTemplate;

        @MockBean
        SampleService mockSampleService;

        @Test
        public void hello() throws Exception {
            // 테스트 환경에서 mockSampleService.getName()이 호출되면 "wooody92"로 응답한다.
            when(mockSampleService.getName()).thenReturn("wooody92");
            String result = testRestTemplate.getForObject("/hello", String.class);
            assertEquals("hello wooody92", result);
        }
    }
     */

}
