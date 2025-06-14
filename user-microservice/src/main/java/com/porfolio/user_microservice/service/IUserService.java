package com.porfolio.user_microservice.service;

import java.util.List;

import com.porfolio.user_microservice.dto.UserRequest;
import com.porfolio.user_microservice.dto.UserResponse;

public interface IUserService {
    List<UserResponse> getAllUsers();

    UserResponse getUserByUsername(String username);

    UserResponse getUserByName(String name);

    UserResponse getUserById(Long id);

    UserResponse createUser(UserRequest userRequest);

    UserResponse updateUser(Long id, UserRequest userDetails);

    void deleteUser(Long id);

}
