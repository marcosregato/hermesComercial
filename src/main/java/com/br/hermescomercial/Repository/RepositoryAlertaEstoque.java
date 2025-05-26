package com.br.hermescomercial.Repository;

import java.util.List;

import com.br.hermescomercial.model.AlertaEstoque;

public interface RepositoryAlertaEstoque {
	
	void salvar(AlertaEstoque alertaEstoque);

	void remove(String nome);

	void update(AlertaEstoque alertaEstoque);

	List<AlertaEstoque> listar();

	List<AlertaEstoque> buscar(String nome);

}
