package com.br.hermescomercial.dao;

import com.br.hermescomercial.connectionBD.ConnectionBD;
import com.br.hermescomercial.model.Usuario;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao {
    
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(UsuarioDao.class);
    // private ConnectionBD con = new ConnectionBD(); - não utilizado


    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("ID"));
        usuario.setNome(rs.getString("NOME"));
        usuario.setEndereco(rs.getString("ENDERECO"));
        usuario.setBairro(rs.getString("BAIRRO"));
        usuario.setCidade(rs.getString("CIDADE"));
        usuario.setEstado(rs.getString("ESTADO"));
        usuario.setCep(rs.getString("CEP"));
        usuario.setWhastsapp(rs.getString("WHATSAPP"));
        usuario.setTelefone(rs.getString("TELEFONE"));
        usuario.setEmail(rs.getString("EMAIL"));
        usuario.setTipoUsuario(rs.getString("TIPO_USUARIO"));
        
        // Para CPF e CNPJ, usar os campos diretos da tabela
        String cpf = rs.getString("CPF");
        String cnpj = rs.getString("CNPJ");
        if (cpf != null && !cpf.isEmpty()) {
            usuario.setNumeroDocumeto(cpf);
            usuario.setTipoDocumento("F"); // Pessoa Física
        } else if (cnpj != null && !cnpj.isEmpty()) {
            usuario.setNumeroDocumeto(cnpj);
            usuario.setTipoDocumento("J"); // Pessoa Jurídica
        }
        
        return usuario;
    }

    public void salvar(Usuario usuario) {
        // Usar campos corretos da tabela: CPF e CNPJ em vez de numeroDocumento
        String sql = "INSERT INTO USUARIO (nome, endereco, bairro, cidade, estado, cep, cpf, cnpj, whatsapp, telefone, email, tipo_usuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = ConnectionBD.getConnection().prepareStatement(sql)) {
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEndereco());
            ps.setString(3, usuario.getBairro());
            ps.setString(4, usuario.getCidade());
            ps.setString(5, usuario.getEstado());
            ps.setString(6, usuario.getCep());
            
            // Determinar se é CPF ou CNPJ baseado no tipo de documento
            if ("F".equals(usuario.getTipoDocumento())) {
                ps.setString(7, usuario.getNumeroDocumeto()); // CPF
                ps.setString(8, null); // CNPJ
            } else if ("J".equals(usuario.getTipoDocumento())) {
                ps.setString(7, null); // CPF
                ps.setString(8, usuario.getNumeroDocumeto()); // CNPJ
            } else {
                // Se não tiver tipo definido, tentar inferir pelo tamanho
                String numDoc = usuario.getNumeroDocumeto();
                if (numDoc != null && numDoc.length() == 11) {
                    ps.setString(7, numDoc); // CPF
                    ps.setString(8, null); // CNPJ
                } else if (numDoc != null && numDoc.length() == 14) {
                    ps.setString(7, null); // CPF
                    ps.setString(8, numDoc); // CNPJ
                } else {
                    ps.setString(7, numDoc); // CPF (padrão)
                    ps.setString(8, null); // CNPJ
                }
            }
            
            ps.setString(9, usuario.getWhastsapp());
            ps.setString(10, usuario.getTelefone());
            ps.setString(11, usuario.getEmail());
            ps.setString(12, usuario.getTipousuario());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao salvar usuario: " + e.getMessage());
        }
    }

    public void update(Usuario usuario) {
        // Usar campos corretos da tabela: CPF e CNPJ em vez de numeroDocumento
        String sql = "UPDATE USUARIO SET endereco=?, bairro=?, cidade=?, estado=?, cep=?, cpf=?, cnpj=?, whatsapp=?, telefone=?, email=?, tipo_usuario=? WHERE nome=?";
        try (PreparedStatement ps = ConnectionBD.getConnection().prepareStatement(sql)) {
            ps.setString(1, usuario.getEndereco());
            ps.setString(2, usuario.getBairro());
            ps.setString(3, usuario.getCidade());
            ps.setString(4, usuario.getEstado());
            ps.setString(5, usuario.getCep());
            
            // Determinar se é CPF ou CNPJ baseado no tipo de documento
            if ("F".equals(usuario.getTipoDocumento())) {
                ps.setString(6, usuario.getNumeroDocumeto()); // CPF
                ps.setString(7, null); // CNPJ
            } else if ("J".equals(usuario.getTipoDocumento())) {
                ps.setString(6, null); // CPF
                ps.setString(7, usuario.getNumeroDocumeto()); // CNPJ
            } else {
                // Se não tiver tipo definido, tentar inferir pelo tamanho
                String numDoc = usuario.getNumeroDocumeto();
                if (numDoc != null && numDoc.length() == 11) {
                    ps.setString(6, numDoc); // CPF
                    ps.setString(7, null); // CNPJ
                } else if (numDoc != null && numDoc.length() == 14) {
                    ps.setString(6, null); // CPF
                    ps.setString(7, numDoc); // CNPJ
                } else {
                    ps.setString(6, numDoc); // CPF (padrão)
                    ps.setString(7, null); // CNPJ
                }
            }
            
            ps.setString(8, usuario.getWhastsapp());
            ps.setString(9, usuario.getTelefone());
            ps.setString(10, usuario.getEmail());
            ps.setString(11, usuario.getTipousuario());
            ps.setString(12, usuario.getNome());
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao atualizar usuario: " + e.getMessage());
        }
    }

    public void remove(String nome) {
        String sql = "DELETE FROM USUARIO WHERE nome=?";
        try (PreparedStatement ps = ConnectionBD.getConnection().prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.executeUpdate();
        } catch (Exception e) {
            logger.error("Erro ao remover usuario: " + e.getMessage());
        }
    }

    public List<Usuario> buscarClientePorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            logger.warn("Busca por nome vazia ou nula");
            return new ArrayList<>();
        }
        
        String sql = "SELECT * FROM USUARIO WHERE TIPO_USUARIO = 'CLIENTE' AND NOME ILIKE ?";
        List<Usuario> lista = new ArrayList<>();
        
        try {
            logger.debug("Buscando clientes por nome: {}", nome);
            
            try (PreparedStatement ps = ConnectionBD.getConnection().prepareStatement(sql)) {
                String busca = "%" + nome + "%";
                ps.setString(1, busca);
                
                try (ResultSet rs = ps.executeQuery()) {
                    int count = 0;
                    while (rs.next()) {
                        count++;
                        Usuario usuario = mapResultSetToUsuario(rs);
                        logger.debug("Cliente encontrado: {} - ID: {}, Nome: {}", count, usuario.getId(), usuario.getNome());
                        lista.add(usuario);
                    }
                    logger.info("Total de clientes encontrados por nome '{}': {}", nome, count);
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao buscar clientes por nome '{}': {}", nome, e.getMessage(), e);
            throw new RuntimeException("Não foi possível buscar clientes por nome", e);
        }
        return lista;
    }

    public List<Usuario> buscarClientePorCpfCnpj(String textoBusca) {
        if (textoBusca == null || textoBusca.trim().isEmpty()) {
            logger.warn("Busca por CPF/CNPJ vazia ou nula");
            return new ArrayList<>();
        }
        
        String sql = "SELECT * FROM USUARIO WHERE TIPO_USUARIO = 'CLIENTE' AND (CPF ILIKE ? OR CNPJ ILIKE ?)";
        List<Usuario> lista = new ArrayList<>();
        
        try {
            logger.debug("Buscando clientes por CPF/CNPJ: {}", textoBusca);
            
            try (PreparedStatement ps = ConnectionBD.getConnection().prepareStatement(sql)) {
                String busca = "%" + textoBusca + "%";
                ps.setString(1, busca);
                ps.setString(2, busca);
                
                try (ResultSet rs = ps.executeQuery()) {
                    int count = 0;
                    while (rs.next()) {
                        count++;
                        Usuario usuario = mapResultSetToUsuario(rs);
                        logger.debug("Cliente encontrado: {} - ID: {}, Nome: {}, CPF/CNPJ: {}", count, usuario.getId(), usuario.getNome(), usuario.getNumeroDocumeto());
                        lista.add(usuario);
                    }
                    logger.info("Total de clientes encontrados por CPF/CNPJ '{}': {}", textoBusca, count);
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao buscar clientes por CPF/CNPJ '{}': {}", textoBusca, e.getMessage(), e);
            throw new RuntimeException("Não foi possível buscar clientes por CPF/CNPJ", e);
        }
        return lista;
    }

    public List<Usuario> buscarClientePorNomeCpfCnpj(String textoBusca) {
        if (textoBusca == null || textoBusca.trim().isEmpty()) {
            logger.warn("Busca unificada vazia ou nula");
            return new ArrayList<>();
        }
        
        String sql = "SELECT * FROM USUARIO WHERE TIPO_USUARIO = 'CLIENTE' AND (NOME ILIKE ? OR CPF ILIKE ? OR CNPJ ILIKE ?)";
        List<Usuario> lista = new ArrayList<>();
        
        try {
            logger.debug("Buscando clientes por Nome/CPF/CNPJ: {}", textoBusca);
            
            try (PreparedStatement ps = ConnectionBD.getConnection().prepareStatement(sql)) {
                String busca = "%" + textoBusca + "%";
                ps.setString(1, busca);
                ps.setString(2, busca);
                ps.setString(3, busca);
                
                try (ResultSet rs = ps.executeQuery()) {
                    int count = 0;
                    while (rs.next()) {
                        count++;
                        Usuario usuario = mapResultSetToUsuario(rs);
                        logger.debug("Cliente encontrado: {} - ID: {}, Nome: {}, CPF/CNPJ: {}", count, usuario.getId(), usuario.getNome(), usuario.getNumeroDocumeto());
                        lista.add(usuario);
                    }
                    logger.info("Total de clientes encontrados por Nome/CPF/CNPJ '{}': {}", textoBusca, count);
                }
            }
        } catch (SQLException e) {
            logger.error("Erro ao buscar clientes por Nome/CPF/CNPJ '{}': {}", textoBusca, e.getMessage(), e);
            throw new RuntimeException("Não foi possível buscar clientes por Nome/CPF/CNPJ", e);
        }
        return lista;
    }

    public List<Usuario> buscarTodos() {
        String sql = "SELECT * FROM USUARIO";
        List<Usuario> lista = new ArrayList<>();
        try (PreparedStatement ps = ConnectionBD.getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapResultSetToUsuario(rs));
            }
        } catch (Exception e) {
            logger.error("Erro ao listar usuarios: " + e.getMessage());
        }
        return lista;
    }

    public List<Usuario> listar() {
        return buscarTodos();
    }
}
