package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.pdv.base.BaseFormulario;
import com.br.hermescomercial.util.SystemLogger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Formulário de Ajuste de Estoque
 * Gerencia ajustes de entrada e saída de produtos no estoque
 */
public class PDVFormularioAjusteEstoque extends BaseFormulario {
    
    // Componentes do formulário
    private JTextField txtBusca;
    private JTextField txtCodigo;
    private JTextField txtProduto;
    private JComboBox<String> comboCategoria;
    private JTextField txtEstoqueAtual;
    private JComboBox<String> comboTipoAjuste;
    private JTextField txtQuantidadeAjuste;
    private JTextField txtMotivo;
    
    // Tabela de ajustes
    private JTable tabelaAjustes;
    private DefaultTableModel modelTabela;
    private List<AjusteEstoque> ajustesRealizados;
    
    public PDVFormularioAjusteEstoque(JPanel workArea, String usuarioAtual, String nomeUsuario) {
        super(workArea, usuarioAtual, nomeUsuario, "Ajuste de Estoque");
        this.ajustesRealizados = new ArrayList<>();
    }
    
    /**
     * Cria o formulário completo de Ajuste de Estoque
     */
    @Override
    public JPanel criarFormulario() {
        try {
            SystemLogger.ui("=== CRIANDO FORMULÁRIO AJUSTE ESTOQUE ===");
            SystemLogger.ui("Usuário: " + usuarioAtual);
            
            JPanel painelPrincipal = new JPanel(new BorderLayout());
            painelPrincipal.setBackground(corPrimaria);
            
            // Header
            JPanel headerPanel = criarPainelTitulo("📦 Ajuste de Estoque");
            painelPrincipal.add(headerPanel, BorderLayout.NORTH);
            
            // Painel central com busca, formulário e tabela
            JPanel painelCentral = new JPanel(new BorderLayout());
            painelCentral.setBackground(corPrimaria);
            
            // Painel de busca rápida
            JPanel buscaPanel = criarPainelBusca();
            painelCentral.add(buscaPanel, BorderLayout.NORTH);
            
            // Painel do formulário
            JPanel formularioPanel = criarPainelFormulario();
            painelCentral.add(formularioPanel, BorderLayout.CENTER);
            
            // Painel da tabela
            JPanel tabelaPanel = criarPainelTabela();
            painelCentral.add(tabelaPanel, BorderLayout.SOUTH);
            
            painelPrincipal.add(painelCentral, BorderLayout.CENTER);
            
            // Carregar dados exemplo
            carregarDadosExemplo();
            
            return painelPrincipal;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar formulário de ajuste de estoque", e);
            return criarPainelErro();
        }
    }
    
    /**
     * Cria painel de busca
     */
    private JPanel criarPainelBusca() {
        JPanel buscaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buscaPanel.setBackground(corPrimaria);
        buscaPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel lblBusca = new JLabel("🔍 Buscar:");
        lblBusca.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        buscaPanel.add(lblBusca);
        
        txtBusca = criarCampoTexto("", false);
        txtBusca.setToolTipText("Digite código, nome ou categoria do produto");
        txtBusca.setPreferredSize(new Dimension(200, 30));
        buscaPanel.add(txtBusca);
        
        JButton btnBuscar = criarBotao("Buscar", corPrimaria);
        btnBuscar.addActionListener(e -> buscarProduto());
        buscaPanel.add(btnBuscar);
        
        JButton btnLimpar = criarBotao("Limpar", corAviso);
        btnLimpar.addActionListener(e -> limparCampos());
        buscaPanel.add(btnLimpar);
        
        return buscaPanel;
    }
    
    /**
     * Cria painel do formulário
     */
    private JPanel criarPainelFormulario() {
        JPanel formularioPanel = new JPanel(new GridBagLayout());
        formularioPanel.setBackground(corPrimaria);
        formularioPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Código do Produto
        gbc.gridy = 0; gbc.gridx = 0; gbc.weightx = 0.0;
        JLabel lblCodigo = new JLabel("Código:");
        lblCodigo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblCodigo, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCodigo = criarCampoTexto("", false);
        txtCodigo.setToolTipText("Código do produto");
        formularioPanel.add(txtCodigo, gbc);
        
        // Nome do Produto
        gbc.gridy = 1; gbc.gridx = 0; gbc.weightx = 0.0;
        JLabel lblProduto = new JLabel("Produto:");
        lblProduto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblProduto, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtProduto = criarCampoTexto("", false);
        txtProduto.setToolTipText("Nome do produto");
        formularioPanel.add(txtProduto, gbc);
        
        // Categoria
        gbc.gridy = 2; gbc.gridx = 0; gbc.weightx = 0.0;
        JLabel lblCategoria = new JLabel("Categoria:");
        lblCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblCategoria, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboCategoria = criarComboBox(new String[]{"Eletrônicos", "Roupas", "Alimentos", "Móveis", "Livros", "Outros"}, "Categoria", false);
        formularioPanel.add(comboCategoria, gbc);
        
        // Estoque Atual
        gbc.gridy = 3; gbc.gridx = 0; gbc.weightx = 0.0;
        JLabel lblEstoqueAtual = new JLabel("Estoque Atual:");
        lblEstoqueAtual.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblEstoqueAtual, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtEstoqueAtual = criarCampoTexto("", false);
        txtEstoqueAtual.setToolTipText("Quantidade atual em estoque");
        formularioPanel.add(txtEstoqueAtual, gbc);
        
        // Tipo de Ajuste
        gbc.gridy = 4; gbc.gridx = 0; gbc.weightx = 0.0;
        JLabel lblTipoAjuste = new JLabel("Tipo Ajuste:");
        lblTipoAjuste.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblTipoAjuste, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboTipoAjuste = criarComboBox(new String[]{"Entrada", "Saída", "Ajuste Positivo", "Ajuste Negativo", "Perda", "Quebra"}, "Tipo de Ajuste", false);
        formularioPanel.add(comboTipoAjuste, gbc);
        
        // Quantidade
        gbc.gridy = 5; gbc.gridx = 0; gbc.weightx = 0.0;
        JLabel lblQuantidadeAjuste = new JLabel("Quantidade:");
        lblQuantidadeAjuste.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblQuantidadeAjuste, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtQuantidadeAjuste = criarCampoTexto("", false);
        txtQuantidadeAjuste.setToolTipText("Quantidade do ajuste");
        formularioPanel.add(txtQuantidadeAjuste, gbc);
        
        // Motivo
        gbc.gridy = 6; gbc.gridx = 0; gbc.weightx = 0.0;
        JLabel lblMotivo = new JLabel("Motivo:");
        lblMotivo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblMotivo, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtMotivo = criarCampoTexto("", false);
        txtMotivo.setToolTipText("Motivo do ajuste (ex: devolução, quebra, ajuste de inventário)");
        formularioPanel.add(txtMotivo, gbc);
        
        // Botões de ação
        gbc.gridy = 7; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoesPanel.setBackground(corPrimaria);
        
        JButton btnProcessar = criarBotao("🔄 Processar Ajuste", corSucesso);
        btnProcessar.addActionListener(e -> processarAjuste());
        
        JButton btnLimpar = criarBotao("🧹 Limpar", corAviso);
        btnLimpar.addActionListener(e -> limparCampos());
        
        botoesPanel.add(btnProcessar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnLimpar);
        
        formularioPanel.add(botoesPanel, gbc);
        
        return formularioPanel;
    }
    
    /**
     * Cria painel da tabela de ajustes
     */
    private JPanel criarPainelTabela() {
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBackground(corPrimaria);
        tabelaPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        
        // Título da tabela
        JLabel lblTituloTabela = new JLabel("📋 Histórico de Ajustes");
        lblTituloTabela.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTituloTabela.setForeground(corPrimaria);
        lblTituloTabela.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        tabelaPanel.add(lblTituloTabela, BorderLayout.NORTH);
        
        // Tabela
        String[] colunas = {"Código", "Data/Hora", "Produto", "Tipo", "Quantidade", "Estoque Anterior", "Estoque Atual", "Motivo", "Responsável", "Status"};
        modelTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return String.class;
                if (columnIndex == 1) return String.class;
                if (columnIndex == 2) return String.class;
                if (columnIndex == 3) return String.class;
                if (columnIndex == 4) return String.class;
                if (columnIndex == 5) return String.class;
                if (columnIndex == 6) return String.class;
                if (columnIndex == 7) return String.class;
                if (columnIndex == 8) return String.class;
                if (columnIndex == 9) return String.class;
                return Object.class;
            }
        };
        
        tabelaAjustes = new JTable(modelTabela);
        tabelaAjustes.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tabelaAjustes.setRowHeight(25);
        tabelaAjustes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaAjustes.getTableHeader().setBackground(corPrimaria);
        tabelaAjustes.getTableHeader().setForeground(corPrimaria);
        
        // Scroll pane para a tabela
        JScrollPane scrollPane = new JScrollPane(tabelaAjustes);
        scrollPane.setPreferredSize(new Dimension(800, 200));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        tabelaPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Painel de botões da tabela
        JPanel botoesTabelaPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoesTabelaPanel.setBackground(corPrimaria);
        botoesTabelaPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JButton btnFiltrar = criarBotao("🔍 Filtrar", corPrimaria);
        btnFiltrar.addActionListener(e -> filtrarAjustes());
        
        JButton btnRelatorio = criarBotao("📊 Relatório", corSucesso);
        btnRelatorio.addActionListener(e -> gerarRelatorio());
        
        JButton btnExportar = criarBotao("📤 Exportar", corAviso);
        btnExportar.addActionListener(e -> exportarDados());
        
        botoesTabelaPanel.add(btnFiltrar);
        botoesTabelaPanel.add(Box.createHorizontalStrut(5));
        botoesTabelaPanel.add(btnRelatorio);
        botoesTabelaPanel.add(Box.createHorizontalStrut(5));
        botoesTabelaPanel.add(btnExportar);
        
        tabelaPanel.add(botoesTabelaPanel, BorderLayout.SOUTH);
        
        return tabelaPanel;
    }
    
    /**
     * Busca produto
     */
    private void buscarProduto() {
        String termo = txtBusca.getText().trim();
        if (termo.isEmpty()) {
            mostrarMensagemAviso("Digite um termo para buscar!");
            return;
        }
        
        // Busca simulada no banco de dados
        mostrarMensagemSucesso("Busca realizada para: " + termo + "\nProdutos encontrados: " + ajustesRealizados.size());
        
        SystemLogger.ui("Busca realizada para: " + termo);
    }
    
    /**
     * Processa ajuste de estoque
     */
    private void processarAjuste() {
        String produto = txtProduto.getText().trim();
        String quantidade = txtQuantidadeAjuste.getText().trim();
        String motivo = txtMotivo.getText().trim();
        String tipoAjuste = (String) comboTipoAjuste.getSelectedItem();
        
        if (!validarCamposObrigatorios(txtProduto, txtQuantidadeAjuste, txtMotivo)) {
            return;
        }
        
        try {
            int qtd = Integer.parseInt(quantidade);
            
            // Processamento simulado no banco de dados
            mostrarMensagemSucesso("Ajuste processado com sucesso!\n" +
                "Produto: " + produto + "\n" +
                "Quantidade: " + quantidade + "\n" +
                "Tipo: " + tipoAjuste + "\n" +
                "Motivo: " + motivo);
            
            // Adicionar à tabela
            Object[] dados = {
                "AJ" + System.currentTimeMillis(),
                java.time.LocalDateTime.now().toString(),
                produto,
                tipoAjuste,
                quantidade,
                txtEstoqueAtual.getText(),
                String.valueOf(Integer.parseInt(txtEstoqueAtual.getText()) + qtd),
                motivo,
                nomeUsuario,
                "Processado"
            };
            
            modelTabela.addRow(dados);
            
            // Adicionar à lista de ajustes
            AjusteEstoque ajuste = new AjusteEstoque();
            ajuste.setCodigo((String) dados[0]);
            ajuste.setDataHora((String) dados[1]);
            ajuste.setProduto((String) dados[2]);
            ajuste.setTipoAjuste((String) dados[3]);
            ajuste.setQuantidade((String) dados[4]);
            ajuste.setEstoqueAnterior((String) dados[5]);
            ajuste.setEstoqueAtual((String) dados[6]);
            ajuste.setMotivo((String) dados[7]);
            ajuste.setResponsavel((String) dados[8]);
            ajuste.setStatus((String) dados[9]);
            ajustesRealizados.add(ajuste);
            
            SystemLogger.ui("Ajuste processado: " + produto + " - " + tipoAjuste + " - " + quantidade);
            
        } catch (NumberFormatException e) {
            mostrarMensagemErro("Quantidade inválida! Digite apenas números.");
        }
    }
    
    /**
     * Limpa os campos do formulário
     */
    private void limparCampos() {
        txtBusca.setText("");
        txtCodigo.setText("");
        txtProduto.setText("");
        comboCategoria.setSelectedIndex(0);
        txtEstoqueAtual.setText("");
        comboTipoAjuste.setSelectedIndex(0);
        txtQuantidadeAjuste.setText("");
        txtMotivo.setText("");
    }
    
    /**
     * Filtra ajustes
     */
    private void filtrarAjustes() {
        mostrarMensagemSucesso("Filtrando ajustes...\n(Implementar filtros por período, tipo, produto, etc.)");
        SystemLogger.ui("Filtrando ajustes - " + ajustesRealizados.size() + " registros");
    }
    
    /**
     * Gera relatório
     */
    private void gerarRelatorio() {
        mostrarMensagemSucesso("Gerando relatório de " + ajustesRealizados.size() + " ajustes...\n(Implementar geração de relatório PDF/Excel)");
        SystemLogger.ui("Gerando relatório de " + ajustesRealizados.size() + " ajustes");
    }
    
    /**
     * Exporta dados da tabela
     */
    private void exportarDados() {
        mostrarMensagemSucesso("Exportando " + ajustesRealizados.size() + " ajustes...\n(Implementar exportação para CSV/Excel)");
        SystemLogger.ui("Exportando " + ajustesRealizados.size() + " ajustes");
    }
    
    /**
     * Carrega dados exemplo na tabela
     */
    private void carregarDadosExemplo() {
        Object[] dadosExemplo = {
            "AJ001", "2024-01-15 10:30", "Notebook Dell", "Entrada", "10", "5", "15", "Compra de novo lote", "João Silva", "Concluído"
        };
        modelTabela.addRow(dadosExemplo);
        
        dadosExemplo = new Object[]{
            "AJ002", "2024-01-15 11:00", "Mouse USB", "Saída", "2", "17", "15", "Venda para cliente", "João Silva", "Concluído"
        };
        modelTabela.addRow(dadosExemplo);
    }
    
    /**
     * Cria painel de erro
     */
    private JPanel criarPainelErro() {
        JPanel painelErro = new JPanel(new BorderLayout());
        painelErro.setBackground(corPrimaria);
        
        JLabel erroLabel = new JLabel("❌ Erro ao carregar formulário");
        erroLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        erroLabel.setForeground(corErro);
        erroLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        painelErro.add(erroLabel, BorderLayout.CENTER);
        return painelErro;
    }
    
    /**
     * Classe interna para representar um ajuste de estoque
     */
    private static class AjusteEstoque {
        private String codigo;
        private String dataHora;
        private String produto;
        private String tipoAjuste;
        private String quantidade;
        private String estoqueAnterior;
        private String estoqueAtual;
        private String motivo;
        private String responsavel;
        private String status;
        
        // Getters e Setters
        @SuppressWarnings("unused")
        public String getCodigo() { return codigo; }
        public void setCodigo(String codigo) { this.codigo = codigo; }
        
        @SuppressWarnings("unused")
        public String getDataHora() { return dataHora; }
        public void setDataHora(String dataHora) { this.dataHora = dataHora; }
        
        @SuppressWarnings("unused")
        public String getProduto() { return produto; }
        public void setProduto(String produto) { this.produto = produto; }
        
        @SuppressWarnings("unused")
        public String getTipoAjuste() { return tipoAjuste; }
        public void setTipoAjuste(String tipoAjuste) { this.tipoAjuste = tipoAjuste; }
        
        @SuppressWarnings("unused")
        public String getQuantidade() { return quantidade; }
        public void setQuantidade(String quantidade) { this.quantidade = quantidade; }
        
        @SuppressWarnings("unused")
        public String getEstoqueAnterior() { return estoqueAnterior; }
        public void setEstoqueAnterior(String estoqueAnterior) { this.estoqueAnterior = estoqueAnterior; }
        
        @SuppressWarnings("unused")
        public String getEstoqueAtual() { return estoqueAtual; }
        public void setEstoqueAtual(String estoqueAtual) { this.estoqueAtual = estoqueAtual; }
        
        @SuppressWarnings("unused")
        public String getMotivo() { return motivo; }
        public void setMotivo(String motivo) { this.motivo = motivo; }
        
        @SuppressWarnings("unused")
        public String getResponsavel() { return responsavel; }
        public void setResponsavel(String responsavel) { this.responsavel = responsavel; }
        
        @SuppressWarnings("unused")
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
