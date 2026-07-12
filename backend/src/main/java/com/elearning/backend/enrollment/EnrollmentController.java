package com.elearning.backend.enrollment;

import com.elearning.backend.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EnrollmentController {
    private final EnrollmentService enrollmentService;
    @PostMapping("/api/courses/{courseId}/enroll")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse> enrollInCourse(@PathVariable Long courseId, Authentication authentication)
    {
        enrollmentService.enrollInCourse(courseId,authentication);
        return ResponseEntity.ok(new ApiResponse("Enrollment successful"));
    }
    @GetMapping("/api/my-courses")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<EnrollmentCourseResponse>> getMyCourses(Authentication authentication)
    {
        List<EnrollmentCourseResponse> courses=enrollmentService.getMyCourses(authentication);
        return ResponseEntity.ok(courses);
    }

}
