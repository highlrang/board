package com.myproject.myweb.controller.api;

import com.myproject.myweb.dto.CategoryResponseDto;
import com.myproject.myweb.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CategoryApiController {

    private final CategoryService categoryService;

    @GetMapping("/api/v1/category")
    public List<CategoryResponseDto> list(){
        List<CategoryResponseDto> cates = categoryService.findAll();
        log.info("category 개수 = " + cates.size());
        return cates;
    }
}
