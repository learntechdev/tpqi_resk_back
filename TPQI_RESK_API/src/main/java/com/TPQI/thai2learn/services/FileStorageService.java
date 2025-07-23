package com.TPQI.thai2learn.services;

import com.TPQI.thai2learn.DTO.EvidenceFileDTO;
import com.TPQI.thai2learn.entities.tpqi_asm.AssessmentEvidenceFile;
import com.TPQI.thai2learn.entities.tpqi_asm.EvidenceCompetencyLink;
import com.TPQI.thai2learn.repositories.tpqi_asm.AssessmentEvidenceFileRepository;
import com.TPQI.thai2learn.repositories.tpqi_asm.EvidenceCompetencyLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @Autowired
    private EvidenceCompetencyLinkRepository evidenceCompetencyLinkRepository;


    @PostConstruct
    public void init() {
        try {
            rootLocation = Paths.get(uploadDir);
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage location", e);
        }
    }

    @Transactional
    public AssessmentEvidenceFile storeAndSave(MultipartFile file, Long applicantId, String description) {
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
            Path destinationFile = this.rootLocation.resolve(Paths.get(newFilename)).normalize().toAbsolutePath();
            Files.copy(file.getInputStream(), destinationFile);

            AssessmentEvidenceFile evidenceFile = new AssessmentEvidenceFile();
            evidenceFile.setAssessmentApplicantId(applicantId);
            evidenceFile.setFilePath("/uploads/" + newFilename);
            evidenceFile.setOriginalFilename(file.getOriginalFilename());
            evidenceFile.setDescription(description);

            return evidenceFileRepository.save(evidenceFile);

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }

    @Transactional
    public void deleteFile(Long fileId) {
        AssessmentEvidenceFile fileToDelete = evidenceFileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found with id: " + fileId));

        try {
            String filename = fileToDelete.getFilePath().replace("/uploads/", "");
            Path filePath = rootLocation.resolve(filename);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.err.println("Could not delete file from disk: " + e.getMessage());
        }

        List<Long> linksToDelete = evidenceCompetencyLinkRepository.findAll().stream()
                .filter(link -> link.getEvidenceFileId().equals(fileId))
                .map(link -> link.getId())
                .collect(Collectors.toList());
        if (!linksToDelete.isEmpty()) {
            evidenceCompetencyLinkRepository.deleteAllById(linksToDelete);
        }

        evidenceFileRepository.deleteById(fileId);
    }


    public List<EvidenceFileDTO> getFilesByApplicantId(Long applicantId) {
        List<AssessmentEvidenceFile> files = evidenceFileRepository.findByAssessmentApplicantId(applicantId);

        List<EvidenceCompetencyLink> allLinks = evidenceCompetencyLinkRepository.findAllByAssessmentApplicantId(applicantId);

        return files.stream().map(fileEntity -> {
            EvidenceFileDTO dto = new EvidenceFileDTO();
            dto.setId(fileEntity.getId());
            dto.setOriginalFilename(fileEntity.getOriginalFilename());
            dto.setFilePath(fileEntity.getFilePath());
            dto.setEvidenceTypes(fileEntity.getEvidenceTypes());
            dto.setDescription(fileEntity.getDescription());

            List<String> codesForThisFile = allLinks.stream()
                    .filter(link -> link.getEvidenceFileId().equals(fileEntity.getId()))
                    .map(EvidenceCompetencyLink::getCompetencyCode)
                    .collect(Collectors.toList());
            dto.setLinkedCompetencyCodes(codesForThisFile);
            dto.setLinked(!codesForThisFile.isEmpty());

            return dto;
        }).collect(Collectors.toList());
    }
}