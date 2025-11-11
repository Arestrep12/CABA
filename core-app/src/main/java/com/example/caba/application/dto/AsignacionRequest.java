package com.example.caba.application.dto;

import com.example.caba.domain.shared.enums.RolAsignacion;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record AsignacionRequest(
        @NotNull UUID partidoId, @NotNull UUID arbitroId, @NotNull RolAsignacion rol) {}

