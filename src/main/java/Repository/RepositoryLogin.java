package Repository;

import java.util.List;

import model.Login;

public interface RepositoryLogin {
	
void salvar(Login produto);
	
	void delete(String nome);
	
	void update(Login produto);
	
	List<Login> lista();
	
	List<Login> buscar();

}
