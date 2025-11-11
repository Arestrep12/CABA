package com.example.caba.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.caba.domain.arbitro.Arbitro;
import com.example.caba.support.TestDataFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class ArbitroRepositoryTest {

    @Autowired
    private ArbitroRepository arbitroRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void findByEmailValue_devuelveArbitroPersistido() {
        Arbitro arbitro = TestDataFactory.arbitro();
        arbitro.setId(null);
        entityManager.persist(arbitro);
        entityManager.flush();
        entityManager.clear();

        assertThat(arbitroRepository.findByEmailValue("juan.perez@example.com"))
                .isPresent()
                .get()
                .extracting(Arbitro::getId)
                .isEqualTo(arbitro.getId());
    }

    @Test
    void findByDocumentoNumero_devuelveVacioParaDocumentoNoRegistrado() {
        assertThat(arbitroRepository.findByDocumentoNumero("99999999")).isEmpty();
    }
}

