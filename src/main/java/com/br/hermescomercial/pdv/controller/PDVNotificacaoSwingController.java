package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.shared.model.Notificacao;
import com.br.hermescomercial.pdv.service.NotificacaoService;
import com.br.hermescomercial.ui.layout.LayoutPadrao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller para tela de notificações do sistema
 * Versão 2.2.0 - Sistema de notificações em tempo real
 */
public class PDVNotificacaoSwingController {
    
    private JFrame frame;
    private JPanel mainPanel;
    private JTable notificacaoTable;
    private DefaultTableModel tableModel;
    private JLabel contadorLabel;
    private JButton btnMarcarLidas;
    private JButton btnLimparAntigas;
    private JButton btnAtualizar;
    private JButton btnFechar;
    private NotificacaoService notificacaoService;
    private String usuarioLogado;
    
    public PDVNotificacaoSwingController(String usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        this.notificacaoService = new NotificacaoService();
        initializeUI();
        atualizarNotificacao();
    }
    
    private void initializeUI() {
        // LayoutPadrao é aplicado diretamente nos componentes
        
        frame = new JFrame("🔔 Notificações - Hermes Comercial PDV");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        
        // Configurar fundo
        frame.getContentPane().setBackground(LayoutPadrao.COR_FUNDO);
        
        createMainPanel();
        frame.add(mainPanel);
    }
    
    private void createMainPanel() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        
        // Painel superior
        createHeaderPanel();
        
        // Painel central com tabela
        createNotificacoesPanel();
        
        // Painel inferior com botões
        createButtonPanel();
    }
    
    private void createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        // Título
        JLabel titleLabel = new JLabel("🔔 Central de Notificações");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(LayoutPadrao.COR_TEXTO);
        
        // Contador de não lidas
        contadorLabel = new JLabel("📬 Carregando...");
        contadorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contadorLabel.setForeground(LayoutPadrao.COR_TEXTO_CLARO);
        contadorLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(contadorLabel, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
    }
    
    private void createNotificacoesPanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
        
        // Configurar tabela
        String[] colunas = {"ID", "Título", "Mensagem", "Tipo", "Prioridade", "Data", "Status"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        notificacaoTable = new JTable(tableModel);
        notificacaoTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        notificacaoTable.setRowHeight(30);
        notificacaoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        notificacaoTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        notificacaoTable.getTableHeader().setBackground(LayoutPadrao.COR_SECUNDARIA);
        
        // Configurar largura das colunas
        notificacaoTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        notificacaoTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Título
        notificacaoTable.getColumnModel().getColumn(2).setPreferredWidth(250); // Mensagem
        notificacaoTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // Tipo
        notificacaoTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Prioridade
        notificacaoTable.getColumnModel().getColumn(5).setPreferredWidth(120); // Data
        notificacaoTable.getColumnModel().getColumn(6).setPreferredWidth(80);  // Status
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(notificacaoTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(LayoutPadrao.BORDA_PADRAO);
        
        // Double click para marcar como lida
        notificacaoTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    marcarNotificacaoSelecionadaComoLida();
                }
            }
        });
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
    }
    
    private void createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        // Botões
        btnAtualizar = LayoutPadrao.criarBotaoPrimario("🔄 Atualizar");
        btnMarcarLidas = LayoutPadrao.criarBotaoSucesso("✅ Marcar Lidas");
        btnLimparAntigas = LayoutPadrao.criarBotaoAlerta("🗑️ Limpar Antigas");
        btnFechar = LayoutPadrao.criarBotaoPerigo("❌ Fechar");
        
        // Eventos
        btnAtualizar.addActionListener(e -> atualizarNotificacao());
        btnMarcarLidas.addActionListener(e -> marcarTodasComoLidas());
        btnLimparAntigas.addActionListener(e -> limparNotificacaoAntigas());
        btnFechar.addActionListener(e -> fecharJanela());
        
        buttonPanel.add(btnAtualizar);
        buttonPanel.add(btnMarcarLidas);
        buttonPanel.add(btnLimparAntigas);
        buttonPanel.add(btnFechar);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void atualizarNotificacao() {
        try {
            List<Notificacao> notificacao = notificacaoService.listarNotificacoes(usuarioLogado);
            
            // Limpar tabela
            tableModel.setRowCount(0);
            
            // Preencher tabela
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM HH:mm");
            
            for (Notificacao notificacaoItem : notificacao) {
                Object[] row = {
                    notificacaoItem.getId(),
                    notificacaoItem.getTitulo(),
                    notificacaoItem.getMensagem(),
                    notificacaoItem.getTipo().getDescricao(),
                    notificacaoItem.getPrioridade().getDescricao(),
                    notificacaoItem.getDataCriacao().format(formatter),
                    notificacaoItem.isLida() ? "✅ Lida" : "🔴 Não lida"
                };
                tableModel.addRow(row);
            }
            
            // Atualizar contador
            int naoLidas = notificacaoService.contarNaoLidas(usuarioLogado);
            contadorLabel.setText("📬 " + naoLidas + " não lidas");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Erro ao atualizar notificações: " + e.getMessage(), 
                                      "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void marcarNotificacaoSelecionadaComoLida() {
        int selectedRow = notificacaoTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Selecione uma notificação para marcar como lida", 
                                          "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Long id = (Long) tableModel.getValueAt(selectedRow, 0);
        boolean sucesso = notificacaoService.marcarComoLida(id);
        
        if (sucesso) {
            JOptionPane.showMessageDialog(frame, "Notificação marcada como lida com sucesso!", 
                                          "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            atualizarNotificacao();
        } else {
            JOptionPane.showMessageDialog(frame, "Falha ao marcar notificação como lida", 
                                          "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void marcarTodasComoLidas() {
        int confirm = JOptionPane.showConfirmDialog(frame, 
                                                    "Deseja marcar todas as notificações como lidas?", 
                                                    "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean sucesso = notificacaoService.marcarTodasComoLidas(usuarioLogado);
            
            if (sucesso) {
                JOptionPane.showMessageDialog(frame, "Todas as notificações foram marcadas como lidas!", 
                                              "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                atualizarNotificacao();
            } else {
                JOptionPane.showMessageDialog(frame, "Falha ao marcar notificações como lidas", 
                                              "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void limparNotificacaoAntigas() {
        int confirm = JOptionPane.showConfirmDialog(frame, 
                                                    "Deseja limpar notificações antigas (mais de 30 dias)?", 
                                                    "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            int excluidas = notificacaoService.limparItensNotificacaoAntigas();
            
            JOptionPane.showMessageDialog(frame, 
                                        excluidas + " notificações antigas foram excluídas!", 
                                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            atualizarNotificacao();
        }
    }
    
    private void fecharJanela() {
        frame.dispose();
    }
    
    public void show() {
        frame.setVisible(true);
    }
    
    // Método estático para fácil acesso
    public static void mostrarNotificacao(String usuarioLogado) {
        SwingUtilities.invokeLater(() -> {
            PDVNotificacaoSwingController controller = new PDVNotificacaoSwingController(usuarioLogado);
            controller.show();
        });
    }
}
