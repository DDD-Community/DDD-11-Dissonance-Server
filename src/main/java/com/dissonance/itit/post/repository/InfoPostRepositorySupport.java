package com.dissonance.itit.post.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.dissonance.itit.post.domain.QInfoPost;
import com.dissonance.itit.post.dto.response.InfoPostDetailRes.InfoPostInfo;
import com.dissonance.itit.post.dto.response.InfoPostRes;
import com.dissonance.itit.post.dto.response.InfoPostUpdateRes;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class InfoPostRepositorySupport {
	private final JPAQueryFactory jpaQueryFactory;
	private final QInfoPost infoPost = QInfoPost.infoPost;

	public InfoPostInfo findInfoPostWithDetails(Long infoPostId) {
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
					infoPost.recruitmentEndDate,
					infoPost.viewCount))
			.from(infoPost)
			.where(buildCategoryCondition(categoryId))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(buildOrderSpecifiers(pageable.getSort()))
			.fetch();

		return paginateInfoPostsByCategory(categoryId, InfoPostRes.of(postInfos), pageable);
	}

	public OrderSpecifier<?>[] buildOrderSpecifiers(Sort sort) {
		List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

		for (Sort.Order order : sort) {
			OrderSpecifier<?> idSort = null;

			LocalDate currentDate = LocalDate.now();

			OrderSpecifier<?> prioritySort = new OrderSpecifier<>(
				Order.DESC,
				Expressions.booleanTemplate("CASE WHEN {0} >= {1} THEN 1 ELSE 0 END",
					infoPost.recruitmentEndDate, currentDate)
			);

			orderSpecifiers.add(prioritySort);

			switch (order.getProperty()) {
				case "latest":
					idSort = new OrderSpecifier<>(Order.DESC, infoPost.id);
					orderSpecifiers.add(idSort);
					break;
				case "deadline":
					OrderSpecifier<?> dateSort = new OrderSpecifier<>(Order.ASC, infoPost.recruitmentEndDate);
					orderSpecifiers.add(dateSort);
					break;
			}

			if (idSort != null) {
				orderSpecifiers.add(idSort);
			}
		}

		if (!orderSpecifiers.isEmpty()) {
			return orderSpecifiers.toArray(new OrderSpecifier<?>[0]);
		} else {
			return new OrderSpecifier<?>[0];
		}
	}

	private Page<InfoPostRes> paginateInfoPostsByCategory(Integer categoryId, List<InfoPostRes> infoPostRes,
		Pageable pageable) {
		JPAQuery<Long> countQuery = getCountQueryByCategory(categoryId);

		return PageableExecutionUtils.getPage(infoPostRes, pageable, countQuery::fetchOne);
	}

	private JPAQuery<Long> getCountQueryByCategory(Integer categoryId) {
		return jpaQueryFactory.select(infoPost.id.count())
			.from(infoPost)
			.where(buildCategoryCondition(categoryId));
	}

	private BooleanExpression buildCategoryCondition(Integer categoryId) {
		BooleanExpression condition = infoPost.category.id.eq(categoryId);

		if (categoryId == 1) {
			condition = infoPost.category.parent.id.eq(categoryId);
		}

		return condition;
	}

	public InfoPostUpdateRes.InfoPostInfo findInfoPostDetailsForUpdate(Long infoPostId) {
		return jpaQueryFactory.select(Projections.constructor(InfoPostUpdateRes.InfoPostInfo.class,
				infoPost.title.as("title"),
				infoPost.category.id.as("categoryId"),
				infoPost.organization.as("organization"),
				infoPost.recruitmentStartDate.as("recruitmentStartDate"),
				infoPost.recruitmentEndDate.as("recruitmentEndDate"),
				infoPost.activityStartDate.as("activityStartDate"),
				infoPost.activityEndDate.as("activityEndDate"),
				infoPost.content.as("content"),
				infoPost.detailUrl.as("detailUrl"),
				infoPost.image.imageUrl.as("imageUrl")
			))
			.from(infoPost)
			.where(infoPost.id.eq(infoPostId))
			.fetchOne();
	}

	public Page<InfoPostRes> findInfoPostsByKeyword(String keyword, Pageable pageable) {
		List<InfoPostRes.InfoPostInfo> postInfos = jpaQueryFactory.select(
				Projections.constructor(InfoPostRes.InfoPostInfo.class,
					infoPost.id,
					infoPost.image.imageUrl,
					infoPost.title,
					infoPost.recruitmentEndDate,
					infoPost.viewCount))
			.from(infoPost)
			.where(buildKeywordMatchCondition(keyword))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(buildOrderSpecifiers(pageable.getSort()))
			.fetch();

		return paginateInfoPostsByKeyword(keyword, InfoPostRes.of(postInfos), pageable);
	}

	private Page<InfoPostRes> paginateInfoPostsByKeyword(String keyword, List<InfoPostRes> infoPostRes,
		Pageable pageable) {
		JPAQuery<Long> countQuery = getCountQueryByKeyword(keyword);

		return PageableExecutionUtils.getPage(infoPostRes, pageable, countQuery::fetchOne);
	}

	private JPAQuery<Long> getCountQueryByKeyword(String keyword) {
		return jpaQueryFactory.select(infoPost.id.count())
			.from(infoPost)
			.where(buildKeywordMatchCondition(keyword));
	}

	private BooleanTemplate buildKeywordMatchCondition(String keyword) {
		return Expressions.booleanTemplate(
			"function('match_against', {0}, {1}, {2})",
			infoPost.title,
			infoPost.content,
			keyword + '*'
		);
	}
}
