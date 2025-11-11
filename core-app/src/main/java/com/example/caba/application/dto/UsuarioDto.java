package com.example.caba.application.dto;

import com.example.caba.domain.shared.enums.RolUsuario;
import java.util.UUID;

public record UsuarioDto(UUID id, String username, RolUsuario rol, boolean enabled, UUID arbitroId) {}

