package com.example.caba.application.dto;

import com.example.caba.domain.shared.enums.EstadoLiquidacion;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record LiquidacionDto(
        UUID id,
        UUID arbitroId,
        EstadoLiquidacion estado,
        PeriodoDto periodo,
        BigDecimal total,
        List<LiquidacionDetalleDto> detalles) {}

