package com.br.hermescomercial.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.br.hermescomercial.Repository.RepositoryDespesa;
import com.br.hermescomercial.connectionDB.ConnectionBD;
import com.br.hermescomercial.model.Despesa;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DespesaDao implements RepositoryDespesa{

	private ConnectionBD con = null;
	private final Statement smt = null;
	private ResultSet rs = null;
    private static final Logger logger = LogManager.getLogger(DespesaDao.class);


	public void salvar(Despesa despesa) {
		try {

			con  = new ConnectionBD();
			String query ="INSERT INTO fornecedor (nome, tipofornecedor) VALUES ( ?, ?)";
			PreparedStatement ps = con.getConnection("").prepareStatement(query);

			//ps.setString(1, fornecedor.getNome());
			//ps.setString(2, fornecedor.getTipoFornecedor());

			ps.executeUpdate();
			ps.close();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}
	public void remove(String nome) {
		try {

			con  = new ConnectionBD();
			String query = "DELETE FROM custo WHERE nome=?";
			PreparedStatement ps = con.getConnection("").prepareStatement(query);
			ps.setString(1, nome);
			ps.executeUpdate();
			ps.close();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}
	public void update(Despesa despesa) {
		try {

			con  = new ConnectionBD();
			String query = "update custo set custounitario = ?,custototal = ?";
			PreparedStatement ps = con.getConnection("").prepareStatement(query);
			//ps.setFloat(1, custo.getCustoUnitario());
			//ps.setFloat(2, custo.getCustoTotal());

			ps.close();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}
	public List<Despesa> listar() {
		try {

			con  = new ConnectionBD();
			String query ="select * from fornecedor";
			List<Despesa> lista = new ArrayList<>();
			PreparedStatement ps = con.getConnection("").prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				Despesa item = new Despesa();
				//	item.setCustoUnitario(rs.getFloat("nome"));
				//	item.setCustoTotal(rs.getFloat("subproduto"));

				lista.add(item);
			}

			return lista;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return Collections.emptyList();
	}
	public List<Despesa> buscar(String nome) {
		List<Despesa> lista = new ArrayList<>();
		Despesa despesa = null;
		try {

			String query =  "SELECT * FROM custo WHERE nome =?";
            PreparedStatement ps = con.getConnection("").prepareStatement(query);
            ps.setString(1, nome);
            rs = ps.executeQuery();

			despesa = new Despesa();

                //custo.setCustoUnitario(rs.getFloat("custounitario"));
                //custo.setCustoTotal(rs.getFloat("custototal"));

			lista.add(despesa);
            ps.close();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return lista;
	}

}
