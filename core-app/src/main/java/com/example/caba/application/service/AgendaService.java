package com.example.caba.application.service;

import com.example.caba.application.client.AgendaClient;
import com.example.caba.application.dto.AgendaSlotResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AgendaService {

    private final AgendaClient agendaClient;

    @Transactional(readOnly = true)
    public List<AgendaSlotResponse> consultarDisponibilidad(UUID arbitroId, LocalDate desde, LocalDate hasta) {
        return agendaClient.obtenerDisponibilidad(arbitroId, desde, hasta);
    }

    public void sincronizarAsignacion(UUID asignacionId, UUID arbitroId, UUID partidoId) {
        agendaClient.notificarAsignacion(new AgendaClient.AgendaSyncRequest(asignacionId, arbitroId, partidoId));
    }
}

