package dao;

import java.util.List;

import Repository.RepositoryAlertaEstoque;
import model.AlertaEstoque;

public class AlertaEstoqueDao implements RepositoryAlertaEstoque{

	@Override
	public void salvar(AlertaEstoque alertaEstoque) {
		try {

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

	}

	@Override
	public void remove(String nome) {
		try {

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

	}

	@Override
	public void update(AlertaEstoque alertaEstoque) {
		try {

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}

	}

	@Override
	public List<AlertaEstoque> listar() {
		try {

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		return null;
	}

	@Override
	public List<AlertaEstoque> buscar(String nome) {
		try {

			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	
	public String getValidadeEstoque() {
		String dia = null;
		return dia;
	}




}
