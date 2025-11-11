package com.example.caba.security;

import com.example.caba.domain.shared.enums.RolUsuario;
import com.example.caba.domain.usuario.Usuario;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class UsuarioPrincipal implements UserDetails {

    private final UUID id;
    private final String username;
    private final String password;
    private final boolean enabled;
    private final RolUsuario rol;

    private UsuarioPrincipal(UUID id, String username, String password, boolean enabled, RolUsuario rol) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.rol = rol;
    }

    public static UsuarioPrincipal from(Usuario usuario) {
        return new UsuarioPrincipal(
                usuario.getId(), usuario.getUsername(), usuario.getPassword(), usuario.isEnabled(), usuario.getRol());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}

