package com.example.caba.domain.shared.valueobject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Objects;

@Embeddable
public class Disponibilidad {

    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana", nullable = false)
    private DayOfWeek diaSemana;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;

    protected Disponibilidad() {
        // JPA
    }

    private Disponibilidad(DayOfWeek diaSemana, LocalTime horaInicio, LocalTime horaFin) {
        if (horaFin.isBefore(horaInicio)) {
            throw new IllegalArgumentException("La hora fin no puede ser anterior a la hora inicio");
        }
        this.diaSemana = Objects.requireNonNull(diaSemana, "El día de la semana es obligatorio");
        this.horaInicio = Objects.requireNonNull(horaInicio, "La hora de inicio es obligatoria");
        this.horaFin = Objects.requireNonNull(horaFin, "La hora fin es obligatoria");
    }

    public static Disponibilidad of(DayOfWeek diaSemana, LocalTime horaInicio, LocalTime horaFin) {
        return new Disponibilidad(diaSemana, horaInicio, horaFin);
    }

    public DayOfWeek getDiaSemana() {
        return diaSemana;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disponibilidad that = (Disponibilidad) o;
        return diaSemana == that.diaSemana
                && Objects.equals(horaInicio, that.horaInicio)
                && Objects.equals(horaFin, that.horaFin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(diaSemana, horaInicio, horaFin);
    }
}

