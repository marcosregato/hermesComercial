package com.br.hermescomercial.service.impl;

import com.br.hermescomercial.repository.ProdutoRepository;
import com.br.hermescomercial.repository.impl.ProdutoRepositoryImpl;
import com.br.hermescomercial.model.Produto;
import com.br.hermescomercial.event.EventSystem;
import com.br.hermescomercial.shared.validator.ProdutoValidator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Implementação do Service Layer Pattern para Produto
 * Centraliza a lógica de negócio e regras para produtos
 * Versão 1.0.0 - Service Layer Pattern Implementation
 */
public class ProdutoServiceImpl {
    
    private static final Logger LOGGER = Logger.getLogger(ProdutoServiceImpl.class.getName());
    
    private final ProdutoRepository produtoRepository;
    private final EventSystem eventSystem;
    private final ProdutoValidator validator;
    
    public ProdutoServiceImpl() {
        this.produtoRepository = new ProdutoRepositoryImpl();
        this.eventSystem = EventSystem.getInstance();
        this.validator = new ProdutoValidator();
    }
    
    public Optional<Produto> salvar(Produto produto) throws BusinessException {
        LOGGER.info("🔄 ProdutoService: Iniciando salvamento do produto: " + produto.getNome());
        
        try {
            // Validações de negócio
            validator.validarProduto(produto);
            validator.validarNomeUnico(produto.getNome(), produto.getId());
            validator.validarCodigoBarrasUnico(produto.getCodigoBarras(), produto.getId());
            validator.validarPreco(produto.getPrecoCusto(), produto.getPrecoVenda());
            validator.validarEstoque(produto.getEstoque(), produto.getEstoqueMinimo());
            
            // Regra de negócio: margem de lucro mínima
            BigDecimal margemLucro = calcularMargemLucro(produto);
            if (margemLucro.compareTo(new BigDecimal("0.10")) < 0) {
                throw new BusinessException("Margem de lucro não pode ser inferior a 10%");
            }
            
            // Salvar no repositório
            boolean salvo = produtoRepository.salvar(produto);
            
            if (salvo) {
                LOGGER.info("✅ ProdutoService: Produto salvo com sucesso - ID: " + produto.getId());
                
                // Publicar evento de negócio
                eventSystem.publish(new EventSystem.Event(
                    "produto.servico.salvo", 
                    produto, 
                    "ProdutoService"
                ));
                
                return Optional.of(produto);
            } else {
                throw new BusinessException("Falha ao salvar produto no banco de dados");
            }
            
        } catch (BusinessException e) {
            LOGGER.log(Level.WARNING, "❌ ProdutoService: Erro de negócio ao salvar produto: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "❌ ProdutoService: Erro inesperado ao salvar produto", e);
            throw new BusinessException("Erro inesperado ao salvar produto", e);
        }
    }
    
    public Optional<Produto> atualizar(Produto produto) throws BusinessException {
        LOGGER.info("🔄 ProdutoService: Iniciando atualização do produto: " + produto.getNome());
        
        try {
            // Verificar se produto existe
            Optional<Produto> existente = produtoRepository.buscarPorId(produto.getId());
            if (!existente.isPresent()) {
                throw new BusinessException("Produto não encontrado para atualização");
            }
            
            // Validações de negócio
            validator.validarProduto(produto);
            validator.validarNomeUnico(produto.getNome(), produto.getId());
            validator.validarCodigoBarrasUnico(produto.getCodigoBarras(), produto.getId());
            validator.validarPreco(produto.getPrecoCusto(), produto.getPrecoVenda());
            validator.validarEstoque(produto.getEstoque(), produto.getEstoqueMinimo());
            
            // Regra de negócio: margem de lucro mínima
            BigDecimal margemLucro = calcularMargemLucro(produto);
            if (margemLucro.compareTo(new BigDecimal("0.10")) < 0) {
                throw new BusinessException("Margem de lucro não pode ser inferior a 10%");
            }
            
            // Atualizar no repositório
            boolean atualizado = produtoRepository.atualizar(produto);
            
            if (atualizado) {
                LOGGER.info("✅ ProdutoService: Produto atualizado com sucesso - ID: " + produto.getId());
                
                // Publicar evento de negócio
                eventSystem.publish(new EventSystem.Event(
                    "produto.servico.atualizado", 
                    produto, 
                    "ProdutoService"
                ));
                
                return Optional.of(produto);
            } else {
                throw new BusinessException("Falha ao atualizar produto no banco de dados");
            }
            
        } catch (BusinessException e) {
            LOGGER.log(Level.WARNING, "❌ ProdutoService: Erro de negócio ao atualizar produto: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "❌ ProdutoService: Erro inesperado ao atualizar produto", e);
            throw new BusinessException("Erro inesperado ao atualizar produto", e);
        }
    }
    
    public boolean remover(Long id) throws BusinessException {
        LOGGER.info("🔄 ProdutoService: Iniciando remoção do produto - ID: " + id);
        
        try {
            Optional<Produto> produto = produtoRepository.buscarPorId(id);
            if (!produto.isPresent()) {
                throw new BusinessException("Produto não encontrado para remoção");
            }
            
            // Regra de negócio: não pode remover produto com estoque
            if (produto.get().getEstoque() > 0) {
                throw new BusinessException("Não é possível remover produto com estoque maior que zero");
            }
            
            // Verificar se há vendas associadas (simulação)
            if (possuiVendasAssociadas(id)) {
                throw new BusinessException("Não é possível remover produto com vendas associadas");
            }
            
            boolean removido = produtoRepository.remover(id);
            
            if (removido) {
                LOGGER.info("✅ ProdutoService: Produto removido com sucesso - ID: " + id);
                
                // Publicar evento de negócio
                eventSystem.publish(new EventSystem.Event(
                    "produto.servico.removido", 
                    id, 
                    "ProdutoService"
                ));
                
                return true;
            } else {
                throw new BusinessException("Falha ao remover produto no banco de dados");
            }
            
        } catch (BusinessException e) {
            LOGGER.log(Level.WARNING, "❌ ProdutoService: Erro de negócio ao remover produto: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "❌ ProdutoService: Erro inesperado ao remover produto", e);
            throw new BusinessException("Erro inesperado ao remover produto", e);
        }
    }
    
    public Optional<Produto> buscarPorId(Long id) {
        LOGGER.info("🔍 ProdutoService: Buscando produto por ID: " + id);
        
        try {
            Optional<Produto> produto = produtoRepository.buscarPorId(id);
            
            if (produto.isPresent()) {
                LOGGER.info("✅ ProdutoService: Produto encontrado - ID: " + id);
            } else {
                LOGGER.info("🔍 ProdutoService: Produto não encontrado - ID: " + id);
            }
            
            return produto;
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "❌ ProdutoService: Erro ao buscar produto por ID", e);
            return Optional.empty();
        }
    }
    
    public List<Produto> buscarTodos() {
        LOGGER.info("🔍 ProdutoService: Buscando todos os produtos");
        
        try {
            List<Produto> produtos = produtoRepository.buscarTodos();
            LOGGER.info("✅ ProdutoService: " + produtos.size() + " produtos encontrados");
            return produtos;
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "❌ ProdutoService: Erro ao buscar todos os produtos", e);
            return List.of();
        }
    }
    
    public List<Produto> buscarPorNome(String nome) {
        LOGGER.info("🔍 ProdutoService: Buscando produtos por nome: " + nome);
        
        try {
            List<Produto> produtos = produtoRepository.buscarPorNome(nome);
            LOGGER.info("✅ ProdutoService: " + produtos.size() + " produtos encontrados");
            return produtos;
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "❌ ProdutoService: Erro ao buscar produtos por nome", e);
            return List.of();
        }
    }
    
    public List<Produto> buscarPorCategoria(String categoria) {
        LOGGER.info("🔍 ProdutoService: Buscando produtos por categoria: " + categoria);
        
        try {
            List<Produto> produtos = produtoRepository.buscarPorCategoria(categoria);
            LOGGER.info("✅ ProdutoService: " + produtos.size() + " produtos encontrados");
            return produtos;
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "❌ ProdutoService: Erro ao buscar produtos por categoria", e);
            return List.of();
        }
    }
    
    public List<Produto> buscarEstoqueBaixo() {
        LOGGER.info("🔍 ProdutoService: Buscando produtos com estoque baixo");
        
        try {
            List<Produto> produtos = produtoRepository.buscarEstoqueBaixo();
            LOGGER.info("✅ ProdutoService: " + produtos.size() + " produtos com estoque baixo");
            
            // Publicar evento de negócio para notificações
            if (!produtos.isEmpty()) {
                eventSystem.publish(new EventSystem.Event(
                    "produto.servico.estoque.baixo", 
                    produtos, 
                    "ProdutoService"
                ));
            }
            
            return produtos;
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "❌ ProdutoService: Erro ao buscar produtos com estoque baixo", e);
            return List.of();
        }
    }
    
    public boolean atualizarEstoque(Long id, int novaQuantidade) throws BusinessException {
        LOGGER.info("🔄 ProdutoService: Atualizando estoque - ID: " + id + ", Nova quantidade: " + novaQuantidade);
        
        try {
            Optional<Produto> produto = produtoRepository.buscarPorId(id);
            if (!produto.isPresent()) {
                throw new BusinessException("Produto não encontrado");
            }
            
            // Regra de negócio: não permite estoque negativo
            if (novaQuantidade < 0) {
                throw new BusinessException("Estoque não pode ser negativo");
            }
            
            boolean atualizado = produtoRepository.atualizarEstoque(id, novaQuantidade);
            
            if (atualizado) {
                LOGGER.info("✅ ProdutoService: Estoque atualizado com sucesso");
                
                // Publicar evento de negócio
                Map<String, Object> dados = new HashMap<>();
                dados.put("id", id);
                dados.put("novaQuantidade", novaQuantidade);
                eventSystem.publish(new EventSystem.Event(
                    "produto.servico.estoque.atualizado", 
                    dados, 
                    "ProdutoService"
                ));
                
                return true;
            } else {
                throw new BusinessException("Falha ao atualizar estoque");
            }
            
        } catch (BusinessException e) {
            LOGGER.log(Level.WARNING, "❌ ProdutoService: Erro de negócio ao atualizar estoque: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "❌ ProdutoService: Erro inesperado ao atualizar estoque", e);
            throw new BusinessException("Erro inesperado ao atualizar estoque", e);
        }
    }
    
    public BigDecimal calcularValorTotalEstoque() {
        LOGGER.info("🔄 ProdutoService: Calculando valor total do estoque");
        
        try {
            List<Produto> produtos = produtoRepository.buscarTodos();
            BigDecimal valorTotal = BigDecimal.ZERO;
            
            for (Produto produto : produtos) {
                BigDecimal valorProduto = produto.getPrecoVenda().multiply(new BigDecimal(produto.getEstoque()));
                valorTotal = valorTotal.add(valorProduto);
            }
            
            LOGGER.info("✅ ProdutoService: Valor total do estoque calculado: R$ " + valorTotal);
            return valorTotal;
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "❌ ProdutoService: Erro ao calcular valor total do estoque", e);
            return BigDecimal.ZERO;
        }
    }
    
    /**
     * Calcula margem de lucro de um produto
     * @param produto Produto para cálculo
     * @return Margem de lucro (0 a 1)
     */
    private BigDecimal calcularMargemLucro(Produto produto) {
        BigDecimal custo = produto.getPrecoCusto();
        BigDecimal venda = produto.getPrecoVenda();
        
        if (custo.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        
        return venda.subtract(custo).divide(venda, 4, RoundingMode.HALF_UP);
    }
    
    /**
     * Verifica se há vendas associadas ao produto (simulação)
     * @param produtoId ID do produto
     * @return true se há vendas associadas
     */
    private boolean possuiVendasAssociadas(Long produtoId) {
        // Simulação - na implementação real, consultaria a tabela de vendas
        return false;
    }
    
    /**
     * Exceção de negócio para ProdutoService
     */
    public static class BusinessException extends Exception {
        public BusinessException(String message) {
            super(message);
        }
        
        public BusinessException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
