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
            model.addAttribute("post", optionalPost.get());
            return "post";
        } else {
            return "error/404";
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/new")
    public String getNewPost(Model model, Principal principal) {
        if (principal == null || principal.getName() == null) {
            return "redirect:/";
        }
        Optional<Account> optionalAccount = accountService.findOneByEmail(principal.getName());
        if (optionalAccount.isEmpty()) {
            return "redirect:/";
        }

        List<String> postTags = new ArrayList<>();
        Post post = new Post();
        post.setAccount(optionalAccount.get());
        post.setTags(postTags);

        model.addAttribute("tags", Constants.TAGS);
        model.addAttribute("post", post);
        return "post_new";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/new")
    public String createNewPost(@ModelAttribute("post") Post post, Principal principal, Model model) {
        if (principal == null || principal.getName() == null) {
            return "redirect:/";
        }

        // The account is always derived from the authenticated session, never
        // trusted from client-submitted form data. This is what was missing
        // and caused the null account_id error.
        Optional<Account> optionalAccount = accountService.findOneByEmail(principal.getName());
        if (optionalAccount.isEmpty()) {
            return "redirect:/";
        }

        boolean hasTitle = post.getTitle() != null && !post.getTitle().isBlank();
        boolean hasContent = post.getContent() != null && !post.getContent().isBlank();
        boolean hasTags = post.getTags() != null && !post.getTags().isEmpty();

        if (!hasTitle || !hasContent || !hasTags) {
            model.addAttribute("tags", Constants.TAGS);
            model.addAttribute("error", "Please fill in all fields.");
            return "post_new";
        }

        post.setAccount(optionalAccount.get());
        postService.save(post);
        return "redirect:/posts/" + post.getPostId();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{postId}/edit")
    public String updatePost(@PathVariable("postId") Long postId, @ModelAttribute("post") Post post, Model model,
            Principal principal) {

        if (principal == null || principal.getName() == null) {
            return "redirect:/";
        }

        boolean hasTitle = post.getTitle() != null && !post.getTitle().isBlank();
        boolean hasContent = post.getContent() != null && !post.getContent().isBlank();
        boolean hasTags = post.getTags() != null && !post.getTags().isEmpty();

        if (!hasTitle || !hasContent || !hasTags) {
            model.addAttribute("tags", Constants.TAGS);
            model.addAttribute("error", "Please fill in all fields.");
            return "post_edit";
        }

        Post existingPost = postService.getById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with postId " + postId));

        existingPost.setTitle(post.getTitle());
        existingPost.setContent(post.getContent());
        existingPost.setTags(post.getTags());

        postService.save(existingPost);
        return "redirect:/posts/" + existingPost.getPostId();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/{postId}/edit")
    public String getPostForEdit(@PathVariable("postId") Long postId, Model model, Principal principal) {
        if (principal == null || principal.getName() == null) {
            return "redirect:/";
        }
        Optional<Post> optionalPost = postService.getById(postId);
        if (optionalPost.isPresent()) {
            model.addAttribute("tags", Constants.TAGS);
            model.addAttribute("post", optionalPost.get());
            return "post_edit";
        } else {
            return "error/404";
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/{postId}/delete")
    public String deletePost(@PathVariable("postId") Long postId, Principal principal) {
        if (principal == null || principal.getName() == null) {
            return "redirect:/";
        }
        Optional<Post> optionalPost = postService.getById(postId);
        if (optionalPost.isPresent()) {
            postService.delete(optionalPost.get());
            return "redirect:/";
        } else {
            return "error/404";
        }
    }
}
