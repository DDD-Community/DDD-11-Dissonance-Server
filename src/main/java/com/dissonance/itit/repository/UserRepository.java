package com.dissonance.itit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dissonance.itit.domain.entity.User;
import com.dissonance.itit.domain.enums.SocialLoginProvider;

public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByProviderAndProviderId(SocialLoginProvider provider, String providerId);

	Optional<User> findByEmail(String email);
}
