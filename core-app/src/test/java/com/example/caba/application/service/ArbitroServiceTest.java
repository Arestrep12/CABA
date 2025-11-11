package com.example.caba.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.example.caba.application.dto.ArbitroDto;
import com.example.caba.application.dto.ArbitroRequest;
import com.example.caba.application.mapper.ArbitroMapper;
import com.example.caba.application.mapper.SharedMapper;
import com.example.caba.domain.arbitro.Arbitro;
import com.example.caba.domain.shared.exception.BusinessRuleException;
import com.example.caba.domain.shared.exception.NotFoundException;
import com.example.caba.infrastructure.repository.ArbitroRepository;
import com.example.caba.support.TestDataFactory;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ArbitroServiceTest {

    @Mock
    private ArbitroRepository arbitroRepository;

    @Mock
    private ArbitroMapper arbitroMapper;

    @Mock
    private SharedMapper sharedMapper;

    @InjectMocks
    private ArbitroService arbitroService;

    @Test
    void registrarArbitro_emailDuplicado_lanzaBusinessRuleException() {
        ArbitroRequest request = TestDataFactory.arbitroRequest();
        when(arbitroRepository.findByEmailValue(request.email())).thenReturn(Optional.of(TestDataFactory.arbitro()));

        assertThrows(BusinessRuleException.class, () -> arbitroService.registrarArbitro(request));

        verifyNoInteractions(arbitroMapper, sharedMapper);
    }

    @Test
    void actualizarArbitro_inexistente_lanzaNotFoundException() {
        UUID arbitroId = UUID.randomUUID();
        when(arbitroRepository.findById(arbitroId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> arbitroService.actualizarArbitro(arbitroId, TestDataFactory.arbitroRequest()));
    }

    @Test
    void listarActivos_devuelveSoloArbitrosActivos() {
        Arbitro activo = TestDataFactory.arbitro();
        Arbitro inactivo = TestDataFactory.arbitro();
        inactivo.setActivo(false);

        when(arbitroRepository.findAll()).thenReturn(List.of(activo, inactivo));

        ArbitroDto dtoActivo = TestDataFactory.arbitroDto();
        when(arbitroMapper.toDto(activo)).thenReturn(dtoActivo);

        List<ArbitroDto> resultado = arbitroService.listarActivos();

        assertThat(resultado).containsExactly(dtoActivo);
        verify(arbitroMapper).toDto(activo);
    }

    @Test
    void obtenerArbitro_devuelveDtoCuandoExiste() {
        Arbitro arbitro = TestDataFactory.arbitro();
        ArbitroDto dto = TestDataFactory.arbitroDto();

        when(arbitroRepository.findById(arbitro.getId())).thenReturn(Optional.of(arbitro));
        when(arbitroMapper.toDto(arbitro)).thenReturn(dto);

        ArbitroDto resultado = arbitroService.obtenerArbitro(arbitro.getId());

        assertThat(resultado).isEqualTo(dto);
    }
}

