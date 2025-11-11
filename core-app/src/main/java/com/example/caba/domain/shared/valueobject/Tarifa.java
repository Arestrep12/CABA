package com.example.caba.domain.shared.valueobject;

import com.example.caba.domain.shared.enums.Escalafon;
import com.example.caba.domain.shared.enums.RolAsignacion;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Tarifa {

    @Enumerated(EnumType.STRING)
    @Column(name = "tarifa_escalafon", nullable = false)
    private Escalafon escalafon;

    @Enumerated(EnumType.STRING)
    @Column(name = "tarifa_rol", nullable = false)
    private RolAsignacion rol;

    @Column(name = "tarifa_monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    protected Tarifa() {
        // JPA
    }

    private Tarifa(Escalafon escalafon, RolAsignacion rol, BigDecimal monto) {
        if (monto == null || monto.signum() < 0) {
            throw new IllegalArgumentException("La tarifa debe ser positiva");
        }
        this.escalafon = Objects.requireNonNull(escalafon, "El escalafón es obligatorio");
        this.rol = Objects.requireNonNull(rol, "El rol de la tarifa es obligatorio");
        this.monto = monto.setScale(2, java.math.RoundingMode.HALF_UP);
    }

    public static Tarifa of(Escalafon escalafon, RolAsignacion rol, BigDecimal monto) {
        return new Tarifa(escalafon, rol, monto);
    }

    public Escalafon getEscalafon() {
        return escalafon;
    }

    public RolAsignacion getRol() {
        return rol;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tarifa tarifa = (Tarifa) o;
        return escalafon == tarifa.escalafon && rol == tarifa.rol;
    }

    @Override
    public int hashCode() {
        return Objects.hash(escalafon, rol);
    }
}

