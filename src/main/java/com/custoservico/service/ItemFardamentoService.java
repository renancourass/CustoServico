package com.custoservico.service;

import com.custoservico.model.ItemFardamento;
import com.custoservico.model.ItemTamanhoEstoque;
import com.custoservico.repository.ItemFardamentoRepository;
import com.custoservico.repository.ItemTamanhoEstoqueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ItemFardamentoService {

    private final ItemFardamentoRepository repository;
    private final ItemTamanhoEstoqueRepository tamanhoRepository;

    public ItemFardamentoService(ItemFardamentoRepository repository,
                                  ItemTamanhoEstoqueRepository tamanhoRepository) {
        this.repository = repository;
        this.tamanhoRepository = tamanhoRepository;
    }

    public List<ItemFardamento> listarAtivos() {
        return repository.findByAtivoTrue();
    }

    public List<ItemFardamento> listarPorFuncao(String funcao) {
        return repository.findByFuncaoAndAtivoTrue(funcao.toUpperCase());
    }

    public Optional<ItemFardamento> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public List<ItemTamanhoEstoque> listarTamanhos(Long itemId) {
        return tamanhoRepository.findByItemFardamentoId(itemId);
    }

    @Transactional
    public ItemFardamento salvar(ItemFardamento item) {
        ItemFardamento salvo = repository.save(item);
        criarEntradasTamanhoSeNecessario(salvo);
        return salvo;
    }

    @Transactional
    public ItemFardamento atualizar(Long id, ItemFardamento dados) {
        ItemFardamento existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item não encontrado: " + id));
        existente.setNome(dados.getNome());
        existente.setFuncao(dados.getFuncao());
        existente.setQuantidade(dados.getQuantidade());
        existente.setUnidade(dados.getUnidade());
        if (dados.getTipoTamanho() != null) {
            existente.setTipoTamanho(dados.getTipoTamanho());
        }
        return repository.save(existente);
    }

    public void ajustarQuantidade(Long id, int quantidade) {
        ItemFardamento existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item não encontrado: " + id));
        existente.setQuantidade(quantidade);
        repository.save(existente);
    }

    @Transactional
    public ItemTamanhoEstoque ajustarTamanho(Long tamanhoId, int delta) {
        ItemTamanhoEstoque entry = tamanhoRepository.findById(tamanhoId)
                .orElseThrow(() -> new RuntimeException("Tamanho não encontrado: " + tamanhoId));

        int novaQtd = entry.getQuantidade() + delta;
        if (novaQtd < 0) novaQtd = 0;
        entry.setQuantidade(novaQtd);
        tamanhoRepository.save(entry);

        // Recalcula total do item somando todos os tamanhos
        ItemFardamento item = entry.getItemFardamento();
        List<ItemTamanhoEstoque> todos = tamanhoRepository.findByItemFardamentoId(item.getId());
        int total = todos.stream().mapToInt(ItemTamanhoEstoque::getQuantidade).sum();
        item.setQuantidade(total);
        repository.save(item);

        return entry;
    }

    public void excluir(Long id) {
        ItemFardamento item = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item não encontrado: " + id));
        item.setAtivo(false);
        repository.save(item);
    }

    private void criarEntradasTamanhoSeNecessario(ItemFardamento item) {
        if ("NENHUM".equals(item.getTipoTamanho())) return;

        List<ItemTamanhoEstoque> existentes = tamanhoRepository.findByItemFardamentoId(item.getId());
        if (!existentes.isEmpty()) return;

        List<String> tamanhos = "VESTUARIO".equals(item.getTipoTamanho())
                ? List.of("P", "M", "G", "GG")
                : List.of("35","36","37","38","39","40","41","42","43","44","45");

        for (String t : tamanhos) {
            tamanhoRepository.save(ItemTamanhoEstoque.builder()
                    .itemFardamento(item)
                    .tamanho(t)
                    .quantidade(0)
                    .build());
        }
    }
}
