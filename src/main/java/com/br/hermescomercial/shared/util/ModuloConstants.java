package com.br.hermescomercial.shared.util;

/**
 * Constantes para identificação de módulos
 * Versão 2.3.0 - Arquitetura Modular
 */
public class ModuloConstants {
    
    // ==================== NOMES DOS MÓDULOS ====================
    
    public static final String MODULO_PDV = "PDV";
    public static final String MODULO_ERP = "ERP";
    public static final String MODULO_SHARED = "SHARED";
    
    // ==================== VERSÕES ====================
    
    public static final String VERSAO_ATUAL = "2.3.0";
    public static final String VERSAO_API = "v1.0";
    
    // ==================== ENDPOINTS DE API ====================
    
    // PDV -> ERP
    public static final String API_PDV_TO_ERP = "/api/pdv";
    public static final String ENDPOINT_VENDAS = API_PDV_TO_ERP + "/vendas";
    public static final String ENDPOINT_CLIENTES = API_PDV_TO_ERP + "/clientes";
    public static final String ENDPOINT_PAGAMENTOS = API_PDV_TO_ERP + "/pagamentos";
    public static final String ENDPOINT_ESTOQUE = API_PDV_TO_ERP + "/estoque";
    public static final String ENDPOINT_NOTIFICACOES = API_PDV_TO_ERP + "/notificacoes";
    
    // ERP -> PDV
    public static final String API_ERP_TO_PDV = "/api/erp";
    public static final String ENDPOINT_PRODUTOS = API_ERP_TO_PDV + "/produtos";
    public static final String ENDPOINT_PRECOS = API_ERP_TO_PDV + "/precos";
    public static final String ENDPOINT_CLIENTES_ERP = API_ERP_TO_PDV + "/clientes";
    public static final String ENDPOINT_RELATORIOS = API_ERP_TO_PDV + "/relatorios";
    
    // ==================== STATUS DE INTEGRAÇÃO ====================
    
    public static final String STATUS_ONLINE = "ONLINE";
    public static final String STATUS_OFFLINE = "OFFLINE";
    public static final String STATUS_SYNC = "SYNC";
    public static final String STATUS_ERROR = "ERROR";
    
    // ==================== TIPOS DE DADOS ====================
    
    public static final String DADO_PRODUTO = "PRODUTO";
    public static final String DADO_CLIENTE = "CLIENTE";
    public static final String DADO_VENDA = "VENDA";
    public static final String DADO_PAGAMENTO = "PAGAMENTO";
    public static final String DADO_ESTOQUE = "ESTOQUE";
    public static final String DADO_NOTIFICACAO = "NOTIFICACAO";
    
    // ==================== PRIORIDADES ====================
    
    public static final String PRIORIDADE_BAIXA = "BAIXA";
    public static final String PRIORIDADE_MEDIA = "MEDIA";
    public static final String PRIORIDADE_ALTA = "ALTA";
    public static final String PRIORIDADE_URGENTE = "URGENTE";
    
    // ==================== FORMAS DE PAGAMENTO ====================
    
    public static final String PAGAMENTO_DINHEIRO = "DINHEIRO";
    public static final String PAGAMENTO_CARTAO = "CARTAO";
    public static final String PAGAMENTO_PIX = "PIX";
    public static final String PAGAMENTO_BOLETO = "BOLETO";
    public static final String PAGAMENTO_TRANSFERENCIA = "TRANSFERENCIA";
    
    // ==================== TIPOS DE NOTIFICAÇÃO ====================
    
    public static final String NOTIFICACAO_SISTEMA = "SISTEMA";
    public static final String NOTIFICACAO_VENDA = "VENDA";
    public static final String NOTIFICACAO_ESTOQUE = "ESTOQUE";
    public static final String NOTIFICACAO_CLIENTE = "CLIENTE";
    public static final String NOTIFICACAO_FINANCEIRO = "FINANCEIRO";
    
    // ==================== CATEGORIAS ====================
    
    public static final String CATEGORIA_VENDAS = "Vendas";
    public static final String CATEGORIA_FINANCEIRO = "Financeiro";
    public static final String CATEGORIA_CLIENTES = "Clientes";
    public static final String CATEGORIA_ESTOQUE = "Estoque";
    public static final String CATEGORIA_OPERACIONAL = "Operacional";
    public static final String CATEGORIA_PERFORMANCE = "Performance";
    
    // ==================== CONFIGURAÇÕES ====================
    
    public static final int TIMEOUT_CONEXAO = 30000; // 30 segundos
    public static final int MAX_TENTATIVAS = 3;
    public static final int TAMANHO_BATCH = 100;
    public static final long INTERVALO_SYNC = 60000; // 1 minuto
    
    // ==================== BANCOS DE DADOS ====================
    
    public static final String DB_PDV = "hermespdv.db";
    public static final String DB_ERP = "hermeserp.db";
    public static final String DB_SHARED = "hermesshared.db";
    
    // ==================== PORTAS E SERVIÇOS ====================
    
    public static final int PORTA_API_PDV = 8080;
    public static final int PORTA_API_ERP = 8081;
    public static final int PORTA_API_SHARED = 8082;
    
    // ==================== AMBIENTES ====================
    
    public static final String AMBIENTE_DESENVOLVIMENTO = "DEVELOPMENT";
    public static final String AMBIENTE_HOMOLOGACAO = "STAGING";
    public static final String AMBIENTE_PRODUCAO = "PRODUCTION";
    
    // ==================== MENSAGENS ====================
    
    public static final String MSG_ERRO_CONEXAO = "Erro de conexão com o ERP";
    public static final String MSG_SUCESSO_SYNC = "Sincronização realizada com sucesso";
    public static final String MSG_DADOS_OFFLINE = "Trabalhando em modo offline";
    public static final String MSG_INTEGRACAO_ATIVA = "Integração ativa";
    
    // Construtor privado para evitar instanciação
    private ModuloConstants() {
        throw new UnsupportedOperationException("Classe de constantes não pode ser instanciada");
    }
}
