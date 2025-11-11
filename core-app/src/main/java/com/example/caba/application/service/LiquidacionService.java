package com.example.caba.application.service;

import com.example.caba.application.dto.LiquidacionDto;
import com.example.caba.application.dto.PeriodoDto;
import com.example.caba.application.mapper.LiquidacionMapper;
import com.example.caba.application.mapper.SharedMapper;
import com.example.caba.application.report.Reporte;
import com.example.caba.application.report.ReporteGenerador;
import com.example.caba.domain.arbitro.Arbitro;
import com.example.caba.domain.asignacion.Asignacion;
import com.example.caba.domain.pago.Liquidacion;
import com.example.caba.domain.pago.LiquidacionDetalle;
import com.example.caba.domain.partido.Partido;
import com.example.caba.domain.shared.enums.Escalafon;
import com.example.caba.domain.shared.enums.RolAsignacion;
import com.example.caba.domain.shared.exception.BusinessRuleException;
import com.example.caba.domain.shared.exception.NotFoundException;
import com.example.caba.domain.shared.valueobject.Periodo;
import com.example.caba.infrastructure.repository.LiquidacionRepository;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LiquidacionService {

    private final LiquidacionRepository liquidacionRepository;
    private final AsignacionService asignacionService;
    private final ArbitroService arbitroService;
    private final SharedMapper sharedMapper;
    private final LiquidacionMapper liquidacionMapper;
    private final NotificacionDomainService notificacionDomainService;
    private final List<ReporteGenerador> reporteGeneradores;

    @Transactional
    public LiquidacionDto generarLiquidacion(UUID arbitroId, @Valid PeriodoDto periodoDto) {
        Periodo periodo = sharedMapper.toPeriodo(periodoDto);
        if (periodo == null) {
            throw new BusinessRuleException("El periodo es obligatorio");
        }

        liquidacionRepository
                .findByArbitroIdAndPeriodo_FechaInicioAndPeriodo_FechaFin(
                        arbitroId, periodo.getFechaInicio(), periodo.getFechaFin())
                .ifPresent(liquidacion -> {
                    throw new BusinessRuleException("Ya existe una liquidación para ese periodo");
                });

        LocalDateTime inicio = periodo.getFechaInicio().atStartOfDay();
        LocalDateTime fin = periodo.getFechaFin().plusDays(1).atStartOfDay().minusSeconds(1);
        List<Asignacion> asignaciones =
                asignacionService.obtenerAsignacionesAceptadasEntre(arbitroId, inicio, fin);

        if (asignaciones.isEmpty()) {
            throw new BusinessRuleException("No se encontraron asignaciones aceptadas en el periodo");
        }

        Arbitro arbitro = arbitroService.obtenerEntidad(arbitroId);
        Liquidacion liquidacion = Liquidacion.builder()
                .arbitro(arbitro)
                .periodo(periodo)
                .build();

        asignaciones.stream()
                .sorted(Comparator.comparing(a -> a.getPartido().getFechaProgramada()))
                .forEach(asignacion -> {
                    BigDecimal monto = calcularMontoAsignacion(arbitro.getEscalafon(), asignacion);
                    if (monto.signum() == 0) {
                        return;
                    }
                    Partido partido = asignacion.getPartido();
                    LiquidacionDetalle detalle = LiquidacionDetalle.of(
                            partido.getId(),
                            asignacion.getRol(),
                            monto,
                            partido.getFechaProgramada().toLocalDate());
                    liquidacion.agregarDetalle(detalle);
                });

        if (liquidacion.getDetalles().isEmpty()) {
            throw new BusinessRuleException("No se pudieron calcular montos para las asignaciones del periodo");
        }
        Liquidacion saved = liquidacionRepository.save(liquidacion);
        notificacionDomainService.publicarLiquidacionGenerada(saved);
        return liquidacionMapper.toDto(saved);
    }

    @Transactional
    public void marcarPagada(UUID liquidacionId) {
        Liquidacion liquidacion = obtenerEntidad(liquidacionId);
        liquidacion.marcarPagada();
        notificacionDomainService.publicarLiquidacionActualizada(liquidacion);
    }

    @Transactional(readOnly = true)
    public List<LiquidacionDto> listarPorArbitro(UUID arbitroId) {
        Arbitro arbitro = arbitroService.obtenerEntidad(arbitroId);
        return liquidacionMapper.toDtoList(liquidacionRepository.findByArbitro(arbitro));
    }

    @Transactional(readOnly = true)
    public List<LiquidacionDto> listarPorEstado() {
        return liquidacionMapper.toDtoList(liquidacionRepository.findAll());
    }

    private Liquidacion obtenerEntidad(UUID liquidacionId) {
        return liquidacionRepository
                .findById(liquidacionId)
                .orElseThrow(() -> new NotFoundException("Liquidación no encontrada"));
    }

    private BigDecimal calcularMontoAsignacion(Escalafon escalafon, Asignacion asignacion) {
        Partido partido = asignacion.getPartido();
        if (partido.getTorneo() == null || partido.getTorneo().getTarifas() == null) {
            return BigDecimal.ZERO;
        }

        RolAsignacion rol = asignacion.getRol();
        Optional<BigDecimal> tarifa = partido.getTorneo().getTarifas().stream()
                .filter(t -> t.getEscalafon() == escalafon && t.getRol() == rol)
                .map(com.example.caba.domain.shared.valueobject.Tarifa::getMonto)
                .findFirst();
        return tarifa.orElse(BigDecimal.ZERO);
    }

    @Transactional(readOnly = true)
    public List<LiquidacionDto> generarResumenMensual(LocalDate periodo) {
        LocalDate inicio = periodo.withDayOfMonth(1);
        LocalDate fin = inicio.plusMonths(1).minusDays(1);

        return liquidacionRepository.findAll().stream()
                .filter(liquidacion -> {
                    Periodo liquidacionPeriodo = liquidacion.getPeriodo();
                    if (liquidacionPeriodo == null) {
                        return false;
                    }
                    LocalDate fechaInicio = liquidacionPeriodo.getFechaInicio();
                    return fechaInicio.getMonth() == periodo.getMonth()
                            && fechaInicio.getYear() == periodo.getYear();
                })
                .map(liquidacionMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Reporte generarReporte(UUID liquidacionId, String formato) {
        Liquidacion liquidacion = obtenerEntidad(liquidacionId);
        LiquidacionDto dto = liquidacionMapper.toDto(liquidacion);

        ReporteGenerador generador = reporteGeneradores.stream()
                .filter(g -> g.soportaFormato(formato))
                .findFirst()
                .orElseThrow(() -> new BusinessRuleException("Formato de reporte no soportado: " + formato));

        return generador.generar(dto);
    }
}

