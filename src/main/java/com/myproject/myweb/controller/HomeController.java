package com.myproject.myweb.controller;

// final & final static

import com.myproject.myweb.service.user.UserService;
import com.myproject.myweb.dto.user.UserRequestDto;
import com.myproject.myweb.dto.user.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RequiredArgsConstructor // 생성자 의존 주입
@Controller
public class HomeController {
    private final UserService userService;
    private final HttpSession session;

    @RequestMapping("/")
    public String home(Model model){
        UserResponseDto user = (UserResponseDto) session.getAttribute("user");

        if(user != null) {
            model.addAttribute("user", user);
        }
        return "home";
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

            if(e.getMessage() == "UserAlreadyExistException"){
                result.rejectValue("email", "userDuplicate", "이미 존재하는 회원 이메일입니다.");
                return "user/register";
            }

            return "user/register";
        }

    }

}
