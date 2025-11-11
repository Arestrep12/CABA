package com.example.caba.application.mapper;

import com.example.caba.application.dto.AsignacionDto;
import com.example.caba.application.dto.AsignacionRequest;
import com.example.caba.domain.asignacion.Asignacion;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AsignacionMapper {

    @Mapping(target = "partido", ignore = true)
    @Mapping(target = "arbitro", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "respondidoEn", ignore = true)
    @Mapping(target = "id", ignore = true)
    Asignacion toEntity(AsignacionRequest request);

    @Mapping(target = "partidoId", source = "partido.id")
    @Mapping(target = "arbitroId", source = "arbitro.id")
    AsignacionDto toDto(Asignacion asignacion);

    List<AsignacionDto> toDtoList(Set<Asignacion> asignaciones);
}

