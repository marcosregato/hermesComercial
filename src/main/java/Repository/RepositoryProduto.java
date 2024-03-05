package Repository;

import java.util.List;

import model.Produto;

public interface RepositoryProduto {
	
	void salvar(Produto produto);
	
	void remove(String nome);
	
	void update(Produto produto);
	
	List<Produto> listar();
	
	List<Produto> buscar(String nome);

}
