package com.br.hermescomercial.Repository;

import java.util.List;

import com.br.hermescomercial.model.Venda;

public interface RepositoryCusto {
	
	void salvar(Venda custo);

	void remove(String nome);

	void update(Venda custo);

	List<Venda> listar();

	List<Venda> buscar(String nome);

}
