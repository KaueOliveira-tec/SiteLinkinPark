package com.example.SiteLinkinPark.model;

import java.io.Serializable;
import java.util.UUID;

public class Playlist implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private UUID usuarioId;
    private String nome;

    public Playlist() {
    }

    public Playlist(UUID usuarioId, String nome) {
        this.usuarioId = usuarioId;
        this.nome = nome;
    }

    public Playlist(String usuarioId, String nome) {
        this.usuarioId = UUID.fromString(usuarioId);
        this.nome = nome;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setId(String id) {
        this.id = UUID.fromString(id);
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(UUID usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = UUID.fromString(usuarioId);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
