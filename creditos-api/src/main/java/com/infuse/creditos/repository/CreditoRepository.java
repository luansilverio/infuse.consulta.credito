package com.infuse.creditos.repository;

import com.infuse.creditos.model.Credito;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface CreditoRepository extends JpaRepository<Credito, Long> {
    List<Credito> findByNumeroNfse(String numeroNfse);
    Optional<Credito> findTopByNumeroCreditoOrderByIdDesc(String numeroCredito);
}
