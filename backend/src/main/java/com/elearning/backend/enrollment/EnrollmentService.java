package com.elearning.backend.enrollment;

import com.elearning.backend.course.Course;
import com.elearning.backend.course.CourseRepository;
import com.elearning.backend.user.User;
import com.elearning.backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    public void enrollInCourse(Long courseId, Authentication authentication)
    {
        String studentEmail = authentication.getName();
        User student = userRepository.findByEmail(studentEmail).orElseThrow(()->new IllegalArgumentException("Logged-in student not found"));
        Course course = courseRepository.findById(courseId).orElseThrow(()->new IllegalArgumentException("Course not found"));
        if(!Boolean.TRUE.equals(course.getActive()))
        {
            throw new IllegalArgumentException("Course is not available for enrollment");
        }
        boolean alreadyEnrolled = enrollmentRepository.existsByUser_IdAndCourse_Id(student.getId(),course.getId());
        if(alreadyEnrolled)
        {
            throw new IllegalArgumentException("You are already enrolled in this course");
        }
        Enrollment enrollment = Enrollment.builder()
                .user(student)
                .course(course)
                .status(EnrollmentStatus.ACTIVE)
                .build();
        try
        {
            enrollmentRepository.save(enrollment);
        }
        catch(DataIntegrityViolationException exception)
        {
            throw new IllegalArgumentException("You are already enrolled in this course");

        }


    }
    public List<EnrollmentCourseResponse> getMyCourses(Authentication authentication)
    {
        String studentEmail = authentication.getName();
        User student=userRepository.findByEmail(studentEmail).orElseThrow(()->new IllegalArgumentException("Logged-in student not found"));
        return enrollmentRepository.findByUser_IdAndStatus(student.getId(),EnrollmentStatus.ACTIVE).stream().map(this::mapToResponse).toList();
    }
    private EnrollmentCourseResponse mapToResponse(Enrollment enrollment)
    {
        Course course = enrollment.getCourse();
        return EnrollmentCourseResponse.builder()
                .enrollmentId(enrollment.getId())
                .courseId(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .category(course.getCategory())
                .status(enrollment.getStatus().name())
                .enrolledAt(enrollment.getEnrolledAt())
                .build();
    }

}
