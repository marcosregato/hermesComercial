package com.br.hermescomercial.service;

import com.br.hermescomercial.dao.DashboardDAO;
import com.br.hermescomercial.model.DashboardMetric;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Service para lógica de negócio de dashboard e métricas
 * Versão 2.3.0 - Dashboard com KPIs e gráficos
 */
public class DashboardService {
    
    private static final Logger logger = LogManager.getLogger(DashboardService.class);
    private final DashboardDAO dashboardDAO;
    
    public DashboardService() {
        this.dashboardDAO = new DashboardDAO();
    }
    
    /**
     * Gera KPIs principais do dashboard
     */
    public List<DashboardMetric> gerarKPIs() {
        try {
            return dashboardDAO.getKPIs();
        } catch (SQLException e) {
            logger.error("Erro ao gerar KPIs", e);
            throw new RuntimeException("Falha ao gerar KPIs", e);
        }
    }
    
    /**
     * Calcula métricas de vendas diárias
     */
    public List<DashboardMetric> calcularVendasDiarias(int dias) {
        try {
            return dashboardDAO.getVendasDiarias(dias);
        } catch (SQLException e) {
            logger.error("Erro ao calcular vendas diárias", e);
            throw new RuntimeException("Falha ao calcular vendas diárias", e);
        }
    }
    
    /**
     * Busca produtos mais vendidos
     */
    public List<DashboardMetric> getTopProdutos(int limite) {
        try {
            return dashboardDAO.getTopProdutos(limite);
        } catch (SQLException e) {
            logger.error("Erro ao buscar top produtos", e);
            throw new RuntimeException("Falha ao buscar top produtos", e);
        }
    }
    
    /**
     * Busca resumo financeiro
     */
    public List<DashboardMetric> getResumoFinanceiro() {
        try {
            return dashboardDAO.getResumoFinanceiro();
        } catch (SQLException e) {
            logger.error("Erro ao buscar resumo financeiro", e);
            throw new RuntimeException("Falha ao buscar resumo financeiro", e);
        }
    }
    
    /**
     * Salva uma métrica no dashboard
     */
    public DashboardMetric salvarMetrica(DashboardMetric metric) {
        try {
            return dashboardDAO.save(metric);
        } catch (SQLException e) {
            logger.error("Erro ao salvar métrica: " + metric.getNome(), e);
            throw new RuntimeException("Falha ao salvar métrica", e);
        }
    }
    
    /**
     * Gera métricas automáticas do dia
     */
    public void gerarMetricasDiarias() {
        LocalDate hoje = LocalDate.now();
        
        try {
            // Vendas do dia
            gerarMetricaVendasDiarias(hoje);
            
            // Total de clientes
            gerarMetricaTotalClientes(hoje);
            
            // Produtos vendidos
            gerarMetricaProdutosVendidos(hoje);
            
            logger.info("Métricas diárias geradas com sucesso para: " + hoje);
            
        } catch (Exception e) {
            logger.error("Erro ao gerar métricas diárias", e);
            throw new RuntimeException("Falha ao gerar métricas diárias", e);
        }
    }
    
    /**
     * Gera métrica de vendas diárias
     */
    private void gerarMetricaVendasDiarias(LocalDate data) {
        try {
            // Simulação - na implementação real, buscar do banco
            BigDecimal valorVendas = new BigDecimal("15000.00"); // Exemplo
            BigDecimal valorAnterior = new BigDecimal("12000.00"); // Exemplo
            
            DashboardMetric metric = new DashboardMetric();
            metric.setNome("Vendas Diárias");
            metric.setValor(valorVendas);
            metric.setUnidade("R$");
            metric.setTipo(DashboardMetric.TipoMetrica.VENDAS_DIARIAS);
            metric.setDataReferencia(data);
            metric.setValorAnterior(valorAnterior);
            metric.setMeta(new BigDecimal("20000.00"));
            metric.setCategoria("Vendas");
            metric.setDescricao("Total de vendas realizadas no dia");
            
            dashboardDAO.save(metric);
            
        } catch (SQLException e) {
            logger.error("Erro ao gerar métrica de vendas diárias", e);
        }
    }
    
    /**
     * Gera métrica de total de clientes
     */
    private void gerarMetricaTotalClientes(LocalDate data) {
        try {
            // Simulação - na implementação real, buscar do banco
            BigDecimal totalClientes = new BigDecimal("1250"); // Exemplo
            BigDecimal clientesNovos = new BigDecimal("25"); // Exemplo
            
            DashboardMetric metric = new DashboardMetric();
            metric.setNome("Total de Clientes");
            metric.setValor(totalClientes);
            metric.setUnidade("clientes");
            metric.setTipo(DashboardMetric.TipoMetrica.TOTAL_CLIENTES);
            metric.setDataReferencia(data);
            metric.setValorAnterior(totalClientes.subtract(clientesNovos));
            metric.setMeta(new BigDecimal("1500"));
            metric.setCategoria("Clientes");
            metric.setDescricao("Total de clientes cadastrados no sistema");
            
            dashboardDAO.save(metric);
            
        } catch (SQLException e) {
            logger.error("Erro ao gerar métrica de total de clientes", e);
        }
    }
    
    /**
     * Gera métrica de produtos vendidos
     */
    private void gerarMetricaProdutosVendidos(LocalDate data) {
        try {
            // Simulação - na implementação real, buscar do banco
            BigDecimal produtosVendidos = new BigDecimal("450"); // Exemplo
            
            DashboardMetric metric = new DashboardMetric();
            metric.setNome("Produtos Vendidos");
            metric.setValor(produtosVendidos);
            metric.setUnidade("unidades");
            metric.setTipo(DashboardMetric.TipoMetrica.PRODUTOS_VENDIDOS);
            metric.setDataReferencia(data);
            metric.setValorAnterior(new BigDecimal("380"));
            metric.setMeta(new BigDecimal("500"));
            metric.setCategoria("Vendas");
            metric.setDescricao("Total de produtos vendidos no dia");
            
            dashboardDAO.save(metric);
            
        } catch (SQLException e) {
            logger.error("Erro ao gerar métrica de produtos vendidos", e);
        }
    }
    
    /**
     * Calcula ticket médio
     */
    public BigDecimal calcularTicketMedio() {
        try {
            List<DashboardMetric> resumo = getResumoFinanceiro();
            BigDecimal faturamento = BigDecimal.ZERO;
            BigDecimal totalVendas = BigDecimal.ZERO;
            
            for (DashboardMetric metric : resumo) {
                if (metric.getTipo() == DashboardMetric.TipoMetrica.FATURAMENTO) {
                    faturamento = metric.getValor();
                } else if (metric.getTipo() == DashboardMetric.TipoMetrica.VENDAS_MENSAIS) {
                    totalVendas = metric.getValor();
                }
            }
            
            if (totalVendas.compareTo(BigDecimal.ZERO) == 0) {
                return BigDecimal.ZERO;
            }
            
            return faturamento.divide(totalVendas, 2, RoundingMode.HALF_UP);
            
        } catch (Exception e) {
            logger.error("Erro ao calcular ticket médio", e);
            return BigDecimal.ZERO;
        }
    }
    
    /**
     * Calcula margem de lucro
     */
    public BigDecimal calcularMargemLucro() {
        try {
            List<DashboardMetric> resumo = getResumoFinanceiro();
            BigDecimal faturamento = BigDecimal.ZERO;
            BigDecimal custos = BigDecimal.ZERO;
            
            for (DashboardMetric metric : resumo) {
                if (metric.getTipo() == DashboardMetric.TipoMetrica.FATURAMENTO) {
                    faturamento = metric.getValor();
                } else if (metric.getTipo() == DashboardMetric.TipoMetrica.CUSTOS) {
                    custos = metric.getValor();
                }
            }
            
            if (faturamento.compareTo(BigDecimal.ZERO) == 0) {
                return BigDecimal.ZERO;
            }
            
            BigDecimal lucro = faturamento.subtract(custos);
            return lucro.divide(faturamento, 4, RoundingMode.HALF_UP)
                       .multiply(new BigDecimal("100"));
            
        } catch (Exception e) {
            logger.error("Erro ao calcular margem de lucro", e);
            return BigDecimal.ZERO;
        }
    }
    
    /**
     * Limpa métricas antigas (manutenção)
     */
    public int limparMetricasAntigas() {
        try {
            int excluidas = dashboardDAO.deleteAntigas();
            logger.info("Limpeza de métricas antigas: {} registros excluídos", excluidas);
            return excluidas;
        } catch (SQLException e) {
            logger.error("Erro ao limpar métricas antigas", e);
            throw new RuntimeException("Falha ao limpar métricas antigas", e);
        }
    }
    
    /**
     * Inicializa tabela de métricas
     */
    public void inicializarTabela() {
        try {
            dashboardDAO.createTableIfNotExists();
            logger.info("Tabela de métricas inicializada com sucesso");
        } catch (SQLException e) {
            logger.error("Erro ao inicializar tabela de métricas", e);
            throw new RuntimeException("Falha ao inicializar tabela de métricas", e);
        }
    }
    
    /**
     * Gera métricas de exemplo para demonstração
     */
    public void gerarMetricasExemplo() {
        LocalDate hoje = LocalDate.now();
        
        try {
            // Verificar se já existem métricas para hoje
            List<DashboardMetric> existentes = dashboardDAO.findByPeriodo(hoje, hoje);
            if (!existentes.isEmpty()) {
                logger.info("Métricas de exemplo já existem para hoje: " + existentes.size() + " registros");
                return;
            }
            
            // Vendas diárias
            DashboardMetric vendas = new DashboardMetric("Vendas Diárias", new BigDecimal("18500.00"), 
                                                        DashboardMetric.TipoMetrica.VENDAS_DIARIAS);
            vendas.setUnidade("R$");
            vendas.setDataReferencia(hoje);
            vendas.setValorAnterior(new BigDecimal("15000.00"));
            vendas.setMeta(new BigDecimal("20000.00"));
            vendas.setCategoria("Vendas");
            vendas.setDescricao("Total de vendas realizadas no dia");
            dashboardDAO.save(vendas);
            
            // Total clientes
            DashboardMetric clientes = new DashboardMetric("Total de Clientes", new BigDecimal("1350"), 
                                                          DashboardMetric.TipoMetrica.TOTAL_CLIENTES);
            clientes.setUnidade("clientes");
            clientes.setDataReferencia(hoje);
            clientes.setValorAnterior(new BigDecimal("1325"));
            clientes.setMeta(new BigDecimal("1500"));
            clientes.setCategoria("Clientes");
            clientes.setDescricao("Total de clientes cadastrados no sistema");
            dashboardDAO.save(clientes);
            
            // Produtos vendidos
            DashboardMetric produtos = new DashboardMetric("Produtos Vendidos", new BigDecimal("520"), 
                                                          DashboardMetric.TipoMetrica.PRODUTOS_VENDIDOS);
            produtos.setUnidade("unidades");
            produtos.setDataReferencia(hoje);
            produtos.setValorAnterior(new BigDecimal("380"));
            produtos.setMeta(new BigDecimal("500"));
            produtos.setCategoria("Vendas");
            produtos.setDescricao("Total de produtos vendidos no dia");
            dashboardDAO.save(produtos);
            
            logger.info("Métricas de exemplo geradas com sucesso: 3 registros");
            
        } catch (SQLException e) {
            logger.error("Erro ao gerar métricas de exemplo", e);
            throw new RuntimeException("Falha ao gerar métricas de exemplo", e);
        }
    }
}
