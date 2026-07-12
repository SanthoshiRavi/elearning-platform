package com.elearning.backend.storage;
import com.elearning.backend.course.ContentType;
import org.springframework.web.multipart.MultipartFile;
public interface FileStorageService {
    StorageResponse uploadCourseContent(
            MultipartFile file,
            Long courseId,
            ContentType contentType
    );
}
