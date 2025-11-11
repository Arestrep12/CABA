package com.example.caba.application.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;
import jakarta.validation.constraints.NotNull;

public record DisponibilidadDto(
        @NotNull DayOfWeek diaSemana, @NotNull LocalTime horaInicio, @NotNull LocalTime horaFin) {}

