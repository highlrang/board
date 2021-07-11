package com.myproject.myweb.controller.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class PostApiControllerTest {

    @Autowired
    TestRestTemplate testRestTemplate; // Servlet context

    @Test
    public void login(){

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("username", "jhw127@naver.com");
        parameters.add("password", "xhxh5314");

        // String parameters = "{\"username\": \"jhw127@naver.com\", \"password\", \"xhxh5314\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity formEntity = new HttpEntity<>(parameters, headers);

        ResponseEntity<String> result = testRestTemplate.postForEntity("/login", formEntity, String.class);
        System.out.println(result.getBody());
    }

    @Test
    public void findAll() {
        // ResponseEntity result = testRestTemplate.getForEntity("/api/v1/posts", class);
        String result = testRestTemplate.getForObject("/api/v1/posts", String.class);

        // login inteceptor에 걸림 >> MockMvc의 WithMockUser
        System.out.println(result);

    }
}
