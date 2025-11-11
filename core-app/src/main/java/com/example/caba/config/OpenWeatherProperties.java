package com.example.caba.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "integrations.openweather")
public class OpenWeatherProperties {

    private boolean enabled = false;
    private String apiKey;
    private String baseUrl = "https://api.openweathermap.org/data/2.5";
    private String defaultCity = "Buenos Aires,AR";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getDefaultCity() {
        return defaultCity;
    }

    public void setDefaultCity(String defaultCity) {
        this.defaultCity = defaultCity;
    }

    public boolean hasValidConfiguration() {
        return enabled && apiKey != null && !apiKey.isBlank();
    }
}

