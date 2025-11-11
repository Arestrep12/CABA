package com.example.caba.application.service;

import com.example.caba.application.event.AsignacionActualizadaEvent;
import com.example.caba.application.event.AsignacionCreadaEvent;
import com.example.caba.application.event.LiquidacionActualizadaEvent;
import com.example.caba.application.event.LiquidacionGeneradaEvent;
import com.example.caba.application.event.PartidoProgramadoEvent;
import com.example.caba.application.event.PartidoReprogramadoEvent;
import com.example.caba.domain.asignacion.Asignacion;
import com.example.caba.domain.pago.Liquidacion;
import com.example.caba.domain.partido.Partido;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificacionDomainService {

    private final ApplicationEventPublisher publisher;

    public void publicarAsignacionCreada(Asignacion asignacion) {
        Partido partido = asignacion.getPartido();
        AsignacionCreadaEvent event = new AsignacionCreadaEvent(
                asignacion.getId(),
                partido.getId(),
                asignacion.getArbitro().getId(),
                asignacion.getRol(),
                partido.getFechaProgramada());
        publisher.publishEvent(event);
    }

    public void publicarAsignacionActualizada(Asignacion asignacion) {
        publicarAsignacionActualizada(asignacion, null);
    }

    public void publicarAsignacionCancelada(Asignacion asignacion, String motivo) {
        publicarAsignacionActualizada(asignacion, motivo);
    }

    private void publicarAsignacionActualizada(Asignacion asignacion, String motivo) {
        Partido partido = asignacion.getPartido();
        AsignacionActualizadaEvent event = new AsignacionActualizadaEvent(
                asignacion.getId(),
                partido.getId(),
                asignacion.getArbitro().getId(),
                asignacion.getRol(),
                asignacion.getEstado(),
                partido.getFechaProgramada(),
                motivo);
        publisher.publishEvent(event);
    }

    public void publicarPartidoProgramado(Partido partido) {
        UUID torneoId = partido.getTorneo() != null ? partido.getTorneo().getId() : null;
        PartidoProgramadoEvent event = new PartidoProgramadoEvent(
                partido.getId(),
                torneoId,
                partido.getFechaProgramada(),
                partido.getSede(),
                partido.getCategoria());
        publisher.publishEvent(event);
    }

    public void publicarPartidoReprogramado(
            Partido partido, LocalDateTime fechaAnterior, LocalDateTime fechaNueva) {
        UUID torneoId = partido.getTorneo() != null ? partido.getTorneo().getId() : null;
        PartidoReprogramadoEvent event = new PartidoReprogramadoEvent(
                partido.getId(),
                torneoId,
                fechaAnterior,
                fechaNueva,
                partido.getSede(),
                partido.getCategoria());
        publisher.publishEvent(event);
    }

    public void publicarLiquidacionGenerada(Liquidacion liquidacion) {
        LiquidacionGeneradaEvent event = new LiquidacionGeneradaEvent(
                liquidacion.getId(),
                liquidacion.getArbitro().getId(),
                liquidacion.getTotal(),
                liquidacion.getPeriodo() != null ? liquidacion.getPeriodo().getFechaInicio() : null,
                liquidacion.getPeriodo() != null ? liquidacion.getPeriodo().getFechaFin() : null);
        publisher.publishEvent(event);
    }

    public void publicarLiquidacionActualizada(Liquidacion liquidacion) {
        LiquidacionActualizadaEvent event = new LiquidacionActualizadaEvent(
                liquidacion.getId(),
                liquidacion.getArbitro().getId(),
                liquidacion.getEstado(),
                liquidacion.getTotal());
        publisher.publishEvent(event);
    }
}

