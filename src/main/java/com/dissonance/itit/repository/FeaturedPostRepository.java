package com.dissonance.itit.repository;

import com.dissonance.itit.domain.entity.FeaturedPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeaturedPostRepository extends JpaRepository<FeaturedPost, Integer> {
}
