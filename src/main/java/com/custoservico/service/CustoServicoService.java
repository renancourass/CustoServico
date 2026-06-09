package com.custoservico.service;

import com.custoservico.model.CustoServico;
import com.custoservico.model.Usuario;
import com.custoservico.repository.CustoServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustoServicoService {

    @Autowired
    private CustoServicoRepository repository;

    public List<CustoServico> listarAtivos() {
        return repository.findByAtivoTrueOrderByCriadoEmDesc();
    }

    public List<CustoServico> listarPorUsuario(Usuario usuario) {
        return repository.findByUsuarioAndAtivoTrueOrderByCriadoEmDesc(usuario);
    }

    public Optional<CustoServico> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public CustoServico salvar(CustoServico custo) {
        return repository.save(custo);
    }

    public CustoServico atualizar(Long id, CustoServico dados) {
        CustoServico existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro não encontrado"));
        dados.setId(id);
        dados.setCriadoEm(existente.getCriadoEm());
        dados.setUsuario(existente.getUsuario());
        return repository.save(dados);
    }

    public void excluir(Long id) {
        CustoServico c = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro não encontrado"));
        c.setAtivo(false);
        repository.save(c);
    }

    public long contarAtivos() {
        return repository.countAtivos();
    }
}
