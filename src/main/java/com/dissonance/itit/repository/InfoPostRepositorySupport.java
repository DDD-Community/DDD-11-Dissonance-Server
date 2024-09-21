package com.dissonance.itit.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.dissonance.itit.domain.entity.QInfoPost;
import com.dissonance.itit.dto.response.InfoPostDetailRes.InfoPostInfo;
import com.dissonance.itit.dto.response.InfoPostRes;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class InfoPostRepositorySupport {
	private final JPAQueryFactory jpaQueryFactory;
	private final QInfoPost infoPost = QInfoPost.infoPost;

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
				infoPost.viewCount.as("viewCount"),
				infoPost.reported.as("reported"),
				infoPost.image.imageUrl.as("imageUrl")
			))
			.from(infoPost)
			.where(infoPost.id.eq(infoPostId))
			.fetchOne();
	}

	private void incrementViewCount(Long infoPostId) {
		jpaQueryFactory.update(infoPost)
			.set(infoPost.viewCount, infoPost.viewCount.add(1))
			.where(infoPost.id.eq(infoPostId))
			.execute();
	}

	public Page<InfoPostRes> findInfoPostsByCategoryId(Integer categoryId, Pageable pageable) {
		List<InfoPostRes.InfoPostInfo> postInfos = jpaQueryFactory.select(
				Projections.constructor(InfoPostRes.InfoPostInfo.class,
					infoPost.id,
					infoPost.image.imageUrl,
					infoPost.title,
					infoPost.recruitmentEndDate))
			.from(infoPost)
			.where(createCondition(categoryId))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(getOrderSpecifiers(pageable.getSort()))
			.fetch();

		return paginationInfoPosts(categoryId, InfoPostRes.of(postInfos), pageable);
	}

	public OrderSpecifier<?>[] getOrderSpecifiers(Sort sort) {
		List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

		for (Sort.Order order : sort) {
			OrderSpecifier<?> orderSpecifier = null;

			switch (order.getProperty()) {
				case "latest":
					orderSpecifier = new OrderSpecifier<>(Order.DESC, infoPost.id);
					break;
				case "deadline":
					// recruitmentEndDate가 현재 날짜보다 크거나 같은 게시글을 먼저 정렬
					LocalDate currentDate = LocalDate.now();

					// 정렬 기준 추가: 현재 날짜보다 크거나 같은 날짜는 우선순위가 높음
					OrderSpecifier<?> prioritySort = new OrderSpecifier<>(
						Order.DESC,
						Expressions.booleanTemplate("CASE WHEN {0} >= {1} THEN 1 ELSE 0 END",
							infoPost.recruitmentEndDate, currentDate)
					);

					// 기본 정렬: recruitmentEndDate 내림차순
					OrderSpecifier<?> dateSort = new OrderSpecifier<>(Order.DESC, infoPost.recruitmentEndDate);

					// 두 개의 정렬 기준을 조합
					orderSpecifiers.add(prioritySort);
					orderSpecifiers.add(dateSort);
					break;
			}

			if (orderSpecifier != null) {
				orderSpecifiers.add(orderSpecifier);
			}
		}

		if (!orderSpecifiers.isEmpty()) {
			return orderSpecifiers.toArray(new OrderSpecifier<?>[0]);
		} else {
			return new OrderSpecifier<?>[0];
		}
	}

	private Page<InfoPostRes> paginationInfoPosts(Integer categoryId, List<InfoPostRes> infoPostRes,
		Pageable pageable) {
		JPAQuery<Long> countQuery = getCountQuery(categoryId);

		return PageableExecutionUtils.getPage(infoPostRes, pageable, countQuery::fetchOne);
	}

	private JPAQuery<Long> getCountQuery(Integer categoryId) {
		return jpaQueryFactory.select(infoPost.id.count())
			.from(infoPost)
			.where(createCondition(categoryId));
	}

	private BooleanExpression createCondition(Integer categoryId) {
		BooleanExpression condition = infoPost.category.id.eq(categoryId);

		if (categoryId == 1) {
			condition = infoPost.category.parent.id.eq(categoryId);
		}

		return condition;
	}
}
