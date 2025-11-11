package com.example.caba.domain.shared.valueobject;

import com.example.caba.domain.shared.enums.TipoDocumento;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;

@Embeddable
public class Documento {

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento", nullable = false)
    private TipoDocumento tipo;

    @NotBlank
    @Column(name = "numero_documento", nullable = false, unique = true, length = 32)
    private String numero;

    protected Documento() {
        // JPA
    }

    private Documento(TipoDocumento tipo, String numero) {
        this.tipo = tipo;
        this.numero = numero;
    }

    public static Documento of(TipoDocumento tipo, String numero) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de documento es obligatorio");
        }
        if (numero == null || numero.isBlank()) {
            throw new IllegalArgumentException("El número de documento es obligatorio");
        }
        return new Documento(tipo, numero.trim());
    }

    public TipoDocumento getTipo() {
        return tipo;
    }

    public String getNumero() {
        return numero;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Documento that = (Documento) o;
        return tipo == that.tipo && Objects.equals(numero, that.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tipo, numero);
    }

    @Override
    public String toString() {
        return tipo + "-" + numero;
    }
}

