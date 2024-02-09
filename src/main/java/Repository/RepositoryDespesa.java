package Repository;

import java.util.List;

import model.Despesa;

public interface RepositoryDespesa {
	
	public void salvar(Despesa despesa);

	public void remove(String nome);

	public void update(Despesa despesa);

	public List<Despesa> listar();

	public List<Despesa> buscar(String nome);

}
