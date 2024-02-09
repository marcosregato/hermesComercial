package dao;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import Repository.RepositoryDespesa;
import connectionDB.ConnectionMySQL;
import model.Despesa;

public class DespesaDao implements RepositoryDespesa{

	private ConnectionMySQL con = null;
	private Statement smt = null;
	private ResultSet rs = null;


	public void salvar(Despesa despesa) {
		try {

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
	public void remove(String nome) {
		try {

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
	public void update(Despesa despesa) {
		try {

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
	public List<Despesa> listar() {
		try {

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	public List<Despesa> buscar(String nome) {
		try {

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}


}
