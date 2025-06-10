package com.superleaf.demo.controller;

import com.superleaf.demo.model.Project;
import com.superleaf.demo.model.User;
import com.superleaf.demo.service.ProjectService;
import com.superleaf.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService; // For fetching full User object from username

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody CreateProjectRequest request,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByUsername(userDetails.getUsername())
    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userDetails.getUsername()));

        Project project = projectService.createProject(request.getTitle(), user);
        return ResponseEntity.ok(project);
    }

    @GetMapping
    public ResponseEntity<List<Project>> getProjects(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserByUsername(userDetails.getUsername())
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userDetails.getUsername()));;
        return ResponseEntity.ok(projectService.getProjectsByUser(user));
    }

    // DTO for input
    public static class CreateProjectRequest {
        private String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
