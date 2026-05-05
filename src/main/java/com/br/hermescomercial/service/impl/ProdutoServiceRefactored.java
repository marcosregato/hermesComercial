package com.br.hermescomercial.service.impl;

import com.br.hermescomercial.service.base.BaseService;
import com.br.hermescomercial.repository.ProdutoRepository;
import com.br.hermescomercial.event.EventSystem;
import com.br.hermescomercial.model.Produto;
import com.br.hermescomercial.validation.ProdutoValidator;
import com.br.hermescomercial.exception.BusinessException;
import com.br.hermescomercial.mapper.ProdutoMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.concurrent.CompletableFuture;

/**
 * Implementação do Service Layer Pattern para Produto - Refatorado
 * Centraliza a lógica de negócio e regras para produtos
 * Versão 2.0.0 - Service Layer Refactoring
 */
public class ProdutoServiceRefactored extends BaseService {
    
    private final ProdutoRepository produtoRepository;
    private final ProdutoValidator validator;
    private final ProdutoMapper mapper;
    
    public ProdutoServiceRefactored() {
        this.produtoRepository = com.br.hermescomercial.injection.DependencyContainerRefactored.getInstance()
            .resolve(ProdutoRepository.class);
        this.validator = new ProdutoValidator();
        this.mapper = new ProdutoMapper();
    }
    
    /**
     * Construtor para injeção de dependências (testes)
     */
    public ProdutoServiceRefactored(ProdutoRepository produtoRepository, 
                                   ProdutoValidator validator,
                                   ProdutoMapper mapper) {
        this.produtoRepository = produtoRepository;
        this.validator = validator;
        this.mapper = mapper;
    }
    
    public Produto salvar(Produto produto) {
        return executeOperation(() -> {
            // Validação
            validator.validar(mapper.toDTO(produto));
            
            // Regras de negócio
            validarRegrasNegocio(produto);
            
            // Salvar
            boolean resultado = produtoRepository.salvar(produto);
            
            if (!resultado) {
                throw new BusinessException("Falha ao salvar produto");
            }
            
            // Publicar evento
            eventSystem.publish(new EventSystem.Event(
                "produto.criado", 
                mapper.toDTO(produto),
                "ProdutoServiceRefactored"
            ));
            
            // Invalidar cache
            invalidateCache("produtos.lista");
            
            return produto;
            
        }, "produto.salvar");
    }
    
    public Produto atualizar(Produto produto) {
        return executeOperation(() -> {
            // Verificar se produto existe
            Optional<Produto> existente = produtoRepository.buscarPorId(produto.getId());
            if (existente.isEmpty()) {
                throw new BusinessException("Produto não encontrado para atualização");
            }
            
            // Validação
            validator.validar(mapper.toDTO(produto));
            
            // Regras de negócio
            validarRegrasNegocio(produto);
            
            // Atualizar
            boolean resultado = produtoRepository.atualizar(produto);
            
            if (!resultado) {
                throw new BusinessException("Falha ao atualizar produto");
            }
            
            // Publicar evento
            eventSystem.publish(new EventSystem.Event(
                "produto.atualizado", 
                mapper.toDTO(produto),
                "ProdutoServiceRefactored"
            ));
            
            // Invalidar cache
            invalidateCache("produtos.lista");
            invalidateCache("produto." + produto.getId());
            
            return produto;
            
        }, "produto.atualizar");
    }
    
    public boolean remover(Long id) {
        return executeOperation(() -> {
            // Verificar se produto existe
            Optional<Produto> produto = produtoRepository.buscarPorId(id);
            if (produto.isEmpty()) {
                throw new BusinessException("Produto não encontrado para exclusão");
            }
            
            // Verificar se pode ser excluído
            validarPodeExcluir(produto.get());
            
            // Excluir
            boolean resultado = produtoRepository.remover(id);
            
            if (!resultado) {
                throw new BusinessException("Falha ao excluir produto");
            }
            
            // Publicar evento
            eventSystem.publish(new EventSystem.Event(
                "produto.excluido", 
                id,
                "ProdutoServiceRefactored"
            ));
            
            // Invalidar cache
            invalidateCache("produtos.lista");
            invalidateCache("produto." + id);
            
            return true;
            
        }, "produto.remover");
    }
    
    public Produto buscarPorId(Long id) {
        String cacheKey = "produto." + id;
        
        return getFromCacheOrExecute(cacheKey, () -> {
            requireNonNull(id, "ID do produto");
            
            Optional<Produto> produto = produtoRepository.buscarPorId(id);
            
            if (produto.isPresent()) {
                // Publicar evento de acesso
                eventSystem.publish(new EventSystem.Event(
                    "produto.acessado", 
                    mapper.toDTO(produto.get()),
                    "ProdutoServiceRefactored"
                ));
            }
            
            return produto.get();
            
        }, "produto.buscarPorId");
    }
    
    public List<Produto> buscarTodos() {
        return getFromCacheOrExecute("produtos.lista", () -> {
            List<Produto> produtos = produtoRepository.buscarTodos();
            
            // Publicar evento de listagem
            eventSystem.publish(new EventSystem.Event(
                "produtos.listados", 
                produtos.size(),
                "ProdutoServiceRefactored"
            ));
            
            return produtos;
            
        }, "produto.buscarTodos");
    }
    
    public List<Produto> buscarPorNome(String nome) {
        requireNonEmpty(nome, "Nome do produto");
        
        return executeOperation(() -> {
            List<Produto> produtos = produtoRepository.buscarPorNome(nome);
            
            // Publicar evento de busca
            eventSystem.publish(new EventSystem.Event(
                "produtos.buscar.nome", 
                Map.of("nome", nome, "resultados", produtos.size()),
                "ProdutoServiceRefactored"
            ));
            
            return produtos;
            
        }, "produto.buscarPorNome");
    }
    
    public List<Produto> buscarPorCategoria(String categoria) {
        requireNonEmpty(categoria, "Categoria do produto");
        
        return executeOperation(() -> {
            List<Produto> produtos = produtoRepository.buscarPorCategoria(categoria);
            
            // Publicar evento de busca
            eventSystem.publish(new EventSystem.Event(
                "produtos.buscar.categoria", 
                Map.of("categoria", categoria, "resultados", produtos.size()),
                "ProdutoServiceRefactored"
            ));
            
            return produtos;
            
        }, "produto.buscarPorCategoria");
    }
    
    public List<Produto> buscarPorFaixaPreco(BigDecimal precoMin, BigDecimal precoMax) {
        requireNonNull(precoMin, "Preço mínimo");
        requireNonNull(precoMax, "Preço máximo");
        requirePositive(precoMin, "Preço mínimo");
        requirePositive(precoMax, "Preço máximo");
        
        if (precoMin.compareTo(precoMax) > 0) {
            throw new BusinessException("Preço mínimo não pode ser maior que o preço máximo");
        }
        
        return executeOperation(() -> {
            List<Produto> produtos = produtoRepository.buscarPorFaixaPreco(precoMin, precoMax);
            
            // Publicar evento de busca
            eventSystem.publish(new EventSystem.Event(
                "produtos.buscar.faixa_preco", 
                Map.of("precoMin", precoMin, "precoMax", precoMax, "resultados", produtos.size()),
                "ProdutoServiceRefactored"
            ));
            
            return produtos;
            
        }, "produto.buscarPorFaixaPreco");
    }
    
    public List<Produto> buscarComEstoqueBaixo(int limite) {
        requireNonNegative(limite, "Limite de estoque");
        
        return executeOperation(() -> {
            List<Produto> produtos = produtoRepository.buscarComEstoqueBaixo(limite);
            
            // Publicar evento de alerta
            if (!produtos.isEmpty()) {
                eventSystem.publish(new EventSystem.Event(
                    "produtos.estoque_baixo", 
                    Map.of("limite", limite, "produtos", produtos.size()),
                    "ProdutoServiceRefactored"
                ));
            }
            
            return produtos;
            
        }, "produto.buscarComEstoqueBaixo");
    }
    
    public boolean atualizarEstoque(Long id, int quantidade) {
        requireNonNull(id, "ID do produto");
        requireNonNegative(quantidade, "Quantidade");
        
        return executeOperation(() -> {
            Optional<Produto> produto = produtoRepository.buscarPorId(id);
            if (produto.isEmpty()) {
                throw new BusinessException("Produto não encontrado");
            }
            
            Produto p = produto.get();
            p.setEstoque(quantidade);
            
            boolean resultado = produtoRepository.atualizar(p);
            
            if (resultado) {
                // Publicar evento de atualização de estoque
                eventSystem.publish(new EventSystem.Event(
                    "produto.estoque.atualizado", 
                    Map.of("id", id, "novaQuantidade", quantidade),
                    "ProdutoServiceRefactored"
                ));
                
                // Invalidar cache
                invalidateCache("produto." + id);
                invalidateCache("produtos.lista");
            }
            
            return resultado;
            
        }, "produto.atualizarEstoque");
    }
    
    public BigDecimal calcularValorTotalEstoque() {
        return getFromCacheOrExecute("produtos.valor_total_estoque", () -> {
            List<Produto> produtos = buscarTodos();
            
            BigDecimal total = produtos.stream()
                .filter(p -> p.getPreco() != null)
                .map(p -> p.getPreco().multiply(new BigDecimal(p.getEstoque())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            // Publicar evento de cálculo
            eventSystem.publish(new EventSystem.Event(
                "produtos.valor_total_calculado", 
                total,
                "ProdutoServiceRefactored"
            ));
            
            return total;
            
        }, "produto.calcularValorTotalEstoque");
    }
    
    public Map<String, Integer> getResumoPorCategoria() {
        return getFromCacheOrExecute("produtos.resumo_categoria", () -> {
            List<Produto> produtos = buscarTodos();
            
            Map<String, Integer> resumo = produtos.stream()
                .filter(p -> p.getCategoria() != null)
                .collect(Collectors.groupingBy(
                    Produto::getCategoria,
                    Collectors.collectingAndThen(Collectors.counting(), Math::toIntExact)
                ));
            
            // Publicar evento de resumo
            eventSystem.publish(new EventSystem.Event(
                "produtos.resumo_categoria_gerado", 
                resumo.size(),
                "ProdutoServiceRefactored"
            ));
            
            return resumo;
            
        }, "produto.getResumoPorCategoria");
    }
    
    public CompletableFuture<List<Produto>> buscarTodosAsync() {
        return executeAsyncOperation(this::buscarTodos, "produto.buscarTodosAsync");
    }
    
    public CompletableFuture<Boolean> salvarAsync(Produto produto) {
        return executeAsyncOperation(() -> salvar(produto) != null, "produto.salvarAsync");
    }
    
    /**
     * Valida regras de negócio específicas
     */
    private void validarRegrasNegocio(Produto produto) {
        // Regra: Produtos com preço zero não podem ter estoque positivo
        if (produto.getPreco() != null && produto.getPreco().compareTo(BigDecimal.ZERO) == 0 && 
            produto.getEstoque() > 0) {
            throw new BusinessException("Produtos com preço zero não podem ter estoque positivo");
        }
        
        // Regra: Código deve ser único
        if (produto.getCodigo() != null && !produto.getCodigo().trim().isEmpty()) {
            List<Produto> existentes = produtoRepository.buscarPorCodigo(produto.getCodigo());
            if (!existentes.isEmpty() && 
                (produto.getId() == null || !existentes.get(0).getId().equals(produto.getId()))) {
                throw new BusinessException("Código de produto já existe");
            }
        }
        
        // Regra: Estoque mínimo configurado
        int estoqueMinimo = configManager.getInteger("produto.estoque_minimo", 5);
        if (produto.getEstoque() < estoqueMinimo) {
            logger.warning("Produto com estoque abaixo do mínimo: " + produto.getNome());
        }
    }
    
    /**
     * Valida se produto pode ser excluído
     */
    private void validarPodeExcluir(Produto produto) {
        // Verificar se não há vendas associadas (simulação)
        if (possuiVendasAssociadas(produto.getId())) {
            throw new BusinessException("Produto não pode ser excluído pois possui vendas associadas");
        }
        
        // Verificar se não é produto essencial
        if ("Essencial".equals(produto.getCategoria())) {
            throw new BusinessException("Produtos essenciais não podem ser excluídos");
        }
    }
    
    /**
     * Verifica se há vendas associadas ao produto (simulação)
     */
    private boolean possuiVendasAssociadas(Long produtoId) {
        // Na implementação real, consultaria a tabela de vendas
        return false;
    }
}
