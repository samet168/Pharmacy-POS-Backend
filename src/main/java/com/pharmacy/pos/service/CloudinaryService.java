package com.pharmacy.pos.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadImage(MultipartFile file, String folderName) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        Map<String, Object> params = Map.of(
            "folder", folderName,
            "resource_type", "auto"
        );

        // Convert MultipartFile to bytes for Cloudinary
        byte[] fileBytes = file.getBytes();
        Map<?, ?> uploadResult = cloudinary.uploader().upload(fileBytes, ObjectUtils.asMap(
            "folder", folderName,
            "resource_type", "auto"
        ));
        
        if (uploadResult.get("url") != null) {
            return uploadResult.get("url").toString();
        }

        throw new RuntimeException("Failed to upload image to Cloudinary");
    }

    public String uploadUserImage(MultipartFile file) throws IOException {
        return uploadImage(file, "pharmacy-pos/users");
    }

    public String uploadProductImage(MultipartFile file) throws IOException {
        return uploadImage(file, "pharmacy-pos/products");
    }

    public String uploadCustomerImage(MultipartFile file) throws IOException {
        return uploadImage(file, "pharmacy-pos/customers");
    }

    public String uploadDoctorImage(MultipartFile file) throws IOException {
        return uploadImage(file, "pharmacy-pos/doctors");
    }

    public boolean deleteImage(String publicId) {
        try {
            Map<?, ?> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return result.get("result") != null && result.get("result").equals("ok");
        } catch (Exception e) {
            return false;
        }
    }
}
