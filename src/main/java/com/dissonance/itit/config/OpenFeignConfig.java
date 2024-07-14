package com.dissonance.itit.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("com.dissonance.itit.client")
public class OpenFeignConfig {
}
