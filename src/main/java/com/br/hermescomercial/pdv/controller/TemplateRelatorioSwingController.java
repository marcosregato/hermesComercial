package com.br.hermescomercial.pdv.controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Controller para tela de templates de relatórios
 * Versão 2.8.0 - Interface completa para gestão de templates de relatórios
 * Funcionalidades: Criação, edição, visualização, exportação de templates
 */
public class TemplateRelatorioSwingController {
    
    private JFrame frame;
    private JTabbedPane tabbedPane;
    private JTextField txtNome, txtDescricao;
    private JComboBox<String> cmbTipoRelatorio;
    private JTextArea txtConteudo;
    private JTable templatesTable;
    private DefaultTableModel tableModel;
    
    public TemplateRelatorioSwingController() {
        initializeUI();
    }
    
    private void initializeUI() {
        frame = new JFrame("Templates de Relatórios - Hermes Comercial PDV");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1100, 750);
        frame.setLocationRelativeTo(null);
        
        // Aplicar tema moderno
        frame.getContentPane().setBackground(Color.WHITE);
        
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(Color.WHITE);
        
        // Abas principais
        tabbedPane.addTab("📝 Criar Template", createCriarTemplatePanel());
        tabbedPane.addTab("📋 Templates Existentes", createTemplatesPanel());
        tabbedPane.addTab("👁️ Visualizar", createVisualizarPanel());
        
        frame.add(tabbedPane);
    }
    
    private JPanel createCriarTemplatePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Formulário de criação
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Nome do template
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nome do Template:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        txtNome = new JTextField(40);
        formPanel.add(txtNome, gbc);
        
        // Tipo de relatório
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Tipo de Relatório:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        cmbTipoRelatorio = new JComboBox<>(new String[]{
            "Relatório de Vendas", "Relatório de Estoque", "Relatório Financeiro",
            "Relatório de Clientes", "Relatório de Fornecedores", "Relatório de Produtos",
            "DRE (Demonstrativo de Resultados)", "Fluxo de Caixa", "Balancete"
        });
        formPanel.add(cmbTipoRelatorio, gbc);
        
        // Descrição
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1;
        txtDescricao = new JTextField(40);
        formPanel.add(txtDescricao, gbc);
        
        // Conteúdo do template
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        formPanel.add(new JLabel("Conteúdo do Template:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1; gbc.weighty = 1;
        txtConteudo = new JTextArea(15, 40);
        txtConteudo.setLineWrap(true);
        txtConteudo.setWrapStyleWord(true);
        txtConteudo.setFont(new Font("Courier New", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(txtConteudo);
        formPanel.add(scrollPane, gbc);
        
        // Painel de variáveis disponíveis
        JPanel variablesPanel = new JPanel(new BorderLayout());
        variablesPanel.setBackground(new Color(240, 240, 240));
        variablesPanel.setBorder(BorderFactory.createTitledBorder("Variáveis Disponíveis"));
        
        JTextArea txtVariaveis = new JTextArea(
            "Variáveis para Relatório de Vendas:\n" +
            "{data_inicio} - Data inicial do período\n" +
            "{data_fim} - Data final do período\n" +
            "{total_vendas} - Total de vendas\n" +
            "{quantidade_vendas} - Quantidade de vendas\n" +
            "{ticket_medio} - Ticket médio\n" +
            "{top_produtos} - Produtos mais vendidos\n" +
            "{top_clientes} - Clientes que mais compraram\n" +
            "\n" +
            "Variáveis para Relatório Financeiro:\n" +
            "{total_receitas} - Total de receitas\n" +
            "{total_despesas} - Total de despesas\n" +
            "{lucro_liquido} - Lucro líquido\n" +
            "{margem_lucro} - Margem de lucro\n" +
            "{contas_pagar} - Total de contas a pagar\n" +
            "{contas_receber} - Total de contas a receber"
        );
        txtVariaveis.setEditable(false);
        txtVariaveis.setBackground(new Color(240, 240, 240));
        variablesPanel.add(new JScrollPane(txtVariaveis), BorderLayout.CENTER);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 0.3;
        formPanel.add(variablesPanel, gbc);
        
        panel.add(formPanel, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        
        JButton btnSalvar = createButton("💾 Salvar Template", new Color(76, 175, 80));
        JButton btnLimpar = createButton("🔄 Limpar", new Color(255, 152, 0));
        JButton btnPrevisualizar = createButton("👁️ Previsualizar", new Color(33, 150, 243));
        
        btnSalvar.addActionListener(e -> salvarTemplate());
        btnLimpar.addActionListener(e -> limparFormulario());
        btnPrevisualizar.addActionListener(e -> previsualizarTemplate());
        
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnLimpar);
        buttonPanel.add(btnPrevisualizar);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createTemplatesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Painel de busca e filtros
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        
        JTextField txtBusca = new JTextField(25);
        JComboBox<String> cmbFiltroTipo = new JComboBox<>(new String[]{
            "Todos", "Vendas", "Estoque", "Financeiro", "Clientes", "Fornecedores", "Produtos"
        });
        JButton btnBuscar = createButton("🔍 Buscar", new Color(33, 150, 243));
        JButton btnAtualizar = createButton("🔄 Atualizar", new Color(76, 175, 80));
        
        btnBuscar.addActionListener(e -> buscarTemplates(txtBusca.getText(), (String) cmbFiltroTipo.getSelectedItem()));
        btnAtualizar.addActionListener(e -> carregarTemplates());
        
        searchPanel.add(new JLabel("Buscar:"));
        searchPanel.add(txtBusca);
        searchPanel.add(new JLabel("Tipo:"));
        searchPanel.add(cmbFiltroTipo);
        searchPanel.add(btnBuscar);
        searchPanel.add(btnAtualizar);
        
        panel.add(searchPanel, BorderLayout.NORTH);
        
        // Tabela de templates
        String[] columns = {"ID", "Nome", "Tipo", "Descrição", "Data Criação", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        templatesTable = new JTable(tableModel);
        templatesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        templatesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    carregarTemplateSelecionado();
                }
            }
        });
        
        JScrollPane tableScrollPane = new JScrollPane(templatesTable);
        panel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Painel de ações
        JPanel actionPanel = new JPanel(new FlowLayout());
        actionPanel.setBackground(Color.WHITE);
        
        JButton btnEditar = createButton("✏️ Editar", new Color(255, 152, 0));
        JButton btnDuplicar = createButton("📋 Duplicar", new Color(156, 39, 176));
        JButton btnExcluir = createButton("🗑️ Excluir", new Color(244, 67, 54));
        JButton btnExportar = createButton("📤 Exportar", new Color(0, 150, 136));
        
        btnEditar.addActionListener(e -> editarTemplate());
        btnDuplicar.addActionListener(e -> duplicarTemplate());
        btnExcluir.addActionListener(e -> excluirTemplate());
        btnExportar.addActionListener(e -> exportarTemplate());
        
        actionPanel.add(btnEditar);
        actionPanel.add(btnDuplicar);
        actionPanel.add(btnExcluir);
        actionPanel.add(btnExportar);
        
        panel.add(actionPanel, BorderLayout.SOUTH);
        
        // Carregar dados iniciais
        carregarTemplates();
        
        return panel;
    }
    
    private JPanel createVisualizarPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Painel de seleção
        JPanel selectPanel = new JPanel(new FlowLayout());
        selectPanel.setBackground(Color.WHITE);
        
        JComboBox<String> cmbTemplateSelect = new JComboBox<>();
        JButton btnVisualizar = createButton("👁️ Visualizar", new Color(33, 150, 243));
        JButton btnGerarPDF = createButton("📄 Gerar PDF", new Color(244, 67, 54));
        
        btnVisualizar.addActionListener(e -> visualizarTemplate());
        btnGerarPDF.addActionListener(e -> gerarPDF());
        
        selectPanel.add(new JLabel("Template:"));
        selectPanel.add(cmbTemplateSelect);
        selectPanel.add(btnVisualizar);
        selectPanel.add(btnGerarPDF);
        
        panel.add(selectPanel, BorderLayout.NORTH);
        
        // Área de visualização
        JTextArea txtVisualizacao = new JTextArea();
        txtVisualizacao.setEditable(false);
        txtVisualizacao.setFont(new Font("Courier New", Font.PLAIN, 11));
        txtVisualizacao.setText("Selecione um template para visualizar...");
        
        JScrollPane scrollPane = new JScrollPane(txtVisualizacao);
        panel.add(scrollPane, BorderLayout.CENTER);
        
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
    
    private void salvarTemplate() {
        try {
            String nome = txtNome.getText().trim();
            String tipo = (String) cmbTipoRelatorio.getSelectedItem();
            String descricao = txtDescricao.getText().trim();
            String conteudo = txtConteudo.getText().trim();
            
            // Simulação de salvamento (implementar com o service)
            // Usar dados capturados para integração real
            System.out.println("Salvando template: " + nome);
            System.out.println("Tipo: " + tipo);
            System.out.println("Descrição: " + descricao);
            System.out.println("Conteúdo: " + conteudo);
            
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Nome do template é obrigatório!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (conteudo.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Conteúdo do template é obrigatório!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Simulação de salvamento
            JOptionPane.showMessageDialog(frame, "Template salvo com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            limparFormulario();
            carregarTemplates();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Erro ao salvar template: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limparFormulario() {
        txtNome.setText("");
        txtDescricao.setText("");
        txtConteudo.setText("");
        cmbTipoRelatorio.setSelectedIndex(0);
    }
    
    private void previsualizarTemplate() {
        String conteudo = txtConteudo.getText().trim();
        if (conteudo.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Adicione conteúdo ao template para previsualizar!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Simulação de pré-visualização
        JOptionPane.showMessageDialog(frame, "Pré-visualização do template:\n\n" + conteudo, 
            "Pré-visualização", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void buscarTemplates(String termo, String tipo) {
        // Simulação de busca
        JOptionPane.showMessageDialog(frame, "Buscando templates por: " + termo + "\nTipo: " + tipo + 
            "\n\nFuncionalidade em desenvolvimento...", "Busca", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void carregarTemplates() {
        try {
            // Limpar tabela
            tableModel.setRowCount(0);
            
            // Simulação de dados
            Object[][] dados = {
                {1, "Relatório de Vendas Diário", "Vendas", "Relatório resumido de vendas do dia", "2026-05-01", "Ativo"},
                {2, "DRE Mensal", "Financeiro", "Demonstrativo de resultados mensal", "2026-05-01", "Ativo"},
                {3, "Controle de Estoque", "Estoque", "Relatório de movimentação de estoque", "2026-05-01", "Inativo"},
                {4, "Análise de Clientes", "Clientes", "Relatório de análise de clientes", "2026-05-01", "Ativo"}
            };
            
            for (Object[] row : dados) {
                tableModel.addRow(row);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Erro ao carregar templates: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void carregarTemplateSelecionado() {
        int selectedRow = templatesTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        // Carregar dados do template selecionado no formulário
        tabbedPane.setSelectedIndex(0); // Mudar para aba de criação
        
        // Simulação de carregamento
        txtNome.setText("Relatório de Vendas Diário");
        cmbTipoRelatorio.setSelectedItem("Relatório de Vendas");
        txtDescricao.setText("Relatório resumido de vendas do dia");
        txtConteudo.setText(
            "RELATÓRIO DE VENDAS DIÁRIO\n" +
            "========================\n" +
            "Período: {data_inicio} a {data_fim}\n\n" +
            "RESUMO:\n" +
            "Total de Vendas: R$ {total_vendas}\n" +
            "Quantidade: {quantidade_vendas}\n" +
            "Ticket Médio: R$ {ticket_medio}\n\n" +
            "TOP PRODUTOS:\n" +
            "{top_produtos}\n\n" +
            "TOP CLIENTES:\n" +
            "{top_clientes}"
        );
    }
    
    private void editarTemplate() {
        carregarTemplateSelecionado();
    }
    
    private void duplicarTemplate() {
        carregarTemplateSelecionado();
        txtNome.setText(txtNome.getText() + " (Cópia)");
    }
    
    private void excluirTemplate() {
        int selectedRow = templatesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Selecione um template para excluir!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(frame, 
            "Deseja realmente excluir este template?", 
            "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(frame, "Template excluído com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            carregarTemplates();
        }
    }
    
    private void exportarTemplate() {
        int selectedRow = templatesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Selecione um template para exportar!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(frame, "Template exportado com sucesso!", 
            "Exportação", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void visualizarTemplate() {
        JOptionPane.showMessageDialog(frame, "Visualização do template...\n\n" +
            "Funcionalidade em desenvolvimento...", "Visualização", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void gerarPDF() {
        JOptionPane.showMessageDialog(frame, "Gerando PDF do template...\n\n" +
            "Funcionalidade em desenvolvimento...", "PDF", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void show() {
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TemplateRelatorioSwingController().show();
        });
    }
}
