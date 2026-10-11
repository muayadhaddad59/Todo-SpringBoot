package com.luv2code.springboot.todos.service;

import com.luv2code.springboot.todos.entity.User;
import com.luv2code.springboot.todos.repository.UserRepository;
import com.luv2code.springboot.todos.response.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserInfo() {
        User user = getAuthenticatedUser();

        return UserResponse.from(user);
    }

    @Override
    @Transactional
    public void deleteUser() {
        User user = getAuthenticatedUser();

        if(isLastAdmin(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Last admin cannot delete itself");
        }

        userRepository.delete(user);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new AccessDeniedException("Authentication required");
        }

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new AccessDeniedException("Authenticated user not found"));
    }

    private boolean isLastAdmin(User user) {
        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));

        if(isAdmin) {
            long adminCount = userRepository.countAdminUsers();
            return adminCount <= 1;
        }

        return false;
    }
}
