package com.luv2code.springboot.todos.response;

import com.luv2code.springboot.todos.entity.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public class UserResponse {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> authorities;

    public UserResponse(long id, String firstName, String lastName, String email, List<String> authorities) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.authorities = authorities;
    }

    public static UserResponse from(User user) {
        List<String> authorities = user.getAuthorities() == null
                ? List.of()
                : user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList();

        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                authorities
        );
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getAuthorities() {
        return authorities;
    }
}
