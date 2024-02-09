package Repository;

import java.util.List;

import model.Produto;

public interface RepositoryProduto {
	
	public void salvar(Produto produto);
	
	public void remove(String nome);
	
	public void update(Produto produto);
	
	public List<Produto> listar();
	
	public List<Produto> buscar(String nome);

}
