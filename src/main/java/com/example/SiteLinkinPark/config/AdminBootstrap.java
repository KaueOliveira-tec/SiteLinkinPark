package com.example.SiteLinkinPark.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.example.SiteLinkinPark.model.Usuario;
import com.example.SiteLinkinPark.model.UsuarioDAO;
import com.example.SiteLinkinPark.model.UsuarioService;

@Configuration
public class AdminBootstrap {

    @Bean
    public CommandLineRunner initAdminUser(UsuarioService usuarioService, UsuarioDAO usuarioDAO, JdbcTemplate jdbcTemplate) {
        return args -> {
            try {
                Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM usuario WHERE email = ?", Integer.class, "admin@linkinpark.com");

                if (count == null || count == 0) {
                    Usuario admin = new Usuario("Administrador", "admin@linkinpark.com", "admin123");
                    usuarioService.cadastroUsuario(admin);
                    System.out.println("  Usuário admin criado com sucesso!");
                    System.out.println("  Email: admin@linkinpark.com");
                    System.out.println("  Senha: admin123");
                }
            } catch (Exception e) {
                System.out.println("Erro ao inicializar usuário admin: " + e.getMessage());
            }
        };
    }
}
