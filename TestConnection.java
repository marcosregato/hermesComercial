import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) {
        System.out.println("=== Teste de Conexão com Banco de Dados ===");
        
        // Testar com H2 (banco em memória)
        testConnection("org.h2.Driver", "jdbc:h2:mem:hermescomercial;DB_CLOSE_DELAY=-1;MODE=MySQL", "sa", "");
        
        // Testar com PostgreSQL
        testConnection("org.postgresql.Driver", "jdbc:postgresql://localhost:5432/hermescomercial", "postgres", "senha");
        
        // Testar com MySQL
        testConnection("com.mysql.cj.jdbc.Driver", "jdbc:mysql://localhost:3306/hermescomercial", "root", "senha");
    }
    
    private static void testConnection(String driver, String url, String user, String password) {
        System.out.println("\n--- Testando com: " + driver + " ---");
        System.out.println("URL: " + url);
        System.out.println("Usuário: " + user);
        
        try {
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, user, password);
            
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ CONEXÃO BEM-SUCEDIDA!");
                System.out.println("Banco de dados: " + conn.getMetaData().getDatabaseProductName());
                System.out.println("Versão: " + conn.getMetaData().getDatabaseProductVersion());
                conn.close();
                System.out.println("Conexão fechada com sucesso.");
            } else {
                System.out.println("❌ FALHA NA CONEXÃO: Conexão nula ou fechada.");
            }
            
        } catch (ClassNotFoundException e) {
            System.out.println("❌ DRIVER NÃO ENCONTRADO: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("❌ ERRO DE CONEXÃO: " + e.getMessage());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Error Code: " + e.getErrorCode());
        } catch (Exception e) {
            System.out.println("❌ ERRO GERAL: " + e.getMessage());
        }
        
        System.out.println("----------------------------------------");
    }
}
