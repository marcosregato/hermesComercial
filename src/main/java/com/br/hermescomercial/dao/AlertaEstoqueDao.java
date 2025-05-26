package com.br.hermescomercial.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.br.hermescomercial.Repository.RepositoryAlertaEstoque;
import com.br.hermescomercial.connectionDB.ConnectionMySQL;
import com.br.hermescomercial.connectionDB.ConnectionSQLite;
import com.br.hermescomercial.model.AlertaEstoque;

public class AlertaEstoqueDao implements RepositoryAlertaEstoque{

	private ConnectionSQLite con = null;
	private final Statement smt = null;
	private ResultSet rs = null;
	
	  Logger logger = Logger.getLogger(getClass().getName());


	@Override
	public void salvar(AlertaEstoque alertaEstoque) {
		try {
			con  = new ConnectionSQLite();
			String query ="INSERT INTO fornecedor (id, nome, tipofornecedor) VALUES (NULL, ?, ?)";
			PreparedStatement ps = con.getConnection().prepareStatement(query);

			//ps.setString(1, fornecedor.getNome());
			//ps.setString(2, fornecedor.getTipoFornecedor());

			ps.executeUpdate();
			ps.close();

		} catch (Exception e) {
			logger.info(e.getMessage());
		}

	}

	@Override
	public void remove(String nome) {
		try {

			con  = new ConnectionSQLite();
			String query = "DELETE FROM custo WHERE nome=?";
			PreparedStatement ps = con.getConnection().prepareStatement(query);
			ps.setString(1, nome);
			ps.executeUpdate();
			rs.close();
			ps.close();

		} catch (Exception e) {
			logger.info(e.getMessage());
		}

	}

	@Override
	public void update(AlertaEstoque alertaEstoque) {
		try {

			con  = new ConnectionSQLite();
			String query = "update custo set custounitario = ?,custototal = ?";
			PreparedStatement ps = con.getConnection().prepareStatement(query);
			//ps.setFloat(1, custo.getCustoUnitario());
			//ps.setFloat(2, custo.getCustoTotal());

			rs.close();
			ps.close();

		} catch (Exception e) {
			logger.info(e.getMessage());
		}

	}

	@Override
	public List<AlertaEstoque> listar() {
		try {

			con  = new ConnectionSQLite();
			String query ="select * from fornecedor";
			List<AlertaEstoque> lista = new ArrayList<>();
			PreparedStatement ps = con.getConnection().prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				AlertaEstoque item = new AlertaEstoque();
				//	item.setCustoUnitario(rs.getFloat("nome"));
				//	item.setCustoTotal(rs.getFloat("subproduto"));

				lista.add(item);
			}

			return lista;

		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return null;
	}

	@Override
	public List<AlertaEstoque> buscar(String nome) {
		try {
			  /*            String query =  "SELECT * FROM custo WHERE nome =?";
            PreparedStatement ps = con.connection().prepareStatement(query);
            ps.setString(1, nome);

            rs = ps.executeQuery();
            AlertaEstoque alerta = null;
            if (rs.next()) {

                alerta = new AlertaEstoque();

                //custo.setCustoUnitario(rs.getFloat("custounitario"));
                //custo.setCustoTotal(rs.getFloat("custototal"));


            }

            rs.close();
            ps.close();
            return atributo;

 */

			
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
		return null;
	}
	
	
	public String getValidadeEstoque() {
		String dia = null;
		return dia;
	}




}
