package com.example.caba.application.mapper;

import com.example.caba.application.dto.LiquidacionDto;
import com.example.caba.domain.pago.Liquidacion;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = SharedMapper.class)
public interface LiquidacionMapper {

    @Mapping(target = "arbitroId", source = "arbitro.id")
    @Mapping(target = "periodo", source = "periodo")
    @Mapping(target = "detalles", source = "detalles")
    LiquidacionDto toDto(Liquidacion liquidacion);

    List<LiquidacionDto> toDtoList(List<Liquidacion> liquidaciones);
}

