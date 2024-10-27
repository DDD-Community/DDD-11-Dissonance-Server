package com.dissonance.itit.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dissonance.itit.domain.entity.QRecruitmentPosition;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RecruitmentPositionRepositorySupport {
	private final JPAQueryFactory jpaQueryFactory;
	private final QRecruitmentPosition recruitmentPosition = QRecruitmentPosition.recruitmentPosition;

	public List<String> findByInfoPostId(Long infoPostId) {
		return jpaQueryFactory.select(recruitmentPosition.name)
			.from(recruitmentPosition)
			.where(recruitmentPosition.infoPost.id.eq(infoPostId))
			.fetch();
	}
}
