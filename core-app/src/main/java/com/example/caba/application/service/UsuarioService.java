package com.example.caba.application.service;

import com.example.caba.application.dto.UsuarioDto;
import com.example.caba.application.dto.UsuarioRequest;
import com.example.caba.application.mapper.UsuarioMapper;
import com.example.caba.domain.shared.enums.RolUsuario;
import com.example.caba.domain.shared.exception.BusinessRuleException;
import com.example.caba.domain.shared.exception.NotFoundException;
import com.example.caba.domain.usuario.Usuario;
import com.example.caba.infrastructure.repository.UsuarioRepository;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final ArbitroService arbitroService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioDto crearUsuario(@Valid UsuarioRequest request) {
        usuarioRepository
                .findByUsernameIgnoreCase(request.username())
                .ifPresent(usuario -> {
                    throw new BusinessRuleException("El nombre de usuario ya está registrado");
                });

        Usuario usuario = Usuario.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .rol(request.rol())
                .build();

        if (RolUsuario.ARBITRO.equals(request.rol()) && request.arbitroId() != null) {
            usuario.setArbitro(arbitroService.obtenerEntidad(request.arbitroId()));
        }

        return usuarioMapper.toDto(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioDto actualizarEstado(UUID usuarioId, boolean enabled) {
        Usuario usuario = obtenerEntidad(usuarioId);
        usuario.setEnabled(enabled);
        return usuarioMapper.toDto(usuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioDto> listarUsuarios() {
        return usuarioMapper.toDtoList(usuarioRepository.findAll());
    }

    @Transactional(readOnly = true)
    public UsuarioDto obtenerUsuario(UUID usuarioId) {
        return usuarioMapper.toDto(obtenerEntidad(usuarioId));
    }

    private Usuario obtenerEntidad(UUID usuarioId) {
        return usuarioRepository
                .findById(usuarioId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
    }
}

