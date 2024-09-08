package com.dissonance.itit.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "AppleInformationFeignClient", url = "${apple.api_url.information}")
public interface AppleInformationFeignClient {
	@GetMapping
	String call();
}
