package com.custoservico.controller;

import com.custoservico.model.Usuario;
import com.custoservico.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class AdminController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "admin/usuarios";
    }

    @GetMapping("/usuarios/novo")
    public String novoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", Usuario.Role.values());
        model.addAttribute("modo", "novo");
        return "admin/usuario-form";
    }

    @PostMapping("/usuarios/salvar")
    public String salvarUsuario(@ModelAttribute Usuario usuario, RedirectAttributes ra) {
        try {
            if (usuarioService.usernameExiste(usuario.getUsername())) {
                ra.addFlashAttribute("erro", "Username já cadastrado.");
                return "redirect:/admin/usuarios/novo";
            }
            usuario.setAtivo(true);
            usuarioService.salvar(usuario, true);
            ra.addFlashAttribute("sucesso", "Usuário criado com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("erro", "Erro ao criar usuário: " + e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/usuarios/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Usuario usuario = usuarioService.buscarPorId(id).orElse(null);
        if (usuario == null) {
            ra.addFlashAttribute("erro", "Usuário não encontrado.");
            return "redirect:/admin/usuarios";
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", Usuario.Role.values());
        model.addAttribute("modo", "editar");
        return "admin/usuario-form";
    }

    @PostMapping("/usuarios/atualizar/{id}")
    public String atualizarUsuario(@PathVariable Long id, @ModelAttribute Usuario usuario,
                                    RedirectAttributes ra) {
        try {
            usuarioService.atualizar(id, usuario);
            ra.addFlashAttribute("sucesso", "Usuário atualizado com sucesso!");
        } catch (Exception e) {
            ra.addFlashAttribute("erro", "Erro ao atualizar: " + e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/usuarios/excluir/{id}")
    public String excluirUsuario(@PathVariable Long id, RedirectAttributes ra) {
        try {
            usuarioService.excluir(id);
            ra.addFlashAttribute("sucesso", "Usuário removido.");
        } catch (Exception e) {
            ra.addFlashAttribute("erro", "Erro ao remover usuário.");
        }
        return "redirect:/admin/usuarios";
    }
}
