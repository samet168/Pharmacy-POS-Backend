package com.pharmacy.pos.common.controller;

import com.pharmacy.pos.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/uploads")
@RequiredArgsConstructor
@Slf4j
public class UploadsController {

    @Value("${upload.directory:uploads}")
    private String uploadDirectory;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload file", description = "Upload a file to the server")
    public ApiResponse<Map<String, String>> upload(
            @Parameter(description = "File to upload", required = true, content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestPart(value = "file", required = true) MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
        String uniqueFilename = UUID.randomUUID().toString() + extension;

        // Save file
        Path filePath = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Build response
        Map<String, String> response = new HashMap<>();
        response.put("filename", uniqueFilename);
        response.put("originalFilename", originalFilename);
        response.put("url", "/uploads/" + uniqueFilename);
        response.put("size", String.valueOf(file.getSize()));
        response.put("contentType", file.getContentType());

        log.info("File uploaded successfully: {}", uniqueFilename);

        return ApiResponse.success("File uploaded successfully", response);
    }

    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload image", description = "Upload an image file to the server")
    public ApiResponse<Map<String, String>> uploadImage(
            @Parameter(description = "Image file to upload", required = true, content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestPart(value = "file", required = true) MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        // Validate content type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        // Create images directory if it doesn't exist
        Path imagesPath = Paths.get(uploadDirectory, "images");
        if (!Files.exists(imagesPath)) {
            Files.createDirectories(imagesPath);
        }

        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
        String uniqueFilename = UUID.randomUUID().toString() + extension;

        // Save file
        Path filePath = imagesPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Build response
        Map<String, String> response = new HashMap<>();
        response.put("filename", uniqueFilename);
        response.put("originalFilename", originalFilename);
        response.put("url", "/uploads/images/" + uniqueFilename);
        response.put("size", String.valueOf(file.getSize()));
        response.put("contentType", file.getContentType());

        log.info("Image uploaded successfully: {}", uniqueFilename);

        return ApiResponse.success("Image uploaded successfully", response);
    }

    @DeleteMapping("/{filename}")
    @Operation(summary = "Delete file", description = "Delete a file from the server")
    public ApiResponse<Void> delete(@PathVariable String filename) throws IOException {
        Path filePath = Paths.get(uploadDirectory, filename);
        if (Files.exists(filePath)) {
            Files.delete(filePath);
            log.info("File deleted successfully: {}", filename);
            return ApiResponse.success("File deleted successfully", null);
        } else {
            throw new IllegalArgumentException("File not found: " + filename);
        }
    }
}
