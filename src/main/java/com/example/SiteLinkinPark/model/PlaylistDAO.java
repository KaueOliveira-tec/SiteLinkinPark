package com.example.SiteLinkinPark.model;

import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;

@Repository
public class PlaylistDAO {

    private static final Logger logger = LoggerFactory.getLogger(PlaylistDAO.class);

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbc;

    @PostConstruct
    public void initialize() {
        jdbc = new JdbcTemplate(dataSource);
    }

    public void salvarPlaylist(Playlist playlist, List<String> musicaIds) {
        String sqlPlaylist = "INSERT INTO playlist(id, usuario_id, nome) VALUES (?,?,?)";
        String playlistId = UUID.randomUUID().toString();
        playlist.setId(playlistId);

        jdbc.update(sqlPlaylist, playlistId, playlist.getUsuarioId(), playlist.getNome());
        logger.info("Playlist criada: {} para usuário {}", playlist.getNome(), playlist.getUsuarioId());

        String sqlLink = "INSERT INTO playlist_musica(playlist_id, musica_id) VALUES (?,?)";
        for (String musicaId : musicaIds) {
            jdbc.update(sqlLink, playlistId, musicaId);
            logger.debug("Adicionada música {} à playlist {}", musicaId, playlistId);
        }
    }
}
