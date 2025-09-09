package com.infuse.creditos;

import com.infuse.creditos.controller.CreditoController;
import com.infuse.creditos.model.Credito;
import com.infuse.creditos.service.CreditoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CreditoController.class)
public class CreditoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CreditoService service;

    @Test
    void deveRetornarListaPorNfse() throws Exception {
        Credito c = Credito.builder()
                .numeroCredito("123456")
                .numeroNfse("7891011")
                .dataConstituicao(LocalDate.of(2024, 2, 25))
                .valorIssqn(new BigDecimal("1500.75"))
                .tipoCredito("ISSQN")
                .simplesNacional(true)
                .aliquota(new BigDecimal("5.00"))
                .valorFaturado(new BigDecimal("30000.00"))
                .valorDeducao(new BigDecimal("5000.00"))
                .baseCalculo(new BigDecimal("25000.00"))
                .build();

        Mockito.when(service.buscarPorNfse("7891011")).thenReturn(Collections.singletonList(c));

        mvc.perform(get("/api/creditos/7891011").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numeroCredito", is("123456")))
                .andExpect(jsonPath("$[0].simplesNacional", is("Sim")));
    }
}
