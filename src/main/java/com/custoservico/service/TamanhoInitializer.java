package com.custoservico.service;

import com.custoservico.model.ItemFardamento;
import com.custoservico.model.ItemTamanhoEstoque;
import com.custoservico.repository.ItemFardamentoRepository;
import com.custoservico.repository.ItemTamanhoEstoqueRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Component
@Order(3)
public class TamanhoInitializer implements CommandLineRunner {

    private final ItemFardamentoRepository itemRepository;
    private final ItemTamanhoEstoqueRepository tamanhoRepository;

    public TamanhoInitializer(ItemFardamentoRepository itemRepository,
                               ItemTamanhoEstoqueRepository tamanhoRepository) {
        this.itemRepository = itemRepository;
        this.tamanhoRepository = tamanhoRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (tamanhoRepository.count() > 0) return;

        List<ItemFardamento> itens = itemRepository.findByAtivoTrue();
        for (ItemFardamento item : itens) {
            String tipo = detectarTipo(item.getNome());
            item.setTipoTamanho(tipo);
            itemRepository.save(item);

            if ("NENHUM".equals(tipo)) continue;

            List<String> tamanhos = "VESTUARIO".equals(tipo)
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

    private String detectarTipo(String nome) {
        if (nome == null) return "NENHUM";
        String lower = nome.toLowerCase();
        if (lower.contains("camisa") || lower.contains("calça") || lower.contains("calca")) {
            return "VESTUARIO";
        }
        if (lower.contains("bota")) {
            return "CALCADO";
        }
        return "NENHUM";
    }
}
