package com.example.caba.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.caba.domain.partido.Partido;
import com.example.caba.domain.torneo.Torneo;
import com.example.caba.support.TestDataFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class PartidoRepositoryTest {

    @Autowired
    private PartidoRepository partidoRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void findByTorneoOrderByFechaProgramadaAsc_ordenaCronologicamente() {
        Torneo torneo = TestDataFactory.torneo();
        torneo.setId(null);
        entityManager.persist(torneo);
        entityManager.flush();

        LocalDateTime base = LocalDateTime.now().withNano(0);

        Partido partidoAntiguo = TestDataFactory.partido(torneo);
        partidoAntiguo.setId(null);
        partidoAntiguo.setFechaProgramada(base.plusDays(1));
        Partido partidoReciente = TestDataFactory.partido(torneo);
        partidoReciente.setId(null);
        partidoReciente.setFechaProgramada(base.plusDays(3));
        Partido partidoMedio = TestDataFactory.partido(torneo);
        partidoMedio.setId(null);
        partidoMedio.setFechaProgramada(base.plusDays(2));

        entityManager.persist(partidoAntiguo);
        entityManager.persist(partidoReciente);
        entityManager.persist(partidoMedio);
        entityManager.flush();
        entityManager.clear();

        List<Partido> resultado = partidoRepository.findByTorneoOrderByFechaProgramadaAsc(torneo);

        assertThat(resultado)
                .extracting(Partido::getFechaProgramada)
                .isSorted()
                .containsExactly(
                        partidoAntiguo.getFechaProgramada(),
                        partidoMedio.getFechaProgramada(),
                        partidoReciente.getFechaProgramada());
    }

    @Test
    void findByFechaProgramadaBetween_filtraPorIntervalo() {
        Torneo torneo = TestDataFactory.torneo();
        torneo.setId(null);
        entityManager.persist(torneo);
        entityManager.flush();

        LocalDateTime base = LocalDateTime.now().withNano(0);

        Partido dentroRango = TestDataFactory.partido(torneo);
        dentroRango.setId(null);
        dentroRango.setFechaProgramada(base.plusDays(5));
        Partido limiteInferior = TestDataFactory.partido(torneo);
        limiteInferior.setId(null);
        limiteInferior.setFechaProgramada(base.plusDays(2));
        Partido fueraRango = TestDataFactory.partido(torneo);
        fueraRango.setId(null);
        fueraRango.setFechaProgramada(base.plusDays(10));

        entityManager.persist(dentroRango);
        entityManager.flush();

        entityManager.persist(limiteInferior);
        entityManager.flush();

        entityManager.persist(fueraRango);
        entityManager.flush();

        LocalDateTime inicio = base.plusDays(2);
        LocalDateTime fin = base.plusDays(6);

        List<Partido> resultado = partidoRepository.findByFechaProgramadaBetween(inicio, fin);

        assertThat(resultado)
                .extracting(Partido::getFechaProgramada)
                .containsExactlyInAnyOrder(dentroRango.getFechaProgramada(), limiteInferior.getFechaProgramada())
                .doesNotContain(fueraRango.getFechaProgramada());
    }
}

