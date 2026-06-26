package com.jjimkong_backend.global.config.s3;

import com.jjimkong_backend.global.response.exception.BadRequestException;
import com.jjimkong_backend.global.response.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

/**
 * S3 업로드/삭제 담당. (서버 경유 업로드)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    /**
     * 파일을 S3의 {dir}/ 경로에 업로드하고 (객체 키, 공개 URL)을 반환한다.
     */
    public UploadResult upload(MultipartFile file, String dir) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException(ExceptionCode.INVALID_IMAGE_FILE);
        }
        String key = dir + "/" + UUID.randomUUID() + extensionOf(file.getOriginalFilename());
        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );
        } catch (IOException e) {
            log.error("S3 업로드 실패 - file: {}", file.getOriginalFilename(), e);
            throw new BadRequestException(ExceptionCode.IMAGE_UPLOAD_FAILED);
        }
        String url = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;
        return new UploadResult(key, url);
    }

    /** 객체 키로 S3에서 삭제한다. */
    public void delete(String key) {
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build());
    }

    private String extensionOf(String originalFilename) {
        if (originalFilename == null || !originalFilename.contains(".")) {
            return "";
        }
        return originalFilename.substring(originalFilename.lastIndexOf("."));
    }

    public record UploadResult(String key, String url) {
    }
}
