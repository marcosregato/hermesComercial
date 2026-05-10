package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe especializada para o formulário de Favoritos
 * Estrutura: Header → Busca → Formulário → Tabela
 */
public class PDVFormularioFavoritos {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes do formulário
    private JTextField txtBusca;
    private JTextField txtNomeFavorito;
    private JTextField txtDescricao;
    private JTextField txtUrl;
    private JComboBox<String> comboCategoria;
    private JComboBox<String> comboTipo;
    
    // Tabela de favoritos
    private JTable tabelaFavoritos;
    private DefaultTableModel modelTabela;
    private List<Favorito> favoritosEncontrados;
    
    // Cores
    private static final Color WHITE = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private static final Color DANGER_COLOR = new Color(231, 76, 60);
    private static final Color WARNING_COLOR = new Color(241, 196, 15);
    private static final Color GRAY = new Color(149, 165, 166);
    
    public PDVFormularioFavoritos(JPanel workArea, String usuarioAtual, String nomeUsuario) {
        this.workArea = workArea;
        this.usuarioAtual = usuarioAtual;
        this.nomeUsuario = nomeUsuario;
        this.favoritosEncontrados = new ArrayList<>();
    }
    
    /**
     * Cria o formulário completo de Favoritos
     */
    public JPanel criarFormularioFavoritos() {
        try {
            SystemLogger.ui("=== CRIANDO FORMULÁRIO FAVORITOS ===");
            SystemLogger.ui("Usuário: " + usuarioAtual);
            
            JPanel painelPrincipal = new JPanel(new BorderLayout());
            painelPrincipal.setBackground(WHITE);
            
            // Header
            JPanel headerPanel = criarHeader();
            painelPrincipal.add(headerPanel, BorderLayout.NORTH);
            
            // Painel central com busca, formulário e tabela
            JPanel painelCentral = new JPanel(new BorderLayout());
            painelCentral.setBackground(WHITE);
            
            // Painel de busca rápida
            JPanel buscaPanel = criarPainelBusca();
            painelCentral.add(buscaPanel, BorderLayout.NORTH);
            
            // Painel do formulário
            JPanel formularioPanel = criarPainelFormulario();
            painelCentral.add(formularioPanel, BorderLayout.CENTER);
            
            // Tabela de favoritos
            JPanel tabelaPanel = criarPainelTabela();
            painelCentral.add(tabelaPanel, BorderLayout.SOUTH);
            
            painelPrincipal.add(painelCentral, BorderLayout.CENTER);
            
            SystemLogger.ui("Formulário Favoritos criado com sucesso");
            return painelPrincipal;
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao criar formulário Favoritos", e);
            return criarPainelErro();
        }
    }
    
    /**
     * Cria o header do formulário
     */
    private JPanel criarHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ACCENT_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        headerPanel.setPreferredSize(new Dimension(0, 80));
        
        // Título
        JLabel titleLabel = new JLabel("⭐ Favoritos");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(WHITE);
        
        // Informações do usuário
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userInfoPanel.setBackground(ACCENT_COLOR);
        
        JLabel userLabel = new JLabel("👤 " + nomeUsuario);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        userLabel.setForeground(WHITE);
        
        JLabel dateLabel = new JLabel("📅 " + new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date()));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setForeground(WHITE);
        
        userInfoPanel.add(userLabel);
        userInfoPanel.add(Box.createHorizontalStrut(20));
        userInfoPanel.add(dateLabel);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userInfoPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    /**
     * Cria o painel de busca rápida
     */
    private JPanel criarPainelBusca() {
        JPanel buscaPanel = new JPanel(new BorderLayout());
        buscaPanel.setBackground(WHITE);
        buscaPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel buscaLabel = new JLabel("🔍 Busca Rápida:");
        buscaLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        buscaLabel.setForeground(ACCENT_COLOR);
        
        txtBusca = new JTextField();
        txtBusca.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtBusca.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        txtBusca.setToolTipText("Digite nome ou descrição do favorito");
        
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setBackground(ACCENT_COLOR);
        btnBuscar.setForeground(WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorderPainted(false);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBuscar.addActionListener(e -> buscarFavoritos());
        
        JPanel buscaInputPanel = new JPanel(new BorderLayout());
        buscaInputPanel.setBackground(WHITE);
        buscaInputPanel.add(txtBusca, BorderLayout.CENTER);
        buscaInputPanel.add(btnBuscar, BorderLayout.EAST);
        
        buscaPanel.add(buscaLabel, BorderLayout.WEST);
        buscaPanel.add(Box.createHorizontalStrut(10), BorderLayout.CENTER);
        buscaPanel.add(buscaInputPanel, BorderLayout.CENTER);
        
        return buscaPanel;
    }
    
    /**
     * Cria o painel do formulário
     */
    private JPanel criarPainelFormulario() {
        JPanel formularioPanel = new JPanel(new GridBagLayout());
        formularioPanel.setBackground(WHITE);
        formularioPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Seção Dados do Favorito
        JLabel dadosSectionLabel = new JLabel("⭐ Dados do Favorito");
        dadosSectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dadosSectionLabel.setForeground(ACCENT_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        formularioPanel.add(dadosSectionLabel, gbc);
        
        // Nome Favorito
        gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel lblNomeFavorito = new JLabel("Nome do Favorito:");
        lblNomeFavorito.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblNomeFavorito, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNomeFavorito = new JTextField();
        txtNomeFavorito.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtNomeFavorito.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtNomeFavorito, gbc);
        
        // Categoria
        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel lblCategoria = new JLabel("Categoria:");
        lblCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblCategoria, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        comboCategoria = new JComboBox<>(new String[]{"Relatórios", "Produtos", "Clientes", "Vendas", "Financeiro", "Sistema", "Outros"});
        comboCategoria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboCategoria.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboCategoria, gbc);
        
        // Descrição
        gbc.gridy = 2; gbc.gridx = 0;
        JLabel lblDescricao = new JLabel("Descrição:");
        lblDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblDescricao, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
        txtDescricao = new JTextField();
        txtDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDescricao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        formularioPanel.add(txtDescricao, gbc);
        
        // URL/Link
        gbc.gridy = 3; gbc.gridx = 0;
        JLabel lblUrl = new JLabel("URL/Link:");
        lblUrl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblUrl, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
        txtUrl = new JTextField();
        txtUrl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtUrl.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtUrl.setToolTipText("URL ou caminho interno do sistema");
        formularioPanel.add(txtUrl, gbc);
        
        // Tipo
        gbc.gridy = 4; gbc.gridx = 0;
        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        formularioPanel.add(lblTipo, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboTipo = new JComboBox<>(new String[]{"Relatório", "Página", "Consulta", "Processo", "Arquivo", "Link Externo"});
        comboTipo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboTipo.setBorder(BorderFactory.createLineBorder(GRAY));
        formularioPanel.add(comboTipo, gbc);
        
        // Botões de ação
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 0.0;
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botoesPanel.setBackground(WHITE);
        
        JButton btnSalvar = new JButton("💾 Salvar Favorito");
        btnSalvar.setBackground(SUCCESS_COLOR);
        btnSalvar.setForeground(WHITE);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setBorderPainted(false);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSalvar.addActionListener(e -> salvarFavorito());
        
        JButton btnLimpar = new JButton("🧹 Limpar");
        btnLimpar.setBackground(GRAY);
        btnLimpar.setForeground(WHITE);
        btnLimpar.setFocusPainted(false);
        btnLimpar.setBorderPainted(false);
        btnLimpar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimpar.addActionListener(e -> limparCampos());
        
        JButton btnAcessar = new JButton("🔗 Acessar");
        btnAcessar.setBackground(ACCENT_COLOR);
        btnAcessar.setForeground(WHITE);
        btnAcessar.setFocusPainted(false);
        btnAcessar.setBorderPainted(false);
        btnAcessar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAcessar.addActionListener(e -> acessarFavorito());
        
        JButton btnExportar = new JButton("📤 Exportar");
        btnExportar.setBackground(WARNING_COLOR);
        btnExportar.setForeground(WHITE);
        btnExportar.setFocusPainted(false);
        btnExportar.setBorderPainted(false);
        btnExportar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExportar.addActionListener(e -> exportarFavoritos());
        
        botoesPanel.add(btnSalvar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnLimpar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnAcessar);
        botoesPanel.add(Box.createHorizontalStrut(10));
        botoesPanel.add(btnExportar);
        
        formularioPanel.add(botoesPanel, gbc);
        
        // Preencher dados iniciais
        preencherDadosIniciais();
        
        return formularioPanel;
    }
    
    /**
     * Cria o painel da tabela
     */
    private JPanel criarPainelTabela() {
        JPanel tabelaPanel = new JPanel(new BorderLayout());
        tabelaPanel.setBackground(WHITE);
        tabelaPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        JLabel tabelaLabel = new JLabel("⭐ Favoritos Salvos");
        tabelaLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabelaLabel.setForeground(ACCENT_COLOR);
        
        // Modelo da tabela
        String[] colunas = {"Nome", "Categoria", "Tipo", "Descrição", "URL", "Data Criação", "Ações"};
        modelTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Apenas a coluna de ações é editável
            }
        };
        
        tabelaFavoritos = new JTable(modelTabela);
        tabelaFavoritos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelaFavoritos.setRowHeight(25);
        tabelaFavoritos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabelaFavoritos.getTableHeader().setBackground(ACCENT_COLOR);
        tabelaFavoritos.getTableHeader().setForeground(WHITE);
        
        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(tabelaFavoritos);
        scrollPane.setPreferredSize(new Dimension(0, 250));
        scrollPane.setBorder(BorderFactory.createLineBorder(GRAY));
        
        // Painel de botões da tabela
        JPanel botoesTabelaPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoesTabelaPanel.setBackground(WHITE);
        
        JButton btnEditar = new JButton("✏️ Editar");
        btnEditar.setBackground(ACCENT_COLOR);
        btnEditar.setForeground(WHITE);
        btnEditar.setFocusPainted(false);
        btnEditar.setBorderPainted(false);
        btnEditar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEditar.addActionListener(e -> editarFavorito());
        
        JButton btnAcessarTabela = new JButton("🔗 Acessar");
        btnAcessarTabela.setBackground(SUCCESS_COLOR);
        btnAcessarTabela.setForeground(WHITE);
        btnAcessarTabela.setFocusPainted(false);
        btnAcessarTabela.setBorderPainted(false);
        btnAcessarTabela.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnAcessarTabela.addActionListener(e -> acessarFavoritoSelecionado());
        
        JButton btnExcluir = new JButton("🗑️ Excluir");
        btnExcluir.setBackground(DANGER_COLOR);
        btnExcluir.setForeground(WHITE);
        btnExcluir.setFocusPainted(false);
        btnExcluir.setBorderPainted(false);
        btnExcluir.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnExcluir.addActionListener(e -> excluirFavorito());
        
        botoesTabelaPanel.add(btnEditar);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnAcessarTabela);
        botoesTabelaPanel.add(Box.createHorizontalStrut(10));
        botoesTabelaPanel.add(btnExcluir);
        
        tabelaPanel.add(tabelaLabel, BorderLayout.NORTH);
        tabelaPanel.add(scrollPane, BorderLayout.CENTER);
        tabelaPanel.add(botoesTabelaPanel, BorderLayout.SOUTH);
        
        // Adicionar dados de exemplo
        adicionarDadosExemplo();
        
        return tabelaPanel;
    }
    
    /**
     * Adiciona dados de exemplo à tabela
     */
    private void adicionarDadosExemplo() {
        // Limpar tabela atual
        modelTabela.setRowCount(0);
        favoritosEncontrados.clear();
        
        // Dados de exemplo
        Object[][] dadosExemplo = {
            {"Relatório de Vendas Diárias", "Relatórios", "Relatório", "Relatório completo de vendas do dia", "/relatorios/vendas/dia", "10/05/2026", "✏️"},
            {"Consulta de Produtos", "Produtos", "Consulta", "Busca rápida de produtos", "/produtos/consulta", "09/05/2026", "✏️"},
            {"Resumo Financeiro", "Financeiro", "Página", "Dashboard financeiro", "/financeiro/resumo", "08/05/2026", "✏️"},
            {"Clientes Ativos", "Clientes", "Página", "Lista de clientes ativos", "/clientes/ativos", "07/05/2026", "✏️"},
            {"Estoque Baixo", "Produtos", "Consulta", "Produtos com estoque baixo", "/produtos/estoque/baixo", "06/05/2026", "✏️"},
            {"Backup do Sistema", "Sistema", "Processo", "Executar backup completo", "/sistema/backup", "05/05/2026", "✏️"},
            {"Documentação Online", "Outros", "Link Externo", "Documentação do sistema", "https://docs.hermes.com", "04/05/2026", "✏️"},
            {"Configurações Gerais", "Sistema", "Página", "Configurações do sistema", "/sistema/configuracoes", "03/05/2026", "✏️"}
        };
        
        for (Object[] dados : dadosExemplo) {
            modelTabela.addRow(dados);
            
            // Adicionar à lista de favoritos
            Favorito favorito = new Favorito();
            favorito.setNome((String) dados[0]);
            favorito.setCategoria((String) dados[1]);
            favorito.setTipo((String) dados[2]);
            favorito.setDescricao((String) dados[3]);
            favorito.setUrl((String) dados[4]);
            favorito.setDataCriacao((String) dados[5]);
            favoritosEncontrados.add(favorito);
        }
    }
    
    /**
     * Preenche dados iniciais
     */
    private void preencherDadosIniciais() {
        comboCategoria.setSelectedIndex(0);
        comboTipo.setSelectedIndex(0);
    }
    
    /**
     * Busca favoritos
     */
    private void buscarFavoritos() {
        String termo = txtBusca.getText().trim();
        if (termo.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite um termo para buscar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // TODO: Implementar lógica de busca no banco de dados
        JOptionPane.showMessageDialog(workArea, 
            "Busca realizada para: " + termo + "\n" +
            "Favoritos encontrados: " + favoritosEncontrados.size(), 
            "Resultado", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Busca realizada para: " + termo);
    }
    
    /**
     * Salva favorito
     */
    private void salvarFavorito() {
        try {
            String nomeFavorito = txtNomeFavorito.getText().trim();
            String descricao = txtDescricao.getText().trim();
            String url = txtUrl.getText().trim();
            
            if (nomeFavorito.isEmpty()) {
                JOptionPane.showMessageDialog(workArea, "Digite o nome do favorito!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (url.isEmpty()) {
                JOptionPane.showMessageDialog(workArea, "Digite a URL ou link do favorito!", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // TODO: Implementar lógica de salvamento no banco de dados
            JOptionPane.showMessageDialog(workArea, 
                "Favorito salvo com sucesso!\n\n" +
                "Nome: " + nomeFavorito + "\n" +
                "Categoria: " + comboCategoria.getSelectedItem() + "\n" +
                "Tipo: " + comboTipo.getSelectedItem() + "\n" +
                "URL: " + url + "\n" +
                "Responsável: " + nomeUsuario, 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Favorito salvo por: " + nomeUsuario);
            
        } catch (Exception e) {
            SystemLogger.error("Erro ao salvar favorito", e);
            JOptionPane.showMessageDialog(workArea, "Erro ao salvar favorito: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Limpa os campos do formulário
     */
    private void limparCampos() {
        txtBusca.setText("");
        txtNomeFavorito.setText("");
        txtDescricao.setText("");
        txtUrl.setText("");
        
        preencherDadosIniciais();
    }
    
    /**
     * Acessa favorito do formulário
     */
    private void acessarFavorito() {
        String url = txtUrl.getText().trim();
        String nome = txtNomeFavorito.getText().trim();
        
        if (url.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Digite a URL do favorito!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(workArea, 
            "Acessando favorito...\n\n" +
            "Nome: " + (nome.isEmpty() ? "Não informado" : nome) + "\n" +
            "URL: " + url + "\n\n" +
            "(Implementar navegação para a URL)", 
            "Acessar Favorito", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Acessando favorito: " + url);
    }
    
    /**
     * Exporta favoritos
     */
    private void exportarFavoritos() {
        JOptionPane.showMessageDialog(workArea, 
            "Exportando " + favoritosEncontrados.size() + " favoritos...\n(Implementar exportação para JSON/CSV)", 
            "Exportar", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Exportando " + favoritosEncontrados.size() + " favoritos");
    }
    
    /**
     * Edita favorito selecionado
     */
    private void editarFavorito() {
        int selectedRow = tabelaFavoritos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione um favorito para editar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Favorito favorito = favoritosEncontrados.get(selectedRow);
        
        // Preencher formulário com dados selecionados
        txtNomeFavorito.setText(favorito.getNome());
        txtDescricao.setText(favorito.getDescricao());
        txtUrl.setText(favorito.getUrl());
        comboCategoria.setSelectedItem(favorito.getCategoria());
        comboTipo.setSelectedItem(favorito.getTipo());
        
        JOptionPane.showMessageDialog(workArea, 
            "Favorito carregado para edição:\n\n" +
            "Nome: " + favorito.getNome() + "\n" +
            "Categoria: " + favorito.getCategoria() + "\n" +
            "URL: " + favorito.getUrl(), 
            "Editar Favorito", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Favorito carregado para edição: " + favorito.getNome());
    }
    
    /**
     * Acessa favorito selecionado na tabela
     */
    private void acessarFavoritoSelecionado() {
        int selectedRow = tabelaFavoritos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione um favorito para acessar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Favorito favorito = favoritosEncontrados.get(selectedRow);
        
        JOptionPane.showMessageDialog(workArea, 
            "Acessando favorito...\n\n" +
            "Nome: " + favorito.getNome() + "\n" +
            "Categoria: " + favorito.getCategoria() + "\n" +
            "URL: " + favorito.getUrl() + "\n\n" +
            "(Implementar navegação para a URL)", 
            "Acessar Favorito", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Acessando favorito: " + favorito.getNome());
    }
    
    /**
     * Exclui favorito selecionado
     */
    private void excluirFavorito() {
        int selectedRow = tabelaFavoritos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione um favorito para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Favorito favorito = favoritosEncontrados.get(selectedRow);
        
        int confirmacao = JOptionPane.showConfirmDialog(
            workArea,
            "Deseja realmente excluir este favorito?\n\n" +
            "Nome: " + favorito.getNome() + "\n" +
            "Esta ação não pode ser desfeita.",
            "Excluir Favorito",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            // TODO: Implementar lógica de exclusão no banco de dados
            
            favoritosEncontrados.remove(selectedRow);
            modelTabela.removeRow(selectedRow);
            
            JOptionPane.showMessageDialog(workArea, 
                "Favorito excluído com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            SystemLogger.ui("Favorito excluído: " + favorito.getNome());
        }
    }
    
    /**
     * Cria painel de erro
     */
    private JPanel criarPainelErro() {
        JPanel painelErro = new JPanel(new BorderLayout());
        painelErro.setBackground(WHITE);
        
        JLabel erroLabel = new JLabel("❌ Erro ao carregar formulário");
        erroLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        erroLabel.setForeground(DANGER_COLOR);
        erroLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        painelErro.add(erroLabel, BorderLayout.CENTER);
        return painelErro;
    }
    
    /**
     * Classe interna para representar um favorito
     */
    private static class Favorito {
        private String nome;
        private String categoria;
        private String tipo;
        private String descricao;
        private String url;
        private String dataCriacao;
        
        // Getters e Setters
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        
        public String getCategoria() { return categoria; }
        public void setCategoria(String categoria) { this.categoria = categoria; }
        
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        
        public String getDescricao() { return descricao; }
        public void setDescricao(String descricao) { this.descricao = descricao; }
        
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
        
        public String getDataCriacao() { return dataCriacao; }
        public void setDataCriacao(String dataCriacao) { this.dataCriacao = dataCriacao; }
    }
}
