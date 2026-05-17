package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.ui.layout.LayoutPadrao;
import com.br.hermescomercial.util.SystemLogger;
import com.br.hermescomercial.shared.model.Notificacao;
import com.br.hermescomercial.pdv.service.NotificacaoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Classe especializada para formulário de notificações do sistema
 * Segue o padrão Header → Busca → Tabela → Ações
 * Versão 1.0.0 - Adaptada para PDVMenuLateralElegante
 */
public class PDVFormularioNotificacoes {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes do formulário
    private JTable notificacoesTable;
    private DefaultTableModel notificacoesModel;
    private JLabel contadorLabel;
    private JTextField txtBuscaTitulo;
    private JTextField txtBuscaMensagem;
    private JComboBox<String> cbTipo;
    private JComboBox<String> cbPrioridade;
    private JComboBox<String> cbStatus;
    
    // Serviço
    private NotificacaoService notificacaoService;
    
    // Cores
    
    public PDVFormularioNotificacoes(JPanel workArea, String usuario, String nome) {
        this.workArea = workArea;
        this.usuarioAtual = usuario;
        this.nomeUsuario = nome;
        this.notificacaoService = new NotificacaoService();
    }
    
    public JPanel criarFormularioNotificacoes() {
        SystemLogger.ui("=== CRIANDO FORMULÁRIO NOTIFICAÇÕES ===");
        SystemLogger.ui("Usuário: " + usuarioAtual);
        
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(Color.WHITE);
        
        // Header
        painelPrincipal.add(criarHeader(), BorderLayout.NORTH);
        
        // Conteúdo principal
        JPanel conteudo = new JPanel(new BorderLayout());
        conteudo.setBackground(Color.WHITE);
        
        // Painel de busca
        conteudo.add(criarPainelBusca(), BorderLayout.NORTH);
        
        // Tabela de notificações
        conteudo.add(criarTabelaNotificacoes(), BorderLayout.CENTER);
        
        // Painel de ações
        conteudo.add(criarPainelAcoes(), BorderLayout.SOUTH);
        
        painelPrincipal.add(conteudo, BorderLayout.CENTER);
        
        // Carregar dados iniciais
        carregarNotificacoes();
        
        SystemLogger.ui("Formulário Notificações criado com sucesso");
        return painelPrincipal;
    }
    
    private JPanel criarHeader() {
        JPanel header = LayoutPadrao.criarHeaderPanel("🔔 Central de Notificações");
        
        // Contador de não lidas
        contadorLabel = new JLabel("📬 Carregando...");
        contadorLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        contadorLabel.setForeground(Color.WHITE);
        contadorLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        // Informações do usuário
        JPanel userInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userInfo.setBackground(new Color(250, 250, 250));
        userInfo.add(new JLabel("👤 " + nomeUsuario));
        userInfo.add(new JLabel(" | "));
        userInfo.add(new JLabel("📅 " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        userInfo.add(contadorLabel);
        
        header.add(userInfo, BorderLayout.EAST);
        return header;
    }
    
    private JPanel criarPainelBusca() {
        JPanel painelBusca = new JPanel(new BorderLayout());
        painelBusca.setBackground(Color.WHITE);
        painelBusca.setBorder(BorderFactory.createTitledBorder("🔍 Filtros de Busca"));
        
        // Painel de campos de busca
        JPanel camposPanel = new JPanel(new GridBagLayout());
        camposPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Título
        gbc.gridx = 0; gbc.gridy = 0;
        camposPanel.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtBuscaTitulo = new JTextField(20);
        txtBuscaTitulo.setToolTipText("Buscar por título da notificação");
        camposPanel.add(txtBuscaTitulo, gbc);
        
        // Mensagem
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        camposPanel.add(new JLabel("Mensagem:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtBuscaMensagem = new JTextField(20);
        txtBuscaMensagem.setToolTipText("Buscar por conteúdo da mensagem");
        camposPanel.add(txtBuscaMensagem, gbc);
        
        // Tipo
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        camposPanel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        cbTipo = new JComboBox<>(new String[]{"Todos", "Sistema", "Vendas", "Estoque", "Financeiro", "Usuário"});
        camposPanel.add(cbTipo, gbc);
        
        // Prioridade
        gbc.gridx = 2; gbc.gridy = 0; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        camposPanel.add(new JLabel("Prioridade:"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        cbPrioridade = new JComboBox<>(new String[]{"Todas", "Alta", "Média", "Baixa"});
        camposPanel.add(cbPrioridade, gbc);
        
        // Status
        gbc.gridx = 2; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        camposPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 3; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        cbStatus = new JComboBox<>(new String[]{"Todos", "Não lidas", "Lidas"});
        camposPanel.add(cbStatus, gbc);
        
        // Botões de busca
        gbc.gridx = 2; gbc.gridy = 2; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoesPanel.setBackground(Color.WHITE);
        
        JButton btnBuscar = LayoutPadrao.criarBotaoPrimario("🔍 Buscar");
        btnBuscar.addActionListener(e -> buscarNotificacoes());
        
        JButton btnLimpar = LayoutPadrao.criarBotaoSecundario("🔄 Limpar");
        btnLimpar.addActionListener(e -> limparFiltros());
        
        JButton btnAtualizar = LayoutPadrao.criarBotaoSucesso("🔄 Atualizar");
        btnAtualizar.addActionListener(e -> carregarNotificacoes());
        
        botoesPanel.add(btnBuscar);
        botoesPanel.add(btnLimpar);
        botoesPanel.add(btnAtualizar);
        
        camposPanel.add(botoesPanel, gbc);
        
        painelBusca.add(camposPanel, BorderLayout.CENTER);
        
        return painelBusca;
    }
    
    private JPanel criarTabelaNotificacoes() {
        JPanel painelTabela = new JPanel(new BorderLayout());
        painelTabela.setBackground(Color.WHITE);
        painelTabela.setBorder(BorderFactory.createTitledBorder("📋 Notificações"));
        
        // Configurar tabela
        String[] colunas = {"ID", "Título", "Mensagem", "Tipo", "Prioridade", "Data/Hora", "Status"};
        notificacoesModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Integer.class;
                return String.class;
            }
        };
        
        notificacoesTable = new JTable(notificacoesModel);
        notificacoesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        notificacoesTable.getTableHeader().setReorderingAllowed(false);
        notificacoesTable.setRowHeight(25);
        
        // Configurar largura das colunas
        notificacoesTable.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        notificacoesTable.getColumnModel().getColumn(1).setPreferredWidth(150);  // Título
        notificacoesTable.getColumnModel().getColumn(2).setPreferredWidth(200);  // Mensagem
        notificacoesTable.getColumnModel().getColumn(3).setPreferredWidth(80);   // Tipo
        notificacoesTable.getColumnModel().getColumn(4).setPreferredWidth(80);   // Prioridade
        notificacoesTable.getColumnModel().getColumn(5).setPreferredWidth(100);  // Data/Hora
        notificacoesTable.getColumnModel().getColumn(6).setPreferredWidth(80);   // Status
        
        // Double click para marcar como lida
        notificacoesTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    marcarComoLida();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(notificacoesTable);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        
        painelTabela.add(scrollPane, BorderLayout.CENTER);
        
        return painelTabela;
    }
    
    private JPanel criarPainelAcoes() {
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelAcoes.setBackground(Color.WHITE);
        painelAcoes.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton btnMarcarLidas = LayoutPadrao.criarBotaoSucesso("✅ Marcar Todas como Lidas");
        btnMarcarLidas.addActionListener(e -> marcarTodasComoLidas());
        
        JButton btnLimparAntigas = LayoutPadrao.criarBotaoAlerta("🗑️ Limpar Antigas");
        btnLimparAntigas.addActionListener(e -> limparAntigas());
        
        JButton btnVerDetalhes = LayoutPadrao.criarBotaoPrimario("👁️ Ver Detalhes");
        btnVerDetalhes.addActionListener(e -> verDetalhes());
        
        JButton btnExcluir = LayoutPadrao.criarBotaoPerigo("🗑️ Excluir");
        btnExcluir.addActionListener(e -> excluirNotificacao());
        
        painelAcoes.add(btnMarcarLidas);
        painelAcoes.add(btnLimparAntigas);
        painelAcoes.add(Box.createHorizontalStrut(10));
        painelAcoes.add(btnVerDetalhes);
        painelAcoes.add(btnExcluir);
        
        return painelAcoes;
    }
    
    // Métodos de ação
    private void carregarNotificacoes() {
        try {
            List<Notificacao> notificacoes = notificacaoService.listarNotificacoes(usuarioAtual);
            
            // Limpar tabela
            notificacoesModel.setRowCount(0);
            
            // Preencher tabela
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM HH:mm");
            
            for (Notificacao notificacao : notificacoes) {
                Object[] row = {
                    notificacao.getId(),
                    notificacao.getTitulo(),
                    notificacao.getMensagem().length() > 30 ? 
                        notificacao.getMensagem().substring(0, 30) + "..." : 
                        notificacao.getMensagem(),
                    notificacao.getTipo().getDescricao(),
                    notificacao.getPrioridade().getDescricao(),
                    notificacao.getDataCriacao().format(formatter),
                    notificacao.isLida() ? "✅ Lida" : "🔴 Não lida"
                };
                notificacoesModel.addRow(row);
            }
            
            // Atualizar contador
            int naoLidas = notificacaoService.contarNaoLidas(usuarioAtual);
            contadorLabel.setText("📬 " + naoLidas + " não lidas");
            
            SystemLogger.ui("Notificações carregadas: " + notificacoes.size() + " total, " + naoLidas + " não lidas");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(workArea, 
                "Erro ao carregar notificações: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
            SystemLogger.error("Erro ao carregar notificações: " + e.getMessage());
        }
    }
    
    private void buscarNotificacoes() {
        String titulo = txtBuscaTitulo.getText().trim();
        String mensagem = txtBuscaMensagem.getText().trim();
        String tipo = (String) cbTipo.getSelectedItem();
        String prioridade = (String) cbPrioridade.getSelectedItem();
        String status = (String) cbStatus.getSelectedItem();
        
        // Simular busca (em um sistema real, isso consultaria o banco de dados)
        JOptionPane.showMessageDialog(workArea, 
            "Funcionalidade de busca avançada em desenvolvimento!\n\n" +
            "Filtros aplicados:\n" +
            "Título: " + (titulo.isEmpty() ? "Todos" : titulo) + "\n" +
            "Mensagem: " + (mensagem.isEmpty() ? "Todas" : mensagem) + "\n" +
            "Tipo: " + tipo + "\n" +
            "Prioridade: " + prioridade + "\n" +
            "Status: " + status,
            "Busca Avançada", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Busca avançada executada por " + usuarioAtual);
    }
    
    private void limparFiltros() {
        txtBuscaTitulo.setText("");
        txtBuscaMensagem.setText("");
        cbTipo.setSelectedIndex(0);
        cbPrioridade.setSelectedIndex(0);
        cbStatus.setSelectedIndex(0);
        
        carregarNotificacoes();
        
        SystemLogger.ui("Filtros limpos por " + usuarioAtual);
    }
    
    private void marcarComoLida() {
        int selectedRow = notificacoesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, 
                "Selecione uma notificação para marcar como lida!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Long id = (Long) notificacoesModel.getValueAt(selectedRow, 0);
        boolean sucesso = notificacaoService.marcarComoLida(id);
        
        if (sucesso) {
            JOptionPane.showMessageDialog(workArea, 
                "Notificação marcada como lida com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarNotificacoes();
            SystemLogger.ui("Notificação " + id + " marcada como lida por " + usuarioAtual);
        } else {
            JOptionPane.showMessageDialog(workArea, 
                "Falha ao marcar notificação como lida", 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void marcarTodasComoLidas() {
        int confirm = JOptionPane.showConfirmDialog(workArea, 
            "Deseja marcar todas as notificações como lidas?", 
            "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean sucesso = notificacaoService.marcarTodasComoLidas(usuarioAtual);
            
            if (sucesso) {
                JOptionPane.showMessageDialog(workArea, 
                    "Todas as notificações foram marcadas como lidas!", 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarNotificacoes();
                SystemLogger.ui("Todas as notificações marcadas como lidas por " + usuarioAtual);
            } else {
                JOptionPane.showMessageDialog(workArea, 
                    "Falha ao marcar notificações como lidas", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void limparAntigas() {
        int confirm = JOptionPane.showConfirmDialog(workArea, 
            "Deseja limpar notificações antigas (mais de 30 dias)?", 
            "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            int excluidas = notificacaoService.limparItensNotificacaoAntigas();
            
            JOptionPane.showMessageDialog(workArea, 
                excluidas + " notificações antigas foram excluídas!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarNotificacoes();
            SystemLogger.ui(excluidas + " notificações antigas excluídas por " + usuarioAtual);
        }
    }
    
    private void verDetalhes() {
        int selectedRow = notificacoesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, 
                "Selecione uma notificação para ver detalhes!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Long id = (Long) notificacoesModel.getValueAt(selectedRow, 0);
        String titulo = (String) notificacoesModel.getValueAt(selectedRow, 1);
        String mensagem = (String) notificacoesModel.getValueAt(selectedRow, 2);
        String tipo = (String) notificacoesModel.getValueAt(selectedRow, 3);
        String prioridade = (String) notificacoesModel.getValueAt(selectedRow, 4);
        String data = (String) notificacoesModel.getValueAt(selectedRow, 5);
        String status = (String) notificacoesModel.getValueAt(selectedRow, 6);
        
        // Criar diálogo de detalhes
        JDialog dialog = new JDialog((JFrame) workArea.getTopLevelAncestor(), "Detalhes da Notificação", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(workArea.getTopLevelAncestor());
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Informações da notificação
        JTextArea txtDetalhes = new JTextArea();
        txtDetalhes.setEditable(false);
        txtDetalhes.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDetalhes.setText(
            "ID: " + id + "\n" +
            "Título: " + titulo + "\n" +
            "Mensagem: " + mensagem + "\n" +
            "Tipo: " + tipo + "\n" +
            "Prioridade: " + prioridade + "\n" +
            "Data/Hora: " + data + "\n" +
            "Status: " + status + "\n" +
            "Usuário: " + usuarioAtual
        );
        
        JScrollPane scrollPane = new JScrollPane(txtDetalhes);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Botão fechar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnFechar = LayoutPadrao.criarBotaoPrimario("Fechar");
        btnFechar.addActionListener(e -> dialog.dispose());
        buttonPanel.add(btnFechar);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(panel);
        dialog.setVisible(true);
        
        SystemLogger.ui("Detalhes da notificação " + id + " visualizados por " + usuarioAtual);
    }
    
    private void excluirNotificacao() {
        int selectedRow = notificacoesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, 
                "Selecione uma notificação para excluir!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Long id = (Long) notificacoesModel.getValueAt(selectedRow, 0);
        String titulo = (String) notificacoesModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(workArea, 
            "Deseja excluir a notificação \"" + titulo + "\"?", 
            "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Simular exclusão (em um sistema real, isso excluiria do banco de dados)
            JOptionPane.showMessageDialog(workArea, 
                "Funcionalidade de exclusão em desenvolvimento!\n" +
                "Notificação ID: " + id + " seria excluída.",
                "Exclusão", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Tentativa de exclusão da notificação " + id + " por " + usuarioAtual);
        }
    }
}
