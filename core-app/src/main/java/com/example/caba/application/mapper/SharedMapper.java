package com.example.caba.application.mapper;

import com.example.caba.application.dto.DisponibilidadDto;
import com.example.caba.application.dto.LiquidacionDetalleDto;
import com.example.caba.application.dto.PeriodoDto;
import com.example.caba.application.dto.TarifaDto;
import com.example.caba.domain.pago.LiquidacionDetalle;
import com.example.caba.domain.shared.valueobject.Disponibilidad;
import com.example.caba.domain.shared.valueobject.Periodo;
import com.example.caba.domain.shared.valueobject.Tarifa;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SharedMapper {

    default Periodo toPeriodo(PeriodoDto dto) {
        if (dto == null) {
            return null;
        }
        return Periodo.of(dto.fechaInicio(), dto.fechaFin());
    }

    default PeriodoDto toPeriodoDto(Periodo periodo) {
        if (periodo == null) {
            return null;
        }
        return new PeriodoDto(periodo.getFechaInicio(), periodo.getFechaFin());
    }

    default Tarifa toTarifa(TarifaDto dto) {
        if (dto == null) {
            return null;
        }
        return Tarifa.of(dto.escalafon(), dto.rol(), dto.monto());
    }

    default TarifaDto toTarifaDto(Tarifa tarifa) {
        if (tarifa == null) {
            return null;
        }
        return new TarifaDto(tarifa.getEscalafon(), tarifa.getRol(), tarifa.getMonto());
    }

    default Set<Tarifa> toTarifas(List<TarifaDto> dtos) {
        if (dtos == null) {
            return Set.of();
        }
        return dtos.stream().filter(Objects::nonNull).map(this::toTarifa).collect(Collectors.toSet());
    }

    default List<TarifaDto> toTarifaDtos(Set<Tarifa> tarifas) {
        if (tarifas == null) {
            return List.of();
        }
        return tarifas.stream().filter(Objects::nonNull).map(this::toTarifaDto).toList();
    }

    default Disponibilidad toDisponibilidad(DisponibilidadDto dto) {
        if (dto == null) {
            return null;
        }
        return Disponibilidad.of(dto.diaSemana(), dto.horaInicio(), dto.horaFin());
    }

    default DisponibilidadDto toDisponibilidadDto(Disponibilidad disponibilidad) {
        if (disponibilidad == null) {
            return null;
        }
        return new DisponibilidadDto(
                disponibilidad.getDiaSemana(), disponibilidad.getHoraInicio(), disponibilidad.getHoraFin());
    }

    default Set<Disponibilidad> toDisponibilidades(List<DisponibilidadDto> dtos) {
        if (dtos == null) {
            return Set.of();
        }
        return dtos.stream().filter(Objects::nonNull).map(this::toDisponibilidad).collect(Collectors.toSet());
    }

    default List<DisponibilidadDto> toDisponibilidadDtos(Set<Disponibilidad> disponibilidades) {
        if (disponibilidades == null) {
            return List.of();
        }
        return disponibilidades.stream()
                .filter(Objects::nonNull)
                .map(this::toDisponibilidadDto)
                .toList();
    }

    default LiquidacionDetalle toDetalle(LiquidacionDetalleDto dto) {
        if (dto == null) {
            return null;
        }
        return LiquidacionDetalle.of(dto.partidoId(), dto.rol(), dto.monto(), dto.fechaPartido());
    }

    default LiquidacionDetalleDto toDetalleDto(LiquidacionDetalle detalle) {
        if (detalle == null) {
            return null;
        }
        return new LiquidacionDetalleDto(
                detalle.getPartidoId(), detalle.getRol(), detalle.getMonto(), detalle.getFechaPartido());
    }
}

