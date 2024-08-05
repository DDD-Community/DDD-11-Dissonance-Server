package com.dissonance.itit.dto.response;

import com.dissonance.itit.domain.entity.FeaturedPost;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FeaturedPostRes {
    private final Integer featuredPostId;
    private final String bannerImageUrl;
    private final Long infoPostId;

    public static List<FeaturedPostRes> of(List<FeaturedPost> featuredPosts) {
        return featuredPosts.stream()
                .map(featuredPost -> FeaturedPostRes.builder()
                        .featuredPostId(featuredPost.getId())
                        .bannerImageUrl(featuredPost.getBannerImageUrl())
                        .infoPostId(featuredPost.getInfoPost().getId())
                        .build())
                .toList();
    }
}
