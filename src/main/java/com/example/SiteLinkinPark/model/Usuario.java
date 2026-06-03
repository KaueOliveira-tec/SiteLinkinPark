package com.example.SiteLinkinPark.model;

import java.io.Serializable;
import java.util.Map;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import com.example.SiteLinkinPark.config.ValidPassword;

public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @ValidPassword
    private String senha;

    //Construtor para o formulario
    public Usuario() {

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

    public static Usuario conversor(Map<String,Object> registro) {
        String nome = (String) registro.get("nome");
        Object idValue = registro.get("id");
        String id = idValue != null ? idValue.toString() : null;
        String email = (String) registro.get("email");
        String senha = (String) registro.get("senha");
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        return usuario;
    }
}