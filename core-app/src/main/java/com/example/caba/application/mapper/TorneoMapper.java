package com.example.caba.application.mapper;

import com.example.caba.application.dto.TorneoDto;
import com.example.caba.application.dto.TorneoRequest;
import com.example.caba.domain.torneo.Torneo;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = SharedMapper.class)
public interface TorneoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "partidos", ignore = true)
    @Mapping(target = "tarifas", source = "tarifas")
    @Mapping(target = "periodo", source = "periodo")
    Torneo toEntity(TorneoRequest request);

    @Mapping(target = "tarifas", source = "tarifas")
    @Mapping(target = "periodo", source = "periodo")
    TorneoDto toDto(Torneo torneo);

    List<TorneoDto> toDtoList(List<Torneo> torneos);
}

