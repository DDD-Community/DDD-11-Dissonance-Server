package com.dissonance.itit.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dissonance.itit.post.domain.InfoPost;

public interface InfoPostRepository extends JpaRepository<InfoPost, Long> {
}
