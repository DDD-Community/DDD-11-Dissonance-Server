package com.dissonance.itit.global.security.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.dissonance.itit.user.domain.UserDetailsImpl;

@Component
public class UserContext {
	public Long getUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated())
			return null;

		Object principal = authentication.getPrincipal();
		if (principal instanceof UserDetailsImpl userDetails) {
			return userDetails.getUser().getId();
		}

		return null;
	}
}
