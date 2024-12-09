package com.dissonance.itit.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dissonance.itit.oauth.enums.SocialLoginProvider;
import com.dissonance.itit.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByProviderAndProviderId(SocialLoginProvider provider, String providerId);

	Optional<User> findByEmail(String email);
}
