package com.example.SiteLinkinPark.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.SiteLinkinPark.model.Musica;
import com.example.SiteLinkinPark.model.MusicaService;
import com.example.SiteLinkinPark.model.Playlist;
import com.example.SiteLinkinPark.model.PlaylistService;
import com.example.SiteLinkinPark.model.Usuario;
import com.example.SiteLinkinPark.model.UsuarioDAO;

@Controller
public class PlaylistController {

    @Autowired
    private MusicaService musicaService;

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private UsuarioDAO usuarioDAO;

    @PostMapping("/playlist")
    public String criarPlaylist(@RequestParam String nomePlaylist,
                                @RequestParam(required = false) List<String> musicaIds,
                                Principal principal,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        Usuario user = getUsuarioLogado(principal);
        if (user == null) {
            return "redirect:/login";
        }

        if (musicaIds == null || musicaIds.isEmpty()) {
            model.addAttribute("erro", "Escolha pelo menos uma música para criar a playlist.");
            return prepararPaginaMusicas(principal, model);
        }

        Playlist playlist = new Playlist(user.getId(), nomePlaylist);
        playlistService.criarPlaylist(playlist, musicaIds);
        redirectAttributes.addFlashAttribute("success", "Playlist criada com sucesso!");
        return "redirect:/playlist/" + playlist.getId();
    }

    @GetMapping("/playlists")
    public String minhasPlaylists(Principal principal, Model model) {
        Usuario user = getUsuarioLogado(principal);
        if (user == null) {
            return "redirect:/login";
        }
        List<Playlist> playlists = playlistService.listarPlaylists(UUID.fromString(user.getId()));
        model.addAttribute("playlists", playlists);
        model.addAttribute("nomeUsuario", user.getNome());
        return "playlists";
    }

    @GetMapping("/playlist/{playlistId}")
    public String verPlaylist(@PathVariable String playlistId, Principal principal, Model model) {
        Usuario user = getUsuarioLogado(principal);
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
                                   Principal principal,
                                   RedirectAttributes redirectAttributes) {
        Usuario user = getUsuarioLogado(principal);
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
                                Principal principal,
                                RedirectAttributes redirectAttributes) {
        Usuario user = getUsuarioLogado(principal);
        if (user == null) {
            return "redirect:/login";
        }

        playlistService.removerMusica(UUID.fromString(playlistId), musicaId);
        redirectAttributes.addFlashAttribute("success", "Música removida da playlist.");
        return "redirect:/playlist/" + playlistId;
    }

    @PostMapping("/playlist/{playlistId}/deletar")
    public String deletarPlaylist(@PathVariable String playlistId,
                                  Principal principal,
                                  RedirectAttributes redirectAttributes) {
        Usuario user = getUsuarioLogado(principal);
        if (user == null) {
            return "redirect:/login";
        }

        playlistService.deletarPlaylist(UUID.fromString(playlistId));
        redirectAttributes.addFlashAttribute("success", "Playlist excluída com sucesso!");
        return "redirect:/playlists";
    }

    private Usuario getUsuarioLogado(Principal principal) {
        if (principal == null) {
            return null;
        }
        return usuarioDAO.buscarPorEmail(principal.getName());
    }

    private String prepararPaginaMusicas(Principal principal, Model model) {
        List<Musica> musicas = musicaService.listarMusicas();
        Map<String, List<Musica>> musicasPorAlbum = new LinkedHashMap<>();
        for (Musica musica : musicas) {
            musicasPorAlbum.computeIfAbsent(musica.getAlbum(), k -> new ArrayList<>()).add(musica);
        }
        model.addAttribute("musicasPorAlbum", musicasPorAlbum);
        model.addAttribute("selectedIds", Collections.emptyList());

        Usuario user = getUsuarioLogado(principal);
        if (user != null) {
            model.addAttribute("nomeUsuario", user.getNome());
            model.addAttribute("emailUsuario", user.getEmail());
        }
        return "musicas";
    }
}
