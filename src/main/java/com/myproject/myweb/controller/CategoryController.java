package com.myproject.myweb.controller;

import com.myproject.myweb.dto.CategoryResponseDto;
import com.myproject.myweb.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/list")
    public String list(Model model){
        List<CategoryResponseDto> cates = categoryService.findAll();
        model.addAttribute("cates", cates);

        return "category/list";
    }
}
