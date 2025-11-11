package com.example.caba;

import com.example.caba.config.OpenWeatherProperties;
import com.example.caba.security.JwtProperties;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class, OpenWeatherProperties.class})
@EnableFeignClients
@OpenAPIDefinition(info = @Info(title = "CABA Pro API", version = "v1", description = "Plataforma integral de arbitraje"))
public class CabaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CabaApplication.class, args);
    }
}
