package com.dissonance.itit.global.config;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI openAPI() {
		Info info = new Info()
			.title("Mozip API Document")
			.version("v1.0")
			.description("Mozip API 문서입니다.");

		Server prodServer = new Server();
		prodServer.description("Production Server")
			.url("https://dissonance-server.duckdns.org/api/v1");

		Server devServer = new Server();
		devServer.description("Development Server")
			.setUrl("http://localhost:8080/api/v1");

		SecurityScheme securityScheme = new SecurityScheme()
			.type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
			.in(SecurityScheme.In.HEADER).name("Authorization");
		SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

		return new OpenAPI()
			.info(info)
			.servers(List.of(prodServer, devServer))
			.components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
			.security(Collections.singletonList(securityRequirement));
	}
}
