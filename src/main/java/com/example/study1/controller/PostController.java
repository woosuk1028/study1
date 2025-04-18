package com.example.study1.controller;

import com.example.study1.dto.PostDto;
import com.example.study1.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/posts")
    public String list(Model model) {
        model.addAttribute("posts", postService.getPostList());
        return "posts/list";
    }

    @GetMapping("/posts/write")
    public String writeForm(Model model) {
        model.addAttribute("postDto", new PostDto());
        return "posts/write";
    }

    @PostMapping("/posts/write")
    public String submit(@ModelAttribute PostDto postDto, Authentication auth) {
        String writer = auth.getName();
        postService.savePost(postDto, writer);
        return "redirect:/posts";
    }

    @GetMapping("/posts/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.getPost(id));
        return "posts/detail";
    }

    @PostMapping("/posts/delete/{id}")
    public String delete(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/posts";
    }

}
