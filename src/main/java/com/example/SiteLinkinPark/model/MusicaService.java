package com.example.SiteLinkinPark.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MusicaService {

    @Autowired
    private MusicaDAO musicaDAO;

    public List<Musica> listarMusicas() {
        return musicaDAO.listarMusicas();
    }

    public void cadastrarMusica(Musica musica) {
        musicaDAO.cadastrarMusica(musica);
    }

    public int cadastrarMusicasEmLote(String album, String artista, String titulos) {
        if (album == null || album.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o álbum.");
        }

        if (artista == null || artista.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe o artista.");
        }

        if (titulos == null || titulos.trim().isEmpty()) {
            throw new IllegalArgumentException("Informe pelo menos uma música.");
        }

        int totalCadastrado = 0;
        String albumTratado = album.trim();
        String artistaTratado = artista.trim();

        String[] linhas = titulos.split("\\r?\\n");
        for (String linha : linhas) {
            String titulo = linha.trim();

            if (titulo.isEmpty()) {
                continue;
            }

            Musica musica = new Musica(titulo, albumTratado, artistaTratado);
            musicaDAO.cadastrarMusica(musica);
            totalCadastrado++;
        }

        if (totalCadastrado == 0) {
            throw new IllegalArgumentException("Informe pelo menos uma música válida.");
        }

        return totalCadastrado;
    }
}
