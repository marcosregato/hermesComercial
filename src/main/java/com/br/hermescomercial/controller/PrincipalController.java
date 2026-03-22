package com.br.hermescomercial.controller;

import com.br.hermescomercial.dao.LoginDao;
import com.br.hermescomercial.model.Usuario;
import org.apache.logging.log4j.LogManager;

import java.util.List;

public class PrincipalController {
	
	
    private String login;
    private String senha;
    LoginController loginController;

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(PrincipalController.class);
    private String tipoDeAcesso;
    
    LoginDao dao = new LoginDao();
    
    
    public void setUsuarioLogado(String login, String senha) {
    	this.login = login;
    	this.senha = senha;
    	
    }
    
  
    public List<Usuario> infoUsuario(String login, String senha) {
        try {
            Usuario usuario = dao.acessarUsuario(login, senha);
            if (usuario != null) {
                return List.of(usuario);
            }
        } catch (Exception e) {
            logger.error("Erro ao buscar informações do Usuario: " + e.getMessage());
        }
        return null;
    }
}
