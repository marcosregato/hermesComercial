package com.br.hermescomercial.connectionDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionSQLite {

	private Connection conecta;
	public Connection getConnection(){
		try {
			Class.forName("org.sqlite.JDBC");
			conecta =  DriverManager.getConnection("jdbc:sqlite:./db/teste.db");
		} catch (SQLException e) {
			System.err.println("Problemas na hora de registrar driver");
			System.err.println("Saindo...");
			System.exit(1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("conexao feito com sucesso");
		return conecta;
	}

}
