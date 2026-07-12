package com.elearning.backend.enrollment;

import com.elearning.backend.course.Course;
import com.elearning.backend.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
@Table
        (name="enrollments",
                uniqueConstraints = {
                        @UniqueConstraint(
                                name="uk_user_course_enrollment",
                                columnNames = {"user_id","course_id"}
                        )
                }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id",nullable = false)
    private User user;
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="course_id",nullable = false)
    private Course course;
    @Column(name="enrolled_at",nullable = false)
    private LocalDateTime enrolledAt;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length=20)
    private EnrollmentStatus status;

    @PrePersist
    protected void onCreate()
    {
        this.enrolledAt=LocalDateTime.now();
        if(this.status==null)
        {
            this.status=EnrollmentStatus.ACTIVE;
        }
    }
}
