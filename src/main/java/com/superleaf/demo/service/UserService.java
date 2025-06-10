package com.superleaf.demo.service;

import com.superleaf.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUser(Long id);
    Optional<User> getUserByUsername(String name);
    User createUser(User user);
    User updateUser(Long id, User userDetails);
    void deleteUser(Long id);
}
