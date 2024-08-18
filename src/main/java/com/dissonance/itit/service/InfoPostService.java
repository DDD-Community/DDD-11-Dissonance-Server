package com.dissonance.itit.service;

import com.dissonance.itit.domain.entity.Category;
import com.dissonance.itit.domain.entity.Image;
import com.dissonance.itit.domain.entity.InfoPost;
import com.dissonance.itit.domain.entity.User;
import com.dissonance.itit.domain.enums.Directory;
import com.dissonance.itit.dto.request.InfoPostReq;
import com.dissonance.itit.dto.response.InfoPostCreateRes;
import com.dissonance.itit.repository.InfoPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class InfoPostService {
    private final InfoPostRepository infoPostRepository;

    private final ImageService imageService;
    private final CategoryService categoryService;
    private final RecruitmentPositionService recruitmentPositionService;

    @Transactional
    public InfoPostCreateRes createInfoPost(MultipartFile imgFile, InfoPostReq infoPostReq, User author) {
        Image image = imageService.upload(Directory.INFORMATION, imgFile);
        Category category = categoryService.findById(infoPostReq.categoryId());

        InfoPost infoPost = infoPostRepository.save(infoPostReq.toEntity(image, author, category));

        recruitmentPositionService.addPositions(infoPost, infoPostReq.positionInfos());

        return InfoPostCreateRes.of(infoPost);
    }
}
