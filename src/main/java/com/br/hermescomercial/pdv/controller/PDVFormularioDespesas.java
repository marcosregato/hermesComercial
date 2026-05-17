package com.br.hermescomercial.pdv.controller;

import com.br.hermescomercial.erp.model.Despesa;
import com.br.hermescomercial.ui.layout.LayoutPadrao;
import com.br.hermescomercial.util.SystemLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe especializada para formulário de gestão de despesas
 * Segue o padrão Header → Busca → Formulário → Tabela
 * Versão 1.0.0 - Adaptada para PDVMenuLateralElegante
 */
public class PDVFormularioDespesas {
    
    private JPanel workArea;
    private String usuarioAtual;
    private String nomeUsuario;
    
    // Componentes da interface
    private JTextField txtDescricao;
    private JTextField txtValor;
    private JTextField txtFornecedor;
    private JTextField txtDocumento;
    private JComboBox<String> comboCategoria;
    private JComboBox<String> comboStatus;
    private JComboBox<String> comboFormaPagamento;
    private JDatePicker txtData;
    private JDatePicker txtDataPagamento;
    private JTextArea txtObservacoes;
    private JTextField txtBuscaDescricao;
    private JComboBox<String> comboBuscaCategoria;
    private JComboBox<String> comboBuscaStatus;
    private JTextField txtBuscaValorMin;
    private JTextField txtBuscaValorMax;
    private JTable tblDespesas;
    private DefaultTableModel tableModel;
    
    // Dados
    private List<Despesa> despesas;
    private Despesa despesaEditando;
    
    public PDVFormularioDespesas(JPanel workArea, String usuario, String nome) {
        this.workArea = workArea;
        this.usuarioAtual = usuario;
        this.nomeUsuario = nome;
        this.despesas = new ArrayList<>();
        carregarDadosExemplo();
    }
    
    public JPanel criarFormularioDespesas() {
        SystemLogger.ui("=== CRIANDO FORMULÁRIO DESPESAS ===");
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
        
        SystemLogger.ui("Formulário Despesas criado com sucesso");
        return painelPrincipal;
    }
    
    private JPanel criarHeader() {
        JPanel header = LayoutPadrao.criarHeaderPanel("💸 Gestão de Despesas");
        
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
        painel.setBorder(BorderFactory.createTitledBorder("🔍 Buscar Despesas"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Campo de busca por descrição
        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtBuscaDescricao = new JTextField(20);
        txtBuscaDescricao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtrarDespesas();
            }
        });
        painel.add(txtBuscaDescricao, gbc);
        
        // Categoria
        gbc.gridx = 2; gbc.weightx = 0;
        painel.add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 3;
        comboBuscaCategoria = new JComboBox<>(new String[]{"Todas", "Aluguel", "Água", "Luz", "Telefone", "Internet", "Material", "Serviços", "Impostos", "Outras"});
        comboBuscaCategoria.addActionListener(e -> filtrarDespesas());
        painel.add(comboBuscaCategoria, gbc);
        
        // Status
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        painel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboBuscaStatus = new JComboBox<>(new String[]{"Todos", "Pendente", "Paga", "Atrasada", "Cancelada"});
        comboBuscaStatus.addActionListener(e -> filtrarDespesas());
        painel.add(comboBuscaStatus, gbc);
        
        // Valor Mínimo
        gbc.gridx = 2; gbc.weightx = 0;
        painel.add(new JLabel("Valor Mín:"), gbc);
        gbc.gridx = 3;
        txtBuscaValorMin = new JTextField(10);
        txtBuscaValorMin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtrarDespesas();
            }
        });
        painel.add(txtBuscaValorMin, gbc);
        
        // Valor Máximo
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        painel.add(new JLabel("Valor Máx:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtBuscaValorMax = new JTextField(10);
        txtBuscaValorMax.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtrarDespesas();
            }
        });
        painel.add(txtBuscaValorMax, gbc);
        
        // Botão limpar
        gbc.gridx = 2; gbc.gridy = 2; gbc.gridwidth = 2;
        JButton btnLimparBusca = LayoutPadrao.criarBotaoSecundario("🔄 Limpar Filtros");
        btnLimparBusca.addActionListener(e -> limparBusca());
        painel.add(btnLimparBusca, gbc);
        
        return painel;
    }
    
    private JPanel criarPainelFormulario() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createTitledBorder("📝 Cadastro de Despesas"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Descrição
        gbc.gridx = 0; gbc.gridy = 0;
        painel.add(new JLabel("Descrição*:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtDescricao = new JTextField(30);
        painel.add(txtDescricao, gbc);
        
        // Valor
        gbc.gridx = 2; gbc.weightx = 0;
        painel.add(new JLabel("Valor*:"), gbc);
        gbc.gridx = 3;
        txtValor = new JTextField(15);
        painel.add(txtValor, gbc);
        
        // Fornecedor
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        painel.add(new JLabel("Fornecedor:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtFornecedor = new JTextField(30);
        painel.add(txtFornecedor, gbc);
        
        // Documento
        gbc.gridx = 2; gbc.weightx = 0;
        painel.add(new JLabel("Documento:"), gbc);
        gbc.gridx = 3;
        txtDocumento = new JTextField(15);
        painel.add(txtDocumento, gbc);
        
        // Categoria
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        painel.add(new JLabel("Categoria*:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboCategoria = new JComboBox<>(new String[]{"Aluguel", "Água", "Luz", "Telefone", "Internet", "Material", "Serviços", "Impostos", "Outras"});
        painel.add(comboCategoria, gbc);
        
        // Forma Pagamento
        gbc.gridx = 2; gbc.weightx = 0;
        painel.add(new JLabel("Forma Pagto*:"), gbc);
        gbc.gridx = 3;
        comboFormaPagamento = new JComboBox<>(new String[]{"Dinheiro", "Cartão", "Transferência", "Boleto", "Pix", "Cheque"});
        painel.add(comboFormaPagamento, gbc);
        
        // Status
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        painel.add(new JLabel("Status*:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        comboStatus = new JComboBox<>(new String[]{"Pendente", "Paga", "Atrasada", "Cancelada"});
        painel.add(comboStatus, gbc);
        
        // Data Vencimento
        gbc.gridx = 2; gbc.weightx = 0;
        painel.add(new JLabel("Data Venc*:"), gbc);
        gbc.gridx = 3;
        txtData = new JDatePicker();
        painel.add(txtData, gbc);
        
        // Data Pagamento
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        painel.add(new JLabel("Data Pagto:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtDataPagamento = new JDatePicker();
        painel.add(txtDataPagamento, gbc);
        
        // Observações
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 4; gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 0.1;
        painel.add(new JLabel("Observações:"), gbc);
        gbc.gridy = 6; gbc.weighty = 0.2;
        JScrollPane scrollPane = new JScrollPane(txtObservacoes = new JTextArea(3, 50));
        txtObservacoes.setLineWrap(true);
        txtObservacoes.setWrapStyleWord(true);
        painel.add(scrollPane, gbc);
        
        return painel;
    }
    
    private JPanel criarPainelTabela() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createTitledBorder("📋 Despesas Cadastradas"));
        
        // Configurar tabela
        String[] colunas = {"ID", "Descrição", "Fornecedor", "Categoria", "Valor", "Forma Pagto", "Data Venc.", "Data Pagto", "Status", "Ações"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 9; // Apenas coluna de ações é editável
            }
        };
        
        tblDespesas = new JTable(tableModel);
        tblDespesas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblDespesas.getTableHeader().setReorderingAllowed(false);
        
        // Configurar largura das colunas
        tblDespesas.getColumnModel().getColumn(0).setPreferredWidth(40);
        tblDespesas.getColumnModel().getColumn(1).setPreferredWidth(180);
        tblDespesas.getColumnModel().getColumn(2).setPreferredWidth(120);
        tblDespesas.getColumnModel().getColumn(3).setPreferredWidth(80);
        tblDespesas.getColumnModel().getColumn(4).setPreferredWidth(80);
        tblDespesas.getColumnModel().getColumn(5).setPreferredWidth(90);
        tblDespesas.getColumnModel().getColumn(6).setPreferredWidth(80);
        tblDespesas.getColumnModel().getColumn(7).setPreferredWidth(80);
        tblDespesas.getColumnModel().getColumn(8).setPreferredWidth(70);
        tblDespesas.getColumnModel().getColumn(9).setPreferredWidth(100);
        
        // Centralizar colunas numéricas e de status
        tblDespesas.getColumnModel().getColumn(0).setCellRenderer(new CenteredRenderer());
        tblDespesas.getColumnModel().getColumn(4).setCellRenderer(new CurrencyRenderer());
        tblDespesas.getColumnModel().getColumn(8).setCellRenderer(createStatusRenderer());
        
        JScrollPane scrollPane = new JScrollPane(tblDespesas);
        painel.add(scrollPane, BorderLayout.CENTER);
        
        return painel;
    }
    
    private DefaultTableCellRenderer createStatusRenderer() {
        return new DefaultTableCellRenderer() {
            {
                setHorizontalAlignment(SwingConstants.CENTER);
                setOpaque(true);
            }
            
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (value != null) {
                    String status = value.toString();
                    switch (status) {
                        case "Paga":
                            c.setBackground(new Color(76, 175, 80));
                            c.setForeground(Color.WHITE);
                            break;
                        case "Pendente":
                            c.setBackground(new Color(255, 193, 7));
                            c.setForeground(Color.BLACK);
                            break;
                        case "Atrasada":
                            c.setBackground(new Color(244, 67, 54));
                            c.setForeground(Color.WHITE);
                            break;
                        case "Cancelada":
                            c.setBackground(new Color(158, 158, 158));
                            c.setForeground(Color.WHITE);
                            break;
                        default:
                            c.setBackground(Color.WHITE);
                            c.setForeground(Color.BLACK);
                    }
                }
                
                return c;
            }
        };
    }
    
    private JPanel criarPainelAcoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painel.setBackground(Color.WHITE);
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton btnSalvar = LayoutPadrao.criarBotaoSucesso("💾 Salvar");
        btnSalvar.addActionListener(e -> salvarDespesa());
        
        JButton btnEditar = LayoutPadrao.criarBotaoPrimario("✏️ Editar");
        btnEditar.addActionListener(e -> editarDespesa());
        
        JButton btnExcluir = LayoutPadrao.criarBotaoPerigo("🗑️ Excluir");
        btnExcluir.addActionListener(e -> excluirDespesa());
        
        JButton btnLimpar = LayoutPadrao.criarBotaoSecundario("🔄 Limpar");
        btnLimpar.addActionListener(e -> limparFormulario());
        
        JButton btnRelatorio = LayoutPadrao.criarBotaoSecundario("📄 Relatório");
        btnRelatorio.addActionListener(e -> gerarRelatorio());
        
        painel.add(btnSalvar);
        painel.add(btnEditar);
        painel.add(btnExcluir);
        painel.add(Box.createHorizontalStrut(10));
        painel.add(btnLimpar);
        painel.add(btnRelatorio);
        
        return painel;
    }
    
    private void salvarDespesa() {
        if (!validarCampos()) {
            return;
        }
        
        try {
            Despesa despesa;
            
            if (despesaEditando != null) {
                // Editando despesa existente
                despesa = despesaEditando;
                atualizarDadosDespesa(despesa);
                JOptionPane.showMessageDialog(workArea, "Despesa atualizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                SystemLogger.ui("Despesa atualizada: " + despesa.getDescricao());
            } else {
                // Nova despesa
                despesa = new Despesa();
                despesa.setId((long)(despesas.size() + 1));
                atualizarDadosDespesa(despesa);
                despesas.add(despesa);
                JOptionPane.showMessageDialog(workArea, "Despesa cadastrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                SystemLogger.ui("Despesa cadastrada: " + despesa.getDescricao());
            }
            
            atualizarTabela();
            limparFormulario();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(workArea, "Erro ao salvar despesa: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            SystemLogger.ui("Erro ao salvar despesa: " + e.getMessage());
        }
    }
    
    private void editarDespesa() {
        int linhaSelecionada = tblDespesas.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione uma despesa para editar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Long id = (Long) tableModel.getValueAt(linhaSelecionada, 0);
        despesaEditando = despesas.stream()
                .filter(d -> d.getId().equals(id))
                .findFirst()
                .orElse(null);
        
        if (despesaEditando != null) {
            carregarDadosFormulario(despesaEditando);
            SystemLogger.ui("Editando despesa: " + despesaEditando.getDescricao());
        }
    }
    
    private void excluirDespesa() {
        int linhaSelecionada = tblDespesas.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(workArea, "Selecione uma despesa para excluir!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacao = JOptionPane.showConfirmDialog(workArea, 
            "Deseja realmente excluir esta despesa?", "Confirmar Exclusão", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            Long id = (Long) tableModel.getValueAt(linhaSelecionada, 0);
            despesas.removeIf(d -> d.getId().equals(id));
            
            atualizarTabela();
            limparFormulario();
            
            JOptionPane.showMessageDialog(workArea, "Despesa excluída com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            SystemLogger.ui("Despesa excluída: ID " + id);
        }
    }
    
    private void limparFormulario() {
        txtDescricao.setText("");
        txtValor.setText("");
        comboCategoria.setSelectedIndex(0);
        comboStatus.setSelectedIndex(0);
        txtData.setDate(LocalDate.now());
        txtObservacoes.setText("");
        despesaEditando = null;
    }
    
    private void limparBusca() {
        txtBuscaDescricao.setText("");
        comboBuscaCategoria.setSelectedIndex(0);
        filtrarDespesas();
    }
    
    private void gerarRelatorio() {
        double totalDespesas = despesas.stream()
                .mapToDouble(Despesa::getValor)
                .sum();
        
        String relatorio = "=== RELATÓRIO DE DESPESAS ===\n\n";
        relatorio += "Total de Despesas: " + despesas.size() + "\n";
        relatorio += "Valor Total: R$ " + String.format("%,.2f", totalDespesas) + "\n\n";
        
        JOptionPane.showMessageDialog(workArea, relatorio, "Relatório de Despesas", JOptionPane.INFORMATION_MESSAGE);
        SystemLogger.ui("Relatório de despesas gerado por " + usuarioAtual);
    }
    
    private boolean validarCampos() {
        // Validação da Descrição (Obrigatório *)
        if (txtDescricao.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "⚠️ A Descrição é obrigatória!\n\nPor favor, informe a descrição da despesa.", "Validação - Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
            txtDescricao.requestFocus();
            txtDescricao.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            return false;
        }
        
        // Validação do Valor (Obrigatório *)
        if (txtValor.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(workArea, "⚠️ O Valor é obrigatório!\n\nPor favor, informe o valor da despesa.", "Validação - Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
            txtValor.requestFocus();
            txtValor.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            return false;
        }
        
        // Validação do formato do Valor
        try {
            BigDecimal valor = new BigDecimal(txtValor.getText().replace(",", "."));
            if (valor.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(workArea, "⚠️ O Valor deve ser maior que zero!\n\nPor favor, informe um valor válido.", "Validação - Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
                txtValor.requestFocus();
                txtValor.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(workArea, "⚠️ Valor inválido!\n\nPor favor, informe um valor numérico válido (ex: 100,50).", "Validação - Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
            txtValor.requestFocus();
            txtValor.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            return false;
        }
        
        // Validação da Data (Obrigatório *)
        if (txtData.getDate() == null) {
            JOptionPane.showMessageDialog(workArea, "⚠️ A Data de Vencimento é obrigatória!\n\nPor favor, selecione a data de vencimento.", "Validação - Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
            txtData.requestFocus();
            return false;
        }
        
        // Validação da Categoria (Obrigatório *)
        if (comboCategoria.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(workArea, "⚠️ A Categoria é obrigatória!\n\nPor favor, selecione uma categoria para a despesa.", "Validação - Campo Obrigatório", JOptionPane.WARNING_MESSAGE);
            comboCategoria.requestFocus();
            return false;
        }
        
        // Resetar bordas dos campos válidos
        txtDescricao.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        txtValor.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        return true;
    }
    
    private void atualizarDadosDespesa(Despesa despesa) {
        despesa.setDescricao(txtDescricao.getText().trim());
        BigDecimal valor = new BigDecimal(txtValor.getText().replace(",", "."));
        despesa.setValor(valor.doubleValue());
        despesa.setCategoria((String) comboCategoria.getSelectedItem());
        despesa.setStatus((String) comboStatus.getSelectedItem());
        despesa.setDataVencimento(txtData.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        // despesa.setObservacoes(txtObservacoes.getText().trim()); // Método não existe no modelo
        // despesa.setUsuarioCadastro(usuarioAtual); // Método não existe no modelo
    }
    
    private void carregarDadosFormulario(Despesa despesa) {
        txtDescricao.setText(despesa.getDescricao());
        txtValor.setText(String.valueOf(despesa.getValor()));
        comboCategoria.setSelectedItem(despesa.getCategoria());
        comboStatus.setSelectedItem(despesa.getStatus());
        txtData.setDate(LocalDate.parse(despesa.getDataVencimento(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        txtObservacoes.setText("");
    }
    
    private void filtrarDespesas() {
        String buscaDescricao = txtBuscaDescricao.getText().toLowerCase().trim();
        String buscaCategoria = (String) comboBuscaCategoria.getSelectedItem();
        
        tableModel.setRowCount(0);
        
        for (Despesa despesa : despesas) {
            boolean matchDescricao = buscaDescricao.isEmpty() || 
                                despesa.getDescricao().toLowerCase().contains(buscaDescricao);
            
            boolean matchCategoria = buscaCategoria.equals("Todas") || 
                                despesa.getCategoria().equals(buscaCategoria);
            
            if (matchDescricao && matchCategoria) {
                Object[] rowData = {
                    despesa.getId(),
                    despesa.getDescricao(),
                    despesa.getCategoria(),
                    "R$ " + String.format("%,.2f", despesa.getValor()),
                    despesa.getDataVencimento(),
                    despesa.getStatus(),
                    "✏️ 🗑️"
                };
                tableModel.addRow(rowData);
            }
        }
    }
    
    private void atualizarTabela() {
        tableModel.setRowCount(0);
        
        for (Despesa despesa : despesas) {
            Object[] rowData = {
                despesa.getId(),
                despesa.getDescricao(),
                despesa.getCategoria(),
                "R$ " + String.format("%,.2f", despesa.getValor()),
                despesa.getDataVencimento(),
                despesa.getStatus(),
                "✏️ 🗑️"
            };
            tableModel.addRow(rowData);
        }
    }
    
    private void carregarDadosExemplo() {
        // Carregar dados de exemplo usando o construtor correto do modelo Despesa
        Despesa despesa1 = new Despesa();
        despesa1.setId(1L);
        despesa1.setDescricao("Aluguel do escritório");
        despesa1.setValor(2500.00);
        despesa1.setTipo("FIXA");
        despesa1.setDataVencimento(LocalDate.now().plusDays(5).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        despesa1.setStatus("Pendente");
        despesa1.setCategoria("Aluguel");
        despesas.add(despesa1);
        
        Despesa despesa2 = new Despesa();
        despesa2.setId(2L);
        despesa2.setDescricao("Conta de luz");
        despesa2.setValor(350.00);
        despesa2.setTipo("VARIÁVEL");
        despesa2.setDataVencimento(LocalDate.now().plusDays(10).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        despesa2.setStatus("Paga");
        despesa2.setCategoria("Luz");
        despesas.add(despesa2);
        
        Despesa despesa3 = new Despesa();
        despesa3.setId(3L);
        despesa3.setDescricao("Internet");
        despesa3.setValor(150.00);
        despesa3.setTipo("VARIÁVEL");
        despesa3.setDataVencimento(LocalDate.now().plusDays(15).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        despesa3.setStatus("Pendente");
        despesa3.setCategoria("Internet");
        despesas.add(despesa3);
    }
    
    // Componente simplificado para seleção de data
    private static class JDatePicker extends JPanel {
        private JTextField txtData;
        private JButton btnCalendario;
        
        public JDatePicker() {
            setLayout(new BorderLayout());
            txtData = new JTextField(10);
            txtData.setEditable(false);
            btnCalendario = new JButton("📅");
            btnCalendario.setPreferredSize(new Dimension(30, 25));
            
            add(txtData, BorderLayout.CENTER);
            add(btnCalendario, BorderLayout.EAST);
            
            setDate(LocalDate.now());
            
            btnCalendario.addActionListener(e -> {
                // Implementar seleção de data (simplificado)
                setDate(LocalDate.now());
            });
        }
        
        public void setDate(LocalDate date) {
            txtData.setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        
        @SuppressWarnings("unused")
        public String getText() {
            return txtData.getText();
        }
        
        public LocalDate getDate() {
            try {
                return LocalDate.parse(txtData.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (Exception e) {
                return LocalDate.now();
            }
        }
    }
    
    // Renderer para centralizar conteúdo
    // Renderer para centralizar conteúdo das células
    private static class CenteredRenderer extends DefaultTableCellRenderer {
        public CenteredRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }
    }
    
    // Renderer para valores monetários
    private static class CurrencyRenderer extends DefaultTableCellRenderer {
        public CurrencyRenderer() {
            setHorizontalAlignment(SwingConstants.RIGHT);
        }
    }
}
