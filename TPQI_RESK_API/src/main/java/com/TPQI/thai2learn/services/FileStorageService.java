package com.TPQI.thai2learn.services;

import com.TPQI.thai2learn.DTO.EvidenceFileDTO; 
import com.TPQI.thai2learn.entities.tpqi_asm.AssessmentEvidenceFile; 
import com.TPQI.thai2learn.repositories.tpqi_asm.AssessmentEvidenceFileRepository; 
import org.springframework.beans.factory.annotation.Autowired; 
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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors; 

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private Path rootLocation;

    @Autowired
    private AssessmentEvidenceFileRepository evidenceFileRepository;

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
            
            return newFilename; 

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }


    public List<EvidenceFileDTO> getFilesByApplicantId(Long applicantId) {
        List<AssessmentEvidenceFile> files = evidenceFileRepository.findByAssessmentApplicantId(applicantId);
        
        return files.stream().map(fileEntity -> {
            EvidenceFileDTO dto = new EvidenceFileDTO();
            dto.setId(fileEntity.getId());
            dto.setOriginalFilename(fileEntity.getOriginalFilename());
            dto.setFilePath(fileEntity.getFilePath());
            dto.setEvidenceType(fileEntity.getEvidenceType());
            return dto;
        }).collect(Collectors.toList());
    }
}