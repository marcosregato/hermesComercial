package com.br.hermescomercial.service;

import com.br.hermescomercial.dao.ProdutoDao;
import com.br.hermescomercial.model.Produto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Serviço de controle de preços e promoções
 * @author Hermes Comercial
 * @version 2.8.0
 */
public class PrecoService {
    
    private final ProdutoDao produtoDao;
    
    // Configurações de negócio
    private static final BigDecimal INFLACAO_MENSAL_PADRAO = BigDecimal.valueOf(0.005); // 0,5% ao mês
    private static final BigDecimal MARGEM_LUCRO_MINIMA = BigDecimal.valueOf(0.20); // 20% de margem
    private static final BigDecimal DESCONTO_PROMOCIONAL_MAXIMO = BigDecimal.valueOf(0.30); // 30% de desconto
        
    public enum TipoReajuste {
        INFLACAO,       // Reajuste por inflação
        CUSTO,         // Reajuste por custo
        MARGEM,        // Reajuste por margem
        PROMOCIONAL,   // Reajuste promocional (desconto)
        MANUAL         // Reajuste manual
    }
    
    public enum StatusPromocao {
        ATIVA,         // Promoção ativa
        AGENDADA,      // Promoção agendada
        FINALIZADA,    // Promoção finalizada
        CANCELADA      // Promoção cancelada
    }
    
    public PrecoService() {
        this.produtoDao = new ProdutoDao();
    }
    
    /**
     * Aplica reajuste automático por inflação
     */
    public boolean aplicarReajusteInflacao(List<String> codigosProdutos) {
        try {
            List<Produto> produtos = new ArrayList<>();
            
            for (String codigo : codigosProdutos) {
                List<Produto> encontrados = produtoDao.buscar(codigo);
                if (!encontrados.isEmpty()) {
                    produtos.add(encontrados.get(0));
                }
            }
            
            BigDecimal fatorReajuste = BigDecimal.ONE.add(INFLACAO_MENSAL_PADRAO);
            
            for (Produto produto : produtos) {
                BigDecimal precoAtual = produto.getPrecoVenda();
                BigDecimal novoPreco = precoAtual.multiply(fatorReajuste).setScale(2, RoundingMode.HALF_UP);
                
                // Validar margem mínima
                if (validarMargemLucro(produto, novoPreco)) {
                    produto.setPrecoVenda(novoPreco);
                    // Aqui teria que salvar no banco de dados
                    registrarAlteracaoPreco(produto.getCodigo(), precoAtual, novoPreco, TipoReajuste.INFLACAO);
                }
            }
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Aplica reajuste por categoria
     */
    public boolean aplicarReajustePorCategoria(String categoria, BigDecimal percentual, TipoReajuste tipo) {
        try {
            List<Produto> produtos = produtoDao.listar();
            BigDecimal fatorReajuste = BigDecimal.ONE.add(percentual.divide(BigDecimal.valueOf(100)));
            
            int atualizados = 0;
            
            for (Produto produto : produtos) {
                if (categoria.equals("TODOS") || categoria.equalsIgnoreCase(produto.getCategoria())) {
                    BigDecimal precoAtual = produto.getPrecoVenda();
                    BigDecimal novoPreco = precoAtual.multiply(fatorReajuste).setScale(2, RoundingMode.HALF_UP);
                    
                    if (validarMargemLucro(produto, novoPreco)) {
                        produto.setPrecoVenda(novoPreco);
                        registrarAlteracaoPreco(produto.getCodigo(), precoAtual, novoPreco, tipo);
                        atualizados++;
                    }
                }
            }
            
            return atualizados > 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Cria promoção para produto ou categoria
     */
    public boolean criarPromocao(String codigoProduto, String categoria, BigDecimal descontoPercentual, 
                                 LocalDate dataInicio, LocalDate dataFim, String descricao) {
        try {
            List<Produto> produtos = new ArrayList<>();
            
            if (codigoProduto != null && !codigoProduto.isEmpty()) {
                List<Produto> encontrados = produtoDao.buscar(codigoProduto);
                if (!encontrados.isEmpty()) {
                    produtos.add(encontrados.get(0));
                }
            } else if (categoria != null && !categoria.isEmpty()) {
                List<Produto> todosProdutos = produtoDao.listar();
                for (Produto produto : todosProdutos) {
                    if (categoria.equalsIgnoreCase(produto.getCategoria())) {
                        produtos.add(produto);
                    }
                }
            }
            
            if (produtos.isEmpty()) {
                return false;
            }
            
            // Validar desconto máximo
            if (descontoPercentual.compareTo(DESCONTO_PROMOCIONAL_MAXIMO.multiply(BigDecimal.valueOf(100))) > 0) {
                return false;
            }
            
            BigDecimal fatorDesconto = BigDecimal.ONE.subtract(descontoPercentual.divide(BigDecimal.valueOf(100)));
            
            for (Produto produto : produtos) {
                BigDecimal precoOriginal = produto.getPrecoVenda();
                BigDecimal precoPromocional = precoOriginal.multiply(fatorDesconto).setScale(2, RoundingMode.HALF_UP);
                
                // Salvar preço original antes de aplicar promoção
                // Aqui teria que salvar no banco de dados
                registrarPromocao(produto.getCodigo(), precoOriginal, precoPromocional, 
                                dataInicio, dataFim, descricao);
                
                produto.setPrecoVenda(precoPromocional);
            }
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Finaliza promoções expiradas
     */
    public int finalizarPromocoesExpiradas() {
        try {
            LocalDate hoje = LocalDate.now();
            int finalizadas = 0;
            
            // Aqui teria que buscar promoções ativas no banco
            // Por enquanto, simulação
            
            List<Produto> produtos = produtoDao.listar();
            
            for (Produto produto : produtos) {
                // Verificar se produto está em promoção (simulação)
                if (estaEmPromocao(produto.getCodigo())) {
                    // Verificar se promoção expirou
                    LocalDate dataFimPromocao = getDataFimPromocao(produto.getCodigo());
                    
                    if (dataFimPromocao != null && dataFimPromocao.isBefore(hoje)) {
                        // Restaurar preço original
                        BigDecimal precoOriginal = getPrecoOriginal(produto.getCodigo());
                        if (precoOriginal != null) {
                            produto.setPrecoVenda(precoOriginal);
                            registrarFinalizacaoPromocao(produto.getCodigo());
                            finalizadas++;
                        }
                    }
                }
            }
            
            return finalizadas;
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Obtém produtos em promoção
     */
    public List<Produto> getProdutosEmPromocao() {
        List<Produto> produtosPromocao = new ArrayList<>();
        
        try {
            List<Produto> todosProdutos = produtoDao.listar();
            
            for (Produto produto : todosProdutos) {
                if (estaEmPromocao(produto.getCodigo())) {
                    produtosPromocao.add(produto);
                }
            }
            
        } catch (Exception e) {
            // Retornar lista vazia em caso de erro
        }
        
        return produtosPromocao;
    }
    
    /**
     * Calcula preço sugerido com base no custo e margem
     */
    public BigDecimal calcularPrecoSugerido(BigDecimal custo, BigDecimal margemDesejada) {
        BigDecimal fatorMargem = BigDecimal.ONE.add(margemDesejada.divide(BigDecimal.valueOf(100)));
        return custo.multiply(fatorMargem).setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Valida se a margem de lucro é adequada
     */
    private boolean validarMargemLucro(Produto produto, BigDecimal precoVenda) {
        // Simulação - na prática teria que ter o custo do produto
        BigDecimal custoSimulado = produto.getPrecoVenda().multiply(BigDecimal.valueOf(0.70)); // 70% do preço atual como custo
        
        BigDecimal margem = precoVenda.subtract(custoSimulado).divide(precoVenda, 4, RoundingMode.HALF_UP);
        
        return margem.compareTo(MARGEM_LUCRO_MINIMA) >= 0;
    }
    
    /**
     * Obtém histórico de preços de um produto
     */
    public List<Map<String, Object>> getHistoricoPrecos(String codigoProduto) {
        List<Map<String, Object>> historico = new ArrayList<>();
        
        try {
            // Simulação de histórico
            List<Produto> produtos = produtoDao.buscar(codigoProduto);
            if (!produtos.isEmpty()) {
                Produto produto = produtos.get(0);
                
                // Adicionar preço atual
                Map<String, Object> precoAtual = new HashMap<>();
                precoAtual.put("data", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                precoAtual.put("preco", produto.getPrecoVenda());
                precoAtual.put("tipo", "Atual");
                precoAtual.put("motivo", "Preço vigente");
                historico.add(precoAtual);
                
                // Simular histórico anterior
                BigDecimal precoAnterior = produto.getPrecoVenda().multiply(BigDecimal.valueOf(0.95));
                Map<String, Object> registroAnterior = new HashMap<>();
                registroAnterior.put("data", LocalDateTime.now().minusDays(30).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                registroAnterior.put("preco", precoAnterior);
                registroAnterior.put("tipo", "Reajuste");
                registroAnterior.put("motivo", "Reajuste inflacionário");
                historico.add(registroAnterior);
            }
            
        } catch (Exception e) {
            // Retornar lista vazia em caso de erro
        }
        
        return historico;
    }
    
    /**
     * Analisa competitividade de preços
     */
    public Map<String, Object> analisarCompetitividade(String codigoProduto) {
        Map<String, Object> analise = new HashMap<>();
        
        try {
            List<Produto> produtos = produtoDao.buscar(codigoProduto);
            if (!produtos.isEmpty()) {
                Produto produto = produtos.get(0);
                BigDecimal precoAtual = produto.getPrecoVenda();
                
                // Simulação de análise de mercado
                BigDecimal precoMedioMercado = precoAtual.multiply(BigDecimal.valueOf(1.05)); // 5% acima como média
                
                String posicao;
                if (precoAtual.compareTo(precoMedioMercado.multiply(BigDecimal.valueOf(0.95))) < 0) {
                    posicao = "Abaixo da média (competitivo)";
                } else if (precoAtual.compareTo(precoMedioMercado.multiply(BigDecimal.valueOf(1.05))) > 0) {
                    posicao = "Acima da média (preço premium)";
                } else {
                    posicao = "Dentro da média (preço justo)";
                }
                
                analise.put("precoAtual", precoAtual);
                analise.put("precoMedioMercado", precoMedioMercado);
                analise.put("posicao", posicao);
                analise.put("diferencaPercentual", precoAtual.subtract(precoMedioMercado)
                    .divide(precoMedioMercado, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)));
                
                // Sugestão
                BigDecimal sugestao;
                if (precoAtual.compareTo(precoMedioMercado.multiply(BigDecimal.valueOf(0.90))) < 0) {
                    sugestao = precoMedioMercado.multiply(BigDecimal.valueOf(0.95)); // Aumentar um pouco
                    analise.put("sugestao", "Aumentar preço para melhorar margem");
                } else if (precoAtual.compareTo(precoMedioMercado.multiply(BigDecimal.valueOf(1.10))) > 0) {
                    sugestao = precoMedioMercado.multiply(BigDecimal.valueOf(1.05)); // Reduzir um pouco
                    analise.put("sugestao", "Reduzir preço para aumentar competitividade");
                } else {
                    sugestao = precoAtual;
                    analise.put("sugestao", "Preço adequado - manter atual");
                }
                
                analise.put("precoSugerido", sugestao.setScale(2, RoundingMode.HALF_UP));
            }
            
        } catch (Exception e) {
            // Valores padrão em caso de erro
            analise.put("erro", "Não foi possível analisar competitividade");
        }
        
        return analise;
    }
    
    /**
     * Obtém estatísticas de preços
     */
    public Map<String, Object> getEstatisticasPrecos() {
        Map<String, Object> estatisticas = new HashMap<>();
        
        try {
            List<Produto> produtos = produtoDao.listar();
            
            if (produtos.isEmpty()) {
                estatisticas.put("totalProdutos", 0);
                return estatisticas;
            }
            
            BigDecimal somaPrecos = BigDecimal.ZERO;
            BigDecimal precoMinimo = BigDecimal.valueOf(Double.MAX_VALUE);
            BigDecimal precoMaximo = BigDecimal.ZERO;
            int produtosEmPromocao = 0;
            
            for (Produto produto : produtos) {
                BigDecimal preco = produto.getPrecoVenda();
                somaPrecos = somaPrecos.add(preco);
                
                if (preco.compareTo(precoMinimo) < 0) {
                    precoMinimo = preco;
                }
                
                if (preco.compareTo(precoMaximo) > 0) {
                    precoMaximo = preco;
                }
                
                if (estaEmPromocao(produto.getCodigo())) {
                    produtosEmPromocao++;
                }
            }
            
            BigDecimal precoMedio = somaPrecos.divide(BigDecimal.valueOf(produtos.size()), 2, RoundingMode.HALF_UP);
            
            estatisticas.put("totalProdutos", produtos.size());
            estatisticas.put("precoMedio", precoMedio);
            estatisticas.put("precoMinimo", precoMinimo);
            estatisticas.put("precoMaximo", precoMaximo);
            estatisticas.put("produtosEmPromocao", produtosEmPromocao);
            estatisticas.put("percentualPromocao", (produtosEmPromocao * 100.0) / produtos.size());
            
        } catch (Exception e) {
            estatisticas.put("erro", "Não foi possível obter estatísticas");
        }
        
        return estatisticas;
    }
    
    // Métodos simulados (em implementação real, interagiriam com banco de dados)
    private void registrarAlteracaoPreco(String codigo, BigDecimal precoAntigo, BigDecimal novoPreco, TipoReajuste tipo) {
        // Simulação - registraria alteração no log de preços
        System.out.println("Registrando alteração de preço - Produto: " + codigo + 
                          ", De: R$" + precoAntigo + ", Para: R$" + novoPreco + 
                          ", Tipo: " + tipo);
    }
    
    private void registrarPromocao(String codigo, BigDecimal precoOriginal, BigDecimal precoPromocional, 
                                 LocalDate dataInicio, LocalDate dataFim, String descricao) {
        // Simulação - registraria promoção no banco
        System.out.println("Registrando promoção - Produto: " + codigo + 
                          ", Preço original: R$" + precoOriginal + 
                          ", Preço promocional: R$" + precoPromocional + 
                          ", Período: " + dataInicio + " a " + dataFim);
    }
    
    private boolean estaEmPromocao(String codigo) {
        // Simulação - verificaria no banco se produto está em promoção
        return Math.random() < 0.2; // 20% de chance de estar em promoção
    }
    
    private LocalDate getDataFimPromocao(String codigo) {
        // Simulação - buscaria data fim da promoção no banco
        return LocalDate.now().plusDays((int)(Math.random() * 10));
    }
    
    private BigDecimal getPrecoOriginal(String codigo) {
        // Simulação - buscaria preço original no banco
        return BigDecimal.valueOf(100 + Math.random() * 900);
    }
    
    private void registrarFinalizacaoPromocao(String codigo) {
        // Simulação - registraria finalização da promoção
        System.out.println("Finalizando promoção - Produto: " + codigo);
    }
}
