package com.dissonance.itit.global.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("com.dissonance.itit.oauth.client")
public class OpenFeignConfig {
}
