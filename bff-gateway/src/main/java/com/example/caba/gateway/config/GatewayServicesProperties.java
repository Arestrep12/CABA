package com.example.caba.gateway.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "services")
public class GatewayServicesProperties {

    @NotBlank
    private String core;

    @NotBlank
    private String agenda;

    @NotBlank
    private String notifications;
}

