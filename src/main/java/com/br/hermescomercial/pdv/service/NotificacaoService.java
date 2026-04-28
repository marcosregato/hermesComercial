package com.br.hermescomercial.pdv.service;

import com.br.hermescomercial.pdv.dao.NotificacaoDAO;
import com.br.hermescomercial.shared.model.Notificacao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.br.hermescomercial.shared.model.Notificacao.TipoNotificacao;
import com.br.hermescomercial.shared.model.Notificacao.PrioridadeNotificacao;

import java.sql.SQLException;
import java.util.List;

/**
 * Service para lógica de negócio de notificações
 * Versão 2.2.0 - Sistema de notificações em tempo real
 */
public class NotificacaoService {
    
    private static final Logger logger = LogManager.getLogger(NotificacaoService.class);
    private final NotificacaoDAO notificacaoDAO;
    
    public NotificacaoService() {
        this.notificacaoDAO = new NotificacaoDAO();
        // Inicializar tabela se não existir
        try {
            notificacaoDAO.createTableIfNotExists();
        } catch (SQLException e) {
            logger.error("Erro ao inicializar tabela de notificações", e);
        }
    }
    
    /**
     * Envia uma notificação para um usuário específico
     */
    public Notificacao enviar(String titulo, String mensagem, 
                                        TipoNotificacao tipo, 
                                        String usuarioDestino) {
        return enviar(titulo, mensagem, tipo, usuarioDestino, 
                                PrioridadeNotificacao.MEDIA);
    }
    
    /**
     * Envia uma notificação com prioridade específica
     */
    public Notificacao enviar(String titulo, String mensagem, 
                                        TipoNotificacao tipo, 
                                        String usuarioDestino,
                                        PrioridadeNotificacao prioridade) {
        try {
            Notificacao notificacao = new Notificacao(titulo, mensagem, tipo);
            notificacao.setUsuarioDestino(usuarioDestino);
            notificacao.setPrioridade(prioridade);
            
            Notificacao salva = notificacaoDAO.save(notificacao);
            logger.info("Notificação enviada: {} para {}", titulo, usuarioDestino);
            
            return salva;
        } catch (SQLException e) {
            logger.error("Erro ao enviar notificação: " + titulo, e);
            throw new RuntimeException("Falha ao enviar notificação", e);
        }
    }
    
    /**
     * Envia notificação para todos os usuários (broadcast)
     */
    public Notificacao enviarBroadcast(String titulo, String mensagem, 
                                                 TipoNotificacao tipo) {
        return enviar(titulo, mensagem, tipo, null, 
                                PrioridadeNotificacao.MEDIA);
    }
    
    /**
     * Lista todas as notificações de um usuário
     */
    public List<Notificacao> listarNotificacoes(String usuario) {
        try {
            return notificacaoDAO.findByUsuario(usuario);
        } catch (SQLException e) {
            logger.error("Erro ao listar notificações para usuário: " + usuario, e);
            throw new RuntimeException("Falha ao listar notificações", e);
        }
    }
    
    /**
     * Lista notificações não lidas de um usuário
     */
    public List<com.br.hermescomercial.shared.model.Notificacao> listarNotificacoesNaoLidas(String usuario) {
        try {
            return notificacaoDAO.findByNaoLidas();
        } catch (SQLException e) {
            logger.error("Erro ao listar notificações não lidas para usuário: " + usuario, e);
            throw new RuntimeException("Falha ao listar notificações não lidas", e);
        }
    }
    
    /**
     * Marca notificação como lida
     */
    public boolean marcarComoLida(Long notificacaoId) {
        try {
            boolean sucesso = notificacaoDAO.markAsRead(notificacaoId);
            if (sucesso) {
                logger.info("Notificação {} marcada como lida", notificacaoId);
            }
            return sucesso;
        } catch (SQLException e) {
            logger.error("Erro ao marcar notificação como lida: " + notificacaoId, e);
            throw new RuntimeException("Falha ao marcar notificação como lida", e);
        }
    }
    
    /**
     * Marca todas as notificações de um usuário como lidas
     */
    public boolean marcarTodasComoLidas(String usuario) {
        try {
            boolean sucesso = notificacaoDAO.markAllAsRead(usuario);
            if (sucesso) {
                logger.info("Todas as notificações do usuário {} marcadas como lidas", usuario);
            }
            return sucesso;
        } catch (SQLException e) {
            logger.error("Erro ao marcar todas as notificações como lidas para usuário: " + usuario, e);
            throw new RuntimeException("Falha ao marcar notificações como lidas", e);
        }
    }
    
    /**
     * Conta notificações não lidas de um usuário
     */
    public int contarNaoLidas(String usuario) {
        try {
            return notificacaoDAO.countNaoLidas(usuario);
        } catch (SQLException e) {
            logger.error("Erro ao contar notificações não lidas para usuário: " + usuario, e);
            throw new RuntimeException("Falha ao contar notificações não lidas", e);
        }
    }
    
    /**
     * Limpa notificações antigas (manutenção)
     */
    public int limparNotificacoesAntigas() {
        try {
            int excluidas = notificacaoDAO.deleteAntigas();
            logger.info("Limpeza de notificações antigas: {} registros excluídos", excluidas);
            return excluidas;
        } catch (SQLException e) {
            logger.error("Erro ao limpar notificações antigas", e);
            throw new RuntimeException("Falha ao limpar notificações antigas", e);
        }
    }
    
    /**
     * Cria notificações automáticas do sistema
     */
    public void criarNotificacaoAutomatica(TipoNotificacao tipo, String mensagem) {
        String titulo = switch (tipo) {
            case SISTEMA -> "🔧 Sistema";
            case VENDA -> "💰 Venda";
            case ESTOQUE -> "📦 Estoque";
            case CLIENTE -> "👤 Cliente";
            case FINANCEIRO -> "💳 Financeiro";
        };
        
        enviarBroadcast(titulo, mensagem, tipo);
    }
    
    /**
     * Cria notificação de estoque baixo
     */
    public void notificarEstoqueBaixo(String produto, int quantidadeAtual, int quantidadeMinima) {
        String mensagem = String.format("Produto %s com estoque baixo! Atual: %d, Mínimo: %d", 
                                        produto, quantidadeAtual, quantidadeMinima);
        criarNotificacaoAutomatica(TipoNotificacao.ESTOQUE, mensagem);
    }
    
    /**
     * Cria notificação de venda realizada
     */
    public void notificarVendaRealizada(String cliente, double valor) {
        String mensagem = String.format("Venda realizada para %s no valor de R$ %.2f", cliente, valor);
        criarNotificacaoAutomatica(TipoNotificacao.VENDA, mensagem);
    }
    
    /**
     * Inicializa tabela de notificações
     */
    public void inicializarTabela() {
        try {
            notificacaoDAO.createTableIfNotExists();
            logger.info("Tabela de notificações inicializada com sucesso");
        } catch (SQLException e) {
            logger.error("Erro ao inicializar tabela de notificações", e);
            throw new RuntimeException("Falha ao inicializar tabela de notificações", e);
        }
    }
}
