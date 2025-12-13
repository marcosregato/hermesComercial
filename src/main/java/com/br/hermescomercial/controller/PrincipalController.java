package com.br.hermescomercial.controller;

import java.util.List;

import com.br.hermescomercial.dao.LoginDao;
import com.br.hermescomercial.model.Usuario;
import org.apache.log4j.Logger;

public class PrincipalController {
	
	
    private String login;
    private String senha;
    LoginController loginController;

    Logger logger = Logger.getLogger(getClass().getName());
    private String tipoDeAcesso;
    
    LoginDao dao = new LoginDao();
    
    
    public void setUsuarioLogado(String login, String senha) {
    	this.login = login;
    	this.senha = senha;
    	
    }
    
  
    public List<Usuario> infoUsuario(){
       try {
    	   List<Usuario> lista = dao.acessarUsuario(login, senha);
		return lista;
	} catch (Exception e) {
		logger.info(e.getMessage());
	}
       return null;
    }
}
