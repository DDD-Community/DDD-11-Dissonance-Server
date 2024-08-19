package com.dissonance.itit.service;

import com.dissonance.itit.domain.entity.InfoPost;
import com.dissonance.itit.domain.entity.RecruitmentPosition;
import com.dissonance.itit.dto.request.InfoPostReq;
import com.dissonance.itit.repository.RecruitmentPositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruitmentPositionService {
    private final RecruitmentPositionRepository recruitmentPositionRepository;

    @Transactional
    public void addPositions(InfoPost infoPost, List<InfoPostReq.PositionInfo> positionInfos) {
        positionInfos.forEach(positionInfo -> {
            RecruitmentPosition newRecruitmentPosition = RecruitmentPosition.builder()
                    .infoPost(infoPost)
                    .name(positionInfo.getPositionName())
                    .recruitingCount(positionInfo.getRecruitingCount())
                    .build();
            recruitmentPositionRepository.save(newRecruitmentPosition);
        });
    }
}
