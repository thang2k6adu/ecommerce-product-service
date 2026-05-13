package com.ecommerce.productservice.modules.productimage;

import com.ecommerce.productservice.common.exception.BadRequestException;
import com.ecommerce.productservice.config.MinioConfig;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    public String uploadImage(MultipartFile file, String folder) {
        try {
            if (file.isEmpty()) {
                throw new BadRequestException("File is empty");
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new BadRequestException("File must be an image");
            }

            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ?
                    originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
            String filename = UUID.randomUUID().toString() + extension;
            String objectName = folder + "/" + filename;

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(contentType)
                            .build()
            );

            String url = String.format("%s/%s/%s",
                    minioConfig.getUrl(),
                    minioConfig.getBucketName(),
                    objectName);

            log.info("Image uploaded successfully: {}", url);
            return url;

        } catch (Exception e) {
            log.error("Error uploading image: ", e);
            throw new BadRequestException("Failed to upload image: " + e.getMessage());
        }
    }

    public InputStream downloadImage(String objectName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            log.error("Error downloading image: ", e);
            throw new BadRequestException("Failed to download image: " + e.getMessage());
        }
    }

    public void deleteImage(String imageUrl) {
        try {
            String objectName = imageUrl.replace(minioConfig.getUrl() + "/" + minioConfig.getBucketName() + "/", "");

            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minioConfig.getBucketName())
                            .object(objectName)
                            .build()
            );

            log.info("Image deleted successfully: {}", imageUrl);

        } catch (Exception e) {
            log.error("Error deleting image: ", e);
            throw new BadRequestException("Failed to delete image: " + e.getMessage());
        }
    }

    public String uploadProductImage(MultipartFile file, UUID productId) {
        return uploadImage(file, "products/" + productId.toString());
    }

    public String uploadCategoryImage(MultipartFile file) {
        return uploadImage(file, "categories");
    }

    public String uploadBrandLogo(MultipartFile file) {
        return uploadImage(file, "brands");
    }
}
