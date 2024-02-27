package Repository;

import java.util.List;

import model.Estoque;
import model.Produto;

public interface RepositoryEstoque {
	
	public void salvar(Estoque estoque);

	public void remove(String nome);

	public void update(Estoque produto);

	public List<Estoque> listar();

	public List<Estoque> buscar(String nome);

}
