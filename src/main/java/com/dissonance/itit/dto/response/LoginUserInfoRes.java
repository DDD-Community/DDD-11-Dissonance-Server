package com.dissonance.itit.dto.response;

public record LoginUserInfoRes(
	boolean isAdmin,
	String provider) {
}
