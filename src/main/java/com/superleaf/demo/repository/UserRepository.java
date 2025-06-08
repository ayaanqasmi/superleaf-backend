package com.superleaf.demo.repository;
import com.superleaf.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User, Long> {
    // You get CRUD methods for free (save, findById, findAll, delete, etc.)
    User findByUsername(String username); // Optional custom method
}