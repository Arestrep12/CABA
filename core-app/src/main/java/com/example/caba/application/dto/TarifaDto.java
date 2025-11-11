package com.example.caba.application.dto;

import com.example.caba.domain.shared.enums.Escalafon;
import com.example.caba.domain.shared.enums.RolAsignacion;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record TarifaDto(
        @NotNull Escalafon escalafon, @NotNull RolAsignacion rol, @DecimalMin("0.0") BigDecimal monto) {}

