package com.example.SiteLinkinPark.model;

import java.io.Serializable;
import java.util.Map;

public class Musica implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String titulo;
    private String album;
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
