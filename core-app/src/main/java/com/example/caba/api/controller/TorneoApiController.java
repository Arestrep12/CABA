package com.example.caba.api.controller;

import com.example.caba.application.dto.TorneoDto;
import com.example.caba.application.dto.TorneoRequest;
import com.example.caba.application.service.TorneoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/torneos")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Torneos", description = "Administración de torneos y periodos")
public class TorneoApiController {

    private final TorneoService torneoService;

    @GetMapping
    @Operation(summary = "Listar todos los torneos")
    public List<TorneoDto> listar() {
        return torneoService.listarTorneos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un torneo por identificador")
    public TorneoDto obtener(@PathVariable UUID id) {
        return torneoService.obtenerTorneo(id);
    }

    @PostMapping
    @Operation(summary = "Crear un torneo")
    public ResponseEntity<TorneoDto> crear(@Valid @RequestBody TorneoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(torneoService.crearTorneo(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un torneo")
    public TorneoDto actualizar(@PathVariable UUID id, @Valid @RequestBody TorneoRequest request) {
        return torneoService.actualizarTorneo(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un torneo")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        torneoService.eliminarTorneo(id);
        return ResponseEntity.noContent().build();
    }
}

