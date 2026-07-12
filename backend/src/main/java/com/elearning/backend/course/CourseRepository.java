package com.elearning.backend.course;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course,Long> {
    List<Course> findByActiveTrue();
    List<Course> findByCategoryAndActiveTrue(String category);
}
