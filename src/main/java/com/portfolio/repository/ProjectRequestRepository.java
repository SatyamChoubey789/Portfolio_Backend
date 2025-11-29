package com.portfolio.repository;

import com.portfolio.model.ProjectRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjectRequestRepository extends JpaRepository<ProjectRequest, Long> {
    List<ProjectRequest> findAllByOrderByCreatedAtDesc();
    List<ProjectRequest> findByStatusOrderByCreatedAtDesc(String status);
    List<ProjectRequest> findByPriorityOrderByCreatedAtDesc(String priority);
    List<ProjectRequest> findByStatusAndPriorityOrderByCreatedAtDesc(String status, String priority);
}