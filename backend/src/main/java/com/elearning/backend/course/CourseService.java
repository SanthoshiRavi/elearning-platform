package com.elearning.backend.course;

import com.elearning.backend.enrollment.EnrollmentRepository;
import com.elearning.backend.storage.FileStorageService;
import com.elearning.backend.storage.StorageResponse;
import com.elearning.backend.user.Role;
import com.elearning.backend.user.User;
import com.elearning.backend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final FileStorageService fileStorageService;
    private final CourseContentRepository courseContentRepository;
    public CourseResponse createCourse(CreateCourseRequest request, Authentication authentication)
    {
        String adminEmail=authentication.getName();
        User admin=userRepository.findByEmail(adminEmail).orElseThrow(()->new IllegalArgumentException("Logged-in admin user not found"));
        Course course=Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .createdBy(admin)
                .active(true)
                .build();
        Course savedCourse=courseRepository.save(course);
        return mapToCourseResponse(savedCourse);
    }
    public List<CourseResponse> getAvailableCourses()
    {
        return courseRepository.findByActiveTrue()
                .stream()
                .map(this::mapToCourseResponse)
                .toList();
    }
    public List<CourseResponse> getAllCoursesForAdmin()
    {
        return courseRepository.findAll()
                .stream()
                .map(this::mapToCourseResponse)
                .toList();
    }
    public CourseContentResponse uploadCourseContent(Long courseId, String contentTitle, ContentType contentType, Integer sequenceOrder, MultipartFile file)
    {
        Course course=courseRepository.findById(courseId).orElseThrow(()->new IllegalArgumentException("Course not found"));
        validateContentUploadRequest(contentTitle,contentType,sequenceOrder,file);
        StorageResponse storageResponse=fileStorageService.uploadCourseContent(file,course.getId(),contentType);
        CourseContent courseContent = CourseContent.builder()
                .course(course)
                .contentTitle(contentTitle)
                .contentType(contentType)
                .contentUrl(storageResponse.getFileUrl())
                .sequenceOrder(sequenceOrder)
                .build();
        CourseContent savedContent=courseContentRepository.save(courseContent);
        return mapToCourseContentResponse(savedContent);
    }
    public List<CourseContentResponse> getCourseContents(Long courseId, Authentication authentication)
    {
        String loggedInUserEmail = authentication.getName();
        User user = userRepository.findByEmail(loggedInUserEmail).orElseThrow(()->new IllegalArgumentException("Course not found"));
        Course course = courseRepository.findById(courseId).orElseThrow(()->new IllegalArgumentException("Course not found"));
        if(!Boolean.TRUE.equals(course.getActive()))
        {
            throw new IllegalArgumentException("Course is not ACTIVE");
        }
        boolean isAdmin=user.getRole()== Role.ADMIN;
        boolean isEnrolledStudent = enrollmentRepository.existsByUser_IdAndCourse_Id(user.getId(),course.getId());
        if(!isAdmin && !isEnrolledStudent)
        {
            throw new IllegalArgumentException("Please enroll in this course to view the content");
        }
        return courseContentRepository.findByCourseIdOrderBySequenceOrderAsc(courseId).stream().map(this::mapToCourseContentResponse).toList();
    }
    private void validateContentUploadRequest(String contentTitle, ContentType contentType,Integer sequenceOrder,MultipartFile file)
    {
        if(contentTitle==null || contentTitle.isBlank())
        {
            throw new IllegalArgumentException("Content title is required");
        }
        if(file==null || file.isEmpty())
        {
            throw new IllegalArgumentException("File is required");
        }
    }
    private CourseResponse mapToCourseResponse(Course course)
    {
        return CourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .category(course.getCategory())
                .active(course.getActive())
                .createdBy(course.getCreatedBy().getEmail())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .build();
    }
    private CourseContentResponse mapToCourseContentResponse(CourseContent courseContent)
    {
        return CourseContentResponse.builder()
                .id(courseContent.getId())
                .courseId(courseContent.getId())
                .contentTitle(courseContent.getContentTitle())
                .contentType(courseContent.getContentType().name())
                .contentUrl(courseContent.getContentUrl())
                .sequenceOrder(courseContent.getSequenceOrder())
                .createdAt(courseContent.getCreatedAt())
                .build();

    }
//    private CourseResponse mapToResponse(Course course)
//    {
//        return CourseResponse.builder()
//            .id(course.getId())
//                .title(course.getTitle())
//                .description(course.getDescription())
//                .category(course.getCategory())
//                .active(course.getActive())
//                .createdBy(course.getCreatedBy().getEmail())
//                .createdAt(course.getCreatedAt())
//                .updatedAt(course.getUpdatedAt())
//                .build();
//    }
}
