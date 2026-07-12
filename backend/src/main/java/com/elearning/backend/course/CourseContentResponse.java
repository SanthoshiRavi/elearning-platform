package com.elearning.backend.course;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CourseContentResponse {
    private Long id;
    private Long courseId;
    private String contentTitle;
    private String contentType;
    private String contentUrl;
    private Integer sequenceOrder;
    private LocalDateTime createdAt;
}
