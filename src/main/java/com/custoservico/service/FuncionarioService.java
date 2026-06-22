package com.custoservico.service;

import com.custoservico.model.Funcionario;
import com.custoservico.repository.FuncionarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FuncionarioService {

    private final FuncionarioRepository repository;

    public FuncionarioService(FuncionarioRepository repository) {
        this.repository = repository;
    }

    public List<Funcionario> listarAtivos() {
        return repository.findByAtivoTrue();
    }

    public Optional<Funcionario> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Funcionario salvar(Funcionario funcionario) {
        return repository.save(funcionario);
    }

    public Funcionario atualizar(Long id, Funcionario dados) {
        Funcionario existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado: " + id));
        existente.setNome(dados.getNome());
        existente.setCpf(dados.getCpf());
        existente.setFuncao(dados.getFuncao());
        existente.setPostoDeTrabalho(dados.getPostoDeTrabalho());
        return repository.save(existente);
    }

    public void excluir(Long id) {
        Funcionario f = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado: " + id));
        f.setAtivo(false);
        repository.save(f);
    }
}
