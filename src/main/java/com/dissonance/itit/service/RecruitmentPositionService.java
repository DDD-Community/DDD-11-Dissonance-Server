package com.dissonance.itit.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dissonance.itit.domain.entity.InfoPost;
import com.dissonance.itit.domain.entity.RecruitmentPosition;
import com.dissonance.itit.repository.RecruitmentPositionRepository;
import com.dissonance.itit.repository.RecruitmentPositionRepositorySupport;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecruitmentPositionService {
	private final RecruitmentPositionRepository recruitmentPositionRepository;
	private final RecruitmentPositionRepositorySupport recruitmentPositionRepositorySupport;

	@Transactional
	public void addPositions(InfoPost infoPost, List<String> positionInfos) {
		List<RecruitmentPosition> recruitmentPositions = positionInfos.stream()
			.map(positionInfo ->
				RecruitmentPosition.builder()
					.infoPost(infoPost)
					.name(positionInfo)
					.build()
			).toList();
		recruitmentPositionRepository.saveAll(recruitmentPositions);
	}

	public List<String> findPositionInfosByInfoPostId(Long infoPostId) {
		return recruitmentPositionRepositorySupport.findByInfoPostId(infoPostId);
	}

	@Transactional
	public void updatePositions(InfoPost infoPost, List<String> positionInfos) {
		// TODO: 전부 삭제 후 재생성 -> 기존 상태에 따라 infoPostId와 name이 같은 경우 그대로 두고, 삭제 혹은 생성된 경우만 처리하도록 변경
		recruitmentPositionRepository.deleteByInfoPostId(infoPost.getId());
		addPositions(infoPost, positionInfos);
	}
}
