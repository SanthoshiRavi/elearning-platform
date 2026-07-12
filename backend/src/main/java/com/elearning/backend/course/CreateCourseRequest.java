package com.elearning.backend.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCourseRequest {
    @NotBlank(message="Course title is required")
    @Size(max=150,message="Course title must not exceed 150 characters")
    private String title;
    @Size(max=2000,message="Description must not exceed 2000 characters")
    private String description;
    @Size(max=100,message="Category must not exceed 100 characters")
    private String category;
}
