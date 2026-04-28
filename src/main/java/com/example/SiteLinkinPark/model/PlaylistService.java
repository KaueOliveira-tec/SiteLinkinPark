package com.example.SiteLinkinPark.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistDAO playlistDAO;

    public void criarPlaylist(Playlist playlist, List<String> musicaIds) {
        playlistDAO.salvarPlaylist(playlist, musicaIds);
    }
}
