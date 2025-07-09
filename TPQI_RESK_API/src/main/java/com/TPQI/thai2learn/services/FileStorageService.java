package com.TPQI.thai2learn.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private Path rootLocation;

    @PostConstruct
    public void init() {
        try {
            rootLocation = Paths.get(uploadDir);
            Files.createDirectories(rootLocation);
            System.out.println("Upload directory created at: " + rootLocation.toAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage location", e);
        }
    }

    public String store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file.");
        }
        
        try {
            // สร้างชื่อไฟล์ใหม่ที่ไม่ซ้ำกัน
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String newFilename = UUID.randomUUID().toString() + fileExtension;

            Path destinationFile = this.rootLocation.resolve(Paths.get(newFilename))
                                     .normalize().toAbsolutePath();

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
            
            // คืนค่าเป็น Path ที่จะเก็บลง DB (เช่น /uploads/xxxxx.jpg)
            return newFilename; 

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }
}