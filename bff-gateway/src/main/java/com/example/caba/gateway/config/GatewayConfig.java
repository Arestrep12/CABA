package com.example.caba.gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final GatewayServicesProperties services;

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route("core-api", route -> route
                        .path("/api/core/**")
                        .filters(filter -> filter.rewritePath("/api/core/(?<segment>.*)", "/${segment}"))
                        .uri(services.getCore()))
                .route("agenda-api", route -> route
                        .path("/api/agenda/**")
                        .filters(filter -> filter.rewritePath("/api/agenda/(?<segment>.*)", "/${segment}"))
                        .uri(services.getAgenda()))
                .route("notifications-api", route -> route
                        .path("/api/notifications/**")
                        .filters(filter ->
                                filter.rewritePath("/api/notifications/(?<segment>.*)", "/${segment}"))
                        .uri(services.getNotifications()))
                .route("static-core", route -> route
                        .path("/**")
                        .uri(services.getCore()))
                .build();
    }
}

