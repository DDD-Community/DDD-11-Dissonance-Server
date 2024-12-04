package com.dissonance.itit.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dissonance.itit.post.domain.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
	boolean existsByInfoPostIdAndUserId(Long infoPostId, Long UserId);
}
