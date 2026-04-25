package com.br.hermescomercial.controller;

import com.br.hermescomercial.dao.LoginDao;
import com.br.hermescomercial.model.Pessoa;
import org.apache.logging.log4j.LogManager;

public class PrincipalController {
	
	
    LoginController loginController;

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(PrincipalController.class);
    
    LoginDao dao = new LoginDao();
    
    
        
  
    public Pessoa infoPessoa(String login, String senha) {
        try {
            return dao.acessarPessoa(login, senha);
        } catch (Exception e) {
            logger.error("Erro ao buscar informações do usuário: " + e.getMessage());
        }
        return null;
    }
}
