package com.dissonance.itit.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dissonance.itit.common.exception.CustomException;
import com.dissonance.itit.common.exception.ErrorCode;
import com.dissonance.itit.domain.entity.User;
import com.dissonance.itit.domain.entity.UserDetailsImpl;
import com.dissonance.itit.repository.UserRepository;

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
