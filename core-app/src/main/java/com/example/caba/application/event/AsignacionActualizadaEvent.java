package com.example.caba.application.event;

import com.example.caba.domain.shared.enums.EstadoAsignacion;
import com.example.caba.domain.shared.enums.RolAsignacion;
import java.time.LocalDateTime;
import java.util.UUID;

public record AsignacionActualizadaEvent(
        UUID asignacionId,
        UUID partidoId,
        UUID arbitroId,
        RolAsignacion rol,
        EstadoAsignacion estado,
        LocalDateTime fechaPartido,
        String motivo) {}

