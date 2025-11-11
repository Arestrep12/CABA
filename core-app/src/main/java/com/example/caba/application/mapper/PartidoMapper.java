package com.example.caba.application.mapper;

import com.example.caba.application.dto.PartidoDto;
import com.example.caba.application.dto.PartidoRequest;
import com.example.caba.domain.partido.Partido;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AsignacionMapper.class})
public interface PartidoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "torneo", ignore = true)
    @Mapping(target = "asignaciones", ignore = true)
    Partido toEntity(PartidoRequest request);

    @Mapping(target = "torneoId", source = "torneo.id")
    @Mapping(target = "asignaciones", source = "asignaciones")
    PartidoDto toDto(Partido partido);

    List<PartidoDto> toDtoList(List<Partido> partidos);

    List<PartidoDto> toDtoList(Set<Partido> partidos);
}

