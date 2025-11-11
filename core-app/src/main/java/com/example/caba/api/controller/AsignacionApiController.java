package com.example.caba.api.controller;

import com.example.caba.application.dto.AsignacionDto;
import com.example.caba.application.dto.AsignacionRequest;
import com.example.caba.application.service.AsignacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/asignaciones")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Asignaciones", description = "Gestión y respuesta de asignaciones")
public class AsignacionApiController {

    private final AsignacionService asignacionService;

    @PostMapping
    @Operation(summary = "Asignar un árbitro a un partido")
    public ResponseEntity<AsignacionDto> asignar(@Valid @RequestBody AsignacionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(asignacionService.asignarArbitro(request));
    }

    @PostMapping("/{id}/respuesta")
    @PreAuthorize("hasAnyRole('ADMIN','ARBITRO')")
    @Operation(summary = "Responder a una asignación")
    public AsignacionDto responder(@PathVariable UUID id, @RequestParam("acepta") boolean acepta) {
        return asignacionService.responderAsignacion(id, acepta);
    }

    @PostMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar una asignación")
    public ResponseEntity<Void> cancelar(
            @PathVariable UUID id, @RequestParam(value = "motivo", required = false) String motivo) {
        asignacionService.cancelarAsignacion(id, motivo);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/partido/{partidoId}")
    @PreAuthorize("hasAnyRole('ADMIN','ARBITRO')")
    @Operation(summary = "Listar asignaciones por partido")
    public List<AsignacionDto> porPartido(@PathVariable UUID partidoId) {
        return asignacionService.listarPorPartido(partidoId);
    }
}

