package com.marlon.blog.config;

import com.marlon.blog.entity.Account;
import com.marlon.blog.entity.Authority;
import com.marlon.blog.entity.Post;
import com.marlon.blog.enums.Role;
import com.marlon.blog.repository.AuthorityRepository;
import com.marlon.blog.service.PostService;
import com.marlon.blog.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SeedData implements CommandLineRunner {
    private final PostService postService;
    private final AccountService accountService;
    private final AuthorityRepository authorityRepository;
    public SeedData(PostService postService, AccountService accountService, AuthorityRepository authorityRepository) {
        this.postService = postService;
        this.accountService = accountService;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Post> posts = postService.getAll();

        if (posts.size() == 0) {

            Authority guest = new Authority();
            guest.setName("ROLE_GUEST");
            authorityRepository.save(guest);

            Authority admin = new Authority();
            admin.setName("ROLE_ADMIN");
            authorityRepository.save(admin);

            Account admin_account = new Account();

            admin_account.setUsername("Marlon");
            admin_account.setEmail("mamedee001@gmail.com");
            admin_account.setPassword("+B.SUYu-:eL?~>7yrTO!k&[{~0}0h<");

            admin_account.setRole(Role.ROLE_ADMIN);
            Set<Authority> authorities = new HashSet<>();
            authorityRepository.findById("ROLE_ADMIN").ifPresent(authorities::add);
            admin_account.setAuthorities(authorities);

            accountService.save(admin_account);

            Post post1 = new Post();
            post1.setTitle("Something else Ipsum");
            post1.setContent("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Magna eget est lorem ipsum dolor sit amet consectetur adipiscing. Tempus quam pellentesque nec nam aliquam sem et tortor. Pellentesque sit amet porttitor eget. Sed augue lacus viverra vitae congue eu consequat. Ultrices vitae auctor eu augue. Mattis rhoncus urna neque viverra. Consectetur lorem donec massa sapien faucibus et molestie ac feugiat. Sociis natoque penatibus et magnis dis parturient montes nascetur. Cursus turpis massa tincidunt dui ut ornare lectus. Odio pellentesque diam volutpat commodo sed egestas egestas fringilla. Id cursus metus aliquam eleifend mi. Nibh nisl condimentum id venenatis a condimentum.");
            post1.setAccount(admin_account);
            List<String> post2_tags = new ArrayList<>();
            post2_tags.add("Other");
            post1.setTags(post2_tags);

            postService.save(post1);
        }
    }

}