package com.dissonance.itit.user.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dissonance.itit.user.domain.User;
import com.dissonance.itit.user.domain.UserDetailsImpl;
import com.dissonance.itit.global.common.exception.CustomException;
import com.dissonance.itit.global.common.exception.ErrorCode;
import com.dissonance.itit.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	private final UserRepository userRepository;

	@Override
	public UserDetailsImpl loadUserByUsername(String email) throws UsernameNotFoundException {
		User findUser = userRepository.findByEmail(email)
			.orElseThrow(() -> new CustomException(ErrorCode.NON_EXISTENT_EMAIL));

		if (findUser != null) {
			return new UserDetailsImpl(findUser);
		}

		return null;
	}
}
