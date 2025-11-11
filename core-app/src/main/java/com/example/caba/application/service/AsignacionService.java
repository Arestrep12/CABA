package com.example.caba.application.service;

import com.example.caba.application.dto.AsignacionDto;
import com.example.caba.application.dto.AsignacionRequest;
import com.example.caba.application.mapper.AsignacionMapper;
import com.example.caba.domain.arbitro.Arbitro;
import com.example.caba.domain.asignacion.Asignacion;
import com.example.caba.domain.partido.Partido;
import com.example.caba.domain.shared.enums.EstadoAsignacion;
import com.example.caba.domain.shared.exception.BusinessRuleException;
import com.example.caba.domain.shared.exception.NotFoundException;
import com.example.caba.infrastructure.repository.AsignacionRepository;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AsignacionService {

    private final AsignacionRepository asignacionRepository;
    private final ArbitroService arbitroService;
    private final PartidoService partidoService;
    private final AsignacionMapper asignacionMapper;
    private final NotificacionDomainService notificacionDomainService;
    private final AgendaService agendaService;

    @Transactional
    public AsignacionDto asignarArbitro(@Valid AsignacionRequest request) {
        Arbitro arbitro = arbitroService.obtenerEntidad(request.arbitroId());
        if (!arbitro.isActivo()) {
            throw new BusinessRuleException("El árbitro está inactivo y no puede ser asignado");
        }

        Partido partido = partidoService.obtenerEntidad(request.partidoId());

        asignacionRepository
                .findByPartidoAndRol(partido, request.rol())
                .ifPresent(asignacion -> {
                    throw new BusinessRuleException("Ya existe una asignación para ese rol en el partido");
                });

        if (asignacionRepository.existsByArbitroAndPartido(arbitro, partido)) {
            throw new BusinessRuleException("El árbitro ya está asignado a este partido");
        }

        Asignacion asignacion = asignacionMapper.toEntity(request);
        asignacion.setArbitro(arbitro);
        asignacion.setPartido(partido);

        Asignacion saved = asignacionRepository.save(asignacion);
        notificacionDomainService.publicarAsignacionCreada(saved);
        agendaService.sincronizarAsignacion(saved.getId(), arbitro.getId(), partido.getId());
        return asignacionMapper.toDto(saved);
    }

    @Transactional
    public AsignacionDto responderAsignacion(UUID asignacionId, boolean aceptar) {
        Asignacion asignacion = obtenerEntidad(asignacionId);
        if (asignacion.getEstado() != EstadoAsignacion.PENDIENTE) {
            throw new BusinessRuleException("La asignación ya fue respondida o cancelada");
        }
        if (aceptar) {
            asignacion.aceptar();
        } else {
            asignacion.rechazar();
        }
        notificacionDomainService.publicarAsignacionActualizada(asignacion);
        return asignacionMapper.toDto(asignacion);
    }

    @Transactional
    public void cancelarAsignacion(UUID asignacionId, String motivo) {
        Asignacion asignacion = obtenerEntidad(asignacionId);
        asignacion.cancelar();
        notificacionDomainService.publicarAsignacionCancelada(asignacion, motivo);
    }

    @Transactional(readOnly = true)
    public List<AsignacionDto> listarPorPartido(UUID partidoId) {
        Partido partido = partidoService.obtenerEntidad(partidoId);
        return asignacionMapper.toDtoList(partido.getAsignaciones());
    }

    @Transactional(readOnly = true)
    public List<Asignacion> obtenerAsignacionesAceptadasEntre(
            UUID arbitroId, LocalDateTime inicio, LocalDateTime fin) {
        Arbitro arbitro = arbitroService.obtenerEntidad(arbitroId);
        return asignacionRepository.findByArbitroAndEstadoAndPartido_FechaProgramadaBetween(
                arbitro, EstadoAsignacion.ACEPTADA, inicio, fin);
    }

    private Asignacion obtenerEntidad(UUID asignacionId) {
        return asignacionRepository
                .findById(asignacionId)
                .orElseThrow(() -> new NotFoundException("Asignación no encontrada"));
    }
}

