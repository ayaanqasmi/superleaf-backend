package com.superleaf.demo.service;

import com.superleaf.demo.model.User;
import com.superleaf.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public List<User> getAllUsers() {
        return repo.findAll();
    }

    public Optional<User> getUser(Long id) {
        return repo.findById(id);
    }
     public User getUserByUsername(String name) {
        return repo.findByUsername(name);
    }

    public User createUser(User user) {
        return repo.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        User user = repo.findById(id).orElseThrow();
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());
        return repo.save(user);
    }

    public void deleteUser(Long id) {
        repo.deleteById(id);
    }
}
