package com.example.caba.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.caba.application.dto.LiquidacionDto;
import com.example.caba.application.mapper.LiquidacionMapper;
import com.example.caba.application.mapper.SharedMapper;
import com.example.caba.application.report.Reporte;
import com.example.caba.application.report.ReporteGenerador;
import com.example.caba.domain.pago.Liquidacion;
import com.example.caba.domain.shared.enums.EstadoLiquidacion;
import com.example.caba.domain.shared.exception.BusinessRuleException;
import com.example.caba.infrastructure.repository.LiquidacionRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LiquidacionServiceReportTest {

    @Mock
    private LiquidacionRepository liquidacionRepository;

    @Mock
    private AsignacionService asignacionService;

    @Mock
    private ArbitroService arbitroService;

    @Mock
    private SharedMapper sharedMapper;

    @Mock
    private LiquidacionMapper liquidacionMapper;

    @Mock
    private NotificacionDomainService notificacionDomainService;

    @Mock
    private ReporteGenerador pdfGenerador;

    @Mock
    private ReporteGenerador excelGenerador;

    private LiquidacionService liquidacionService;

    @BeforeEach
    void setUp() {
        liquidacionService = new LiquidacionService(
                liquidacionRepository,
                asignacionService,
                arbitroService,
                sharedMapper,
                liquidacionMapper,
                notificacionDomainService,
                List.of(pdfGenerador, excelGenerador));
    }

    @Test
    void generarReporteSeleccionaGeneradorAdecuado() {
        UUID liquidacionId = UUID.randomUUID();
        Liquidacion liquidacion = Liquidacion.builder().id(liquidacionId).build();
        LiquidacionDto dto = new LiquidacionDto(
                liquidacionId, UUID.randomUUID(), EstadoLiquidacion.GENERADA, null, BigDecimal.ZERO, List.of());
        Reporte reporte = new Reporte("liquidacion.pdf", "application/pdf", new byte[0]);

        when(liquidacionRepository.findById(liquidacionId)).thenReturn(Optional.of(liquidacion));
        when(liquidacionMapper.toDto(liquidacion)).thenReturn(dto);
        when(pdfGenerador.soportaFormato("pdf")).thenReturn(true);
        when(pdfGenerador.generar(dto)).thenReturn(reporte);
        Reporte resultado = liquidacionService.generarReporte(liquidacionId, "pdf");

        assertThat(resultado).isEqualTo(reporte);
        verify(pdfGenerador).generar(dto);
        verify(excelGenerador, never()).generar(any());
    }

    @Test
    void generarReporteFormatoNoSoportadoLanzaExcepcion() {
        UUID liquidacionId = UUID.randomUUID();
        Liquidacion liquidacion = Liquidacion.builder().id(liquidacionId).build();
        LiquidacionDto dto = new LiquidacionDto(
                liquidacionId, UUID.randomUUID(), EstadoLiquidacion.GENERADA, null, BigDecimal.ZERO, List.of());

        when(liquidacionRepository.findById(liquidacionId)).thenReturn(Optional.of(liquidacion));
        when(liquidacionMapper.toDto(liquidacion)).thenReturn(dto);
        when(pdfGenerador.soportaFormato("csv")).thenReturn(false);
        when(excelGenerador.soportaFormato("csv")).thenReturn(false);

        assertThrows(BusinessRuleException.class, () -> liquidacionService.generarReporte(liquidacionId, "csv"));
    }
}

