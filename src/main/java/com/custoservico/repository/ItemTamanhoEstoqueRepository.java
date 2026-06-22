package com.custoservico.repository;

import com.custoservico.model.ItemTamanhoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ItemTamanhoEstoqueRepository extends JpaRepository<ItemTamanhoEstoque, Long> {
    List<ItemTamanhoEstoque> findByItemFardamentoId(Long itemId);
    Optional<ItemTamanhoEstoque> findByItemFardamentoIdAndTamanho(Long itemId, String tamanho);
}
