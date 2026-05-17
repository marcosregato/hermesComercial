package com.br.hermescomercial.service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Serviço de internacionalização e suporte a múltiplas linguagens
 * @author Hermes Comercial
 * @version 2.8.0
 */
public class InternacionalizacaoService {
    
    private static InternacionalizacaoService instance;
    private Locale localeAtual;
    private final Map<String, Map<String, String>> traducoes;
    private final Map<String, ResourceBundle> bundles;
    
    public enum Idioma {
        PORTUGUES_BRASIL("pt", "BR", "Português (Brasil)"),
        PORTUGUES_PORTUGAL("pt", "PT", "Português (Portugal)"),
        INGLES("en", "US", "English"),
        ESPANHOL("es", "ES", "Español"),
        FRANCES("fr", "FR", "Français"),
        ALEMAO("de", "DE", "Deutsch"),
        ITALIANO("it", "IT", "Italiano");
        
        private final String codigo;
        private final String pais;
        private final String nome;
        
        Idioma(String codigo, String pais, String nome) {
            this.codigo = codigo;
            this.pais = pais;
            this.nome = nome;
        }
        
        public String getCodigo() { return codigo; }
        public String getPais() { return pais; }
        public String getNome() { return nome; }
        
        public Locale getLocale() {
            @SuppressWarnings("deprecation")
            Locale locale = new Locale(codigo, pais);
            return locale;
        }
    }
    
    private InternacionalizacaoService() {
        this.localeAtual = Idioma.PORTUGUES_BRASIL.getLocale();
        this.traducoes = new ConcurrentHashMap<>();
        this.bundles = new ConcurrentHashMap<>();
        inicializarTraducoes();
    }
    
    public static synchronized InternacionalizacaoService getInstance() {
        if (instance == null) {
            instance = new InternacionalizacaoService();
        }
        return instance;
    }
    
    /**
     * Define o idioma atual
     */
    public void setIdioma(Idioma idioma) {
        this.localeAtual = idioma.getLocale();
        carregarBundle(idioma);
    }
    
    /**
     * Obtém o idioma atual
     */
    public Idioma getIdiomaAtual() {
        for (Idioma idioma : Idioma.values()) {
            if (idioma.getLocale().equals(localeAtual)) {
                return idioma;
            }
        }
        return Idioma.PORTUGUES_BRASIL;
    }
    
    /**
     * Obtém tradução para uma chave
     */
    public String traduzir(String chave) {
        return traduzir(chave, new Object[0]);
    }
    
    /**
     * Obtém tradução para uma chave com parâmetros
     */
    public String traduzir(String chave, Object... parametros) {
        // Primeiro tentar usar ResourceBundle
        ResourceBundle bundle = bundles.get(localeAtual.toString());
        if (bundle != null && bundle.containsKey(chave)) {
            String mensagem = bundle.getString(chave);
            return formatarMensagem(mensagem, parametros);
        }
        
        // Fallback para mapa de traduções
        Map<String, String> tradIdioma = traducoes.get(localeAtual.toString());
        if (tradIdioma != null && tradIdioma.containsKey(chave)) {
            String mensagem = tradIdioma.get(chave);
            return formatarMensagem(mensagem, parametros);
        }
        
        // Fallback para português
        Map<String, String> tradPT = traducoes.get(Idioma.PORTUGUES_BRASIL.getLocale().toString());
        if (tradPT != null && tradPT.containsKey(chave)) {
            String mensagem = tradPT.get(chave);
            return formatarMensagem(mensagem, parametros);
        }
        
        // Retornar a chave se não encontrar tradução
        return chave;
    }
    
    /**
     * Verifica se uma chave de tradução existe
     */
    public boolean existeTraducao(String chave) {
        ResourceBundle bundle = bundles.get(localeAtual.toString());
        return bundle != null && bundle.containsKey(chave);
    }
    
    /**
     * Obtém todas as chaves disponíveis
     */
    public Map<String, String> getTodasTraducoes() {
        Map<String, String> resultado = new HashMap<>();
        
        ResourceBundle bundle = bundles.get(localeAtual.toString());
        if (bundle != null) {
            for (String chave : bundle.keySet()) {
                resultado.put(chave, bundle.getString(chave));
            }
        }
        
        return resultado;
    }
    
    /**
     * Lista todos os idiomas disponíveis
     */
    public Idioma[] getIdiomasDisponiveis() {
        return Idioma.values();
    }
    
    /**
     * Obtém informações do idioma atual
     */
    public Map<String, Object> getInformacoesIdioma() {
        Map<String, Object> info = new HashMap<>();
        Idioma idiomaAtual = getIdiomaAtual();
        
        info.put("codigo", idiomaAtual.getCodigo());
        info.put("pais", idiomaAtual.getPais());
        info.put("nome", idiomaAtual.getNome());
        info.put("locale", localeAtual.toString());
        info.put("displayLanguage", localeAtual.getDisplayLanguage(localeAtual));
        info.put("displayCountry", localeAtual.getDisplayCountry(localeAtual));
        
        return info;
    }
    
    /**
     * Formata data de acordo com o idioma
     */
    public String formatarData(java.time.LocalDate data) {
        return java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                            .withLocale(localeAtual)
                                            .format(data);
    }
    
    /**
     * Formata data e hora de acordo com o idioma
     */
    public String formatarDataHora(java.time.LocalDateTime dataHora) {
        return java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                                            .withLocale(localeAtual)
                                            .format(dataHora);
    }
    
    /**
     * Formata valor monetário de acordo com o idioma
     */
    public String formatarMoeda(java.math.BigDecimal valor) {
        java.text.NumberFormat format = java.text.NumberFormat.getCurrencyInstance(localeAtual);
        return format.format(valor);
    }
    
    /**
     * Formata número de acordo com o idioma
     */
    public String formatarNumero(Number numero) {
        java.text.NumberFormat format = java.text.NumberFormat.getNumberInstance(localeAtual);
        return format.format(numero);
    }
    
    /**
     * Obtém tradução específica para o módulo PDV
     */
    public String traduzirPDV(String chave) {
        return traduzir("pdv." + chave);
    }
    
    /**
     * Obtém tradução específica para o módulo Dashboard
     */
    public String traduzirDashboard(String chave) {
        return traduzir("dashboard." + chave);
    }
    
    /**
     * Obtém tradução específica para o módulo Financeiro
     */
    public String traduzirFinanceiro(String chave) {
        return traduzir("financeiro." + chave);
    }
    
    /**
     * Obtém tradução específica para o módulo Estoque
     */
    public String traduzirEstoque(String chave) {
        return traduzir("estoque." + chave);
    }
    
    /**
     * Obtém tradução para mensagens de erro
     */
    public String traduzirErro(String chave) {
        return traduzir("erro." + chave);
    }
    
    /**
     * Obtém tradução para mensagens de sucesso
     */
    public String traduzirSucesso(String chave) {
        return traduzir("sucesso." + chave);
    }
    
    /**
     * Obtém tradução para mensagens de validação
     */
    public String traduzirValidacao(String chave) {
        return traduzir("validacao." + chave);
    }
    
    /**
     * Recarrega as traduções
     */
    public void recarregarTraducoes() {
        traducoes.clear();
        bundles.clear();
        inicializarTraducoes();
        carregarBundle(getIdiomaAtual());
    }
    
    // Métodos privados
    private void inicializarTraducoes() {
        // Português Brasil
        Map<String, String> ptBR = new HashMap<>();
        ptBR.put("sistema.titulo", "Hermes Comercial PDV");
        ptBR.put("sistema.versao", "Versão 2.8.0");
        ptBR.put("menu.vendas", "Vendas");
        ptBR.put("menu.produtos", "Produtos");
        ptBR.put("menu.clientes", "Clientes");
        ptBR.put("menu.financeiro", "Financeiro");
        ptBR.put("menu.estoque", "Estoque");
        ptBR.put("menu.relatorios", "Relatórios");
        ptBR.put("menu.configuracoes", "Configurações");
        ptBR.put("menu.sair", "Sair");
        
        // PDV
        ptBR.put("pdv.nova_venda", "Nova Venda");
        ptBR.put("pdv.produto", "Produto");
        ptBR.put("pdv.quantidade", "Quantidade");
        ptBR.put("pdv.valor", "Valor");
        ptBR.put("pdv.total", "Total");
        ptBR.put("pdv.desconto", "Desconto");
        ptBR.put("pdv.finalizar", "Finalizar");
        ptBR.put("pdv.cancelar", "Cancelar");
        
        // Dashboard
        ptBR.put("dashboard.titulo", "Dashboard Analytics");
        ptBR.put("dashboard.vendas.hoje", "Vendas Hoje");
        ptBR.put("dashboard.vendas.mes", "Vendas do Mês");
        ptBR.put("dashboard.top.produtos", "Top Produtos");
        ptBR.put("dashboard.faturamento", "Faturamento");
        
        // Financeiro
        ptBR.put("financeiro.contas.pagar", "Contas a Pagar");
        ptBR.put("financeiro.contas.receber", "Contas a Receber");
        ptBR.put("financeiro.fluxo.caixa", "Fluxo de Caixa");
        ptBR.put("financeiro.saldo", "Saldo");
        
        // Estoque
        ptBR.put("estoque.estoque.baixo", "Estoque Baixo");
        ptBR.put("estoque.estoque.critico", "Estoque Crítico");
        ptBR.put("estoque.repor", "Repor Estoque");
        ptBR.put("estoque.movimentacao", "Movimentação");
        
        // Erros
        ptBR.put("erro.conexao.banco", "Erro de conexão com o banco de dados");
        ptBR.put("erro.validacao.campos", "Campos obrigatórios não preenchidos");
        ptBR.put("erro.estoque.insuficiente", "Estoque insuficiente");
        ptBR.put("erro.sistema.geral", "Erro interno do sistema");
        
        // Sucesso
        ptBR.put("sucesso.venda.concluida", "Venda concluída com sucesso");
        ptBR.put("sucesso.dados.salvos", "Dados salvos com sucesso");
        ptBR.put("sucesso.operacao.concluida", "Operação concluída com sucesso");
        
        // Validação
        ptBR.put("validacao.campo.obrigatorio", "Campo obrigatório");
        ptBR.put("validacao.email.invalido", "E-mail inválido");
        ptBR.put("validacao.cpf.invalido", "CPF inválido");
        ptBR.put("validacao.valor.negativo", "Valor não pode ser negativo");
        
        traducoes.put(Idioma.PORTUGUES_BRASIL.getLocale().toString(), ptBR);
        
        // Inglês
        Map<String, String> enUS = new HashMap<>();
        enUS.put("sistema.titulo", "Hermes Commercial POS");
        enUS.put("sistema.versao", "Version 2.8.0");
        enUS.put("menu.vendas", "Sales");
        enUS.put("menu.produtos", "Products");
        enUS.put("menu.clientes", "Customers");
        enUS.put("menu.financeiro", "Financial");
        enUS.put("menu.estoque", "Inventory");
        enUS.put("menu.relatorios", "Reports");
        enUS.put("menu.configuracoes", "Settings");
        enUS.put("menu.sair", "Exit");
        
        // PDV
        enUS.put("pdv.nova_venda", "New Sale");
        enUS.put("pdv.produto", "Product");
        enUS.put("pdv.quantidade", "Quantity");
        enUS.put("pdv.valor", "Value");
        enUS.put("pdv.total", "Total");
        enUS.put("pdv.desconto", "Discount");
        enUS.put("pdv.finalizar", "Finish");
        enUS.put("pdv.cancelar", "Cancel");
        
        // Dashboard
        enUS.put("dashboard.titulo", "Dashboard Analytics");
        enUS.put("dashboard.vendas.hoje", "Today's Sales");
        enUS.put("dashboard.vendas.mes", "Month Sales");
        enUS.put("dashboard.top.produtos", "Top Products");
        enUS.put("dashboard.faturamento", "Revenue");
        
        // Financeiro
        enUS.put("financeiro.contas.pagar", "Accounts Payable");
        enUS.put("financeiro.contas.receber", "Accounts Receivable");
        enUS.put("financeiro.fluxo.caixa", "Cash Flow");
        enUS.put("financeiro.saldo", "Balance");
        
        // Estoque
        enUS.put("estoque.estoque.baixo", "Low Stock");
        enUS.put("estoque.estoque.critico", "Critical Stock");
        enUS.put("estoque.repor", "Replenish Stock");
        enUS.put("estoque.movimentacao", "Movement");
        
        // Erros
        enUS.put("erro.conexao.banco", "Database connection error");
        enUS.put("erro.validacao.campos", "Required fields not filled");
        enUS.put("erro.estoque.insuficiente", "Insufficient stock");
        enUS.put("erro.sistema.geral", "Internal system error");
        
        // Sucesso
        enUS.put("sucesso.venda.concluida", "Sale completed successfully");
        enUS.put("sucesso.dados.salvos", "Data saved successfully");
        enUS.put("sucesso.operacao.concluida", "Operation completed successfully");
        
        // Validação
        enUS.put("validacao.campo.obrigatorio", "Required field");
        enUS.put("validacao.email.invalido", "Invalid email");
        enUS.put("validacao.cpf.invalido", "Invalid CPF");
        enUS.put("validacao.valor.negativo", "Value cannot be negative");
        
        traducoes.put(Idioma.INGLES.getLocale().toString(), enUS);
        
        // Espanhol
        Map<String, String> esES = new HashMap<>();
        esES.put("sistema.titulo", "Hermes Comercial PDV");
        esES.put("sistema.versao", "Versión 2.8.0");
        esES.put("menu.vendas", "Ventas");
        esES.put("menu.produtos", "Productos");
        esES.put("menu.clientes", "Clientes");
        esES.put("menu.financeiro", "Financiero");
        esES.put("menu.estoque", "Inventario");
        esES.put("menu.relatorios", "Informes");
        esES.put("menu.configuracoes", "Configuraciones");
        esES.put("menu.sair", "Salir");
        
        // PDV
        esES.put("pdv.nova_venda", "Nueva Venta");
        esES.put("pdv.produto", "Producto");
        esES.put("pdv.quantidade", "Cantidad");
        esES.put("pdv.valor", "Valor");
        esES.put("pdv.total", "Total");
        esES.put("pdv.desconto", "Descuento");
        esES.put("pdv.finalizar", "Finalizar");
        esES.put("pdv.cancelar", "Cancelar");
        
        traducoes.put(Idioma.ESPANHOL.getLocale().toString(), esES);
    }
    
    private void carregarBundle(Idioma idioma) {
        try {
            // Tentar carregar arquivo de propriedades se existir
            ResourceBundle bundle = ResourceBundle.getBundle("messages", idioma.getLocale());
            bundles.put(idioma.getLocale().toString(), bundle);
        } catch (Exception e) {
            // Se não encontrar arquivo, usa as traduções em memória
            // Bundle já estará disponível através do mapa de traduções
        }
    }
    
    private String formatarMensagem(String mensagem, Object... parametros) {
        if (parametros.length == 0) {
            return mensagem;
        }
        
        // Formatação simples de parâmetros {0}, {1}, etc.
        String resultado = mensagem;
        for (int i = 0; i < parametros.length; i++) {
            resultado = resultado.replace("{" + i + "}", String.valueOf(parametros[i]));
        }
        
        return resultado;
    }
}
