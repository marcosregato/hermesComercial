package Repository;

import java.util.List;

import model.Usuario;

public interface RepositoryUsuario {
	
	
	public void salvar(Usuario usuario);
	
	public void remove(String nome);
	
	public void update(Usuario usuario);
	
	public List<Usuario> lista();
	
	public List<Usuario> buscar(String nome);
	
	
	

}
