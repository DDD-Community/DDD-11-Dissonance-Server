package com.dissonance.itit.recruitmentPosition.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dissonance.itit.recruitmentPosition.domain.RecruitmentPosition;

public interface RecruitmentPositionRepository extends JpaRepository<RecruitmentPosition, Integer> {
	void deleteByInfoPostId(Long infoPostId);
}
