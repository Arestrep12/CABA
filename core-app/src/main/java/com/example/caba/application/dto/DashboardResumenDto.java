package com.example.caba.application.dto;

import java.util.List;

public record DashboardResumenDto(
        long totalAsignaciones,
        long totalAceptadas,
        long totalRechazadas,
        long totalPendientes,
        double porcentajeAceptadas,
        long torneosVigentes,
        List<TopArbitroDto> topArbitros,
        ClimaResumenDto climaActual) {}

