package com.example.caba.application.client;

import com.example.caba.application.dto.AgendaSlotResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "agendaClient", url = "${services.agenda.url:http://localhost:4001}")
public interface AgendaClient {

    @GetMapping("/availability/{arbitroId}")
    List<AgendaSlotResponse> obtenerDisponibilidad(
            @PathVariable("arbitroId") UUID arbitroId,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta);

    @PostMapping("/assignments")
    void notificarAsignacion(@RequestBody AgendaSyncRequest request);

    record AgendaSyncRequest(UUID asignacionId, UUID arbitroId, UUID partidoId) {}
}

