package com.example.caba.application.dto;

import com.example.caba.domain.shared.enums.Escalafon;
import com.example.caba.domain.shared.enums.Especialidad;
import com.example.caba.domain.shared.enums.TipoDocumento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.List;

public record ArbitroRequest(
        @NotBlank String nombres,
        @NotBlank String apellidos,
        @Email @NotBlank String email,
        @NotNull TipoDocumento tipoDocumento,
        @NotBlank String numeroDocumento,
        @NotNull Especialidad especialidad,
        @NotNull Escalafon escalafon,
        String fotoUrl,
        @PastOrPresent LocalDate fechaIngreso,
        @Valid List<DisponibilidadDto> disponibilidades) {}

