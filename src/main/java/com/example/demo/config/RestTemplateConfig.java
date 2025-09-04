package com.example.demo.config;


import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
/**
 * Configuration class for RestTemplate.
 * Sets connection and read timeouts for external API calls.
 * If communication with ViaCEP fails due to a timeout, the application should return a 503 Service Unavailable error.
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .requestFactory(() -> {
                    var factory = new org.springframework.http.client.SimpleClientHttpRequestFactory();
                    factory.setConnectTimeout(5000);
                    factory.setReadTimeout(5000);
                    return factory;
                })
                .build();
    }
}
