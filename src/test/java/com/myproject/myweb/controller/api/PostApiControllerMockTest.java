package com.myproject.myweb.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.myweb.domain.user.User;
import com.myproject.myweb.dto.post.PostRequestDto;
import com.myproject.myweb.repository.like.LikeRepository;
import com.myproject.myweb.repository.post.PostRepository;
import com.myproject.myweb.repository.post.query.PostQueryRepository;
import com.myproject.myweb.repository.user.UserRepository;
import com.myproject.myweb.service.PostService;
import com.myproject.myweb.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpSession;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc // @WebMvcTest(controllers = PostApiController.class)
public class PostApiControllerMockTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired UserRepository userRepository;

    @Test
    @WithMockUser
    public void postApifindAllByCateTest() throws Exception {

        mvc.perform(
                    get("/api/v1/posts/category/2")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print());
    }


    @Test
    @WithMockUser
    public void postApiSaveTest() throws Exception{
        User user = userRepository.findByEmail("jhw127@naver.com").get();
        String title = "title equals checking";
        PostRequestDto post = new PostRequestDto(2L, user.getId(), title, "content", true);
        String content = objectMapper.writeValueAsString(post);

        // when(mockSampleService.getName()).thenReturn("wooody92");

        mvc.perform(
                post("/api/v1/posts")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(title)))
                .andDo(print());

    }

}