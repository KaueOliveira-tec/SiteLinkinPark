package com.example.SiteLinkinPark.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    UsuarioDAO usuarioDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void cadastroUsuario(Usuario usuario){
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuarioDAO.cadastroUsuario(usuario);
    }

    public Usuario autenticarUsuario(String email, String senha) {
        Usuario usuario = usuarioDAO.buscarPorEmail(email);
        if (usuario != null && passwordEncoder.matches(senha, usuario.getSenha())) {
            return usuario;
        }
        return null;
    }

    public boolean atualizarUsuario(Usuario usuario, String emailAtual, String senhaAtual) {
        Usuario existente = usuarioDAO.buscarPorEmail(emailAtual);
        if (existente != null && passwordEncoder.matches(senhaAtual, existente.getSenha())) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
            return usuarioDAO.atualizarUsuarioPorId(usuario, existente.getId());
        }
        return false;
    }

    public boolean excluirUsuario(String email, String senha) {
        Usuario existente = usuarioDAO.buscarPorEmail(email);
        if (existente != null && passwordEncoder.matches(senha, existente.getSenha())) {
            return usuarioDAO.excluirUsuarioPorId(existente.getId());
        }
        return false;
    }
}
