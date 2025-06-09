package com.superleaf.demo.repository;

import com.superleaf.demo.model.Project;
import com.superleaf.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByUser(User user);
    Optional<Project> findByUserAndTitle(User user, String title);
}