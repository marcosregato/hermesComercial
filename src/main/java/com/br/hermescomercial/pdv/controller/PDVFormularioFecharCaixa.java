package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.ui.layout.LayoutPadrao;
import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe especializada para formulário de fechamento de caixa
 * Segue o padrão Header → Informações → Tabela → Ações
 * Versão 1.0.0 - Adaptada para PDVMenuLateralElegante
 */
public class PDVFormularioFecharCaixa {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes do formulário
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
    
    // Cores
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private static final Color DANGER_COLOR = new Color(244, 67, 54);
    private static final Color WARNING_COLOR = new Color(255, 193, 7);
    private static final Color PRIMARY_COLOR = new Color(33, 150, 243);
    
    public PDVFormularioFecharCaixa(JPanel workArea, String usuario, String nome) {
        this.workArea = workArea;
        this.usuarioAtual = usuario;
        this.nomeUsuario = nome;
        this.movimentacoes = new ArrayList<>(50);
        this.saldoInicial = new BigDecimal("1000.00");
        this.saldoAtual = saldoInicial;
    }
    
    public JPanel criarFormularioFecharCaixa() {
        SystemLogger.ui("=== CRIANDO FORMULÁRIO FECHAR CAIXA ===");
        SystemLogger.ui("Usuário: " + usuarioAtual);
        
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(Color.WHITE);
        
        // Header
        painelPrincipal.add(criarHeader(), BorderLayout.NORTH);
        
        // Conteúdo principal
        JPanel conteudo = new JPanel(new BorderLayout());
        conteudo.setBackground(Color.WHITE);
        
        // Painel de informações
        conteudo.add(criarPainelInformacoes(), BorderLayout.NORTH);
        
        // Tabela de resumo
        conteudo.add(criarTabelaResumo(), BorderLayout.CENTER);
        
        // Painel de ações
        conteudo.add(criarPainelAcoes(), BorderLayout.SOUTH);
        
        painelPrincipal.add(conteudo, BorderLayout.CENTER);
        
        // Carregar dados iniciais
        carregarDadosExemplo();
        
        // Iniciar timer para atualizar data/hora
        iniciarTimer();
        
        SystemLogger.ui("Formulário Fechar Caixa criado com sucesso");
        return painelPrincipal;
    }
    
    private JPanel criarHeader() {
        JPanel header = LayoutPadrao.criarHeaderPanel("💰 Fechamento de Caixa");
        
        // Informações do usuário
        JPanel userInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userInfo.setBackground(new Color(250, 250, 250));
        userInfo.add(new JLabel("👤 " + nomeUsuario));
        userInfo.add(new JLabel(" | "));
        userInfo.add(new JLabel("📅 " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        
        header.add(userInfo, BorderLayout.EAST);
        return header;
    }
    
    private JPanel criarPainelInformacoes() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createTitledBorder("📊 Informações do Fechamento"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Data e Hora
        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(new JLabel("Data/Hora:"), gbc);
        gbc.gridx = 1;
        lblDataHora = new JLabel(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        lblDataHora.setFont(new Font("Arial", Font.BOLD, 12));
        painel.add(lblDataHora, gbc);
        
        // Operador
        gbc.gridx = 2; gbc.gridy = 0;
        painel.add(new JLabel("Operador:"), gbc);
        gbc.gridx = 3;
        lblOperador = new JLabel(nomeUsuario);
        lblOperador.setFont(new Font("Arial", Font.BOLD, 12));
        painel.add(lblOperador, gbc);
        
        // Saldo Inicial
        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(new JLabel("Saldo Inicial:"), gbc);
        gbc.gridx = 1;
        lblSaldoInicial = new JLabel("R$ 0,00");
        lblSaldoInicial.setFont(new Font("Arial", Font.BOLD, 14));
        lblSaldoInicial.setForeground(PRIMARY_COLOR);
        painel.add(lblSaldoInicial, gbc);
        
        // Saldo Atual
        gbc.gridx = 2; gbc.gridy = 1;
        painel.add(new JLabel("Saldo Atual:"), gbc);
        gbc.gridx = 3;
        lblSaldoAtual = new JLabel("R$ 0,00");
        lblSaldoAtual.setFont(new Font("Arial", Font.BOLD, 14));
        lblSaldoAtual.setForeground(SUCCESS_COLOR);
        painel.add(lblSaldoAtual, gbc);
        
        // Totais
        gbc.gridx = 0; gbc.gridy = 2;
        painel.add(new JLabel("Total Vendas:"), gbc);
        gbc.gridx = 1;
        lblTotalVendas = new JLabel("R$ 0,00");
        lblTotalVendas.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalVendas.setForeground(SUCCESS_COLOR);
        painel.add(lblTotalVendas, gbc);
        
        gbc.gridx = 2; gbc.gridy = 2;
        painel.add(new JLabel("Total Sangrias:"), gbc);
        gbc.gridx = 3;
        lblTotalSangrias = new JLabel("R$ 0,00");
        lblTotalSangrias.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalSangrias.setForeground(DANGER_COLOR);
        painel.add(lblTotalSangrias, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        painel.add(new JLabel("Total Suprimentos:"), gbc);
        gbc.gridx = 1;
        lblTotalSuprimentos = new JLabel("R$ 0,00");
        lblTotalSuprimentos.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalSuprimentos.setForeground(WARNING_COLOR);
        painel.add(lblTotalSuprimentos, gbc);
        
        gbc.gridx = 2; gbc.gridy = 3;
        painel.add(new JLabel("Saldo Final:"), gbc);
        gbc.gridx = 3;
        lblSaldoFinal = new JLabel("R$ 0,00");
        lblSaldoFinal.setFont(new Font("Arial", Font.BOLD, 16));
        lblSaldoFinal.setForeground(SUCCESS_COLOR);
        painel.add(lblSaldoFinal, gbc);
        
        return painel;
    }
    
    private JPanel criarTabelaResumo() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createTitledBorder("📋 Resumo de Movimentações"));
        
        // Tabela de resumo
        String[] colunas = {"Tipo", "Descrição", "Quantidade", "Valor Total", "Data/Hora"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) return Integer.class;
                return String.class;
            }
        };
        
        resumoTable = new JTable(tableModel);
        resumoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resumoTable.getTableHeader().setReorderingAllowed(false);
        resumoTable.setRowHeight(25);
        
        // Configurar larguras das colunas
        resumoTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        resumoTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        resumoTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        resumoTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        resumoTable.getColumnModel().getColumn(4).setPreferredWidth(150);
        
        JScrollPane scrollPane = new JScrollPane(resumoTable);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        
        painel.add(scrollPane, BorderLayout.CENTER);
        
        return painel;
    }
    
    private JPanel criarPainelAcoes() {
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelAcoes.setBackground(Color.WHITE);
        painelAcoes.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton btnRecalcular = LayoutPadrao.criarBotaoPrimario("🔄 Recalcular");
        btnRecalcular.addActionListener(e -> recalcular());
        
        JButton btnImprimir = LayoutPadrao.criarBotaoSecundario("🖨️ Imprimir Relatório");
        btnImprimir.addActionListener(e -> imprimirRelatorio());
        
        JButton btnConfirmar = LayoutPadrao.criarBotaoSucesso("✅ Confirmar Fechamento");
        btnConfirmar.addActionListener(e -> confirmarFechamento());
        
        JButton btnCancelar = LayoutPadrao.criarBotaoPerigo("❌ Cancelar");
        btnCancelar.addActionListener(e -> cancelarFechamento());
        
        painelAcoes.add(btnRecalcular);
        painelAcoes.add(btnImprimir);
        painelAcoes.add(btnConfirmar);
        painelAcoes.add(btnCancelar);
        
        return painelAcoes;
    }
    
    // Métodos de ação
    private void recalcular() {
        calcularTotais();
        atualizarTabela();
        
        JOptionPane.showMessageDialog(workArea, 
            "Valores recalculados com sucesso!", 
            "Recálculo", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Valores do fechamento de caixa recalculados por " + usuarioAtual);
    }
    
    private void imprimirRelatorio() {
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
        
        relatorio.append("\n=== VERSÃO PDVMENULATERALELEGANTE ===\n");
        relatorio.append("Hermes Comercial PDV\n");
        relatorio.append("Sistema Integrado de Gestão\n");
        
        // Exibir relatório em uma janela dedicada
        JFrame relatorioFrame = new JFrame("Relatório de Fechamento de Caixa");
        relatorioFrame.setSize(600, 500);
        relatorioFrame.setLocationRelativeTo(workArea.getTopLevelAncestor());
        
        JTextArea relatorioArea = new JTextArea(relatorio.toString());
        relatorioArea.setEditable(false);
        relatorioArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        relatorioArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(relatorioArea);
        relatorioFrame.add(scrollPane, BorderLayout.CENTER);
        
        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        
        JButton btnCopiar = LayoutPadrao.criarBotaoPrimario("📋 Copiar");
        btnCopiar.addActionListener(ev -> {
            String relatorioText = relatorio.toString();
            java.awt.datatransfer.StringSelection selection = new java.awt.datatransfer.StringSelection(relatorioText);
            java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
            JOptionPane.showMessageDialog(relatorioFrame, 
                "Relatório copiado para área de transferência!", 
                "Copiado", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JButton btnFechar = LayoutPadrao.criarBotaoPerigo("❌ Fechar");
        btnFechar.addActionListener(ev -> relatorioFrame.dispose());
        
        buttonPanel.add(btnCopiar);
        buttonPanel.add(btnFechar);
        relatorioFrame.add(buttonPanel, BorderLayout.SOUTH);
        
        relatorioFrame.setVisible(true);
        
        SystemLogger.ui("Relatório de fechamento de caixa gerado por " + usuarioAtual);
    }
    
    private void confirmarFechamento() {
        int confirm = JOptionPane.showConfirmDialog(workArea, 
            "Deseja confirmar o fechamento do caixa?\n\n" +
            "Saldo Final: " + lblSaldoFinal.getText() + "\n" +
            "Total de Movimentações: " + movimentacoes.size() +
            "\n\nConfirmar Fechamento", 
            "Confirmar Fechamento",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(workArea, 
                "Fechamento confirmado com sucesso!\n\n" +
                "Relatório gerado e caixa encerrado.\n" +
                "Versão PDVMenuLateralElegante",
                "Fechamento Concluído", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Fechamento de caixa confirmado por " + usuarioAtual);
            
            // Limpar formulário após fechamento
            limparFormulario();
        }
    }
    
    private void cancelarFechamento() {
        int confirm = JOptionPane.showConfirmDialog(workArea, 
            "Deseja cancelar o fechamento do caixa?\n\n" +
            "Todos os dados não salvos serão perdidos.",
            "Cancelar Fechamento",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            SystemLogger.ui("Fechamento de caixa cancelado por " + usuarioAtual);
            
            // Limpar formulário
            limparFormulario();
        }
    }
    
    private void limparFormulario() {
        // Limpar tabela
        tableModel.setRowCount(0);
        
        // Limpar labels
        lblTotalVendas.setText("R$ 0,00");
        lblTotalSangrias.setText("R$ 0,00");
        lblTotalSuprimentos.setText("R$ 0,00");
        lblSaldoFinal.setText("R$ 0,00");
        lblSaldoAtual.setText("R$ 0,00");
        
        // Limpar movimentações
        movimentacoes.clear();
        
        JOptionPane.showMessageDialog(workArea, 
            "Formulário limpo com sucesso!", 
            "Limpar", JOptionPane.INFORMATION_MESSAGE);
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
        
        SystemLogger.ui("Dados de exemplo carregados para fechamento de caixa");
    }
    
    private void iniciarTimer() {
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
