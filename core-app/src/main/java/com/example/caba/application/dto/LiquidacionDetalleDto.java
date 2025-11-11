package com.example.caba.application.dto;

import com.example.caba.domain.shared.enums.RolAsignacion;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record LiquidacionDetalleDto(UUID partidoId, RolAsignacion rol, BigDecimal monto, LocalDate fechaPartido) {}

