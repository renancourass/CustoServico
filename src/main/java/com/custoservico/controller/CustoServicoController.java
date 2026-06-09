package com.custoservico.controller;

import com.custoservico.model.CustoServico;
import com.custoservico.model.Usuario;
import com.custoservico.service.CustoServicoService;
import com.custoservico.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/custos")
public class CustoServicoController {

    @Autowired
    private CustoServicoService custoServicoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("custo", new CustoServico());
        model.addAttribute("modo", "novo");
        return "custo-form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute CustoServico custo, Authentication auth,
                         RedirectAttributes ra) {
        try {
            Usuario usuario = usuarioService.buscarPorUsername(auth.getName()).orElseThrow();
            custo.setUsuario(usuario);
            custo.setAtivo(true);
            custoServicoService.salvar(custo);
            ra.addFlashAttribute("sucesso", "Registro salvo com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("erro", "Erro ao salvar: " + e.getMessage());
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, Authentication auth,
                         RedirectAttributes ra) {
        CustoServico custo = custoServicoService.buscarPorId(id).orElse(null);
        if (custo == null) {
            ra.addFlashAttribute("erro", "Registro não encontrado.");
            return "redirect:/dashboard";
        }
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"));
        if (!isAdmin && !custo.getUsuario().getUsername().equals(auth.getName())) {
            ra.addFlashAttribute("erro", "Acesso negado.");
            return "redirect:/dashboard";
        }
        model.addAttribute("custo", custo);
        model.addAttribute("modo", "editar");
        return "custo-form";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizar(@PathVariable Long id, @ModelAttribute CustoServico custo,
                            Authentication auth, RedirectAttributes ra) {
        try {
            custoServicoService.atualizar(id, custo);
            ra.addFlashAttribute("sucesso", "Registro atualizado com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("erro", "Erro ao atualizar: " + e.getMessage());
        }
        return "redirect:/dashboard";
    }

    @GetMapping("/visualizar/{id}")
    public String visualizar(@PathVariable Long id, Model model, Authentication auth,
                             RedirectAttributes ra) {
        CustoServico custo = custoServicoService.buscarPorId(id).orElse(null);
        if (custo == null) {
            ra.addFlashAttribute("erro", "Registro não encontrado.");
            return "redirect:/dashboard";
        }
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"));
        if (!isAdmin && !custo.getUsuario().getUsername().equals(auth.getName())) {
            ra.addFlashAttribute("erro", "Acesso negado.");
            return "redirect:/dashboard";
        }
        model.addAttribute("custo", custo);
        model.addAttribute("isAdmin", isAdmin);
        return "custo-visualizar";
    }

    @PostMapping("/excluir/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String excluir(@PathVariable Long id, RedirectAttributes ra) {
        try {
            custoServicoService.excluir(id);
            ra.addFlashAttribute("sucesso", "Registro excluído com sucesso.");
        } catch (Exception e) {
            ra.addFlashAttribute("erro", "Erro ao excluir.");
        }
        return "redirect:/dashboard";
    }
}
