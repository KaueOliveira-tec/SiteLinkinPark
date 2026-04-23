package com.example.SiteLinkinPark.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.SiteLinkinPark.model.Usuario;
import com.example.SiteLinkinPark.model.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MenuController {

    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/")
    public String paginaPrincipal(){
        return "index";
    }

    @GetMapping("/musicas")
    public String musica(){
        return "musicas";
    }

    @GetMapping("/integrantes_originais")
    public String integranteOriginal(){
        return "integrantes_originais";
    }

    @GetMapping("/integrantes_atuais")
    public String integranteAtual(){
        return "integrantes_atuais";
    }

    @GetMapping("/form_user")
	public String formUser(Model model) {
		model.addAttribute("usuario",new Usuario());
		return "form_user";
	}

    @PostMapping("/usuario")
	public String postCliente(@ModelAttribute Usuario usuario,
			                  Model model) {
        try {
            logger.info("Cadastrando novo usuário com email: {}", usuario.getEmail());
            usuarioService.cadastroUsuario(usuario);
            logger.info("Cadastro bem-sucedido para: {}", usuario.getEmail());
            return "form_sucesso";
        } catch (Exception e) {
            logger.error("Erro ao cadastrar usuário com email: {}", usuario.getEmail(), e);
            model.addAttribute("erro", "Erro ao cadastrar usuário. Tente novamente.");
            model.addAttribute("usuario", usuario);
            return "form_user";
        }
	}

    @GetMapping("/login")
    public String telaLogin() {
        return "login";
    }

    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model) {
        Usuario user = (Usuario) session.getAttribute("usuarioLogado");
        if (user != null) {
            model.addAttribute("nomeUsuario", user.getNome());
            model.addAttribute("emailUsuario", user.getEmail());
            model.addAttribute("uuid", user.getId());
        }
        return "perfil";
    }

    @GetMapping("/editar_usuario")
    public String editarUsuario(HttpSession session, Model model) {
        Usuario user = (Usuario) session.getAttribute("usuarioLogado");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuario", user);
        return "editar_usuario";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @PostMapping("/usuario/atualizar")
    public String atualizarUsuario(@ModelAttribute Usuario usuario,
                                   @RequestParam String emailAtual,
                                   @RequestParam String senhaAtual,
                                   HttpSession session,
                                   Model model) {
        try {
            logger.info("Atualizando usuário com email: {}", emailAtual);
            boolean atualizado = usuarioService.atualizarUsuario(usuario, emailAtual, senhaAtual);
            if (atualizado) {
                logger.info("Usuário atualizado com sucesso: {}", emailAtual);
                session.setAttribute("usuarioLogado", usuario);
                return "form_sucesso";
            }
            logger.warn("Falha ao atualizar usuário: {} - Credenciais inválidas", emailAtual);
            model.addAttribute("erro", "Atualização falhou. Verifique o e-mail e senha atuais e tente novamente.");
            model.addAttribute("usuario", usuario);
            return "editar_usuario";
        } catch (Exception e) {
            logger.error("Erro ao atualizar usuário: {}", emailAtual, e);
            model.addAttribute("erro", "Erro ao processar a atualização. Tente novamente.");
            model.addAttribute("usuario", usuario);
            return "editar_usuario";
        }
    }

    @PostMapping("/efetuarLogin")
    public String efetuarLogin(@RequestParam String email, @RequestParam String senha, HttpSession session, Model model) {
        try {
            logger.info("Tentativa de login com email: {}", email);
            Usuario user = usuarioService.login(email, senha);

            if (user != null) {
                logger.info("Login bem-sucedido para usuário: {}", email);
                session.setAttribute("usuarioLogado", user);
                model.addAttribute("nomeUsuario", user.getNome());
                model.addAttribute("emailUsuario", user.getEmail());
                model.addAttribute("uuid", user.getId());
                return "perfil";
            } else {
                logger.warn("Falha no login para email: {} - Usuário não encontrado ou senha inválida", email);
                model.addAttribute("erro", "Conta não encontrada ou senha inválida");
                return "login";
            }
        } catch (Exception e) {
            logger.error("Erro durante o login para email: {}", email, e);
            model.addAttribute("erro", "Erro ao processar o login. Tente novamente.");
            return "login";
        }
    }

    @PostMapping("/usuario/excluir")
    public String excluirUsuario(@RequestParam String email,
                                 @RequestParam String senha,
                                 HttpSession session,
                                 Model model) {
        try {
            Usuario user = (Usuario) session.getAttribute("usuarioLogado");
            if (user == null) {
                logger.warn("Tentativa de exclusão sem usuário logado");
                return "redirect:/login";
            }

            if (!user.getEmail().equals(email)) {
                logger.warn("Tentativa de exclusão com email diferente: {} vs {}", user.getEmail(), email);
                return "redirect:/login";
            }

            logger.info("Iniciando exclusão de conta para: {}", email);
            boolean excluido = usuarioService.excluirUsuario(email, senha);
            if (excluido) {
                logger.info("Conta excluída com sucesso: {}", email);
                session.invalidate();
                return "form_sucesso";
            }

            logger.warn("Falha ao excluir conta: {} - Credenciais inválidas", email);
            model.addAttribute("erro", "Falha ao excluir conta. Verifique a senha e tente novamente.");
            model.addAttribute("nomeUsuario", user.getNome());
            model.addAttribute("emailUsuario", user.getEmail());
            model.addAttribute("uuid", user.getId());
            return "perfil";
        } catch (Exception e) {
            logger.error("Erro ao excluir usuário", e);
            Usuario user = (Usuario) session.getAttribute("usuarioLogado");
            model.addAttribute("erro", "Erro ao processar exclusão. Tente novamente.");
            if (user != null) {
                model.addAttribute("nomeUsuario", user.getNome());
                model.addAttribute("emailUsuario", user.getEmail());
                model.addAttribute("uuid", user.getId());
            }
            return "perfil";
        }
    }
}