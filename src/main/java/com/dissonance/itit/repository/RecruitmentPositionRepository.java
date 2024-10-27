package com.dissonance.itit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dissonance.itit.domain.entity.RecruitmentPosition;

public interface RecruitmentPositionRepository extends JpaRepository<RecruitmentPosition, Integer> {
	void deleteByInfoPostId(Long infoPostId);
}
