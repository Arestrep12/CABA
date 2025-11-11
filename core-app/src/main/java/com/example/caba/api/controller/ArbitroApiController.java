package com.example.caba.api.controller;

import com.example.caba.application.dto.ArbitroDto;
import com.example.caba.application.dto.ArbitroRequest;
import com.example.caba.application.service.ArbitroService;
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
@RequestMapping("/api/arbitros")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Árbitros", description = "Operaciones de administración de árbitros")
public class ArbitroApiController {

    private final ArbitroService arbitroService;

    @GetMapping
    @Operation(summary = "Listar árbitros activos")
    public List<ArbitroDto> listar() {
        return arbitroService.listarActivos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un árbitro por identificador")
    public ArbitroDto obtener(@PathVariable UUID id) {
        return arbitroService.obtenerArbitro(id);
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo árbitro")
    public ResponseEntity<ArbitroDto> crear(@Valid @RequestBody ArbitroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(arbitroService.registrarArbitro(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar la información de un árbitro")
    public ArbitroDto actualizar(@PathVariable UUID id, @Valid @RequestBody ArbitroRequest request) {
        return arbitroService.actualizarArbitro(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar un árbitro")
    public ResponseEntity<Void> desactivar(@PathVariable UUID id) {
        arbitroService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}

