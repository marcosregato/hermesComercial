package Repository;

import java.util.List;

import model.Despesa;

public interface RepositoryDespesa {
	
	void salvar(Despesa despesa);

	void remove(String nome);

	void update(Despesa despesa);

	List<Despesa> listar();

	List<Despesa> buscar(String nome);

}
