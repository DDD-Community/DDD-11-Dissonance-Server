package com.dissonance.itit.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dissonance.itit.image.domain.Image;
import com.dissonance.itit.post.domain.InfoPost;

public interface InfoPostRepository extends JpaRepository<InfoPost, Long> {
	@Modifying
	@Query("update InfoPost ip set ip.image=:image where ip.id=:infoPostId")
	void updateImage(Long infoPostId, Image image);
}
