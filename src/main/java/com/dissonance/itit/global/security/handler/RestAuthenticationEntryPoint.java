package com.dissonance.itit.global.security.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(
		HttpServletRequest request
		, HttpServletResponse response
		, AuthenticationException e
	) throws IOException {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());

		final Map<String, Object> errorJson = new HashMap<>();
		errorJson.put("code", HttpStatus.UNAUTHORIZED.value());
		errorJson.put("message", "Authentication failed");

		ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), errorJson);
	}
}
