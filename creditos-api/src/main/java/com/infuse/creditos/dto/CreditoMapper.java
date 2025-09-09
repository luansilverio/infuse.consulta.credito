package com.infuse.creditos.dto;

import com.infuse.creditos.model.Credito;

public class CreditoMapper {
    public static CreditoDto toDto(Credito c) {
        if (c == null) return null;
        return CreditoDto.builder()
                .numeroCredito(c.getNumeroCredito())
                .numeroNfse(c.getNumeroNfse())
                .dataConstituicao(c.getDataConstituicao())
                .valorIssqn(c.getValorIssqn())
                .tipoCredito(c.getTipoCredito())
                .simplesNacional(c.isSimplesNacional() ? "Sim" : "Não")
                .aliquota(c.getAliquota())
                .valorFaturado(c.getValorFaturado())
                .valorDeducao(c.getValorDeducao())
                .baseCalculo(c.getBaseCalculo())
                .build();
    }
}
