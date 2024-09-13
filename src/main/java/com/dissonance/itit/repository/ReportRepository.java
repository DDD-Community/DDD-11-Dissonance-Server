package com.dissonance.itit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dissonance.itit.domain.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
	boolean existsByInfoPostIdAndUserId(Long infoPostId, Long UserId);
}
