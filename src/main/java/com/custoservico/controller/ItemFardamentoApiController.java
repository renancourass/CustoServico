package com.custoservico.controller;

import com.custoservico.model.ItemFardamento;
import com.custoservico.model.ItemTamanhoEstoque;
import com.custoservico.service.ItemFardamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fardamento")
@CrossOrigin(origins = "*")
public class ItemFardamentoApiController {

    private final ItemFardamentoService service;

    public ItemFardamentoApiController(ItemFardamentoService service) {
        this.service = service;
    }

    @GetMapping
    public List<ItemFardamento> listar() {
        return service.listarAtivos();
    }

    @GetMapping("/funcao/{funcao}")
    public List<ItemFardamento> listarPorFuncao(@PathVariable String funcao) {
        return service.listarPorFuncao(funcao);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemFardamento> buscar(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/tamanhos")
    public ResponseEntity<List<ItemTamanhoEstoque>> listarTamanhos(@PathVariable Long id) {
        return ResponseEntity.ok(service.listarTamanhos(id));
    }

    @PostMapping
    public ResponseEntity<ItemFardamento> salvar(@RequestBody ItemFardamento item) {
        try {
            item.setAtivo(true);
            return ResponseEntity.ok(service.salvar(item));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemFardamento> atualizar(@PathVariable Long id, @RequestBody ItemFardamento dados) {
        try {
            return ResponseEntity.ok(service.atualizar(id, dados));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/quantidade")
    public ResponseEntity<Void> ajustarQuantidade(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        try {
            service.ajustarQuantidade(id, body.get("quantidade"));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/tamanhos/{tamanhoId}/quantidade")
    public ResponseEntity<ItemTamanhoEstoque> ajustarTamanho(
            @PathVariable Long tamanhoId,
            @RequestBody Map<String, Integer> body) {
        try {
            int delta = body.getOrDefault("delta", 0);
            return ResponseEntity.ok(service.ajustarTamanho(tamanhoId, delta));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        try {
            service.excluir(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
