package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Repository.RepositoryDespesa;
import connectionDB.ConnectionMySQL;
import model.Atributo;
import model.Despesa;

public class DespesaDao implements RepositoryDespesa{

	private ConnectionMySQL con = null;
	private Statement smt = null;
	private ResultSet rs = null;


	public void salvar(Despesa despesa) {
		try {


			con  = new ConnectionMySQL();
			String query ="INSERT INTO fornecedor (id, nome, tipofornecedor) VALUES (NULL, ?, ?)";
			PreparedStatement ps = con.connection().prepareStatement(query);

			//ps.setString(1, fornecedor.getNome());
			//ps.setString(2, fornecedor.getTipoFornecedor());

			ps.executeUpdate();
			ps.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
	public void remove(String nome) {
		try {

			con  = new ConnectionMySQL();
			String query = "DELETE FROM custo WHERE nome=?";
			PreparedStatement ps = con.connection().prepareStatement(query);
			ps.setString(1, nome);
			ps.executeUpdate();
			rs.close();
			ps.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
	public void update(Despesa despesa) {
		try {

			con  = new ConnectionMySQL();
			String query = "update custo set custounitario = ?,custototal = ?";
			PreparedStatement ps = con.connection().prepareStatement(query);
			//ps.setFloat(1, custo.getCustoUnitario());
			//ps.setFloat(2, custo.getCustoTotal());

			rs.close();
			ps.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
	public List<Despesa> listar() {
		try {

			con  = new ConnectionMySQL();
			String query ="select * from fornecedor";
			List<Despesa> lista = new ArrayList<>();
			PreparedStatement ps = con.connection().prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				Despesa item = new Despesa();
				//	item.setCustoUnitario(rs.getFloat("nome"));
				//	item.setCustoTotal(rs.getFloat("subproduto"));

				lista.add(item);
			}

			return lista;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	public List<Despesa> buscar(String nome) {
		try {

			/*            String query =  "SELECT * FROM custo WHERE nome =?";
            PreparedStatement ps = con.connection().prepareStatement(query);
            ps.setString(1, nome);

            rs = ps.executeQuery();
            Atributo atributo = null;
            if (rs.next()) {

                atributo = new Atributo();

                //custo.setCustoUnitario(rs.getFloat("custounitario"));
                //custo.setCustoTotal(rs.getFloat("custototal"));


            }

            rs.close();
            ps.close();
            return atributo;

 */

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}


}
