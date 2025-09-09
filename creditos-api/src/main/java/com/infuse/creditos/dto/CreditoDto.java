package com.infuse.creditos.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CreditoDto {
    private String numeroCredito;
    private String numeroNfse;
    private LocalDate dataConstituicao;
    private BigDecimal valorIssqn;
    private String tipoCredito;
    private String simplesNacional; // "Sim" | "Não"
    private BigDecimal aliquota;
    private BigDecimal valorFaturado;
    private BigDecimal valorDeducao;
    private BigDecimal baseCalculo;
}
