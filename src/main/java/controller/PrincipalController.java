package controller;

import dao.LoginDao;

public class PrincipalController {
	
	
    private String login;
    private String senha;
    LoginController loginController;
    
    private String tipoDeAcesso;
    
    LoginDao dao = new LoginDao();
    
    
    public PrincipalController(String login, String senha){
        this.login = login;
        this.senha = senha;

    }
    public String infoUsuario(){
       try {
		return dao.acessarUsuario(login, senha);
	} catch (Exception e) {
		// TODO: handle exception
		System.out.println(e.getMessage());
	}
       return null;
    }
}
