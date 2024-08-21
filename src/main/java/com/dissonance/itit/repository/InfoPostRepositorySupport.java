package com.dissonance.itit.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dissonance.itit.domain.entity.QInfoPost;
import com.dissonance.itit.domain.entity.QRecruitmentPosition;
import com.dissonance.itit.dto.response.InfoPostDetailRes;
import com.dissonance.itit.dto.response.InfoPostDetailRes.InfoPostInfo;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class InfoPostRepositorySupport {
	private final JPAQueryFactory jpaQueryFactory;
	private final QInfoPost infoPost = QInfoPost.infoPost;
	private final QRecruitmentPosition recruitmentPosition = QRecruitmentPosition.recruitmentPosition;

	public InfoPostInfo findById(Long infoPostId) {
		incrementViewCount(infoPostId);

		return jpaQueryFactory.select(Projections.constructor(InfoPostInfo.class,
				infoPost.title.as("title"),
				infoPost.category.name.as("categoryName"),
				infoPost.organization.as("organization"),
				infoPost.recruitmentStartDate.as("recruitmentStartDate"),
				infoPost.recruitmentEndDate.as("recruitmentEndDate"),
				infoPost.activityStartDate.as("activityStartDate"),
				infoPost.activityEndDate.as("activityEndDate"),
				infoPost.content.as("content"),
				infoPost.detailUrl.as("detailUrl"),
				infoPost.viewCount.as("viewCount")
			))
			.from(infoPost)
			.where(infoPost.id.eq(infoPostId))
			.fetchOne();
	}

	public List<InfoPostDetailRes.PositionInfo> findByInfoPostId(Long infoPostId) {
		return jpaQueryFactory.select(Projections.constructor(InfoPostDetailRes.PositionInfo.class,
				recruitmentPosition.name.as("positionName"),
				recruitmentPosition.recruitingCount.as("recruitingCount")
			))
			.from(recruitmentPosition)
			.where(recruitmentPosition.infoPost.id.eq(infoPostId))
			.fetch();
	}

	private void incrementViewCount(Long infoPostId) {
		jpaQueryFactory.update(infoPost)
			.set(infoPost.viewCount, infoPost.viewCount.add(1))
			.where(infoPost.id.eq(infoPostId))
			.execute();
	}
}
