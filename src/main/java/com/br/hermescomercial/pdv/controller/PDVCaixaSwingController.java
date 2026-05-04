package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.ui.layout.LayoutPadrao;
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
        frame = new JFrame("PDV - Operações de Caixa v2.8.3 - LayoutPadrao");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        
        // Configurar fundo com LayoutPadrao
        frame.getContentPane().setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        frame.add(createMainPanel());
    }
    
    private JPanel createMainPanel() {
        JPanel mainPanel = LayoutPadrao.criarPainelComMargem(15);
        mainPanel.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        // Header com LayoutPadrao
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
        panel.setBackground(LayoutPadrao.COR_PRIMARIA);
        panel.setPreferredSize(new Dimension(0, 80));
        
        // Título central com LayoutPadrao
        JLabel titleLabel = LayoutPadrao.criarRotuloTitulo("💰 Operações de Caixa v2.8.3 - LayoutPadrao");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Botão voltar com LayoutPadrao
        JButton btnVoltar = LayoutPadrao.criarBotaoSecundario("← Voltar");
        btnVoltar.addActionListener(e -> frame.dispose());
        
        // Data e hora atual com LayoutPadrao
        JLabel lblDataHora = LayoutPadrao.criarRotuloTexto(
            java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        lblDataHora.setForeground(Color.WHITE);
        lblDataHora.setHorizontalAlignment(SwingConstants.RIGHT);
        
        panel.add(btnVoltar, BorderLayout.WEST);
        panel.add(titleLabel, BorderLayout.CENTER);
        panel.add(lblDataHora, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createStatusPanel() {
        JPanel panel = LayoutPadrao.criarPainelBranco();
        panel.setBorder(BorderFactory.createCompoundBorder(
            LayoutPadrao.BORDA_PADRAO,
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setLayout(new GridLayout(2, 2, 10, 10));
        
        lblStatusCaixa = LayoutPadrao.criarRotuloTexto("Fechado");
        lblStatusCaixa.setFont(LayoutPadrao.FONTE_SUBTITULO);
        lblStatusCaixa.setForeground(LayoutPadrao.COR_PERIGO);
        lblStatusCaixa.setHorizontalAlignment(SwingConstants.CENTER);
        lblStatusCaixa.setOpaque(true);
        lblStatusCaixa.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        lblSaldoInicial = LayoutPadrao.criarRotuloTexto("R$ 0,00");
        lblSaldoInicial.setFont(LayoutPadrao.FONTE_ROTULO);
        lblSaldoInicial.setHorizontalAlignment(SwingConstants.CENTER);
        
        lblSaldoAtual = LayoutPadrao.criarRotuloTexto("R$ 0,00");
        lblSaldoAtual.setFont(LayoutPadrao.FONTE_ROTULO);
        lblSaldoAtual.setHorizontalAlignment(SwingConstants.CENTER);
        
        lblDataHora = LayoutPadrao.criarRotuloTexto(
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        lblDataHora.setHorizontalAlignment(SwingConstants.CENTER);
        
        panel.add(LayoutPadrao.criarRotuloCampo("Status:"));
        panel.add(LayoutPadrao.criarRotuloCampo("Saldo Inicial:"));
        panel.add(lblStatusCaixa);
        panel.add(lblSaldoInicial);
        
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        bottomPanel.setBackground(LayoutPadrao.COR_FUNDO);
        bottomPanel.add(LayoutPadrao.criarRotuloCampo("Saldo Atual:"));
        bottomPanel.add(LayoutPadrao.criarRotuloCampo("Data/Hora:"));
        bottomPanel.add(lblSaldoAtual);
        bottomPanel.add(lblDataHora);
        
        JPanel statusContainer = new JPanel(new BorderLayout());
        statusContainer.setBackground(LayoutPadrao.COR_FUNDO);
        statusContainer.add(panel, BorderLayout.NORTH);
        statusContainer.add(bottomPanel, BorderLayout.SOUTH);
        
        return statusContainer;
    }
    
    private JPanel createMovimentacoesPanel() {
        JPanel panel = LayoutPadrao.criarPainelBranco();
        panel.setBorder(BorderFactory.createCompoundBorder(
            LayoutPadrao.BORDA_PADRAO,
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setLayout(new BorderLayout());
        
        String[] columns = {"Data/Hora", "Tipo", "Descrição", "Valor", "Saldo"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Bloquear edição de todas as células
            }
        };
        movimentacoesTable = LayoutPadrao.criarTabela();
        
        JScrollPane scrollPane = LayoutPadrao.criarBarraRolagem(movimentacoesTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        JButton btnAbrirCaixa = LayoutPadrao.criarBotaoSucesso("🔓 Abrir Caixa");
        btnAbrirCaixa.addActionListener(this::abrirCaixa);
        
        JButton btnAdicionarFundos = LayoutPadrao.criarBotaoPrimario("💰 Adicionar Fundos");
        btnAdicionarFundos.addActionListener(this::adicionarFundos);
        
        JButton btnRetirarFundos = LayoutPadrao.criarBotaoAlerta("💸 Retirar Fundos");
        btnRetirarFundos.addActionListener(this::retirarFundos);
        
        JButton btnFecharCaixa = LayoutPadrao.criarBotaoPerigo("🔒 Fechar Caixa");
        btnFecharCaixa.addActionListener(this::fecharCaixa);
        
        JButton btnRelatorio = LayoutPadrao.criarBotaoSecundario("📊 Relatório");
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
            
            int confirm = com.br.hermescomercial.theme.ModernTheme.showCustomConfirmDialog(frame, 
                "Deseja fechar o caixa?\n\nSaldo atual: R$ " + String.format("%.2f", saldoAtual), 
                "Fechar Caixa", 
                new String[]{"Sim", "Não"}, 0);
                
            if (confirm == 0) {
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
