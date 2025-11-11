package com.example.caba.application.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.example.caba.application.dto.PartidoRequest;
import com.example.caba.application.mapper.PartidoMapper;
import com.example.caba.application.service.NotificacionDomainService;
import com.example.caba.domain.shared.exception.BusinessRuleException;
import com.example.caba.domain.shared.valueobject.Periodo;
import com.example.caba.domain.torneo.Torneo;
import com.example.caba.infrastructure.repository.PartidoRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PartidoServiceTest {

    @Mock
    private PartidoRepository partidoRepository;

    @Mock
    private TorneoService torneoService;

    @Mock
    private PartidoMapper partidoMapper;

    @Mock
    private NotificacionDomainService notificacionDomainService;

    @InjectMocks
    private PartidoService partidoService;

    @Test
    void programarPartido_fechaFueraDePeriodo_lanzaBusinessRuleException() {
        UUID torneoId = UUID.randomUUID();
        LocalDateTime fechaFueraDePeriodo = LocalDateTime.now().plusMonths(2);
        PartidoRequest request =
                new PartidoRequest(torneoId, fechaFueraDePeriodo, "Microestadio", "Mayor");

        Torneo torneo = Torneo.builder()
                .id(torneoId)
                .nombre("Liga Apertura")
                .periodo(Periodo.of(LocalDate.now(), LocalDate.now().plusWeeks(2)))
                .build();

        when(torneoService.obtenerEntidad(torneoId)).thenReturn(torneo);

        assertThrows(BusinessRuleException.class, () -> partidoService.programarPartido(request));

        verifyNoInteractions(partidoRepository, partidoMapper, notificacionDomainService);
    }
}

