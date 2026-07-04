package com.marlon.blog.controller;

import com.marlon.blog.entity.Account;
import com.marlon.blog.service.AccountService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public String showAccount(Model model, Authentication authentication) {
        Optional<Account> optionalAccount = accountService.findOneByEmail(authentication.getName());
        if (optionalAccount.isEmpty() || !optionalAccount.get().getEmail().equals(authentication.getName())) {
            return "error/404";
        }
        Account account = optionalAccount.get();
        model.addAttribute("account", account);
        return "account";
    }

    // Change this line too
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete")
    public String deleteAccount(Model model, Authentication authentication, RedirectAttributes redirectAttributes) {
        Optional<Account> optionalAccount = accountService.findOneByEmail(authentication.getName());

        if (optionalAccount.isEmpty()) {
            model.addAttribute("error", "Account not found");
            return "account";
        }

        Account account = optionalAccount.get();

        if (!account.getEmail().equals(authentication.getName())) {
            model.addAttribute("error", "You do not have permission to delete this account");
            return "account";
        }

        accountService.delete(account);
        redirectAttributes.addFlashAttribute("accountDeleted", "Account successfully deleted.\nPlease log out.");
        SecurityContextHolder.clearContext();
        SecurityContextHolder.getContext().setAuthentication(null); // add this line
        return "redirect:/login?logout";
    }

}

