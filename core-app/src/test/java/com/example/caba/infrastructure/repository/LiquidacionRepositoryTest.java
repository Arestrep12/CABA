package com.example.caba.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.caba.domain.arbitro.Arbitro;
import com.example.caba.domain.pago.Liquidacion;
import com.example.caba.domain.pago.LiquidacionDetalle;
import com.example.caba.domain.shared.enums.EstadoLiquidacion;
import com.example.caba.domain.shared.enums.RolAsignacion;
import com.example.caba.domain.shared.valueobject.Periodo;
import com.example.caba.support.TestDataFactory;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class LiquidacionRepositoryTest {

    @Autowired
    private LiquidacionRepository liquidacionRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void findByArbitro_devuelveLiquidacionesDelArbitro() {
        Arbitro arbitro = TestDataFactory.arbitro();
        arbitro.setId(null);
        entityManager.persist(arbitro);
        entityManager.flush();

        Liquidacion liquidacion = liquidacionGenerada(arbitro, LocalDate.now().minusMonths(1), LocalDate.now().minusWeeks(2));
        Liquidacion otraLiquidacion = liquidacionGenerada(arbitro, LocalDate.now().minusWeeks(2), LocalDate.now());

        entityManager.persist(liquidacion);
        entityManager.persist(otraLiquidacion);
        entityManager.flush();

        assertThat(liquidacionRepository.findByArbitro(arbitro))
                .hasSize(2)
                .extracting(Liquidacion::getPeriodo)
                .allMatch(periodo -> periodo.getFechaFin().isAfter(periodo.getFechaInicio()));
    }

    @Test
    void findByEstado_filtraPorEstado() {
        Arbitro arbitro = TestDataFactory.arbitro();
        arbitro.setId(null);
        entityManager.persist(arbitro);
        entityManager.flush();

        Liquidacion pagada = liquidacionGenerada(arbitro, LocalDate.now().minusMonths(2), LocalDate.now().minusMonths(1));
        pagada.marcarPagada();
        Liquidacion generada = liquidacionGenerada(arbitro, LocalDate.now().minusMonths(1), LocalDate.now());

        entityManager.persist(pagada);
        entityManager.persist(generada);
        entityManager.flush();

        assertThat(liquidacionRepository.findByEstado(EstadoLiquidacion.PAGADA))
                .singleElement()
                .extracting(Liquidacion::getEstado)
                .isEqualTo(EstadoLiquidacion.PAGADA);
    }

    @Test
    void findByArbitroIdAndPeriodo_devuelveCoincidenciaExacta() {
        Arbitro arbitro = TestDataFactory.arbitro();
        arbitro.setId(null);
        entityManager.persist(arbitro);
        entityManager.flush();

        LocalDate inicio = LocalDate.of(2024, 1, 1);
        LocalDate fin = LocalDate.of(2024, 1, 31);
        Liquidacion liquidacion = liquidacionGenerada(arbitro, inicio, fin);
        entityManager.persist(liquidacion);
        entityManager.flush();

        assertThat(liquidacionRepository.findByArbitroIdAndPeriodo_FechaInicioAndPeriodo_FechaFin(
                        arbitro.getId(), inicio, fin))
                .isPresent()
                .get()
                .extracting(Liquidacion::getTotal)
                .asInstanceOf(org.assertj.core.api.InstanceOfAssertFactories.BIG_DECIMAL)
                .isGreaterThan(BigDecimal.ZERO);
    }

    private Liquidacion liquidacionGenerada(Arbitro arbitro, LocalDate inicio, LocalDate fin) {
        Liquidacion liquidacion = Liquidacion.builder()
                .arbitro(arbitro)
                .periodo(Periodo.of(inicio, fin))
                .estado(EstadoLiquidacion.GENERADA)
                .build();
        liquidacion.setId(null);
        liquidacion.agregarDetalle(LiquidacionDetalle.of(
                UUID.randomUUID(), RolAsignacion.PRIMER_ARBITRO, BigDecimal.valueOf(15000), inicio.plusDays(1)));
        return liquidacion;
    }
}

