package com.br.hermescomercial.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.br.hermescomercial.model.Atributo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.br.hermescomercial.Repository.RepositoryAlertaEstoque;
import com.br.hermescomercial.connectionDB.ConnectionBD;
import com.br.hermescomercial.model.AlertaEstoque;

public class AlertaEstoqueDao implements RepositoryAlertaEstoque{

	private ConnectionBD con = null;
	private ResultSet rs = null;
	
	  private static final Logger logger = LogManager.getLogger(AlertaEstoqueDao.class);


	@Override
	public void salvar(AlertaEstoque alertaEstoque) {
		try {
			con  = new ConnectionBD();
			String query ="INSERT INTO fornecedor (id, nome, tipofornecedor) VALUES (NULL, ?, ?)";
			PreparedStatement ps = con.getConnection("").prepareStatement(query);

			//ps.setString(1, fornecedor.getNome());
			//ps.setString(2, fornecedor.getTipoFornecedor());

			ps.executeUpdate();
			ps.close();

		} catch (Exception e) {
			logger.error("Error saving alert", e);
		}

	}

	@Override
	public void remove(String nome) {
		try {

			con  = new ConnectionBD();
			String query = "DELETE FROM custo WHERE nome=?";
			PreparedStatement ps = con.getConnection("").prepareStatement(query);
			ps.setString(1, nome);
			ps.executeUpdate();
			ps.close();

		} catch (Exception e) {
			logger.error("Error removing alert", e);
		}

	}

	@Override
	public void update(AlertaEstoque alertaEstoque) {
		try {

			con  = new ConnectionBD();
			String query = "update custo set custounitario = ?,custototal = ?";
			PreparedStatement ps = con.getConnection("").prepareStatement(query);
			//ps.setFloat(1, custo.getCustoUnitario());
			//ps.setFloat(2, custo.getCustoTotal());

			ps.close();

		} catch (Exception e) {
			logger.error("Error updating alert", e);
		}

	}

	@Override
	public List<AlertaEstoque> listar() {
		try {

			con  = new ConnectionBD();
			String query ="select * from fornecedor";
			List<AlertaEstoque> lista = new ArrayList<>();
			PreparedStatement ps = con.getConnection("").prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				AlertaEstoque item = new AlertaEstoque();
					item.setValor(rs.getString("nome"));
					item.setTempoEstoque(rs.getString("subproduto"));

				lista.add(item);
			}

			return lista;

		} catch (Exception e) {
			logger.error("Error listing alerts", e);
		}
		return Collections.emptyList();
	}

	@Override
	public List<AlertaEstoque> buscar(String nome) {
		try {
			String query =  "SELECT * FROM custo WHERE nome =?";
            PreparedStatement ps =con.getConnection("").prepareStatement(query);
            List<AlertaEstoque> lista = new ArrayList<>();
            ps.setString(1, nome);

            rs = ps.executeQuery();
            if (rs.next()) {
                AlertaEstoque alerta =new AlertaEstoque();
                alerta.setTempoEstoque(rs.getString(""));
                alerta.setValor(rs.getString("valor"));
                lista.add(alerta);
            }

            rs.close();
            ps.close();
            return lista;


		} catch (Exception e) {
			logger.error("Error searching alert", e);
		}
		return Collections.emptyList();
	}
	
	
	public String getValidadeEstoque() {
		String dia = null;
		return dia;
	}

}
