package com.myproject.myweb.controller;


import com.myproject.myweb.domain.Post;
import com.myproject.myweb.domain.user.User;
import com.myproject.myweb.dto.like.LikeResponseDto;
import com.myproject.myweb.dto.post.PostDetailResponseDto;
import com.myproject.myweb.dto.post.PostRequestDto;
import com.myproject.myweb.dto.user.UserResponseDto;
import com.myproject.myweb.service.PostService;
import com.myproject.myweb.dto.post.PostResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final HttpSession session;

    @GetMapping("/create")
    public String postCreateForm(@RequestParam("cateId") Long cateId,
                                 @ModelAttribute PostRequestDto postRequestDto, // 자동으로 넘어감
                                 Model model){
        model.addAttribute("cateId", cateId);
        return "post/create";
    }

    @PostMapping("/create")
    public String postCreate(PostRequestDto postRequestDto){
        Long id = postService.save(postRequestDto);
        return "redirect:/detail/" + id; // redirect 에 + id해서 보내기
    }

    @GetMapping("/detail/{id}") // ?key=value는 RequestParam
    public String postDetail(@PathVariable Long id, Model model) { // @AuthenticationPrincipal userDetailService랑 같이
        PostDetailResponseDto post = postService.findById(id);
        model.addAttribute("post", post);

        UserResponseDto user = (UserResponseDto) session.getAttribute("user");
        if (post.getWriterId().equals(user.getId())) {
            model.addAttribute("myPost", true);
        }else {
            Boolean alreadyLiked = post.getLikes()
                    .stream().anyMatch(l -> l.getUserId().equals(user.getId()));
            model.addAttribute("alreadyLiked", alreadyLiked);
        }
        return "post/detail";
    }

    @GetMapping("/list")
    public String postList(@RequestParam("cateId") Long cateId, Model model){
        model.addAttribute("cateId", cateId); // 후에 API로
        return "post/list";
    }

    @GetMapping("/list/mine") // 없애도 됨
    public String myPostList(@RequestParam("cateId") Long cateId, Model model){
        User writer = (User)session.getAttribute("user"); // authenticationPrincipal과 session의 차이?
        List<PostResponseDto> posts = postService.findAllMine(cateId, writer.getId());
        model.addAttribute("posts", posts);
        return "post/list";
    }

    @GetMapping("/update/{id}")
    public String postUpdateForm(@PathVariable Long id, Model model){
        PostDetailResponseDto post = postService.findById(id);
        model.addAttribute("post", post);
        return "post/update";
    }

    @PostMapping("/update/{id}")
    public String postUpdate(@RequestParam Long id, PostRequestDto postRequestDto){
        postService.update(id, postRequestDto);
        return "redirect:/detail/" + id;
    }

    @PostMapping("/delete")
    public String postDelete(@RequestParam Long id){
        postService.delete(id);
        return "redirect:/";
    }
}
