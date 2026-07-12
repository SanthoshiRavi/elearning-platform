package com.elearning.backend.user;
import com.elearning.backend.user.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table (
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name="uk_users_email",columnNames = "email")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,length=100)
    private String name;
    @Column(nullable = false,unique=true,length=150)
    private String email;
    @Column(name="password_hash",nullable=false)
    private String passwordHash;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length=20)
    private Role role;
    @Column(name="created_at",nullable = false)
    private LocalDateTime createdAt;
    @Column(name="updated_at",nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate()
    {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate()
    {
        this.updatedAt = LocalDateTime.now();
    }

}
