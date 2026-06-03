package com.example.SiteLinkinPark.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM playlist WHERE usuario_id = ?", Integer.class, playlist.getUsuarioId());
        if (count != null && count >= 50) {
            throw new RuntimeException("Você atingiu o limite máximo de 50 playlists");
        }

        Integer nomeCount = jdbc.queryForObject("SELECT COUNT(*) FROM playlist WHERE usuario_id = ? AND LOWER(nome) = LOWER(?)", Integer.class, playlist.getUsuarioId(), playlist.getNome());
        if (nomeCount != null && nomeCount > 0) {
            throw new RuntimeException("Você já possui uma playlist com este nome");
        }

        String sqlPlaylist = "INSERT INTO playlist(id, usuario_id, nome) VALUES (?,?,?)";
        UUID playlistId = UUID.randomUUID();
        playlist.setId(playlistId);

        jdbc.update(sqlPlaylist, playlistId, playlist.getUsuarioId(), playlist.getNome());
        logger.info("Playlist criada: {} para usuário {}", playlist.getNome(), playlist.getUsuarioId());

        String sqlLink = "INSERT INTO playlist_musica(playlist_id, musica_id) VALUES (?,?)";
        for (String musicaId : musicaIds) {
            jdbc.update(sqlLink, playlistId, UUID.fromString(musicaId));
            logger.debug("Adicionada música {} à playlist {}", musicaId, playlistId);
        }
    }

    public List<Playlist> listarPlaylistsPorUsuario(UUID usuarioId) {
        String sql = "SELECT * FROM playlist WHERE usuario_id = ? ORDER BY nome";
        List<Map<String, Object>> registros = jdbc.queryForList(sql, usuarioId);
        List<Playlist> playlists = new ArrayList<>();
        for (Map<String, Object> registro : registros) {
            playlists.add(Playlist.conversor(registro));
        }
        logger.debug("Carregadas {} playlists para o usuário {}", playlists.size(), usuarioId);
        return playlists;
    }

    public Playlist buscarPlaylist(UUID playlistId) {
        String sql = "SELECT * FROM playlist WHERE id = ?";
        List<Map<String, Object>> registros = jdbc.queryForList(sql, playlistId);
        if (registros.isEmpty()) {
            return null;
        }

        Playlist playlist = Playlist.conversor(registros.get(0));
        playlist.setMusicas(buscarMusicasDaPlaylist(playlistId));
        return playlist;
    }

    private List<Musica> buscarMusicasDaPlaylist(UUID playlistId) {
        String sql = "SELECT m.* FROM musica m JOIN playlist_musica pm ON m.id = pm.musica_id WHERE pm.playlist_id = ? ORDER BY m.album, m.titulo";
        List<Map<String, Object>> registros = jdbc.queryForList(sql, playlistId);
        List<Musica> musicas = new ArrayList<>();
        for (Map<String, Object> registro : registros) {
            musicas.add(Musica.conversor(registro));
        }
        return musicas;
    }

    public void atualizarPlaylistMusicas(UUID playlistId, List<String> musicaIds) {
        if (musicaIds != null && musicaIds.size() > 500) {
            throw new RuntimeException("Uma playlist pode conter no máximo 500 músicas");
        }

        if (musicaIds != null && musicaIds.size() != musicaIds.stream().distinct().count()) {
            throw new RuntimeException("Não é permitido adicionar a mesma música mais de uma vez na playlist");
        }

        String sqlDelete = "DELETE FROM playlist_musica WHERE playlist_id = ?";
        jdbc.update(sqlDelete, playlistId);

        String sqlLink = "INSERT INTO playlist_musica(playlist_id, musica_id) VALUES (?,?)";
        if (musicaIds != null) {
            for (String musicaId : musicaIds) {
                if (musicaId != null && !musicaId.isBlank()) {
                    jdbc.update(sqlLink, playlistId, UUID.fromString(musicaId));
                }
            }
        }
        logger.info("Playlist {} atualizada com {} músicas", playlistId, musicaIds == null ? 0 : musicaIds.size());
    }

    public void removerMusica(UUID playlistId, UUID musicaId) {
        String sql = "DELETE FROM playlist_musica WHERE playlist_id = ? AND musica_id = ?";
        jdbc.update(sql, playlistId, musicaId);
        logger.info("Removida música {} da playlist {}", musicaId, playlistId);
    }

    public void deletarPlaylist(UUID playlistId) {
        String sql = "DELETE FROM playlist WHERE id = ?";
        jdbc.update(sql, playlistId);
        logger.info("Playlist {} deletada", playlistId);
    }
}
