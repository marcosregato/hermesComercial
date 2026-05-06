package com.br.hermescomercial;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * Teste simples de conexão com PostgreSQL
 * Para diagnosticar problemas de conexão
 */
public class TestConnectionPostgreSQL {
    
    public static void main(String[] args) {
        System.out.println("🔍 Testando conexão com PostgreSQL...");
        
        // Configurações do database-config.properties
        String host = "localhost";
        String port = "5432";
        String database = "hermescomercialdb";
        String user = "postgres";
        String password = "postgres";
        
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + database;
        
        System.out.println("📋 Configuração:");
        System.out.println("   URL: " + url);
        System.out.println("   Usuário: " + user);
        System.out.println("   Senha: " + password);
        System.out.println();
        
        try {
            // Testar driver
            System.out.println("🔧 Carregando driver PostgreSQL...");
            Class.forName("org.postgresql.Driver");
            System.out.println("✅ Driver PostgreSQL carregado com sucesso!");
            
            // Testar conexão
            System.out.println("🔗 Tentando conexão...");
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Conexão estabelecida com sucesso!");
            
            // Testar consulta simples
            System.out.println("📊 Testando consulta...");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT version()");
            if (rs.next()) {
                System.out.println("🗄️  Versão PostgreSQL: " + rs.getString(1));
            }
            
            // Verificar se tabelas existem
            System.out.println("📋 Verificando tabelas...");
            rs = stmt.executeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'");
            boolean hasTables = false;
            while (rs.next()) {
                if (!hasTables) {
                    System.out.println("   Tabelas encontradas:");
                    hasTables = true;
                }
                System.out.println("   - " + rs.getString("table_name"));
            }
            
            if (!hasTables) {
                System.out.println("   ⚠️ Nenhuma tabela encontrada");
                System.out.println("   💡 Execute o script SQL para criar as tabelas");
            }
            
            // Fechar conexão
            conn.close();
            System.out.println("✅ Teste concluído com sucesso!");
            
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Erro: Driver PostgreSQL não encontrado");
            System.out.println("   Solução: Adicionar o driver PostgreSQL ao classpath");
            System.out.println("   Comando: export CLASSPATH=$CLASSPATH:postgresql-xx.x.x.jar");
            e.printStackTrace();
            
        } catch (SQLException e) {
            System.out.println("❌ Erro de conexão SQL: " + e.getMessage());
            
            // Analisar erro específico
            String errorMsg = e.getMessage().toLowerCase();
            if (errorMsg.contains("connection refused")) {
                System.out.println("   🔍 Diagnóstico: PostgreSQL não está rodando");
                System.out.println("   Solução: sudo systemctl start postgresql");
            } else if (errorMsg.contains("authentication failed") || errorMsg.contains("password")) {
                System.out.println("   🔍 Diagnóstico: Falha de autenticação");
                System.out.println("   Solução: Verificar usuário/senha no database-config.properties");
            } else if (errorMsg.contains("database") && errorMsg.contains("does not exist")) {
                System.out.println("   🔍 Diagnóstico: Banco de dados não existe");
                System.out.println("   Solução: sudo -u postgres createdb hermescomercialdb");
            } else if (errorMsg.contains("server closed")) {
                System.out.println("   🔍 Diagnóstico: Servidor PostgreSQL fechado");
                System.out.println("   Solução: sudo systemctl start postgresql");
            }
            
            e.printStackTrace();
            
        } catch (Exception e) {
            System.out.println("❌ Erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
