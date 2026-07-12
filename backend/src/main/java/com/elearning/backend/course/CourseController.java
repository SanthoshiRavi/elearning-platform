package com.elearning.backend.course;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.management.MemoryUsage;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    @PostMapping("/api/admin/courses")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseResponse> createCourse(@Valid @RequestBody CreateCourseRequest request, Authentication authentication)
    {
        CourseResponse response=courseService.createCourse(request,authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/api/courses")
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
    public ResponseEntity<List<CourseResponse>> getAvailableCourses()
    {
        List<CourseResponse> courses = courseService.getAvailableCourses();
        return ResponseEntity.ok(courses);
    }
    @GetMapping("/api/admin/courses")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CourseResponse>> getAllCoursesForAdmin()
    {
        List<CourseResponse> courses=courseService.getAllCoursesForAdmin();
        return ResponseEntity.ok(courses);
    }
    @PostMapping("/api/admin/courses/{courseId}/contents")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseContentResponse> uploadCourseContent(
            @PathVariable Long courseId,
            @RequestParam String contentTitle,
            @RequestParam ContentType contentType,
            @RequestParam Integer sequenceOrder,
            @RequestParam MultipartFile file
            )
    {
        CourseContentResponse response=courseService.uploadCourseContent(courseId,contentTitle,contentType,sequenceOrder,file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/api/courses/{courseId}/contents")
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
    public ResponseEntity<List<CourseContentResponse>> getCourseContents(
            @PathVariable Long courseId,
            Authentication authentication
    )
    {
        List<CourseContentResponse> response = courseService.getCourseContents(courseId,authentication);
        return ResponseEntity.ok(response);
    }
}
