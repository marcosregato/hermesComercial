package com.br.hermescomercial.shared.model;

import java.time.LocalDateTime;

/**
 * Modelo de notificação do sistema
 * Versão 2.2.0 - Sistema de notificações em tempo real
 */
public class Notificacao {
    
    private Long id;
    private String titulo;
    private String mensagem;
    private TipoNotificacao tipo;
    private LocalDateTime dataCriacao;
    private boolean lida;
    private String usuarioDestino;
    private PrioridadeNotificacao prioridade;
    
    public enum TipoNotificacao {
        SISTEMA("Sistema"),
        VENDA("Venda"),
        ESTOQUE("Estoque"),
        CLIENTE("Cliente"),
        FINANCEIRO("Financeiro");
        
        private final String descricao;
        
        TipoNotificacao(String descricao) {
            this.descricao = descricao;
        }
        
        public String getDescricao() {
            return descricao;
        }
    }
    
    public enum PrioridadeNotificacao {
        BAIXA("Baixa"),
        MEDIA("Média"),
        ALTA("Alta"),
        URGENTE("Urgente");
        
        private final String descricao;
        
        PrioridadeNotificacao(String descricao) {
            this.descricao = descricao;
        }
        
        public String getDescricao() {
            return descricao;
        }
    }
    
    // Construtores
    public Notificacao() {
        this.dataCriacao = LocalDateTime.now();
        this.lida = false;
        this.prioridade = PrioridadeNotificacao.MEDIA;
    }
    
    public Notificacao(String titulo, String mensagem, TipoNotificacao tipo) {
        this();
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.tipo = tipo;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getMensagem() {
        return mensagem;
    }
    
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
    
    public TipoNotificacao getTipo() {
        return tipo;
    }
    
    public void setTipo(TipoNotificacao tipo) {
        this.tipo = tipo;
    }
    
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    
    public boolean isLida() {
        return lida;
    }
    
    public void setLida(boolean lida) {
        this.lida = lida;
    }
    
    public String getUsuarioDestino() {
        return usuarioDestino;
    }
    
    public void setUsuarioDestino(String usuarioDestino) {
        this.usuarioDestino = usuarioDestino;
    }
    
    public PrioridadeNotificacao getPrioridade() {
        return prioridade;
    }
    
    public void setPrioridade(PrioridadeNotificacao prioridade) {
        this.prioridade = prioridade;
    }
    
    // Métodos utilitários
    public void marcarComoLida() {
        this.lida = true;
    }
    
    public void marcarComoNaoLida() {
        this.lida = false;
    }
    
    public boolean isRecente() {
        return dataCriacao.isAfter(LocalDateTime.now().minusHours(24));
    }
    
    @Override
    public String toString() {
        return "Notificacao{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", mensagem='" + mensagem + '\'' +
                ", tipo=" + tipo +
                ", dataCriacao=" + dataCriacao +
                ", lida=" + lida +
                ", prioridade=" + prioridade +
                '}';
    }
}
