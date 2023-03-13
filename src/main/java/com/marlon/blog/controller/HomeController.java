package com.marlon.blog.controller;

import com.marlon.blog.entity.Post;
import com.marlon.blog.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping({"/", "/home"})
public class HomeController {
    private final PostService postService;
    public HomeController(PostService postService) {
        this.postService = postService;
    }
    @GetMapping
    public String home(Model model) {
        List<Post> posts = postService.findAllByOrderByUpdatedOnDesc();
        model.addAttribute("posts", posts);
        return "home";
    }

}
