package com.dissonance.itit.repository;

import com.dissonance.itit.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByProviderAndProviderId(String provider, String providerId);
    Optional<User> findByEmail(String email);
}
