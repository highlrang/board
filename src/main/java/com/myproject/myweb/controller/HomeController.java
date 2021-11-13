package com.myproject.myweb.controller;

import com.myproject.myweb.service.UserService;
import com.myproject.myweb.dto.user.UserRequestDto;
import com.myproject.myweb.dto.user.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {
    private final UserService userService;

    @RequestMapping("/")
    public String home(Model model){
        return "home";
    }

    @GetMapping("/register")
    public String registerForm(@ModelAttribute UserRequestDto userRequestDto){
        return "user/register";
    }

    @PostMapping("/register")
    public String register(@Valid UserRequestDto userRequestDto, BindingResult result) {
        // setter로 입력됨

        if(result.hasErrors()){
            // 필드에 에러 있으면 메세지 담김
            return "user/register";
        }

        try{
            userService.join(userRequestDto);
            return "user/registered";


        } catch (IllegalStateException e) {

            if(e.getMessage().equals("UserAlreadyExistException")){
                result.rejectValue("email", "UserAlreadyExistException");
                return "user/register";
            }

            return "user/register";
        }

    }

    @GetMapping("/mypage")
    public String mypage(){
        return "user/mypage";
    }

    @GetMapping("/signout")
    public String signout(HttpSession session){
        UserResponseDto user = (UserResponseDto) session.getAttribute("user");
        userService.signOut(user.getId());
        session.invalidate();
        return "redirect:/";
    }

}
