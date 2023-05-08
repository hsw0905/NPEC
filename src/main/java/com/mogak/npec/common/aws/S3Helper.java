package com.mogak.npec.common.aws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Slf4j
@Component
public class S3Helper {
    private static final String S3 = "S3";
    private final String bucketName;
    private final String region;

    private final S3Client s3Client;

    public S3Helper(S3Client s3Client, @Value("${s3.bucket}") String bucketName,
                    @Value("${spring.cloud.aws.region.static}") String region) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.region = region;
    }

    public boolean uploadImage(String imageName, String path, String extension, MultipartFile multipartFile) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(path + "/" + imageName)
                .contentType("image/" + extension)
                .build();

        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(multipartFile.getBytes()));
            return true;
        } catch (IOException e) {
            log.error("bucket: " + bucketName + ", fileName: " + imageName + " error : " + e.getMessage());
        }

        return false;
    }

    public String getPath(String path, String imageName) {
        return "https://" + bucketName + "." + S3 + "." + region + "." + "amazonaws.com" + "/"
                + path + "/" + imageName;
    }
}
