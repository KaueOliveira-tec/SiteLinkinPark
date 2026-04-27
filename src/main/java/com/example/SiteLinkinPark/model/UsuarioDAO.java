package com.example.SiteLinkinPark.model;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;

@Repository
public class UsuarioDAO {

	private static final Logger logger = LoggerFactory.getLogger(UsuarioDAO.class);

	@Autowired
	private DataSource dataSource;
	
	private JdbcTemplate jdbc;
	
	@PostConstruct
	public void initialize() {
		jdbc = new JdbcTemplate(dataSource);
	}
	
	public void cadastroUsuario(Usuario usuario) {
		try {
			String sql = "INSERT INTO usuario(id, nome, email, senha)" +
		                 " VALUES (?,?,?,?)";
			Object[] obj = new Object[4];
			//primeiro ?
			obj[0] = java.util.UUID.randomUUID();
			//segundo ?
			obj[1] = usuario.getNome();
			//terceiro ?
			obj[2] = usuario.getEmail();
			//quarto ?
			obj[3] = usuario.getSenha();
			jdbc.update(sql, obj);
			logger.info("Usuário cadastrado com sucesso: {}", usuario.getEmail());
		} catch (Exception e) {
			logger.error("Erro ao cadastrar usuário: {}", usuario.getEmail(), e);
			throw new RuntimeException("Erro ao cadastrar usuário", e);
		}
	}

	public Usuario verificarLogin(String email, String senha) {
        String sql = "SELECT * FROM usuario WHERE email = ? AND senha = ?";
        try {
            return Usuario.conversor(jdbc.queryForMap(sql, email, senha));
        } catch (EmptyResultDataAccessException e) {
            logger.info("Nenhum usuário encontrado com email: {}", email);
            return null;
        } catch (Exception e) {
            logger.error("Erro ao verificar login para email: {}. Erro: {}", email, e.getMessage(), e);
            return null;
        }
    }

    public boolean atualizarUsuario(Usuario usuario, String emailAtual, String senhaAtual) {
        try {
            String sql = "UPDATE usuario SET nome = ?, email = ?, senha = ? WHERE email = ? AND senha = ?";
            int rows = jdbc.update(sql, usuario.getNome(), usuario.getEmail(), usuario.getSenha(), emailAtual, senhaAtual);
            logger.info("Usuário atualizado: {} - Linhas afetadas: {}", emailAtual, rows);
            return rows > 0;
        } catch (Exception e) {
            logger.error("Erro ao atualizar usuário: {}", emailAtual, e);
            return false;
        }
    }

	public boolean excluirUsuario(String email, String senha) {
		try {
			String sql = "DELETE FROM usuario WHERE email = ? AND senha = ?";
			int rows = jdbc.update(sql, email, senha);
			logger.info("Usuário deletado: {} - Linhas afetadas: {}", email, rows);
			return rows > 0;
		} catch (Exception e) {
			logger.error("Erro ao deletar usuário: {}", email, e);
			return false;
		}
	}
}

