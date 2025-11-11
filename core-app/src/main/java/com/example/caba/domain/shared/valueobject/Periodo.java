package com.example.caba.domain.shared.valueobject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class Periodo {

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    protected Periodo() {
        // JPA
    }

    private Periodo(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("El periodo debe tener fechas de inicio y fin");
        }
        if (fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha fin no puede ser anterior a la fecha inicio");
        }
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public static Periodo of(LocalDate inicio, LocalDate fin) {
        return new Periodo(inicio, fin);
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public boolean contiene(LocalDate fecha) {
        return (fecha.equals(fechaInicio) || fecha.isAfter(fechaInicio))
                && (fecha.equals(fechaFin) || fecha.isBefore(fechaFin));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Periodo periodo = (Periodo) o;
        return Objects.equals(fechaInicio, periodo.fechaInicio)
                && Objects.equals(fechaFin, periodo.fechaFin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fechaInicio, fechaFin);
    }
}

