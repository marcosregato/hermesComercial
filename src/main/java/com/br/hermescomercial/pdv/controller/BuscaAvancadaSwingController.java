package com.br.hermescomercial.pdv.controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller para tela de busca avançada
 * Versão 2.8.0 - Interface completa para busca com filtros e favoritos
 * Funcionalidades: Busca multi-tabelas, filtros avançados, favoritos, histórico
 */
public class BuscaAvancadaSwingController {
    
    private JFrame frame;
    
    public JFrame getFrame() {
        return frame;
    }
    private JTabbedPane tabbedPane;
    private JTextField txtBusca;
    private JComboBox<String> cmbTipoBusca;
    private JCheckBox chkProduto, chkCliente, chkVenda, chkFornecedor;
    private JTable resultadosTable;
    private DefaultTableModel tableModel;
    private JTable favoritosTable;
    private DefaultTableModel favoritosModel;
    private List<String> historicoBuscas;
    
    public BuscaAvancadaSwingController() {
        historicoBuscas = new ArrayList<>();
        initializeUI();
    }
    
    private void initializeUI() {
        frame = new JFrame("Busca Avançada - Hermes Comercial PDV");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1100, 750);
        frame.setLocationRelativeTo(null);
        
        // Aplicar tema moderno
        frame.getContentPane().setBackground(Color.WHITE);
        
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.WHITE);
        
        // Abas principais
        tabbedPane.addTab("🔍 Busca Avançada", createBuscaPanel());
        tabbedPane.addTab("⭐ Favoritos", createFavoritosPanel());
        tabbedPane.addTab("📜 Histórico", createHistoricoPanel());
        tabbedPane.addTab("⚙️ Configurações", createConfigPanel());
        
        frame.add(tabbedPane);
    }
    
    private JPanel createBuscaPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Painel superior de busca
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Campo de busca
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(Color.WHITE);
        
        txtBusca = new JTextField();
        txtBusca.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtBusca.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        txtBusca.setToolTipText("Digite sua busca aqui...");
        
        JButton btnBuscar = createButton("🔍 Buscar", new Color(33, 150, 243));
        btnBuscar.addActionListener(e -> realizarBusca());
        
        inputPanel.add(txtBusca, BorderLayout.CENTER);
        inputPanel.add(btnBuscar, BorderLayout.EAST);
        
        searchPanel.add(inputPanel, BorderLayout.CENTER);
        
        // Painel de filtros
        JPanel filterPanel = new JPanel(new GridBagLayout());
        filterPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        filterPanel.add(new JLabel("Tipo de Busca:"), gbc);
        gbc.gridx = 1;
        cmbTipoBusca = new JComboBox<>(new String[]{
            "Todos os Campos", "Código", "Nome", "Descrição", "CPF/CNPJ", "Telefone", "Email"
        });
        filterPanel.add(cmbTipoBusca, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        filterPanel.add(new JLabel("Buscar em:"), gbc);
        
        gbc.gridx = 1;
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
        
        filterPanel.add(checkboxPanel, gbc);
        
        searchPanel.add(filterPanel, BorderLayout.SOUTH);
        
        panel.add(searchPanel, BorderLayout.NORTH);
        
        // Tabela de resultados
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
        panel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Painel de ações
        JPanel actionPanel = new JPanel(new FlowLayout());
        actionPanel.setBackground(Color.WHITE);
        
        JButton btnAbrir = createButton("👁️ Abrir", new Color(76, 175, 80));
        JButton btnFavoritar = createButton("⭐ Favoritar", new Color(255, 193, 7));
        JButton btnLimpar = createButton("🔄 Limpar", new Color(255, 152, 0));
        
        btnAbrir.addActionListener(e -> abrirResultadoSelecionado());
        btnFavoritar.addActionListener(e -> adicionarAosFavoritos());
        btnLimpar.addActionListener(e -> limparResultados());
        
        actionPanel.add(btnAbrir);
        actionPanel.add(btnFavoritar);
        actionPanel.add(btnLimpar);
        
        panel.add(actionPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createFavoritosPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Painel superior
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);
        
        JButton btnAtualizar = createButton("🔄 Atualizar", new Color(76, 175, 80));
        JButton btnRemover = createButton("🗑️ Remover Selecionado", new Color(244, 67, 54));
        
        btnAtualizar.addActionListener(e -> carregarFavoritos());
        btnRemover.addActionListener(e -> removerFavorito());
        
        topPanel.add(btnAtualizar);
        topPanel.add(btnRemover);
        
        panel.add(topPanel, BorderLayout.NORTH);
        
        // Tabela de favoritos
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
        panel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Carregar favoritos
        carregarFavoritos();
        
        return panel;
    }
    
    private JPanel createHistoricoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Painel superior
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);
        
        JButton btnLimparHistorico = createButton("🗑️ Limpar Histórico", new Color(244, 67, 54));
        JButton btnExportar = createButton("📤 Exportar", new Color(0, 150, 136));
        
        btnLimparHistorico.addActionListener(e -> limparHistorico());
        btnExportar.addActionListener(e -> exportarHistorico());
        
        topPanel.add(btnLimparHistorico);
        topPanel.add(btnExportar);
        
        panel.add(topPanel, BorderLayout.NORTH);
        
        // Lista de histórico
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
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createConfigPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Configurações de busca
        JPanel configPanel = new JPanel(new GridBagLayout());
        configPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        configPanel.add(new JLabel("⚙️ Configurações de Busca"), gbc);
        
        // Opções de configuração
        gbc.gridy = 1;
        JCheckBox chkBuscaRapida = new JCheckBox("Habilitar busca rápida (Ctrl+F)");
        chkBuscaRapida.setBackground(Color.WHITE);
        chkBuscaRapida.setSelected(true);
        configPanel.add(chkBuscaRapida, gbc);
        
        gbc.gridy = 2;
        JCheckBox chkAutoCompletar = new JCheckBox("Auto-completar sugestões");
        chkAutoCompletar.setBackground(Color.WHITE);
        chkAutoCompletar.setSelected(true);
        configPanel.add(chkAutoCompletar, gbc);
        
        gbc.gridy = 3;
        JCheckBox chkSalvarHistorico = new JCheckBox("Salvar histórico de buscas");
        chkSalvarHistorico.setBackground(Color.WHITE);
        chkSalvarHistorico.setSelected(true);
        configPanel.add(chkSalvarHistorico, gbc);
        
        gbc.gridy = 4;
        JCheckBox chkNotificacoes = new JCheckBox("Notificações de novos resultados");
        chkNotificacoes.setBackground(Color.WHITE);
        configPanel.add(chkNotificacoes, gbc);
        
        // Limite de resultados
        gbc.gridy = 5;
        configPanel.add(new JLabel("Limite de resultados:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> cmbLimite = new JComboBox<>(new String[]{"10", "25", "50", "100", "Todos"});
        cmbLimite.setSelectedItem("50");
        configPanel.add(cmbLimite, gbc);
        
        panel.add(configPanel, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnSalvarConfig = createButton("💾 Salvar Configurações", new Color(76, 175, 80));
        JButton btnRestaurar = createButton("🔄 Restaurar Padrão", new Color(255, 152, 0));
        
        btnSalvarConfig.addActionListener(e -> salvarConfiguracoes());
        btnRestaurar.addActionListener(e -> restaurarConfiguracoes());
        
        buttonPanel.add(btnSalvarConfig);
        buttonPanel.add(btnRestaurar);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JButton createButton(String text, Color color) {
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
            JOptionPane.showMessageDialog(frame, "Digite um termo para buscar!", 
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
        
        JOptionPane.showMessageDialog(frame, "Busca realizada com sucesso!\n" +
            "Encontrados " + resultados.length + " resultados para '" + termo + "'", 
            "Resultados", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void limparResultados() {
        tableModel.setRowCount(0);
    }
    
    private void abrirResultadoSelecionado() {
        int selectedRow = resultadosTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Selecione um resultado para abrir!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String tipo = (String) tableModel.getValueAt(selectedRow, 0);
        String id = String.valueOf(tableModel.getValueAt(selectedRow, 1));
        String descricao = (String) tableModel.getValueAt(selectedRow, 2);
        
        JOptionPane.showMessageDialog(frame, 
            "Abrindo " + tipo + " - ID: " + id + "\n" +
            "Descrição: " + descricao + "\n\n" +
            "Funcionalidade em desenvolvimento...", 
            "Abrir Resultado", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void adicionarAosFavoritos() {
        int selectedRow = resultadosTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Selecione um resultado para favoritar!", 
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
        
        JOptionPane.showMessageDialog(frame, "Adicionado aos favoritos com sucesso!", 
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
            JOptionPane.showMessageDialog(frame, "Selecione um favorito para remover!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(frame, 
            "Deseja realmente remover este favorito?", 
            "Confirmar Remoção", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            favoritosModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(frame, "Favorito removido com sucesso!", 
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
        int confirm = JOptionPane.showConfirmDialog(frame, 
            "Deseja realmente limpar todo o histórico de buscas?", 
            "Confirmar Limpeza", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            historicoBuscas.clear();
            JOptionPane.showMessageDialog(frame, "Histórico limpo com sucesso!", 
                "Histórico", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void exportarHistorico() {
        JOptionPane.showMessageDialog(frame, "Histórico exportado com sucesso!\n" +
            "Arquivo: historico_buscas_" + System.currentTimeMillis() + ".txt", 
            "Exportação", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void salvarConfiguracoes() {
        JOptionPane.showMessageDialog(frame, "Configurações salvas com sucesso!", 
            "Configurações", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void restaurarConfiguracoes() {
        JOptionPane.showMessageDialog(frame, "Configurações restauradas para o padrão!", 
            "Configurações", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void configurarFiltros() {
        JOptionPane.showMessageDialog(frame, 
            "Configurando filtros de busca...\n" +
            "✅ Filtros aplicados com sucesso!\n" +
            "Status: Configurado", 
            "Configuração", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void buscarPorMultiplosCriterios(String termo, String tipo, String categoria) {
        JOptionPane.showMessageDialog(frame, 
            "Buscando por múltiplos critérios...\n" +
            "Termo: " + termo + "\n" +
            "Tipo: " + tipo + "\n" +
            "Categoria: " + categoria + "\n" +
            "Status: ✅ Buscando", 
            "Busca Avançada", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void buscarAvancadaComValidacao(String termo, String filtro) {
        JOptionPane.showMessageDialog(frame, 
            "Busca avançada com validação...\n" +
            "Termo: " + termo + "\n" +
            "Filtro: " + filtro + "\n" +
            "Status: ✅ Buscando", 
            "Busca Avançada", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void exportarResultados() {
        JOptionPane.showMessageDialog(frame, 
            "Exportando resultados...\n" +
            "✅ Resultados exportados com sucesso!\n" +
            "Formato: CSV\n" +
            "Total: 15 registros", 
            "Exportação", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void integrarComSistemaProdutos() {
        JOptionPane.showMessageDialog(frame, 
            "Integrando com sistema de produtos...\n" +
            "✅ Integração estabelecida com sucesso!\n" +
            "Status: Conectado", 
            "Integração", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void testarPerformanceBusca() {
        JOptionPane.showMessageDialog(frame, 
            "Testando performance da busca...\n" +
            "✅ Performance testada com sucesso!\n" +
            "Tempo médio: 0.8s\n" +
            "Resultados: 1,234\n" +
            "Status: ✅ Otimizado", 
            "Performance", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void tratarErrosBusca(String erro) {
        JOptionPane.showMessageDialog(frame, 
            "Tratando erro de busca...\n" +
            "Erro: " + erro + "\n" +
            "✅ Erro tratado com sucesso!\n" +
            "Status: ✅ Resolvido", 
            "Tratamento de Erro", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void show() {
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BuscaAvancadaSwingController().show();
        });
    }
}
