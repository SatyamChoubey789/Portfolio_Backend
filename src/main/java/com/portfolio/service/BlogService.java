package com.portfolio.service;

import com.portfolio.model.BlogPost;
import com.portfolio.repository.BlogRepository;
import com.portfolio.dto.request.BlogPostRequest;
import com.portfolio.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {
    
    private final BlogRepository blogRepository;
    
    public Page<BlogPost> getPublishedPosts(Pageable pageable) {
        return blogRepository.findByStatusOrderByPublishedAtDesc("PUBLISHED", pageable);
    }
    
    public Page<BlogPost> getPostsByTag(String tag, Pageable pageable) {
        return blogRepository.findByTagAndStatus(tag, pageable);
    }
    
    public List<BlogPost> getFeaturedPosts() {
        return blogRepository.findByFeaturedTrueAndStatusOrderByPublishedAtDesc("PUBLISHED");
    }
    
    public BlogPost getPostBySlug(String slug) {
        return blogRepository.findBySlug(slug)
            .orElseThrow(() -> new ResourceNotFoundException("Blog post not found: " + slug));
    }
    
    @Transactional
    public void incrementViews(String slug) {
        BlogPost post = getPostBySlug(slug);
        post.setViews(post.getViews() + 1);
        blogRepository.save(post);
    }
    
    @Transactional
    public BlogPost createPost(BlogPostRequest request) {
        if (blogRepository.existsBySlug(request.getSlug())) {
            throw new IllegalArgumentException("Post with slug already exists");
        }
        
        BlogPost post = BlogPost.builder()
            .slug(request.getSlug())
            .title(request.getTitle())
            .excerpt(request.getExcerpt())
            .content(request.getContent())
            .coverImageUrl(request.getCoverImageUrl())
            .tags(request.getTags())
            .readTimeMinutes(request.getReadTimeMinutes())
            .featured(request.getFeatured() != null ? request.getFeatured() : false)
            .status(request.getStatus() != null ? request.getStatus() : "DRAFT")
            .views(0)
            .build();
        
        return blogRepository.save(post);
    }
    
    @Transactional
    public BlogPost updatePost(Long id, BlogPostRequest request) {
        BlogPost post = blogRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Blog post not found"));
        
        post.setTitle(request.getTitle());
        post.setExcerpt(request.getExcerpt());
        post.setContent(request.getContent());
        post.setCoverImageUrl(request.getCoverImageUrl());
        post.setTags(request.getTags());
        post.setReadTimeMinutes(request.getReadTimeMinutes());
        post.setFeatured(request.getFeatured() != null ? request.getFeatured() : false);
        post.setStatus(request.getStatus() != null ? request.getStatus() : "DRAFT");
        
        return blogRepository.save(post);
    }
    
    @Transactional
    public BlogPost publishPost(Long id) {
        BlogPost post = blogRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Blog post not found"));
        
        post.setStatus("PUBLISHED");
        post.setPublishedAt(LocalDateTime.now());
        
        return blogRepository.save(post);
    }
    
    @Transactional
    public void deletePost(Long id) {
        if (!blogRepository.existsById(id)) {
            throw new ResourceNotFoundException("Blog post not found");
        }
        blogRepository.deleteById(id);
    }
}