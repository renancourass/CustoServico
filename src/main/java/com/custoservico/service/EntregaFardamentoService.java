package com.custoservico.service;

import com.custoservico.model.EntregaFardamento;
import com.custoservico.model.Funcionario;
import com.custoservico.model.ItemFardamento;
import com.custoservico.model.ItemTamanhoEstoque;
import com.custoservico.repository.EntregaFardamentoRepository;
import com.custoservico.repository.FuncionarioRepository;
import com.custoservico.repository.ItemFardamentoRepository;
import com.custoservico.repository.ItemTamanhoEstoqueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class EntregaFardamentoService {

    private final EntregaFardamentoRepository repository;
    private final FuncionarioRepository funcionarioRepository;
    private final ItemFardamentoRepository itemRepository;
    private final ItemTamanhoEstoqueRepository tamanhoRepository;

    public EntregaFardamentoService(EntregaFardamentoRepository repository,
                                    FuncionarioRepository funcionarioRepository,
                                    ItemFardamentoRepository itemRepository,
                                    ItemTamanhoEstoqueRepository tamanhoRepository) {
        this.repository = repository;
        this.funcionarioRepository = funcionarioRepository;
        this.itemRepository = itemRepository;
        this.tamanhoRepository = tamanhoRepository;
    }

    public List<EntregaFardamento> listarPorFuncionario(Long funcionarioId) {
        return repository.findByFuncionarioId(funcionarioId);
    }

    @Transactional
    public EntregaFardamento entregar(Long funcionarioId, Long itemId, String tamanho) {
        Funcionario funcionario = funcionarioRepository.findById(funcionarioId)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado: " + funcionarioId));
        ItemFardamento item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item não encontrado: " + itemId));

        EntregaFardamento entrega = repository
                .findByFuncionarioIdAndItemFardamentoId(funcionarioId, itemId)
                .orElseGet(() -> EntregaFardamento.builder()
                        .funcionario(funcionario)
                        .itemFardamento(item)
                        .build());

        if (entrega.isEntregue()) {
            throw new RuntimeException("Item já foi entregue a este funcionário.");
        }

        if (!"NENHUM".equals(item.getTipoTamanho()) && tamanho != null && !tamanho.isBlank()) {
            ItemTamanhoEstoque entry = tamanhoRepository
                    .findByItemFardamentoIdAndTamanho(itemId, tamanho)
                    .orElseThrow(() -> new RuntimeException("Tamanho não encontrado: " + tamanho));
            if (entry.getQuantidade() <= 0) {
                throw new RuntimeException("Estoque insuficiente para tamanho " + tamanho);
            }
            entry.setQuantidade(entry.getQuantidade() - 1);
            tamanhoRepository.save(entry);
            entrega.setTamanho(tamanho);

            // Recalcula total do item
            List<ItemTamanhoEstoque> todos = tamanhoRepository.findByItemFardamentoId(itemId);
            int total = todos.stream().mapToInt(ItemTamanhoEstoque::getQuantidade).sum();
            item.setQuantidade(total);
        } else {
            if (item.getQuantidade() <= 0) {
                throw new RuntimeException("Estoque insuficiente para este item.");
            }
            item.setQuantidade(item.getQuantidade() - 1);
        }

        itemRepository.save(item);
        entrega.setEntregue(true);
        entrega.setDataEntrega(LocalDate.now());
        return repository.save(entrega);
    }

    @Transactional
    public EntregaFardamento devolver(Long entregaId) {
        EntregaFardamento entrega = repository.findById(entregaId)
                .orElseThrow(() -> new RuntimeException("Entrega não encontrada: " + entregaId));

        if (!entrega.isEntregue()) {
            throw new RuntimeException("Item não está marcado como entregue.");
        }

        ItemFardamento item = entrega.getItemFardamento();
        String tamanho = entrega.getTamanho();

        if (!"NENHUM".equals(item.getTipoTamanho()) && tamanho != null && !tamanho.isBlank()) {
            tamanhoRepository.findByItemFardamentoIdAndTamanho(item.getId(), tamanho)
                    .ifPresent(entry -> {
                        entry.setQuantidade(entry.getQuantidade() + 1);
                        tamanhoRepository.save(entry);
                    });
            // Recalcula total
            List<ItemTamanhoEstoque> todos = tamanhoRepository.findByItemFardamentoId(item.getId());
            int total = todos.stream().mapToInt(ItemTamanhoEstoque::getQuantidade).sum();
            item.setQuantidade(total);
        } else {
            item.setQuantidade(item.getQuantidade() + 1);
        }

        itemRepository.save(item);
        entrega.setEntregue(false);
        entrega.setDataEntrega(null);
        return repository.save(entrega);
    }
}
