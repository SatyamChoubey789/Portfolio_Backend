package com.portfolio.repository;

import com.portfolio.model.BlogPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BlogRepository extends JpaRepository<BlogPost, Long> {
    Optional<BlogPost> findBySlug(String slug);
    Page<BlogPost> findByStatusOrderByPublishedAtDesc(String status, Pageable pageable);
    
    // FIXED: Use native SQL query for PostgreSQL array operations
    @Query(value = "SELECT * FROM blog_posts WHERE :tag = ANY(tags) AND status = 'PUBLISHED' ORDER BY published_at DESC",
           countQuery = "SELECT COUNT(*) FROM blog_posts WHERE :tag = ANY(tags) AND status = 'PUBLISHED'",
           nativeQuery = true)
    Page<BlogPost> findByTagAndStatus(@Param("tag") String tag, Pageable pageable);
    
    List<BlogPost> findByFeaturedTrueAndStatusOrderByPublishedAtDesc(String status);
    boolean existsBySlug(String slug);
}
