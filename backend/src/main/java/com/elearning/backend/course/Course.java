package com.elearning.backend.course;

import lombok.*;
import com.elearning.backend.user.User;
import jakarta.persistence.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
@Table(name="courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false,length=150)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(length=100)
    private String category;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="created_by",nullable = false)
    private User createdBy;
    @Column(name="is_active",nullable = false)
    private Boolean active;
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
        if(this.active == null)
        {
            this.active=true;
        }
    }

    @PreUpdate
    protected void onUpdate()
    {
        this.updatedAt = LocalDateTime.now();
    }
}
