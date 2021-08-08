package com.myproject.myweb.controller;

import com.myproject.myweb.service.user.UserService;
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
    private final MessageSource messageSource;

    @RequestMapping("/")
    public String home(Model model){
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
                result.rejectValue("email", "userDuplicate", "이미 존재하는 회원 이메일입니다.");
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

    @GetMapping("/errored")
    public String error(@RequestParam("status") String status,
                        @RequestParam("location") String location,
                        Model model){
        String error = "";

        if(status.equals("404")) {
            return "error/4xx";
        }else if(status.equals("500")){
            return "error/5xx";

        }else if(status.equals("parsererror")) {
            error = "데이터를 알맞은 형태로 가공하는 데에 어려움이 있습니다.";
        }else if(status.equals("timeout")){
            error = "시간이 초과되었습니다. 응답을 기다리는 것에 무리가 있습니다.";

        }else{
            log.info("unknown error = " + status);
        }

        String locationMessage = messageSource.getMessage(location, null, null);
        //  new String[]{"arg1"},  Locale.KOREA
        log.info("error = " + error + " location = " + locationMessage);

        model.addAttribute("error", error);
        model.addAttribute("location", locationMessage);
        return "error/error";
    }

}
