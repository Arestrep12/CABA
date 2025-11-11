package com.example.caba.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(OpenWeatherProperties.class)
public class OpenWeatherConfig {

    @Bean
    @Qualifier("openWeatherWebClient")
    public WebClient openWeatherWebClient(WebClient.Builder builder, OpenWeatherProperties properties) {
        return builder
                .baseUrl(properties.getBaseUrl())
                .defaultHeaders(headers -> headers.setAccept(List.of(MediaType.APPLICATION_JSON)))
                .build();
    }
}

