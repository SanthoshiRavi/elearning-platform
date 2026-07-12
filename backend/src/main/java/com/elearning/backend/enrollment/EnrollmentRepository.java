package com.elearning.backend.enrollment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByUser_IdAndCourse_Id(Long userId, Long courseId);

    Optional<Enrollment> findByUser_IdAndCourse_Id(Long userId, Long courseId);

    List<Enrollment> findByUser_IdAndStatus(Long userId, EnrollmentStatus status);
}