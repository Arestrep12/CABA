package com.example.caba.application.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record PartidoReprogramadoEvent(
        UUID partidoId,
        UUID torneoId,
        LocalDateTime fechaAnterior,
        LocalDateTime fechaNueva,
        String sede,
        String categoria) {}

