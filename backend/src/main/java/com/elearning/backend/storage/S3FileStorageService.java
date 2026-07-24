package com.elearning.backend.storage;

import com.elearning.backend.course.ContentType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name="app.storage.type", havingValue="s3")
public class S3FileStorageService implements FileStorageService{
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    @Value("${app.storage.s3.bucket-name}")
    private String bucketName;
    @Value("${app.storage.s3.presigned-url-expiration-minutes}")
    private Long presignedUrlExpirationMinutes;
    @Override
    public StorageResponse uploadCourseContent (MultipartFile file, Long courseId, ContentType contentType)
    {
        validateFile(file,contentType);
        try {
            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename()==null?"file":file.getOriginalFilename());
            String extension = getFileExtension(originalFileName);
            String storedFileName = UUID.randomUUID() + extension;
            String objectKey = "course-"+courseId + "/" + storedFileName;
            PutObjectRequest putObjectRequest = PutObjectRequest.builder().bucket(bucketName).key(objectKey).contentType(file.getContentType()).build();
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(),file.getSize()));
            return new StorageResponse(objectKey,storedFileName);
        }
        catch (IOException exception)
        {
            throw new IllegalArgumentException("Failed to upload file to S3");
        }
    }
    @Override
    public String getDownloadUrl(String storedFilePath)
    {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(storedFilePath).build();
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder().signatureDuration(Duration.ofMinutes(presignedUrlExpirationMinutes)).getObjectRequest(getObjectRequest).build();
        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }
    private void validateFile(MultipartFile file, ContentType contentType)
    {
        if(file==null || file.isEmpty())
        {
            throw new IllegalArgumentException("File is required");
        }
        String originalFilename = file.getOriginalFilename();
        if(originalFilename == null || originalFilename.isBlank())
        {
            throw new IllegalArgumentException("Invalid file name");
        }
        String lowerCaseFileName = originalFilename.toLowerCase();
        if(contentType==ContentType.PDF && !lowerCaseFileName.endsWith(".pdf"))
        {
            throw new IllegalArgumentException("Only pdf files are allowed for PDF Content");
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
