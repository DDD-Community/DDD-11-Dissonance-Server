package com.dissonance.itit.user.domain;

import com.dissonance.itit.global.common.entity.BaseTime;
import com.dissonance.itit.oauth.enums.SocialLoginProvider;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "user")
public class User extends BaseTime {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Size(max = 50)
	@Column(name = "name")
	private String name;

	@Size(max = 100)
	@NotNull
	@Column(name = "email")
	private String email;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(name = "role")
	private Role role;

	@Column(name = "provider_id")
	private String providerId;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(name = "provider")
	private SocialLoginProvider provider;

	@Size(max = 255)
	@Column(name = "profile_img_url")
	private String profileImgUrl;
}