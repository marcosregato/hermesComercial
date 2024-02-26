package Repository;

import model.AlertaEstoque;
import model.Atributo;

import java.util.List;

public interface RepositoryAtributo {

    public void salvar(Atributo atributo);

    public void remove(String nome);

    public void update(Atributo atributo);

    public List<Atributo> listar();

    public List<Atributo> buscar(String nome);
}
