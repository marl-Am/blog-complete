package com.marlon.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Specific, restrictive rules must come before any broad pattern.
                        .requestMatchers("/posts/new", "/posts/*/edit", "/posts/*/delete")
                        .hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/", "/home", "/posts", "/posts/*",
                                "/account", "/account/**",
                                "/js/**", "/css/**", "/images/**", "/fontawesome/**",
                                "/webjars/**",
                                "/error", "/error/**",
                                "/projects", "/contact",
                                "/register", "/register/**", "/login")
                        .permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/")
                        .failureUrl("/login?error=The credentials you entered are incorrect.")
                        .permitAll());
        return http.build();
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }
}
