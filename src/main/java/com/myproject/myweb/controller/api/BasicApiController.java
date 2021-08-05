package com.myproject.myweb.controller.api;

import com.myproject.myweb.dto.CategoryResponseDto;
import com.myproject.myweb.dto.user.UserResponseDto;
import com.myproject.myweb.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BasicApiController {

    private final CategoryService categoryService;

    @GetMapping("/api/v1/category")
    public List<CategoryResponseDto> list(){
        List<CategoryResponseDto> cates = categoryService.findAll();
        return cates;
    }

    @AllArgsConstructor
    @Getter
    static class Result{
        private List<CategoryResponseDto> cateList;
        private UserResponseDto user;
    }

    /*
    @AllArgsConstructor
    @Getter
    static class Results<T>{
        private List<T> objectList;
        private T oneObject;
    }
     */
}
