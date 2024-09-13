package com.dissonance.itit.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dissonance.itit.common.exception.CustomException;
import com.dissonance.itit.common.exception.ErrorCode;
import com.dissonance.itit.domain.entity.InfoPost;
import com.dissonance.itit.domain.entity.Report;
import com.dissonance.itit.domain.entity.User;
import com.dissonance.itit.repository.ReportRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {
	private final ReportRepository reportRepository;

	private final InfoPostService infoPostService;

	@Transactional
	public Long reportedInfoPost(Long infoPostId, User user) {
		InfoPost infoPost = infoPostService.findById(infoPostId);

		if (reportRepository.existsByInfoPostIdAndUserId(infoPostId, user.getId())) {
			throw new CustomException(ErrorCode.ALREADY_REPORTED_POST);
		}

		Report report = Report.builder()
			.infoPost(infoPost)
			.user(user)
			.build();
		reportRepository.save(report);

		return infoPost.getId();
	}
}
