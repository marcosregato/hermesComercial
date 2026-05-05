package com.br.hermescomercial.service;

import com.br.hermescomercial.model.Produto;
import com.br.hermescomercial.repository.ProdutoRepository;
import com.br.hermescomercial.exception.BusinessException;
import com.br.hermescomercial.exception.ValidationException;
import com.br.hermescomercial.validation.Validator;
import com.br.hermescomercial.injection.DependencyContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.List;

/**
 * Serviço refatorado para gerenciamento de produtos
 * Implementa injeção de dependências e validações adequadas
 * @author marcos
 */
public class ProdutoService {
    
    private static final Logger logger = LogManager.getLogger(ProdutoService.class);
    private final ProdutoRepository produtoRepository;
    
    /**
     * Construtor com injeção de dependência
     * @param produtoRepository Repository de produtos
     */
    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
        logger.info("ProdutoServiceRefactored inicializado com repository injetado");
    }
    
    /**
     * Construtor padrão usando container de dependências
     */
    public ProdutoService() {
        this(DependencyContainer.getInstance().get(ProdutoRepository.class));
    }
    
    /**
     * Salva um novo produto com validação
     * @param produto Produto a ser salvo
     * @return true se salvo com sucesso
     * @throws ValidationException Se o produto for inválido
     * @throws BusinessException Se ocorrer erro de negócio
     */
    public boolean salvar(Produto produto) {
        try {
            logger.debug("Iniciando salvamento do produto: {}", produto != null ? produto.getNome() : "null");
            
            // Validação do produto
            Validator.validarProduto(produto);
            
            // Verificar se produto já existe (mesmo código ou código de barras)
            if (produto.getCodigo() != null && !produto.getCodigo().trim().isEmpty()) {
                List<Produto> existentes = listar();
                for (Produto existente : existentes) {
                    // Para novos produtos (ID null), apenas verificar duplicação de código
                    if (produto.getId() == null) {
                        if (produto.getCodigo().equals(existente.getCodigo())) {
                            throw new BusinessException("Já existe um produto cadastrado com este código");
                        }
                    } else {
                        // Para produtos existentes, verificar duplicação exceto ele mesmo
                        if (produto.getCodigo().equals(existente.getCodigo()) && 
                            !existente.getId().equals(produto.getId())) {
                            throw new BusinessException("Já existe um produto cadastrado com este código");
                        }
                    }
                }
            }
            
            boolean resultado = produtoRepository.salvar(produto);
            
            if (resultado) {
                logger.info("Produto salvo com sucesso: {}", produto.getNome());
            } else {
                logger.warn("Falha ao salvar produto: {}", produto.getNome());
            }
            
            return resultado;
            
        } catch (ValidationException e) {
            logger.warn("Validação falhou ao salvar produto: {}", e.getMessage());
            throw e;
        } catch (BusinessException e) {
            logger.warn("Erro de negócio ao salvar produto: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao salvar produto: {}", e.getMessage(), e);
            throw new BusinessException("Não foi possível salvar o produto", e);
        }
    }
    
    /**
     * Atualiza um produto existente com validação
     * @param produto Produto a ser atualizado
     * @return true se atualizado com sucesso
     */
    public boolean atualizar(Produto produto) {
        try {
            logger.debug("Iniciando atualização do produto: {}", produto != null ? produto.getNome() : "null");
            
            if (produto == null || produto.getId() == null) {
                throw new ValidationException("Produto e ID são obrigatórios para atualização");
            }
            
            // Validação do produto
            Validator.validarProduto(produto);
            
            // Verificar se produto existe
            java.util.Optional<Produto> existente = produtoRepository.buscarPorId(produto.getId());
            if (existente.isEmpty()) {
                throw new BusinessException("Produto não encontrado para atualização");
            }
            
            boolean resultado = produtoRepository.atualizar(produto);
            
            if (resultado) {
                logger.info("Produto atualizado com sucesso: {}", produto.getNome());
            } else {
                logger.warn("Falha ao atualizar produto: {}", produto.getNome());
            }
            
            return resultado;
            
        } catch (ValidationException e) {
            logger.warn("Validação falhou ao atualizar produto: {}", e.getMessage());
            throw e;
        } catch (BusinessException e) {
            logger.warn("Erro de negócio ao atualizar produto: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao atualizar produto: {}", e.getMessage(), e);
            throw new BusinessException("Não foi possível atualizar o produto", e);
        }
    }
    
    /**
     * Exclui um produto pelo ID
     * @param id ID do produto
     * @return true se excluído com sucesso
     */
    public boolean excluir(Long id) {
        try {
            logger.debug("Iniciando exclusão do produto ID: {}", id);
            
            if (id == null) {
                throw new ValidationException("ID do produto é obrigatório para exclusão");
            }
            
            // Verificar se produto existe
            java.util.Optional<Produto> produtoOpt = produtoRepository.buscarPorId(id);
            if (produtoOpt.isEmpty()) {
                throw new BusinessException("Produto não encontrado para exclusão");
            }
            
            Produto produto = produtoOpt.get();
            boolean resultado = produtoRepository.remover(id);
            
            if (resultado) {
                logger.info("Produto excluído com sucesso: {} (ID: {})", produto.getNome(), id);
            } else {
                logger.warn("Falha ao excluir produto ID: {}", id);
            }
            
            return resultado;
            
        } catch (ValidationException e) {
            logger.warn("Validação falhou ao excluir produto: {}", e.getMessage());
            throw e;
        } catch (BusinessException e) {
            logger.warn("Erro de negócio ao excluir produto: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao excluir produto ID {}: {}", id, e.getMessage(), e);
            throw new BusinessException("Não foi possível excluir o produto", e);
        }
    }
    
    /**
     * Exclui um produto pelo nome
     * @param nome Nome do produto
     * @return true se excluído com sucesso
     */
    public boolean excluir(String nome) {
        try {
            logger.debug("Iniciando exclusão do produto por nome: {}", nome);
            
            if (nome == null || nome.trim().isEmpty()) {
                throw new ValidationException("Nome do produto é obrigatório para exclusão");
            }
            
            boolean resultado = produtoRepository.excluir(nome);
            
            if (resultado) {
                logger.info("Produto excluído com sucesso: {}", nome);
            } else {
                logger.warn("Falha ao excluir produto: {}", nome);
            }
            
            return resultado;
            
        } catch (ValidationException e) {
            logger.warn("Validação falhou ao excluir produto: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao excluir produto '{}': {}", nome, e.getMessage(), e);
            throw new BusinessException("Não foi possível excluir o produto", e);
        }
    }
    
    /**
     * Busca produtos com filtros avançados
     * @param nome Nome do produto
     * @param categoria Categoria do produto
     * @param precoMin Preço mínimo
     * @param precoMax Preço máximo
     * @return Lista de produtos encontrados
     */
    public List<Produto> buscarComFiltros(String nome, String categoria, BigDecimal precoMin, BigDecimal precoMax) {
        try {
            logger.debug("Buscando produtos com filtros - nome: {}, categoria: {}, preco: {}-{}", 
                        nome, categoria, precoMin, precoMax);
            
            List<Produto> produtos = produtoRepository.buscarComFiltros(
                nome, categoria, null, null, precoMin, precoMax, null, true, false);
            
            logger.info("Encontrados {} produtos com os filtros aplicados", produtos.size());
            return produtos;
            
        } catch (Exception e) {
            logger.error("Erro ao buscar produtos com filtros: {}", e.getMessage(), e);
            throw new BusinessException("Não foi possível buscar produtos com filtros", e);
        }
    }
    
    /**
     * Busca produto por código de barras
     * @param codigoBarras Código de barras do produto
     * @return Produto encontrado ou null
     */
    public Produto buscarPorCodigoBarras(String codigoBarras) {
        try {
            logger.debug("Buscando produto por código de barras: {}", codigoBarras);
            
            if (codigoBarras == null || codigoBarras.trim().isEmpty()) {
                logger.warn("Busca por código de barras vazia");
                return null;
            }
            
            Produto produto = produtoRepository.buscarPorCodigoBarras(codigoBarras);
            
            if (produto != null) {
                logger.debug("Produto encontrado por código de barras: {}", produto.getNome());
            } else {
                logger.debug("Nenhum produto encontrado com código de barras: {}", codigoBarras);
            }
            
            return produto;
            
        } catch (Exception e) {
            logger.error("Erro ao buscar produto por código de barras '{}': {}", codigoBarras, e.getMessage(), e);
            throw new BusinessException("Não foi possível buscar produto por código de barras", e);
        }
    }
    
    /**
     * Lista todos os produtos
     * @return Lista de todos os produtos
     */
    public List<Produto> listar() {
        try {
            logger.debug("Listando todos os produtos");
            
            List<Produto> produtos = produtoRepository.listar();
            
            logger.info("Total de produtos listados: {}", produtos.size());
            return produtos;
            
        } catch (Exception e) {
            logger.error("Erro ao listar produtos: {}", e.getMessage(), e);
            throw new BusinessException("Não foi possível listar produtos", e);
        }
    }
    
    /**
     * Busca produto pelo ID
     * @param id ID do produto
     * @return Produto encontrado ou null
     */
    public Produto buscarPorId(Long id) {
        try {
            logger.debug("Buscando produto por ID: {}", id);
            
            if (id == null) {
                logger.warn("Busca por ID nula");
                return null;
            }
            
            java.util.Optional<Produto> produtoOpt = produtoRepository.buscarPorId(id);
            
            if (produtoOpt.isPresent()) {
                logger.debug("Produto encontrado por ID: {}", produtoOpt.get().getNome());
                return produtoOpt.get();
            } else {
                logger.debug("Nenhum produto encontrado com ID: {}", id);
                return null;
            }
            
        } catch (Exception e) {
            logger.error("Erro ao buscar produto por ID '{}': {}", id, e.getMessage(), e);
            throw new BusinessException("Não foi possível buscar produto por ID", e);
        }
    }
    
    /**
     * Verifica se um produto tem estoque disponível
     * @param produto Produto a ser verificado
     * @param quantidade Quantidade desejada
     * @return true se tiver estoque suficiente
     */
    public boolean verificarEstoque(Produto produto, int quantidade) {
        try {
            if (produto == null) {
                logger.warn("Produto nulo fornecido para verificação de estoque");
                return false;
            }
            
            if (quantidade <= 0) {
                logger.warn("Quantidade inválida para verificação de estoque: {}", quantidade);
                return false;
            }
            
            boolean disponivel = produto.getEstoque() >= quantidade;
            
            logger.debug("Estoque verificado - Produto: {}, Estoque: {}, Necessário: {}, Disponível: {}", 
                        produto.getNome(), produto.getEstoque(), quantidade, disponivel);
            
            return disponivel;
            
        } catch (Exception e) {
            logger.error("Erro ao verificar estoque do produto: {}", e.getMessage(), e);
            return false;
        }
    }
}
