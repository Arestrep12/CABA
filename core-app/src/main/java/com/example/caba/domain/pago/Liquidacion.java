package com.example.caba.domain.pago;

import com.example.caba.domain.arbitro.Arbitro;
import com.example.caba.domain.shared.enums.EstadoLiquidacion;
import com.example.caba.domain.shared.valueobject.Periodo;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "liquidaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Liquidacion {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "arbitro_id", nullable = false)
    private Arbitro arbitro;

    @Embedded
    private Periodo periodo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 32)
    @Builder.Default
    private EstadoLiquidacion estado = EstadoLiquidacion.GENERADA;

    @Column(name = "total", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal total = BigDecimal.ZERO;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "liquidacion_detalles", joinColumns = @JoinColumn(name = "liquidacion_id"))
    @Builder.Default
    private Set<LiquidacionDetalle> detalles = new HashSet<>();

    public void agregarDetalle(LiquidacionDetalle detalle) {
        detalles.add(detalle);
        total = total.add(detalle.getMonto()).setScale(2, java.math.RoundingMode.HALF_UP);
    }

    public void marcarPagada() {
        estado = EstadoLiquidacion.PAGADA;
    }
}

