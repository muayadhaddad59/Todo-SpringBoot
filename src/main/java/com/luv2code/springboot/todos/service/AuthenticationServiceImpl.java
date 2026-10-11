package com.luv2code.springboot.todos.service;

import com.luv2code.springboot.todos.entity.Authority;
import com.luv2code.springboot.todos.entity.User;
import com.luv2code.springboot.todos.repository.UserRepository;
import com.luv2code.springboot.todos.request.RegisterRequest;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Transactional
    public void register(RegisterRequest input) throws Exception {
        String email = normalizeEmail(input.getEmail());

        if(isEmailTaken(email)) { throw new IllegalArgumentException("Email already exists"); }

        User user = buildNewUser(input);
        user.setEmail(email);

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new IllegalArgumentException("Email already exists", exception);
        }
    }

    private boolean isEmailTaken(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private User buildNewUser(RegisterRequest input) {
        User user = new User();
        user.setFirstName(input.getFirstName().trim());
        user.setLastName(input.getLastName().trim());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setAuthorities(initialAuthorities());
        return user;
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private List<Authority> initialAuthorities() {
        boolean isFirstUser = userRepository.count() == 0;
        List<Authority> authorities = new ArrayList<>();
        authorities.add(new Authority("ROLE_EMPLOYEE"));

        if (isFirstUser) {
            authorities.add(new Authority("ROLE_ADMIN"));
        }

        return authorities;
    }
}
