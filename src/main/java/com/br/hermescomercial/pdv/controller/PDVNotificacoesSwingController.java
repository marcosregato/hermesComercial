package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.shared.model.Notificacao;
import com.br.hermescomercial.pdv.service.NotificacaoService;
import com.br.hermescomercial.theme.ModernTheme;

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
public class PDVNotificacoesSwingController {
    
    private JFrame frame;
    private JPanel mainPanel;
    private JTable notificacoesTable;
    private DefaultTableModel tableModel;
    private JLabel contadorLabel;
    private JButton btnMarcarLidas;
    private JButton btnLimparAntigas;
    private JButton btnAtualizar;
    private JButton btnFechar;
    private NotificacaoService notificacaoService;
    private String usuarioLogado;
    
    public PDVNotificacoesSwingController(String usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        this.notificacaoService = new NotificacaoService();
        initializeUI();
        atualizarNotificacoes();
    }
    
    private void initializeUI() {
        // Aplicar tema moderno
        ModernTheme.applyModernTheme();
        
        frame = new JFrame("🔔 Notificações - Hermes Comercial PDV");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        
        // Configurar fundo
        frame.getContentPane().setBackground(ModernTheme.BACKGROUND_PRIMARY);
        
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
        titleLabel.setForeground(ModernTheme.TEXT_PRIMARY);
        
        // Contador de não lidas
        contadorLabel = new JLabel("📬 Carregando...");
        contadorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contadorLabel.setForeground(ModernTheme.TEXT_SECONDARY);
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
        
        notificacoesTable = new JTable(tableModel);
        notificacoesTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        notificacoesTable.setRowHeight(30);
        notificacoesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        notificacoesTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        notificacoesTable.getTableHeader().setBackground(ModernTheme.BACKGROUND_SECONDARY);
        
        // Configurar largura das colunas
        notificacoesTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        notificacoesTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Título
        notificacoesTable.getColumnModel().getColumn(2).setPreferredWidth(250); // Mensagem
        notificacoesTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // Tipo
        notificacoesTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Prioridade
        notificacoesTable.getColumnModel().getColumn(5).setPreferredWidth(120); // Data
        notificacoesTable.getColumnModel().getColumn(6).setPreferredWidth(80);  // Status
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(notificacoesTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createLineBorder(ModernTheme.BORDER_LIGHT, 1));
        
        // Double click para marcar como lida
        notificacoesTable.addMouseListener(new MouseAdapter() {
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
        btnAtualizar = createModernButton("🔄 Atualizar", ModernTheme.PRIMARY_COLOR);
        btnMarcarLidas = createModernButton("✅ Marcar Lidas", ModernTheme.SUCCESS_COLOR);
        btnLimparAntigas = createModernButton("🗑️ Limpar Antigas", ModernTheme.WARNING_COLOR);
        btnFechar = createModernButton("❌ Fechar", ModernTheme.DANGER_COLOR);
        
        // Eventos
        btnAtualizar.addActionListener(e -> atualizarNotificacoes());
        btnMarcarLidas.addActionListener(e -> marcarTodasComoLidas());
        btnLimparAntigas.addActionListener(e -> limparNotificacoesAntigas());
        btnFechar.addActionListener(e -> fecharJanela());
        
        buttonPanel.add(btnAtualizar);
        buttonPanel.add(btnMarcarLidas);
        buttonPanel.add(btnLimparAntigas);
        buttonPanel.add(btnFechar);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JButton createModernButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efeito hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void atualizarNotificacoes() {
        try {
            List<Notificacao> notificacoes = notificacaoService.listarNotificacoes(usuarioLogado);
            
            // Limpar tabela
            tableModel.setRowCount(0);
            
            // Preencher tabela
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM HH:mm");
            
            for (Notificacao notificacao : notificacoes) {
                Object[] row = {
                    notificacao.getId(),
                    notificacao.getTitulo(),
                    notificacao.getMensagem(),
                    notificacao.getTipo().getDescricao(),
                    notificacao.getPrioridade().getDescricao(),
                    notificacao.getDataCriacao().format(formatter),
                    notificacao.isLida() ? "✅ Lida" : "🔴 Não lida"
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
        int selectedRow = notificacoesTable.getSelectedRow();
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
            atualizarNotificacoes();
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
                atualizarNotificacoes();
            } else {
                JOptionPane.showMessageDialog(frame, "Falha ao marcar notificações como lidas", 
                                              "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void limparNotificacoesAntigas() {
        int confirm = JOptionPane.showConfirmDialog(frame, 
                                                    "Deseja limpar notificações antigas (mais de 30 dias)?", 
                                                    "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            int excluidas = notificacaoService.limparNotificacoesAntigas();
            
            JOptionPane.showMessageDialog(frame, 
                                        excluidas + " notificações antigas foram excluídas!", 
                                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            atualizarNotificacoes();
        }
    }
    
    private void fecharJanela() {
        frame.dispose();
    }
    
    public void show() {
        frame.setVisible(true);
    }
    
    // Método estático para fácil acesso
    public static void mostrarNotificacoes(String usuarioLogado) {
        SwingUtilities.invokeLater(() -> {
            PDVNotificacoesSwingController controller = new PDVNotificacoesSwingController(usuarioLogado);
            controller.show();
        });
    }
}
