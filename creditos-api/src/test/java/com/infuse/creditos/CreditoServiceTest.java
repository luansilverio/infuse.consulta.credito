package com.infuse.creditos;

import com.infuse.creditos.exception.NotFoundException;
import com.infuse.creditos.model.Credito;
import com.infuse.creditos.repository.CreditoRepository;
import com.infuse.creditos.service.CreditoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreditoServiceTest {

    @Test
    void deveBuscarPorNumeroCredito() {
        CreditoRepository repo = Mockito.mock(CreditoRepository.class);
        Credito esperado = Credito.builder()
                .id(1L)
                .numeroCredito("123")
                .numeroNfse("NF1")
                .dataConstituicao(LocalDate.now())
                .valorIssqn(new BigDecimal("10.00"))
                .tipoCredito("ISSQN")
                .simplesNacional(true)
                .aliquota(new BigDecimal("5.00"))
                .valorFaturado(new BigDecimal("100.00"))
                .valorDeducao(new BigDecimal("0.00"))
                .baseCalculo(new BigDecimal("100.00"))
                .build();
        when(repo.findTopByNumeroCreditoOrderByIdDesc("123")).thenReturn(Optional.of(esperado));

        CreditoService service = new CreditoService(repo);
        Credito res = service.buscarPorNumeroCredito("123");

        assertEquals("123", res.getNumeroCredito());
        verify(repo, times(1)).findTopByNumeroCreditoOrderByIdDesc("123");
    }

    @Test
    void deveLancarNotFoundQuandoNaoEncontrar() {
        CreditoRepository repo = Mockito.mock(CreditoRepository.class);
        when(repo.findTopByNumeroCreditoOrderByIdDesc("NAOEXISTE")).thenReturn(Optional.empty());

        CreditoService service = new CreditoService(repo);
        assertThrows(NotFoundException.class, () -> service.buscarPorNumeroCredito("NAOEXISTE"));
    }
}
