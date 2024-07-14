package com.dissonance.itit.repository;

import com.dissonance.itit.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByProviderAndProviderId(String provider, String providerId);
}
