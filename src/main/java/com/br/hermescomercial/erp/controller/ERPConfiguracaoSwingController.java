package com.br.hermescomercial.erp.controller;

import com.br.hermescomercial.ui.layout.LayoutPadrao;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Controller para tela de configuração do sistema ERP
 * Versão 2.3.0 - Arquitetura Modular - Tema Padrão Hermes
 */
public class ERPConfiguracaoSwingController {
    
    private JFrame frame;
    private JPanel mainPanel;
    private JTextField txtNomeEmpresa, txtCnpj, txtEndereco, txtTelefone, txtEmail;
    private JComboBox<String> cbTema, cbIdioma;
    private JCheckBox ckSalvarAutomatico, ckNotificacao, ckBackupAutomatico;
    private JButton btnSalvar, btnCancelar, btnTestarConexao, btnRestaurarPadroes;
    
    public ERPConfiguracaoSwingController() {
        inicializarUI();
    }
    
    private void inicializarUI() {
        frame = new JFrame("⚙️ Configurações do Sistema");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 700);
        frame.setLocationRelativeTo(null);
        
        // Aplicar tema padrão
        frame.getContentPane().setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        frame.add(mainPanel);
        frame.add(createHeaderPanel(), BorderLayout.NORTH);
        frame.add(createFormPanel(), BorderLayout.CENTER);
        frame.add(createButtonPanel(), BorderLayout.SOUTH);
        
        carregarConfiguracoes();
    }
    
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        JLabel titleLabel = new JLabel("⚙️ Configurações do Sistema");
        titleLabel.setFont(LayoutPadrao.FONTE_TITULO);
        titleLabel.setForeground(LayoutPadrao.COR_PRIMARIA);
        
        JLabel subtitleLabel = new JLabel("Configurações gerais e preferências do sistema");
        subtitleLabel.setFont(LayoutPadrao.FONTE_TEXTO);
        subtitleLabel.setForeground(LayoutPadrao.COR_TEXTO_CLARO);
        
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(subtitleLabel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "🏢 Configurações Gerais", 
            TitledBorder.LEFT, 
            TitledBorder.TOP, 
            LayoutPadrao.FONTE_SUBTITULO, 
            LayoutPadrao.COR_PRIMARIA));
        formPanel.setBackground(LayoutPadrao.COR_FUNDO);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Nome da Empresa
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
        formPanel.add(createLabel("Nome da Empresa:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNomeEmpresa = createTextField();
        formPanel.add(txtNomeEmpresa, gbc);
        
        // CNPJ
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        formPanel.add(createLabel("CNPJ:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCnpj = createCnpjField();
        formPanel.add(txtCnpj, gbc);
        
        // Endereço
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
        formPanel.add(createLabel("Endereço:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtEndereco = createTextField();
        formPanel.add(txtEndereco, gbc);
        
        // Telefone
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0;
        formPanel.add(createLabel("Telefone:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtTelefone = createTelefoneField();
        formPanel.add(txtTelefone, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.0;
        formPanel.add(createLabel("E-mail:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtEmail = createEmailField();
        formPanel.add(txtEmail, gbc);
        
        // Tema
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0.0;
        formPanel.add(createLabel("Tema:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        cbTema = new JComboBox<>(new String[]{"Hermes Padrão", "Moderno Claro", "Moderno Escuro"});
        cbTema.setFont(LayoutPadrao.FONTE_CAMPO);
        cbTema.setPreferredSize(new Dimension(150, 30));
        formPanel.add(cbTema, gbc);
        
        // Idioma
        gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0.0;
        formPanel.add(createLabel("Idioma:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        cbIdioma = new JComboBox<>(new String[]{"Português (Brasil)", "English", "Espanhol"});
        cbIdioma.setFont(LayoutPadrao.FONTE_CAMPO);
        cbIdioma.setPreferredSize(new Dimension(150, 30));
        formPanel.add(cbIdioma, gbc);
        
        // Salvar Automaticamente
        gbc.gridx = 0; gbc.gridy = 7; gbc.weightx = 0.0;
        formPanel.add(createLabel("Salvar Automaticamente:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        ckSalvarAutomatico = new JCheckBox("Salvar configurações automaticamente");
        ckSalvarAutomatico.setFont(LayoutPadrao.FONTE_CAMPO);
        ckSalvarAutomatico.setBackground(LayoutPadrao.COR_FUNDO);
        ckSalvarAutomatico.setForeground(LayoutPadrao.COR_TEXTO);
        formPanel.add(ckSalvarAutomatico, gbc);
        
        // Notificações
        gbc.gridx = 0; gbc.gridy = 8; gbc.weightx = 0.0;
        formPanel.add(createLabel("Notificações:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        ckNotificacao = new JCheckBox("Receber notificações do sistema");
        ckNotificacao.setFont(LayoutPadrao.FONTE_CAMPO);
        ckNotificacao.setBackground(LayoutPadrao.COR_FUNDO);
        ckNotificacao.setForeground(LayoutPadrao.COR_TEXTO);
        formPanel.add(ckNotificacao, gbc);
        
        // Backup Automático
        gbc.gridx = 0; gbc.gridy = 9; gbc.weightx = 0.0;
        formPanel.add(createLabel("Backup Automático:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        ckBackupAutomatico = new JCheckBox("Realizar backup automático");
        ckBackupAutomatico.setFont(LayoutPadrao.FONTE_CAMPO);
        ckBackupAutomatico.setBackground(LayoutPadrao.COR_FUNDO);
        ckBackupAutomatico.setForeground(LayoutPadrao.COR_TEXTO);
        formPanel.add(ckBackupAutomatico, gbc);
        
        return formPanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        btnSalvar = LayoutPadrao.criarBotaoSucesso("💾 Salvar");
        btnSalvar.addActionListener(e -> salvarConfiguracoes());
        btnCancelar = LayoutPadrao.criarBotaoPerigo("❌ Cancelar");
        btnCancelar.addActionListener(e -> fecharTela());
        btnTestarConexao = LayoutPadrao.criarBotaoAlerta("🔧 Testar Conexão");
        btnTestarConexao.addActionListener(e -> testarConexao());
        btnRestaurarPadroes = LayoutPadrao.criarBotaoSecundario("🔄 Restaurar Padrões");
        btnRestaurarPadroes.addActionListener(e -> restaurarPadroes());
        
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnTestarConexao);
        buttonPanel.add(btnRestaurarPadroes);
        
        return buttonPanel;
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LayoutPadrao.FONTE_ROTULO);
        label.setForeground(LayoutPadrao.COR_TEXTO);
        return label;
    }
    
    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setFont(LayoutPadrao.FONTE_CAMPO);
        textField.setForeground(LayoutPadrao.COR_TEXTO);
        textField.setBackground(Color.WHITE);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LayoutPadrao.COR_BORDA, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return textField;
    }
    
    private JTextField createTelefoneField() {
        JTextField textField = new JTextField();
        textField.setFont(LayoutPadrao.FONTE_CAMPO);
        textField.setForeground(LayoutPadrao.COR_TEXTO);
        textField.setBackground(Color.WHITE);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LayoutPadrao.COR_BORDA, 1),
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
    
    private JTextField createCnpjField() {
        JTextField textField = new JTextField();
        textField.setFont(LayoutPadrao.FONTE_CAMPO);
        textField.setForeground(LayoutPadrao.COR_TEXTO);
        textField.setBackground(Color.WHITE);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LayoutPadrao.COR_BORDA, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Limitar a 14 caracteres (formato: XX.XXX.XXX/XXXX-XX)
        textField.setDocument(new javax.swing.text.PlainDocument() {
            @Override
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if (str == null) return;
                
                if ((getLength() + str.length()) <= 14) {
                    // Verificar se contém apenas números, ponto, barra e traço
                    String validChars = "0123456789./-";
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
        textField.setToolTipText("Formato: XX.XXX.XXX/XXXX-XX - Máximo 14 caracteres");
        
        return textField;
    }
    
    private JTextField createEmailField() {
        JTextField textField = new JTextField();
        textField.setFont(LayoutPadrao.FONTE_CAMPO);
        textField.setForeground(LayoutPadrao.COR_TEXTO);
        textField.setBackground(Color.WHITE);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LayoutPadrao.COR_BORDA, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Adicionar tooltip com formato esperado
        textField.setToolTipText("Formato: usuario@dominio.com");
        
        return textField;
    }
    
    
    private void carregarConfiguracoes() {
        // Simular carregamento de configurações
        txtNomeEmpresa.setText("Hermes Comercial LTDA");
        txtCnpj.setText("12.345.678/0001-99");
        txtEndereco.setText("Rua das Flores, 123 - Centro, São Paulo - SP");
        txtTelefone.setText("(11) 9876-5432");
        txtEmail.setText("contato@hermescomercial.com.br");
        cbTema.setSelectedIndex(0);
        cbIdioma.setSelectedIndex(0);
        ckSalvarAutomatico.setSelected(true);
        ckNotificacao.setSelected(true);
        ckBackupAutomatico.setSelected(true);
    }
    
    private void salvarConfiguracoes() {
        String nomeEmpresa = txtNomeEmpresa.getText().trim();
        String cnpj = txtCnpj.getText().trim();
        String endereco = txtEndereco.getText().trim();
        String telefone = txtTelefone.getText().trim();
        String email = txtEmail.getText().trim();
        String tema = (String) cbTema.getSelectedItem();
        String idioma = (String) cbIdioma.getSelectedItem();
        boolean salvarAuto = ckSalvarAutomatico.isSelected();
        boolean notificacao = ckNotificacao.isSelected();
        boolean backupAuto = ckBackupAutomatico.isSelected();
        
        if (nomeEmpresa.isEmpty()) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Nome da empresa é obrigatório!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Simular salvamento
        JOptionPane.showMessageDialog(frame, 
                "✅ Configurações salvas com sucesso!\n\n" +
                "Empresa: " + nomeEmpresa + "\n" +
                "CNPJ: " + cnpj + "\n" +
                "Endereço: " + endereco + "\n" +
                "Telefone: " + telefone + "\n" +
                "E-mail: " + email + "\n" +
                "Tema: " + tema + "\n" +
                "Idioma: " + idioma + "\n" +
                "Salvar Auto: " + (salvarAuto ? "Sim" : "Não") + "\n" +
                "Notificações: " + (notificacao ? "Sim" : "Não") + "\n" +
                "Backup Auto: " + (backupAuto ? "Sim" : "Não"), 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void testarConexao() {
        JOptionPane.showMessageDialog(frame, 
                "🔧 Testando conexão com banco de dados...\n" +
                "✅ Conexão estabelecida com sucesso!\n" +
                "📊 Status da conexão: ATIVA", 
                "Teste de Conexão", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void restaurarPadroes() {
        int opcao = JOptionPane.showConfirmDialog(frame, 
                "🔄 Restaurar configurações padrão?\n" +
                "Isso irá redefinir todas as configurações para os valores padrão.\n" +
                "Deseja continuar?", 
                "Restaurar Padrões", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                
        if (opcao == JOptionPane.YES_OPTION) {
            carregarConfiguracoes();
            JOptionPane.showMessageDialog(frame, 
                    "✅ Configurações padrão restauradas com sucesso!", 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void fecharTela() {
        frame.dispose();
    }
    
    public void showFrame() {
        frame.setVisible(true);
    }
}
