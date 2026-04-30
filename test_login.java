import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.br.hermescomercial.util.DatabaseConfig;

public class test_login {
    public static void main(String[] args) {
        try {
            String sql = "SELECT u.id, u.nome, l.login, 'ADMIN' as perfil FROM usuario u " +
                        "INNER JOIN login l ON l.fk_usuario = u.id " +
                        "WHERE l.login = ? AND l.senha = ? AND l.ativo = TRUE";
            
            Connection conn = DatabaseConfig.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "admin");
            pstmt.setString(2, "admin");
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("✅ Login bem-sucedido!");
                System.out.println("ID: " + rs.getLong("id"));
                System.out.println("Nome: " + rs.getString("nome"));
                System.out.println("Login: " + rs.getString("login"));
                System.out.println("Perfil: " + rs.getString("perfil"));
            } else {
                System.out.println("❌ Login falhou!");
            }
            
            rs.close();
            pstmt.close();
            conn.close();
            
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
