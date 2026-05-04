package com.br.hermescomercial.service;

import com.br.hermescomercial.dao.ProdutoDao;
import com.br.hermescomercial.model.Produto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Serviço de validação e controle de estoque
 * @author Hermes Comercial
 * @version 2.8.0
 */
public class EstoqueService {
    
    private final ProdutoDao produtoDao;
    
    // Configurações de negócio
    private static final int ESTOQUE_MINIMO_PADRAO = 5;
    private static final BigDecimal MARGEM_SEGURANCA = BigDecimal.valueOf(0.10); // 10%
    private static final int LIMITE_ALERTA_VERMELHO = 2;
        
    public enum StatusEstoque {
        SUFICIENTE,      // Estoque adequado
        BAIXO,          // Abaixo do mínimo
        CRITICO,        // Quase esgotado
        ESGOTADO,       // Sem estoque
        INDISPONIVEL    // Produto não encontrado
    }
    
    public enum ResultadoValidacao {
        OK,             // Venda permitida
        ESTOQUE_INSUFICIENTE,  // Não há estoque suficiente
        PRODUTO_INEXISTENTE,   // Produto não encontrado
        QUANTIDADE_INVALIDA    // Quantidade <= 0
    }
    
    public EstoqueService() {
        this.produtoDao = new ProdutoDao();
    }
    
    /**
     * Valida se é possível vender a quantidade solicitada
     */
    public ResultadoValidacao validarVenda(String codigoProduto, int quantidade) {
        try {
            // Validar quantidade
            if (quantidade <= 0) {
                return ResultadoValidacao.QUANTIDADE_INVALIDA;
            }
            
            // Buscar produto
            List<Produto> produtos = produtoDao.buscar(codigoProduto);
            if (produtos.isEmpty()) {
                return ResultadoValidacao.PRODUTO_INEXISTENTE;
            }
            
            Produto produto = produtos.get(0);
            int estoqueDisponivel = produto.getQuantidadeEstoque();
            
            // Validar estoque
            if (estoqueDisponivel < quantidade) {
                return ResultadoValidacao.ESTOQUE_INSUFICIENTE;
            }
            
            return ResultadoValidacao.OK;
            
        } catch (Exception e) {
            // Em caso de erro, não permitir a venda por segurança
            return ResultadoValidacao.PRODUTO_INEXISTENTE;
        }
    }
    
    /**
     * Obtém o status do estoque de um produto
     */
    public StatusEstoque getStatusEstoque(String codigoProduto) {
        try {
            List<Produto> produtos = produtoDao.buscar(codigoProduto);
            if (produtos.isEmpty()) {
                return StatusEstoque.INDISPONIVEL;
            }
            
            Produto produto = produtos.get(0);
            int estoque = produto.getQuantidadeEstoque();
            int estoqueMinimo = produto.getEstoqueMinimo() > 0 ? produto.getEstoqueMinimo() : ESTOQUE_MINIMO_PADRAO;
            
            if (estoque == 0) {
                return StatusEstoque.ESGOTADO;
            } else if (estoque <= LIMITE_ALERTA_VERMELHO) {
                return StatusEstoque.CRITICO;
            } else if (estoque <= estoqueMinimo) {
                return StatusEstoque.BAIXO;
            } else {
                return StatusEstoque.SUFICIENTE;
            }
            
        } catch (Exception e) {
            return StatusEstoque.INDISPONIVEL;
        }
    }
    
    /**
     * Verifica múltiplos produtos para venda
     */
    public Map<String, ResultadoValidacao> validarMultiplosProdutos(Map<String, Integer> itens) {
        Map<String, ResultadoValidacao> resultados = new HashMap<>();
        
        for (Map.Entry<String, Integer> item : itens.entrySet()) {
            String codigoProduto = item.getKey();
            Integer quantidade = item.getValue();
            
            ResultadoValidacao resultado = validarVenda(codigoProduto, quantidade);
            resultados.put(codigoProduto, resultado);
        }
        
        return resultados;
    }
    
    /**
     * Obtém produtos com estoque baixo
     */
    public List<Produto> getProdutosEstoqueBaixo() {
        List<Produto> produtosBaixo = new ArrayList<>();
        
        try {
            List<Produto> todosProdutos = produtoDao.listar();
            
            for (Produto produto : todosProdutos) {
                StatusEstoque status = getStatusEstoque(produto.getCodigo());
                if (status == StatusEstoque.BAIXO || status == StatusEstoque.CRITICO) {
                    produtosBaixo.add(produto);
                }
            }
            
        } catch (Exception e) {
            // Retornar lista vazia em caso de erro
        }
        
        return produtosBaixo;
    }
    
    /**
     * Obtém produtos esgotados
     */
    public List<Produto> getProdutosEsgotados() {
        List<Produto> produtosEsgotados = new ArrayList<>();
        
        try {
            List<Produto> todosProdutos = produtoDao.listar();
            
            for (Produto produto : todosProdutos) {
                StatusEstoque status = getStatusEstoque(produto.getCodigo());
                if (status == StatusEstoque.ESGOTADO) {
                    produtosEsgotados.add(produto);
                }
            }
            
        } catch (Exception e) {
            // Retornar lista vazia em caso de erro
        }
        
        return produtosEsgotados;
    }
    
    /**
     * Calcula sugestão de reposição com base no histórico
     */
    public int calcularSugestaoReposicao(String codigoProduto, int diasHistorico) {
        try {
            // Por enquanto, usar uma fórmula simples
            // Futuro: analisar histórico de vendas
            
            List<Produto> produtos = produtoDao.buscar(codigoProduto);
            if (produtos.isEmpty()) {
                return ESTOQUE_MINIMO_PADRAO;
            }
            
            Produto produto = produtos.get(0);
            int estoqueMinimo = produto.getEstoqueMinimo() > 0 ? produto.getEstoqueMinimo() : ESTOQUE_MINIMO_PADRAO;
            
            // Sugestão: estoque mínimo + margem de segurança
            int sugestao = estoqueMinimo + (int)(estoqueMinimo * MARGEM_SEGURANCA.doubleValue());
            
            return Math.max(sugestao, ESTOQUE_MINIMO_PADRAO);
            
        } catch (Exception e) {
            return ESTOQUE_MINIMO_PADRAO;
        }
    }
    
    /**
     * Verifica se produto precisa de atenção
     */
    public boolean precisaAtencao(String codigoProduto) {
        StatusEstoque status = getStatusEstoque(codigoProduto);
        return status == StatusEstoque.CRITICO || status == StatusEstoque.ESGOTADO;
    }
    
    /**
     * Obtém mensagem de alerta para o status
     */
    public String getMensagemAlerta(StatusEstoque status) {
        switch (status) {
            case ESGOTADO:
                return "🔴 PRODUTO ESGOTADO - Impossível vender";
            case CRITICO:
                return "🟠 ESTOQUE CRÍTICO - Reposição urgente";
            case BAIXO:
                return "🟡 ESTOQUE BAIXO - Atenção necessária";
            case SUFICIENTE:
                return "🟢 Estoque adequado";
            case INDISPONIVEL:
                return "⚫ Produto não encontrado";
            default:
                return "❓ Status desconhecido";
        }
    }
    
    /**
     * Obtém cor para o status
     */
    public String getCorStatus(StatusEstoque status) {
        switch (status) {
            case ESGOTADO:
                return "#FF0000"; // Vermelho
            case CRITICO:
                return "#FF8C00"; // Laranja
            case BAIXO:
                return "#FFD700"; // Amarelo
            case SUFICIENTE:
                return "#00FF00"; // Verde
            case INDISPONIVEL:
                return "#808080"; // Cinza
            default:
                return "#000000"; // Preto
        }
    }
    
    /**
     * Simula reserva de estoque (para verificação)
     */
    public boolean simularReserva(String codigoProduto, int quantidade) {
        try {
            List<Produto> produtos = produtoDao.buscar(codigoProduto);
            if (produtos.isEmpty()) {
                return false;
            }
            
            Produto produto = produtos.get(0);
            return produto.getQuantidadeEstoque() >= quantidade;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Obtém estatísticas gerais do estoque
     */
    public Map<String, Object> getEstatisticasEstoque() {
        Map<String, Object> estatisticas = new HashMap<>();
        
        try {
            List<Produto> todosProdutos = produtoDao.listar();
            
            int totalProdutos = todosProdutos.size();
            int produtosSuficientes = 0;
            int produtosBaixos = 0;
            int produtosCriticos = 0;
            int produtosEsgotados = 0;
            
            for (Produto produto : todosProdutos) {
                StatusEstoque status = getStatusEstoque(produto.getCodigo());
                
                switch (status) {
                    case SUFICIENTE:
                        produtosSuficientes++;
                        break;
                    case BAIXO:
                        produtosBaixos++;
                        break;
                    case CRITICO:
                        produtosCriticos++;
                        break;
                    case ESGOTADO:
                        produtosEsgotados++;
                        break;
                    case INDISPONIVEL:
                        // Não conta como produto ativo
                        break;
                }
            }
            
            estatisticas.put("totalProdutos", totalProdutos);
            estatisticas.put("suficientes", produtosSuficientes);
            estatisticas.put("baixos", produtosBaixos);
            estatisticas.put("criticos", produtosCriticos);
            estatisticas.put("esgotados", produtosEsgotados);
            
            // Percentuais
            if (totalProdutos > 0) {
                estatisticas.put("percentualSuficientes", (produtosSuficientes * 100.0) / totalProdutos);
                estatisticas.put("percentualAtencao", ((produtosBaixos + produtosCriticos + produtosEsgotados) * 100.0) / totalProdutos);
            }
            
        } catch (Exception e) {
            // Valores padrão em caso de erro
            estatisticas.put("totalProdutos", 0);
            estatisticas.put("suficientes", 0);
            estatisticas.put("baixos", 0);
            estatisticas.put("criticos", 0);
            estatisticas.put("esgotados", 0);
            estatisticas.put("percentualSuficientes", 0.0);
            estatisticas.put("percentualAtencao", 0.0);
        }
        
        return estatisticas;
    }
}
