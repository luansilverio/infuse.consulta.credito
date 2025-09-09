package com.infuse.creditos.controller;

import com.infuse.creditos.dto.CreditoDto;
import com.infuse.creditos.dto.CreditoMapper;
import com.infuse.creditos.model.Credito;
import com.infuse.creditos.service.CreditoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/creditos", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CreditoController {

    private final CreditoService service;

    @GetMapping("/{numeroNfse}")
    public List<CreditoDto> listarPorNfse(@PathVariable String numeroNfse) {
        List<Credito> lista = service.buscarPorNfse(numeroNfse);
        return lista.stream().map(CreditoMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/credito/{numeroCredito}")
    public CreditoDto detalheCredito(@PathVariable String numeroCredito) {
        return CreditoMapper.toDto(service.buscarPorNumeroCredito(numeroCredito));
    }
}
