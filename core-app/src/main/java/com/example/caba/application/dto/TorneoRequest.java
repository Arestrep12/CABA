package com.example.caba.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record TorneoRequest(
        @NotBlank String nombre,
        String descripcion,
        @Valid @NotNull PeriodoDto periodo,
        @Valid List<TarifaDto> tarifas) {}

