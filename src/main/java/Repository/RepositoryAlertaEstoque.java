package Repository;

import java.util.List;

import model.AlertaEstoque;

public interface RepositoryAlertaEstoque {
	
	void salvar(AlertaEstoque alertaEstoque);

	void remove(String nome);

	void update(AlertaEstoque alertaEstoque);

	List<AlertaEstoque> listar();

	List<AlertaEstoque> buscar(String nome);

}
