package com.br.hermescomercial.erp.controller;

import com.br.hermescomercial.ui.layout.LayoutPadrao;

import javax.swing.*;
import java.awt.*;

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
        
        // Criar painéis de formulário e tabela (tabela pode ser null para telas sem tabela)
        JPanel formularioPanel = createFormPanel();
        JPanel tabelaPanel = null; // Tela de configuração não tem tabela
        
        // Usando layout padrão Header → Busca → Formulário (sem tabela)
        mainPanel = LayoutPadrao.criarLayoutPadraoGestao(
            false, // isPDV (false para ERP)
            "⚙️ Configurações do Sistema - ERP",
            "Digite configuração desejada...",
            formularioPanel,
            tabelaPanel
        );
        
        frame.add(mainPanel);
        
        carregarConfiguracoes();
        frame.setVisible(true);
    }
    
    public JFrame getFrame() {
        return frame;
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
            "🏢 Configurações Gerais"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
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
        cbTema = new JComboBox<>(new String[]{"Hermes Padrão", "Escuro", "Claro"});
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
        
        // Opções
        gbc.gridx = 0; gbc.gridy = 7; gbc.weightx = 1.0; gbc.gridwidth = 2;
        ckSalvarAutomatico = new JCheckBox("Salvar automaticamente");
        ckSalvarAutomatico.setFont(LayoutPadrao.FONTE_CAMPO);
        ckSalvarAutomatico.setBackground(LayoutPadrao.COR_FUNDO);
        ckSalvarAutomatico.setForeground(LayoutPadrao.COR_TEXTO);
        formPanel.add(ckSalvarAutomatico, gbc);
        
        gbc.gridy = 8;
        ckNotificacao = new JCheckBox("Ativar notificações");
        ckNotificacao.setFont(LayoutPadrao.FONTE_CAMPO);
        ckNotificacao.setBackground(LayoutPadrao.COR_FUNDO);
        ckNotificacao.setForeground(LayoutPadrao.COR_TEXTO);
        formPanel.add(ckNotificacao, gbc);
        
        gbc.gridy = 9;
        ckBackupAutomatico = new JCheckBox("Realizar backup automático");
        ckBackupAutomatico.setFont(LayoutPadrao.FONTE_CAMPO);
        ckBackupAutomatico.setBackground(LayoutPadrao.COR_FUNDO);
        ckBackupAutomatico.setForeground(LayoutPadrao.COR_TEXTO);
        formPanel.add(ckBackupAutomatico, gbc);
        
        return formPanel;
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
        textField.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(LayoutPadrao.COR_BORDA, 1),
            javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return textField;
    }
    
    private JFormattedTextField createTelefoneField() {
        JFormattedTextField textField = new JFormattedTextField();
        textField.setFont(LayoutPadrao.FONTE_CAMPO);
        textField.setForeground(LayoutPadrao.COR_TEXTO);
        textField.setBackground(Color.WHITE);
        textField.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(LayoutPadrao.COR_BORDA, 1),
            javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Adicionar tooltip com formato esperado
        textField.setToolTipText("Formato: (XX) XXXXX-XXXX - Máximo 13 caracteres");
        
        return textField;
    }
    
    private JFormattedTextField createCnpjField() {
        JFormattedTextField textField = new JFormattedTextField();
        textField.setFont(LayoutPadrao.FONTE_CAMPO);
        textField.setForeground(LayoutPadrao.COR_TEXTO);
        textField.setBackground(Color.WHITE);
        textField.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(LayoutPadrao.COR_BORDA, 1),
            javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Adicionar tooltip com formato esperado
        textField.setToolTipText("Formato: XX.XXX.XXX/XXXX-XX - Máximo 14 caracteres");
        
        return textField;
    }
    
    private JTextField createEmailField() {
        JTextField textField = new JTextField();
        textField.setFont(LayoutPadrao.FONTE_CAMPO);
        textField.setForeground(LayoutPadrao.COR_TEXTO);
        textField.setBackground(Color.WHITE);
        textField.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(LayoutPadrao.COR_BORDA, 1),
            javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Adicionar tooltip com formato esperado
        textField.setToolTipText("Formato: usuario@dominio.com");
        
        return textField;
    }
    
    private void carregarConfiguracoes() {
        // Simular carregamento de configurações
        txtNomeEmpresa.setText("Hermes Comercial LTDA");
        txtCnpj.setText("");
        txtTelefone.setText("");
        txtEndereco.setText("Rua das Flores, 123 - Centro, São Paulo - SP");
        txtEmail.setText("contato@hermescomercial.com.br");
        cbTema.setSelectedIndex(0);
        cbIdioma.setSelectedIndex(0);
        ckSalvarAutomatico.setSelected(true);
        ckNotificacao.setSelected(true);
        ckBackupAutomatico.setSelected(true);
    }
}
