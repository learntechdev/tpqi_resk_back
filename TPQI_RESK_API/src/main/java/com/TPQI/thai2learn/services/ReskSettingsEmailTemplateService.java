package com.TPQI.thai2learn.services;

import com.TPQI.thai2learn.DTO.EmailTemplateRequestDTO;
import com.TPQI.thai2learn.DTO.EmailTemplateResponseDTO;
import com.TPQI.thai2learn.DTO.EmailTemplateShortResponseDTO;
import com.TPQI.thai2learn.entities.tpqi_asm.ReskSettingsEmailTemplate;
import com.TPQI.thai2learn.repositories.tpqi_asm.ReskSettingsEmailTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReskSettingsEmailTemplateService {

    @Autowired
    private ReskSettingsEmailTemplateRepository repository;

    public EmailTemplateResponseDTO saveTemplate(EmailTemplateRequestDTO dto) {
        ReskSettingsEmailTemplate entity = new ReskSettingsEmailTemplate();
        entity.setEtName(dto.getEtName());
        entity.setEtDescription(dto.getEtDescription());
        entity.setEtSubject(dto.getEtSubject());
        entity.setEtEmailContent(dto.getEtEmailContent());
        entity.setCreatedAt(java.time.LocalDateTime.now());

        ReskSettingsEmailTemplate saved = repository.save(entity);
        return convertToResponseDTO(saved);
    }

    public EmailTemplateResponseDTO updateTemplate(int id, EmailTemplateRequestDTO dto) {
        Optional<ReskSettingsEmailTemplate> optionalEntity = repository.findById(id);
        if (optionalEntity.isEmpty()) {
            return null;
        }

        ReskSettingsEmailTemplate entity = optionalEntity.get();
        entity.setEtName(dto.getEtName());
        entity.setEtDescription(dto.getEtDescription());
        entity.setEtSubject(dto.getEtSubject());       
        entity.setEtStatus(dto.getEtStatus());
        entity.setEtEmailContent(dto.getEtEmailContent());
        entity.setUpdatedAt(java.time.LocalDateTime.now());

        ReskSettingsEmailTemplate updated = repository.save(entity);
        return convertToResponseDTO(updated);
    }

    public List<EmailTemplateResponseDTO> getAllTemplates() {
        return repository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public EmailTemplateResponseDTO getTemplateById(Integer id) {
        return repository.findById(id)
                .map(this::convertToResponseDTO)
                .orElse(null);
    }

    public void deleteTemplate(Integer id) {
        repository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    private EmailTemplateResponseDTO convertToResponseDTO(ReskSettingsEmailTemplate entity) {
        EmailTemplateResponseDTO dto = new EmailTemplateResponseDTO();
        dto.setEtId(entity.getEtId());
        dto.setEtName(entity.getEtName());
        dto.setEtDescription(entity.getEtDescription());
        dto.setEtSubject(entity.getEtSubject());
        dto.setEtEmailContent(entity.getEtEmailContent());
        dto.setEtStatus(entity.getEtStatus());
        return dto;
    }

    public List<EmailTemplateShortResponseDTO> getAllTemplatesShort() {
    return repository.findAll().stream().map(entity -> {
        EmailTemplateShortResponseDTO dto = new EmailTemplateShortResponseDTO();
        dto.setEtId(entity.getEtId());
        dto.setEtName(entity.getEtName());
        dto.setEtDescription(entity.getEtDescription());
        dto.setEtSubject(entity.getEtSubject());
        dto.setEtEmailContent(entity.getEtEmailContent());
        dto.setEtStatus(entity.getEtStatus());
        return dto;
    }).collect(Collectors.toList());
}
}
