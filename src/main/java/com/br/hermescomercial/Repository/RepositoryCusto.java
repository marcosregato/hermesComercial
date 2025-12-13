package com.br.hermescomercial.Repository;

import java.util.List;

import com.br.hermescomercial.model.Custo;

public interface RepositoryCusto {
	
	void salvar(Custo custo);

	void remove(String nome);

	void update(Custo custo);

	List<Custo> listar();

	List<Custo> buscar(String nome);

}
