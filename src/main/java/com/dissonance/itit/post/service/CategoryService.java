package com.dissonance.itit.post.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dissonance.itit.global.common.exception.CustomException;
import com.dissonance.itit.global.common.exception.ErrorCode;
import com.dissonance.itit.post.domain.Category;
import com.dissonance.itit.post.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

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
