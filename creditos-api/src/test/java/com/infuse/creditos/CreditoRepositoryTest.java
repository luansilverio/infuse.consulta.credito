package com.infuse.creditos;

import com.infuse.creditos.model.Credito;
import com.infuse.creditos.repository.CreditoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CreditoRepositoryTest {

    @Autowired
    private CreditoRepository repository;

    @Test
    void deveSalvarEBuscarPorNumeroNfse() {
        Credito c1 = Credito.builder()
                .numeroCredito("A1")
                .numeroNfse("NF-001")
                .dataConstituicao(LocalDate.now())
                .valorIssqn(new BigDecimal("100.00"))
                .tipoCredito("ISSQN")
                .simplesNacional(true)
                .aliquota(new BigDecimal("5.00"))
                .valorFaturado(new BigDecimal("1000.00"))
                .valorDeducao(new BigDecimal("0.00"))
                .baseCalculo(new BigDecimal("1000.00"))
                .build();
        repository.save(c1);

        List<Credito> lista = repository.findByNumeroNfse("NF-001");
        assertEquals(1, lista.size());
        assertEquals("A1", lista.get(0).getNumeroCredito());
    }

    @Test
    void deveSalvarEBuscarPorNumeroCredito() {
        Credito c = Credito.builder()
                .numeroCredito("B2")
                .numeroNfse("NF-002")
                .dataConstituicao(LocalDate.now())
                .valorIssqn(new BigDecimal("200.00"))
                .tipoCredito("Outros")
                .simplesNacional(false)
                .aliquota(new BigDecimal("3.50"))
                .valorFaturado(new BigDecimal("2000.00"))
                .valorDeducao(new BigDecimal("200.00"))
                .baseCalculo(new BigDecimal("1800.00"))
                .build();
        repository.save(c);

        Optional<Credito> found = repository.findTopByNumeroCreditoOrderByIdDesc("B2");
        assertTrue(found.isPresent());
        assertEquals("NF-002", found.get().getNumeroNfse());
    }
}
