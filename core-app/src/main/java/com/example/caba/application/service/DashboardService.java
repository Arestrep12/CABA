package com.example.caba.application.service;

import com.example.caba.application.dto.ClimaResumenDto;
import com.example.caba.application.dto.DashboardResumenDto;
import com.example.caba.application.dto.TopArbitroDto;
import com.example.caba.domain.asignacion.Asignacion;
import com.example.caba.domain.shared.enums.EstadoAsignacion;
import com.example.caba.infrastructure.repository.AsignacionRepository;
import com.example.caba.infrastructure.repository.TorneoRepository;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final AsignacionRepository asignacionRepository;
    private final TorneoRepository torneoRepository;
    private final ClimaService climaService;

    @Transactional(readOnly = true)
    public DashboardResumenDto obtenerResumen() {
        List<Asignacion> asignaciones = asignacionRepository.findAll();
        long total = asignaciones.size();
        long aceptadas = asignaciones.stream()
                .filter(asignacion -> asignacion.getEstado() == EstadoAsignacion.ACEPTADA)
                .count();
        long rechazadas = asignaciones.stream()
                .filter(asignacion -> asignacion.getEstado() == EstadoAsignacion.RECHAZADA)
                .count();
        long pendientes = asignaciones.stream()
                .filter(asignacion -> asignacion.getEstado() == EstadoAsignacion.PENDIENTE)
                .count();

        double porcentajeAceptadas = total == 0 ? 0.0 : (double) aceptadas * 100.0 / total;

        Map<UUID, Long> asignacionesPorArbitro = asignaciones.stream()
                .filter(asignacion -> asignacion.getArbitro() != null)
                .collect(Collectors.groupingBy(asignacion -> asignacion.getArbitro().getId(), Collectors.counting()));

        List<TopArbitroDto> topArbitros = asignacionesPorArbitro.entrySet().stream()
                .sorted(Map.Entry.<UUID, Long>comparingByValue(Comparator.reverseOrder()))
                .limit(5)
                .map(entry -> {
                    UUID arbitroId = entry.getKey();
                    String nombreCompleto = asignaciones.stream()
                            .map(Asignacion::getArbitro)
                            .filter(arbitro -> arbitro != null && arbitroId.equals(arbitro.getId()))
                            .findFirst()
                            .map(arbitro -> arbitro.getNombres() + " " + arbitro.getApellidos())
                            .orElse("N/A");
                    return new TopArbitroDto(arbitroId, nombreCompleto, entry.getValue());
                })
                .toList();

        long torneosVigentes = torneoRepository.findAll().stream()
                .filter(torneo -> torneo.getPeriodo() != null
                        && torneo.getPeriodo().contiene(LocalDate.now()))
                .count();

        ClimaResumenDto climaActual = climaService.obtenerClimaActual().orElse(null);

        return new DashboardResumenDto(
                total,
                aceptadas,
                rechazadas,
                pendientes,
                porcentajeAceptadas,
                torneosVigentes,
                topArbitros,
                climaActual);
    }
}

