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

    public Usuario selectUser(String uuid){
        return usuarioDAO.selectUser(uuid);
    }
}
