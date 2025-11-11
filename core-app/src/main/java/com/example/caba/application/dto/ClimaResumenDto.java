package com.example.caba.application.dto;

public record ClimaResumenDto(
        String ciudad,
        String descripcion,
        double temperatura,
        double sensacionTermica,
        int humedad) {}

