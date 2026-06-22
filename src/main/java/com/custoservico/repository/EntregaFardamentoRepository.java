package com.custoservico.repository;

import com.custoservico.model.EntregaFardamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EntregaFardamentoRepository extends JpaRepository<EntregaFardamento, Long> {
    List<EntregaFardamento> findByFuncionarioId(Long funcionarioId);
    Optional<EntregaFardamento> findByFuncionarioIdAndItemFardamentoId(Long funcionarioId, Long itemFardamentoId);
}
