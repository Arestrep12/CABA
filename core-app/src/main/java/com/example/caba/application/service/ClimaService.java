package com.example.caba.application.service;

import com.example.caba.application.dto.ClimaResumenDto;
import com.example.caba.config.OpenWeatherProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClimaService {

    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(5);

    @Qualifier("openWeatherWebClient")
    private final WebClient openWeatherWebClient;
    private final OpenWeatherProperties properties;

    public Optional<ClimaResumenDto> obtenerClimaActual() {
        if (!properties.hasValidConfiguration()) {
            return Optional.empty();
        }

        String ciudad = ciudadConfigurada();
        try {
            OpenWeatherResponse response = openWeatherWebClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/weather")
                            .queryParam("q", ciudad)
                            .queryParam("appid", properties.getApiKey())
                            .queryParam("units", "metric")
                            .queryParam("lang", "es")
                            .build())
                    .retrieve()
                    .bodyToMono(OpenWeatherResponse.class)
                    .block(REQUEST_TIMEOUT);

            if (response == null || response.main() == null || response.weather() == null || response.weather().isEmpty()) {
                return Optional.empty();
            }

            OpenWeatherResponse.Main main = response.main();
            OpenWeatherResponse.Weather weather = response.weather().getFirst();

            return Optional.of(new ClimaResumenDto(
                    response.name(),
                    capitalizar(weather.description()),
                    main.temp(),
                    main.feelsLike(),
                    (int) Math.round(main.humidity())));

        } catch (WebClientResponseException ex) {
            log.warn("Fallo al consultar clima de {}: {} {}", ciudad, ex.getStatusCode(), ex.getResponseBodyAsString());
        } catch (Exception ex) {
            log.warn("Fallo inesperado consultando clima de {}: {}", ciudad, ex.getMessage());
        }
        return Optional.empty();
    }

    private String ciudadConfigurada() {
        String ciudad = properties.getDefaultCity();
        if (ciudad == null || ciudad.isBlank()) {
            return "Buenos Aires,AR";
        }
        return ciudad;
    }

    private String capitalizar(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) {
            return "Sin información";
        }
        return descripcion.substring(0, 1).toUpperCase(Locale.getDefault()) + descripcion.substring(1);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record OpenWeatherResponse(String name, Main main, List<Weather> weather) {

        @JsonIgnoreProperties(ignoreUnknown = true)
        private record Main(
                double temp,
                @JsonProperty("feels_like") double feelsLike,
                double humidity) {}

        @JsonIgnoreProperties(ignoreUnknown = true)
        private record Weather(String description) {}
    }
}

