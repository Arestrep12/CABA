package com.example.caba.domain.asignacion;

import com.example.caba.domain.arbitro.Arbitro;
import com.example.caba.domain.partido.Partido;
import com.example.caba.domain.shared.enums.EstadoAsignacion;
import com.example.caba.domain.shared.enums.RolAsignacion;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "asignaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Asignacion {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "partido_id", nullable = false)
    private Partido partido;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "arbitro_id", nullable = false)
    private Arbitro arbitro;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false, length = 32)
    private RolAsignacion rol;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 32)
    @Builder.Default
    private EstadoAsignacion estado = EstadoAsignacion.PENDIENTE;

    @Column(name = "respondido_en")
    private LocalDateTime respondidoEn;

    public void aceptar() {
        this.estado = EstadoAsignacion.ACEPTADA;
        this.respondidoEn = LocalDateTime.now();
    }

    public void rechazar() {
        this.estado = EstadoAsignacion.RECHAZADA;
        this.respondidoEn = LocalDateTime.now();
    }

    public void cancelar() {
        this.estado = EstadoAsignacion.CANCELADA;
    }
}

