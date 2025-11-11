package com.example.caba.infrastructure.repository;

import com.example.caba.domain.asignacion.Asignacion;
import com.example.caba.domain.arbitro.Arbitro;
import com.example.caba.domain.partido.Partido;
import com.example.caba.domain.shared.enums.EstadoAsignacion;
import com.example.caba.domain.shared.enums.RolAsignacion;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsignacionRepository extends JpaRepository<Asignacion, UUID> {

    List<Asignacion> findByArbitroAndEstado(Arbitro arbitro, EstadoAsignacion estado);

    Optional<Asignacion> findByPartidoAndRol(Partido partido, RolAsignacion rol);

    boolean existsByArbitroAndPartido(Arbitro arbitro, Partido partido);

    List<Asignacion> findByArbitroAndEstadoAndPartido_FechaProgramadaBetween(
            Arbitro arbitro, EstadoAsignacion estado, java.time.LocalDateTime inicio, java.time.LocalDateTime fin);
}

