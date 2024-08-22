package com.dissonance.itit.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dissonance.itit.domain.entity.InfoPost;
import com.dissonance.itit.domain.entity.RecruitmentPosition;
import com.dissonance.itit.dto.common.PositionInfo;
import com.dissonance.itit.repository.RecruitmentPositionRepository;
import com.dissonance.itit.repository.RecruitmentPositionRepositorySupport;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecruitmentPositionService {
	private final RecruitmentPositionRepository recruitmentPositionRepository;
	private final RecruitmentPositionRepositorySupport recruitmentPositionRepositorySupport;

	@Transactional
	public void addPositions(InfoPost infoPost, List<PositionInfo> positionInfos) {
		positionInfos.forEach(positionInfo -> {
			RecruitmentPosition newRecruitmentPosition = RecruitmentPosition.builder()
				.infoPost(infoPost)
				.name(positionInfo.positionName())
				.recruitingCount(positionInfo.recruitingCount())
				.build();
			recruitmentPositionRepository.save(newRecruitmentPosition);
		});
	}

	public List<PositionInfo> findPositionInfosByInfoPostId(Long infoPostId) {
		return recruitmentPositionRepositorySupport.findByInfoPostId(infoPostId);
	}
}
