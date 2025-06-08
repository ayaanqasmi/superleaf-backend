package com.superleaf.demo.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*; // Lombok import

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

    @NotBlank(message = "Username is mandatory")
    @Column(nullable = false)
    private String password;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is mandatory")
    @Column(nullable = false, unique = true)
    private String email;
}
