package com.br.hermescomercial.repository.impl;

import com.br.hermescomercial.repository.ProdutoRepository;
import com.br.hermescomercial.model.Produto;
import com.br.hermescomercial.service.DatabaseService;
import com.br.hermescomercial.event.EventSystem;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementação do Repository Pattern para Produto
 * Centraliza e abstrai todas as operações de acesso a dados de produtos
 * Versão 1.0.0 - Repository Pattern Implementation
 */
public class ProdutoRepositoryImpl implements ProdutoRepository {
    
    private final DatabaseService databaseService;
    private final EventSystem eventSystem;
    
    public ProdutoRepositoryImpl() {
        this.databaseService = DatabaseService.getInstance();
        this.eventSystem = EventSystem.getInstance();
    }
    
    @Override
    public boolean salvar(Produto produto) {
        String sql = "INSERT INTO produtos (nome, descricao, preco_custo, preco_venda, estoque, " +
                    "estoque_minimo, categoria, unidade, codigo_barras, ativo) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setBigDecimal(3, produto.getPrecoCusto());
            stmt.setBigDecimal(4, produto.getPrecoVenda());
            stmt.setInt(5, produto.getEstoque());
            stmt.setInt(6, produto.getEstoqueMinimo());
            stmt.setString(7, produto.getCategoria());
            stmt.setString(8, produto.getUnidade());
            stmt.setString(9, produto.getCodigoBarras());
            stmt.setBoolean(10, produto.isAtivo());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        produto.setId(generatedKeys.getLong(1));
                    }
                }
                
                // Publicar evento de produto cadastrado
                eventSystem.publish(new EventSystem.Event(
                    EventSystem.EventTypes.PRODUTO_CADASTRADO, 
                    produto, 
                    "ProdutoRepository"
                ));
                
                System.out.println("✅ ProdutoRepository: Produto salvo com sucesso - ID: " + produto.getId());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ ProdutoRepository: Erro ao salvar produto: " + e.getMessage());
        }
        
        return false;
    }
    
    @Override
    public boolean atualizar(Produto produto) {
        String sql = "UPDATE produtos SET nome = ?, descricao = ?, preco_custo = ?, " +
                    "preco_venda = ?, estoque = ?, estoque_minimo = ?, categoria = ?, " +
                    "unidade = ?, codigo_barras = ?, ativo = ? WHERE id = ?";
        
        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setBigDecimal(3, produto.getPrecoCusto());
            stmt.setBigDecimal(4, produto.getPrecoVenda());
            stmt.setInt(5, produto.getEstoque());
            stmt.setInt(6, produto.getEstoqueMinimo());
            stmt.setString(7, produto.getCategoria());
            stmt.setString(8, produto.getUnidade());
            stmt.setString(9, produto.getCodigoBarras());
            stmt.setBoolean(10, produto.isAtivo());
            stmt.setLong(11, produto.getId());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                // Publicar evento de produto atualizado
                eventSystem.publish(new EventSystem.Event(
                    EventSystem.EventTypes.PRODUTO_ATUALIZADO, 
                    produto, 
                    "ProdutoRepository"
                ));
                
                System.out.println("✅ ProdutoRepository: Produto atualizado com sucesso - ID: " + produto.getId());
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ ProdutoRepository: Erro ao atualizar produto: " + e.getMessage());
        }
        
        return false;
    }
    
    @Override
    public boolean remover(Long id) {
        String sql = "DELETE FROM produtos WHERE id = ?";
        
        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            Optional<Produto> produto = buscarPorId(id);
            if (produto.isPresent()) {
                int affectedRows = stmt.executeUpdate();
                
                if (affectedRows > 0) {
                    // Publicar evento de produto removido
                    eventSystem.publish(new EventSystem.Event(
                        EventSystem.EventTypes.PRODUTO_REMOVIDO, 
                        produto.get(), 
                        "ProdutoRepository"
                    ));
                    
                    System.out.println("✅ ProdutoRepository: Produto removido com sucesso - ID: " + id);
                    return true;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ ProdutoRepository: Erro ao remover produto: " + e.getMessage());
        }
        
        return false;
    }
    
    @Override
    public Optional<Produto> buscarPorId(Long id) {
        String sql = "SELECT * FROM produtos WHERE id = ?";
        
        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Produto produto = mapResultSetToProduto(rs);
                    System.out.println("🔍 ProdutoRepository: Produto encontrado - ID: " + id);
                    return Optional.of(produto);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ ProdutoRepository: Erro ao buscar produto por ID: " + e.getMessage());
        }
        
        System.out.println("🔍 ProdutoRepository: Produto não encontrado - ID: " + id);
        return Optional.empty();
    }
    
    @Override
    public List<Produto> buscarTodos() {
        String sql = "SELECT * FROM produtos ORDER BY nome";
        List<Produto> produtos = new ArrayList<>();
        
        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                produtos.add(mapResultSetToProduto(rs));
            }
            
            System.out.println("🔍 ProdutoRepository: " + produtos.size() + " produtos encontrados");
            
        } catch (SQLException e) {
            System.err.println("❌ ProdutoRepository: Erro ao buscar todos os produtos: " + e.getMessage());
        }
        
        return produtos;
    }
    
    @Override
    public List<Produto> buscarPorNome(String nome) {
        String sql = "SELECT * FROM produtos WHERE nome ILIKE ? ORDER BY nome";
        List<Produto> produtos = new ArrayList<>();
        
        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nome + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    produtos.add(mapResultSetToProduto(rs));
                }
            }
            
            System.out.println("🔍 ProdutoRepository: " + produtos.size() + " produtos encontrados para nome: " + nome);
            
        } catch (SQLException e) {
            System.err.println("❌ ProdutoRepository: Erro ao buscar produtos por nome: " + e.getMessage());
        }
        
        return produtos;
    }
    
    @Override
    public List<Produto> buscarPorCategoria(String categoria) {
        String sql = "SELECT * FROM produtos WHERE categoria = ? ORDER BY nome";
        List<Produto> produtos = new ArrayList<>();
        
        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, categoria);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    produtos.add(mapResultSetToProduto(rs));
                }
            }
            
            System.out.println("🔍 ProdutoRepository: " + produtos.size() + " produtos encontrados na categoria: " + categoria);
            
        } catch (SQLException e) {
            System.err.println("❌ ProdutoRepository: Erro ao buscar produtos por categoria: " + e.getMessage());
        }
        
        return produtos;
    }
    
    @Override
    public List<Produto> buscarEstoqueBaixo() {
        String sql = "SELECT * FROM produtos WHERE estoque <= estoque_minimo AND ativo = true ORDER BY nome";
        List<Produto> produtos = new ArrayList<>();
        
        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Produto produto = mapResultSetToProduto(rs);
                produtos.add(produto);
                
                // Publicar evento de estoque baixo
                eventSystem.publish(new EventSystem.Event(
                    EventSystem.EventTypes.ESTOQUE_BAIXO, 
                    produto, 
                    "ProdutoRepository"
                ));
            }
            
            System.out.println("🔍 ProdutoRepository: " + produtos.size() + " produtos com estoque baixo");
            
        } catch (SQLException e) {
            System.err.println("❌ ProdutoRepository: Erro ao buscar produtos com estoque baixo: " + e.getMessage());
        }
        
        return produtos;
    }
    
    @Override
    public boolean atualizarEstoque(Long id, int novaQuantidade) {
        String sql = "UPDATE produtos SET estoque = ? WHERE id = ?";
        
        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, novaQuantidade);
            stmt.setLong(2, id);
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                Optional<Produto> produto = buscarPorId(id);
                if (produto.isPresent()) {
                    // Publicar evento de estoque atualizado
                    eventSystem.publish(new EventSystem.Event(
                        EventSystem.EventTypes.ESTOQUE_ATUALIZADO, 
                        produto.get(), 
                        "ProdutoRepository"
                    ));
                    
                    // Verificar se está zerado
                    if (novaQuantidade == 0) {
                        eventSystem.publish(new EventSystem.Event(
                            EventSystem.EventTypes.ESTOQUE_ZERADO, 
                            produto.get(), 
                            "ProdutoRepository"
                        ));
                    }
                }
                
                System.out.println("✅ ProdutoRepository: Estoque atualizado - ID: " + id + ", Nova quantidade: " + novaQuantidade);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ ProdutoRepository: Erro ao atualizar estoque: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Mapeia ResultSet para objeto Produto
     * @param rs ResultSet com dados do produto
     * @return Objeto Produto
     * @throws SQLException Erro no acesso aos dados
     */
    private Produto mapResultSetToProduto(ResultSet rs) throws SQLException {
        Produto produto = new Produto();
        produto.setId(rs.getLong("id"));
        produto.setNome(rs.getString("nome"));
        produto.setDescricao(rs.getString("descricao"));
        produto.setPrecoCusto(rs.getBigDecimal("preco_custo"));
        produto.setPrecoVenda(rs.getBigDecimal("preco_venda"));
        produto.setEstoque(rs.getInt("estoque"));
        produto.setEstoqueMinimo(rs.getInt("estoque_minimo"));
        produto.setCategoria(rs.getString("categoria"));
        produto.setUnidade(rs.getString("unidade"));
        produto.setCodigoBarras(rs.getString("codigo_barras"));
        produto.setAtivo(rs.getBoolean("ativo"));
        produto.setDataCriacaoTimestamp(rs.getTimestamp("data_criacao"));
        produto.setDataAtualizacaoTimestamp(rs.getTimestamp("data_atualizacao"));
        return produto;
    }
    
    @Override
    public Produto buscarPorCodigoBarras(String codigoBarras) {
        String sql = "SELECT * FROM produtos WHERE codigo_barras = ?";
        
        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codigoBarras);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduto(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ ProdutoRepository: Erro ao buscar por código de barras: " + e.getMessage());
        }
        
        return null;
    }
    
    @Override
    public List<Produto> listar() {
        String sql = "SELECT * FROM produtos ORDER BY nome";
        List<Produto> produtos = new ArrayList<>();
        
        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                produtos.add(mapResultSetToProduto(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("❌ ProdutoRepository: Erro ao listar produtos: " + e.getMessage());
        }
        
        return produtos;
    }
    
    @Override
    public boolean excluir(String nome) {
        String sql = "DELETE FROM produtos WHERE nome = ?";
        
        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nome);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Publicar evento de exclusão
                eventSystem.publish(new EventSystem.Event(
                    EventSystem.EventTypes.PRODUTO_REMOVIDO,
                    nome,
                    "ProdutoRepository"
                ));
                
                System.out.println("✅ ProdutoRepository: Produto excluído - Nome: " + nome);
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("❌ ProdutoRepository: Erro ao excluir produto: " + e.getMessage());
        }
        
        return false;
    }
    
    @Override
    public List<Produto> buscarPorFaixaPreco(BigDecimal precoMin, BigDecimal precoMax) {
        String sql = "SELECT * FROM produtos WHERE preco_venda BETWEEN ? AND ? AND ativo = true ORDER BY preco_venda";
        
        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setBigDecimal(1, precoMin);
            stmt.setBigDecimal(2, precoMax);
            
            ResultSet rs = stmt.executeQuery();
            List<Produto> produtos = new ArrayList<>();
            
            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getLong("id"));
                produto.setNome(rs.getString("nome"));
                produto.setDescricao(rs.getString("descricao"));
                produto.setPrecoCusto(rs.getBigDecimal("preco_custo"));
                produto.setPrecoVenda(rs.getBigDecimal("preco_venda"));
                produto.setEstoque(rs.getInt("estoque"));
                produto.setEstoqueMinimo(rs.getInt("estoque_minimo"));
                produto.setCategoria(rs.getString("categoria"));
                produto.setUnidade(rs.getString("unidade"));
                produto.setCodigoBarras(rs.getString("codigo_barras"));
                produto.setAtivo(rs.getBoolean("ativo"));
                produtos.add(produto);
            }
            
            return produtos;
            
        } catch (SQLException e) {
            System.err.println("❌ ProdutoRepository: Erro ao buscar por faixa de preço: " + e.getMessage());
            return List.of();
        }
    }
    
    @Override
    public List<Produto> buscarComEstoqueBaixo(int limite) {
        String sql = "SELECT * FROM produtos WHERE estoque < ? AND ativo = true ORDER BY estoque ASC";
        
        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limite);
            
            ResultSet rs = stmt.executeQuery();
            List<Produto> produtos = new ArrayList<>();
            
            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getLong("id"));
                produto.setNome(rs.getString("nome"));
                produto.setDescricao(rs.getString("descricao"));
                produto.setPrecoCusto(rs.getBigDecimal("preco_custo"));
                produto.setPrecoVenda(rs.getBigDecimal("preco_venda"));
                produto.setEstoque(rs.getInt("estoque"));
                produto.setEstoqueMinimo(rs.getInt("estoque_minimo"));
                produto.setCategoria(rs.getString("categoria"));
                produto.setUnidade(rs.getString("unidade"));
                produto.setCodigoBarras(rs.getString("codigo_barras"));
                produto.setAtivo(rs.getBoolean("ativo"));
                produtos.add(produto);
            }
            
            return produtos;
            
        } catch (SQLException e) {
            System.err.println("❌ ProdutoRepository: Erro ao buscar produtos com estoque baixo: " + e.getMessage());
            return List.of();
        }
    }
    
    @Override
    public List<Produto> buscarPorCodigo(String codigo) {
        String sql = "SELECT * FROM produtos WHERE codigo_barras = ? AND ativo = true ORDER BY nome";
        
        try (Connection conn = databaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codigo);
            
            ResultSet rs = stmt.executeQuery();
            List<Produto> produtos = new ArrayList<>();
            
            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getLong("id"));
                produto.setNome(rs.getString("nome"));
                produto.setDescricao(rs.getString("descricao"));
                produto.setPrecoCusto(rs.getBigDecimal("preco_custo"));
                produto.setPrecoVenda(rs.getBigDecimal("preco_venda"));
                produto.setEstoque(rs.getInt("estoque"));
                produto.setEstoqueMinimo(rs.getInt("estoque_minimo"));
                produto.setCategoria(rs.getString("categoria"));
                produto.setUnidade(rs.getString("unidade"));
                produto.setCodigoBarras(rs.getString("codigo_barras"));
                produto.setAtivo(rs.getBoolean("ativo"));
                produtos.add(produto);
            }
            
            return produtos;
            
        } catch (SQLException e) {
            System.err.println("❌ ProdutoRepository: Erro ao buscar por código: " + e.getMessage());
            return List.of();
        }
    }
    
    @Override
    public List<Produto> buscarComFiltros(String nome, String categoria, String subCategoria, 
                                          String codigoBarras, BigDecimal precoMin, BigDecimal precoMax, 
                                          Integer estoqueMin, boolean ativos, boolean inativos) {
        // Implementação básica - retorna lista vazia por enquanto
        return List.of();
    }
}
