package com.example.caba.domain.partido;

import com.example.caba.domain.asignacion.Asignacion;
import com.example.caba.domain.torneo.Torneo;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "partidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Partido {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "torneo_id", nullable = false)
    private Torneo torneo;

    @NotNull
    @Column(name = "fecha_programada", nullable = false)
    private LocalDateTime fechaProgramada;

    @NotBlank
    @Column(name = "sede", nullable = false, length = 150)
    private String sede;

    @NotBlank
    @Column(name = "categoria", nullable = false, length = 100)
    private String categoria;

    @OneToMany(
            mappedBy = "partido",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Asignacion> asignaciones = new HashSet<>();

    public void programarPara(LocalDateTime fechaProgramada) {
        this.fechaProgramada = Objects.requireNonNull(fechaProgramada);
    }

    public void actualizarDatos(String sede, String categoria) {
        this.sede = sede;
        this.categoria = categoria;
    }

    public void agregarAsignacion(Asignacion asignacion) {
        asignaciones.add(asignacion);
        asignacion.setPartido(this);
    }
}

