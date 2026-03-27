package com.example.SiteLinkinPark.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.SiteLinkinPark.model.Usuario;
import com.example.SiteLinkinPark.model.UsuarioService;

@Controller
public class MenuController {

    @Autowired
    private ApplicationContext context;

    @GetMapping("/")
    public String paginaPrincipal(){
        return "index";
    }

    @GetMapping("/musicas")
    public String musica(){
        return "musicas";
    }

    @GetMapping("/integrantes_originais")
    public String integranteOriginal(){
        return "integrantes_originais";
    }

    @GetMapping("/integrantes_atuais")
    public String integranteAtual(){
        return "integrantes_atuais";
    }

    @GetMapping("/form_user")
	public String formUser(Model model) {
		model.addAttribute("usuario",new Usuario());
		return "form_user";
	}

    @PostMapping("/usuario")
	public String postCliente(@ModelAttribute Usuario usuario,
			                  Model model) {
        
		UsuarioService us = context.getBean(UsuarioService.class);
		us.cadastroUsuario(usuario);
		return "form_sucesso";
	}

    @GetMapping("/perfil/{uuid}")
	public String verPerfil(@PathVariable String uuid, Model model){
		UsuarioService us = context.getBean(UsuarioService.class);
		Usuario usuario = us.selectUser(uuid);
		model.addAttribute("nomeUsuario",usuario.getNome());
        model.addAttribute("idUsuario",usuario.getId());
		model.addAttribute("emailUsuario",usuario.getEmail());
		return "perfil";
	}
}
