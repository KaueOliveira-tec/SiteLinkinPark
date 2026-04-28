package com.example.SiteLinkinPark.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MusicaService {

    @Autowired
    private MusicaDAO musicaDAO;

    public List<Musica> listarMusicas() {
        return musicaDAO.listarMusicas();
    }
}
