package com.superleaf.demo.service;

import com.superleaf.demo.model.Project;
import com.superleaf.demo.model.User;
import com.superleaf.demo.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public List<Project> getProjectsByUser(User user) {
        return projectRepository.findByUser(user);
    }

    public Project createProject(String title, User user) {
        // Check for duplicate title for this user
        if (projectRepository.findByUserAndTitle(user, title).isPresent()) {
            throw new IllegalArgumentException("Project with this title already exists for this user.");
        }

        Project project = new Project(title, user);
        return projectRepository.save(project);
    }
}
