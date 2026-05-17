package com.br.hermescomercial.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Serviço de busca avançada com filtros
 * @author Hermes Comercial
 * @version 2.8.0
 */
public class BuscaAvancadaService {
    
    public enum TipoBusca {
        PRODUTOS, CLIENTES, VENDAS, FORNECEDORES, FINANCEIRO, ESTOQUE
    }
    
    public enum OperadorComparacao {
        IGUAL, MAIOR_IGUAL, MENOR_IGUAL, MAIOR, MENOR, DIFERENTE, CONTEM, COMECA_COM, TERMINA_COM
    }
    
    public static class FiltroBusca {
        private String campo;
        private Object valor;
        private OperadorComparacao operador;
        private TipoBusca tipoBusca;
        
        public FiltroBusca(String campo, Object valor, OperadorComparacao operador, TipoBusca tipoBusca) {
            this.campo = campo;
            this.valor = valor;
            this.operador = operador;
            this.tipoBusca = tipoBusca;
        }
        
        // Getters
        public String getCampo() { return campo; }
        public Object getValor() { return valor; }
        public OperadorComparacao getOperador() { return operador; }
        public TipoBusca getTipoBusca() { return tipoBusca; }
    }
    
    public static class ResultadoBusca {
        private TipoBusca tipoBusca;
        private List<Map<String, Object>> resultados;
        private int totalRegistros;
        private long tempoExecucao;
        private List<String> sugestoes;
        
        public ResultadoBusca(TipoBusca tipoBusca) {
            this.tipoBusca = tipoBusca;
            this.resultados = new ArrayList<>();
            this.sugestoes = new ArrayList<>();
        }
        
        // Getters e setters
        public TipoBusca getTipoBusca() { return tipoBusca; }
        public List<Map<String, Object>> getResultados() { return resultados; }
        public void setResultados(List<Map<String, Object>> resultados) { 
            this.resultados = resultados; 
            this.totalRegistros = resultados.size();
        }
        public int getTotalRegistros() { return totalRegistros; }
        public long getTempoExecucao() { return tempoExecucao; }
        public void setTempoExecucao(long tempoExecucao) { this.tempoExecucao = tempoExecucao; }
        public List<String> getSugestoes() { return sugestoes; }
        public void adicionarSugestao(String sugestao) { this.sugestoes.add(sugestao); }
    }
    
    /**
     * Realiza busca avançada com múltiplos filtros
     */
    public ResultadoBusca buscarComFiltros(TipoBusca tipoBusca, List<FiltroBusca> filtros) {
        long inicio = System.currentTimeMillis();
        ResultadoBusca resultado = new ResultadoBusca(tipoBusca);
        
        try {
            List<Map<String, Object>> dados = obterDadosParaBusca(tipoBusca);
            List<Map<String, Object>> dadosFiltrados = new ArrayList<>();
            
            for (Map<String, Object> registro : dados) {
                boolean atendeTodosFiltros = true;
                
                for (FiltroBusca filtro : filtros) {
                    if (!atendeFiltro(registro, filtro)) {
                        atendeTodosFiltros = false;
                        break;
                    }
                }
                
                if (atendeTodosFiltros) {
                    dadosFiltrados.add(registro);
                }
            }
            
            resultado.setResultados(dadosFiltrados);
            resultado.setTempoExecucao(System.currentTimeMillis() - inicio);
            
            // Gerar sugestões baseadas nos resultados
            gerarSugestoes(resultado, tipoBusca, dadosFiltrados);
            
        } catch (Exception e) {
            resultado.adicionarSugestao("Erro na busca: " + e.getMessage());
        }
        
        return resultado;
    }
    
    /**
     * Busca por texto livre
     */
    public ResultadoBusca buscarPorTexto(TipoBusca tipoBusca, String textoBusca) {
        long inicio = System.currentTimeMillis();
        ResultadoBusca resultado = new ResultadoBusca(tipoBusca);
        
        try {
            List<Map<String, Object>> dados = obterDadosParaBusca(tipoBusca);
            List<Map<String, Object>> dadosFiltrados = new ArrayList<>();
            
            String textoLower = textoBusca.toLowerCase().trim();
            
            for (Map<String, Object> registro : dados) {
                if (contemTexto(registro, textoLower)) {
                    dadosFiltrados.add(registro);
                }
            }
            
            resultado.setResultados(dadosFiltrados);
            resultado.setTempoExecucao(System.currentTimeMillis() - inicio);
            
            // Gerar sugestões
            gerarSugestoes(resultado, tipoBusca, dadosFiltrados);
            
        } catch (Exception e) {
            resultado.adicionarSugestao("Erro na busca: " + e.getMessage());
        }
        
        return resultado;
    }
    
    /**
     * Busca por período de datas
     */
    public ResultadoBusca buscarPorPeriodo(TipoBusca tipoBusca, String campoData, 
                                        LocalDate dataInicio, LocalDate dataFim) {
        long inicio = System.currentTimeMillis();
        ResultadoBusca resultado = new ResultadoBusca(tipoBusca);
        
        try {
            List<Map<String, Object>> dados = obterDadosParaBusca(tipoBusca);
            List<Map<String, Object>> dadosFiltrados = new ArrayList<>();
            
            for (Map<String, Object> registro : dados) {
                Object valorData = registro.get(campoData);
                
                if (valorData instanceof LocalDate) {
                    LocalDate data = (LocalDate) valorData;
                    if (!data.isBefore(dataInicio) && !data.isAfter(dataFim)) {
                        dadosFiltrados.add(registro);
                    }
                } else if (valorData instanceof LocalDateTime) {
                    LocalDateTime dataHora = (LocalDateTime) valorData;
                    LocalDate data = dataHora.toLocalDate();
                    if (!data.isBefore(dataInicio) && !data.isAfter(dataFim)) {
                        dadosFiltrados.add(registro);
                    }
                }
            }
            
            resultado.setResultados(dadosFiltrados);
            resultado.setTempoExecucao(System.currentTimeMillis() - inicio);
            
        } catch (Exception e) {
            resultado.adicionarSugestao("Erro na busca por período: " + e.getMessage());
        }
        
        return resultado;
    }
    
    /**
     * Busca por faixa de valores
     */
    public ResultadoBusca buscarPorFaixaValores(TipoBusca tipoBusca, String campoValor, 
                                             BigDecimal valorMin, BigDecimal valorMax) {
        long inicio = System.currentTimeMillis();
        ResultadoBusca resultado = new ResultadoBusca(tipoBusca);
        
        try {
            List<Map<String, Object>> dados = obterDadosParaBusca(tipoBusca);
            List<Map<String, Object>> dadosFiltrados = new ArrayList<>();
            
            for (Map<String, Object> registro : dados) {
                Object valor = registro.get(campoValor);
                
                if (valor instanceof Number) {
                    BigDecimal valorNumerico = BigDecimal.valueOf(((Number) valor).doubleValue());
                    
                    if (valorNumerico.compareTo(valorMin) >= 0 && valorNumerico.compareTo(valorMax) <= 0) {
                        dadosFiltrados.add(registro);
                    }
                }
            }
            
            resultado.setResultados(dadosFiltrados);
            resultado.setTempoExecucao(System.currentTimeMillis() - inicio);
            
        } catch (Exception e) {
            resultado.adicionarSugestao("Erro na busca por faixa de valores: " + e.getMessage());
        }
        
        return resultado;
    }
    
    /**
     * Obtém sugestões de busca rápida
     */
    public List<String> getSugestoesBuscaRapida(TipoBusca tipoBusca) {
        List<String> sugestoes = new ArrayList<>();
        
        switch (tipoBusca) {
            case PRODUTOS:
                sugestoes.add("Produtos com estoque baixo");
                sugestoes.add("Produtos acima de R$ 100,00");
                sugestoes.add("Produtos da categoria Eletrônicos");
                sugestoes.add("Produtos sem estoque");
                break;
                
            case CLIENTES:
                sugestoes.add("Clientes de São Paulo");
                sugestoes.add("Clientes com compras recentes");
                sugestoes.add("Clientes inativos há mais de 90 dias");
                break;
                
            case VENDAS:
                sugestoes.add("Vendas do último mês");
                sugestoes.add("Vendas acima de R$ 1.000,00");
                sugestoes.add("Vendas canceladas");
                break;
                
            case FORNECEDORES:
                sugestoes.add("Fornecedores ativos");
                sugestoes.add("Fornecedores de São Paulo");
                sugestoes.add("Fornecedores sem compras recentes");
                break;
                
            case FINANCEIRO:
                sugestoes.add("Contas a pagar vencidas");
                sugestoes.add("Contas a receber em atraso");
                sugestoes.add("Despesas do mês atual");
                break;
                
            case ESTOQUE:
                sugestoes.add("Itens com estoque crítico");
                sugestoes.add("Movimentações da semana");
                sugestoes.add("Produtos com baixo giro");
                break;
        }
        
        return sugestoes;
    }
    
    /**
     * Obtém estatísticas da busca
     */
    public Map<String, Object> getEstatisticasBusca(TipoBusca tipoBusca) {
        Map<String, Object> estatisticas = new HashMap<>();
        
        // Simulação de estatísticas
        estatisticas.put("totalRegistros", getTotalRegistros(tipoBusca));
        estatisticas.put("camposDisponiveis", getCamposDisponiveis(tipoBusca));
        estatisticas.put("buscasRecentes", getBuscasRecentes(tipoBusca));
        estatisticas.put("filtrosMaisUsados", getFiltrosMaisUsados(tipoBusca));
        
        return estatisticas;
    }
    
    // Métodos auxiliares
    private boolean atendeFiltro(Map<String, Object> registro, FiltroBusca filtro) {
        Object valorCampo = registro.get(filtro.getCampo());
        Object valorFiltro = filtro.getValor();
        
        if (valorCampo == null || valorFiltro == null) {
            return false;
        }
        
        switch (filtro.getOperador()) {
            case IGUAL:
                return valorCampo.toString().equals(valorFiltro.toString());
                
            case CONTEM:
                return valorCampo.toString().toLowerCase().contains(valorFiltro.toString().toLowerCase());
                
            case COMECA_COM:
                return valorCampo.toString().toLowerCase().startsWith(valorFiltro.toString().toLowerCase());
                
            case TERMINA_COM:
                return valorCampo.toString().toLowerCase().endsWith(valorFiltro.toString().toLowerCase());
                
            case MAIOR:
                if (valorCampo instanceof Number && valorFiltro instanceof Number) {
                    return ((Number) valorCampo).doubleValue() > ((Number) valorFiltro).doubleValue();
                }
                return false;
                
            case MENOR:
                if (valorCampo instanceof Number && valorFiltro instanceof Number) {
                    return ((Number) valorCampo).doubleValue() < ((Number) valorFiltro).doubleValue();
                }
                return false;
                
            case MAIOR_IGUAL:
                if (valorCampo instanceof Number && valorFiltro instanceof Number) {
                    return ((Number) valorCampo).doubleValue() >= ((Number) valorFiltro).doubleValue();
                }
                return false;
                
            case MENOR_IGUAL:
                if (valorCampo instanceof Number && valorFiltro instanceof Number) {
                    return ((Number) valorCampo).doubleValue() <= ((Number) valorFiltro).doubleValue();
                }
                return false;
                
            case DIFERENTE:
                return !valorCampo.toString().equals(valorFiltro.toString());
                
            default:
                return false;
        }
    }
    
    private boolean contemTexto(Map<String, Object> registro, String textoBusca) {
        for (Object valor : registro.values()) {
            if (valor != null && valor.toString().toLowerCase().contains(textoBusca)) {
                return true;
            }
        }
        return false;
    }
    
    private void gerarSugestoes(ResultadoBusca resultado, TipoBusca tipoBusca, List<Map<String, Object>> dadosFiltrados) {
        if (dadosFiltrados.isEmpty()) {
            resultado.adicionarSugestao("Nenhum resultado encontrado. Tente usar termos diferentes ou menos filtros.");
            resultado.adicionarSugestao("Verifique a ortografia dos termos de busca.");
            resultado.adicionarSugestao("Use busca por texto livre para encontrar resultados relacionados.");
        } else {
            resultado.adicionarSugestao("Encontrados " + dadosFiltrados.size() + " resultados em " + resultado.getTempoExecucao() + "ms");
            
            if (dadosFiltrados.size() > 100) {
                resultado.adicionarSugestao("Muitos resultados. Considere adicionar mais filtros para refinar a busca.");
            }
        }
    }
    
    private List<Map<String, Object>> obterDadosParaBusca(TipoBusca tipoBusca) {
        List<Map<String, Object>> dados = new ArrayList<>();
        
        // Simulação de dados - em implementação real, buscaria do banco
        switch (tipoBusca) {
            case PRODUTOS:
                for (int i = 1; i <= 50; i++) {
                    Map<String, Object> produto = new HashMap<>();
                    produto.put("codigo", "PROD" + String.format("%03d", i));
                    produto.put("nome", "Produto " + i);
                    produto.put("descricao", "Descrição do produto " + i);
                    produto.put("preco", 10.50 + i * 5);
                    produto.put("estoque", 100 - i * 2);
                    produto.put("categoria", "Categoria " + (i % 5 + 1));
                    produto.put("marca", "Marca " + (char)('A' + (i - 1) % 5));
                    produto.put("unidade", "UN");
                    produto.put("ativo", i % 4 != 0);
                    dados.add(produto);
                }
                break;
                
            case CLIENTES:
                for (int i = 1; i <= 30; i++) {
                    Map<String, Object> cliente = new HashMap<>();
                    cliente.put("id", "CLI" + String.format("%03d", i));
                    cliente.put("nome", "Cliente " + i);
                    cliente.put("cpf", "123.456.789-" + String.format("%02d", i));
                    cliente.put("email", "cliente" + i + "@email.com");
                    cliente.put("telefone", "(11)9999-" + String.format("%04d", 1000 + i));
                    cliente.put("cidade", i % 3 == 0 ? "São Paulo" : "Rio de Janeiro");
                    cliente.put("estado", i % 3 == 0 ? "SP" : "RJ");
                    cliente.put("dataCadastro", LocalDate.now().minusDays(i * 10));
                    cliente.put("ativo", i % 5 != 0);
                    dados.add(cliente);
                }
                break;
                
            case VENDAS:
                for (int i = 1; i <= 40; i++) {
                    Map<String, Object> venda = new HashMap<>();
                    venda.put("id", "V" + String.format("%04d", i));
                    venda.put("data", LocalDate.now().minusDays(i));
                    venda.put("cliente", "Cliente " + i);
                    venda.put("valor", 100.0 + i * 15);
                    venda.put("status", i % 4 == 0 ? "CANCELADA" : "CONCLUIDA");
                    venda.put("itens", i + 2);
                    venda.put("operador", "Operador " + ((i - 1) % 3 + 1));
                    dados.add(venda);
                }
                break;
                
            default:
                // Outros tipos
                break;
        }
        
        return dados;
    }
    
    private int getTotalRegistros(TipoBusca tipoBusca) {
        switch (tipoBusca) {
            case PRODUTOS: return 150;
            case CLIENTES: return 85;
            case VENDAS: return 320;
            case FORNECEDORES: return 25;
            case FINANCEIRO: return 200;
            case ESTOQUE: return 180;
            default: return 0;
        }
    }
    
    private List<String> getCamposDisponiveis(TipoBusca tipoBusca) {
        List<String> campos = new ArrayList<>();
        
        switch (tipoBusca) {
            case PRODUTOS:
                campos.add("codigo");
                campos.add("nome");
                campos.add("descricao");
                campos.add("preco");
                campos.add("estoque");
                campos.add("categoria");
                campos.add("marca");
                campos.add("ativo");
                break;
                
            case CLIENTES:
                campos.add("nome");
                campos.add("cpf");
                campos.add("email");
                campos.add("telefone");
                campos.add("cidade");
                campos.add("estado");
                campos.add("dataCadastro");
                campos.add("ativo");
                break;
                
            case VENDAS:
                campos.add("id");
                campos.add("data");
                campos.add("cliente");
                campos.add("valor");
                campos.add("status");
                campos.add("itens");
                campos.add("operador");
                break;
                
            default:
                break;
        }
        
        return campos;
    }
    
    private List<String> getBuscasRecentes(TipoBusca tipoBusca) {
        List<String> recentes = new ArrayList<>();
        
        // Simulação de buscas recentes
        switch (tipoBusca) {
            case PRODUTOS:
                recentes.add("estoque baixo");
                recentes.add("categoria Eletrônicos");
                recentes.add("preço > 100");
                break;
            case CLIENTES:
                recentes.add("São Paulo");
                recentes.add("ativos");
                recentes.add("dataCadastro > 2024-01-01");
                break;
            default:
                break;
        }
        
        return recentes;
    }
    
    private Map<String, Integer> getFiltrosMaisUsados(TipoBusca tipoBusca) {
        Map<String, Integer> filtros = new HashMap<>();
        
        // Simulação de filtros mais usados
        filtros.put("categoria", 45);
        filtros.put("preco", 38);
        filtros.put("estoque", 32);
        filtros.put("ativo", 28);
        filtros.put("data", 25);
        
        return filtros;
    }
}
