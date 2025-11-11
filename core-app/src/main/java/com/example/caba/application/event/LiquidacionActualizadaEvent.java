package com.example.caba.application.event;

import com.example.caba.domain.shared.enums.EstadoLiquidacion;
import java.math.BigDecimal;
import java.util.UUID;

public record LiquidacionActualizadaEvent(
        UUID liquidacionId, UUID arbitroId, EstadoLiquidacion estado, BigDecimal total) {}

