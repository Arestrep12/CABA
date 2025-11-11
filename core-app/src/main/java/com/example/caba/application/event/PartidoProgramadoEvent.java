package com.example.caba.application.event;

import java.time.LocalDateTime;
import java.util.UUID;

public record PartidoProgramadoEvent(
        UUID partidoId, UUID torneoId, LocalDateTime fechaProgramada, String sede, String categoria) {}

