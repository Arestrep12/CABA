package com.example.caba.domain.usuario;

import com.example.caba.domain.arbitro.Arbitro;
import com.example.caba.domain.shared.enums.RolUsuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Usuario {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotBlank
    @Column(name = "username", nullable = false, unique = true, length = 80)
    private String username;

    @NotBlank
    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false, length = 32)
    private RolUsuario rol;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arbitro_id")
    private Arbitro arbitro;

    @Column(name = "enabled", nullable = false)
    @Builder.Default
    private boolean enabled = true;

    public boolean esAdmin() {
        return RolUsuario.ADMIN.equals(rol);
    }
}

