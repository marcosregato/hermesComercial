package com.br.hermescomercial.controller;

import com.br.hermescomercial.dao.LoginDao;
import com.br.hermescomercial.model.Usuario;
import org.apache.logging.log4j.LogManager;

public class PrincipalController {
	
	
    LoginController loginController;

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(PrincipalController.class);
    
    LoginDao dao = new LoginDao();
    
    
        
  
    public Usuario infoUsuario(String login, String senha) {
        try {
            return dao.acessarUsuario(login, senha);
        } catch (Exception e) {
            logger.error("Erro ao buscar informações do usuário: " + e.getMessage());
        }
        return null;
    }
}
