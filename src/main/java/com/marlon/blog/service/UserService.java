package com.marlon.blog.service;

import com.marlon.blog.entity.Authority;
import com.marlon.blog.entity.User;
import com.marlon.blog.repository.AuthorityRepository;
import com.marlon.blog.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, AuthorityRepository authorityRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }
    public void save(User user) {
        if (user.getId() == null) {
            if (user.getAuthorities().isEmpty()) {
                Set<Authority> authorities = new HashSet<>();
                authorityRepository.findById("ROLE_GUEST").ifPresent(authorities::add);
                user.setAuthorities(authorities);
            }
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
    public Optional<User> findOneByEmail(String email) {
        return userRepository.findOneByEmailIgnoreCase(email);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
