package com.elearning.backend.enrollment;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class EnrollmentCourseResponse {
    private Long enrollmentId;
    private Long courseId;
    private String title;
    private String description;
    private String category;
    private String status;
    private LocalDateTime enrolledAt;
}
