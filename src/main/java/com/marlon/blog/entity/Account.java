package com.marlon.blog.entity;

import com.marlon.blog.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long accountId;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "account")
    private List<Post> posts;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "account_authority",
            joinColumns = {@JoinColumn(name = "account_id", referencedColumnName = "accountId")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    private Set<Authority> authorities = new HashSet<>();

    @Override
    public String toString() {
        return "User {" +
                "accountId=" + accountId +
                ", username='" + username + "'" +
                ", email='" + email + "'" +
                ", authorities=" + authorities +
                "}";
    }
}