package com.example.SiteLinkinPark.controller;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.SiteLinkinPark.model.Usuario;
import com.example.SiteLinkinPark.model.UsuarioDAO;
import com.example.SiteLinkinPark.model.UsuarioService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioDAO usuarioDAO;

    @GetMapping("/form_user")
    public String exibirFormularioUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "form_user";
    }

    @PostMapping("/usuario")
    public String cadastrarUsuario(@Valid @ModelAttribute Usuario usuario, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("usuario", usuario);
            return "form_user";
        }

        try {
            logger.info("Cadastrando novo usuário com email: {}", usuario.getEmail());
            usuarioService.cadastroUsuario(usuario);
            logger.info("Cadastro bem-sucedido para: {}", usuario.getEmail());
            return "redirect:/login?cadastro=true";
        } catch (Exception e) {
            logger.error("Erro ao cadastrar usuário com email: {}", usuario.getEmail(), e);
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("usuario", usuario);
            return "form_user";
        }
    }

    @GetMapping("/login")
    public String exibirTelaLogin() {
        return "login";
    }

    @GetMapping("/perfil")
    public String exibirPerfil(Principal principal, Model model) {
        if (principal != null) {
            Usuario user = usuarioDAO.buscarPorEmail(principal.getName());
            if (user != null) {
                model.addAttribute("nomeUsuario", user.getNome());
                model.addAttribute("emailUsuario", user.getEmail());
                model.addAttribute("uuid", user.getId());
            }
        }
        return "perfil";
    }

    @GetMapping("/editar_usuario")
    public String editarUsuario(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }
        Usuario user = usuarioDAO.buscarPorEmail(principal.getName());
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuario", user);
        return "editar_usuario";
    }

    @PostMapping("/usuario/atualizar")
    public String atualizarUsuario(@Valid @ModelAttribute Usuario usuario,
                                   BindingResult bindingResult,
                                   @RequestParam String emailAtual,
                                   @RequestParam String senhaAtual,
                                   HttpSession session,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("usuario", usuario);
            return "editar_usuario";
        }

        try {
            logger.info("Atualizando usuário com email: {}", emailAtual);
            boolean atualizado = usuarioService.atualizarUsuario(usuario, emailAtual, senhaAtual);
            if (atualizado) {
                logger.info("Usuário atualizado com sucesso: {}", emailAtual);
                return "form_sucesso";
            }
            logger.warn("Falha ao atualizar usuário: {} - Credenciais inválidas", emailAtual);
            model.addAttribute("erro", "Atualização falhou. Verifique o e-mail e senha atuais e tente novamente.");
            model.addAttribute("usuario", usuario);
            return "editar_usuario";
        } catch (Exception e) {
            logger.error("Erro ao atualizar usuário: {}", emailAtual, e);
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("usuario", usuario);
            return "editar_usuario";
        }
    }

    @PostMapping("/usuario/excluir")
    public String excluirUsuarioConta(@RequestParam String email,
                                 @RequestParam String senha,
                                 HttpSession session,
                                 Principal principal,
                                 Model model) {
        try {
            logger.info("Iniciando exclusão de conta para: {}", email);
            boolean excluido = usuarioService.excluirUsuario(email, senha);
            if (excluido) {
                logger.info("Conta excluída com sucesso: {}", email);
                session.invalidate();
                return "form_sucesso";
            }
            logger.warn("Falha ao excluir conta: {} - Credenciais inválidas", email);
            if (principal != null) {
                Usuario user = usuarioDAO.buscarPorEmail(principal.getName());
                if (user != null) {
                    model.addAttribute("nomeUsuario", user.getNome());
                    model.addAttribute("emailUsuario", user.getEmail());
                    model.addAttribute("uuid", user.getId());
                }
            }
            model.addAttribute("erro", "Falha ao excluir conta. Verifique a senha e tente novamente.");
            return "perfil";
        } catch (Exception e) {
            logger.error("Erro ao excluir usuário", e);
            if (principal != null) {
                Usuario user = usuarioDAO.buscarPorEmail(principal.getName());
                if (user != null) {
                    model.addAttribute("nomeUsuario", user.getNome());
                    model.addAttribute("emailUsuario", user.getEmail());
                    model.addAttribute("uuid", user.getId());
                }
            }
            model.addAttribute("erro", "Erro ao processar exclusão. Tente novamente.");
            return "perfil";
        }
    }
}
