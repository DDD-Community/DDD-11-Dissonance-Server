package com.dissonance.itit.post.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dissonance.itit.global.common.exception.CustomException;
import com.dissonance.itit.global.common.exception.ErrorCode;
import com.dissonance.itit.post.domain.InfoPost;
import com.dissonance.itit.post.domain.Report;
import com.dissonance.itit.post.repository.ReportRepository;
import com.dissonance.itit.user.domain.User;

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
