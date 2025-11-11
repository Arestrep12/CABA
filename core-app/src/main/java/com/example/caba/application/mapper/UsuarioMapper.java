package com.example.caba.application.mapper;

import com.example.caba.application.dto.UsuarioDto;
import com.example.caba.domain.usuario.Usuario;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "arbitroId", source = "arbitro.id")
    UsuarioDto toDto(Usuario usuario);

    List<UsuarioDto> toDtoList(List<Usuario> usuarios);
}

