package com.myproject.myweb.controller;


import com.myproject.myweb.domain.user.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.Contains;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class PostContollerMockTest {

    @Autowired MockMvc mockMvc;

    @Test
    @WithMockUser(username = "jhw127@naver.com", roles = "NORMAL") // @WithAnonymousUser
    public void findAll() throws Exception{

        mockMvc.perform(
                get("/post/detail/mine/" + 31)
                // .param("id", "29")
        )
                .andDo(print())
                .andExpect(status().isOk());
                // .andExpect(content().string(contains("")));



    }


}
