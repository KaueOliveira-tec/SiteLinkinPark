package com.example.SiteLinkinPark.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.SiteLinkinPark.model.Musica;
import com.example.SiteLinkinPark.model.MusicaService;
import com.example.SiteLinkinPark.model.Usuario;
import com.example.SiteLinkinPark.model.UsuarioDAO;

@Controller
public class MusicaController {

    @Autowired
    private MusicaService musicaService;

    @Autowired
    private UsuarioDAO usuarioDAO;

    @GetMapping("/musicas")
    public String listarMusicas(Principal principal, Model model) {
        List<Musica> musicas = musicaService.listarMusicas();
        Map<String, List<Musica>> musicasPorAlbum = new LinkedHashMap<>();
        for (Musica musica : musicas) {
            musicasPorAlbum.computeIfAbsent(musica.getAlbum(), k -> new ArrayList<>()).add(musica);
        }
        model.addAttribute("musicasPorAlbum", musicasPorAlbum);
        model.addAttribute("selectedIds", Collections.emptyList());

        if (principal != null) {
            Usuario user = usuarioDAO.buscarPorEmail(principal.getName());
            if (user != null) {
                model.addAttribute("nomeUsuario", user.getNome());
                model.addAttribute("emailUsuario", user.getEmail());
            }
        }
        return "musicas";
    }
}
