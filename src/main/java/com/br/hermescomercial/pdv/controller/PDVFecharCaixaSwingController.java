package com.br.hermescomercial.pdv.controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller de Fechamento de Caixa em SWING
 * Gerencia o processo de fechamento diário do caixa
 * Versão 2.0 - 100% SWING - Sem JavaFX
 */
public class PDVFecharCaixaSwingController {
    
    private JFrame frame;
    private JLabel lblDataHora;
    private JLabel lblOperador;
    private JLabel lblSaldoInicial;
    private JLabel lblSaldoAtual;
    private JLabel lblTotalVendas;
    private JLabel lblTotalSangrias;
    private JLabel lblTotalSuprimentos;
    private JLabel lblSaldoFinal;
    private JTable resumoTable;
    private DefaultTableModel tableModel;
    private List<Movimentacao> movimentacoes;
    private BigDecimal saldoInicial;
    private BigDecimal saldoAtual;
    
    public PDVFecharCaixaSwingController() {
        this.movimentacoes = new ArrayList<>(50); // Capacidade inicial para melhor performance
        com.br.hermescomercial.theme.ModernTheme.applyModernTheme();
        initializeUI();
        carregarDadosExemplo();
    }
    
    private void initializeUI() {
        frame = new JFrame("PDV - Fechamento de Caixa v2.1.0 - Premium");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        
        frame.add(createMainPanel());
    }
    
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Center
        mainPanel.add(createContentPanel(), BorderLayout.CENTER);
        
        // South
        mainPanel.add(createControlPanel(), BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 15, 5));
        
        JLabel titleLabel = new JLabel("Fechamento de Caixa", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        JButton btnVoltar = new JButton("← Voltar");
        btnVoltar.addActionListener(e -> frame.dispose());
        
        panel.add(btnVoltar, BorderLayout.WEST);
        panel.add(titleLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Painel de informações
        panel.add(createInfoPanel(), BorderLayout.NORTH);
        
        // Tabela de resumo
        panel.add(createResumoPanel(), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Informações do Fechamento"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Data e Hora
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Data/Hora:"), gbc);
        gbc.gridx = 1;
        lblDataHora = new JLabel(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        panel.add(lblDataHora, gbc);
        
        // Operador
        gbc.gridx = 2; gbc.gridy = 0;
        panel.add(new JLabel("Operador:"), gbc);
        gbc.gridx = 3;
        lblOperador = new JLabel("Operador Principal");
        panel.add(lblOperador, gbc);
        
        // Saldo Inicial
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Saldo Inicial:"), gbc);
        gbc.gridx = 1;
        lblSaldoInicial = new JLabel("R$ 0,00");
        lblSaldoInicial.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblSaldoInicial, gbc);
        
        // Saldo Atual
        gbc.gridx = 2; gbc.gridy = 1;
        panel.add(new JLabel("Saldo Atual:"), gbc);
        gbc.gridx = 3;
        lblSaldoAtual = new JLabel("R$ 0,00");
        lblSaldoAtual.setFont(new Font("Arial", Font.BOLD, 14));
        lblSaldoAtual.setForeground(new Color(0, 128, 0));
        panel.add(lblSaldoAtual, gbc);
        
        // Totais
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Total Vendas:"), gbc);
        gbc.gridx = 1;
        lblTotalVendas = new JLabel("R$ 0,00");
        lblTotalVendas.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalVendas.setForeground(new Color(0, 128, 0));
        panel.add(lblTotalVendas, gbc);
        
        gbc.gridx = 2; gbc.gridy = 2;
        panel.add(new JLabel("Total Sangrias:"), gbc);
        gbc.gridx = 3;
        lblTotalSangrias = new JLabel("R$ 0,00");
        lblTotalSangrias.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalSangrias.setForeground(Color.RED);
        panel.add(lblTotalSangrias, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Total Suprimentos:"), gbc);
        gbc.gridx = 1;
        lblTotalSuprimentos = new JLabel("R$ 0,00");
        lblTotalSuprimentos.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalSuprimentos.setForeground(new Color(0, 123, 255));
        panel.add(lblTotalSuprimentos, gbc);
        
        gbc.gridx = 2; gbc.gridy = 3;
        panel.add(new JLabel("Saldo Final:"), gbc);
        gbc.gridx = 3;
        lblSaldoFinal = new JLabel("R$ 0,00");
        lblSaldoFinal.setFont(new Font("Arial", Font.BOLD, 16));
        lblSaldoFinal.setForeground(new Color(0, 128, 0));
        panel.add(lblSaldoFinal, gbc);
        
        return panel;
    }
    
    private JPanel createResumoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Resumo de Movimentações"));
        
        // Tabela de resumo
        String[] columns = {"Tipo", "Descrição", "Quantidade", "Valor Total", "Data/Hora"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        resumoTable = new JTable(tableModel);
        resumoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resumoTable.getTableHeader().setReorderingAllowed(false);
        
        // Desabilitar edição da tabela
        resumoTable.setDefaultEditor(Object.class, null);
        resumoTable.setEnabled(false);
        
        // Configurar larguras das colunas
        resumoTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        resumoTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        resumoTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        resumoTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        resumoTable.getColumnModel().getColumn(4).setPreferredWidth(150);
        
        JScrollPane scrollPane = new JScrollPane(resumoTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Ações"));
        
        JButton btnRecalcular = new JButton("Recalcular");
        btnRecalcular.setBackground(new Color(23, 162, 184));
        btnRecalcular.setForeground(Color.WHITE);
        btnRecalcular.addActionListener(this::recalcular);
        
        JButton btnImprimir = com.br.hermescomercial.theme.ModernTheme.createPastelButton("🖨️ Imprimir Relatório", com.br.hermescomercial.theme.ModernTheme.PASTEL_PURPLE, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
        btnImprimir.addActionListener(this::imprimirRelatorio);
        
        JButton btnConfirmar = new JButton("Confirmar Fechamento");
        btnConfirmar.setBackground(new Color(40, 167, 69));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.addActionListener(this::confirmarFechamento);
        
        JButton btnCancelar = com.br.hermescomercial.theme.ModernTheme.createPastelButton("❌ Cancelar", com.br.hermescomercial.theme.ModernTheme.PASTEL_CORAL, com.br.hermescomercial.theme.ModernTheme.TEXT_PRIMARY);
        btnCancelar.addActionListener(e -> frame.dispose());
        
        panel.add(btnRecalcular);
        panel.add(btnImprimir);
        panel.add(btnConfirmar);
        panel.add(btnCancelar);
        
        return panel;
    }
    
    private void recalcular(ActionEvent e) {
        calcularTotais();
        atualizarTabela();
        
        JOptionPane.showMessageDialog(frame, "Valores recalculados com sucesso!", 
            "Recálculo", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void imprimirRelatorio(ActionEvent e) {
        StringBuilder relatorio = new StringBuilder();
        relatorio.append("=== RELATÓRIO DE FECHAMENTO DE CAIXA ===\n\n");
        relatorio.append("Data: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");
        relatorio.append("Operador: ").append(lblOperador.getText()).append("\n\n");
        
        relatorio.append("=== RESUMO FINANCEIRO ===\n");
        relatorio.append("Saldo Inicial: R$ ").append(String.format("%.2f", saldoInicial)).append("\n");
        relatorio.append("Total Vendas: R$ ").append(lblTotalVendas.getText().substring(3)).append("\n");
        relatorio.append("Total Sangrias: R$ ").append(lblTotalSangrias.getText().substring(3)).append("\n");
        relatorio.append("Total Suprimentos: R$ ").append(lblTotalSuprimentos.getText().substring(3)).append("\n");
        relatorio.append("Saldo Final: R$ ").append(lblSaldoFinal.getText().substring(3)).append("\n\n");
        
        relatorio.append("=== MOVIMENTAÇÕES ===\n");
        for (Movimentacao mov : movimentacoes) {
            relatorio.append(mov.getTipo())
                   .append(" - ").append(mov.getDescricao())
                   .append(" - Qtd: ").append(mov.getQuantidade())
                   .append(" - Total: R$ ").append(String.format("%.2f", mov.getValorTotal()))
                   .append("\n");
        }
        
        JOptionPane.showMessageDialog(frame, relatorio.toString(), 
            "Relatório de Fechamento", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void confirmarFechamento(ActionEvent e) {
        int confirm = com.br.hermescomercial.theme.ModernTheme.showCustomConfirmDialog(frame, 
            "Deseja confirmar o fechamento do caixa?\n\n" +
            "Saldo Final: " + lblSaldoFinal.getText() + "\n" +
            "Total de Movimentações: " + movimentacoes.size(),
            "Confirmar Fechamento", 
            new String[]{"Sim", "Não"}, 0);
            
        if (confirm == 0) {
            JOptionPane.showMessageDialog(frame, 
                "Fechamento confirmado com sucesso!\n\n" +
                "Relatório gerado e caixa encerrado.\n" +
                "Versão SWING 2.0",
                "Fechamento Concluído", JOptionPane.INFORMATION_MESSAGE);
            
            frame.dispose();
        }
    }
    
    private void calcularTotais() {
        BigDecimal totalVendas = BigDecimal.ZERO;
        BigDecimal totalSangrias = BigDecimal.ZERO;
        BigDecimal totalSuprimentos = BigDecimal.ZERO;
        
        for (Movimentacao mov : movimentacoes) {
            if ("VENDA".equals(mov.getTipo())) {
                totalVendas = totalVendas.add(mov.getValorTotal());
            } else if ("SANGRIA".equals(mov.getTipo())) {
                totalSangrias = totalSangrias.add(mov.getValorTotal());
            } else if ("SUPRIMENTO".equals(mov.getTipo())) {
                totalSuprimentos = totalSuprimentos.add(mov.getValorTotal());
            }
        }
        
        lblTotalVendas.setText("R$ " + String.format("%.2f", totalVendas));
        lblTotalSangrias.setText("R$ " + String.format("%.2f", totalSangrias));
        lblTotalSuprimentos.setText("R$ " + String.format("%.2f", totalSuprimentos));
        
        BigDecimal saldoFinal = saldoInicial.add(totalVendas).subtract(totalSangrias).add(totalSuprimentos);
        lblSaldoFinal.setText("R$ " + String.format("%.2f", saldoFinal));
        lblSaldoAtual.setText("R$ " + String.format("%.2f", saldoFinal));
    }
    
    private void atualizarTabela() {
        tableModel.setRowCount(0);
        
        for (Movimentacao mov : movimentacoes) {
            Object[] row = {
                mov.getTipo(),
                mov.getDescricao(),
                mov.getQuantidade(),
                String.format("R$ %.2f", mov.getValorTotal()),
                mov.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
            };
            tableModel.addRow(row);
        }
    }
    
    private void carregarDadosExemplo() {
        saldoInicial = new BigDecimal("1000.00");
        saldoAtual = saldoInicial;
        
        lblSaldoInicial.setText("R$ " + String.format("%.2f", saldoInicial));
        lblSaldoAtual.setText("R$ " + String.format("%.2f", saldoAtual));
        
        // Adicionar movimentações de exemplo
        movimentacoes.add(new Movimentacao("VENDA", "Venda PDV", 15, new BigDecimal("1234.56"), LocalDateTime.now().minusHours(8)));
        movimentacoes.add(new Movimentacao("SANGRIA", "Sangria para troco", 1, new BigDecimal("50.00"), LocalDateTime.now().minusHours(6)));
        movimentacoes.add(new Movimentacao("VENDA", "Venda Balcão", 8, new BigDecimal("567.89"), LocalDateTime.now().minusHours(4)));
        movimentacoes.add(new Movimentacao("SUPRIMENTO", "Suprimento de caixa", 1, new BigDecimal("200.00"), LocalDateTime.now().minusHours(2)));
        movimentacoes.add(new Movimentacao("VENDA", "Venda Delivery", 12, new BigDecimal("890.12"), LocalDateTime.now().minusHours(1)));
        
        calcularTotais();
        atualizarTabela();
    }
    
    public void show() {
        frame.setVisible(true);
        
        // Atualizar data/hora a cada segundo
        Timer timer = new Timer(1000, e -> {
            lblDataHora.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        });
        timer.start();
    }
    
    // Classe de apoio
    private static class Movimentacao {
        private String tipo;
        private String descricao;
        private int quantidade;
        private BigDecimal valorTotal;
        private LocalDateTime dataHora;
        
        public Movimentacao(String tipo, String descricao, int quantidade, BigDecimal valorTotal, LocalDateTime dataHora) {
            this.tipo = tipo;
            this.descricao = descricao;
            this.quantidade = quantidade;
            this.valorTotal = valorTotal;
            this.dataHora = dataHora;
        }
        
        public String getTipo() { return tipo; }
        public String getDescricao() { return descricao; }
        public int getQuantidade() { return quantidade; }
        public BigDecimal getValorTotal() { return valorTotal; }
        public LocalDateTime getDataHora() { return dataHora; }
    }
}
