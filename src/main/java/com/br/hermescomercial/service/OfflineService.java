package com.br.hermescomercial.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Serviço de modo offline com sincronização
 * @author Hermes Comercial
 * @version 2.8.0
 */
public class OfflineService {
    
    private static OfflineService instance;
    private boolean modoOffline;
    private LocalDateTime dataUltimaSincronizacao;
    private final Map<String, OperacaoPendente> operacoesPendentes;
    private final ScheduledExecutorService scheduler;
    private final int maxOperacoesPendentes;
    
    public enum TipoOperacao {
        VENDA,           // Venda realizada offline
        ESTOQUE,         // Movimentação de estoque
        CLIENTE,         // Cadastro/alteração de cliente
        PRODUTO,         // Cadastro/alteração de produto
        CONFIGURACAO,    // Alteração de configuração
        PAGAMENTO        // Processamento de pagamento
    }
    
    public enum StatusSincronizacao {
        PENDENTE,        // Aguardando sincronização
        EM_ANDAMENTO,    // Sincronizando
        SINCRONIZADO,    // Sincronizado com sucesso
        ERRO,           // Erro na sincronização
        CONFLITO        // Conflito detectado
    }
    
    public static class OperacaoPendente {
        private String id;
        private TipoOperacao tipo;
        private Map<String, Object> dados;
        private LocalDateTime dataCriacao;
        private LocalDateTime dataSincronizacao;
        private StatusSincronizacao status;
        private String mensagemErro;
        private int tentativas;
        private String idUsuario;
        
        public OperacaoPendente(TipoOperacao tipo, Map<String, Object> dados, String idUsuario) {
            this.id = generateId();
            this.tipo = tipo;
            this.dados = new HashMap<>(dados);
            this.dataCriacao = LocalDateTime.now();
            this.status = StatusSincronizacao.PENDENTE;
            this.tentativas = 0;
            this.idUsuario = idUsuario;
        }
        
        private String generateId() {
            return "OFF_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
        }
        
        // Getters e setters
        public String getId() { return id; }
        public TipoOperacao getTipo() { return tipo; }
        public Map<String, Object> getDados() { return dados; }
        public LocalDateTime getDataCriacao() { return dataCriacao; }
        public LocalDateTime getDataSincronizacao() { return dataSincronizacao; }
        public void setDataSincronizacao(LocalDateTime dataSincronizacao) { this.dataSincronizacao = dataSincronizacao; }
        public StatusSincronizacao getStatus() { return status; }
        public void setStatus(StatusSincronizacao status) { this.status = status; }
        public String getMensagemErro() { return mensagemErro; }
        public void setMensagemErro(String mensagemErro) { this.mensagemErro = mensagemErro; }
        public int getTentativas() { return tentativas; }
        public void incrementarTentativas() { this.tentativas++; }
        public void setTentativas(int tentativas) { this.tentativas = tentativas; }
        public String getIdUsuario() { return idUsuario; }
        public void setId(String id) { this.id = id; }
        public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    }
    
    private OfflineService() {
        this.modoOffline = false;
        this.operacoesPendentes = new ConcurrentHashMap<>();
        this.maxOperacoesPendentes = 1000;
        this.scheduler = Executors.newScheduledThreadPool(2);
        
        // Agendar verificação de sincronização a cada 30 segundos
        scheduler.scheduleAtFixedRate(this::verificarSincronizacao, 30, 30, TimeUnit.SECONDS);
        
        // Agendar limpeza de operações antigas a cada hora
        scheduler.scheduleAtFixedRate(this::limparOperacoesAntigas, 1, 1, TimeUnit.HOURS);
    }
    
    public static synchronized OfflineService getInstance() {
        if (instance == null) {
            instance = new OfflineService();
        }
        return instance;
    }
    
    /**
     * Ativa modo offline
     */
    public void ativarModoOffline() {
        this.modoOffline = true;
        System.out.println("Modo offline ativado - Operações serão salvas localmente");
    }
    
    /**
     * Desativa modo offline
     */
    public void desativarModoOffline() {
        this.modoOffline = false;
        System.out.println("Modo offline desativado - Iniciando sincronização");
        iniciarSincronizacao();
    }
    
    /**
     * Verifica se está em modo offline
     */
    public boolean isModoOffline() {
        return modoOffline;
    }
    
    /**
     * Adiciona operação pendente
     */
    public String adicionarOperacaoPendente(TipoOperacao tipo, Map<String, Object> dados, String idUsuario) {
        if (operacoesPendentes.size() >= maxOperacoesPendentes) {
            throw new IllegalStateException("Limite de operações pendentes atingido");
        }
        
        OperacaoPendente operacao = new OperacaoPendente(tipo, dados, idUsuario);
        operacoesPendentes.put(operacao.getId(), operacao);
        
        System.out.println("Operação pendente adicionada: " + tipo + " - ID: " + operacao.getId());
        
        return operacao.getId();
    }
    
    /**
     * Adiciona venda offline
     */
    public String adicionarVendaOffline(Map<String, Object> dadosVenda, String idUsuario) {
        Map<String, Object> dados = new HashMap<>(dadosVenda);
        dados.put("tipo_operacao", "VENDA_OFFLINE");
        dados.put("data_criacao", LocalDateTime.now());
        
        return adicionarOperacaoPendente(TipoOperacao.VENDA, dados, idUsuario);
    }
    
    /**
     * Adiciona movimentação de estoque offline
     */
    public String adicionarMovimentacaoEstoqueOffline(Map<String, Object> dadosMovimentacao, String idUsuario) {
        Map<String, Object> dados = new HashMap<>(dadosMovimentacao);
        dados.put("tipo_operacao", "ESTOQUE_OFFLINE");
        dados.put("data_criacao", LocalDateTime.now());
        
        return adicionarOperacaoPendente(TipoOperacao.ESTOQUE, dados, idUsuario);
    }
    
    /**
     * Adiciona cadastro de cliente offline
     */
    public String adicionarClienteOffline(Map<String, Object> dadosCliente, String idUsuario) {
        Map<String, Object> dados = new HashMap<>(dadosCliente);
        dados.put("tipo_operacao", "CLIENTE_OFFLINE");
        dados.put("data_criacao", LocalDateTime.now());
        
        return adicionarOperacaoPendente(TipoOperacao.CLIENTE, dados, idUsuario);
    }
    
    /**
     * Obtém operações pendentes
     */
    public List<OperacaoPendente> getOperacoesPendentes() {
        return new ArrayList<>(operacoesPendentes.values());
    }
    
    /**
     * Obtém operações pendentes por tipo
     */
    public List<OperacaoPendente> getOperacoesPendentesPorTipo(TipoOperacao tipo) {
        List<OperacaoPendente> resultado = new ArrayList<>();
        
        for (OperacaoPendente operacao : operacoesPendentes.values()) {
            if (operacao.getTipo() == tipo) {
                resultado.add(operacao);
            }
        }
        
        return resultado;
    }
    
    /**
     * Obtém operações pendentes por usuário
     */
    public List<OperacaoPendente> getOperacoesPendentesPorUsuario(String idUsuario) {
        List<OperacaoPendente> resultado = new ArrayList<>();
        
        for (OperacaoPendente operacao : operacoesPendentes.values()) {
            if (operacao.getIdUsuario().equals(idUsuario)) {
                resultado.add(operacao);
            }
        }
        
        return resultado;
    }
    
    /**
     * Inicia sincronização manual
     */
    public void iniciarSincronizacao() {
        if (!modoOffline && !operacoesPendentes.isEmpty()) {
            new Thread(this::processarSincronizacao).start();
        }
    }
    
    /**
     * Processa sincronização de operações pendentes
     */
    private void processarSincronizacao() {
        List<OperacaoPendente> paraSincronizar = new ArrayList<>();
        
        // Obter operações pendentes
        for (OperacaoPendente operacao : operacoesPendentes.values()) {
            if (operacao.getStatus() == StatusSincronizacao.PENDENTE || 
                operacao.getStatus() == StatusSincronizacao.ERRO) {
                paraSincronizar.add(operacao);
            }
        }
        
        if (paraSincronizar.isEmpty()) {
            System.out.println("Nenhuma operação pendente para sincronizar");
            return;
        }
        
        System.out.println("Iniciando sincronização de " + paraSincronizar.size() + " operações");
        
        for (OperacaoPendente operacao : paraSincronizar) {
            sincronizarOperacao(operacao);
            
            // Pequena pausa entre operações para não sobrecarregar
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        dataUltimaSincronizacao = LocalDateTime.now();
        System.out.println("Sincronização concluída");
    }
    
    /**
     * Sincroniza operação individual
     */
    private void sincronizarOperacao(OperacaoPendente operacao) {
        operacao.setStatus(StatusSincronizacao.EM_ANDAMENTO);
        operacao.incrementarTentativas();
        
        try {
            boolean sucesso = false;
            
            switch (operacao.getTipo()) {
                case VENDA:
                    sucesso = sincronizarVenda(operacao);
                    break;
                case ESTOQUE:
                    sucesso = sincronizarEstoque(operacao);
                    break;
                case CLIENTE:
                    sucesso = sincronizarCliente(operacao);
                    break;
                case PRODUTO:
                    sucesso = sincronizarProduto(operacao);
                    break;
                case CONFIGURACAO:
                    sucesso = sincronizarConfiguracao(operacao);
                    break;
                case PAGAMENTO:
                    sucesso = sincronizarPagamento(operacao);
                    break;
            }
            
            if (sucesso) {
                operacao.setStatus(StatusSincronizacao.SINCRONIZADO);
                operacao.setDataSincronizacao(LocalDateTime.now());
                operacoesPendentes.remove(operacao.getId());
                System.out.println("Operação sincronizada com sucesso: " + operacao.getId());
            } else {
                operacao.setStatus(StatusSincronizacao.ERRO);
                operacao.setMensagemErro("Falha na sincronização");
                
                // Remover se excedeu tentativas
                if (operacao.getTentativas() >= 3) {
                    operacoesPendentes.remove(operacao.getId());
                    System.out.println("Operação removida após falhas: " + operacao.getId());
                }
            }
            
        } catch (Exception e) {
            operacao.setStatus(StatusSincronizacao.ERRO);
            operacao.setMensagemErro(e.getMessage());
            
            if (operacao.getTentativas() >= 3) {
                operacoesPendentes.remove(operacao.getId());
            }
        }
    }
    
    // Métodos de sincronização específicos
    private boolean sincronizarVenda(OperacaoPendente operacao) {
        // Simulação - em implementação real, enviaria para o servidor
        System.out.println("Sincronizando venda: " + operacao.getDados().get("id_venda"));
        return Math.random() < 0.8; // 80% de sucesso
    }
    
    private boolean sincronizarEstoque(OperacaoPendente operacao) {
        System.out.println("Sincronizando movimentação de estoque: " + operacao.getDados().get("codigo_produto"));
        return Math.random() < 0.9; // 90% de sucesso
    }
    
    private boolean sincronizarCliente(OperacaoPendente operacao) {
        System.out.println("Sincronizando cliente: " + operacao.getDados().get("nome"));
        return Math.random() < 0.85; // 85% de sucesso
    }
    
    private boolean sincronizarProduto(OperacaoPendente operacao) {
        System.out.println("Sincronizando produto: " + operacao.getDados().get("nome"));
        return Math.random() < 0.85; // 85% de sucesso
    }
    
    private boolean sincronizarConfiguracao(OperacaoPendente operacao) {
        System.out.println("Sincronizando configuração: " + operacao.getDados().get("chave"));
        return Math.random() < 0.95; // 95% de sucesso
    }
    
    private boolean sincronizarPagamento(OperacaoPendente operacao) {
        System.out.println("Sincronizando pagamento: " + operacao.getDados().get("id_pagamento"));
        return Math.random() < 0.8; // 80% de sucesso
    }
    
    /**
     * Verifica automaticamente a sincronização
     */
    private void verificarSincronizacao() {
        if (!modoOffline && !operacoesPendentes.isEmpty()) {
            iniciarSincronizacao();
        }
    }
    
    /**
     * Limpa operações antigas (mais de 7 dias)
     */
    private void limparOperacoesAntigas() {
        LocalDateTime limite = LocalDateTime.now().minusDays(7);
        final int[] removidas = {0};
        
        operacoesPendentes.entrySet().removeIf(entry -> {
            boolean remover = entry.getValue().getDataCriacao().isBefore(limite);
            if (remover) {
                removidas[0]++;
            }
            return remover;
        });
        
        if (removidas[0] > 0) {
            System.out.println("Removidas " + removidas[0] + " operações antigas");
        }
    }
    
    /**
     * Obtém estatísticas do modo offline
     */
    public Map<String, Object> getEstatisticas() {
        Map<String, Object> estatisticas = new HashMap<>();
        
        estatisticas.put("modoOffline", modoOffline);
        estatisticas.put("totalOperacoesPendentes", operacoesPendentes.size());
        estatisticas.put("maxOperacoesPendentes", maxOperacoesPendentes);
        
        if (dataUltimaSincronizacao != null) {
            estatisticas.put("dataUltimaSincronizacao", dataUltimaSincronizacao);
        }
        
        // Contagem por tipo
        Map<TipoOperacao, Integer> contagemPorTipo = new HashMap<>();
        Map<StatusSincronizacao, Integer> contagemPorStatus = new HashMap<>();
        
        for (OperacaoPendente operacao : operacoesPendentes.values()) {
            contagemPorTipo.merge(operacao.getTipo(), 1, Integer::sum);
            contagemPorStatus.merge(operacao.getStatus(), 1, Integer::sum);
        }
        
        estatisticas.put("contagemPorTipo", contagemPorTipo);
        estatisticas.put("contagemPorStatus", contagemPorStatus);
        
        // Operações mais antigas
        if (!operacoesPendentes.isEmpty()) {
            OperacaoPendente maisAntiga = operacoesPendentes.values().stream()
                .min((o1, o2) -> o1.getDataCriacao().compareTo(o2.getDataCriacao()))
                .orElse(null);
            
            if (maisAntiga != null) {
                estatisticas.put("operacaoMaisAntiga", maisAntiga.getDataCriacao());
                estatisticas.put("horasDesdeMaisAntiga", 
                    java.time.temporal.ChronoUnit.HOURS.between(maisAntiga.getDataCriacao(), LocalDateTime.now()));
            }
        }
        
        return estatisticas;
    }
    
    /**
     * Força sincronização de uma operação específica
     */
    public boolean forcarSincronizacao(String idOperacao) {
        OperacaoPendente operacao = operacoesPendentes.get(idOperacao);
        if (operacao != null) {
            sincronizarOperacao(operacao);
            return operacao.getStatus() == StatusSincronizacao.SINCRONIZADO;
        }
        return false;
    }
    
    /**
     * Cancela operação pendente
     */
    public boolean cancelarOperacao(String idOperacao) {
        OperacaoPendente removida = operacoesPendentes.remove(idOperacao);
        return removida != null;
    }
    
    /**
     * Reenvia operações com erro
     */
    public void reenviarOperacoesComErro() {
        for (OperacaoPendente operacao : operacoesPendentes.values()) {
            if (operacao.getStatus() == StatusSincronizacao.ERRO) {
                operacao.setStatus(StatusSincronizacao.PENDENTE);
                operacao.setTentativas(0);
                operacao.setMensagemErro(null);
            }
        }
        
        iniciarSincronizacao();
    }
    
    /**
     * Exporta operações pendentes para backup
     */
    public List<Map<String, Object>> exportarOperacoesPendentes() {
        List<Map<String, Object>> exportacao = new ArrayList<>();
        
        for (OperacaoPendente operacao : operacoesPendentes.values()) {
            Map<String, Object> dados = new HashMap<>();
            dados.put("id", operacao.getId());
            dados.put("tipo", operacao.getTipo());
            dados.put("dados", operacao.getDados());
            dados.put("dataCriacao", operacao.getDataCriacao());
            dados.put("status", operacao.getStatus());
            dados.put("tentativas", operacao.getTentativas());
            dados.put("idUsuario", operacao.getIdUsuario());
            
            if (operacao.getMensagemErro() != null) {
                dados.put("mensagemErro", operacao.getMensagemErro());
            }
            
            exportacao.add(dados);
        }
        
        return exportacao;
    }
    
    /**
     * Importa operações pendentes de backup
     */
    public void importarOperacoesPendentes(List<Map<String, Object>> operacoesImportadas) {
        for (Map<String, Object> dados : operacoesImportadas) {
            try {
                TipoOperacao tipo = TipoOperacao.valueOf((String) dados.get("tipo"));
                @SuppressWarnings("unchecked")
                Map<String, Object> dadosOperacao = (Map<String, Object>) dados.get("dados");
                String idUsuario = (String) dados.get("idUsuario");
                
                OperacaoPendente operacao = new OperacaoPendente(tipo, dadosOperacao, idUsuario);
                operacao.setId((String) dados.get("id"));
                operacao.setDataCriacao((LocalDateTime) dados.get("dataCriacao"));
                operacao.setStatus(StatusSincronizacao.valueOf((String) dados.get("status")));
                operacao.setTentativas((Integer) dados.get("tentativas"));
                
                if (dados.containsKey("mensagemErro")) {
                    operacao.setMensagemErro((String) dados.get("mensagemErro"));
                }
                
                operacoesPendentes.put(operacao.getId(), operacao);
                
            } catch (Exception e) {
                System.err.println("Erro ao importar operação: " + e.getMessage());
            }
        }
    }
    
    /**
     * Finaliza o serviço offline
     */
    public void finalizar() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}
