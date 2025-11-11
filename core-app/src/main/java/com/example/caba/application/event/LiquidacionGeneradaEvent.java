package com.example.caba.application.event;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record LiquidacionGeneradaEvent(
        UUID liquidacionId, UUID arbitroId, BigDecimal total, LocalDate periodoInicio, LocalDate periodoFin) {}

