package com.example.SiteLinkinPark.model;


public class Usuario {
    private String id, nome, email, senha;

    //Construtor para o formulario
    public Usuario() {

    }

    //Construtor para usar SELECT
    public Usuario(String id, String email) {
        this.id = id;
        this.email = email;
    }

    //Construtor para insert
    public Usuario(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}