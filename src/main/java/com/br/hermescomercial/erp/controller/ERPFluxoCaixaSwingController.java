package com.br.hermescomercial.erp.controller;

import com.br.hermescomercial.ui.layout.LayoutPadrao;
import com.br.hermescomercial.ui.layout.MenuColors;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

/**
 * Controller para tela de Fluxo de Caixa no ERP
 * Versão 3.0.0 - Interface com headers coloridos
 */
public class ERPFluxoCaixaSwingController {
    
    private JFrame frame;
    
    public JFrame getFrame() {
        return frame;
    }
    private JPanel mainPanel;
    private JTable fluxoTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> cbPeriodo, cbAno, cbMes;
    private JButton btnFiltrar, btnExportar, btnAtualizar;
    
    public ERPFluxoCaixaSwingController() {
        initializeUI();
        carregarDadosExemplo();
    }
    
    private void initializeUI() {
        frame = new JFrame("💰 Fluxo de Caixa - ERP");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        
        frame.getContentPane().setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        createMainPanel();
        frame.add(mainPanel);
        frame.setVisible(true);
    }
    
    private void createMainPanel() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Header colorido
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Painel central
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        
        // Painel de filtros
        centerPanel.add(createFiltroPanel(), BorderLayout.NORTH);
        
        // Painel de resumo
        centerPanel.add(createResumoPanel(), BorderLayout.CENTER);
        
        // Tabela
        centerPanel.add(createTablePanel(), BorderLayout.SOUTH);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Painel de botões
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        // Header colorido para o setor financeiro
        JPanel headerColorido = MenuColors.criarHeaderSetor("financeiro", "💰 Fluxo de Caixa");
        
        JButton btnVoltar = LayoutPadrao.criarBotaoSecundario("← Voltar");
        btnVoltar.addActionListener(e -> frame.dispose());
        
        headerPanel.add(btnVoltar, BorderLayout.WEST);
        headerPanel.add(headerColorido, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JPanel createFiltroPanel() {
        JPanel filtroPanel = new JPanel(new GridBagLayout());
        filtroPanel.setOpaque(false);
        filtroPanel.setBorder(BorderFactory.createTitledBorder("🔍 Filtros"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Linha 1
        gbc.gridx = 0; gbc.gridy = 0;
        filtroPanel.add(new JLabel("Período:"), gbc);
        
        gbc.gridx = 1;
        cbPeriodo = new JComboBox<>(new String[]{"Hoje", "Esta Semana", "Este Mês", "Mês Anterior", "Este Ano", "Personalizado"});
        filtroPanel.add(cbPeriodo, gbc);
        
        gbc.gridx = 2;
        filtroPanel.add(new JLabel("Mês:"), gbc);
        
        gbc.gridx = 3;
        cbMes = new JComboBox<>(new String[]{"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", 
                                      "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"});
        cbMes.setSelectedIndex(LocalDate.now().getMonthValue() - 1);
        filtroPanel.add(cbMes, gbc);
        
        gbc.gridx = 4;
        filtroPanel.add(new JLabel("Ano:"), gbc);
        
        gbc.gridx = 5;
        String[] anos = new String[5];
        int anoAtual = LocalDate.now().getYear();
        for (int i = 0; i < 5; i++) {
            anos[i] = String.valueOf(anoAtual - 2 + i);
        }
        cbAno = new JComboBox<>(anos);
        cbAno.setSelectedItem(String.valueOf(anoAtual));
        filtroPanel.add(cbAno, gbc);
        
        gbc.gridx = 6;
        btnFiltrar = LayoutPadrao.criarBotaoPrimario("🔍 Filtrar");
        btnFiltrar.addActionListener(e -> filtrarFluxo());
        filtroPanel.add(btnFiltrar, gbc);
        
        return filtroPanel;
    }
    
    private JPanel createResumoPanel() {
        JPanel resumoPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        resumoPanel.setOpaque(false);
        resumoPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        // Saldo Anterior
        JPanel panelSaldoAnterior = criarPainelResumo("Saldo Anterior", "R$ 25.680,00", LayoutPadrao.COR_SECUNDARIA);
        resumoPanel.add(panelSaldoAnterior);
        
        // Entradas
        JPanel panelEntradas = criarPainelResumo("Entradas", "R$ 45.230,00", LayoutPadrao.COR_SUCESSO);
        resumoPanel.add(panelEntradas);
        
        // Saídas
        JPanel panelSaidas = criarPainelResumo("Saídas", "R$ 32.450,00", LayoutPadrao.COR_PERIGO);
        resumoPanel.add(panelSaidas);
        
        // Saldo Atual
        JPanel panelSaldoAtual = criarPainelResumo("Saldo Atual", "R$ 38.460,00", LayoutPadrao.COR_PRIMARIA);
        resumoPanel.add(panelSaldoAtual);
        
        return resumoPanel;
    }
    
    private JPanel criarPainelResumo(String titulo, String valor, Color cor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(cor, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(LayoutPadrao.FONTE_SUBTITULO);
        lblTitulo.setForeground(LayoutPadrao.COR_TEXTO_CLARO);
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Arial", Font.BOLD, 18));
        lblValor.setForeground(cor);
        lblValor.setHorizontalAlignment(SwingConstants.CENTER);
        
        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(lblValor, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.setBorder(BorderFactory.createTitledBorder("📋 Movimentações"));
        
        // Modelo da tabela
        String[] colunas = {"Data", "Descrição", "Tipo", "Categoria", "Valor", "Saldo", "Usuário"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        fluxoTable = new JTable(tableModel);
        fluxoTable.setFont(LayoutPadrao.FONTE_TEXTO);
        fluxoTable.setRowHeight(25);
        fluxoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fluxoTable.getTableHeader().setFont(LayoutPadrao.FONTE_SUBTITULO);
        
        // Configurar largura das colunas
        fluxoTable.getColumnModel().getColumn(0).setPreferredWidth(100); // Data
        fluxoTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Descrição
        fluxoTable.getColumnModel().getColumn(2).setPreferredWidth(80);  // Tipo
        fluxoTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Categoria
        fluxoTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Valor
        fluxoTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Saldo
        fluxoTable.getColumnModel().getColumn(6).setPreferredWidth(100); // Usuário
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(fluxoTable);
        scrollPane.setPreferredSize(new Dimension(0, 250));
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        btnAtualizar = LayoutPadrao.criarBotaoSecundario("🔄 Atualizar");
        btnAtualizar.addActionListener(e -> atualizarDados());
        
        btnExportar = LayoutPadrao.criarBotaoPrimario("📄 Exportar");
        btnExportar.addActionListener(e -> exportarDados());
        
        buttonPanel.add(btnAtualizar);
        buttonPanel.add(btnExportar);
        
        return buttonPanel;
    }
    
    private void carregarDadosExemplo() {
        // Adicionar dados de exemplo
        Object[][] dados = {
            {"01/05/2024", "Saldo Anterior", "SALDO", "Inicial", "25.680,00", "25.680,00", "Sistema"},
            {"02/05/2024", "Venda de Produtos", "ENTRADA", "Vendas", "8.500,00", "34.180,00", "João"},
            {"03/05/2024", "Pagamento Fornecedor", "SAÍDA", "Despesas", "2.300,00", "31.880,00", "Maria"},
            {"04/05/2024", "Serviço de Consultoria", "ENTRADA", "Serviços", "3.200,00", "35.080,00", "Pedro"},
            {"05/05/2024", "Aluguel", "SAÍDA", "Fixas", "3.500,00", "31.580,00", "Admin"},
            {"06/05/2024", "Venda PDV", "ENTRADA", "Vendas", "12.450,00", "44.030,00", "Ana"},
            {"07/05/2024", "Material Escritório", "SAÍDA", "Material", "1.800,00", "42.230,00", "Carlos"},
            {"08/05/2024", "Recebimento Cliente", "ENTRADA", "Recebimentos", "15.000,00", "57.230,00", "João"},
            {"09/05/2024", "Impostos", "SAÍDA", "Impostos", "4.200,00", "53.030,00", "Admin"},
            {"10/05/2024", "Serviços", "ENTRADA", "Serviços", "6.200,00", "59.230,00", "Maria"}
        };
        
        for (Object[] linha : dados) {
            tableModel.addRow(linha);
        }
    }
    
    private void filtrarFluxo() {
        JOptionPane.showMessageDialog(frame, "Funcionalidade de filtro em desenvolvimento", 
            "Informação", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void atualizarDados() {
        JOptionPane.showMessageDialog(frame, "Funcionalidade de atualização em desenvolvimento", 
            "Informação", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void exportarDados() {
        JOptionPane.showMessageDialog(frame, "Funcionalidade de exportação em desenvolvimento", 
            "Informação", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void show() {
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ERPFluxoCaixaSwingController();
        });
    }
}
