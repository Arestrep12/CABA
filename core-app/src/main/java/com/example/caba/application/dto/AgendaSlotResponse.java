package com.example.caba.application.dto;

import java.time.LocalDateTime;

public record AgendaSlotResponse(LocalDateTime inicio, LocalDateTime fin, boolean disponible) {}

