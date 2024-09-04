package com.dissonance.itit.common.jwt.util;

import static com.dissonance.itit.domain.enums.JwtTokenExpiration.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.dissonance.itit.domain.entity.UserDetailsImpl;
import com.dissonance.itit.dto.response.GeneratedToken;
import com.dissonance.itit.service.UserDetailsServiceImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtUtil {
	@Value("${jwt.token.secret-key}")
	private String jwtSecretKey;
	private String secretKey;

	private final UserDetailsServiceImpl userDetailsService;

	// TODO: redis를 이용한 refresh token 재발급 구현
	//    private final RedisService redisService;

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
	}

	public GeneratedToken generateToken(String email, String role) {
		String refreshToken = generateRefreshToken(email, role);
		String accessToken = generateAccessToken(email, role);

		return GeneratedToken.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}

	public String generateRefreshToken(String email, String role) {
		// Claim에 이메일, 권한 세팅
		Claims claims = Jwts.claims().setSubject(email);
		claims.put("role", role);

		Date now = new Date();

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)   // 발행일자
			.setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_TIME.getValue()))     // 만료 일시
			.signWith(SignatureAlgorithm.HS256, secretKey)      // HS256 알고리즘과 secretKey로 서명
			.compact();
	}

	public String generateAccessToken(String email, String role) {
		Claims claims = Jwts.claims().setSubject(email);
		claims.put("role", role);

		Date now = new Date();
		return
			Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRED_TIME.getValue()))
				.signWith(SignatureAlgorithm.HS256, secretKey)
				.compact();
	}

	public boolean verifyToken(String token) {
		try {
			//            if (redisService.getValues(token) != null && redisService.getValues(token).equals("logout")) {
			//                throw new JwtException("Invalid JWT Token - logout");
			//            }
			Jws<Claims> claims = Jwts.parser()
				.setSigningKey(secretKey)
				.parseClaimsJws(token);

			return claims.getBody()
				.getExpiration()
				.after(new Date());
		} catch (MalformedJwtException e) {
			log.info("Invalid JWT Token", e);
			throw new JwtException("Invalid JWT Token", e);
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT Token", e);
			throw new JwtException("Expired JWT Token", e);
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT Token", e);
			throw new JwtException("Unsupported JWT Token", e);
		} catch (IllegalArgumentException e) {
			log.info("JWT claims string is empty.", e);
			throw new JwtException("JWT claims string is empty.", e);
		}
	}

	// 토큰에서 email 추출
	public String getUid(String token) {
		try {
			return Jwts.parser()
				.setSigningKey(secretKey)
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
		} catch (ExpiredJwtException e) {
			return e.getClaims().getSubject();
		}
	}

	// 토큰에서 권한 추출
	public String getRole(String token) {
		try {
			return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("role", String.class);
		} catch (ExpiredJwtException e) {
			return e.getClaims().get("role", String.class);
		}
	}

	//JWT 토큰의 남은 유효 시간 추출
	public Long getExpiration(String token) {
		Date expiration = Jwts.parser().setSigningKey(secretKey)
			.parseClaimsJws(token).getBody().getExpiration();

		return expiration.getTime() - new Date().getTime();
	}

	// request Header에서 토큰 추출
	public String resolveToken(String requestAccessTokenInHeader) {
		if (requestAccessTokenInHeader != null && requestAccessTokenInHeader.startsWith("Bearer ")) {
			return requestAccessTokenInHeader.substring(7);
		}
		return null;
	}

	// Authentication 생성
	public Authentication getAuthentication(String accessToken) {
		String email = getUid(accessToken);

		UserDetailsImpl userDetailsImpl = userDetailsService.loadUserByUsername(email);

		return new UsernamePasswordAuthenticationToken(userDetailsImpl, "", userDetailsImpl.getAuthorities());
	}
}