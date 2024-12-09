package com.dissonance.itit.global.common.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RedisService {
	private final RedisTemplate<String, String> redisTemplate;

	public String getValues(String key) {
		return redisTemplate.opsForValue().get(key);
	}

	public void setValuesWithTimeout(String key, String value, long duration) {
		redisTemplate.opsForValue().set(key, value, duration, TimeUnit.MILLISECONDS);
	}

	public void deleteValues(String key) {
		redisTemplate.delete(key);
	}
}
