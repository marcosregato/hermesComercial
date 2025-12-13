package com.br.hermescomercial.Repository;

import java.util.List;

import com.br.hermescomercial.model.Usuario;

public interface RepositoryUsuario {
	
	
	void salvar(Usuario usuario);
	
	void remove(String nome);
	
	void update(Usuario usuario);
	
	List<Usuario> lista();
	
	List<Usuario> buscar(String nome);
	
	
	

}
