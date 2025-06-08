package com.superleaf.demo.controller;

import com.superleaf.demo.model.User;
import com.superleaf.demo.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return service.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return service.getUser(id).orElseThrow();
    }

    @PostMapping
    public ResponseEntity<?>  createUser(@Valid @RequestBody User user,BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
        // Return first validation error message (or customize)
        String errorMsg = bindingResult.getFieldError().getDefaultMessage();
        return ResponseEntity.badRequest().body(errorMsg);
    }
        User createdUser = service.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return service.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
    }
}
