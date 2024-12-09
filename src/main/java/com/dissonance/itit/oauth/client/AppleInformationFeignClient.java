package com.dissonance.itit.oauth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "AppleInformationFeignClient", url = "${apple.api_url.information}")
// @EnableFeignClients(basePackages = "com.dissonance.itit.oauth.client")
public interface AppleInformationFeignClient {
	@GetMapping
	String call();
}
