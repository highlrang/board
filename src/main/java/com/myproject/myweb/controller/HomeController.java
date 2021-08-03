package com.myproject.myweb.controller;

import com.myproject.myweb.domain.Category;
import com.myproject.myweb.dto.CategoryResponseDto;
import com.myproject.myweb.service.CategoryService;
import com.myproject.myweb.service.user.UserService;
import com.myproject.myweb.dto.user.UserRequestDto;
import com.myproject.myweb.dto.user.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {
    private final UserService userService;
    private final CategoryService categoryService;
    private final HttpSession session;

    @RequestMapping("/")
    public String home(Model model){

        UserResponseDto user = (UserResponseDto) session.getAttribute("user");
        if(user != null) {
            model.addAttribute("user", user);
        }

        return "layout/basic";
    }

    @GetMapping("/login")
    public String loginForm(){
        return "user/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/register")
    public String registerForm(@ModelAttribute UserRequestDto userRequestDto){
        return "user/register";
    }

    @PostMapping("/register") // 커맨드객체 @RequestParam or @ModelAttribute - 생략 가능
    public String register(@Valid UserRequestDto user, BindingResult result) {

        if(result.hasErrors()){
            // email 필드에 에러 있으면 메세지 담김
            return "user/register";
        }

        try{
            userService.join(user);
            return "user/registered";


        } catch (IllegalArgumentException e) {

            if(e.getMessage().equals("UserAlreadyExistException")){
                result.rejectValue("email", "userDuplicate", "이미 존재하는 회원 이메일입니다.");
                return "user/register";
            }

            return "user/register";
        }

    }

    @GetMapping("/myPage/{userId}")
    public String myPage(@PathVariable("userId") Long userId){
        // userId
        return "mypage";
    }

}
