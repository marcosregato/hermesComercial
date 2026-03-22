package com.br.hermescomercial.Repository;

import com.br.hermescomercial.model.Usuario;
import java.util.List;

public interface RepositoryUsuario {
	
	void salvar(Usuario usuario);
	
	void remove(String nome);
	
	void update(Usuario usuario);
	
	List<Usuario> lista();
	
	List<Usuario> buscar(String nome);

}
