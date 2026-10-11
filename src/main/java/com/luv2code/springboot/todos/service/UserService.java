package com.luv2code.springboot.todos.service;

import com.luv2code.springboot.todos.response.UserResponse;

public interface UserService {
    UserResponse getUserInfo();
    void deleteUser();
}
