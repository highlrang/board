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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final HttpSession session;

    @GetMapping("/create")
    public String postCreateForm(@RequestParam("cateId") Long cateId,
                                 @ModelAttribute PostRequestDto postRequestDto, // 자동으로 넘어감
                                 Model model,
                                 @AuthenticationPrincipal User user){

        UserResponseDto sessionUser = (UserResponseDto)session.getAttribute("user");
        model.addAttribute("cateId", cateId);
        model.addAttribute("userId", sessionUser.getId());

        return "post/create";
    }

    @PostMapping("/create")
    public String postCreate(PostRequestDto postRequestDto){
        Long id = postService.save(postRequestDto);
        return "redirect:/detail/" + id; // redirect 에 + id해서 보내기
    }

    @GetMapping("/detail/{id}") // ?key=value는 RequestParam
    public String postDetail(@PathVariable Long id, Model model){
        PostDetailResponseDto post = postService.findById(id);
        model.addAttribute("post", post);

        UserResponseDto user = (UserResponseDto) session.getAttribute("user");
        model.addAttribute("userId", user.getId());

        Boolean alreadyLiked = post.getLikes()
                .stream().anyMatch(l -> l.getUserId().equals(user.getId()));
                // .filter(l -> l.getUserId() == user.getId())
                // .map(l -> l.getUserId() == user.getId() ? true : false);
        model.addAttribute("alreadyLiked", alreadyLiked);

        return "post/detail";
    }

    @GetMapping("/detail/mine/{id}") // ?key=value는 RequestParam
    public String postDetailMine(@PathVariable Long id, Model model){
        PostDetailResponseDto post = postService.findById(id);
        model.addAttribute("post", post);
        return "post/detailones";

    }

    @GetMapping("/list")
    public String postList(@RequestParam("cateId") Long cateId, Model model){
        model.addAttribute("cateId", cateId);
        postService.findAllByCategory(cateId);

        UserResponseDto user = (UserResponseDto)session.getAttribute("user");
        model.addAttribute("userId", user.getId());
        return "post/list";
    }

    @GetMapping("/list/mine")
    public String myPostList(@RequestParam("cateId") Long cateId, Model model){
        User writer = (User)session.getAttribute("user");
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
