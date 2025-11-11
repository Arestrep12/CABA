package com.example.caba.infrastructure.repository;

import com.example.caba.domain.torneo.Torneo;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TorneoRepository extends JpaRepository<Torneo, UUID> {

    Optional<Torneo> findByNombreIgnoreCase(String nombre);
}

