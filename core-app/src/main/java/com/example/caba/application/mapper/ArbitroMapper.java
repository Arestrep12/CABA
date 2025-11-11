package com.example.caba.application.mapper;

import com.example.caba.application.dto.ArbitroDto;
import com.example.caba.application.dto.ArbitroRequest;
import com.example.caba.domain.arbitro.Arbitro;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(
        componentModel = "spring",
        uses = SharedMapper.class,
        imports = {
            com.example.caba.domain.shared.valueobject.Documento.class,
            com.example.caba.domain.shared.valueobject.Email.class
        })
public interface ArbitroMapper {

    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "activo", ignore = true),
        @Mapping(
                target = "documento",
                expression =
                        "java(com.example.caba.domain.shared.valueobject.Documento.of(request.tipoDocumento(), request.numeroDocumento()))"),
        @Mapping(
                target = "email",
                expression =
                        "java(com.example.caba.domain.shared.valueobject.Email.of(request.email()))"),
        @Mapping(target = "disponibilidades", source = "disponibilidades")
    })
    Arbitro toEntity(ArbitroRequest request);

    @Mappings({
        @Mapping(target = "email", expression = "java(arbitro.getEmail().getValue())"),
        @Mapping(target = "disponibilidades", source = "disponibilidades")
    })
    ArbitroDto toDto(Arbitro arbitro);

    List<ArbitroDto> toDtoList(List<Arbitro> arbitros);

    Set<ArbitroDto> toDtoSet(Set<Arbitro> arbitros);
}

