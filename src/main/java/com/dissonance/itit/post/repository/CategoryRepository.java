package com.dissonance.itit.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dissonance.itit.post.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
