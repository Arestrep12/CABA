package com.example.caba.api.controller;

import com.example.caba.application.dto.PartidoDto;
import com.example.caba.application.dto.PartidoRequest;
import com.example.caba.application.service.PartidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/partidos")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Partidos", description = "Programación y consulta de partidos")
public class PartidoApiController {

    private final PartidoService partidoService;

    @PostMapping
    @Operation(summary = "Programar un nuevo partido")
    public ResponseEntity<PartidoDto> programar(@Valid @RequestBody PartidoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(partidoService.programarPartido(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Reprogramar un partido existente")
    public PartidoDto reprogramar(@PathVariable UUID id, @Valid @RequestBody PartidoRequest request) {
        return partidoService.reprogramarPartido(id, request);
    }

    @GetMapping("/torneo/{torneoId}")
    @Operation(summary = "Listar partidos por torneo")
    public List<PartidoDto> porTorneo(@PathVariable UUID torneoId) {
        return partidoService.listarPorTorneo(torneoId);
    }

    @GetMapping
    @Operation(summary = "Buscar partidos por rango de fechas")
    public List<PartidoDto> porRango(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return partidoService.buscarPorRango(inicio, fin);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un partido")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        partidoService.eliminarPartido(id);
        return ResponseEntity.noContent().build();
    }
}

