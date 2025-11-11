package com.example.caba.application.dto;

import java.util.List;
import java.util.UUID;

public record TorneoDto(
        UUID id,
        String nombre,
        String descripcion,
        PeriodoDto periodo,
        List<TarifaDto> tarifas) {}

