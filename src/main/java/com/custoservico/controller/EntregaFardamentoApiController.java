package com.custoservico.controller;

import com.custoservico.model.EntregaFardamento;
import com.custoservico.service.EntregaFardamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/entregas")
@CrossOrigin(origins = "*")
public class EntregaFardamentoApiController {

    private final EntregaFardamentoService service;

    public EntregaFardamentoApiController(EntregaFardamentoService service) {
        this.service = service;
    }

    @GetMapping("/funcionario/{funcionarioId}")
    public List<EntregaFardamento> listarPorFuncionario(@PathVariable Long funcionarioId) {
        return service.listarPorFuncionario(funcionarioId);
    }

    @PostMapping("/entregar")
    public ResponseEntity<?> entregar(@RequestBody Map<String, Object> body) {
        try {
            Long funcionarioId = Long.valueOf(body.get("funcionarioId").toString());
            Long itemFardamentoId = Long.valueOf(body.get("itemFardamentoId").toString());
            String tamanho = body.containsKey("tamanho") ? body.get("tamanho").toString() : "";
            return ResponseEntity.ok(service.entregar(funcionarioId, itemFardamentoId, tamanho));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @PostMapping("/devolver/{id}")
    public ResponseEntity<?> devolver(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.devolver(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }
}
