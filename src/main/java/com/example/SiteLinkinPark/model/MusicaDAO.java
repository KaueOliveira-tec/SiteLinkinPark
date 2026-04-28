package com.example.SiteLinkinPark.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;

@Repository
public class MusicaDAO {

    private static final Logger logger = LoggerFactory.getLogger(MusicaDAO.class);

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbc;

    @PostConstruct
    public void initialize() {
        jdbc = new JdbcTemplate(dataSource);
    }

    public List<Musica> listarMusicas() {
        String sql = "SELECT * FROM musica ORDER BY titulo";
        List<Map<String, Object>> registros = jdbc.queryForList(sql);
        List<Musica> musicas = new ArrayList<>();
        for (Map<String, Object> registro : registros) {
            musicas.add(Musica.conversor(registro));
        }
        logger.debug("Carregadas {} músicas do banco", musicas.size());
        return musicas;
    }
}
