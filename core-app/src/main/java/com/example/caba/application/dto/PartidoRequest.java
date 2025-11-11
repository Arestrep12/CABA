package com.example.caba.application.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

public record PartidoRequest(
        @NotNull UUID torneoId,
        @NotNull @FutureOrPresent LocalDateTime fechaProgramada,
        @NotBlank String sede,
        @NotBlank String categoria) {}

