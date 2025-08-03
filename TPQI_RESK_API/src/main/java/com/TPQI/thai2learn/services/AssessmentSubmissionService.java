package com.TPQI.thai2learn.services;

import com.TPQI.thai2learn.DTO.*;
import com.TPQI.thai2learn.entities.tpqi_asm.AssessmentApplicant;
import com.TPQI.thai2learn.entities.tpqi_asm.AssessmentSubmissionDetails;
import com.TPQI.thai2learn.entities.tpqi_asm.EvidenceCompetencyLink;
import com.TPQI.thai2learn.repositories.tpqi_asm.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.TPQI.thai2learn.entities.tpqi_asm.ReskRequestedEvidence;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssessmentSubmissionService {

    @Autowired
    private AssessmentApplicantRepository assessmentApplicantRepository;
    @Autowired
    private AssessmentSubmissionDetailsRepository submissionDetailsRepository;
    @Autowired
    private EvidenceCompetencyLinkRepository evidenceCompetencyLinkRepository;
    @Autowired
    private CompetencyService competencyService;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private CompetencyRepository competencyRepository;
    @Autowired
    private AssessmentPriorCertificateRepository priorCertificateRepository;
    @Autowired
    private ReskRequestedEvidenceRepository requestedEvidenceRepository;

    @Transactional(readOnly = true)
    public AssessmentSubmissionPageDTO getSubmissionPageDetails(Long assessmentApplicantId) {
        AssessmentApplicant applicant = assessmentApplicantRepository.findById(assessmentApplicantId)
                .orElseThrow(() -> new RuntimeException("ไม่พบ Applicant ID: " + assessmentApplicantId));

        String tpqiExamNo = applicant.getExamScheduleId();
        Long examSchedulePkId = competencyRepository.findExamScheduleIdByTpqiExamNo(tpqiExamNo);

        if (examSchedulePkId == null) {
            throw new RuntimeException("ไม่พบรอบสอบที่ตรงกันสำหรับ tpqi_exam_no: " + tpqiExamNo);
        }

        String fullNameThai = applicant.getInitialName() + applicant.getName() + " " + applicant.getLastname();


        Optional<AssessmentSubmissionDetails> detailsOpt = submissionDetailsRepository.findByAssessmentApplicantId(assessmentApplicantId);
        Integer experienceYears = detailsOpt.map(AssessmentSubmissionDetails::getExperienceYears).orElse(null);

        List<UocDTO> competencyTree = competencyService.getCompetencyTreeByExamScheduleId(String.valueOf(examSchedulePkId));
        List<RelatedQualificationDTO> relatedQualifications = competencyService.getRelatedQualifications(String.valueOf(examSchedulePkId));

        List<String> linkedCompetencyCodes = evidenceCompetencyLinkRepository
                .findAllByAssessmentApplicantId(assessmentApplicantId).stream()
                .map(EvidenceCompetencyLink::getCompetencyCode)
                .collect(Collectors.toList());

        updateCompetencyTreeLinks(competencyTree, linkedCompetencyCodes);

        Map<Long, EvidenceFileDTO> fileMap = fileStorageService.getFilesByApplicantId(assessmentApplicantId)
                .stream().collect(Collectors.toMap(EvidenceFileDTO::getId, Function.identity()));

        List<PriorCertificateDTO> savedPriorCertificates = priorCertificateRepository
                .findAllByAssessmentApplicantId(assessmentApplicantId).stream()
                .map(certEntity -> {
                    PriorCertificateDTO dto = new PriorCertificateDTO();
                    dto.setQualificationId(certEntity.getQualificationId());
                    dto.setFileId(certEntity.getEvidenceFileId());

                    EvidenceFileDTO fileInfo = fileMap.get(certEntity.getEvidenceFileId());
                    if (fileInfo != null) {
                        dto.setFileName(fileInfo.getOriginalFilename());
                        dto.setFilePath(fileInfo.getFilePath());
                    }
                    
                    return dto;
                })
                .collect(Collectors.toList());

        List<UocDTO> unlinkedCompetencyTree = competencyTree.stream()
            .map(uoc -> {
                List<EocDTO> filteredEocs = uoc.getElementsOfCompetency().stream()
                    .map(eoc -> {
                        List<PcDTO> filteredPcs = eoc.getPerformanceCriteria().stream()
                            .filter(pc -> !pc.isEvidenceLinked())
                            .collect(Collectors.toList());
                        if (!eoc.isEvidenceLinked() || !filteredPcs.isEmpty()) {
                            EocDTO newEoc = new EocDTO(eoc.getEocId(), eoc.getEocCode(), eoc.getEocName(), filteredPcs);
                            newEoc.setParentUocId(eoc.getParentUocId());
                            return newEoc;
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
                if (!uoc.isEvidenceLinked() || !filteredEocs.isEmpty()) {
                    return new UocDTO(uoc.getUocId(), uoc.getUocCode(), uoc.getUocName(), filteredEocs);
                }
                return null;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        List<UnlinkedUocDTO> simplifiedUnlinkedTree = convertToSimplifiedTree(unlinkedCompetencyTree);

        AssessmentSubmissionPageDTO pageDTO = new AssessmentSubmissionPageDTO();
        pageDTO.setAssessmentApplicantId(assessmentApplicantId);
        pageDTO.setApplicationCode(applicant.getAppId());
        pageDTO.setFullNameThai(fullNameThai);
        pageDTO.setProfessionName(applicant.getOccLevelName());
        pageDTO.setExperienceYears(experienceYears);
        pageDTO.setEvidenceFiles(fileStorageService.getFilesByApplicantId(assessmentApplicantId));
        pageDTO.setCompetencyTree(competencyTree);
        pageDTO.setRelatedQualifications(relatedQualifications);
        pageDTO.setSavedPriorCertificates(savedPriorCertificates);
        pageDTO.setHasPriorCertificate(!savedPriorCertificates.isEmpty());
        pageDTO.setUnlinkedCompetencies(simplifiedUnlinkedTree);

        List<ReskRequestedEvidence> requestedList = requestedEvidenceRepository.findAllByAssessmentApplicantId(assessmentApplicantId);

        Map<String, String> uocCodeToNameMap = competencyTree.stream()
            .collect(Collectors.toMap(UocDTO::getUocCode, UocDTO::getUocName, (a, b) -> a));

        List<RequestedEvidenceInfoDTO> requestedInfoList = requestedList.stream()
            .map(req -> new RequestedEvidenceInfoDTO(
                req.getUocCode(),
                uocCodeToNameMap.getOrDefault(req.getUocCode(), req.getUocCode()),
                req.getDetails()
            ))
            .collect(Collectors.toList());

        pageDTO.setRequestedEvidences(requestedInfoList);
          
        return pageDTO;
    }

    private void updateCompetencyTreeLinks(List<UocDTO> uocList, List<String> linkedCodes) {
        if (uocList == null || linkedCodes == null || linkedCodes.isEmpty()) {
            return;
        }
        for (UocDTO uoc : uocList) {
            if (linkedCodes.contains(uoc.getUocCode())) {
                uoc.setEvidenceLinked(true);
            }
            if (uoc.getElementsOfCompetency() != null) {
                for (EocDTO eoc : uoc.getElementsOfCompetency()) {
                    if (linkedCodes.contains(eoc.getEocCode())) {
                        eoc.setEvidenceLinked(true);
                    }
                    if (eoc.getPerformanceCriteria() != null) {
                        for (PcDTO pc : eoc.getPerformanceCriteria()) {
                            if (linkedCodes.contains(pc.getPcId())) {
                                pc.setEvidenceLinked(true);
                            }
                        }
                    }
                }
            }
        }
    }

    private List<UnlinkedUocDTO> convertToSimplifiedTree(List<UocDTO> uocTree) {
        if (uocTree == null) {
            return null;
        }
        return uocTree.stream()
            .map(uoc -> {
                UnlinkedUocDTO newUoc = new UnlinkedUocDTO();
                newUoc.setUocId(uoc.getUocId());
                newUoc.setUocCode(uoc.getUocCode());
                newUoc.setUocName(uoc.getUocName());

                if (uoc.getElementsOfCompetency() != null) {
                    List<UnlinkedEocDTO> newEocs = uoc.getElementsOfCompetency().stream()
                        .map(eoc -> {
                            UnlinkedEocDTO newEoc = new UnlinkedEocDTO();
                            newEoc.setEocId(eoc.getEocId());
                            newEoc.setEocCode(eoc.getEocCode());
                            newEoc.setEocName(eoc.getEocName());
                            newEoc.setParentUocId(eoc.getParentUocId());

                            if (eoc.getPerformanceCriteria() != null) {
                                List<UnlinkedPcDTO> newPcs = eoc.getPerformanceCriteria().stream()
                                    .map(pc -> {
                                        UnlinkedPcDTO newPc = new UnlinkedPcDTO();
                                        newPc.setPcId(pc.getPcId());
                                        newPc.setPcName(pc.getPcName());
                                        newPc.setParentEocId(pc.getParentEocId());
                                        return newPc;
                                    }).collect(Collectors.toList());
                                newEoc.setPerformanceCriteria(newPcs);
                            }
                            return newEoc;
                        }).collect(Collectors.toList());
                    newUoc.setElementsOfCompetency(newEocs);
                }
                return newUoc;
            }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UnlinkedUocDTO> getUnlinkedCompetencyTree(Long assessmentApplicantId) {

        AssessmentApplicant applicant = assessmentApplicantRepository.findById(assessmentApplicantId)
                .orElseThrow(() -> new RuntimeException("ไม่พบ Applicant ID: " + assessmentApplicantId));

        String tpqiExamNo = applicant.getExamScheduleId();
        Long examSchedulePkId = competencyRepository.findExamScheduleIdByTpqiExamNo(tpqiExamNo);

        if (examSchedulePkId == null) {
            throw new RuntimeException("ไม่พบรอบสอบที่ตรงกันสำหรับ tpqi_exam_no: " + tpqiExamNo);
        }

        List<UocDTO> competencyTree = competencyService.getCompetencyTreeByExamScheduleId(String.valueOf(examSchedulePkId));

        List<String> linkedCompetencyCodes = evidenceCompetencyLinkRepository
                .findAllByAssessmentApplicantId(assessmentApplicantId).stream()
                .map(EvidenceCompetencyLink::getCompetencyCode)
                .collect(Collectors.toList());

        updateCompetencyTreeLinks(competencyTree, linkedCompetencyCodes);

        List<UocDTO> unlinkedCompetencyTree = competencyTree.stream()
            .map(uoc -> {
                List<EocDTO> filteredEocs = uoc.getElementsOfCompetency().stream()
                    .map(eoc -> {
                        List<PcDTO> filteredPcs = eoc.getPerformanceCriteria().stream()
                            .filter(pc -> !pc.isEvidenceLinked())
                            .collect(Collectors.toList());

                        if (!eoc.isEvidenceLinked() || !filteredPcs.isEmpty()) {
                            EocDTO newEoc = new EocDTO(eoc.getEocId(), eoc.getEocCode(), eoc.getEocName(), filteredPcs);
                            newEoc.setParentUocId(eoc.getParentUocId());
                            return newEoc;
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

                if (!uoc.isEvidenceLinked() || !filteredEocs.isEmpty()) {
                    return new UocDTO(uoc.getUocId(), uoc.getUocCode(), uoc.getUocName(), filteredEocs);
                }
                return null;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        return convertToSimplifiedTree(unlinkedCompetencyTree);
    }

    
}
