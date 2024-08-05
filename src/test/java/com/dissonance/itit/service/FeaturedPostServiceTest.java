package com.dissonance.itit.service;

import com.dissonance.itit.domain.entity.FeaturedPost;
import com.dissonance.itit.domain.entity.InfoPost;
import com.dissonance.itit.dto.response.FeaturedPostRes;
import com.dissonance.itit.repository.FeaturedPostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class FeaturedPostServiceTest {
    @InjectMocks
    private FeaturedPostService featuredPostService;
    @Mock
    private FeaturedPostRepository featuredPostRepository;

    @Test
    @DisplayName("추천 게시글 목록을 조회한다.")
    public void getFeaturedPost_returnFeaturedPostRes() {
        InfoPost infoPost = InfoPost.builder().id(10L).build();

        FeaturedPost featuredPost = FeaturedPost.builder()
                .id(1)
                .bannerImageUrl("http://example.com/image.jpg")
                .infoPost(infoPost)
                .build();

        // given
        given(featuredPostRepository.findAll()).willReturn(List.of(featuredPost));

        // when
        List<FeaturedPostRes> result = featuredPostService.getFeaturedPost();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFeaturedPostId()).isEqualTo(featuredPost.getId());
        assertThat(result.get(0).getBannerImageUrl()).isEqualTo(featuredPost.getBannerImageUrl());
        assertThat(result.get(0).getInfoPostId()).isEqualTo(featuredPost.getInfoPost().getId());
    }
}
