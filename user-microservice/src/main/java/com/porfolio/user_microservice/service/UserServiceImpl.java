package com.porfolio.user_microservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.porfolio.user_microservice.dto.UserRequest;
import com.porfolio.user_microservice.dto.UserResponse;
import com.porfolio.user_microservice.model.User;
import com.porfolio.user_microservice.repository.UserRepository;

@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::toUserResponse)
                .orElse(null);
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::toUserResponse)
                .orElse(null);

    }

    @Override
    public UserResponse getUserByName(String name) {
        return userRepository.findByName(name)
                .map(this::toUserResponse)
                .orElse(null);
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        User user = new User();
        BeanUtils.copyProperties(userRequest, user); // copia campos
        String encodedPassword = passwordEncoder.encode(userRequest.getPassword());
        user.setPassword(encodedPassword);
        return toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest userDetails) {
        User user = userRepository.findById(id).orElseThrow();

        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setName(userDetails.getName());
        user.setLastName(userDetails.getLastName());
        user.setRole(userDetails.getRole());

        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        return toUserResponse(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Helper para mapear entidad a DTO
    private UserResponse toUserResponse(User user) {
        UserResponse response = new UserResponse();
        BeanUtils.copyProperties(user, response);
        return response;
    }

}
