package com.TPQI.thai2learn.controllers;

import com.TPQI.thai2learn.entities.tpqi_asm.ReskSettingsFileExtension;
import com.TPQI.thai2learn.services.ReskSettingsFileExtensionService;

import com.TPQI.thai2learn.DTO.EmailTemplateRequestDTO;
import com.TPQI.thai2learn.DTO.EmailTemplateResponseDTO;
import com.TPQI.thai2learn.DTO.EmailTemplateShortResponseDTO;
import com.TPQI.thai2learn.services.ReskSettingsEmailTemplateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/SystemSetting")
public class SystemSettingsController {

    private final ReskSettingsFileExtensionService fileExtensionService;
    private final ReskSettingsEmailTemplateService emailTemplateService;

    @Autowired
    public SystemSettingsController(
            ReskSettingsFileExtensionService fileExtensionService,
            ReskSettingsEmailTemplateService emailTemplateService
    ) {
        this.fileExtensionService = fileExtensionService;
        this.emailTemplateService = emailTemplateService;
    }

    // ==============================
    // File Extension APIs
    // ==============================

    @GetMapping("/file-extensions/ShowFileExtensionById/{ext_id}")
    public ResponseEntity<ReskSettingsFileExtension> getFileExtensionById(@PathVariable("ext_id") Integer id) {
        return fileExtensionService.getFileExtensionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/file-extensions/ShowAllFileExtension")
    public ResponseEntity<List<ReskSettingsFileExtension>> getAllFileExtensions() {
        List<ReskSettingsFileExtension> extensions = fileExtensionService.getAllFileExtensions();
        return extensions.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(extensions);
    }

    @PutMapping("/file-extensions/UpdateFileExtension/{ext_id}")
    public ResponseEntity<ReskSettingsFileExtension> updateFileExtension(
            @PathVariable("ext_id") Integer id,
            @RequestBody ReskSettingsFileExtension updatedFileExt) {
        if (!fileExtensionService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        updatedFileExt.setFileExtId(id);
        ReskSettingsFileExtension saved = fileExtensionService.updateFileExtension(updatedFileExt);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/file-extensions/DeleteFileExtensionById/{ext_id}")
    public ResponseEntity<Void> deleteFileExtension(@PathVariable("ext_id") Integer id) {
        if (!fileExtensionService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        fileExtensionService.deleteFileExtension(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/file-extensions/AddFileExtension")
    public ResponseEntity<ReskSettingsFileExtension> createFileExtension(
            @RequestBody ReskSettingsFileExtension newFileExt) {
        ReskSettingsFileExtension saved = fileExtensionService.updateFileExtension(newFileExt);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ==============================
    // Email Template APIs
    // ==============================

   @GetMapping("/email-templates/ShowAllEmailTemplate")
    public ResponseEntity<List<EmailTemplateShortResponseDTO>> getAllTemplates() {
    List<EmailTemplateShortResponseDTO> templates = emailTemplateService.getAllTemplatesShort();
    return templates.isEmpty()
            ? ResponseEntity.noContent().build()
            : ResponseEntity.ok(templates);
}

    @GetMapping("/email-templates/ShowEmailTemplateById/{et_id}")
    public ResponseEntity<EmailTemplateResponseDTO> getTemplateById(@PathVariable("et_id") Integer id) {
        EmailTemplateResponseDTO dto = emailTemplateService.getTemplateById(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @PostMapping("/email-templates/AddEmailTemplate")
    public ResponseEntity<EmailTemplateResponseDTO> createTemplate(
            @RequestBody EmailTemplateRequestDTO requestDTO) {
        EmailTemplateResponseDTO saved = emailTemplateService.saveTemplate(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/email-templates/UpdateEmailTemplate/{et_id}")
    public ResponseEntity<EmailTemplateResponseDTO> updateTemplate(
            @PathVariable("et_id") Integer id,
            @RequestBody EmailTemplateRequestDTO requestDTO) {
        EmailTemplateResponseDTO updated = emailTemplateService.updateTemplate(id, requestDTO);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/email-templates/DeleteEmailTemplate/{et_id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable("et_id") Integer id) {
        if (!emailTemplateService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        emailTemplateService.deleteTemplate(id);
        return ResponseEntity.noContent().build();
    }
}
