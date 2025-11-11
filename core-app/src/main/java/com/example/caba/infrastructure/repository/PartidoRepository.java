package com.example.caba.infrastructure.repository;

import com.example.caba.domain.partido.Partido;
import com.example.caba.domain.torneo.Torneo;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartidoRepository extends JpaRepository<Partido, UUID> {

    List<Partido> findByTorneoOrderByFechaProgramadaAsc(Torneo torneo);

    List<Partido> findByFechaProgramadaBetween(LocalDateTime inicio, LocalDateTime fin);
}

