package com.example.SiteLinkinPark.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    UsuarioDAO usuarioDAO;

    public void cadastroUsuario(Usuario usuario){
        usuarioDAO.cadastroUsuario(usuario);
    }

    public Usuario login(String email, String senha) {
        return usuarioDAO.verificarLogin(email, senha);
    }

    public boolean atualizarUsuario(Usuario usuario, String emailAtual, String senhaAtual) {
        return usuarioDAO.atualizarUsuario(usuario, emailAtual, senhaAtual);
    }

    public boolean excluirUsuario(String email, String senha) {
        return usuarioDAO.excluirUsuario(email, senha);
    }
}
