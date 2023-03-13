package com.marlon.blog.controller;

import com.marlon.blog.entity.Authority;
import com.marlon.blog.entity.User;
import com.marlon.blog.enums.Role;
import com.marlon.blog.repository.AuthorityRepository;
import com.marlon.blog.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/register")
public class RegisterController {
    private final UserService userService;
    private final AuthorityRepository authorityRepository;

    public RegisterController(UserService userService, AuthorityRepository authorityRepository) {
        this.userService = userService;
        this.authorityRepository = authorityRepository;
    }

    @GetMapping
    public String getRegisterForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }
    @PostMapping
    public String registerNewUser(@ModelAttribute User user) {
        if (userService.existsByUsername(user.getUsername()) || userService.existsByEmail(user.getEmail())) {
            return "redirect:/register?error=Username or email already taken.";
        }
        user.setRole(Role.ROLE_GUEST);
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById("ROLE_GUEST").ifPresent(authorities::add);
        user.setAuthorities(authorities);
        userService.save(user);
        return "redirect:/login?message=Account created successfully.";
    }
}
