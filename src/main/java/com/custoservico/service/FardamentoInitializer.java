package com.custoservico.service;

import com.custoservico.model.ItemFardamento;
import com.custoservico.repository.ItemFardamentoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class FardamentoInitializer implements CommandLineRunner {

    private final ItemFardamentoRepository repository;

    public FardamentoInitializer(ItemFardamentoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) return;

        // Fardamento — Porteiro
        repository.save(ItemFardamento.builder().nome("Camisa Social Manga Longa").funcao("PORTEIRO").quantidade(0).unidade("peça").ativo(true).build());
        repository.save(ItemFardamento.builder().nome("Calça").funcao("PORTEIRO").quantidade(0).unidade("peça").ativo(true).build());

        // Fardamento — ASG (Zelador)
        repository.save(ItemFardamento.builder().nome("Bota").funcao("ASG").quantidade(0).unidade("par").ativo(true).build());
        repository.save(ItemFardamento.builder().nome("Camisa de Proteção UV").funcao("ASG").quantidade(0).unidade("peça").ativo(true).build());
        repository.save(ItemFardamento.builder().nome("Calça").funcao("ASG").quantidade(0).unidade("peça").ativo(true).build());
        repository.save(ItemFardamento.builder().nome("Luva").funcao("ASG").quantidade(0).unidade("par").ativo(true).build());
    }
}
