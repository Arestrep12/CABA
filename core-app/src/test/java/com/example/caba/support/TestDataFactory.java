package com.example.caba.support;

import com.example.caba.application.dto.ArbitroDto;
import com.example.caba.application.dto.ArbitroRequest;
import com.example.caba.application.dto.AsignacionDto;
import com.example.caba.application.dto.DisponibilidadDto;
import com.example.caba.application.dto.PartidoDto;
import com.example.caba.application.dto.PeriodoDto;
import com.example.caba.application.dto.TarifaDto;
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
import com.example.caba.domain.shared.valueobject.Disponibilidad;
import com.example.caba.domain.torneo.Torneo;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class TestDataFactory {

    private TestDataFactory() {}

    public static Arbitro arbitro() {
        return Arbitro.builder()
                .id(UUID.randomUUID())
                .nombres("Juan")
                .apellidos("Pérez")
                .documento(Documento.of(TipoDocumento.DNI, "12345678"))
                .email(Email.of("juan.perez@example.com"))
                .especialidad(Especialidad.CAMPO)
                .escalafon(Escalafon.FIBA)
                .fechaIngreso(LocalDate.now().minusYears(2))
                .disponibilidades(
                        Set.of(Disponibilidad.of(LocalDate.now().getDayOfWeek(), LocalTime.of(18, 0), LocalTime.of(22, 0))))
                .build();
    }

    public static Torneo torneo() {
        return Torneo.builder()
                .id(UUID.randomUUID())
                .nombre("Liga Metropolitana")
                .descripcion("Torneo de prueba")
                .periodo(com.example.caba.domain.shared.valueobject.Periodo.of(
                        LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(2)))
                .build();
    }

    public static Partido partido() {
        return partido(null);
    }

    public static Partido partido(Torneo torneo) {
        Torneo torneoAsociado = torneo != null ? torneo : torneo();
        return Partido.builder()
                .id(UUID.randomUUID())
                .torneo(torneoAsociado)
                .fechaProgramada(LocalDateTime.now().plusDays(2))
                .sede("Estadio Parque")
                .categoria("U18")
                .build();
    }

    public static Asignacion asignacion(Arbitro arbitro, Partido partido) {
        return Asignacion.builder()
                .id(UUID.randomUUID())
                .arbitro(arbitro != null ? arbitro : arbitro())
                .partido(partido != null ? partido : partido())
                .rol(RolAsignacion.PRIMER_ARBITRO)
                .estado(EstadoAsignacion.ACEPTADA)
                .build();
    }

    public static ArbitroDto arbitroDto() {
        return new ArbitroDto(UUID.randomUUID(), "Juan", "Pérez", "juan.perez@example.com", Especialidad.CAMPO, Escalafon.FIBA, null, true, LocalDate.now().minusYears(1),
                List.of(new DisponibilidadDto(LocalDate.now().getDayOfWeek(), LocalTime.of(18, 0), LocalTime.of(22, 0))));
    }

    public static PartidoDto partidoDto() {
        UUID partidoId = UUID.randomUUID();
        UUID torneoId = UUID.randomUUID();
        return new PartidoDto(
                partidoId,
                torneoId,
                LocalDateTime.now().plusDays(4),
                "Estadio Parque",
                "U18",
                List.of(new AsignacionDto(
                        UUID.randomUUID(), partidoId, UUID.randomUUID(), RolAsignacion.PRIMER_ARBITRO, EstadoAsignacion.ACEPTADA, LocalDateTime.now())));
    }

    public static com.example.caba.application.dto.TorneoDto torneoDto() {
        PeriodoDto periodo = new PeriodoDto(LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(3));
        return new com.example.caba.application.dto.TorneoDto(
                UUID.randomUUID(),
                "Liga Metropolitana",
                "Torneo de clubes de Buenos Aires",
                periodo,
                List.of(new TarifaDto(Escalafon.FIBA, RolAsignacion.PRIMER_ARBITRO, BigDecimal.valueOf(25000))));
    }

    public static AsignacionDto asignacionDto() {
        return new AsignacionDto(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                RolAsignacion.PRIMER_ARBITRO,
                EstadoAsignacion.ACEPTADA,
                LocalDateTime.now());
    }

    public static ArbitroRequest arbitroRequest() {
        return new ArbitroRequest(
                "Juan",
                "Pérez",
                "juan.perez@example.com",
                TipoDocumento.DNI,
                "12345678",
                Especialidad.CAMPO,
                Escalafon.FIBA,
                null,
                LocalDate.now().minusYears(1),
                List.of(new DisponibilidadDto(
                        LocalDate.now().getDayOfWeek(), LocalTime.of(18, 0), LocalTime.of(22, 0))));
    }
}

