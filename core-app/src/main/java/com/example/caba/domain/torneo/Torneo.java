package com.example.caba.domain.torneo;

import com.example.caba.domain.partido.Partido;
import com.example.caba.domain.shared.valueobject.Periodo;
import com.example.caba.domain.shared.valueobject.Tarifa;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "torneos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Torneo {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotBlank
    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Embedded
    private Periodo periodo;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "torneo_tarifas", joinColumns = @JoinColumn(name = "torneo_id"))
    @Builder.Default
    private Set<Tarifa> tarifas = new HashSet<>();

    @OneToMany(
            mappedBy = "torneo",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Partido> partidos = new HashSet<>();

    public void agregarTarifa(Tarifa tarifa) {
        tarifas.remove(tarifa);
        tarifas.add(Objects.requireNonNull(tarifa));
    }

    public void actualizarPeriodo(Periodo nuevoPeriodo) {
        this.periodo = Objects.requireNonNull(nuevoPeriodo);
    }

    public void agregarPartido(Partido partido) {
        partidos.add(partido);
        partido.setTorneo(this);
    }
}

