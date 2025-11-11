package com.example.caba.infrastructure.repository;

import com.example.caba.domain.arbitro.Arbitro;
import com.example.caba.domain.pago.Liquidacion;
import com.example.caba.domain.shared.enums.EstadoLiquidacion;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LiquidacionRepository extends JpaRepository<Liquidacion, UUID> {

    List<Liquidacion> findByArbitro(Arbitro arbitro);

    List<Liquidacion> findByEstado(EstadoLiquidacion estado);

    Optional<Liquidacion> findByArbitroIdAndPeriodo_FechaInicioAndPeriodo_FechaFin(
            UUID arbitroId, LocalDate inicio, LocalDate fin);
}

