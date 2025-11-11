package com.example.caba.api.controller;

import com.example.caba.application.dto.DashboardResumenDto;
import com.example.caba.application.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','ARBITRO')")
@Tag(name = "Dashboard", description = "Consultas generales del panel de control")
public class DashboardApiController {

    private final DashboardService dashboardService;

    @GetMapping("/resumen")
    @Operation(
            summary = "Obtener resumen del dashboard",
            description = "Incluye métricas de asignaciones, torneos vigentes y clima actual configurado.")
    @ApiResponse(responseCode = "200", description = "Resumen generado exitosamente")
    public DashboardResumenDto obtenerResumen() {
        return dashboardService.obtenerResumen();
    }
}

