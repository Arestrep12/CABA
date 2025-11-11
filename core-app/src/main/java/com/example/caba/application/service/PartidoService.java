package com.example.caba.application.service;

import com.example.caba.application.dto.PartidoDto;
import com.example.caba.application.dto.PartidoRequest;
import com.example.caba.application.mapper.PartidoMapper;
import com.example.caba.domain.partido.Partido;
import com.example.caba.domain.shared.exception.BusinessRuleException;
import com.example.caba.domain.shared.exception.NotFoundException;
import com.example.caba.domain.torneo.Torneo;
import com.example.caba.infrastructure.repository.PartidoRepository;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PartidoService {

    private final PartidoRepository partidoRepository;
    private final TorneoService torneoService;
    private final PartidoMapper partidoMapper;
    private final NotificacionDomainService notificacionDomainService;

    @Transactional
    public PartidoDto programarPartido(@Valid PartidoRequest request) {
        Torneo torneo = torneoService.obtenerEntidad(request.torneoId());
        validarFechaDentroDePeriodo(torneo, request.fechaProgramada());

        Partido partido = partidoMapper.toEntity(request);
        partido.setTorneo(torneo);
        torneo.agregarPartido(partido);

        Partido saved = partidoRepository.save(partido);
        notificacionDomainService.publicarPartidoProgramado(saved);
        return partidoMapper.toDto(saved);
    }

    @Transactional
    public PartidoDto reprogramarPartido(UUID partidoId, @Valid PartidoRequest request) {
        Partido partido = obtenerEntidad(partidoId);
        Torneo torneo = torneoService.obtenerEntidad(request.torneoId());
        LocalDateTime fechaAnterior = partido.getFechaProgramada();
        validarFechaDentroDePeriodo(torneo, request.fechaProgramada());

        partido.setTorneo(torneo);
        partido.programarPara(request.fechaProgramada());
        partido.actualizarDatos(request.sede(), request.categoria());

        notificacionDomainService.publicarPartidoReprogramado(
                partido, fechaAnterior, request.fechaProgramada());
        return partidoMapper.toDto(partido);
    }

    @Transactional(readOnly = true)
    public List<PartidoDto> listarPorTorneo(UUID torneoId) {
        Torneo torneo = torneoService.obtenerEntidad(torneoId);
        return partidoMapper.toDtoList(partidoRepository.findByTorneoOrderByFechaProgramadaAsc(torneo));
    }

    @Transactional(readOnly = true)
    public List<PartidoDto> buscarPorRango(LocalDate inicio, LocalDate fin) {
        LocalDateTime desde = inicio.atStartOfDay();
        LocalDateTime hasta = fin.plusDays(1).atStartOfDay().minusSeconds(1);
        return partidoMapper.toDtoList(partidoRepository.findByFechaProgramadaBetween(desde, hasta));
    }

    @Transactional
    public void eliminarPartido(UUID partidoId) {
        Partido partido = obtenerEntidad(partidoId);
        partidoRepository.delete(partido);
    }

    Partido obtenerEntidad(UUID partidoId) {
        return partidoRepository
                .findById(partidoId)
                .orElseThrow(() -> new NotFoundException("Partido no encontrado"));
    }

    private void validarFechaDentroDePeriodo(Torneo torneo, LocalDateTime fechaProgramada) {
        if (torneo.getPeriodo() == null) {
            return;
        }
        LocalDate fecha = fechaProgramada.toLocalDate();
        boolean dentro = torneo.getPeriodo().contiene(fecha);
        if (!dentro) {
            throw new BusinessRuleException("La fecha del partido no está dentro del periodo del torneo");
        }
    }
}

