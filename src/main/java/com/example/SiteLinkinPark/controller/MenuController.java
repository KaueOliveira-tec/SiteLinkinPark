package com.example.SiteLinkinPark.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.SiteLinkinPark.model.Musica;
import com.example.SiteLinkinPark.model.MusicaService;
import com.example.SiteLinkinPark.model.Playlist;
import com.example.SiteLinkinPark.model.PlaylistService;
import com.example.SiteLinkinPark.model.Usuario;
import com.example.SiteLinkinPark.model.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MenuController {

    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private MusicaService musicaService;

    @Autowired
    private PlaylistService playlistService;

    @GetMapping("/")
    public String paginaPrincipal(){
        return "index";
    }

    @GetMapping("/musicas")
    public String musica(HttpSession session, Model model) {
        List<Musica> musicas = musicaService.listarMusicas();
        Map<String, List<Musica>> musicasPorAlbum = new LinkedHashMap<>();
        for (Musica musica : musicas) {
            musicasPorAlbum.computeIfAbsent(musica.getAlbum(), k -> new ArrayList<>()).add(musica);
        }
        model.addAttribute("musicasPorAlbum", musicasPorAlbum);
        model.addAttribute("selectedIds", Collections.emptyList());

        Usuario user = (Usuario) session.getAttribute("usuarioLogado");
        if (user != null) {
            model.addAttribute("nomeUsuario", user.getNome());
            model.addAttribute("emailUsuario", user.getEmail());
        }
        return "musicas";
    }

    @PostMapping("/playlist")
    public String criarPlaylist(@RequestParam String nomePlaylist,
                                @RequestParam(required = false) List<String> musicaIds,
                                HttpSession session,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        Usuario user = (Usuario) session.getAttribute("usuarioLogado");
        if (user == null) {
            return "redirect:/login";
        }

        if (musicaIds == null || musicaIds.isEmpty()) {
            model.addAttribute("erro", "Escolha pelo menos uma música para criar a playlist.");
            return musica(session, model);
        }

        Playlist playlist = new Playlist(user.getId(), nomePlaylist);
        playlistService.criarPlaylist(playlist, musicaIds);
        redirectAttributes.addFlashAttribute("success", "Playlist criada com sucesso!");
        return "redirect:/playlist/" + playlist.getId();
    }

    @GetMapping("/playlists")
    public String minhasPlaylists(HttpSession session, Model model) {
        Usuario user = (Usuario) session.getAttribute("usuarioLogado");
        if (user == null) {
            return "redirect:/login";
        }
        List<Playlist> playlists = playlistService.listarPlaylists(UUID.fromString(user.getId()));
        model.addAttribute("playlists", playlists);
        model.addAttribute("nomeUsuario", user.getNome());
        return "playlists";
    }

    @GetMapping("/playlist/{playlistId}")
    public String verPlaylist(@PathVariable String playlistId, HttpSession session, Model model) {
        Usuario user = (Usuario) session.getAttribute("usuarioLogado");
        if (user == null) {
            return "redirect:/login";
        }

        Playlist playlist = playlistService.buscarPlaylist(UUID.fromString(playlistId));
        if (playlist == null || !playlist.getUsuarioId().equals(UUID.fromString(user.getId()))) {
            return "redirect:/playlists";
        }

        List<Musica> musicas = musicaService.listarMusicas();
        Map<String, List<Musica>> musicasPorAlbum = new LinkedHashMap<>();
        for (Musica musica : musicas) {
            musicasPorAlbum.computeIfAbsent(musica.getAlbum(), k -> new ArrayList<>()).add(musica);
        }

        List<String> selectedIds = playlist.getMusicas().stream()
                .map(Musica::getId)
                .collect(Collectors.toList());

        model.addAttribute("playlist", playlist);
        model.addAttribute("musicasPorAlbum", musicasPorAlbum);
        model.addAttribute("selectedIds", selectedIds);
        model.addAttribute("nomeUsuario", user.getNome());
        return "playlist_detalhe";
    }

    @PostMapping("/playlist/{playlistId}/atualizar")
    public String atualizarPlaylist(@PathVariable String playlistId,
                                   @RequestParam(required = false) List<String> musicaIds,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        Usuario user = (Usuario) session.getAttribute("usuarioLogado");
        if (user == null) {
            return "redirect:/login";
        }

        playlistService.atualizarPlaylist(UUID.fromString(playlistId), musicaIds == null ? Collections.emptyList() : musicaIds);
        redirectAttributes.addFlashAttribute("success", "Playlist atualizada com sucesso!");
        return "redirect:/playlist/" + playlistId;
    }

    @PostMapping("/playlist/{playlistId}/musica/{musicaId}/remover")
    public String removerMusica(@PathVariable String playlistId,
                                @PathVariable String musicaId,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        Usuario user = (Usuario) session.getAttribute("usuarioLogado");
        if (user == null) {
            return "redirect:/login";
        }

        playlistService.removerMusica(UUID.fromString(playlistId), musicaId);
        redirectAttributes.addFlashAttribute("success", "Música removida da playlist.");
        return "redirect:/playlist/" + playlistId;
    }

    @PostMapping("/playlist/{playlistId}/deletar")
    public String deletarPlaylist(@PathVariable String playlistId,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        Usuario user = (Usuario) session.getAttribute("usuarioLogado");
        if (user == null) {
            return "redirect:/login";
        }

        playlistService.deletarPlaylist(UUID.fromString(playlistId));
        redirectAttributes.addFlashAttribute("success", "Playlist excluída com sucesso!");
        return "redirect:/playlists";
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