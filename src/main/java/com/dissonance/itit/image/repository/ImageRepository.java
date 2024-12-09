package com.dissonance.itit.image.repository;

import com.dissonance.itit.image.domain.Image;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
