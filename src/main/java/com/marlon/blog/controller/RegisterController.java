package com.marlon.blog.controller;

import com.marlon.blog.entity.Account;
import com.marlon.blog.entity.Authority;
import com.marlon.blog.enums.Role;
import com.marlon.blog.repository.AuthorityRepository;
import com.marlon.blog.service.AccountService;
import org.springframework.security.core.Authentication;
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
    private final AccountService accountService;
    private final AuthorityRepository authorityRepository;

    public RegisterController(AccountService accountService, AuthorityRepository authorityRepository) {
        this.accountService = accountService;
        this.authorityRepository = authorityRepository;
    }

    @GetMapping
    public String getRegisterForm(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/";
        }
        Account account = new Account();
        model.addAttribute("account", account);
        return "register";
    }
    @PostMapping
    public String registerNewUser(@ModelAttribute Account account) {
        if (accountService.existsByUsername(account.getUsername()) || accountService.existsByEmail(account.getEmail())) {
            return "redirect:/register?error=Username or email already taken.";
        }
        account.setRole(Role.ROLE_GUEST);
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById("ROLE_GUEST").ifPresent(authorities::add);
        account.setAuthorities(authorities);
        accountService.save(account);
        return "redirect:/login?message=Account created successfully.";
    }
}
