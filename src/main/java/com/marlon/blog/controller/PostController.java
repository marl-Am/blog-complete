package com.marlon.blog.controller;

import com.marlon.blog.constants.Constants;
import com.marlon.blog.entity.Account;
import com.marlon.blog.entity.Post;
import com.marlon.blog.exceptions.ResourceNotFoundException;
import com.marlon.blog.service.PostService;
import com.marlon.blog.service.AccountService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final AccountService accountService;

    public PostController(PostService postService, AccountService accountService) {
        this.postService = postService;
        this.accountService = accountService;
    }

    @GetMapping("/{postId}")
    public String getPost(@PathVariable("postId") Long postId, Model model) {
        Optional<Post> optionalPost = this.postService.getById(postId);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            model.addAttribute("post", post);
            return "post";
        } else {
            return "error/404";
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/new")
    public String getNewPost(Model model, Principal principal) {
        if (principal.getName() == null) {
            return "redirect:/";
        }
        String authUsername = principal.getName();
        Optional<Account> optionalUser = accountService.findOneByEmail(authUsername);
        if (optionalUser.isPresent()) {
            List<String> postTags = new ArrayList<>();
            Post post = new Post();
            post.setAccount(optionalUser.get());
            post.setTags(postTags);

            model.addAttribute("tags", Constants.TAGS);
            model.addAttribute("post", post);
            return "post_new";
        } else {
            return "redirect:/";
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/new")
    public String createNewPost(@ModelAttribute("post") Post post, Principal principal, Model model) {
        if (principal.getName() == null) {
            return "redirect:/";
        }
        if (post.getTitle().isEmpty() || post.getContent().isEmpty() || post.getTags() == null) {
            model.addAttribute("tags", Constants.TAGS);
            model.addAttribute("error", "Please fill in all fields.");
            return "post_new";
        }
        postService.save(post);
        return "redirect:/posts/" + post.getPostId();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{postId}/edit")
    public String updatePost(@PathVariable("postId") Long postId, @ModelAttribute("post") Post post, Model model,
                             Principal principal) {

        if (principal.getName() == null) {
            return "redirect:/";
        }

        if (post.getTitle().isEmpty() || post.getContent().isEmpty() || post.getTags() == null || post.getTags().isEmpty()) {
            model.addAttribute("tags", Constants.TAGS);
            model.addAttribute("error", "Please fill in all fields.");
            return "post_edit";
        }

        Post existingPost = postService.getById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with postId " + postId));
        if (!post.getTitle().equals(existingPost.getTitle())) {
            existingPost.setTitle(post.getTitle());
        }
        if (!post.getContent().equals(existingPost.getContent())) {
            existingPost.setContent(post.getContent());
        }
        if (!post.getTags().equals(existingPost.getTags())) {
            existingPost.setTags(post.getTags());
        }
        postService.save(existingPost);
        return "redirect:/posts/" + existingPost.getPostId();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/{postId}/edit")
    public String getPostForEdit(@PathVariable("postId") Long postId, Model model, Principal principal) {
        if (principal.getName() == null) {
            return "redirect:/";
        }
        Optional<Post> optionalPost = postService.getById(postId);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();

            model.addAttribute("tags", Constants.TAGS);
            model.addAttribute("post", post);
            return "post_edit";
        } else {
            return "error/404";
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/{postId}/delete")
    public String deletePost(@PathVariable("postId") Long postId, Principal principal) {
        if (principal.getName() == null) {
            return "redirect:/";
        }
        Optional<Post> optionalPost = postService.getById(postId);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            postService.delete(post);
            return "redirect:/";
        } else {
            return "error/404";
        }
    }
}
