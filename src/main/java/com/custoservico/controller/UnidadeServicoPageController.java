package com.custoservico.controller;

import com.custoservico.model.Usuario;
import com.custoservico.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UnidadeServicoPageController {

    private final UsuarioService usuarioService;

    public UnidadeServicoPageController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    private void addCommonAttrs(Model model, Authentication auth) {
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);
        usuarioService.buscarPorUsername(auth.getName())
                .map(Usuario::getNome)
                .ifPresent(nome -> model.addAttribute("nomeUsuario", nome));
    }

    @GetMapping("/unidade-servico")
    public String unidadeServico(Model model, Authentication auth) {
        addCommonAttrs(model, auth);
        return "unidade-servico";
    }

    @GetMapping("/fardamento-estoque")
    public String fardamentoEstoque(Model model, Authentication auth) {
        addCommonAttrs(model, auth);
        return "fardamento-estoque";
    }

    @GetMapping("/fardamento-relatorio")
    public String fardamentoRelatorio(Model model, Authentication auth) {
        addCommonAttrs(model, auth);
        return "fardamento-relatorio";
    }
}
