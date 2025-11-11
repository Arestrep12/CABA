package com.example.caba.domain.shared.valueobject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

@Embeddable
public class Email {

    @jakarta.validation.constraints.Email
    @NotBlank
    @Column(name = "email", nullable = false, unique = true)
    private String value;

    protected Email() {
        // JPA
    }

    private Email(String value) {
        this.value = value.toLowerCase();
    }

    public static Email of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El email no puede ser vacío");
        }
        return new Email(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(value, email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}

