package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.ui.layout.LayoutPadrao;
import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe especializada para formulário de gestão de caixa
 * Segue o padrão Header → Busca → Formulário → Tabela
 * Versão 1.0.0 - Adaptada para PDVMenuLateralElegante
 */
public class PDVFormularioCaixa {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes da interface
    private JLabel lblStatusCaixa;
    private JLabel lblSaldoAtual;
    private JLabel lblDataAbertura;
    private JLabel lblHoraAbertura;
    private JTextArea txtObservacoes;
    private JTable tblMovimentacoes;
    private DefaultTableModel tableModel;
    
    // Dados do caixa
    private boolean caixaAberto = false;
    private BigDecimal saldoAtual = BigDecimal.ZERO;
    private LocalDateTime dataAbertura;
    private List<MovimentacaoCaixa> movimentacoes;
    
    // Cores
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private static final Color DANGER_COLOR = new Color(244, 67, 54);
    private static final Color WARNING_COLOR = new Color(255, 193, 7);
    private static final Color PRIMARY_COLOR = new Color(33, 150, 243);
    
    public PDVFormularioCaixa(JPanel workArea, String usuario, String nome) {
        this.workArea = workArea;
        this.usuarioAtual = usuario;
        this.nomeUsuario = nome;
        this.movimentacoes = new ArrayList<>();
    }
    
    public JPanel criarFormularioCaixa() {
        SystemLogger.ui("=== CRIANDO FORMULÁRIO CAIXA ===");
        SystemLogger.ui("Usuário: " + usuarioAtual);
        
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(Color.WHITE);
        
        // Header
        painelPrincipal.add(criarHeader(), BorderLayout.NORTH);
        
        // Painel central com busca, formulário e tabela
        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.setBackground(Color.WHITE);
        
        // Painel de busca
        JPanel buscaPanel = criarPainelBusca();
        painelCentral.add(buscaPanel, BorderLayout.NORTH);
        
        // Painel do formulário
        JPanel formularioPanel = criarPainelFormulario();
        painelCentral.add(formularioPanel, BorderLayout.CENTER);
        
        // Tabela de movimentações
        JPanel tabelaPanel = criarPainelTabela();
        painelCentral.add(tabelaPanel, BorderLayout.SOUTH);
        
        painelPrincipal.add(painelCentral, BorderLayout.CENTER);
        
        SystemLogger.ui("Formulário Caixa criado com sucesso");
        return painelPrincipal;
    }
    
    private JPanel criarHeader() {
        JPanel header = LayoutPadrao.criarHeaderPanel("💰 Gestão de Caixa");
        
        // Informações do usuário
        JPanel userInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userInfo.setBackground(new Color(250, 250, 250));
        userInfo.add(new JLabel("👤 " + nomeUsuario));
        userInfo.add(new JLabel(" | "));
        userInfo.add(new JLabel("📅 " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        
        header.add(userInfo, BorderLayout.EAST);
        return header;
    }
    
    private JPanel criarPainelBusca() {
        JPanel painelBusca = new JPanel(new BorderLayout());
        painelBusca.setBackground(Color.WHITE);
        painelBusca.setBorder(BorderFactory.createTitledBorder("🔍 Busca de Movimentações"));
        
        // Campos de busca
        JPanel camposBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        camposBusca.setBackground(Color.WHITE);
        
        camposBusca.add(new JLabel("Data Inicial:"));
        JTextField txtDataInicial = new JTextField(10);
        camposBusca.add(txtDataInicial);
        
        camposBusca.add(new JLabel("Data Final:"));
        JTextField txtDataFinal = new JTextField(10);
        camposBusca.add(txtDataFinal);
        
        camposBusca.add(new JLabel("Tipo:"));
        JComboBox<String> cbTipo = new JComboBox<>(new String[]{
            "Todos", "Entrada", "Saída", "Sangria", "Suprimento"
        });
        camposBusca.add(cbTipo);
        
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBackground(PRIMARY_COLOR);
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.addActionListener(e -> buscarMovimentacoes(txtDataInicial.getText(), txtDataFinal.getText(), (String) cbTipo.getSelectedItem()));
        camposBusca.add(btnBuscar);
        
        JButton btnLimpar = new JButton("🗑️ Limpar");
        btnLimpar.addActionListener(e -> {
            txtDataInicial.setText("");
            txtDataFinal.setText("");
            cbTipo.setSelectedIndex(0);
        });
        camposBusca.add(btnLimpar);
        
        painelBusca.add(camposBusca, BorderLayout.CENTER);
        
        return painelBusca;
    }
    
    private JPanel criarPainelFormulario() {
        JPanel painelFormulario = new JPanel(new BorderLayout());
        painelFormulario.setBackground(Color.WHITE);
        painelFormulario.setBorder(BorderFactory.createTitledBorder("📝 Informações do Caixa"));
        
        // Manter o painel de informações como formulário principal
        painelFormulario.add(criarPainelInformacoes(), BorderLayout.CENTER);
        
        return painelFormulario;
    }
    
    private JPanel criarPainelInformacoes() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createTitledBorder("Informações do Caixa"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Status do caixa
        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        lblStatusCaixa = new JLabel("🔴 Fechado");
        lblStatusCaixa.setFont(new Font("Arial", Font.BOLD, 14));
        lblStatusCaixa.setForeground(DANGER_COLOR);
        painel.add(lblStatusCaixa, gbc);
        
        // Saldo atual
        gbc.gridx = 0; gbc.gridy = 1;
        painel.add(new JLabel("Saldo Atual:"), gbc);
        gbc.gridx = 1;
        lblSaldoAtual = new JLabel("R$ 0,00");
        lblSaldoAtual.setFont(new Font("Arial", Font.BOLD, 16));
        lblSaldoAtual.setForeground(PRIMARY_COLOR);
        painel.add(lblSaldoAtual, gbc);
        
        // Data de abertura
        gbc.gridx = 2; gbc.gridy = 0;
        painel.add(new JLabel("Data Abertura:"), gbc);
        gbc.gridx = 3;
        lblDataAbertura = new JLabel("-");
        painel.add(lblDataAbertura, gbc);
        
        // Hora de abertura
        gbc.gridx = 2; gbc.gridy = 1;
        painel.add(new JLabel("Hora Abertura:"), gbc);
        gbc.gridx = 3;
        lblHoraAbertura = new JLabel("-");
        painel.add(lblHoraAbertura, gbc);
        
        // Observações
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4; gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 0.1;
        painel.add(new JLabel("Observações:"), gbc);
        gbc.gridy = 3; gbc.weighty = 0.2;
        JScrollPane scrollPane = new JScrollPane(txtObservacoes = new JTextArea(3, 50));
        txtObservacoes.setLineWrap(true);
        txtObservacoes.setWrapStyleWord(true);
        painel.add(scrollPane, gbc);
        
        return painel;
    }
    
    private JPanel criarPainelTabela() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createTitledBorder("Movimentações do Caixa"));
        
        // Configurar tabela
        String[] colunas = {"Data/Hora", "Tipo", "Descrição", "Valor", "Saldo", "Operador"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblMovimentacoes = new JTable(tableModel);
        tblMovimentacoes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblMovimentacoes.getTableHeader().setReorderingAllowed(false);
        
        // Configurar largura das colunas
        tblMovimentacoes.getColumnModel().getColumn(0).setPreferredWidth(150);
        tblMovimentacoes.getColumnModel().getColumn(1).setPreferredWidth(80);
        tblMovimentacoes.getColumnModel().getColumn(2).setPreferredWidth(200);
        tblMovimentacoes.getColumnModel().getColumn(3).setPreferredWidth(100);
        tblMovimentacoes.getColumnModel().getColumn(4).setPreferredWidth(100);
        tblMovimentacoes.getColumnModel().getColumn(5).setPreferredWidth(120);
        
        // Centralizar colunas numéricas
        tblMovimentacoes.getColumnModel().getColumn(3).setCellRenderer(new CenteredRenderer());
        tblMovimentacoes.getColumnModel().getColumn(4).setCellRenderer(new CenteredRenderer());
        
        JScrollPane scrollPane = new JScrollPane(tblMovimentacoes);
        painel.add(scrollPane, BorderLayout.CENTER);
        
        return painel;
    }
    
    private JPanel criarPainelAcoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton btnAbrirCaixa = LayoutPadrao.criarBotaoSucesso("💰 Abrir Caixa");
        btnAbrirCaixa.addActionListener(e -> abrirCaixa());
        
        JButton btnFecharCaixa = LayoutPadrao.criarBotaoAlerta("🔒 Fechar Caixa");
        btnFecharCaixa.addActionListener(e -> fecharCaixa());
        btnFecharCaixa.setEnabled(false);
        
        JButton btnSuprir = LayoutPadrao.criarBotaoPrimario("💵 Suprir");
        btnSuprir.addActionListener(e -> suprirCaixa());
        btnSuprir.setEnabled(false);
        
        JButton btnSangria = LayoutPadrao.criarBotaoPerigo("💸 Sangria");
        btnSangria.addActionListener(e -> sangriaCaixa());
        btnSangria.setEnabled(false);
        
        JButton btnRelatorio = LayoutPadrao.criarBotaoSecundario("📄 Relatório");
        btnRelatorio.addActionListener(e -> gerarRelatorio());
        
        painel.add(btnAbrirCaixa);
        painel.add(btnFecharCaixa);
        painel.add(Box.createHorizontalStrut(10));
        painel.add(btnSuprir);
        painel.add(btnSangria);
        painel.add(Box.createHorizontalStrut(10));
        painel.add(btnRelatorio);
        
        return painel;
    }
    
    private void abrirCaixa() {
        if (caixaAberto) {
            JOptionPane.showMessageDialog(workArea, "O caixa já está aberto!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Dialog para informar valor inicial
        String valorStr = JOptionPane.showInputDialog(workArea, "Informe o valor inicial do caixa:", "Abrir Caixa", JOptionPane.QUESTION_MESSAGE);
        
        if (valorStr != null && !valorStr.trim().isEmpty()) {
            try {
                BigDecimal valorInicial = new BigDecimal(valorStr.replace(",", "."));
                
                caixaAberto = true;
                saldoAtual = valorInicial;
                dataAbertura = LocalDateTime.now();
                
                // Atualizar interface
                atualizarStatusCaixa();
                
                // Adicionar movimentação de abertura
                adicionarMovimentacao("ABERTURA", "Abertura do caixa", valorInicial, usuarioAtual);
                
                // Habilitar/desabilitar botões
                habilitarBotoesCaixaAberto();
                
                JOptionPane.showMessageDialog(workArea, "Caixa aberto com sucesso!\nValor inicial: R$ " + valorInicial, 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
                SystemLogger.ui("Caixa aberto por " + usuarioAtual + " com valor inicial R$ " + valorInicial);
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(workArea, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void fecharCaixa() {
        if (!caixaAberto) {
            JOptionPane.showMessageDialog(workArea, "O caixa já está fechado!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacao = JOptionPane.showConfirmDialog(workArea, 
            "Deseja fechar o caixa?\nSaldo atual: R$ " + saldoAtual, 
            "Fechar Caixa", JOptionPane.YES_NO_OPTION);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            caixaAberto = false;
            
            // Adicionar movimentação de fechamento
            adicionarMovimentacao("FECHAMENTO", "Fechamento do caixa", BigDecimal.ZERO, usuarioAtual);
            
            // Atualizar interface
            atualizarStatusCaixa();
            
            // Habilitar/desabilitar botões
            habilitarBotoesCaixaFechado();
            
            JOptionPane.showMessageDialog(workArea, "Caixa fechado com sucesso!\nSaldo final: R$ " + saldoAtual, 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Caixa fechado por " + usuarioAtual + " com saldo final R$ " + saldoAtual);
        }
    }
    
    private void suprirCaixa() {
        if (!caixaAberto) {
            JOptionPane.showMessageDialog(workArea, "Abra o caixa primeiro!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String valorStr = JOptionPane.showInputDialog(workArea, "Informe o valor para suprir:", "Suprir Caixa", JOptionPane.QUESTION_MESSAGE);
        
        if (valorStr != null && !valorStr.trim().isEmpty()) {
            try {
                BigDecimal valor = new BigDecimal(valorStr.replace(",", "."));
                saldoAtual = saldoAtual.add(valor);
                
                adicionarMovimentacao("SUPRIMENTO", "Suprimento de caixa", valor, usuarioAtual);
                atualizarStatusCaixa();
                
                JOptionPane.showMessageDialog(workArea, "Suprimento de R$ " + valor + " realizado com sucesso!", 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(workArea, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void sangriaCaixa() {
        if (!caixaAberto) {
            JOptionPane.showMessageDialog(workArea, "Abra o caixa primeiro!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String valorStr = JOptionPane.showInputDialog(workArea, "Informe o valor para sangria:", "Sangria", JOptionPane.QUESTION_MESSAGE);
        
        if (valorStr != null && !valorStr.trim().isEmpty()) {
            try {
                BigDecimal valor = new BigDecimal(valorStr.replace(",", "."));
                
                if (valor.compareTo(saldoAtual) > 0) {
                    JOptionPane.showMessageDialog(workArea, "Valor maior que o saldo disponível!", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                saldoAtual = saldoAtual.subtract(valor);
                
                adicionarMovimentacao("SANGRIA", "Sangria de caixa", valor.negate(), usuarioAtual);
                atualizarStatusCaixa();
                
                JOptionPane.showMessageDialog(workArea, "Sangria de R$ " + valor + " realizada com sucesso!", 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(workArea, "Valor inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void gerarRelatorio() {
        JOptionPane.showMessageDialog(workArea, "Relatório do caixa gerado com sucesso!\nTotal de movimentações: " + movimentacoes.size(), 
            "Relatório", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Relatório do caixa gerado por " + usuarioAtual);
    }
    
    private void atualizarStatusCaixa() {
        if (caixaAberto) {
            lblStatusCaixa.setText("🟢 Aberto");
            lblStatusCaixa.setForeground(SUCCESS_COLOR);
            lblDataAbertura.setText(dataAbertura.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            lblHoraAbertura.setText(dataAbertura.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        } else {
            lblStatusCaixa.setText("🔴 Fechado");
            lblStatusCaixa.setForeground(DANGER_COLOR);
            lblDataAbertura.setText("-");
            lblHoraAbertura.setText("-");
        }
        
        lblSaldoAtual.setText("R$ " + String.format("%,.2f", saldoAtual));
    }
    
    private void habilitarBotoesCaixaAberto() {
        // Habilitar/desabilitar botões conforme o estado do caixa
        Component[] componentes = workArea.getParent().getComponents();
        for (Component comp : componentes) {
            if (comp instanceof JPanel) {
                encontrarEBotar((JPanel) comp, "Abrir Caixa", false);
                encontrarEBotar((JPanel) comp, "Fechar Caixa", true);
                encontrarEBotar((JPanel) comp, "Suprir", true);
                encontrarEBotar((JPanel) comp, "Sangria", true);
            }
        }
    }
    
    private void habilitarBotoesCaixaFechado() {
        Component[] componentes = workArea.getParent().getComponents();
        for (Component comp : componentes) {
            if (comp instanceof JPanel) {
                encontrarEBotar((JPanel) comp, "Abrir Caixa", true);
                encontrarEBotar((JPanel) comp, "Fechar Caixa", false);
                encontrarEBotar((JPanel) comp, "Suprir", false);
                encontrarEBotar((JPanel) comp, "Sangria", false);
            }
        }
    }
    
    private void encontrarEBotar(JPanel painel, String texto, boolean habilitar) {
        for (Component comp : painel.getComponents()) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                if (btn.getText().contains(texto)) {
                    btn.setEnabled(habilitar);
                }
            } else if (comp instanceof JPanel) {
                encontrarEBotar((JPanel) comp, texto, habilitar);
            }
        }
    }
    
    private void adicionarMovimentacao(String tipo, String descricao, BigDecimal valor, String operador) {
        MovimentacaoCaixa mov = new MovimentacaoCaixa(
            LocalDateTime.now(),
            tipo,
            descricao,
            valor,
            saldoAtual,
            operador
        );
        
        movimentacoes.add(mov);
        atualizarTabela();
        SystemLogger.ui("Movimentação adicionada: " + tipo + " - " + descricao + " - R$ " + valor);
    }
    
    private void atualizarTabela() {
        if (tableModel != null) {
            // Limpar tabela atual
            tableModel.setRowCount(0);
            
            // Adicionar movimentações
            for (MovimentacaoCaixa mov : movimentacoes) {
                Object[] rowData = {
                    mov.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                    mov.getTipo(),
                    mov.getDescricao(),
                    String.format("R$ %,.2f", mov.getValor()),
                    String.format("R$ %,.2f", mov.getSaldo()),
                    mov.getOperador()
                };
                tableModel.addRow(rowData);
            }
        }
    }
    
    private void buscarMovimentacoes(String dataInicial, String dataFinal, String tipo) {
        SystemLogger.ui("Buscando movimentações - Data: " + dataInicial + " a " + dataFinal + ", Tipo: " + tipo);
        
        // Lógica de busca simulada
        if (dataInicial.isEmpty() && dataFinal.isEmpty() && "Todos".equals(tipo)) {
            JOptionPane.showMessageDialog(workArea, 
                "Por favor, informe pelo menos um filtro para buscar.",
                "Busca", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Simulação de busca
        String mensagem = "Resultados encontrados:\n\n";
        mensagem += "• Período: " + (dataInicial.isEmpty() ? "início" : dataInicial) + " a " + (dataFinal.isEmpty() ? "hoje" : dataFinal) + "\n";
        mensagem += "• Tipo: " + tipo + "\n";
        mensagem += "• " + movimentacoes.size() + " movimentações encontradas";
        
        JOptionPane.showMessageDialog(workArea, mensagem, 
            "Resultados da Busca", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Busca realizada por " + usuarioAtual + ": " + dataInicial + " a " + dataFinal + " - " + tipo);
    }
    
    // Classe interna para representar movimentações
    private static class MovimentacaoCaixa {
        private LocalDateTime dataHora;
        private String tipo;
        private String descricao;
        private BigDecimal valor;
        private BigDecimal saldo;
        private String operador;
        
        public MovimentacaoCaixa(LocalDateTime dataHora, String tipo, String descricao, 
                                BigDecimal valor, BigDecimal saldo, String operador) {
            this.dataHora = dataHora;
            this.tipo = tipo;
            this.descricao = descricao;
            this.valor = valor;
            this.saldo = saldo;
            this.operador = operador;
        }
        
        // Getters
        public LocalDateTime getDataHora() { return dataHora; }
        public String getTipo() { return tipo; }
        public String getDescricao() { return descricao; }
        public BigDecimal getValor() { return valor; }
        public BigDecimal getSaldo() { return saldo; }
        public String getOperador() { return operador; }
    }
    
    // Renderer para centralizar conteúdo das células
    private static class CenteredRenderer extends DefaultTableCellRenderer {
        public CenteredRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }
    }
}
