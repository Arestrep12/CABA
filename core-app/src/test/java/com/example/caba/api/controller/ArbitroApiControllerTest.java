package com.example.caba.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.caba.application.dto.ArbitroDto;
import com.example.caba.application.dto.ArbitroRequest;
import com.example.caba.application.service.ArbitroService;
import com.example.caba.support.TestDataFactory;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class ArbitroApiControllerTest {

    @Mock
    private ArbitroService arbitroService;

    private ArbitroApiController controller;

    @BeforeEach
    void setUp() {
        controller = new ArbitroApiController(arbitroService);
    }

    @Test
    void crearArbitro_devuelve201ConPayloadValido() {
        ArbitroRequest request = TestDataFactory.arbitroRequest();
        ArbitroDto respuesta = TestDataFactory.arbitroDto();
        when(arbitroService.registrarArbitro(any())).thenReturn(respuesta);

        var response = controller.crear(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(respuesta);
        verify(arbitroService).registrarArbitro(request);
    }

    @Test
    void listarArbitros_devuelveLista() {
        var esperado = List.of(TestDataFactory.arbitroDto());
        when(arbitroService.listarActivos()).thenReturn(esperado);

        var resultado = controller.listar();

        assertThat(resultado).isEqualTo(esperado);
        verify(arbitroService).listarActivos();
    }

    @Test
    void obtenerArbitro_devuelveDto() {
        var arbitro = TestDataFactory.arbitroDto();
        UUID id = arbitro.id();
        when(arbitroService.obtenerArbitro(id)).thenReturn(arbitro);

        var resultado = controller.obtener(id);

        assertThat(resultado).isEqualTo(arbitro);
        verify(arbitroService).obtenerArbitro(id);
    }
}

