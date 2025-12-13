package com.br.hermescomercial.Repository;

import java.util.List;

import com.br.hermescomercial.model.Estoque;
import com.br.hermescomercial.model.Produto;

public interface RepositoryEstoque {
	
	void salvar(Estoque estoque);

	void remove(String nome);

	void update(Estoque produto);

	List<Estoque> listar();

	List<Estoque> buscar(String nome);

}
