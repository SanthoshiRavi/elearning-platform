package com.elearning.backend.storage;

import com.elearning.backend.course.ContentType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name="app.storage.type",havingValue="local")
public class LocalFileStorageService implements FileStorageService{
    @Value("${app.storage.local.upload-dir}")
    private String uploadDir;
    @Override
    public StorageResponse uploadCourseContent(
            MultipartFile file,
            Long courseId,
            ContentType contentType
    ) {
        validateFile(file, contentType);
        try {
            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename() == null ? "file" : file.getOriginalFilename());
            String extension = getFileExtension(originalFileName);
            String storedFilename = UUID.randomUUID() + extension;
            Path courseUploadPath = Paths.get(uploadDir, "course-" + courseId)
                    .toAbsolutePath()
                    .normalize();
            Files.createDirectories(courseUploadPath);
            Path targetPath = courseUploadPath.resolve(storedFilename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            String fileUrl = "/uploads/course-" + courseId + "/" + storedFilename;
            return new StorageResponse(fileUrl, storedFilename);
        } catch (IOException exception) {
            throw new IllegalArgumentException("Failed to upload file");
        }
    }
    private void validateFile(MultipartFile file, ContentType contentType) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isBlank()) {
            throw new IllegalArgumentException("Invalid file name");

        }
        String lowerCaseFileName = originalFileName.toLowerCase();
        if (contentType == ContentType.PDF && !lowerCaseFileName.endsWith(".pdf")) {
            throw new IllegalArgumentException("Only PDF files are allowed fpr PDF content");
        }
        if (contentType == ContentType.VIDEO && !(lowerCaseFileName.endsWith(".mp4") || lowerCaseFileName.endsWith(".mov") || lowerCaseFileName.endsWith("mkv"))) {
            throw new IllegalArgumentException("Only MP4, MOV, MKV files are allowed for video content");
        }
    }
    public String getFileExtension (String fileName)
    {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1) {
            return "";
        }
        return fileName.substring(dotIndex);
    }
}

