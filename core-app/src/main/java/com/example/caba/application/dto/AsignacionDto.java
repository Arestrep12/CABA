package com.example.caba.application.dto;

import com.example.caba.domain.shared.enums.EstadoAsignacion;
import com.example.caba.domain.shared.enums.RolAsignacion;
import java.time.LocalDateTime;
import java.util.UUID;

public record AsignacionDto(
        UUID id,
        UUID partidoId,
        UUID arbitroId,
        RolAsignacion rol,
        EstadoAsignacion estado,
        LocalDateTime respondidoEn) {}

