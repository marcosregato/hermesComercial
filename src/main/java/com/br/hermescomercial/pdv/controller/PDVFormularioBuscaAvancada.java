package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.ui.layout.LayoutPadrao;
import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe especializada para formulário de busca avançada
 * Estrutura: Header → Busca → Filtros → Resultados
 * Versão 1.0.0 - Adaptada para PDVMenuLateralElegante
 */
public class PDVFormularioBuscaAvancada {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes do formulário
    private JTextField txtBusca;
    private JComboBox<String> cmbTipoBusca;
    private JCheckBox chkProduto, chkCliente, chkVenda, chkFornecedor;
    private JTable resultadosTable;
    private DefaultTableModel tableModel;
    private JTable favoritosTable;
    private DefaultTableModel favoritosModel;
    private List<String> historicoBuscas;
    private JTabbedPane tabbedPane;
    
    // Cores
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private static final Color DANGER_COLOR = new Color(244, 67, 54);
    private static final Color PRIMARY_COLOR = new Color(33, 150, 243);
    
    public PDVFormularioBuscaAvancada(JPanel workArea, String usuario, String nome) {
        this.workArea = workArea;
        this.usuarioAtual = usuario;
        this.nomeUsuario = nome;
        this.historicoBuscas = new ArrayList<>();
    }
    
    public JPanel criarFormularioBuscaAvancada() {
        SystemLogger.ui("=== CRIANDO FORMULÁRIO BUSCA AVANÇADA ===");
        SystemLogger.ui("Usuário: " + usuarioAtual);
        
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(Color.WHITE);
        
        // Header
        painelPrincipal.add(criarHeader(), BorderLayout.NORTH);
        
        // Conteúdo principal com abas
        JPanel conteudo = new JPanel(new BorderLayout());
        conteudo.setBackground(Color.WHITE);
        
        // Abas
        conteudo.add(criarAbas(), BorderLayout.CENTER);
        
        painelPrincipal.add(conteudo, BorderLayout.CENTER);
        
        SystemLogger.ui("Formulário Busca Avançada criado com sucesso");
        return painelPrincipal;
    }
    
    private JPanel criarHeader() {
        JPanel header = LayoutPadrao.criarHeaderPanel("🔍 Busca Avançada");
        
        // Informações do usuário
        JPanel userInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userInfo.setBackground(new Color(250, 250, 250));
        userInfo.add(new JLabel("👤 " + nomeUsuario));
        userInfo.add(new JLabel(" | "));
        userInfo.add(new JLabel("📅 " + java.time.LocalDate.now()));
        
        header.add(userInfo, BorderLayout.EAST);
        return header;
    }
    
    private JTabbedPane criarAbas() {
        tabbedPane = new JTabbedPane();
        
        // Aba de Busca Avançada
        tabbedPane.addTab("🔍 Busca Avançada", criarAbaBuscaAvancada());
        
        // Aba de Favoritos
        tabbedPane.addTab("⭐ Favoritos", criarAbaFavoritos());
        
        // Aba de Histórico
        tabbedPane.addTab("📜 Histórico", criarAbaHistorico());
        
        // Aba de Configurações
        tabbedPane.addTab("⚙️ Configurações", criarAbaConfiguracoes());
        
        return tabbedPane;
    }
    
    private JPanel criarAbaBuscaAvancada() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Painel de busca
        JPanel buscaPanel = LayoutPadrao.criarPainelComBorda("🔍 Busca Avançada");
        
        JPanel buscaContainer = new JPanel(new GridBagLayout());
        buscaContainer.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Campo de busca
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        buscaContainer.add(new JLabel("Termo de Busca:"), gbc);
        gbc.gridy = 1;
        txtBusca = new JTextField();
        txtBusca.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        buscaContainer.add(txtBusca, gbc);
        
        // Tipo de busca
        gbc.gridy = 2; gbc.gridwidth = 1; gbc.weightx = 0;
        buscaContainer.add(new JLabel("Tipo de Busca:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        cmbTipoBusca = new JComboBox<>(new String[]{
            "Todos os Campos", "Código", "Nome", "Descrição", "CPF/CNPJ", "Telefone", "Email"
        });
        buscaContainer.add(cmbTipoBusca, gbc);
        
        // Filtros
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        buscaContainer.add(new JLabel("Buscar em:"), gbc);
        gbc.gridy = 4;
        
        JPanel checkboxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        checkboxPanel.setBackground(Color.WHITE);
        
        chkProduto = new JCheckBox("Produtos");
        chkCliente = new JCheckBox("Clientes");
        chkVenda = new JCheckBox("Vendas");
        chkFornecedor = new JCheckBox("Fornecedores");
        
        // Marcar todos por padrão
        chkProduto.setSelected(true);
        chkCliente.setSelected(true);
        chkVenda.setSelected(true);
        chkFornecedor.setSelected(true);
        
        checkboxPanel.add(chkProduto);
        checkboxPanel.add(chkCliente);
        checkboxPanel.add(chkVenda);
        checkboxPanel.add(chkFornecedor);
        
        buscaContainer.add(checkboxPanel, gbc);
        
        // Botões de ação
        gbc.gridy = 5;
        JPanel botoesPanel = new JPanel(new FlowLayout());
        botoesPanel.setBackground(Color.WHITE);
        
        JButton btnBuscar = criarBotao("🔍 Buscar", PRIMARY_COLOR);
        JButton btnLimpar = criarBotao("🔄 Limpar", PRIMARY_COLOR);
        
        btnBuscar.addActionListener(e -> realizarBusca());
        btnLimpar.addActionListener(e -> limparBusca());
        
        botoesPanel.add(btnBuscar);
        botoesPanel.add(btnLimpar);
        
        buscaContainer.add(botoesPanel, gbc);
        
        buscaPanel.add(buscaContainer, BorderLayout.CENTER);
        panel.add(buscaPanel, BorderLayout.NORTH);
        
        // Tabela de resultados
        JPanel resultadosPanel = LayoutPadrao.criarPainelComBorda("📋 Resultados da Busca");
        
        String[] columns = {"Tipo", "ID", "Descrição", "Detalhes", "Data"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        resultadosTable = new JTable(tableModel);
        resultadosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultadosTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    abrirResultadoSelecionado();
                }
            }
        });
        
        JScrollPane tableScrollPane = new JScrollPane(resultadosTable);
        resultadosPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Painel de ações dos resultados
        JPanel acoesPanel = new JPanel(new FlowLayout());
        acoesPanel.setBackground(Color.WHITE);
        
        JButton btnAbrir = criarBotao("👁️ Abrir", SUCCESS_COLOR);
        JButton btnFavoritar = criarBotao("⭐ Favoritar", PRIMARY_COLOR);
        
        btnAbrir.addActionListener(e -> abrirResultadoSelecionado());
        btnFavoritar.addActionListener(e -> adicionarAosFavoritos());
        
        acoesPanel.add(btnAbrir);
        acoesPanel.add(btnFavoritar);
        
        resultadosPanel.add(acoesPanel, BorderLayout.SOUTH);
        
        panel.add(resultadosPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel criarAbaFavoritos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Painel superior
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);
        
        JButton btnAtualizar = criarBotao("🔄 Atualizar", SUCCESS_COLOR);
        JButton btnRemover = criarBotao("🗑️ Remover Selecionado", DANGER_COLOR);
        
        btnAtualizar.addActionListener(e -> carregarFavoritos());
        btnRemover.addActionListener(e -> removerFavorito());
        
        topPanel.add(btnAtualizar);
        topPanel.add(btnRemover);
        
        panel.add(topPanel, BorderLayout.NORTH);
        
        // Tabela de favoritos
        JPanel favoritosPanel = LayoutPadrao.criarPainelComBorda("⭐ Buscas Favoritas");
        
        String[] columns = {"Nome", "Tipo", "Critério", "Data Criação"};
        favoritosModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        favoritosTable = new JTable(favoritosModel);
        favoritosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        favoritosTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    executarFavoritoSelecionado();
                }
            }
        });
        
        JScrollPane tableScrollPane = new JScrollPane(favoritosTable);
        favoritosPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        panel.add(favoritosPanel, BorderLayout.CENTER);
        
        // Carregar favoritos
        carregarFavoritos();
        
        return panel;
    }
    
    private JPanel criarAbaHistorico() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Painel superior
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);
        
        JButton btnLimparHistorico = criarBotao("🗑️ Limpar Histórico", DANGER_COLOR);
        JButton btnExportar = criarBotao("📤 Exportar", PRIMARY_COLOR);
        
        btnLimparHistorico.addActionListener(e -> limparHistorico());
        btnExportar.addActionListener(e -> exportarHistorico());
        
        topPanel.add(btnLimparHistorico);
        topPanel.add(btnExportar);
        
        panel.add(topPanel, BorderLayout.NORTH);
        
        // Lista de histórico
        JPanel historicoPanel = LayoutPadrao.criarPainelComBorda("📜 Histórico de Buscas");
        
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> historicoList = new JList<>(listModel);
        historicoList.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        historicoList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    String selecionado = historicoList.getSelectedValue();
                    if (selecionado != null) {
                        txtBusca.setText(selecionado);
                        tabbedPane.setSelectedIndex(0); // Mudar para aba de busca
                    }
                }
            }
        });
        
        // Adicionar histórico simulado
        listModel.addElement("produto notebook");
        listModel.addElement("cliente joão");
        listModel.addElement("venda 12345");
        listModel.addElement("fornecedor papelaria");
        listModel.addElement("estoque baixo");
        
        JScrollPane scrollPane = new JScrollPane(historicoList);
        historicoPanel.add(scrollPane, BorderLayout.CENTER);
        
        panel.add(historicoPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel criarAbaConfiguracoes() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Configurações de busca
        JPanel configPanel = LayoutPadrao.criarPainelComBorda("⚙️ Configurações de Busca");
        
        JPanel configContainer = new JPanel(new GridBagLayout());
        configContainer.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Opções de configuração
        gbc.gridy = 0;
        JCheckBox chkBuscaRapida = new JCheckBox("Habilitar busca rápida (Ctrl+F)");
        chkBuscaRapida.setBackground(Color.WHITE);
        chkBuscaRapida.setSelected(true);
        configContainer.add(chkBuscaRapida, gbc);
        
        gbc.gridy = 1;
        JCheckBox chkAutoCompletar = new JCheckBox("Auto-completar sugestões");
        chkAutoCompletar.setBackground(Color.WHITE);
        chkAutoCompletar.setSelected(true);
        configContainer.add(chkAutoCompletar, gbc);
        
        gbc.gridy = 2;
        JCheckBox chkSalvarHistorico = new JCheckBox("Salvar histórico de buscas");
        chkSalvarHistorico.setBackground(Color.WHITE);
        chkSalvarHistorico.setSelected(true);
        configContainer.add(chkSalvarHistorico, gbc);
        
        gbc.gridy = 3;
        JCheckBox chkNotificacoes = new JCheckBox("Notificações de novos resultados");
        chkNotificacoes.setBackground(Color.WHITE);
        configContainer.add(chkNotificacoes, gbc);
        
        // Limite de resultados
        gbc.gridy = 4;
        configContainer.add(new JLabel("Limite de resultados:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> cmbLimite = new JComboBox<>(new String[]{"10", "25", "50", "100", "Todos"});
        cmbLimite.setSelectedItem("50");
        configContainer.add(cmbLimite, gbc);
        
        configPanel.add(configContainer, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnSalvarConfig = criarBotao("💾 Salvar Configurações", SUCCESS_COLOR);
        JButton btnRestaurar = criarBotao("🔄 Restaurar Padrão", DANGER_COLOR);
        
        btnSalvarConfig.addActionListener(e -> salvarConfiguracoes());
        btnRestaurar.addActionListener(e -> restaurarConfiguracoes());
        
        buttonPanel.add(btnSalvarConfig);
        buttonPanel.add(btnRestaurar);
        
        configPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        panel.add(configPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JButton criarBotao(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    private void realizarBusca() {
        String termo = txtBusca.getText().trim();
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite um termo para buscar!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Adicionar ao histórico
        if (!historicoBuscas.contains(termo)) {
            historicoBuscas.add(0, termo);
            if (historicoBuscas.size() > 50) {
                historicoBuscas.remove(historicoBuscas.size() - 1);
            }
        }
        
        // Simulação de busca
        limparResultados();
        
        Object[][] resultados = {
            {"Produto", 1, "Notebook Dell Inspiron", "R$ 3.500,00 - 10 unidades", "2026-05-04"},
            {"Cliente", 1, "João Silva", "joao@email.com - (11) 1234-5678", "2026-05-04"},
            {"Venda", 12345, "Venda #12345", "5 produtos - R$ 450,00", "2026-05-04"},
            {"Fornecedor", 1, "Papelaria ABC", "Material de escritório", "2026-05-04"}
        };
        
        for (Object[] resultado : resultados) {
            tableModel.addRow(resultado);
        }
        
        JOptionPane.showMessageDialog(workArea, "Busca realizada com sucesso!\n" +
            "Encontrados " + resultados.length + " resultados para '" + termo + "'", 
            "Resultados", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Busca realizada por " + usuarioAtual + ": " + termo);
    }
    
    private void limparBusca() {
        txtBusca.setText("");
        cmbTipoBusca.setSelectedIndex(0);
        chkProduto.setSelected(true);
        chkCliente.setSelected(true);
        chkVenda.setSelected(true);
        chkFornecedor.setSelected(true);
        limparResultados();
    }
    
    private void limparResultados() {
        tableModel.setRowCount(0);
    }
    
    private void abrirResultadoSelecionado() {
        int selectedRow = resultadosTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione um resultado para abrir!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String tipo = (String) tableModel.getValueAt(selectedRow, 0);
        String id = String.valueOf(tableModel.getValueAt(selectedRow, 1));
        String descricao = (String) tableModel.getValueAt(selectedRow, 2);
        
        JOptionPane.showMessageDialog(workArea, 
            "Abrindo " + tipo + " - ID: " + id + "\n" +
            "Descrição: " + descricao + "\n\n" +
            "Funcionalidade em desenvolvimento...", 
            "Abrir Resultado", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void adicionarAosFavoritos() {
        int selectedRow = resultadosTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione um resultado para favoritar!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String termo = txtBusca.getText().trim();
        String tipo = (String) tableModel.getValueAt(selectedRow, 0);
        
        // Simulação de adição aos favoritos
        Object[] favorito = {
            "Busca: " + termo + " (" + tipo + ")",
            tipo,
            termo,
            "2026-05-04 10:30"
        };
        
        favoritosModel.addRow(favorito);
        
        JOptionPane.showMessageDialog(workArea, "Adicionado aos favoritos com sucesso!", 
            "Favoritos", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void carregarFavoritos() {
        favoritosModel.setRowCount(0);
        
        // Simulação de dados
        Object[][] favoritos = {
            {"Produtos Eletrônicos", "Produto", "notebook", "2026-05-01 14:30"},
            {"Clientes Ativos", "Cliente", "ativo", "2026-05-02 09:15"},
            {"Vendas do Mês", "Venda", "2026-05", "2026-05-03 16:45"}
        };
        
        for (Object[] favorito : favoritos) {
            favoritosModel.addRow(favorito);
        }
    }
    
    private void removerFavorito() {
        int selectedRow = favoritosTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione um favorito para remover!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(workArea, 
            "Deseja realmente remover este favorito?", 
            "Confirmar Remoção", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            favoritosModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(workArea, "Favorito removido com sucesso!", 
                "Favoritos", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void executarFavoritoSelecionado() {
        int selectedRow = favoritosTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        String criterio = (String) favoritosModel.getValueAt(selectedRow, 2);
        txtBusca.setText(criterio);
        tabbedPane.setSelectedIndex(0); // Mudar para aba de busca
        realizarBusca();
    }
    
    private void limparHistorico() {
        int confirm = JOptionPane.showConfirmDialog(workArea, 
            "Deseja realmente limpar todo o histórico de buscas?", 
            "Confirmar Limpeza", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            historicoBuscas.clear();
            JOptionPane.showMessageDialog(workArea, "Histórico limpo com sucesso!", 
                "Histórico", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void exportarHistorico() {
        JOptionPane.showMessageDialog(workArea, "Histórico exportado com sucesso!\n" +
            "Arquivo: historico_buscas_" + System.currentTimeMillis() + ".txt", 
            "Exportação", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void salvarConfiguracoes() {
        JOptionPane.showMessageDialog(workArea, "Configurações salvas com sucesso!", 
            "Configurações", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void restaurarConfiguracoes() {
        JOptionPane.showMessageDialog(workArea, "Configurações restauradas para o padrão!", 
            "Configurações", JOptionPane.INFORMATION_MESSAGE);
    }
}
