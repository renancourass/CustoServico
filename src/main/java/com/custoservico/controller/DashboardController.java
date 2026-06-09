package com.custoservico.controller;

import com.custoservico.model.CustoServico;
import com.custoservico.model.Usuario;
import com.custoservico.service.CustoServicoService;
import com.custoservico.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private CustoServicoService custoServicoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"));

        List<CustoServico> custos;
        if (isAdmin) {
            custos = custoServicoService.listarAtivos();
        } else {
            Usuario usuario = usuarioService.buscarPorUsername(auth.getName()).orElseThrow();
            custos = custoServicoService.listarPorUsuario(usuario);
        }

        model.addAttribute("custos", custos);
        model.addAttribute("totalRegistros", custos.size());
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("nomeUsuario", auth.getName());

        if (isAdmin) {
            model.addAttribute("totalUsuarios", usuarioService.listarTodos().size());
        }

        return "dashboard";
    }
}
