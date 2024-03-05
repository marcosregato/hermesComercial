package Repository;

import model.AlertaEstoque;
import model.Atributo;

import java.util.List;

public interface RepositoryAtributo {

    void salvar(Atributo atributo);

    void remove(String nome);

    void update(Atributo atributo);

    List<Atributo> listar();

    List<Atributo> buscar(String nome);
}
