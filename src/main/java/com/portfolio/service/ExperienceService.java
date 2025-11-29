package com.portfolio.service;

import com.portfolio.model.Experience;
import com.portfolio.repository.ExperienceRepository;
import com.portfolio.dto.request.ExperienceRequest;
import com.portfolio.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExperienceService {
    
    private final ExperienceRepository experienceRepository;
    
    public List<Experience> getAllExperiences() {
        return experienceRepository.findAllByOrderByDisplayOrderAsc();
    }
    
    @Transactional
    public Experience createExperience(ExperienceRequest request) {
        Experience experience = Experience.builder()
            .company(request.getCompany())
            .role(request.getRole())
            .location(request.getLocation())
            .startDate(request.getStartDate())
            .endDate(request.getEndDate())
            .description(request.getDescription())
            .achievements(request.getAchievements())
            .techStack(request.getTechStack())
            .companyLogoUrl(request.getCompanyLogoUrl())
            .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
            .build();
        
        return experienceRepository.save(experience);
    }
    
    @Transactional
    public Experience updateExperience(Long id, ExperienceRequest request) {
        Experience experience = experienceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Experience not found"));
        
        experience.setCompany(request.getCompany());
        experience.setRole(request.getRole());
        experience.setLocation(request.getLocation());
        experience.setStartDate(request.getStartDate());
        experience.setEndDate(request.getEndDate());
        experience.setDescription(request.getDescription());
        experience.setAchievements(request.getAchievements());
        experience.setTechStack(request.getTechStack());
        experience.setCompanyLogoUrl(request.getCompanyLogoUrl());
        experience.setDisplayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0);
        
        return experienceRepository.save(experience);
    }
    
    @Transactional
    public void deleteExperience(Long id) {
        if (!experienceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Experience not found");
        }
        experienceRepository.deleteById(id);
    }
}
