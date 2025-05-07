package com.dissonance.itit.bookmark.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.dissonance.itit.bookmark.domain.QBookmark;
import com.dissonance.itit.bookmark.dto.BookmarkRes;
import com.dissonance.itit.bookmark.dto.BookmarkRes.BookmarkedPost;
import com.dissonance.itit.post.domain.QInfoPost;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BookmarkRepositorySupport {
	private final JPAQueryFactory jpaQueryFactory;
	private final QBookmark bookmark = QBookmark.bookmark;
	private final QInfoPost post = QInfoPost.infoPost;

	public Slice<BookmarkRes> findBookmarkedPostsByUserId(Long userId, Pageable pageable) {
		int pageSize = pageable.getPageSize();

		// limit + 1로 다음 페이지 존재 여부 판단
		List<BookmarkedPost> results = jpaQueryFactory.select(
				Projections.constructor(BookmarkedPost.class,
					post.id,
					post.title,
					post.recruitmentEndDate
				))
			.from(bookmark)
			.join(bookmark.post, post)
			.where(userIdEq(userId))
			.orderBy(buildOrderSpecifiers(pageable.getSort()))
			.offset(pageable.getOffset())
			.limit(pageSize + 1)
			.fetch();

		boolean hasNext = results.size() > pageSize;

		// hasNext 판단 후, Slice에 담을 결과는 pageSize만큼 자름
		List<BookmarkedPost> content = hasNext ? results.subList(0, pageSize) : results;

		return new SliceImpl<>(BookmarkRes.of(content), pageable, hasNext);
	}

	public OrderSpecifier<?>[] buildOrderSpecifiers(Sort sort) {
		List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

		for (Sort.Order order : sort) {
			OrderSpecifier<?> idSort = null;

			LocalDate currentDate = LocalDate.now();

			OrderSpecifier<?> prioritySort = new OrderSpecifier<>(
				Order.DESC,
				Expressions.booleanTemplate("CASE WHEN {0} >= {1} THEN 1 ELSE 0 END",
					post.recruitmentEndDate, currentDate)
			);

			orderSpecifiers.add(prioritySort);

			switch (order.getProperty()) {
				case "latest":
					idSort = new OrderSpecifier<>(Order.DESC, post.id);
					orderSpecifiers.add(idSort);
					break;
				case "deadline":
					OrderSpecifier<?> dateSort = new OrderSpecifier<>(Order.ASC, post.recruitmentEndDate);
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

	private BooleanExpression userIdEq(Long userId) {
		return bookmark.user.id.eq(userId);
	}
}
