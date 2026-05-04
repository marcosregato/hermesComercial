package com.br.hermescomercial.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Serviço de notificações em tempo real
 * @author Hermes Comercial
 * @version 2.8.0
 */
public class NotificacaoService {
    
    private static NotificacaoService instance;
    private final Map<String, List<Notificacao>> notificacoesPorUsuario;
    private final List<NotificacaoListener> listeners;
    
    public enum TipoNotificacao {
        ESTOQUE_BAIXO,      // Estoque abaixo do mínimo
        ESTOQUE_CRITICO,    // Estoque crítico/esgotado
        VENDA_REALIZADA,    // Nova venda realizada
        META_ALCANCADA,     // Meta de vendas alcançada
        CAIXA_ABERTO,       // Caixa aberto
        CAIXA_FECHADO,      // Caixa fechado
        PROMOCAO_ATIVA,     // Promoção ativada
        PROMOCAO_FINALIZADA, // Promoção finalizada
        ALERTA_FINANCEIRO,  // Alerta financeiro
        SISTEMA,            // Mensagens do sistema
        ERRO                // Erros críticos
    }
    
    public enum PrioridadeNotificacao {
        BAIXA,      // Informação geral
        MEDIA,      // Alerta moderado
        ALTA,       // Alerta importante
        CRITICA     // Alerta crítico/urgente
    }
    
    public interface NotificacaoListener {
        void onNovaNotificacao(Notificacao notificacao);
        void onNotificacaoLida(String idNotificacao);
    }
    
    public static class Notificacao {
        private String id;
        private String idUsuario;
        private String titulo;
        private String mensagem;
        private TipoNotificacao tipo;
        private PrioridadeNotificacao prioridade;
        private LocalDateTime dataCriacao;
        private LocalDateTime dataLeitura;
        private boolean lida;
        private Map<String, Object> dadosAdicionais;
        
        public Notificacao(String idUsuario, String titulo, String mensagem, 
                          TipoNotificacao tipo, PrioridadeNotificacao prioridade) {
            this.id = generateId();
            this.idUsuario = idUsuario;
            this.titulo = titulo;
            this.mensagem = mensagem;
            this.tipo = tipo;
            this.prioridade = prioridade;
            this.dataCriacao = LocalDateTime.now();
            this.lida = false;
            this.dadosAdicionais = new HashMap<>();
        }
        
        private String generateId() {
            return "NOT_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
        }
        
        // Getters e setters
        public String getId() { return id; }
        public String getIdUsuario() { return idUsuario; }
        public String getTitulo() { return titulo; }
        public String getMensagem() { return mensagem; }
        public TipoNotificacao getTipo() { return tipo; }
        public PrioridadeNotificacao getPrioridade() { return prioridade; }
        public LocalDateTime getDataCriacao() { return dataCriacao; }
        public LocalDateTime getDataLeitura() { return dataLeitura; }
        public boolean isLida() { return lida; }
        public Map<String, Object> getDadosAdicionais() { return dadosAdicionais; }
        
        public void marcarComoLida() {
            this.lida = true;
            this.dataLeitura = LocalDateTime.now();
        }
        
        public void adicionarDado(String chave, Object valor) {
            this.dadosAdicionais.put(chave, valor);
        }
    }
    
    private NotificacaoService() {
        this.notificacoesPorUsuario = new ConcurrentHashMap<>();
        this.listeners = new ArrayList<>();
    }
    
    public static synchronized NotificacaoService getInstance() {
        if (instance == null) {
            instance = new NotificacaoService();
        }
        return instance;
    }
    
    /**
     * Adiciona listener para receber notificações
     */
    public void addListener(NotificacaoListener listener) {
        listeners.add(listener);
    }
    
    /**
     * Remove listener
     */
    public void removeListener(NotificacaoListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * Envia notificação para usuário específico
     */
    public void enviarNotificacao(String idUsuario, String titulo, String mensagem, 
                                 TipoNotificacao tipo, PrioridadeNotificacao prioridade) {
        Notificacao notificacao = new Notificacao(idUsuario, titulo, mensagem, tipo, prioridade);
        
        // Adicionar às notificações do usuário
        notificacoesPorUsuario.computeIfAbsent(idUsuario, k -> new ArrayList<>()).add(notificacao);
        
        // Notificar listeners
        for (NotificacaoListener listener : listeners) {
            listener.onNovaNotificacao(notificacao);
        }
        
        // Limitar número de notificações (manter apenas 100 mais recentes)
        List<Notificacao> notifsUsuario = notificacoesPorUsuario.get(idUsuario);
        if (notifsUsuario.size() > 100) {
            notifsUsuario.remove(0);
        }
    }
    
    /**
     * Envia notificação para todos os usuários
     */
    public void enviarNotificacaoGlobal(String titulo, String mensagem, 
                                       TipoNotificacao tipo, PrioridadeNotificacao prioridade) {
        // Para todos os usuários ativos (simulação)
        String[] usuariosAtivos = {"admin", "operador1", "operador2", "gerente"};
        
        for (String usuario : usuariosAtivos) {
            enviarNotificacao(usuario, titulo, mensagem, tipo, prioridade);
        }
    }
    
    /**
     * Notificação de estoque baixo
     */
    public void notificarEstoqueBaixo(String codigoProduto, String nomeProduto, int estoqueAtual, int estoqueMinimo) {
        String titulo = "⚠️ ESTOQUE BAIXO";
        String mensagem = String.format("Produto %s (%s) com estoque baixo.\nAtual: %d | Mínimo: %d", 
                                       nomeProduto, codigoProduto, estoqueAtual, estoqueMinimo);
        
        Notificacao notificacao = new Notificacao("admin", titulo, mensagem, 
                                                 TipoNotificacao.ESTOQUE_BAIXO, PrioridadeNotificacao.MEDIA);
        notificacao.adicionarDado("codigoProduto", codigoProduto);
        notificacao.adicionarDado("nomeProduto", nomeProduto);
        notificacao.adicionarDado("estoqueAtual", estoqueAtual);
        notificacao.adicionarDado("estoqueMinimo", estoqueMinimo);
        
        adicionarNotificacaoPersonalizada(notificacao);
    }
    
    /**
     * Notificação de estoque crítico
     */
    public void notificarEstoqueCritico(String codigoProduto, String nomeProduto, int estoqueAtual) {
        String titulo = "🔴 ESTOQUE CRÍTICO";
        String mensagem = String.format("Produto %s (%s) ESGOTADO ou quase esgotado.\nEstoque atual: %d unidades", 
                                       nomeProduto, codigoProduto, estoqueAtual);
        
        Notificacao notificacao = new Notificacao("admin", titulo, mensagem, 
                                                 TipoNotificacao.ESTOQUE_CRITICO, PrioridadeNotificacao.ALTA);
        notificacao.adicionarDado("codigoProduto", codigoProduto);
        notificacao.adicionarDado("nomeProduto", nomeProduto);
        notificacao.adicionarDado("estoqueAtual", estoqueAtual);
        
        adicionarNotificacaoPersonalizada(notificacao);
    }
    
    /**
     * Notificação de venda realizada
     */
    public void notificarVendaRealizada(String idVenda, BigDecimal valorTotal, String nomeCliente, int quantidadeItens) {
        String titulo = "💰 VENDA REALIZADA";
        String mensagem = String.format("Venda #%s concluída com sucesso.\nValor: R$ %.2f | Cliente: %s | Itens: %d", 
                                       idVenda, valorTotal, nomeCliente, quantidadeItens);
        
        Notificacao notificacao = new Notificacao("admin", titulo, mensagem, 
                                                 TipoNotificacao.VENDA_REALIZADA, PrioridadeNotificacao.BAIXA);
        notificacao.adicionarDado("idVenda", idVenda);
        notificacao.adicionarDado("valorTotal", valorTotal);
        notificacao.adicionarDado("nomeCliente", nomeCliente);
        notificacao.adicionarDado("quantidadeItens", quantidadeItens);
        
        adicionarNotificacaoPersonalizada(notificacao);
    }
    
    /**
     * Notificação de meta alcançada
     */
    public void notificarMetaAlcancada(String tipoMeta, BigDecimal valorMeta, BigDecimal valorAlcancado, String periodo) {
        String titulo = "🎯 META ALCANÇADA!";
        String mensagem = String.format("Meta de %s alcançada!\nMeta: R$ %.2f | Alcançado: R$ %.2f | Período: %s", 
                                       tipoMeta, valorMeta, valorAlcancado, periodo);
        
        Notificacao notificacao = new Notificacao("admin", titulo, mensagem, 
                                                 TipoNotificacao.META_ALCANCADA, PrioridadeNotificacao.MEDIA);
        notificacao.adicionarDado("tipoMeta", tipoMeta);
        notificacao.adicionarDado("valorMeta", valorMeta);
        notificacao.adicionarDado("valorAlcancado", valorAlcancado);
        notificacao.adicionarDado("periodo", periodo);
        
        adicionarNotificacaoPersonalizada(notificacao);
    }
    
    /**
     * Notificação de caixa aberto/fechado
     */
    public void notificarOperacaoCaixa(String operacao, String operador, BigDecimal valorTotal) {
        String titulo = operacao.equals("ABERTO") ? "📋 CAIXA ABERTO" : "🔒 CAIXA FECHADO";
        String mensagem = String.format("Caixa %s por %s.\nValor total: R$ %.2f", 
                                       operacao.toLowerCase(), operador, valorTotal);
        
        TipoNotificacao tipo = operacao.equals("ABERTO") ? TipoNotificacao.CAIXA_ABERTO : TipoNotificacao.CAIXA_FECHADO;
        
        Notificacao notificacao = new Notificacao("admin", titulo, mensagem, 
                                                 tipo, PrioridadeNotificacao.BAIXA);
        notificacao.adicionarDado("operacao", operacao);
        notificacao.adicionarDado("operador", operador);
        notificacao.adicionarDado("valorTotal", valorTotal);
        
        adicionarNotificacaoPersonalizada(notificacao);
    }
    
    /**
     * Notificação de promoção
     */
    public void notificarPromocao(String acao, String nomeProduto, BigDecimal desconto, String periodo) {
        String titulo = acao.equals("ATIVA") ? "🎉 PROMOÇÃO ATIVADA" : "⏰ PROMOÇÃO FINALIZADA";
        String acaoTexto = acao.toLowerCase();
        String mensagem = String.format("Promoção %s para %s.\nDesconto: %.1f%% | Período: %s", 
                                       acaoTexto, nomeProduto, desconto, periodo);
        
        TipoNotificacao tipo = acao.equals("ATIVA") ? TipoNotificacao.PROMOCAO_ATIVA : TipoNotificacao.PROMOCAO_FINALIZADA;
        
        Notificacao notificacao = new Notificacao("admin", titulo, mensagem, 
                                                 tipo, PrioridadeNotificacao.MEDIA);
        notificacao.adicionarDado("acao", acao);
        notificacao.adicionarDado("nomeProduto", nomeProduto);
        notificacao.adicionarDado("desconto", desconto);
        notificacao.adicionarDado("periodo", periodo);
        
        adicionarNotificacaoPersonalizada(notificacao);
    }
    
    /**
     * Notificação de alerta financeiro
     */
    public void notificarAlertaFinanceiro(String tipoAlerta, BigDecimal valor, String descricao) {
        String titulo = "⚠️ ALERTA FINANCEIRO";
        String mensagem = String.format("%s: %s\nValor: R$ %.2f", tipoAlerta, descricao, valor);
        
        Notificacao notificacao = new Notificacao("admin", titulo, mensagem, 
                                                 TipoNotificacao.ALERTA_FINANCEIRO, PrioridadeNotificacao.ALTA);
        notificacao.adicionarDado("tipoAlerta", tipoAlerta);
        notificacao.adicionarDado("valor", valor);
        notificacao.adicionarDado("descricao", descricao);
        
        adicionarNotificacaoPersonalizada(notificacao);
    }
    
    /**
     * Notificação de erro do sistema
     */
    public void notificarErro(String componente, String erro, String detalhes) {
        String titulo = "❌ ERRO DO SISTEMA";
        String mensagem = String.format("Erro no componente: %s\n%s\n\nDetalhes: %s", 
                                       componente, erro, detalhes);
        
        Notificacao notificacao = new Notificacao("admin", titulo, mensagem, 
                                                 TipoNotificacao.ERRO, PrioridadeNotificacao.CRITICA);
        notificacao.adicionarDado("componente", componente);
        notificacao.adicionarDado("erro", erro);
        notificacao.adicionarDado("detalhes", detalhes);
        
        adicionarNotificacaoPersonalizada(notificacao);
    }
    
    /**
     * Adiciona notificação personalizada
     */
    private void adicionarNotificacaoPersonalizada(Notificacao notificacao) {
        notificacoesPorUsuario.computeIfAbsent(notificacao.getIdUsuario(), k -> new ArrayList<>()).add(notificacao);
        
        // Notificar listeners
        for (NotificacaoListener listener : listeners) {
            listener.onNovaNotificacao(notificacao);
        }
        
        // Limitar notificações
        List<Notificacao> notifsUsuario = notificacoesPorUsuario.get(notificacao.getIdUsuario());
        if (notifsUsuario.size() > 100) {
            notifsUsuario.remove(0);
        }
    }
    
    /**
     * Obtém notificações do usuário
     */
    public List<Notificacao> getNotificacoesUsuario(String idUsuario) {
        return notificacoesPorUsuario.getOrDefault(idUsuario, new ArrayList<>());
    }
    
    /**
     * Obtém notificações não lidas do usuário
     */
    public List<Notificacao> getNotificacoesNaoLidas(String idUsuario) {
        List<Notificacao> todas = getNotificacoesUsuario(idUsuario);
        List<Notificacao> naoLidas = new ArrayList<>();
        
        for (Notificacao notificacao : todas) {
            if (!notificacao.isLida()) {
                naoLidas.add(notificacao);
            }
        }
        
        return naoLidas;
    }
    
    /**
     * Marca notificação como lida
     */
    public void marcarComoLida(String idNotificacao) {
        for (List<Notificacao> notificacoes : notificacoesPorUsuario.values()) {
            for (Notificacao notificacao : notificacoes) {
                if (notificacao.getId().equals(idNotificacao)) {
                    notificacao.marcarComoLida();
                    
                    // Notificar listeners
                    for (NotificacaoListener listener : listeners) {
                        listener.onNotificacaoLida(idNotificacao);
                    }
                    return;
                }
            }
        }
    }
    
    /**
     * Marca todas as notificações do usuário como lidas
     */
    public void marcarTodasComoLidas(String idUsuario) {
        List<Notificacao> notificacoes = getNotificacoesUsuario(idUsuario);
        for (Notificacao notificacao : notificacoes) {
            if (!notificacao.isLida()) {
                notificacao.marcarComoLida();
            }
        }
    }
    
    /**
     * Obtém estatísticas de notificações
     */
    public Map<String, Object> getEstatisticasNotificacoes(String idUsuario) {
        Map<String, Object> estatisticas = new HashMap<>();
        
        List<Notificacao> todas = getNotificacoesUsuario(idUsuario);
        List<Notificacao> naoLidas = getNotificacoesNaoLidas(idUsuario);
        
        // Contagem por tipo
        Map<TipoNotificacao, Integer> contagemPorTipo = new HashMap<>();
        Map<PrioridadeNotificacao, Integer> contagemPorPrioridade = new HashMap<>();
        
        for (Notificacao notificacao : todas) {
            contagemPorTipo.merge(notificacao.getTipo(), 1, Integer::sum);
            contagemPorPrioridade.merge(notificacao.getPrioridade(), 1, Integer::sum);
        }
        
        estatisticas.put("totalNotificacoes", todas.size());
        estatisticas.put("naoLidas", naoLidas.size());
        estatisticas.put("lidas", todas.size() - naoLidas.size());
        estatisticas.put("contagemPorTipo", contagemPorTipo);
        estatisticas.put("contagemPorPrioridade", contagemPorPrioridade);
        
        // Últimas notificações
        if (!todas.isEmpty()) {
            Notificacao ultima = todas.get(todas.size() - 1);
            estatisticas.put("ultimaNotificacao", ultima.getMensagem());
            estatisticas.put("dataUltimaNotificacao", ultima.getDataCriacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        }
        
        return estatisticas;
    }
    
    /**
     * Limpa notificações antigas (mais de 30 dias)
     */
    public void limparNotificacoesAntigas() {
        LocalDateTime limite = LocalDateTime.now().minusDays(30);
        
        for (Map.Entry<String, List<Notificacao>> entry : notificacoesPorUsuario.entrySet()) {
            List<Notificacao> notificacoes = entry.getValue();
            notificacoes.removeIf(notificacao -> notificacao.getDataCriacao().isBefore(limite));
        }
    }
}
