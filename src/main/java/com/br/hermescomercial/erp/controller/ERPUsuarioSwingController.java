package com.br.hermescomercial.erp.controller;

import com.br.hermescomercial.theme.HermesTheme;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;


/**
 * Controller para tela de gestão de usuários e permissões ERP
 * Versão 2.3.0 - Arquitetura Modular - Tema Padrão Hermes
 */
public class ERPUsuarioSwingController {
    
    private JFrame frame;
    private JPanel mainPanel;
    private JTextField txtNome, txtEmail, txtUsuario, txtBusca;
    private JPasswordField txtSenha, txtConfirmarSenha;
    private JComboBox<String> cbNivelAcesso, cbStatus;
    private JCheckBox ckAtivo, ckAlterarSenha;
    private JTable usuariosTable;
    private DefaultTableModel tableModel;
    private JButton btnNovo, btnSalvar, btnCancelar, btnEditar, btnExcluir, btnBuscar, btnLimpar, btnAtualizar;
    private Long usuarioEditId = null;
    private boolean editMode = false;
    
    public ERPUsuarioSwingController() {
        inicializarUI();
    }
    
    private void inicializarUI() {
        frame = new JFrame("👥 Gestão de Usuários e Permissões");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        
        // Aplicar tema padrão
        frame.getContentPane().setBackground(HermesTheme.BACKGROUND_PRIMARY);
        
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(HermesTheme.BACKGROUND_PRIMARY);
        
        frame.add(mainPanel);
        frame.add(createHeaderPanel(), BorderLayout.NORTH);
        frame.add(createMainContentPanel(), BorderLayout.CENTER);
        
        carregarDadosExemplo();
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        JLabel titleLabel = new JLabel("👥 Gestão de Usuários e Permissões");
        titleLabel.setFont(HermesTheme.FONT_HEADER);
        titleLabel.setForeground(HermesTheme.TEXT_PRIMARY);
        
        JLabel subtitleLabel = new JLabel("Gerenciamento de usuários, senhas e níveis de acesso ao sistema");
        subtitleLabel.setFont(HermesTheme.FONT_DEFAULT);
        subtitleLabel.setForeground(HermesTheme.TEXT_SECONDARY);
        
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JPanel createMainContentPanel() {
        JPanel mainContentPanel = new JPanel(new BorderLayout(10, 10));
        mainContentPanel.setOpaque(false);
        
        // Painel esquerdo - Formulário
        JPanel formPanel = createFormPanel();
        formPanel.setPreferredSize(new Dimension(400, 0));
        
        // Painel direito - Tabela
        JPanel tablePanel = createTablePanel();
        
        mainContentPanel.add(formPanel, BorderLayout.WEST);
        mainContentPanel.add(tablePanel, BorderLayout.CENTER);
        
        return mainContentPanel;
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
        formPanel.setOpaque(false);
        
        // Título do formulário
        JLabel formTitle = new JLabel("📝 Cadastro de Usuário");
        formTitle.setFont(HermesTheme.FONT_HEADER);
        formTitle.setForeground(HermesTheme.TEXT_PRIMARY);
        
        // Campos do formulário
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "🔐 Dados do Usuário", 
            TitledBorder.LEFT, 
            TitledBorder.TOP, 
            HermesTheme.FONT_HEADER, 
            HermesTheme.TEXT_PRIMARY));
        fieldsPanel.setBackground(HermesTheme.BACKGROUND_SECONDARY);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Nome
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
        fieldsPanel.add(createLabel("Nome Completo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNome = createTextField();
        fieldsPanel.add(txtNome, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        fieldsPanel.add(createLabel("E-mail:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtEmail = createEmailField();
        fieldsPanel.add(txtEmail, gbc);
        
        // Telefone
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
        fieldsPanel.add(createLabel("Telefone:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        JTextField txtTelefone = createTelefoneField();
        fieldsPanel.add(txtTelefone, gbc);
        
        // Usuário
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0;
        fieldsPanel.add(createLabel("Usuário:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtUsuario = createTextField();
        fieldsPanel.add(txtUsuario, gbc);
        
        // Senha
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.0;
        fieldsPanel.add(createLabel("Senha:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtSenha = new JPasswordField();
        txtSenha.setFont(HermesTheme.FONT_DEFAULT);
        txtSenha.setForeground(HermesTheme.TEXT_PRIMARY);
        txtSenha.setBackground(Color.WHITE);
        txtSenha.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(HermesTheme.BORDER_LIGHT, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        fieldsPanel.add(txtSenha, gbc);
        
        // Confirmar Senha
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0.0;
        fieldsPanel.add(createLabel("Confirmar Senha:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtConfirmarSenha = new JPasswordField();
        txtConfirmarSenha.setFont(HermesTheme.FONT_DEFAULT);
        txtConfirmarSenha.setForeground(HermesTheme.TEXT_PRIMARY);
        txtConfirmarSenha.setBackground(Color.WHITE);
        txtConfirmarSenha.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(HermesTheme.BORDER_LIGHT, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        fieldsPanel.add(txtConfirmarSenha, gbc);
        
        // Nível de Acesso
        gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0.0;
        fieldsPanel.add(createLabel("Nível de Acesso:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        cbNivelAcesso = new JComboBox<>(new String[]{"Administrador", "Gerente", "Operador", "Visualizador"});
        cbNivelAcesso.setFont(HermesTheme.FONT_DEFAULT);
        cbNivelAcesso.setPreferredSize(new Dimension(150, 30));
        fieldsPanel.add(cbNivelAcesso, gbc);
        
        // Status
        gbc.gridx = 0; gbc.gridy = 7; gbc.weightx = 0.0;
        fieldsPanel.add(createLabel("Status:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        cbStatus = new JComboBox<>(new String[]{"Ativo", "Inativo", "Bloqueado"});
        cbStatus.setFont(HermesTheme.FONT_DEFAULT);
        cbStatus.setPreferredSize(new Dimension(150, 30));
        fieldsPanel.add(cbStatus, gbc);
        
        // Checkboxes
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2; gbc.weightx = 1.0;
        JPanel checkboxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        checkboxPanel.setOpaque(false);
        
        ckAtivo = new JCheckBox("Usuário Ativo");
        ckAtivo.setFont(HermesTheme.FONT_DEFAULT);
        ckAtivo.setForeground(HermesTheme.TEXT_PRIMARY);
        ckAtivo.setSelected(true);
        
        ckAlterarSenha = new JCheckBox("Exigir alteração de senha no próximo login");
        ckAlterarSenha.setFont(HermesTheme.FONT_DEFAULT);
        ckAlterarSenha.setForeground(HermesTheme.TEXT_PRIMARY);
        
        checkboxPanel.add(ckAtivo);
        checkboxPanel.add(ckAlterarSenha);
        fieldsPanel.add(checkboxPanel, gbc);
        
        // Botões do formulário
        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel formButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        formButtonPanel.setOpaque(false);
        
        btnNovo = createStyledButton("➕ Novo", HermesTheme.SUCCESS_COLOR, e -> novoUsuario());
        btnSalvar = createStyledButton("💾 Salvar", HermesTheme.PRIMARY_COLOR, e -> salvarUsuario());
        btnCancelar = createStyledButton("❌ Cancelar", HermesTheme.DANGER_COLOR, e -> limparFormulario());
        btnEditar = createStyledButton("✏️ Editar", HermesTheme.WARNING_COLOR, e -> editarUsuario());
        btnExcluir = createStyledButton("🗑️ Excluir", HermesTheme.DANGER_COLOR, e -> excluirUsuario());
        
        formButtonPanel.add(btnNovo);
        formButtonPanel.add(btnSalvar);
        formButtonPanel.add(btnCancelar);
        formButtonPanel.add(btnEditar);
        formButtonPanel.add(btnExcluir);
        
        fieldsPanel.add(formButtonPanel, gbc);
        
        // Montar painel do formulário
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(formTitle, BorderLayout.NORTH);
        topPanel.add(fieldsPanel, BorderLayout.CENTER);
        
        formPanel.add(topPanel, BorderLayout.CENTER);
        
        return formPanel;
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Painel de busca e ações
        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));
        searchPanel.setOpaque(false);
        
        // Campo de busca
        JPanel searchFieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchFieldPanel.setOpaque(false);
        searchFieldPanel.add(createLabel("🔍 Buscar:"));
        txtBusca = createTextField();
        txtBusca.setPreferredSize(new Dimension(200, 30));
        searchFieldPanel.add(txtBusca);
        
        btnBuscar = createStyledButton("🔍 Buscar", HermesTheme.PRIMARY_COLOR, e -> buscarUsuarios());
        btnLimpar = createStyledButton("🗑️ Limpar", HermesTheme.WARNING_COLOR, e -> limparBusca());
        btnAtualizar = createStyledButton("🔄 Atualizar", HermesTheme.SECONDARY_COLOR, e -> atualizarTabela());
        
        searchFieldPanel.add(btnBuscar);
        searchFieldPanel.add(btnLimpar);
        searchFieldPanel.add(btnAtualizar);
        
        searchPanel.add(searchFieldPanel, BorderLayout.NORTH);
        
        // Título da tabela
        JLabel titleLabel = new JLabel("👥 Usuários Cadastrados");
        titleLabel.setFont(HermesTheme.FONT_HEADER);
        titleLabel.setForeground(HermesTheme.TEXT_PRIMARY);
        
        // Criar tabela
        String[] colunas = {"ID", "Nome", "Usuário", "E-mail", "Nível", "Status", "Último Acesso"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0: return Long.class;
                    default: return String.class;
                }
            }
            
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return false; // Tabela não editável diretamente
            }
        };
        
        usuariosTable = new JTable(tableModel);
        usuariosTable.setFont(HermesTheme.FONT_DEFAULT);
        usuariosTable.setRowHeight(25);
        usuariosTable.getTableHeader().setFont(HermesTheme.FONT_DEFAULT);
        usuariosTable.getTableHeader().setBackground(HermesTheme.PRIMARY_COLOR);
        usuariosTable.getTableHeader().setForeground(Color.WHITE);
        
        // Evento de duplo clique para editar
        usuariosTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editarUsuario();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(usuariosTable);
        scrollPane.setPreferredSize(new Dimension(700, 500));
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(HermesTheme.BORDER_LIGHT, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(titleLabel, BorderLayout.WEST);
        
        tablePanel.add(searchPanel, BorderLayout.NORTH);
        tablePanel.add(topPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(HermesTheme.FONT_DEFAULT);
        label.setForeground(HermesTheme.TEXT_PRIMARY);
        return label;
    }
    
    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setFont(HermesTheme.FONT_DEFAULT);
        textField.setForeground(HermesTheme.TEXT_PRIMARY);
        textField.setBackground(Color.WHITE);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(HermesTheme.BORDER_LIGHT, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return textField;
    }
    
    private JTextField createTelefoneField() {
        JTextField textField = new JTextField();
        textField.setFont(HermesTheme.FONT_DEFAULT);
        textField.setForeground(HermesTheme.TEXT_PRIMARY);
        textField.setBackground(Color.WHITE);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(HermesTheme.BORDER_LIGHT, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Limitar a 13 caracteres (formato: (XX) XXXXX-XXXX)
        textField.setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if (str == null) return;
                
                if ((getLength() + str.length()) <= 13) {
                    // Verificar se contém apenas números, parênteses, espaço e traço
                    String validChars = "0123456789() -";
                    for (char c : str.toCharArray()) {
                        if (validChars.indexOf(c) == -1) {
                            return; // Ignora caracteres inválidos
                        }
                    }
                    super.insertString(offs, str, a);
                }
            }
        });
        
        // Adicionar tooltip com formato esperado
        textField.setToolTipText("Formato: (XX) XXXXX-XXXX - Máximo 13 caracteres");
        
        return textField;
    }
    
    private JTextField createEmailField() {
        JTextField textField = new JTextField();
        textField.setFont(HermesTheme.FONT_DEFAULT);
        textField.setForeground(HermesTheme.TEXT_PRIMARY);
        textField.setBackground(Color.WHITE);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(HermesTheme.BORDER_LIGHT, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Adicionar tooltip com formato esperado
        textField.setToolTipText("Formato: usuario@dominio.com");
        
        return textField;
    }
    
    private JButton createStyledButton(String text, Color bgColor, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(HermesTheme.FONT_BUTTON);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 35));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efeito hover elegante (padrão das telas antigas)
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        // Adicionar o action listener
        if (actionListener != null) {
            button.addActionListener(actionListener);
        }
        
        return button;
    }
    
    private void carregarDadosExemplo() {
        // Limpar tabela
        tableModel.setRowCount(0);
        
        // Dados de exemplo
        Object[][] dados = {
            {1L, "João Silva", "joao.silva", "joao@hermes.com", "Administrador", "Ativo", "28/04/2026 14:30"},
            {2L, "Maria Santos", "maria.santos", "maria@hermes.com", "Gerente", "Ativo", "28/04/2026 13:15"},
            {3L, "Pedro Oliveira", "pedro.oliveira", "pedro@hermes.com", "Operador", "Ativo", "27/04/2026 16:45"},
            {4L, "Ana Costa", "ana.costa", "ana@hermes.com", "Visualizador", "Inativo", "25/04/2026 09:20"},
            {5L, "Carlos Ferreira", "carlos.ferreira", "carlos@hermes.com", "Operador", "Bloqueado", "20/04/2026 11:30"}
        };
        
        // Adicionar dados na tabela
        for (Object[] linha : dados) {
            tableModel.addRow(linha);
        }
    }
    
    private void novoUsuario() {
        limparFormulario();
        editMode = false;
        usuarioEditId = null;
        txtNome.requestFocus();
    }
    
    private void salvarUsuario() {
        String nome = txtNome.getText().trim();
        String email = txtEmail.getText().trim();
        String usuario = txtUsuario.getText().trim();
        String senha = new String(txtSenha.getPassword());
        String confirmarSenha = new String(txtConfirmarSenha.getPassword());
        String nivelAcesso = (String) cbNivelAcesso.getSelectedItem();
        String status = (String) cbStatus.getSelectedItem();
        boolean ativo = ckAtivo.isSelected();
        boolean alterarSenha = ckAlterarSenha.isSelected();
        
        // Validações
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Nome é obrigatório!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ E-mail é obrigatório!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (usuario.isEmpty()) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Usuário é obrigatório!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!editMode && senha.isEmpty()) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Senha é obrigatória para novos usuários!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!senha.isEmpty() && !senha.equals(confirmarSenha)) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Senhas não conferem!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Simular salvamento
        String mensagem = editMode ? 
            "✅ Usuário atualizado com sucesso! ID: " + usuarioEditId : 
            "✅ Usuário criado com sucesso!";
            
        JOptionPane.showMessageDialog(frame, 
                mensagem + "\n\n" +
                "Nome: " + nome + "\n" +
                "E-mail: " + email + "\n" +
                "Usuário: " + usuario + "\n" +
                "Nível: " + nivelAcesso + "\n" +
                "Status: " + status + "\n" +
                "Ativo: " + (ativo ? "Sim" : "Não") + "\n" +
                "Exigir alteração de senha: " + (alterarSenha ? "Sim" : "Não"), 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        
        // Adicionar/atualizar na tabela
        if (!editMode) {
            long novoId = tableModel.getRowCount() + 1L;
            tableModel.addRow(new Object[]{
                novoId, nome, usuario, email, nivelAcesso, status, "28/04/2026 " + java.time.LocalTime.now().toString().substring(0, 5)
            });
        } else {
            // Atualizar usuário existente (simulação)
            System.out.println("Atualizando usuário ID: " + usuarioEditId);
        }
        
        limparFormulario();
    }
    
    private void editarUsuario() {
        int linhaSelecionada = usuariosTable.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Nenhum usuário selecionado!\n" +
                "Selecione um usuário na tabela para editar.", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Carregar dados do usuário selecionado
        usuarioEditId = (Long) tableModel.getValueAt(linhaSelecionada, 0);
        txtNome.setText((String) tableModel.getValueAt(linhaSelecionada, 1));
        txtUsuario.setText((String) tableModel.getValueAt(linhaSelecionada, 2));
        txtEmail.setText((String) tableModel.getValueAt(linhaSelecionada, 3));
        cbNivelAcesso.setSelectedItem((String) tableModel.getValueAt(linhaSelecionada, 4));
        cbStatus.setSelectedItem((String) tableModel.getValueAt(linhaSelecionada, 5));
        
        String status = (String) tableModel.getValueAt(linhaSelecionada, 5);
        ckAtivo.setSelected("Ativo".equals(status));
        
        editMode = true;
        txtNome.requestFocus();
    }
    
    private void excluirUsuario() {
        int linhaSelecionada = usuariosTable.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Nenhum usuário selecionado!\n" +
                "Selecione um usuário na tabela para excluir.", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String nomeUsuario = (String) tableModel.getValueAt(linhaSelecionada, 1);
        
        int opcao = JOptionPane.showConfirmDialog(frame, 
                "🗑️ Excluir usuário: " + nomeUsuario + "?\n" +
                "Esta ação não poderá ser desfeita.\n" +
                "Deseja continuar?", 
                "Confirmar Exclusão", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                
        if (opcao == JOptionPane.YES_OPTION) {
            tableModel.removeRow(linhaSelecionada);
            JOptionPane.showMessageDialog(frame, 
                    "✅ Usuário excluído com sucesso!", 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparFormulario();
        }
    }
    
    private void buscarUsuarios() {
        String termoBusca = txtBusca.getText().trim().toLowerCase();
        
        if (termoBusca.isEmpty()) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Digite um termo para buscar!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Simular busca
        JOptionPane.showMessageDialog(frame, 
                "🔍 Buscando usuários por: '" + termoBusca + "'\n" +
                "✅ " + tableModel.getRowCount() + " usuários encontrados", 
                "Resultado da Busca", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void limparBusca() {
        txtBusca.setText("");
        carregarDadosExemplo();
    }
    
    private void atualizarTabela() {
        carregarDadosExemplo();
        JOptionPane.showMessageDialog(frame, 
                "🔄 Tabela atualizada com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void limparFormulario() {
        txtNome.setText("");
        txtEmail.setText("");
        txtUsuario.setText("");
        txtSenha.setText("");
        txtConfirmarSenha.setText("");
        cbNivelAcesso.setSelectedIndex(0);
        cbStatus.setSelectedIndex(0);
        ckAtivo.setSelected(true);
        ckAlterarSenha.setSelected(false);
        
        editMode = false;
        usuarioEditId = null;
        txtNome.requestFocus();
    }
    
    public void showFrame() {
        frame.setVisible(true);
    }
}
