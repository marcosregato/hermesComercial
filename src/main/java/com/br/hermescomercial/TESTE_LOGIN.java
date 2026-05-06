package com.br.hermescomercial;

import com.br.hermescomercial.pdv.controller.PDVLoginSimpleController;
import com.br.hermescomercial.util.DatabaseConfig;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Teste de Login para verificar credenciais admin/admin123
 */
public class TESTE_LOGIN {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame testFrame = new JFrame("🧪 Teste de Login - Hermes Comercial PDV");
            testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            testFrame.setSize(600, 400);
            testFrame.setLocationRelativeTo(null);
            
            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            JLabel title = new JLabel("🧪 Teste de Login");
            title.setFont(new Font("Segoe UI", Font.BOLD, 16));
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            panel.add(title, gbc);
            
            JButton btnTestarConexao = new JButton("🔗 Testar Conexão BD");
            btnTestarConexao.setBackground(new Color(52, 152, 219));
            btnTestarConexao.setForeground(Color.WHITE);
            btnTestarConexao.addActionListener(e -> testarConexaoBanco());
            gbc.gridy = 1;
            panel.add(btnTestarConexao, gbc);
            
            JButton btnTestarLogin = new JButton("🔐 Testar Login ADMIN/ADMIN123");
            btnTestarLogin.setBackground(new Color(46, 204, 113));
            btnTestarLogin.setForeground(Color.WHITE);
            btnTestarLogin.addActionListener(e -> testarLoginAdmin());
            gbc.gridy = 2;
            panel.add(btnTestarLogin, gbc);
            
            JButton btnVerificarUsuarios = new JButton("👥 Verificar Usuários no BD");
            btnVerificarUsuarios.setBackground(new Color(155, 89, 182));
            btnVerificarUsuarios.setForeground(Color.WHITE);
            btnVerificarUsuarios.addActionListener(e -> verificarUsuariosBD());
            gbc.gridy = 3;
            panel.add(btnVerificarUsuarios, gbc);
            
            JButton btnCriarUsuario = new JButton("➕ Criar Usuário ADMIN");
            btnCriarUsuario.setBackground(new Color(230, 126, 34));
            btnCriarUsuario.setForeground(Color.WHITE);
            btnCriarUsuario.addActionListener(e -> criarUsuarioAdmin());
            gbc.gridy = 4;
            panel.add(btnCriarUsuario, gbc);
            
            JButton btnTestarDireto = new JButton("⚡ Testar Login Direto");
            btnTestarDireto.setBackground(new Color(231, 76, 60));
            btnTestarDireto.setForeground(Color.WHITE);
            btnTestarDireto.addActionListener(e -> testarLoginDireto());
            gbc.gridy = 5;
            panel.add(btnTestarDireto, gbc);
            
            testFrame.add(panel);
            testFrame.setVisible(true);
        });
    }
    
    private static void testarConexaoBanco() {
        try {
            Connection conn = DatabaseConfig.getConnection();
            if (conn != null && !conn.isClosed()) {
                JOptionPane.showMessageDialog(null, 
                    "✅ Conexão com banco de dados estabelecida com sucesso!\n\n" +
                    "📊 Status: Conectado\n" +
                    "🗄️  Banco: PostgreSQL\n" +
                    "🔗 URL: " + conn.getMetaData().getURL(), 
                    "🔗 Teste de Conexão", 
                    JOptionPane.INFORMATION_MESSAGE);
                conn.close();
            } else {
                JOptionPane.showMessageDialog(null, 
                    "❌ Falha na conexão com banco de dados!", 
                    "🔗 Teste de Conexão", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "❌ Erro ao testar conexão:\n\n" + e.getMessage(), 
                "🔗 Teste de Conexão", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void testarLoginAdmin() {
        boolean resultado = PDVLoginSimpleController.loginConsole("admin", "admin123");
        
        if (resultado) {
            JOptionPane.showMessageDialog(null, 
                "✅ Login ADMIN/ADMIN123 realizado com sucesso!\n\n" +
                "👤 Usuário: admin\n" +
                "🔐 Senha: admin123\n" +
                "📊 Status: Autenticado", 
                "🔐 Teste de Login", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, 
                "❌ Falha no login ADMIN/ADMIN123!\n\n" +
                "👤 Usuário: admin\n" +
                "🔐 Senha: admin123\n" +
                "📊 Status: Falha na autenticação\n\n" +
                "💡 Verifique se o usuário existe no banco de dados.", 
                "🔐 Teste de Login", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void verificarUsuariosBD() {
        try {
            Connection conn = DatabaseConfig.getConnection();
            String sql = "SELECT u.id, u.nome, l.login, l.ativo FROM usuario u " +
                        "INNER JOIN login l ON l.fk_usuario = u.id ORDER BY u.id";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {
                
                StringBuilder resultado = new StringBuilder();
                resultado.append("👥 Usuários encontrados no banco de dados:\n\n");
                
                boolean encontrou = false;
                while (rs.next()) {
                    encontrou = true;
                    resultado.append("📋 ID: ").append(rs.getInt("id"))
                           .append(" | 👤 Nome: ").append(rs.getString("nome"))
                           .append(" | 🔐 Login: ").append(rs.getString("login"))
                           .append(" | ✅ Ativo: ").append(rs.getBoolean("ativo") ? "Sim" : "Não")
                           .append("\n");
                }
                
                if (!encontrou) {
                    resultado.append("❌ Nenhum usuário encontrado no banco de dados!");
                }
                
                JOptionPane.showMessageDialog(null, 
                    resultado.toString(), 
                    "👥 Verificação de Usuários", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "❌ Erro ao verificar usuários:\n\n" + e.getMessage(), 
                "👥 Verificação de Usuários", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void criarUsuarioAdmin() {
        try {
            Connection conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);
            
            // Verificar se usuário já existe
            String checkSql = "SELECT COUNT(*) as total FROM usuario WHERE nome = 'Administrador'";
            try (PreparedStatement checkPstmt = conn.prepareStatement(checkSql);
                 ResultSet checkRs = checkPstmt.executeQuery()) {
                
                if (checkRs.next() && checkRs.getInt("total") > 0) {
                    JOptionPane.showMessageDialog(null, 
                        "⚠️ Usuário Administrador já existe no banco de dados!", 
                        "➕ Criação de Usuário", 
                        JOptionPane.WARNING_MESSAGE);
                    conn.close();
                    return;
                }
            }
            
            // Inserir usuário
            String insertUsuarioSql = "INSERT INTO usuario (nome, email, telefone, cpf_cnpj, data_cadastro) " +
                                    "VALUES ('Administrador', 'admin@hermescomercial.com', '00000000000', '00000000000', CURRENT_DATE)";
            
            try (PreparedStatement usuarioPstmt = conn.prepareStatement(insertUsuarioSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                usuarioPstmt.executeUpdate();
                
                ResultSet generatedKeys = usuarioPstmt.getGeneratedKeys();
                long usuarioId = 0;
                if (generatedKeys.next()) {
                    usuarioId = generatedKeys.getLong(1);
                }
                
                // Inserir login
                String insertLoginSql = "INSERT INTO login (fk_usuario, login, senha, ativo) " +
                                       "VALUES (?, 'admin', 'admin123', TRUE)";
                
                try (PreparedStatement loginPstmt = conn.prepareStatement(insertLoginSql)) {
                    loginPstmt.setLong(1, usuarioId);
                    loginPstmt.executeUpdate();
                }
                
                conn.commit();
                
                JOptionPane.showMessageDialog(null, 
                    "✅ Usuário ADMIN criado com sucesso!\n\n" +
                    "👤 Nome: Administrador\n" +
                    "🔐 Login: admin\n" +
                    "🔑 Senha: admin123\n" +
                    "✅ Status: Ativo\n\n" +
                    "💡 Agora você pode fazer login com admin/admin123", 
                    "➕ Criação de Usuário", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "❌ Erro ao criar usuário:\n\n" + e.getMessage(), 
                "➕ Criação de Usuário", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void testarLoginDireto() {
        try {
            Connection conn = DatabaseConfig.getConnection();
            String sql = "SELECT u.id, u.nome, l.login, 'ADMIN' as perfil FROM usuario u " +
                        "INNER JOIN login l ON l.fk_usuario = u.id " +
                        "WHERE l.login = ? AND l.senha = ? AND l.ativo = TRUE";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, "admin");
                pstmt.setString(2, "admin123");
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, 
                            "✅ Login direto bem-sucedido!\n\n" +
                            "👤 ID: " + rs.getInt("id") + "\n" +
                            "📝 Nome: " + rs.getString("nome") + "\n" +
                            "🔐 Login: " + rs.getString("login") + "\n" +
                            "🎯 Perfil: " + rs.getString("perfil") + "\n" +
                            "📊 Status: Autenticado", 
                            "⚡ Teste de Login Direto", 
                            JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, 
                            "❌ Falha no login direto!\n\n" +
                            "👤 Usuário: admin\n" +
                            "🔐 Senha: admin123\n" +
                            "📊 Status: Não encontrado no banco de dados\n\n" +
                            "💡 Use o botão 'Criar Usuário ADMIN' para criar o usuário.", 
                            "⚡ Teste de Login Direto", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "❌ Erro no teste de login direto:\n\n" + e.getMessage(), 
                "⚡ Teste de Login Direto", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
