package com.br.hermescomercial.controller;

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
 * Controller de Caixa em SWING
 * Gerencia operações de abertura e fechamento de caixa
 * Versão 2.0 - 100% SWING - Sem JavaFX
 */
public class PDVCaixaSwingController {
    
    private JFrame frame;
    private JLabel lblStatusCaixa;
    private JLabel lblSaldoInicial;
    private JLabel lblSaldoAtual;
    private JLabel lblDataHora;
    private JTable movimentacoesTable;
    private DefaultTableModel tableModel;
    private List<MovimentacaoCaixa> movimentacoes;
    private boolean caixaAberto = false;
    private BigDecimal saldoAtual = BigDecimal.ZERO;
    
    public PDVCaixaSwingController() {
        try {
            System.out.println("Iniciando PDVCaixaSwingController");
            this.movimentacoes = new ArrayList<>(50); // Capacidade inicial para melhor performance
            initializeUI();
            System.out.println("PDVCaixaSwingController inicializado com sucesso");
        } catch (Exception e) {
            System.err.println("Erro ao inicializar PDVCaixaSwingController: " + e.getMessage());
        }
    }
    
    private void initializeUI() {
        frame = new JFrame("PDV - Operações de Caixa v2.1.0 - Premium");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        
        // Configurar fundo padrão Nova Venda
        frame.getContentPane().setBackground(new Color(245, 245, 250));
        
        frame.add(createMainPanel());
    }
    
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 245, 250));
        
        // Header padrão Nova Venda
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Center - Status e Movimentações
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(createStatusPanel(), BorderLayout.NORTH);
        centerPanel.add(createMovimentacoesPanel(), BorderLayout.CENTER);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Bottom - Botões
        mainPanel.add(createButtonsPanel(), BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        panel.setBackground(new Color(41, 128, 185));
        panel.setPreferredSize(new Dimension(0, 80));
        
        // Título central
        JLabel titleLabel = new JLabel("💰 Operações de Caixa v2.1.0 - Premium", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        // Botão voltar estilizado
        JButton btnVoltar = new JButton("← Voltar");
        btnVoltar.setBackground(new Color(255, 255, 255));
        btnVoltar.setForeground(new Color(41, 128, 185));
        btnVoltar.setFont(new Font("Arial", Font.BOLD, 12));
        btnVoltar.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnVoltar.setFocusPainted(false);
        btnVoltar.addActionListener(e -> frame.dispose());
        
        // Data e hora atual
        JLabel lblDataHora = new JLabel(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), JLabel.RIGHT);
        lblDataHora.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDataHora.setForeground(Color.WHITE);
        
        panel.add(btnVoltar, BorderLayout.WEST);
        panel.add(titleLabel, BorderLayout.CENTER);
        panel.add(lblDataHora, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Status do Caixa"));
        
        lblStatusCaixa = new JLabel("Fechado", JLabel.CENTER);
        lblStatusCaixa.setFont(new Font("Arial", Font.BOLD, 16));
        lblStatusCaixa.setForeground(Color.RED);
        lblStatusCaixa.setOpaque(true);
        lblStatusCaixa.setBackground(Color.LIGHT_GRAY);
        
        lblSaldoInicial = new JLabel("R$ 0,00", JLabel.CENTER);
        lblSaldoInicial.setFont(new Font("Arial", Font.BOLD, 14));
        
        lblSaldoAtual = new JLabel("R$ 0,00", JLabel.CENTER);
        lblSaldoAtual.setFont(new Font("Arial", Font.BOLD, 14));
        
        lblDataHora = new JLabel(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), JLabel.CENTER);
        lblDataHora.setFont(new Font("Arial", Font.PLAIN, 12));
        
        panel.add(new JLabel("Status:"));
        panel.add(new JLabel("Saldo Inicial:"));
        panel.add(lblStatusCaixa);
        panel.add(lblSaldoInicial);
        
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        bottomPanel.add(new JLabel("Saldo Atual:"));
        bottomPanel.add(new JLabel("Data/Hora:"));
        bottomPanel.add(lblSaldoAtual);
        bottomPanel.add(lblDataHora);
        
        JPanel statusContainer = new JPanel(new BorderLayout());
        statusContainer.add(panel, BorderLayout.NORTH);
        statusContainer.add(bottomPanel, BorderLayout.SOUTH);
        
        return statusContainer;
    }
    
    private JPanel createMovimentacoesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Movimentações"));
        
        String[] columns = {"Data/Hora", "Tipo", "Descrição", "Valor", "Saldo"};
        tableModel = new DefaultTableModel(columns, 0);
        movimentacoesTable = new JTable(tableModel);
        
        JScrollPane scrollPane = new JScrollPane(movimentacoesTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton btnAbrirCaixa = new JButton("Abrir Caixa");
        btnAbrirCaixa.setBackground(new Color(0, 128, 0));
        btnAbrirCaixa.setForeground(Color.WHITE);
        btnAbrirCaixa.addActionListener(this::abrirCaixa);
        
        JButton btnAdicionarFundos = new JButton("Adicionar Fundos");
        btnAdicionarFundos.setBackground(new Color(0, 123, 255));
        btnAdicionarFundos.setForeground(Color.WHITE);
        btnAdicionarFundos.addActionListener(this::adicionarFundos);
        
        JButton btnRetirarFundos = new JButton("Retirar Fundos");
        btnRetirarFundos.setBackground(new Color(255, 193, 7));
        btnRetirarFundos.setForeground(Color.BLACK);
        btnRetirarFundos.addActionListener(this::retirarFundos);
        
        JButton btnFecharCaixa = new JButton("Fechar Caixa");
        btnFecharCaixa.setBackground(new Color(220, 53, 69));
        btnFecharCaixa.setForeground(Color.WHITE);
        btnFecharCaixa.addActionListener(this::fecharCaixa);
        
        JButton btnRelatorio = new JButton("Relatório");
        btnRelatorio.setBackground(new Color(108, 117, 125));
        btnRelatorio.setForeground(Color.WHITE);
        btnRelatorio.addActionListener(e -> gerarRelatorio(e));
        
        panel.add(btnAbrirCaixa);
        panel.add(btnAdicionarFundos);
        panel.add(btnRetirarFundos);
        panel.add(btnFecharCaixa);
        panel.add(btnRelatorio);
        
        return panel;
    }
    
    private void abrirCaixa(ActionEvent e) {
        try {
            System.out.println("Iniciando operação de abertura de caixa");
            
            if (caixaAberto) {
                System.out.println("Tentativa de abrir caixa já aberto");
                JOptionPane.showMessageDialog(frame, "Caixa já está aberto!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Simular abertura de caixa
            caixaAberto = true;
            BigDecimal saldoInicial = BigDecimal.valueOf(1000.00);
            saldoAtual = saldoInicial;
            
            // Adicionar movimentação de abertura
            MovimentacaoCaixa mov = new MovimentacaoCaixa(
                LocalDateTime.now(),
                "ABERTURA",
                "Abertura do caixa",
                saldoInicial,
                saldoAtual
            );
            movimentacoes.add(mov);
            atualizarTabela();
            
            // Atualizar status
            lblStatusCaixa.setText("Aberto");
            lblStatusCaixa.setForeground(Color.GREEN);
            lblSaldoInicial.setText("R$ " + String.format("%.2f", saldoInicial));
            lblSaldoAtual.setText("R$ " + String.format("%.2f", saldoAtual));
            
            // System.out.logFinancialTransaction("ABERTURA_CAIXA", saldoInicial, "Abertura do caixa"); // Método não existe
            System.out.println("Caixa aberto com sucesso - Saldo inicial: R$ " + saldoInicial);
            
            JOptionPane.showMessageDialog(frame, 
                "Caixa aberto com sucesso!\n\nSaldo inicial: R$ 1.000,00",
                "Caixa Aberto", JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception ex) {
            System.err.println("Erro ao abrir caixa: " + ex.getMessage());
            JOptionPane.showMessageDialog(frame, 
                "Erro ao abrir caixa: " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void adicionarFundos(ActionEvent e) {
        if (!caixaAberto) {
            JOptionPane.showMessageDialog(frame, "O caixa precisa estar aberto!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String valorStr = JOptionPane.showInputDialog(frame, "Digite o valor a adicionar:", "Adicionar Fundos", JOptionPane.QUESTION_MESSAGE);
        
        if (valorStr != null && !valorStr.trim().isEmpty()) {
            try {
                BigDecimal valor = new BigDecimal(valorStr.replace(",", "."));
                
                if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(frame, "Valor deve ser positivo!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                saldoAtual = saldoAtual.add(valor);
                
                // Adicionar movimentação
                MovimentacaoCaixa mov = new MovimentacaoCaixa(
                    LocalDateTime.now(),
                    "ENTRADA",
                    "Adição de fundos",
                    valor,
                    saldoAtual
                );
                movimentacoes.add(mov);
                atualizarTabela();
                
                lblSaldoAtual.setText("R$ " + String.format("%.2f", saldoAtual));
                
                // System.out.logFinancialTransaction("ADICAO_FUNDOS", valor, "Adição de fundos ao caixa"); // Método não existe
                System.out.println("Fundos adicionados com sucesso - Valor: R$ " + valor);
                
                JOptionPane.showMessageDialog(frame, "Fundos adicionados com sucesso!\nValor: R$ " + String.format("%.2f", valor), 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void retirarFundos(ActionEvent e) {
        if (!caixaAberto) {
            JOptionPane.showMessageDialog(frame, "O caixa precisa estar aberto!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String valorStr = JOptionPane.showInputDialog(frame, "Digite o valor a retirar:", "Retirar Fundos", JOptionPane.QUESTION_MESSAGE);
        
        if (valorStr != null && !valorStr.trim().isEmpty()) {
            try {
                BigDecimal valor = new BigDecimal(valorStr.replace(",", "."));
                
                if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(frame, "Valor deve ser positivo!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (valor.compareTo(saldoAtual) > 0) {
                    JOptionPane.showMessageDialog(frame, "Saldo insuficiente!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                saldoAtual = saldoAtual.subtract(valor);
                
                // Adicionar movimentação
                MovimentacaoCaixa mov = new MovimentacaoCaixa(
                    LocalDateTime.now(),
                    "SAÍDA",
                    "Retirada de fundos",
                    valor.negate(),
                    saldoAtual
                );
                movimentacoes.add(mov);
                atualizarTabela();
                
                lblSaldoAtual.setText("R$ " + String.format("%.2f", saldoAtual));
                
                // System.out.logFinancialTransaction("RETIRADA_FUNDOS", valor, "Retirada de fundos do caixa"); // Método não existe
                System.out.println("Fundos retirados com sucesso - Valor: R$ " + valor);
                
                JOptionPane.showMessageDialog(frame, "Fundos retirados com sucesso!\nValor: R$ " + String.format("%.2f", valor), 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void fecharCaixa(ActionEvent e) {
        try {
            if (!caixaAberto) {
                System.out.println("Tentativa de fechar caixa já fechado");
                JOptionPane.showMessageDialog(frame, "O caixa já está fechado!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(frame, 
                "Deseja fechar o caixa?\n\nSaldo atual: R$ " + String.format("%.2f", saldoAtual), 
                "Fechar Caixa", JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION) {
                caixaAberto = false;
                
                // Adicionar movimentação de fechamento
                MovimentacaoCaixa mov = new MovimentacaoCaixa(
                    LocalDateTime.now(),
                    "FECHAMENTO",
                    "Fechamento do caixa",
                    BigDecimal.ZERO,
                    saldoAtual
                );
                movimentacoes.add(mov);
                atualizarTabela();
                
                // Atualizar status
                lblStatusCaixa.setText("Fechado");
                lblStatusCaixa.setForeground(Color.RED);
                
                // System.out.logFinancialTransaction("FECHAMENTO_CAIXA", BigDecimal.ZERO, "Fechamento do caixa"); // Método não existe
                System.out.println("Caixa fechado com sucesso - Saldo final: R$ " + saldoAtual);
                
                JOptionPane.showMessageDialog(frame, 
                    "Caixa fechado com sucesso!\n\n" +
                    "Saldo final: R$ " + String.format("%.2f", saldoAtual) + "\n" +
                    "Total de movimentações: " + movimentacoes.size(),
                    "Fechamento Concluído", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            System.err.println("Erro ao fechar caixa: " + ex.getMessage());
            JOptionPane.showMessageDialog(frame, 
                "Erro ao fechar caixa: " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void gerarRelatorio(ActionEvent e) {
        try {
            System.out.println("Gerando relatório de caixa");
            
            StringBuilder relatorio = new StringBuilder();
            relatorio.append("=== RELATÓRIO DE CAIXA ===\n\n");
            relatorio.append("Status: ").append(caixaAberto ? "Aberto" : "Fechado").append("\n");
            relatorio.append("Saldo Inicial: R$ ").append(String.format("%.2f", 
                movimentacoes.isEmpty() ? BigDecimal.ZERO : movimentacoes.get(0).getValor())).append("\n");
            relatorio.append("Saldo Atual: R$ ").append(String.format("%.2f", saldoAtual)).append("\n");
            relatorio.append("Total Movimentações: ").append(movimentacoes.size()).append("\n\n");
            
            relatorio.append("=== MOVIMENTAÇÕES ===\n");
            for (MovimentacaoCaixa mov : movimentacoes) {
                relatorio.append(mov.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
                       .append(" - ").append(mov.getTipo())
                       .append(" - ").append(mov.getDescricao())
                       .append(" - R$ ").append(String.format("%.2f", mov.getValor()))
                       .append("\n");
            }
            
            System.out.println("Relatório de caixa gerado com sucesso");
            JOptionPane.showMessageDialog(frame, relatorio.toString(), "Relatório de Caixa", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception ex) {
            System.err.println("Erro ao gerar relatório de caixa: " + ex.getMessage());
            JOptionPane.showMessageDialog(frame, 
                "Erro ao gerar relatório: " + ex.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void atualizarTabela() {
        tableModel.setRowCount(0);
        for (MovimentacaoCaixa mov : movimentacoes) {
            Object[] row = {
                mov.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                mov.getTipo(),
                mov.getDescricao(),
                "R$ " + String.format("%.2f", mov.getValor()),
                "R$ " + String.format("%.2f", mov.getSaldo())
            };
            tableModel.addRow(row);
        }
    }
    
    public void show() {
        frame.setVisible(true);
    }
    
    // Classe de apoio
    private static class MovimentacaoCaixa {
        private LocalDateTime dataHora;
        private String tipo;
        private String descricao;
        private BigDecimal valor;
        private BigDecimal saldo;
        
        public MovimentacaoCaixa(LocalDateTime dataHora, String tipo, String descricao, BigDecimal valor, BigDecimal saldo) {
            this.dataHora = dataHora;
            this.tipo = tipo;
            this.descricao = descricao;
            this.valor = valor;
            this.saldo = saldo;
        }
        
        public LocalDateTime getDataHora() { return dataHora; }
        public String getTipo() { return tipo; }
        public String getDescricao() { return descricao; }
        public BigDecimal getValor() { return valor; }
        public BigDecimal getSaldo() { return saldo; }
    }
}
