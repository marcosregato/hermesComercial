package Repository;

import java.util.List;

import model.Custo;

public interface RepositoryCusto {
	
	public void salvar(Custo custo);

	public void remove(String nome);

	public void update(Custo custo);

	public List<Custo> listar();

	public List<Custo> buscar(String nome);

}
