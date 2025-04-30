package com.example.study1.controller;

import com.example.study1.dto.PostDto;
import com.example.study1.entity.Post;
import com.example.study1.service.PostService;
import com.example.study1.service.ReservationService;
import com.example.study1.util.ReservationLockManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final ReservationService reservationService;
    private final ReservationLockManager reservationLockManager;

    @GetMapping("/posts")
    public String list(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Post> postPage = postService.getPagedPostList(page);
        model.addAttribute("postPage", postPage);
//        model.addAttribute("posts", postService.getPostList());
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

    @GetMapping("/posts/reserve-fail")
    public String reserveFail(@PathVariable Long id, Model model) {
        return "posts/reserve-fail";
    }

    @GetMapping("/posts/reserve/{id}")
    public String reserve(@PathVariable Long id, Model model, Authentication auth) {
        String username = auth.getName();

        if(!reservationLockManager.tryLock(id, username)) {
            model.addAttribute("message", "이미 다른 사용자가 예매중입니다.");
            return "posts/reserve-fail";
        }

        try {
            Post post = postService.getPostWithLock(id);

            if(post.isReserved()) {
                model.addAttribute("message", "이미 예매된 게시글입니다.");
                return "posts/reserve-fail";
            }

            model.addAttribute("post", post);
            return "posts/reserve";
        } catch (Exception e) {
            model.addAttribute("message", "현재 다른 사용자가 예매 중입니다.");
            return "posts/reserve-fail";
        }

//        model.addAttribute("post", postService.getPost(id));
//        return "posts/reserve";
    }

    @PostMapping("/posts/reserve/unlock/{id}")
    public String unlock(@PathVariable Long id, Authentication auth) {
        String username = auth.getName();
        reservationLockManager.unlock(id, username);
        return "redirect:/posts";
    }
}
