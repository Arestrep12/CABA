package com.example.caba.domain.pago;

import com.example.caba.domain.shared.enums.RolAsignacion;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class LiquidacionDetalle {

    @Column(name = "partido_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID partidoId;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol_asignacion", nullable = false)
    private RolAsignacion rol;

    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(name = "fecha_partido", nullable = false)
    private LocalDate fechaPartido;

    protected LiquidacionDetalle() {
        // JPA
    }

    private LiquidacionDetalle(UUID partidoId, RolAsignacion rol, BigDecimal monto, LocalDate fechaPartido) {
        if (monto == null || monto.signum() < 0) {
            throw new IllegalArgumentException("El monto del detalle debe ser positivo");
        }
        this.partidoId = Objects.requireNonNull(partidoId, "El partido es obligatorio");
        this.rol = Objects.requireNonNull(rol, "El rol es obligatorio");
        this.monto = monto.setScale(2, java.math.RoundingMode.HALF_UP);
        this.fechaPartido = Objects.requireNonNull(fechaPartido, "La fecha del partido es obligatoria");
    }

    public static LiquidacionDetalle of(UUID partidoId, RolAsignacion rol, BigDecimal monto, LocalDate fechaPartido) {
        return new LiquidacionDetalle(partidoId, rol, monto, fechaPartido);
    }

    public UUID getPartidoId() {
        return partidoId;
    }

    public RolAsignacion getRol() {
        return rol;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public LocalDate getFechaPartido() {
        return fechaPartido;
    }
}

