package com.dissonance.itit.post.domain;

import java.time.LocalDate;

import com.dissonance.itit.global.common.entity.BaseTime;
import com.dissonance.itit.image.domain.Image;
import com.dissonance.itit.post.dto.request.InfoPostUpdateReq;
import com.dissonance.itit.user.domain.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "info_post")
public class InfoPost extends BaseTime {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Size(max = 255)
	@NotNull
	@Column(name = "title")
	private String title;

	@NotNull
	@Size(max = 4000)
	@Column(name = "content")
	private String content;

	@NotNull
	@Column(name = "view_count")
	private Integer viewCount;

	@Column(name = "recruitment_start_date")
	private LocalDate recruitmentStartDate;

	@NotNull
	@Column(name = "recruitment_end_date")
	private LocalDate recruitmentEndDate;

	@Column(name = "activity_start_date")
	private LocalDate activityStartDate;

	@Column(name = "activity_end_date")
	private LocalDate activityEndDate;

	@Size(max = 500)
	@NotNull
	@Column(name = "detail_url")
	private String detailUrl;

	@Size(max = 100)
	@NotNull
	@Column(name = "organization")
	private String organization;

	@Column(name = "recruitment_closed")
	private Boolean recruitmentClosed;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "image_id")
	private Image image;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "author_id")
	private User author;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	public void update(InfoPostUpdateReq updateReq) {
		this.category = updateReq.category();
		this.title = updateReq.title();
		this.organization = updateReq.organization();
		this.content = updateReq.content();
		this.recruitmentStartDate = updateReq.recruitmentStartDate();
		this.recruitmentEndDate = updateReq.recruitmentEndDate();
		this.activityStartDate = updateReq.activityStartDate();
		this.activityEndDate = updateReq.activityEndDate();
		this.detailUrl = updateReq.detailUrl();
	}

	public boolean isAuthor(User loginUser) {
		return this.getAuthor().getId().equals(loginUser.getId());
	}

	public void updateImage(Image image) {
		this.image = image;
	}
}