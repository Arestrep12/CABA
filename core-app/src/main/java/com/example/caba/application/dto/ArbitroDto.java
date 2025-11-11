package com.example.caba.application.dto;

import com.example.caba.domain.shared.enums.Escalafon;
import com.example.caba.domain.shared.enums.Especialidad;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ArbitroDto(
        UUID id,
        String nombres,
        String apellidos,
        String email,
        Especialidad especialidad,
        Escalafon escalafon,
        String fotoUrl,
        boolean activo,
        LocalDate fechaIngreso,
        List<DisponibilidadDto> disponibilidades) {}

