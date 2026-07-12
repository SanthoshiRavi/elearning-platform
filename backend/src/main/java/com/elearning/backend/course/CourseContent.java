package com.elearning.backend.course;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name="course_contents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CourseContent {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="course_id",nullable = false)
    private Course course;
    @Column(name="content_title",nullable = false)
    private String contentTitle;
    @Enumerated(EnumType.STRING)
    @Column(name="content_type",nullable=false,length=20)
    private ContentType contentType;
    @Column(name="content_url",nullable = false)
    private String contentUrl;
    @Column(name="sequence_order",nullable = false)
    private Integer sequenceOrder;
    @Column(name="created_at",nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate()
    {
        this.createdAt = LocalDateTime.now();
    }
}
