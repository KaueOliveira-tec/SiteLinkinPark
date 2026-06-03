package com.example.SiteLinkinPark.model;

import java.io.Serializable;
import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Musica implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    @NotBlank(message = "Título da música é obrigatório")
    @Size(min = 1, max = 200, message = "Título deve ter entre 1 e 200 caracteres")
    private String titulo;

    @Size(max = 200, message = "Álbum deve ter no máximo 200 caracteres")
    private String album;

    @NotBlank(message = "Artista é obrigatório")
    @Size(min = 1, max = 200, message = "Artista deve ter entre 1 e 200 caracteres")
    private String artista;

    public Musica() {
    }

    public Musica(String titulo, String album, String artista) {
        this.titulo = titulo;
        this.album = album;
        this.artista = artista;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtista() {
        return artista;
    }

    public void setArtista(String artista) {
        this.artista = artista;
    }

    public static Musica conversor(Map<String, Object> registro) {
        Musica musica = new Musica();
        musica.setId(registro.get("id").toString());
        musica.setTitulo((String) registro.get("titulo"));
        musica.setAlbum((String) registro.get("album"));
        musica.setArtista((String) registro.get("artista"));
        return musica;
    }
}
