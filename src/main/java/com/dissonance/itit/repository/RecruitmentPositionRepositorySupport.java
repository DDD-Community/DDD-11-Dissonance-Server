package com.dissonance.itit.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dissonance.itit.domain.entity.QRecruitmentPosition;
import com.dissonance.itit.dto.common.PositionInfo;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RecruitmentPositionRepositorySupport {
	private final JPAQueryFactory jpaQueryFactory;
	private final QRecruitmentPosition recruitmentPosition = QRecruitmentPosition.recruitmentPosition;

	public List<PositionInfo> findByInfoPostId(Long infoPostId) {
		return jpaQueryFactory.select(Projections.constructor(PositionInfo.class,
				recruitmentPosition.name.as("positionName"),
				recruitmentPosition.recruitingCount.as("recruitingCount")
			))
			.from(recruitmentPosition)
			.where(recruitmentPosition.infoPost.id.eq(infoPostId))
			.fetch();
	}
}
