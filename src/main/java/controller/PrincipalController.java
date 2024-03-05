package controller;

import java.util.List;

import dao.LoginDao;
import model.Usuario;

public class PrincipalController {
	
	
    private final String login;
    private final String senha;
    LoginController loginController;
    
    private String tipoDeAcesso;
    
    LoginDao dao = new LoginDao();
    
    
    public PrincipalController(String login, String senha){
        this.login = login;
        this.senha = senha;

    }
    public List<Usuario> infoUsuario(){
       try {
    	   List<Usuario> lista = dao.acessarUsuario(login, senha);
		return lista;
	} catch (Exception e) {
		// TODO: handle exception
		System.out.println(e.getMessage());
	}
       return null;
    }
}
