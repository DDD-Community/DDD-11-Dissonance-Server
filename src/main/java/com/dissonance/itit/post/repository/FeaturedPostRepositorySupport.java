package com.dissonance.itit.post.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dissonance.itit.post.domain.QFeaturedPost;
import com.dissonance.itit.post.dto.response.FeaturedPostRes;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FeaturedPostRepositorySupport {
	private final JPAQueryFactory jpaQueryFactory;
	private final QFeaturedPost featuredPost = QFeaturedPost.featuredPost;

	public List<FeaturedPostRes> findAll() {
		return jpaQueryFactory.select(
				Projections.constructor(FeaturedPostRes.class,
					featuredPost.id,
					featuredPost.bannerImageUrl,
					featuredPost.infoPost.id,
					featuredPost.infoPost.title
				))
			.from(featuredPost)
			.limit(5)
			.fetch();
	}
}
