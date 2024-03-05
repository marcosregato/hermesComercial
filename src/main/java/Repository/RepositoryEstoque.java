package Repository;

import java.util.List;

import model.Estoque;
import model.Produto;

public interface RepositoryEstoque {
	
	void salvar(Estoque estoque);

	void remove(String nome);

	void update(Estoque produto);

	List<Estoque> listar();

	List<Estoque> buscar(String nome);

}
