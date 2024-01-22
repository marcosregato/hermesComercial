package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connectionDB.ConnectionPostgreSQL;
import model.Estoque;

public class EstoqueDao {
	
	private ConnectionPostgreSQL con = null;
    private Statement smt = null;
    private ResultSet rs = null;
	
	public List<Estoque> listaQuantidadeEstoque(){
		try {
			
			con  = new ConnectionPostgreSQL();
            String query ="select * from cliente";
            List<Estoque> lista = new ArrayList<>();
            PreparedStatement ps = con.connection().prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
            	Estoque item = new Estoque();
            	item.setQuantidade(rs.getString("quantidade"));
      

                lista.add(item);
            }

            return lista;
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return null;
	}

}
