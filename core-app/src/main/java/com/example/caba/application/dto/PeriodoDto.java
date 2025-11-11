package com.example.caba.application.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record PeriodoDto(@NotNull LocalDate fechaInicio, @NotNull LocalDate fechaFin) {}

