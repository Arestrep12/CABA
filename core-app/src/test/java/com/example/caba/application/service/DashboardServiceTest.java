package com.example.caba.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.example.caba.application.dto.ClimaResumenDto;
import com.example.caba.application.dto.DashboardResumenDto;
import com.example.caba.application.dto.TopArbitroDto;
import com.example.caba.domain.arbitro.Arbitro;
import com.example.caba.domain.asignacion.Asignacion;
import com.example.caba.domain.partido.Partido;
import com.example.caba.domain.shared.enums.Escalafon;
import com.example.caba.domain.shared.enums.Especialidad;
import com.example.caba.domain.shared.enums.EstadoAsignacion;
import com.example.caba.domain.shared.enums.RolAsignacion;
import com.example.caba.domain.shared.enums.TipoDocumento;
import com.example.caba.domain.shared.valueobject.Documento;
import com.example.caba.domain.shared.valueobject.Email;
import com.example.caba.domain.shared.valueobject.Periodo;
import com.example.caba.domain.torneo.Torneo;
import com.example.caba.infrastructure.repository.AsignacionRepository;
import com.example.caba.infrastructure.repository.TorneoRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private AsignacionRepository asignacionRepository;

    @Mock
    private TorneoRepository torneoRepository;

    @Mock
    private ClimaService climaService;

    @InjectMocks
    private DashboardService dashboardService;

    @Test
    void obtenerResumen_calculaMetricasCompletas() {
        Arbitro arbitroPrincipal = Arbitro.builder()
                .id(UUID.randomUUID())
                .nombres("Juan")
                .apellidos("Pérez")
                .documento(Documento.of(TipoDocumento.DNI, "12345678"))
                .email(Email.of("juan@example.com"))
                .especialidad(Especialidad.CAMPO)
                .escalafon(Escalafon.FIBA)
                .build();

        Arbitro arbitroSecundario = Arbitro.builder()
                .id(UUID.randomUUID())
                .nombres("Luis")
                .apellidos("Gómez")
                .documento(Documento.of(TipoDocumento.DNI, "87654321"))
                .email(Email.of("luis@example.com"))
                .especialidad(Especialidad.MESA)
                .escalafon(Escalafon.PRIMERA)
                .build();

        LocalDateTime base = LocalDateTime.now();
        Partido partido1 = Partido.builder()
                .id(UUID.randomUUID())
                .fechaProgramada(base.minusDays(2))
                .sede("Sede 1")
                .categoria("U18")
                .build();
        Partido partido2 = Partido.builder()
                .id(UUID.randomUUID())
                .fechaProgramada(base.minusDays(1))
                .sede("Sede 2")
                .categoria("U20")
                .build();

        Asignacion aceptada1 = Asignacion.builder()
                .id(UUID.randomUUID())
                .arbitro(arbitroPrincipal)
                .partido(partido1)
                .rol(RolAsignacion.PRIMER_ARBITRO)
                .estado(EstadoAsignacion.ACEPTADA)
                .build();
        Asignacion aceptada2 = Asignacion.builder()
                .id(UUID.randomUUID())
                .arbitro(arbitroPrincipal)
                .partido(partido2)
                .rol(RolAsignacion.SEGUNDO_ARBITRO)
                .estado(EstadoAsignacion.ACEPTADA)
                .build();
        Asignacion rechazada = Asignacion.builder()
                .id(UUID.randomUUID())
                .arbitro(arbitroSecundario)
                .partido(partido2)
                .rol(RolAsignacion.APUNTADOR)
                .estado(EstadoAsignacion.RECHAZADA)
                .build();
        Asignacion pendiente = Asignacion.builder()
                .id(UUID.randomUUID())
                .arbitro(arbitroPrincipal)
                .partido(partido1)
                .rol(RolAsignacion.CRONOMETRADOR)
                .estado(EstadoAsignacion.PENDIENTE)
                .build();

        when(asignacionRepository.findAll())
                .thenReturn(List.of(aceptada1, aceptada2, rechazada, pendiente));

        Torneo torneoVigente = Torneo.builder()
                .id(UUID.randomUUID())
                .nombre("Liga Metropolitana")
                .periodo(Periodo.of(LocalDate.now().minusDays(3), LocalDate.now().plusDays(10)))
                .build();
        Torneo torneoVencido = Torneo.builder()
                .id(UUID.randomUUID())
                .nombre("Liga Histórica")
                .periodo(Periodo.of(LocalDate.now().minusMonths(3), LocalDate.now().minusMonths(2)))
                .build();

        when(torneoRepository.findAll()).thenReturn(List.of(torneoVigente, torneoVencido));
        ClimaResumenDto climaResumen = new ClimaResumenDto("Buenos Aires", "Despejado", 22.5, 21.8, 60);
        when(climaService.obtenerClimaActual()).thenReturn(Optional.of(climaResumen));

        DashboardResumenDto resumen = dashboardService.obtenerResumen();

        assertThat(resumen.totalAsignaciones()).isEqualTo(4);
        assertThat(resumen.totalAceptadas()).isEqualTo(2);
        assertThat(resumen.totalRechazadas()).isEqualTo(1);
        assertThat(resumen.totalPendientes()).isEqualTo(1);
        assertThat(resumen.porcentajeAceptadas()).isEqualTo(50.0);
        assertThat(resumen.torneosVigentes()).isEqualTo(1);
        assertThat(resumen.topArbitros()).hasSize(2);
        assertThat(resumen.topArbitros().get(0).arbitroId()).isEqualTo(arbitroPrincipal.getId());
        assertThat(resumen.topArbitros().get(0).nombreCompleto()).isEqualTo("Juan Pérez");
        assertThat(resumen.topArbitros().get(0).asignaciones()).isEqualTo(3);
        assertThat(resumen.topArbitros().stream().map(TopArbitroDto::arbitroId))
                .containsExactly(arbitroPrincipal.getId(), arbitroSecundario.getId());
        assertThat(resumen.climaActual()).isEqualTo(climaResumen);
    }
}
