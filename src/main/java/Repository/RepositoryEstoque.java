package Repository;

import java.util.List;

import model.Estoque;

public interface RepositoryEstoque {
	
	public void salvar(Estoque estoque);

	public void delete(String nome);

	public void update(Estoque produto);

	public List<Estoque> listar();

	public List<Estoque> buscar();

}
