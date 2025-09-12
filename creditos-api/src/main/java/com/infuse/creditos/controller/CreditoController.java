package com.infuse.creditos.controller;

import com.infuse.creditos.dto.CreditoDto;
import com.infuse.creditos.dto.CreditoMapper;
import com.infuse.creditos.model.Credito;
import com.infuse.creditos.service.CreditoService;
import com.infuse.creditos.messaging.ServiceBusPublisher;
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
    private final ServiceBusPublisher publisher;

    @GetMapping("/{numeroNfse}")
    public List<CreditoDto> listarPorNfse(@PathVariable String numeroNfse) {
        List<Credito> lista = service.buscarPorNfse(numeroNfse);
        List<CreditoDto> out = lista.stream().map(CreditoMapper::toDto).collect(Collectors.toList());
        try {
            publisher.sendConsultaEvento("NFSE", numeroNfse, true, out.size(), null);
        } catch (Exception ignored) {
        }
        return out;
    }

    @GetMapping("/credito/{numeroCredito}")
    public CreditoDto detalheCredito(@PathVariable String numeroCredito) {
        CreditoDto dto = CreditoMapper.toDto(service.buscarPorNumeroCredito(numeroCredito));
        try {
            publisher.sendConsultaEvento("CREDITO", numeroCredito, dto != null, dto != null ? 1 : 0,
                    dto != null ? dto.getNumeroCredito() : null);
        } catch (Exception ignored) {
        }
        return dto;
    }
}
