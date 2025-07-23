package com.TPQI.thai2learn.services;

import com.TPQI.thai2learn.DTO.AssessmentFilterOptionsDTO;
import com.TPQI.thai2learn.DTO.AssessmentInfoDTO;
import com.TPQI.thai2learn.repositories.tpqi_asm.AssessmentFilterRepository;
import com.TPQI.thai2learn.repositories.tpqi_asm.AssessmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final AssessmentFilterRepository assessmentFilterRepository;

    public AssessmentService(AssessmentRepository assessmentRepository, AssessmentFilterRepository assessmentFilterRepository) {
        this.assessmentRepository = assessmentRepository;
        this.assessmentFilterRepository = assessmentFilterRepository;
    }

    @Transactional(readOnly = true)
    public Page<AssessmentInfoDTO> getAssessmentsByAppId(String appId, String search, Pageable pageable) {
        if (appId == null || appId.trim().isEmpty()) {
            return Page.empty(); 
        }
        return assessmentRepository.findAssessmentInfoByAppId(appId, search, pageable);
    }

    @Transactional(readOnly = true)
    public AssessmentFilterOptionsDTO getFilterOptionsByAppId(String appId) { 
        AssessmentFilterOptionsDTO options = new AssessmentFilterOptionsDTO();

        List<String> allOccLevelNames = assessmentFilterRepository.findDistinctOccLevelNamesByAppId(appId); 

        Set<String> professions = new HashSet<>();
        Set<String> branches = new HashSet<>();
        Set<String> occupations = new HashSet<>();
        Set<String> levels = new HashSet<>();

        for (String occLevelName : allOccLevelNames) {
            if (occLevelName != null && !occLevelName.isEmpty()) {
                try {
                    String textToParse = occLevelName;
                    String currentProfession = textToParse;
                    String currentBranch = "ไม่มีสาขา";
                    String currentOccupation = null;
                    String currentLevel = null;

                    String[] levelParts = textToParse.split("ระดับ", 2);
                    if (levelParts.length > 1) {
                        currentLevel = levelParts[1].trim();
                        textToParse = levelParts[0].trim();
                    }
                    String[] occupationParts = textToParse.split("อาชีพ", 2);
                    if (occupationParts.length > 1) {
                        currentOccupation = occupationParts[1].trim();
                        textToParse = occupationParts[0].trim();
                    }
                    int lastBranchIndex = textToParse.lastIndexOf("สาขา");
                    if (lastBranchIndex > 0) {
                        currentProfession = textToParse.substring(0, lastBranchIndex).trim();
                        currentBranch = textToParse.substring(lastBranchIndex).trim();
                    } else {
                        currentProfession = textToParse.trim();
                    }
                    
                    professions.add(currentProfession);
                    branches.add(currentBranch);
                    if (currentOccupation != null) occupations.add(currentOccupation);
                    if (currentLevel != null) levels.add(currentLevel);

                } catch (Exception e) {
                    System.err.println("Could not parse occ_level_name for filter: " + occLevelName + "; Error: " + e.getMessage());
                }
            }
        }
        
        options.setProfessions(new ArrayList<>(professions));
        options.setBranches(new ArrayList<>(branches));
        options.setOccupations(new ArrayList<>(occupations));
        options.setLevels(new ArrayList<>(levels));
        Collections.sort(options.getProfessions());
        Collections.sort(options.getBranches());
        Collections.sort(options.getOccupations());
        Collections.sort(options.getLevels());

        options.setAssessmentTools(assessmentFilterRepository.findDistinctAssessmentToolsByAppId(appId));

        return options;
    }
}