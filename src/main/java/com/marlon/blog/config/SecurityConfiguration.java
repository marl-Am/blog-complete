package com.marlon.blog.config;

import com.marlon.blog.enums.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // So we can use @PreAuthorize
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
                .requestMatchers("/js/**", "/css/**", "/images/**", "/fontawesome/**", "/error/403", "/error/404", "/error/500", "/**", "/home", "/projects", "/contact", "/register/**").permitAll()
                .requestMatchers("/posts/new", "/posts/**/edit", "/posts/**/delete").hasRole(Role.ROLE_ADMIN.toString())
                .anyRequest().authenticated()
                .and()
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/")
                        .failureUrl("/login?error=The credentials you entered are incorrect.")
                        .permitAll()
                );
        return http.build();
    }

    // supports the use of HTTP methods other than GET and POST in a web application
    // allows the Put operation
    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }
}
