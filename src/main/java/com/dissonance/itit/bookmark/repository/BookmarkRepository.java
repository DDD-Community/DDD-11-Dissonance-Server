package com.dissonance.itit.bookmark.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dissonance.itit.bookmark.domain.Bookmark;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
	boolean existsByUserIdAndPostId(Long userId, Long postId);

	Optional<Bookmark> findByUserIdAndPostId(Long userId, Long postId);
}
