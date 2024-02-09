package Repository;

import java.util.List;

import model.Estoque;
import model.Fornecedor;

public interface RepositoryFornecedor {
	
	public void salvar(Fornecedor fornecedor);

	public void remove(String nome);

	public void update(Fornecedor fornecedor);

	public List<Fornecedor> listar();

	public List<Fornecedor> buscar(String nome);

}
