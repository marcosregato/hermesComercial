package Repository;

import java.util.List;

import model.Login;

public interface RepositoryLogin {
	
public void salvar(Login produto);
	
	public void delete(String nome);
	
	public void update(Login produto);
	
	public List<Login> lista();
	
	public List<Login> buscar();

}
