package com.example.caba.api.controller;

import com.example.caba.application.dto.LiquidacionDto;
import com.example.caba.application.dto.PeriodoDto;
import com.example.caba.application.service.LiquidacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/liquidaciones")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Liquidaciones", description = "Gestión de procesos de cobro")
public class LiquidacionApiController {

    private final LiquidacionService liquidacionService;

    @PostMapping("/arbitro/{arbitroId}")
    @Operation(summary = "Generar liquidación para un árbitro en un periodo")
    public ResponseEntity<LiquidacionDto> generar(
            @PathVariable UUID arbitroId, @Valid @RequestBody PeriodoDto periodo) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(liquidacionService.generarLiquidacion(arbitroId, periodo));
    }

    @PostMapping("/{id}/pagar")
    @Operation(summary = "Marcar liquidación como pagada")
    public ResponseEntity<Void> marcarPagada(@PathVariable UUID id) {
        liquidacionService.marcarPagada(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/arbitro/{arbitroId}")
    @PreAuthorize("hasAnyRole('ADMIN','ARBITRO')")
    @Operation(summary = "Listar liquidaciones por árbitro")
    public List<LiquidacionDto> porArbitro(@PathVariable UUID arbitroId) {
        return liquidacionService.listarPorArbitro(arbitroId);
    }

    @GetMapping("/resumen")
    @Operation(summary = "Generar resumen mensual de liquidaciones")
    public List<LiquidacionDto> resumenMensual(
            @RequestParam("periodo") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodo) {
        return liquidacionService.generarResumenMensual(periodo);
    }

    @GetMapping("/{id}/reporte")
    @PreAuthorize("hasAnyRole('ADMIN','ARBITRO')")
    @Operation(summary = "Descargar reporte de liquidación en PDF o Excel")
    public ResponseEntity<byte[]> descargarReporte(
            @PathVariable UUID id, @RequestParam(defaultValue = "pdf") String formato) {
        var reporte = liquidacionService.generarReporte(id, formato);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + reporte.nombreArchivo())
                .contentType(MediaType.parseMediaType(reporte.contentType()))
                .body(reporte.contenido());
    }
}

