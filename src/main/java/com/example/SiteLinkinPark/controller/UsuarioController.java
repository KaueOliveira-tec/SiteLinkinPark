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
    public String cadastrarUsuario(
            @Valid @ModelAttribute Usuario usuario, 
            BindingResult bindingResult, 
            Model model) {
                
        if (bindingResult.hasErrors()) {
            model.addAttribute("usuario", usuario);
            return "form_user";
        }

        try {
            logger.info("Cadastrando novo usuário com email: {}", usuario.getEmail());
            usuarioService.cadastroUsuario(usuario);
            logger.info("Cadastro bem-sucedido para: {}", usuario.getEmail());
            return "redirect:/form_sucesso?tipo=cadastro";
        } catch (Exception e) {
            logger.error("Erro ao cadastrar usuário com email: {}", usuario.getEmail(), e);
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("usuario", usuario);
            return "form_user";
        }
    }

    @GetMapping("/form_sucesso")
    public String exibirTelaSucesso(@RequestParam(required = false) String tipo, Model model) {
        String titulo = "Feito com sucesso!";
        String mensagem = "Operação realizada com sucesso.";
        String urlRetorno = "/";
        String textoBotao = "Retornar";

        if ("cadastro".equals(tipo)) {
            titulo = "Cadastro realizado com sucesso!";
            mensagem = "Sua conta foi criada. Agora você já pode fazer login.";
            urlRetorno = "/login";
            textoBotao = "Ir para Login";
        } else if ("atualizacao".equals(tipo)) {
            titulo = "Perfil atualizado com sucesso!";
            mensagem = "Suas informações foram atualizadas. Faça login novamente para continuar.";
            urlRetorno = "/login";
            textoBotao = "Ir para Login";
        } else if ("exclusao".equals(tipo)) {
            titulo = "Conta excluída com sucesso!";
            mensagem = "Seu cadastro foi removido do sistema.";
            urlRetorno = "/";
            textoBotao = "Voltar à página inicial";
        }

        model.addAttribute("titulo", titulo);
        model.addAttribute("mensagem", mensagem);
        model.addAttribute("urlRetorno", urlRetorno);
        model.addAttribute("textoBotao", textoBotao);
        return "form_sucesso";
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
        user.setSenha("");
        model.addAttribute("usuario", user);
        return "editar_usuario";
    }

    @PostMapping("/usuario/atualizar")
    public String atualizarUsuario(@Valid @ModelAttribute Usuario usuario,
                                   BindingResult bindingResult,
                                   @RequestParam String senhaAtual,
                                   HttpSession session,
                                   Principal principal,
                                   Model model) {

        if(principal == null) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("usuario", usuario);
            return "editar_usuario";
        }

        try {
            String emailLogado = principal.getName();
            logger.info("Atualizando usuário logado com email: {}", emailLogado);
            boolean atualizado = usuarioService.atualizarUsuario(usuario, emailLogado, senhaAtual);

            if (atualizado) {
                logger.info("Usuário atualizado com sucesso: {}", emailLogado);
                session.invalidate();
                return "redirect:/form_sucesso?tipo=atualizacao";
            }

            logger.warn("Falha ao atualizar usuário: {} - senha atual inválida", emailLogado);
            model.addAttribute("erro", "Senha atual incorreta.");
            model.addAttribute("usuario", usuario);
            return "editar_usuario";
        } 
        catch (Exception e) {
            logger.error("Erro ao atualizar usuário: {}", e);
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("usuario", usuario);
            return "editar_usuario";
        }
    }

    @PostMapping("/usuario/excluir")
     public String excluirUsuarioConta(@RequestParam String senha,
                                      HttpSession session,
                                      Principal principal,
                                      Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        try {
            String emailLogado = principal.getName();
            logger.info("Iniciando exclusão de conta para: {}", emailLogado);
            boolean excluido = usuarioService.excluirUsuario(emailLogado, senha);
            if (excluido) {
                logger.info("Conta excluída com sucesso: {}", emailLogado);
                session.invalidate();
                return "redirect:/form_sucesso?tipo=exclusao";
            }
            logger.warn("Falha ao excluir conta: {} - senha inválida", emailLogado);
            Usuario user = usuarioDAO.buscarPorEmail(emailLogado);
            if (user != null) {
                model.addAttribute("nomeUsuario", user.getNome());
                model.addAttribute("emailUsuario", user.getEmail());
                model.addAttribute("uuid", user.getId());
            }
            model.addAttribute("erro", "Senha incorreta.");
            return "perfil";
        } catch (Exception e) {
            logger.error("Erro ao excluir usuário", e);
            Usuario user = usuarioDAO.buscarPorEmail(principal.getName());
            if (user != null) {
                model.addAttribute("nomeUsuario", user.getNome());
                model.addAttribute("emailUsuario", user.getEmail());
                model.addAttribute("uuid", user.getId());
            }
            model.addAttribute("erro", "Erro ao processar exclusão. Tente novamente.");
            return "perfil";
        }
    }
}
