package com.custoservico.repository;

import com.custoservico.model.ItemFardamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemFardamentoRepository extends JpaRepository<ItemFardamento, Long> {
    List<ItemFardamento> findByAtivoTrue();
    List<ItemFardamento> findByFuncaoAndAtivoTrue(String funcao);
}
