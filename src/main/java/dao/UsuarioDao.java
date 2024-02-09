package dao;

import java.util.List;

import Repository.RepositoryUsuario;
import model.Usuario;

public class UsuarioDao implements RepositoryUsuario {

	@Override
	public void salvar(Usuario usuario) {
		try {

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

	}

	@Override
	public void remove(String nome) {
		try {

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}		
	}

	@Override
	public void update(Usuario usuario) {
		try {

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}		
	}

	@Override
	public List<Usuario> lista() {
		try {

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}	
		return null;
	}

	public List<Usuario> buscar(String nome) {
		try {

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}		return null;
	}


}
