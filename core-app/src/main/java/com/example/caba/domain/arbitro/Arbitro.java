package com.example.caba.domain.arbitro;

import com.example.caba.domain.shared.enums.Escalafon;
import com.example.caba.domain.shared.enums.Especialidad;
import com.example.caba.domain.shared.valueobject.Disponibilidad;
import com.example.caba.domain.shared.valueobject.Documento;
import com.example.caba.domain.shared.valueobject.Email;
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
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
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
@Table(name = "arbitros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Arbitro {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotBlank
    @Column(name = "nombres", nullable = false, length = 120)
    private String nombres;

    @NotBlank
    @Column(name = "apellidos", nullable = false, length = 120)
    private String apellidos;

    @Embedded
    @NotNull
    private Documento documento;

    @Embedded
    @NotNull
    private Email email;

    @Enumerated(EnumType.STRING)
    @Column(name = "especialidad", nullable = false, length = 32)
    private Especialidad especialidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "escalafon", nullable = false, length = 32)
    private Escalafon escalafon;

    @Column(name = "foto_url")
    private String fotoUrl;

    @Column(name = "activo", nullable = false)
    @Builder.Default
    private boolean activo = true;

    @Column(name = "fecha_ingreso")
    private LocalDate fechaIngreso;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "arbitro_disponibilidades", joinColumns = @JoinColumn(name = "arbitro_id"))
    @Builder.Default
    private Set<Disponibilidad> disponibilidades = new HashSet<>();

    public void actualizarDatos(
            String nombres,
            String apellidos,
            Escalafon escalafon,
            Especialidad especialidad,
            String fotoUrl,
            Set<Disponibilidad> disponibilidades) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.escalafon = escalafon;
        this.especialidad = especialidad;
        this.fotoUrl = fotoUrl;
        this.disponibilidades.clear();
        if (disponibilidades != null) {
            this.disponibilidades.addAll(disponibilidades);
        }
    }

    public void desactivar() {
        this.activo = false;
    }

    public void reactivar() {
        this.activo = true;
    }

    public void actualizarEmail(Email nuevoEmail) {
        this.email = Objects.requireNonNull(nuevoEmail);
    }
}

