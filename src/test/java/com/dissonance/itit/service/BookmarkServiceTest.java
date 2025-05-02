package com.dissonance.itit.service;

import static com.dissonance.itit.global.common.exception.ErrorCode.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import com.dissonance.itit.bookmark.domain.Bookmark;
import com.dissonance.itit.bookmark.dto.BookmarkToggleRes;
import com.dissonance.itit.bookmark.repository.BookmarkRepository;
import com.dissonance.itit.bookmark.service.BookmarkService;
import com.dissonance.itit.global.common.exception.CustomException;
import com.dissonance.itit.post.domain.InfoPost;
import com.dissonance.itit.post.repository.InfoPostRepository;
import com.dissonance.itit.user.domain.User;
import com.dissonance.itit.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class BookmarkServiceTest {
	@InjectMocks
	private BookmarkService bookmarkService;
	@Mock
	private BookmarkRepository bookmarkRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private InfoPostRepository infoPostRepository;

	private final Long userId = 1L;
	private final Long postId = 100L;

	@Nested
	@DisplayName("toggleBookmark 메서드 테스트")
	class ToggleBookmark {

		@Test
		@DisplayName("북마크가 존재하면 삭제 후 false 반환")
		void deleteExistingBookmark() {
			// given
			Bookmark existingBookmark = mock(Bookmark.class);
			given(bookmarkRepository.findByUserIdAndPostId(userId, postId))
				.willReturn(Optional.of(existingBookmark));

			// when
			BookmarkToggleRes result = bookmarkService.toggleBookmark(userId, postId);

			// then
			verify(bookmarkRepository).delete(existingBookmark);
			assertThat(result.isBookmarked()).isFalse();
		}

		@Test
		@DisplayName("북마크가 없으면 등록 후 true 반환")
		void addNewBookmark() {
			// given
			given(bookmarkRepository.findByUserIdAndPostId(userId, postId))
				.willReturn(Optional.empty());

			User user = mock(User.class);
			InfoPost post = mock(InfoPost.class);
			given(userRepository.getReferenceById(userId)).willReturn(user);
			given(infoPostRepository.findById(postId)).willReturn(Optional.of(post));

			// when
			BookmarkToggleRes result = bookmarkService.toggleBookmark(userId, postId);

			// then
			verify(bookmarkRepository).save(any(Bookmark.class));
			assertThat(result.isBookmarked()).isTrue();
		}

		@Test
		@DisplayName("Post가 존재하지 않으면 예외 발생")
		void throwCustomException_whenNonExistentPost() {
			// given
			given(bookmarkRepository.findByUserIdAndPostId(userId, postId))
				.willReturn(Optional.empty());
			given(infoPostRepository.findById(postId)).willReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> bookmarkService.toggleBookmark(userId, postId))
				.isInstanceOf(CustomException.class)
				.hasMessageContaining(NON_EXISTENT_INFO_POST_ID.getMessage());
		}

		@Test
		@DisplayName("중복 요청 등으로 무결성제약예외가 발생하면 true 반환")
		void returnTrue_WhenDuplicateInsert() {
			// given
			given(bookmarkRepository.findByUserIdAndPostId(userId, postId))
				.willReturn(Optional.empty());

			InfoPost post = mock(InfoPost.class);
			given(userRepository.getReferenceById(userId)).willReturn(mock(User.class));
			given(infoPostRepository.findById(postId)).willReturn(Optional.of(post));

			willThrow(DataIntegrityViolationException.class)
				.given(bookmarkRepository).save(any());

			// when
			BookmarkToggleRes result = bookmarkService.toggleBookmark(userId, postId);

			// then
			assertThat(result.isBookmarked()).isTrue();
		}
	}
}
