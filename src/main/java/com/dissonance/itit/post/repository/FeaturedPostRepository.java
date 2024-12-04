package com.dissonance.itit.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dissonance.itit.post.domain.FeaturedPost;

public interface FeaturedPostRepository extends JpaRepository<FeaturedPost, Integer> {
}
