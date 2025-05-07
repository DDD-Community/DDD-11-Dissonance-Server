package com.dissonance.itit.bookmark.service;

import static com.dissonance.itit.global.common.exception.ErrorCode.*;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dissonance.itit.bookmark.domain.Bookmark;
import com.dissonance.itit.bookmark.dto.BookmarkRes;
import com.dissonance.itit.bookmark.dto.BookmarkToggleRes;
import com.dissonance.itit.bookmark.repository.BookmarkRepository;
import com.dissonance.itit.bookmark.repository.BookmarkRepositorySupport;
import com.dissonance.itit.global.common.exception.CustomException;
import com.dissonance.itit.post.domain.InfoPost;
import com.dissonance.itit.post.repository.InfoPostRepository;
import com.dissonance.itit.user.domain.User;
import com.dissonance.itit.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookmarkService {
	private final BookmarkRepository bookmarkRepository;
	private final BookmarkRepositorySupport bookmarkRepositorySupport;
	private final UserRepository userRepository;
	private final InfoPostRepository infoPostRepository;

	@Transactional
	public BookmarkToggleRes toggleBookmark(Long userId, Long postId) {
		return bookmarkRepository.findByUserIdAndPostId(userId, postId)
			.map(bookmark -> {
				bookmarkRepository.delete(bookmark);
				return new BookmarkToggleRes(false);
			})
			.orElseGet(() -> addBookmark(userId, postId));
	}

	private BookmarkToggleRes addBookmark(Long userId, Long postId) {
		try {
			User user = userRepository.getReferenceById(userId);
			InfoPost post = infoPostRepository.findById(postId)
				.orElseThrow(() -> new CustomException(NON_EXISTENT_INFO_POST_ID));

			Bookmark bookmark = Bookmark.builder().user(user).post(post).build();
			bookmarkRepository.save(bookmark);

			return new BookmarkToggleRes(true);
		} catch (DataIntegrityViolationException e) {        // 중복 요청 (e.g. 연속 클릭)
			return new BookmarkToggleRes(true);
		}
	}

	@Transactional(readOnly = true)
	public Slice<BookmarkRes> getBookmarkedPostsByUser(Long userId, Pageable pageable) {
		return bookmarkRepositorySupport.findBookmarkedPostsByUserId(userId, pageable);
	}
}
