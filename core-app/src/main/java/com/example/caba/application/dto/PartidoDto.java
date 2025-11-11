package com.example.caba.application.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PartidoDto(
        UUID id,
        UUID torneoId,
        LocalDateTime fechaProgramada,
        String sede,
        String categoria,
        List<AsignacionDto> asignaciones) {}

