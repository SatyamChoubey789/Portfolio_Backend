package com.portfolio.service;

import com.portfolio.model.Project;
import com.portfolio.model.ProjectImage;
import com.portfolio.repository.ProjectRepository;
import com.portfolio.repository.ProjectImageRepository;
import com.portfolio.dto.request.ProjectRequestCreate;
import com.portfolio.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
    
    private final ProjectRepository projectRepository;
    private final ProjectImageRepository projectImageRepository;
    
    public List<Project> getAllPublishedProjects() {
        return projectRepository.findByStatusOrderByDisplayOrderAsc("PUBLISHED");
    }
    
    public List<Project> getProjectsByCategory(String category) {
        return projectRepository.findByCategoryAndStatusOrderByDisplayOrderAsc(category, "PUBLISHED");
    }
    
    public List<Project> getFeaturedProjects() {
        return projectRepository.findByFeaturedTrueAndStatusOrderByDisplayOrderAsc("PUBLISHED");
    }
    
    public Project getProjectBySlug(String slug) {
        return projectRepository.findBySlug(slug)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found: " + slug));
    }
    
    @Transactional
    public Project createProject(ProjectRequestCreate request) {
        if (projectRepository.existsBySlug(request.getSlug())) {
            throw new IllegalArgumentException("Project with slug already exists");
        }
        
        Project project = Project.builder()
            .slug(request.getSlug())
            .title(request.getTitle())
            .subtitle(request.getSubtitle())
            .description(request.getDescription())
            .coverImageUrl(request.getCoverImageUrl())
            .techStack(request.getTechStack())
            .githubUrl(request.getGithubUrl())
            .liveUrl(request.getLiveUrl())
            .category(request.getCategory())
            .featured(request.getFeatured() != null ? request.getFeatured() : false)
            .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
            .status(request.getStatus() != null ? request.getStatus() : "DRAFT")
            .build();
        
        return projectRepository.save(project);
    }
    
    @Transactional
    public Project updateProject(Long id, ProjectRequestCreate request) {
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        
        project.setTitle(request.getTitle());
        project.setSubtitle(request.getSubtitle());
        project.setDescription(request.getDescription());
        project.setCoverImageUrl(request.getCoverImageUrl());
        project.setTechStack(request.getTechStack());
        project.setGithubUrl(request.getGithubUrl());
        project.setLiveUrl(request.getLiveUrl());
        project.setCategory(request.getCategory());
        project.setFeatured(request.getFeatured() != null ? request.getFeatured() : false);
        project.setDisplayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0);
        project.setStatus(request.getStatus() != null ? request.getStatus() : "DRAFT");
        
        return projectRepository.save(project);
    }
    
    @Transactional
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project not found");
        }
        projectRepository.deleteById(id);
    }
    
    @Transactional
    public ProjectImage addProjectImage(Long projectId, String imageUrl, String caption, Integer displayOrder) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        
        ProjectImage image = ProjectImage.builder()
            .project(project)
            .imageUrl(imageUrl)
            .caption(caption)
            .displayOrder(displayOrder != null ? displayOrder : 0)
            .build();
        
        return projectImageRepository.save(image);
    }
    
    public List<ProjectImage> getProjectImages(Long projectId) {
        return projectImageRepository.findByProjectIdOrderByDisplayOrderAsc(projectId);
    }
    
    @Transactional
    public void deleteProjectImage(Long imageId) {
        projectImageRepository.deleteById(imageId);
    }
}