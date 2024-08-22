package com.dissonance.itit.service;

import com.dissonance.itit.common.exception.CustomException;
import com.dissonance.itit.common.exception.ErrorCode;
import com.dissonance.itit.domain.entity.Category;
import com.dissonance.itit.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Category findById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NON_EXISTENT_CATEGORY_ID));
    }
}
