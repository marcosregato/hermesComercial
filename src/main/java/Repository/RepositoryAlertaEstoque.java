package Repository;

import java.util.List;

import model.AlertaEstoque;

public interface RepositoryAlertaEstoque {
	
	public void salvar(AlertaEstoque alertaEstoque);

	public void remove(String nome);

	public void update(AlertaEstoque alertaEstoque);

	public List<AlertaEstoque> listar();

	public List<AlertaEstoque> buscar(String nome);

}
