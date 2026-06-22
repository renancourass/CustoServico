package com.custoservico.controller;

import com.custoservico.model.Funcionario;
import com.custoservico.service.FuncionarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
@CrossOrigin(origins = "*")
public class FuncionarioApiController {

    private final FuncionarioService service;

    public FuncionarioApiController(FuncionarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<Funcionario> listar() {
        return service.listarAtivos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> buscar(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Funcionario> salvar(@RequestBody Funcionario funcionario) {
        try {
            funcionario.setAtivo(true);
            return ResponseEntity.ok(service.salvar(funcionario));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Funcionario> atualizar(@PathVariable Long id, @RequestBody Funcionario dados) {
        try {
            return ResponseEntity.ok(service.atualizar(id, dados));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
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
