package com.example.caba.application.dto;

import com.example.caba.domain.shared.enums.RolUsuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UsuarioRequest(
        @NotBlank String username,
        @NotBlank String password,
        @NotNull RolUsuario rol,
        UUID arbitroId) {}

