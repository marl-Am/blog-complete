package com.marlon.blog.service;

import com.marlon.blog.entity.Authority;
import com.marlon.blog.entity.Account;
import com.marlon.blog.repository.AuthorityRepository;
import com.marlon.blog.repository.AccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class AccountService {
    private static final Pattern BCRYPT_PATTERN = Pattern.compile("^\\$2[abxy]\\$\\d{2}\\$[./A-Za-z0-9]{53}$");

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final AuthorityRepository authorityRepository;

    public AccountService(PasswordEncoder passwordEncoder, AccountRepository accountRepository,
            AuthorityRepository authorityRepository) {
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
        this.authorityRepository = authorityRepository;
    }

    public void save(Account account) {
        if (account.getAccountId() == null) {
            if (account.getAuthorities().isEmpty()) {
                Set<Authority> authorities = new HashSet<>();
                authorityRepository.findById("ROLE_GUEST").ifPresent(authorities::add);
                account.setAuthorities(authorities);
            }
        }

        String rawPassword = account.getPassword();
        if (rawPassword != null && !isBcryptHash(rawPassword)) {
            account.setPassword(passwordEncoder.encode(rawPassword));
        }

        accountRepository.save(account);
    }

    private boolean isBcryptHash(String value) {
        return BCRYPT_PATTERN.matcher(value).matches();
    }

    public Optional<Account> findOneByEmail(String email) {
        return accountRepository.findOneByEmailIgnoreCase(email);
    }

    public boolean existsByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }

    public void delete(Account account) {
        accountRepository.delete(account);
    }

    public Optional<Account> findById(Long accountId) {
        return accountRepository.findById(accountId);
    }
}
