package com.infuse.creditos.service;

import com.infuse.creditos.exception.NotFoundException;
import com.infuse.creditos.model.Credito;
import com.infuse.creditos.repository.CreditoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditoService {

    private final CreditoRepository repository;

    public List<Credito> buscarPorNfse(String numeroNfse) {
        return repository.findByNumeroNfse(numeroNfse);
    }

    public Credito buscarPorNumeroCredito(String numeroCredito) {
        return repository.findTopByNumeroCreditoOrderByIdDesc(numeroCredito)
                .orElseThrow(() -> new NotFoundException("Crédito não encontrado: " + numeroCredito));
    }
}
