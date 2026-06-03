package com.example.SiteLinkinPark.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.SiteLinkinPark.model.UsuarioDAO;
import com.example.SiteLinkinPark.model.Usuario;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioDAO.buscarPorEmail(email);

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + email);
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        if (email.equals("admin@linkinpark.com")) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
