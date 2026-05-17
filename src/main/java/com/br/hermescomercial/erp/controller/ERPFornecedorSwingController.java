package com.br.hermescomercial.erp.controller;

import com.br.hermescomercial.ui.layout.LayoutPadrao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Controller para tela de gestão de fornecedores do ERP
 * Versão 2.3.0 - Arquitetura Modular - Tema Padrão Hermes
 */
public class ERPFornecedorSwingController {
    
    private JFrame frame;
    
    public JFrame getFrame() {
        return frame;
    }
    private JPanel mainPanel;
    private JTable fornecedoresTable;
    private DefaultTableModel tableModel;
    private JTextField txtNome, txtCnpj, txtEmail, txtTelefone, txtEndereco;
    private JTextField txtContato, txtProduto, txtPrazoEntrega;
    private JComboBox<String> cbStatus, cbCategoria;
        
    public ERPFornecedorSwingController() {
        inicializarUI();
    }
    
    private void inicializarUI() {
        frame = new JFrame("🏢 Gestão de Fornecedores - ERP");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        
                
        // Aplicar tema padrão
        frame.getContentPane().setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        // Criar painéis de formulário e tabela
        JPanel formularioPanel = createFormPanel();
        JPanel tabelaPanel = createTablePanel();
        
        // Usando layout padrão Header → Busca → Formulário → Tabela
        mainPanel = LayoutPadrao.criarLayoutPadraoGestao(
            false, // isPDV (false para ERP)
            "🏢 Gestão de Fornecedores - ERP",
            "Digite nome, CNPJ ou produto do fornecedor...",
            formularioPanel,
            tabelaPanel
        );
        
        frame.add(mainPanel);
        
        carregarDadosExemplo();
        frame.setVisible(true);
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Nome
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
        formPanel.add(createLabel("Nome:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
        txtNome = createModernTextField(40);
        formPanel.add(txtNome, gbc);
        
        // CNPJ
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.weightx = 0.0;
        formPanel.add(createLabel("CNPJ:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.5;
        txtCnpj = createModernTextField(20);
        formPanel.add(txtCnpj, gbc);
        
        // Categoria
        gbc.gridx = 2; gbc.weightx = 0.0;
        formPanel.add(createLabel("Categoria:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.5;
        cbCategoria = new JComboBox<>(new String[]{"Material", "Serviço", "Equipamento", "Software", "Outros"});
        cbCategoria.setFont(LayoutPadrao.FONTE_CAMPO);
        formPanel.add(cbCategoria, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
        formPanel.add(createLabel("Email:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.5;
        txtEmail = createModernTextField(25);
        formPanel.add(txtEmail, gbc);
        
        // Telefone
        gbc.gridx = 2; gbc.weightx = 0.0;
        formPanel.add(createLabel("Telefone:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.5;
        txtTelefone = createModernTextField(15);
        formPanel.add(txtTelefone, gbc);
        
        // Endereço
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.weightx = 0.0;
        formPanel.add(createLabel("Endereço:"), gbc);
        
        gbc.gridx = 2; gbc.gridwidth = 2; gbc.weightx = 1.0;
        txtEndereco = createModernTextField(40);
        formPanel.add(txtEndereco, gbc);
        
        // Contato
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1; gbc.weightx = 0.0;
        formPanel.add(createLabel("Contato:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.5;
        txtContato = createModernTextField(20);
        formPanel.add(txtContato, gbc);
        
        // Status
        gbc.gridx = 2; gbc.weightx = 0.0;
        formPanel.add(createLabel("Status:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.5;
        cbStatus = new JComboBox<>(new String[]{"Ativo", "Inativo", "Em Análise"});
        cbStatus.setFont(LayoutPadrao.FONTE_CAMPO);
        formPanel.add(cbStatus, gbc);
        
        // Produto Principal
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1; gbc.weightx = 0.0;
        formPanel.add(createLabel("Produto Principal:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.5;
        txtProduto = createModernTextField(20);
        formPanel.add(txtProduto, gbc);
        
        // Prazo Entrega
        gbc.gridx = 2; gbc.weightx = 0.0;
        formPanel.add(createLabel("Prazo Entrega:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.5;
        txtPrazoEntrega = createModernTextField(10);
        formPanel.add(txtPrazoEntrega, gbc);
        
        // Botões
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 4;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        
        JButton btnSalvar = LayoutPadrao.criarBotaoSucesso("💾 Salvar");
        btnSalvar.addActionListener(e -> salvarFornecedor());
        buttonPanel.add(btnSalvar);
        
        JButton btnEditar = LayoutPadrao.criarBotaoPrimario("✏️ Editar");
        btnEditar.addActionListener(e -> editarFornecedor());
        buttonPanel.add(btnEditar);
        
        JButton btnExcluir = LayoutPadrao.criarBotaoPerigo("🗑️ Excluir");
        btnExcluir.addActionListener(e -> excluirFornecedor());
        buttonPanel.add(btnExcluir);
        
        JButton btnCancelar = LayoutPadrao.criarBotaoSecundario("❌ Cancelar");
        btnCancelar.addActionListener(e -> limparFormulario());
        buttonPanel.add(btnCancelar);
        
        JButton btnAtualizar = LayoutPadrao.criarBotaoSecundario("🔄 Atualizar");
        btnAtualizar.addActionListener(e -> carregarDadosExemplo());
        buttonPanel.add(btnAtualizar);
        
        formPanel.add(buttonPanel, gbc);
        
        return formPanel;
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        
        // Configurar tabela
        String[] colunas = {"ID", "Nome", "CNPJ", "Categoria", "Email", "Telefone", "Status", "Produto Principal"};
        
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        fornecedoresTable = new JTable(tableModel);
        fornecedoresTable.setFont(LayoutPadrao.FONTE_TEXTO);
        fornecedoresTable.getTableHeader().setFont(LayoutPadrao.FONTE_SUBTITULO);
        fornecedoresTable.setRowHeight(25);
        fornecedoresTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Ajustar largura das colunas
        fornecedoresTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        fornecedoresTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Nome
        fornecedoresTable.getColumnModel().getColumn(2).setPreferredWidth(120); // CNPJ
        fornecedoresTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Categoria
        fornecedoresTable.getColumnModel().getColumn(4).setPreferredWidth(150); // Email
        fornecedoresTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Telefone
        fornecedoresTable.getColumnModel().getColumn(6).setPreferredWidth(80);  // Status
        fornecedoresTable.getColumnModel().getColumn(7).setPreferredWidth(120); // Produto Principal
        
        JScrollPane scrollPane = new JScrollPane(fornecedoresTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LayoutPadrao.FONTE_ROTULO);
        label.setForeground(LayoutPadrao.COR_TEXTO);
        return label;
    }
    
    private JTextField createModernTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(LayoutPadrao.FONTE_CAMPO);
        field.setForeground(LayoutPadrao.COR_TEXTO);
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LayoutPadrao.COR_BORDA, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return field;
    }
    
    private void salvarFornecedor() {
        String nome = txtNome.getText().trim();
        String cnpj = txtCnpj.getText().trim();
        String email = txtEmail.getText().trim();
        
        // Validação
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Nome é obrigatório!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (cnpj.isEmpty()) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ CNPJ é obrigatório!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            Long novoId = System.currentTimeMillis();
            Object[] novaLinha = {
                novoId, nome, cnpj, cbCategoria.getSelectedItem(), email, 
                txtTelefone.getText(), cbStatus.getSelectedItem(), txtProduto.getText()
            };
            tableModel.addRow(novaLinha);
            
            JOptionPane.showMessageDialog(frame, 
                "✅ Fornecedor salvo com sucesso!\n" +
                "Nome: " + nome + "\n" +
                "CNPJ: " + cnpj + "\n" +
                "Email: " + email, 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            limparFormulario();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, 
                "❌ Erro ao salvar fornecedor: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editarFornecedor() {
        int linhaSelecionada = fornecedoresTable.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Nenhum fornecedor selecionado!\n" +
                "Selecione um fornecedor na tabela para editar.", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Preencher formulário com dados selecionados
        txtNome.setText((String) tableModel.getValueAt(linhaSelecionada, 1));
        txtCnpj.setText((String) tableModel.getValueAt(linhaSelecionada, 2));
        cbCategoria.setSelectedItem(tableModel.getValueAt(linhaSelecionada, 3));
        txtEmail.setText((String) tableModel.getValueAt(linhaSelecionada, 4));
        txtTelefone.setText((String) tableModel.getValueAt(linhaSelecionada, 5));
        cbStatus.setSelectedItem(tableModel.getValueAt(linhaSelecionada, 6));
        txtProduto.setText((String) tableModel.getValueAt(linhaSelecionada, 7));
        
        txtNome.requestFocus();
    }
    
    private void excluirFornecedor() {
        int linhaSelecionada = fornecedoresTable.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Nenhum fornecedor selecionado!\n" +
                "Selecione um fornecedor na tabela para excluir.", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String nome = (String) tableModel.getValueAt(linhaSelecionada, 1);
        
        int confirmacao = JOptionPane.showConfirmDialog(frame, 
            "🗑️ Confirmar Exclusão\n" +
            "Deseja realmente excluir o fornecedor selecionado?\n\n" +
            "Nome: " + nome, 
            "Confirmar Exclusão", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                tableModel.removeRow(linhaSelecionada);
                
                JOptionPane.showMessageDialog(frame, 
                    "✅ Fornecedor excluído com sucesso!", 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, 
                    "❌ Erro ao excluir fornecedor: " + e.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void limparFormulario() {
        txtNome.setText("");
        txtCnpj.setText("");
        txtEmail.setText("");
        txtTelefone.setText("");
        txtEndereco.setText("");
        txtContato.setText("");
        txtProduto.setText("");
        txtPrazoEntrega.setText("");
        cbCategoria.setSelectedIndex(0);
        cbStatus.setSelectedIndex(0);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ERPFornecedorSwingController();
        });
    }
    
    private void carregarDadosExemplo() {
        // Limpar tabela
        tableModel.setRowCount(0);
        
        // Dados de exemplo
        Object[][] dados = {
            {1L, "Tech Solutions Ltda", "12.345.678/0001-90", "Software", "contato@techsolutions.com.br", "(11) 3456-7890", "Ativo", "Sistema ERP"},
            {2L, "Material Industrial SA", "98.765.432/0001-10", "Material", "vendas@materialindustrial.com", "(21) 2345-6789", "Ativo", "Aço Inoxidável"},
            {3L, "Equipamentos Digitais", "45.678.901/0001-23", "Equipamento", "orcamento@equipdigitais.com", "(31) 1234-5678", "Ativo", "Computadores"},
            {4L, "Serviços de Limpeza", "78.901.234/0001-45", "Serviço", "contato@servlimpeza.com", "(41) 9876-5432", "Em Análise", "Limpeza Predial"},
            {5L, "Software House Brasil", "34.567.890/0001-67", "Software", "comercial@softwarehouse.com", "(51) 3456-7890", "Ativo", "Desenvolvimento Customizado"}
        };
        
        for (Object[] row : dados) {
            tableModel.addRow(row);
        }
    }
}
