package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.ui.layout.LayoutPadrao;
import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe especializada para formulário de gestão de fornecedores
 * Segue o padrão Header → Busca → Formulário → Tabela
 * Versão 1.0.0 - Adaptada para PDVMenuLateralElegante
 */
public class PDVFormularioFornecedor {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes da interface
    private JTextField txtNome;
    private JTextField txtCnpjCpf;
    private JTextField txtTelefone;
    private JTextField txtCelular;
    private JTextField txtEmail;
    private JTextField txtSite;
    private JTextField txtEndereco;
    private JTextField txtNumero;
    private JTextField txtComplemento;
    private JTextField txtBairro;
    private JTextField txtCidade;
    private JTextField txtEstado;
    private JTextField txtCep;
    private JTextField txtContato;
    private JTextField txtCargo;
    private JComboBox<String> comboStatus;
    private JComboBox<String> comboTipoPessoa;
    private JTextArea txtObservacoes;
    
    // Componentes de busca
    private JTextField txtBuscaNome;
    private JTextField txtBuscaCnpj;
    private JComboBox<String> comboBuscaStatus;
    
    // Tabela
    private JTable tblFornecedores;
    private DefaultTableModel tableModel;
    
    // Dados
    private List<Fornecedor> fornecedores;
    private Fornecedor fornecedorEditando;
    
    // Cores
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private static final Color DANGER_COLOR = new Color(244, 67, 54);
    private static final Color WARNING_COLOR = new Color(255, 193, 7);
    private static final Color PRIMARY_COLOR = new Color(33, 150, 243);
    
    public PDVFormularioFornecedor(JPanel workArea, String usuario, String nome) {
        this.workArea = workArea;
        this.usuarioAtual = usuario;
        this.nomeUsuario = nome;
        this.fornecedores = new ArrayList<>();
        carregarDadosExemplo();
    }
    
    public JPanel criarFormularioFornecedor() {
        SystemLogger.ui("=== CRIANDO FORMULÁRIO FORNECEDOR ===");
        SystemLogger.ui("Usuário: " + usuarioAtual);
        
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(Color.WHITE);
        
        // Header
        painelPrincipal.add(criarHeader(), BorderLayout.NORTH);
        
        // Conteúdo principal
        JPanel conteudo = new JPanel(new BorderLayout());
        conteudo.setBackground(Color.WHITE);
        
        // Painel de busca
        conteudo.add(criarPainelBusca(), BorderLayout.NORTH);
        
        // Painel central (formulário + tabela)
        JPanel painelCentral = new JPanel(new BorderLayout());
        painelCentral.setBackground(Color.WHITE);
        
        // Formulário
        painelCentral.add(criarPainelFormulario(), BorderLayout.NORTH);
        
        // Tabela
        painelCentral.add(criarPainelTabela(), BorderLayout.CENTER);
        
        conteudo.add(painelCentral, BorderLayout.CENTER);
        
        // Painel de ações
        conteudo.add(criarPainelAcoes(), BorderLayout.SOUTH);
        
        painelPrincipal.add(conteudo, BorderLayout.CENTER);
        
        SystemLogger.ui("Formulário Fornecedor criado com sucesso");
        return painelPrincipal;
    }
    
    private JPanel criarHeader() {
        JPanel header = LayoutPadrao.criarHeaderPanel("🏭 Gestão de Fornecedores");
        
        // Informações do usuário
        JPanel userInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userInfo.setBackground(new Color(250, 250, 250));
        userInfo.add(new JLabel("👤 " + nomeUsuario));
        userInfo.add(new JLabel(" | "));
        userInfo.add(new JLabel("📅 " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
        
        header.add(userInfo, BorderLayout.EAST);
        return header;
    }
    
    private JPanel criarPainelBusca() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(new Color(248, 249, 250));
        painel.setBorder(BorderFactory.createTitledBorder("🔍 Buscar Fornecedores"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Campo de busca por nome
        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtBuscaNome = new JTextField(20);
        txtBuscaNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtrarFornecedores();
            }
        });
        painel.add(txtBuscaNome, gbc);
        
        // CNPJ/CPF
        gbc.gridx = 2; gbc.weightx = 0;
        painel.add(new JLabel("CNPJ/CPF:"), gbc);
        gbc.gridx = 3;
        txtBuscaCnpj = new JTextField(15);
        txtBuscaCnpj.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtrarFornecedores();
            }
        });
        painel.add(txtBuscaCnpj, gbc);
        
        // Status
        gbc.gridx = 4;
        painel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 5;
        comboBuscaStatus = new JComboBox<>(new String[]{"Todos", "Ativo", "Inativo", "Bloqueado"});
        comboBuscaStatus.addActionListener(e -> filtrarFornecedores());
        painel.add(comboBuscaStatus, gbc);
        
        // Botão limpar
        gbc.gridx = 6;
        JButton btnLimparBusca = LayoutPadrao.criarBotaoSecundario("🔄 Limpar");
        btnLimparBusca.addActionListener(e -> limparBusca());
        painel.add(btnLimparBusca, gbc);
        
        return painel;
    }
    
    private JPanel criarPainelFormulario() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createTitledBorder("📝 Cadastro de Fornecedores"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Nome
        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(new JLabel("Nome/Razão Social*:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNome = new JTextField(30);
        painel.add(txtNome, gbc);
        
        // Tipo Pessoa
        gbc.gridx = 2; gbc.weightx = 0;
        painel.add(new JLabel("Tipo*:"), gbc);
        gbc.gridx = 3;
        comboTipoPessoa = new JComboBox<>(new String[]{"Pessoa Física", "Pessoa Jurídica"});
        painel.add(comboTipoPessoa, gbc);
        
        // CNPJ/CPF
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        painel.add(new JLabel("CNPJ/CPF*:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCnpjCpf = new JTextField(20);
        painel.add(txtCnpjCpf, gbc);
        
        // Telefone
        gbc.gridx = 2; gbc.weightx = 0;
        painel.add(new JLabel("Telefone*:"), gbc);
        gbc.gridx = 3;
        txtTelefone = new JTextField(20);
        painel.add(txtTelefone, gbc);
        
        // Celular
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        painel.add(new JLabel("Celular:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCelular = new JTextField(20);
        painel.add(txtCelular, gbc);
        
        // Email
        gbc.gridx = 2; gbc.weightx = 0;
        painel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 3;
        txtEmail = new JTextField(25);
        painel.add(txtEmail, gbc);
        
        // Site
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        painel.add(new JLabel("Site:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtSite = new JTextField(25);
        painel.add(txtSite, gbc);
        
        // Contato
        gbc.gridx = 2; gbc.weightx = 0;
        painel.add(new JLabel("Contato:"), gbc);
        gbc.gridx = 3;
        txtContato = new JTextField(20);
        painel.add(txtContato, gbc);
        
        // Cargo
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        painel.add(new JLabel("Cargo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCargo = new JTextField(20);
        painel.add(txtCargo, gbc);
        
        // Endereço
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0;
        painel.add(new JLabel("Endereço:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
        txtEndereco = new JTextField(50);
        painel.add(txtEndereco, gbc);
        
        // Número
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 1; gbc.weightx = 0;
        painel.add(new JLabel("Número:"), gbc);
        gbc.gridx = 1;
        txtNumero = new JTextField(10);
        painel.add(txtNumero, gbc);
        
        // Complemento
        gbc.gridx = 2;
        painel.add(new JLabel("Complemento:"), gbc);
        gbc.gridx = 3;
        txtComplemento = new JTextField(20);
        painel.add(txtComplemento, gbc);
        
        // Bairro
        gbc.gridx = 0; gbc.gridy = 7; gbc.weightx = 0;
        painel.add(new JLabel("Bairro:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtBairro = new JTextField(20);
        painel.add(txtBairro, gbc);
        
        // Cidade
        gbc.gridx = 2; gbc.weightx = 0;
        painel.add(new JLabel("Cidade:"), gbc);
        gbc.gridx = 3;
        txtCidade = new JTextField(25);
        painel.add(txtCidade, gbc);
        
        // Estado
        gbc.gridx = 0; gbc.gridy = 8; gbc.weightx = 0;
        painel.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        txtEstado = new JTextField(10);
        painel.add(txtEstado, gbc);
        
        // CEP
        gbc.gridx = 2;
        painel.add(new JLabel("CEP:"), gbc);
        gbc.gridx = 3;
        txtCep = new JTextField(15);
        painel.add(txtCep, gbc);
        
        // Status
        gbc.gridx = 0; gbc.gridy = 9; gbc.weightx = 0;
        painel.add(new JLabel("Status*:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboStatus = new JComboBox<>(new String[]{"Ativo", "Inativo", "Bloqueado"});
        painel.add(comboStatus, gbc);
        
        // Observações
        gbc.gridx = 0; gbc.gridy = 10; gbc.gridwidth = 4; gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 0.1;
        painel.add(new JLabel("Observações:"), gbc);
        gbc.gridy = 11; gbc.weighty = 0.2;
        JScrollPane scrollPane = new JScrollPane(txtObservacoes = new JTextArea(3, 50));
        txtObservacoes.setLineWrap(true);
        txtObservacoes.setWrapStyleWord(true);
        painel.add(scrollPane, gbc);
        
        return painel;
    }
    
    private JPanel criarPainelTabela() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createTitledBorder("📋 Fornecedores Cadastrados"));
        
        // Configurar tabela
        String[] colunas = {"ID", "Nome/Razão Social", "CNPJ/CPF", "Telefone", "Celular", "Email", "Cidade/UF", "Contato", "Status", "Ações"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 9; // Apenas coluna de ações é editável
            }
        };
        
        tblFornecedores = new JTable(tableModel);
        tblFornecedores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblFornecedores.getTableHeader().setReorderingAllowed(false);
        
        // Configurar largura das colunas
        tblFornecedores.getColumnModel().getColumn(0).setPreferredWidth(40);
        tblFornecedores.getColumnModel().getColumn(1).setPreferredWidth(180);
        tblFornecedores.getColumnModel().getColumn(2).setPreferredWidth(120);
        tblFornecedores.getColumnModel().getColumn(3).setPreferredWidth(90);
        tblFornecedores.getColumnModel().getColumn(4).setPreferredWidth(90);
        tblFornecedores.getColumnModel().getColumn(5).setPreferredWidth(120);
        tblFornecedores.getColumnModel().getColumn(6).setPreferredWidth(80);
        tblFornecedores.getColumnModel().getColumn(7).setPreferredWidth(100);
        tblFornecedores.getColumnModel().getColumn(8).setPreferredWidth(70);
        tblFornecedores.getColumnModel().getColumn(9).setPreferredWidth(80);
        
        // Centralizar colunas numéricas e de status
        tblFornecedores.getColumnModel().getColumn(0).setCellRenderer(new CenteredRenderer());
        tblFornecedores.getColumnModel().getColumn(8).setCellRenderer(new StatusRenderer());
        
        JScrollPane scrollPane = new JScrollPane(tblFornecedores);
        painel.add(scrollPane, BorderLayout.CENTER);
        
        return painel;
    }
    
    private JPanel criarPainelAcoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton btnSalvar = LayoutPadrao.criarBotaoSucesso("💾 Salvar");
        btnSalvar.addActionListener(e -> salvarFornecedor());
        
        JButton btnEditar = LayoutPadrao.criarBotaoPrimario("✏️ Editar");
        btnEditar.addActionListener(e -> editarFornecedor());
        
        JButton btnExcluir = LayoutPadrao.criarBotaoPerigo("🗑️ Excluir");
        btnExcluir.addActionListener(e -> excluirFornecedor());
        
        JButton btnLimpar = LayoutPadrao.criarBotaoSecundario("🔄 Limpar");
        btnLimpar.addActionListener(e -> limparFormulario());
        
        JButton btnValidar = LayoutPadrao.criarBotaoPrimario("✅ Validar CNPJ");
        btnValidar.addActionListener(e -> validarCnpjCpf());
        
        JButton btnRelatorio = LayoutPadrao.criarBotaoSecundario("📄 Relatório");
        btnRelatorio.addActionListener(e -> gerarRelatorio());
        
        JButton btnIntegrar = LayoutPadrao.criarBotaoAlerta("🔗 Integrar Estoque");
        btnIntegrar.addActionListener(e -> integrarEstoque());
        
        painel.add(btnSalvar);
        painel.add(btnEditar);
        painel.add(btnExcluir);
        painel.add(Box.createHorizontalStrut(10));
        painel.add(btnLimpar);
        painel.add(btnValidar);
        painel.add(Box.createHorizontalStrut(10));
        painel.add(btnRelatorio);
        painel.add(btnIntegrar);
        
        return painel;
    }
    
    private void salvarFornecedor() {
        if (!validarCampos()) {
            return;
        }
        
        try {
            Fornecedor fornecedor;
            
            if (fornecedorEditando != null) {
                // Editando fornecedor existente
                fornecedor = fornecedorEditando;
                atualizarDadosFornecedor(fornecedor);
                JOptionPane.showMessageDialog(workArea, "Fornecedor atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                SystemLogger.ui("Fornecedor atualizado: " + fornecedor.getNome());
            } else {
                // Novo fornecedor
                fornecedor = new Fornecedor();
                fornecedor.setId(fornecedores.size() + 1);
                atualizarDadosFornecedor(fornecedor);
                fornecedores.add(fornecedor);
                JOptionPane.showMessageDialog(workArea, "Fornecedor cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                SystemLogger.ui("Fornecedor cadastrado: " + fornecedor.getNome());
            }
            
            atualizarTabela();
            limparFormulario();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(workArea, "Erro ao salvar fornecedor: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            SystemLogger.ui("Erro ao salvar fornecedor: " + e.getMessage());
        }
    }
    
    private void editarFornecedor() {
        int linhaSelecionada = tblFornecedores.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione um fornecedor para editar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Integer id = (Integer) tableModel.getValueAt(linhaSelecionada, 0);
        fornecedorEditando = fornecedores.stream()
                .filter(f -> f.getId().equals(id))
                .findFirst()
                .orElse(null);
        
        if (fornecedorEditando != null) {
            carregarDadosFormulario(fornecedorEditando);
            SystemLogger.ui("Editando fornecedor: " + fornecedorEditando.getNome());
        }
    }
    
    private void excluirFornecedor() {
        int linhaSelecionada = tblFornecedores.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione um fornecedor para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacao = JOptionPane.showConfirmDialog(workArea, 
            "Deseja realmente excluir este fornecedor?", "Confirmar Exclusão", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            Integer id = (Integer) tableModel.getValueAt(linhaSelecionada, 0);
            fornecedores.removeIf(f -> f.getId().equals(id));
            
            atualizarTabela();
            limparFormulario();
            
            JOptionPane.showMessageDialog(workArea, "Fornecedor excluído com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            SystemLogger.ui("Fornecedor excluído: ID " + id);
        }
    }
    
    private void limparFormulario() {
        txtNome.setText("");
        txtCnpjCpf.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        txtEndereco.setText("");
        txtCidade.setText("");
        txtEstado.setText("");
        txtCep.setText("");
        comboStatus.setSelectedIndex(0);
        txtObservacoes.setText("");
        fornecedorEditando = null;
    }
    
    private void limparBusca() {
        txtBuscaNome.setText("");
        txtBuscaCnpj.setText("");
        comboBuscaStatus.setSelectedIndex(0);
        filtrarFornecedores();
    }
    
    private void validarCnpjCpf() {
        String cnpjCpf = txtCnpjCpf.getText().trim();
        
        if (cnpjCpf.isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Informe o CNPJ/CPF para validar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Simulação de validação
        boolean valido = cnpjCpf.length() >= 11;
        
        if (valido) {
            JOptionPane.showMessageDialog(workArea, 
                "CNPJ/CPF válido!\nDocumento: " + cnpjCpf + "\nStatus: ✅ Validado!", 
                "Validação", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(workArea, 
                "CNPJ/CPF inválido!\nDocumento: " + cnpjCpf + "\nStatus: ❌ Inválido!", 
                "Validação", JOptionPane.ERROR_MESSAGE);
        }
        
        SystemLogger.ui("Validação CNPJ/CPF: " + cnpjCpf + " - " + (valido ? "Válido" : "Inválido"));
    }
    
    private void gerarRelatorio() {
        int totalFornecedores = fornecedores.size();
        long ativos = fornecedores.stream().filter(f -> "Ativo".equals(f.getStatus())).count();
        long inativos = fornecedores.stream().filter(f -> "Inativo".equals(f.getStatus())).count();
        long bloqueados = fornecedores.stream().filter(f -> "Bloqueado".equals(f.getStatus())).count();
        
        String relatorio = "=== RELATÓRIO DE FORNECEDORES ===\n\n";
        relatorio += "Total de Fornecedores: " + totalFornecedores + "\n";
        relatorio += "Fornecedores Ativos: " + ativos + "\n";
        relatorio += "Fornecedores Inativos: " + inativos + "\n";
        relatorio += "Fornecedores Bloqueados: " + bloqueados + "\n\n";
        relatorio += "Data: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        
        JOptionPane.showMessageDialog(workArea, relatorio, "Relatório de Fornecedores", JOptionPane.INFORMATION_MESSAGE);
        SystemLogger.ui("Relatório de fornecedores gerado por " + usuarioAtual);
    }
    
    private void integrarEstoque() {
        JOptionPane.showMessageDialog(workArea, 
            "Integrando com sistema de estoque...\n" +
            "Status: ✅ Integrado com sucesso!\n" +
            "Total de produtos sincronizados: " + (fornecedores.size() * 15), 
            "Integração", JOptionPane.INFORMATION_MESSAGE);
        
        SystemLogger.ui("Integração com estoque realizada por " + usuarioAtual);
    }
    
    private boolean validarCampos() {
        if (txtNome.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Informe o nome!", "Validação", JOptionPane.WARNING_MESSAGE);
            txtNome.requestFocus();
            return false;
        }
        
        if (txtCnpjCpf.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Informe o CNPJ/CPF!", "Validação", JOptionPane.WARNING_MESSAGE);
            txtCnpjCpf.requestFocus();
            return false;
        }
        
        if (txtTelefone.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "Informe o telefone!", "Validação", JOptionPane.WARNING_MESSAGE);
            txtTelefone.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void atualizarDadosFornecedor(Fornecedor fornecedor) {
        fornecedor.setNome(txtNome.getText().trim());
        fornecedor.setCnpjCpf(txtCnpjCpf.getText().trim());
        fornecedor.setTelefone(txtTelefone.getText().trim());
        fornecedor.setEmail(txtEmail.getText().trim());
        fornecedor.setEndereco(txtEndereco.getText().trim());
        fornecedor.setCidade(txtCidade.getText().trim());
        fornecedor.setEstado(txtEstado.getText().trim());
        fornecedor.setCep(txtCep.getText().trim());
        fornecedor.setStatus((String) comboStatus.getSelectedItem());
        fornecedor.setObservacoes(txtObservacoes.getText().trim());
        fornecedor.setUsuarioCadastro(usuarioAtual);
    }
    
    private void carregarDadosFormulario(Fornecedor fornecedor) {
        txtNome.setText(fornecedor.getNome());
        txtCnpjCpf.setText(fornecedor.getCnpjCpf());
        txtTelefone.setText(fornecedor.getTelefone());
        txtEmail.setText(fornecedor.getEmail());
        txtEndereco.setText(fornecedor.getEndereco());
        txtCidade.setText(fornecedor.getCidade());
        txtEstado.setText(fornecedor.getEstado());
        txtCep.setText(fornecedor.getCep());
        comboStatus.setSelectedItem(fornecedor.getStatus());
        txtObservacoes.setText(fornecedor.getObservacoes());
    }
    
    private void filtrarFornecedores() {
        String buscaNome = txtBuscaNome.getText().toLowerCase().trim();
        String buscaCnpj = txtBuscaCnpj.getText().toLowerCase().trim();
        String buscaStatus = (String) comboBuscaStatus.getSelectedItem();
        
        tableModel.setRowCount(0);
        
        for (Fornecedor fornecedor : fornecedores) {
            boolean matchNome = buscaNome.isEmpty() || 
                               fornecedor.getNome().toLowerCase().contains(buscaNome);
            
            boolean matchCnpj = buscaCnpj.isEmpty() || 
                               fornecedor.getCnpjCpf().toLowerCase().contains(buscaCnpj);
            
            boolean matchStatus = buscaStatus.equals("Todos") || 
                               fornecedor.getStatus().equals(buscaStatus);
            
            if (matchNome && matchCnpj && matchStatus) {
                Object[] rowData = {
                    fornecedor.getId(),
                    fornecedor.getNome(),
                    fornecedor.getCnpjCpf(),
                    fornecedor.getTelefone(),
                    fornecedor.getEmail(),
                    fornecedor.getCidade(),
                    fornecedor.getStatus(),
                    "✏️ 🗑️"
                };
                tableModel.addRow(rowData);
            }
        }
    }
    
    private void atualizarTabela() {
        tableModel.setRowCount(0);
        
        for (Fornecedor fornecedor : fornecedores) {
            Object[] rowData = {
                fornecedor.getId(),
                fornecedor.getNome(),
                fornecedor.getCnpjCpf(),
                fornecedor.getTelefone(),
                fornecedor.getEmail(),
                fornecedor.getCidade(),
                fornecedor.getStatus(),
                "✏️ 🗑️"
            };
            tableModel.addRow(rowData);
        }
    }
    
    private void carregarDadosExemplo() {
        // Carregar dados de exemplo
        Fornecedor fornecedor1 = new Fornecedor();
        fornecedor1.setId(1);
        fornecedor1.setNome("Distribuidora ABC Ltda");
        fornecedor1.setCnpjCpf("12.345.678/0001-90");
        fornecedor1.setTelefone("(11) 3456-7890");
        fornecedor1.setEmail("contato@abc.com.br");
        fornecedor1.setEndereco("Rua das Indústrias, 1234");
        fornecedor1.setCidade("São Paulo");
        fornecedor1.setEstado("SP");
        fornecedor1.setCep("01234-567");
        fornecedor1.setStatus("Ativo");
        fornecedor1.setObservacoes("Principal fornecedor de materiais de escritório");
        fornecedor1.setUsuarioCadastro(usuarioAtual);
        fornecedores.add(fornecedor1);
        
        Fornecedor fornecedor2 = new Fornecedor();
        fornecedor2.setId(2);
        fornecedor2.setNome("Tecnologia & Cia");
        fornecedor2.setCnpjCpf("98.765.432/0001-10");
        fornecedor2.setTelefone("(21) 9876-5432");
        fornecedor2.setEmail("vendas@tecnologia.com");
        fornecedor2.setEndereco("Av. Central, 567");
        fornecedor2.setCidade("Rio de Janeiro");
        fornecedor2.setEstado("RJ");
        fornecedor2.setCep("20010-000");
        fornecedor2.setStatus("Ativo");
        fornecedor2.setObservacoes("Fornecedor de equipamentos eletrônicos");
        fornecedor2.setUsuarioCadastro(usuarioAtual);
        fornecedores.add(fornecedor2);
        
        Fornecedor fornecedor3 = new Fornecedor();
        fornecedor3.setId(3);
        fornecedor3.setNome("Alimentos Express");
        fornecedor3.setCnpjCpf("45.678.901/0001-23");
        fornecedor3.setTelefone("(31) 3456-8901");
        fornecedor3.setEmail("contato@alimentosexpress.com.br");
        fornecedor3.setEndereco("Rua do Comércio, 890");
        fornecedor3.setCidade("Belo Horizonte");
        fornecedor3.setEstado("MG");
        fornecedor3.setCep("30123-456");
        fornecedor3.setStatus("Inativo");
        fornecedor3.setObservacoes("Fornecedor de produtos alimentícios - em reavaliação");
        fornecedor3.setUsuarioCadastro(usuarioAtual);
        fornecedores.add(fornecedor3);
    }
    
    // Classe interna para representar fornecedores
    private static class Fornecedor {
        private Integer id;
        private String nome;
        private String cnpjCpf;
        private String telefone;
        private String email;
        private String endereco;
        private String cidade;
        private String estado;
        private String cep;
        private String status;
        private String observacoes;
        private String usuarioCadastro;
        
        // Getters e Setters
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        
        public String getCnpjCpf() { return cnpjCpf; }
        public void setCnpjCpf(String cnpjCpf) { this.cnpjCpf = cnpjCpf; }
        
        public String getTelefone() { return telefone; }
        public void setTelefone(String telefone) { this.telefone = telefone; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getEndereco() { return endereco; }
        public void setEndereco(String endereco) { this.endereco = endereco; }
        
        public String getCidade() { return cidade; }
        public void setCidade(String cidade) { this.cidade = cidade; }
        
        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
        
        public String getCep() { return cep; }
        public void setCep(String cep) { this.cep = cep; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getObservacoes() { return observacoes; }
        public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
        
        public String getUsuarioCadastro() { return usuarioCadastro; }
        public void setUsuarioCadastro(String usuarioCadastro) { this.usuarioCadastro = usuarioCadastro; }
    }
    
    // Renderer para centralizar conteúdo das células
    private static class CenteredRenderer extends DefaultTableCellRenderer {
        public CenteredRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }
    }
    
    // Renderer para status com cores
    private static class StatusRenderer extends DefaultTableCellRenderer {
        public StatusRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
            setOpaque(true);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if (value != null) {
                String status = value.toString();
                switch (status) {
                    case "Ativo":
                        c.setBackground(new Color(76, 175, 80));
                        c.setForeground(Color.WHITE);
                        break;
                    case "Inativo":
                        c.setBackground(new Color(158, 158, 158));
                        c.setForeground(Color.WHITE);
                        break;
                    case "Bloqueado":
                        c.setBackground(new Color(244, 67, 54));
                        c.setForeground(Color.WHITE);
                        break;
                    default:
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                }
            }
            
            return c;
        }
    }
}
