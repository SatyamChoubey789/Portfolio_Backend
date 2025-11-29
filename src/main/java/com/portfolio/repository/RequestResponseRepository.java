package com.portfolio.repository;

import com.portfolio.model.RequestResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RequestResponseRepository extends JpaRepository<RequestResponse, Long> {
    List<RequestResponse> findByRequestIdOrderByCreatedAtAsc(Long requestId);
}
