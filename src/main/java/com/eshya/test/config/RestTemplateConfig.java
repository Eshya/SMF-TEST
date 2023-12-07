package com.eshya.test.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

// this is configuration for JWT
@Configuration
public class RestTemplateConfig {

	private static final long CONNECTION_TIMEOUT = 10;

	private static final long READ_TIMEOUT = 10;

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.setConnectTimeout(Duration.ofSeconds(CONNECTION_TIMEOUT))
				.setReadTimeout(Duration.ofSeconds(READ_TIMEOUT)).build();
	}

}
