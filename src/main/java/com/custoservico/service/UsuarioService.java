package com.custoservico.service;

import com.custoservico.model.Usuario;
import com.custoservico.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public Optional<Usuario> buscarPorUsername(String username) {
        return repository.findByUsername(username);
    }

    public Usuario salvar(Usuario usuario, boolean encryptPassword) {
        if (encryptPassword) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        return repository.save(usuario);
    }

    public Usuario atualizar(Long id, Usuario dados) {
        Usuario existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        existente.setNome(dados.getNome());
        existente.setEmail(dados.getEmail());
        existente.setRole(dados.getRole());
        existente.setAtivo(dados.isAtivo());
        if (dados.getPassword() != null && !dados.getPassword().isBlank()) {
            existente.setPassword(passwordEncoder.encode(dados.getPassword()));
        }
        return repository.save(existente);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }

    public boolean usernameExiste(String username) {
        return repository.existsByUsername(username);
    }

    public boolean emailExiste(String email) {
        return repository.existsByEmail(email);
    }
}
