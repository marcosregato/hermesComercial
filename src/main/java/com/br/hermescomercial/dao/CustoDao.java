/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.br.hermescomercial.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.br.hermescomercial.Repository.RepositoryCusto;
import com.br.hermescomercial.connectionDB.ConnectionBD;
import com.br.hermescomercial.model.Custo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author marcos
 */
public class CustoDao implements RepositoryCusto{

	private ConnectionBD con = null;
	private final Statement smt = null;
	private ResultSet rs = null;
    private static final Logger logger = LogManager.getLogger(CustoDao.class);
	
	
	@Override
	public void salvar(Custo custo) {
		try {
			con  = new ConnectionBD();
			String query ="INSERT INTO custo (id, custounitario, custototal) VALUES (NULL, ?, ?)";
			PreparedStatement ps = con.getConnection("").prepareStatement(query);

			// ps.setFloat(1, custo.getCustoUnitario());
			//ps.setFloat(2, custo.getCustoTotal());

			ps.executeUpdate();
			ps.close();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
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
			logger.error(e.getMessage(), e);
		}

	}
	@Override
	public void update(Custo custo) {
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
	@Override
	public List<Custo> listar() {
		try {
			con  = new ConnectionBD();
			String query ="select * from fornecedor";
			List<Custo> lista = new ArrayList<>();
			PreparedStatement ps = con.getConnection("").prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				Custo custo = new Custo();
					//custo.setrs.getFloat("nome"));
					//custo.setCustoTotal(rs.getFloat("subproduto"));

				lista.add(custo);
			}

			return lista;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return Collections.emptyList();
	}
	@Override
	public List<Custo> buscar(String nome) {
		try {

			String query =  "SELECT * FROM custo WHERE nome =?";
			PreparedStatement ps = con.getConnection("").prepareStatement(query);
			ps.setString(1, nome);
            List<Custo> lista = new ArrayList<>();
			rs = ps.executeQuery();

			if (rs.next()) {

				Custo item = new Custo();
				//custo.setCustoUnitario(rs.getFloat("custounitario"));
				//custo.setCustoTotal(rs.getFloat("custototal"));
                lista.add(item);
			}

			rs.close();
			ps.close();
			return lista;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return Collections.emptyList();
	}



}
