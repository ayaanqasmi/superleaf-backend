package com.superleaf.demo.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*; // Lombok import
import com.superleaf.demo.model.Project;
import java.util.List;

@Entity
@Table(name = "users")
@Data                         // Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor            // Generates no-args constructor
@AllArgsConstructor 

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is mandatory")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Column(nullable = false)
    private String password;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> projects;

    
    public void addProject(Project project) {
        projects.add(project);
        project.setUser(this);
    }

    public void removeProject(Project project) {
        projects.remove(project);
        project.setUser(null);
    }

}
