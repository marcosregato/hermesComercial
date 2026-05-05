package com.br.hermescomercial.erp.controller;

import com.br.hermescomercial.ui.layout.LayoutPadrao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Controller para tela de gestão de serviços do ERP
 * Versão 2.3.0 - Arquitetura Modular - Tema Padrão Hermes
 */
public class ERPServicoSwingController {
    
    private JFrame frame;
    private JPanel mainPanel;
    private JTable servicosTable;
    private DefaultTableModel tableModel;
    private JTextField txtNome, txtValor, txtDuracao;
    private JTextArea txtDescricao;
    private JComboBox<String> cbCategoria, cbStatus, cbPrioridade;
    private JFormattedTextField txtDataInicio, txtDataFim;
    private DecimalFormat currencyFormat;
    
    public ERPServicoSwingController() {
        inicializarUI();
    }
    
    private void inicializarUI() {
        frame = new JFrame("🔧 Gestão de Serviços - ERP");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        
        // Configurar formato de moeda e data
        currencyFormat = new DecimalFormat("R$ #,##0.00");
        
        // Aplicar tema padrão
        frame.getContentPane().setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        // Criar painéis de formulário e tabela
        JPanel formularioPanel = createFormPanel();
        JPanel tabelaPanel = createTablePanel();
        
        // Usando layout padrão Header → Busca → Formulário → Tabela
        mainPanel = LayoutPadrao.criarLayoutPadraoGestao(
            false, // isPDV (false para ERP)
            "🔧 Gestão de Serviços - ERP",
            "Digite nome ou categoria do serviço...",
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
        formPanel.add(createLabel("Nome do Serviço:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
        txtNome = createModernTextField(40);
        formPanel.add(txtNome, gbc);
        
        // Categoria
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.weightx = 0.0;
        formPanel.add(createLabel("Categoria:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.5;
        cbCategoria = new JComboBox<>(new String[]{"Consultoria", "Manutenção", "Instalação", "Treinamento", "Suporte", "Outros"});
        cbCategoria.setFont(LayoutPadrao.FONTE_CAMPO);
        formPanel.add(cbCategoria, gbc);
        
        // Prioridade
        gbc.gridx = 2; gbc.weightx = 0.0;
        formPanel.add(createLabel("Prioridade:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.5;
        cbPrioridade = new JComboBox<>(new String[]{"Baixa", "Média", "Alta", "Urgente"});
        cbPrioridade.setFont(LayoutPadrao.FONTE_CAMPO);
        formPanel.add(cbPrioridade, gbc);
        
        // Valor
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
        formPanel.add(createLabel("Valor (R$):"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.5;
        txtValor = createModernTextField(15);
        formPanel.add(txtValor, gbc);
        
        // Duração
        gbc.gridx = 2; gbc.weightx = 0.0;
        formPanel.add(createLabel("Duração (horas):"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.5;
        txtDuracao = createModernTextField(10);
        formPanel.add(txtDuracao, gbc);
        
        // Data Início
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0;
        formPanel.add(createLabel("Data Início:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.5;
        txtDataInicio = createFormattedTextField();
        txtDataInicio.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        formPanel.add(txtDataInicio, gbc);
        
        // Data Fim
        gbc.gridx = 2; gbc.weightx = 0.0;
        formPanel.add(createLabel("Data Fim:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.5;
        txtDataFim = createFormattedTextField();
        txtDataFim.setText(LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        formPanel.add(txtDataFim, gbc);
        
        // Status
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.0;
        formPanel.add(createLabel("Status:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 0.5;
        cbStatus = new JComboBox<>(new String[]{"Pendente", "Em Andamento", "Concluído", "Cancelado"});
        cbStatus.setFont(LayoutPadrao.FONTE_CAMPO);
        formPanel.add(cbStatus, gbc);
        
        // Descrição
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 4; gbc.weightx = 1.0;
        formPanel.add(createLabel("Descrição:"), gbc);
        
        gbc.gridy = 6;
        txtDescricao = createModernTextArea(5, 50);
        JScrollPane scrollDescricao = new JScrollPane(txtDescricao);
        scrollDescricao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LayoutPadrao.COR_BORDA, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        formPanel.add(scrollDescricao, gbc);
        
        // Botões
        gbc.gridy = 7;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        
        JButton btnSalvar = LayoutPadrao.criarBotaoSucesso("💾 Salvar");
        btnSalvar.addActionListener(e -> salvarServico());
        buttonPanel.add(btnSalvar);
        
        JButton btnEditar = LayoutPadrao.criarBotaoPrimario("✏️ Editar");
        btnEditar.addActionListener(e -> editarServico());
        buttonPanel.add(btnEditar);
        
        JButton btnExcluir = LayoutPadrao.criarBotaoPerigo("🗑️ Excluir");
        btnExcluir.addActionListener(e -> excluirServico());
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
        String[] colunas = {"ID", "Nome", "Categoria", "Valor", "Status", "Prioridade", "Data Início", "Data Fim"};
        
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        servicosTable = new JTable(tableModel);
        servicosTable.setFont(LayoutPadrao.FONTE_TEXTO);
        servicosTable.getTableHeader().setFont(LayoutPadrao.FONTE_SUBTITULO);
        servicosTable.setRowHeight(25);
        servicosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Ajustar largura das colunas
        servicosTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        servicosTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Nome
        servicosTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Categoria
        servicosTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Valor
        servicosTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Status
        servicosTable.getColumnModel().getColumn(5).setPreferredWidth(80);  // Prioridade
        servicosTable.getColumnModel().getColumn(6).setPreferredWidth(100); // Data Início
        servicosTable.getColumnModel().getColumn(7).setPreferredWidth(100); // Data Fim
        
        JScrollPane scrollPane = new JScrollPane(servicosTable);
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
    
    private JTextArea createModernTextArea(int rows, int columns) {
        JTextArea area = new JTextArea(rows, columns);
        area.setFont(LayoutPadrao.FONTE_CAMPO);
        area.setForeground(LayoutPadrao.COR_TEXTO);
        area.setBackground(Color.WHITE);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        return area;
    }
    
    private JFormattedTextField createFormattedTextField() {
        JFormattedTextField field = new JFormattedTextField();
        field.setFont(LayoutPadrao.FONTE_CAMPO);
        field.setForeground(LayoutPadrao.COR_TEXTO);
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(LayoutPadrao.COR_BORDA, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return field;
    }
    
    private void salvarServico() {
        String nome = txtNome.getText().trim();
        String valorStr = txtValor.getText().trim();
        String duracaoStr = txtDuracao.getText().trim();
        
        // Validação
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Nome do serviço é obrigatório!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (valorStr.isEmpty()) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Valor é obrigatório!", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            BigDecimal valor = new BigDecimal(valorStr.replace("R$", "").replace(",", "."));
            int duracao = Integer.parseInt(duracaoStr);
            
            Long novoId = System.currentTimeMillis();
            Object[] novaLinha = {
                novoId, nome, cbCategoria.getSelectedItem(), currencyFormat.format(valor), 
                cbStatus.getSelectedItem(), cbPrioridade.getSelectedItem(),
                txtDataInicio.getText(), txtDataFim.getText()
            };
            tableModel.addRow(novaLinha);
            
            JOptionPane.showMessageDialog(frame, 
                "✅ Serviço salvo com sucesso!\n" +
                "Nome: " + nome + "\n" +
                "Valor: " + currencyFormat.format(valor) + "\n" +
                "Duração: " + duracao + " horas", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            limparFormulario();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, 
                "❌ Erro ao salvar serviço: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editarServico() {
        int linhaSelecionada = servicosTable.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Nenhum serviço selecionado!\n" +
                "Selecione um serviço na tabela para editar.", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Preencher formulário com dados selecionados
        txtNome.setText((String) tableModel.getValueAt(linhaSelecionada, 1));
        cbCategoria.setSelectedItem(tableModel.getValueAt(linhaSelecionada, 2));
        txtValor.setText(tableModel.getValueAt(linhaSelecionada, 3).toString());
        cbStatus.setSelectedItem(tableModel.getValueAt(linhaSelecionada, 4));
        cbPrioridade.setSelectedItem(tableModel.getValueAt(linhaSelecionada, 5));
        txtDataInicio.setText((String) tableModel.getValueAt(linhaSelecionada, 6));
        txtDataFim.setText((String) tableModel.getValueAt(linhaSelecionada, 7));
        
        txtNome.requestFocus();
    }
    
    private void excluirServico() {
        int linhaSelecionada = servicosTable.getSelectedRow();
        
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(frame, 
                "⚠️ Nenhum serviço selecionado!\n" +
                "Selecione um serviço na tabela para excluir.", 
                "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String nome = (String) tableModel.getValueAt(linhaSelecionada, 1);
        
        int confirmacao = JOptionPane.showConfirmDialog(frame, 
            "🗑️ Confirmar Exclusão\n" +
            "Deseja realmente excluir o serviço selecionado?\n\n" +
            "Nome: " + nome, 
            "Confirmar Exclusão", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            try {
                tableModel.removeRow(linhaSelecionada);
                
                JOptionPane.showMessageDialog(frame, 
                    "✅ Serviço excluído com sucesso!", 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, 
                    "❌ Erro ao excluir serviço: " + e.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void limparFormulario() {
        txtNome.setText("");
        txtDescricao.setText("");
        txtValor.setText("");
        txtDuracao.setText("");
        cbCategoria.setSelectedIndex(0);
        cbStatus.setSelectedIndex(0);
        cbPrioridade.setSelectedIndex(0);
        txtDataInicio.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        txtDataFim.setText(LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }
    
    private void carregarDadosExemplo() {
        // Limpar tabela
        tableModel.setRowCount(0);
        
        // Dados de exemplo
        Object[][] dados = {
            {1L, "Consultoria ERP", "Consultoria", "R$ 5.000,00", "Em Andamento", "Alta", "01/05/2024", "15/05/2024"},
            {2L, "Manutenção Servidores", "Manutenção", "R$ 1.500,00", "Concluído", "Média", "10/05/2024", "12/05/2024"},
            {3L, "Instalação Software", "Instalação", "R$ 800,00", "Pendente", "Baixa", "20/05/2024", "22/05/2024"},
            {4L, "Treinamento Equipe", "Treinamento", "R$ 3.000,00", "Pendente", "Média", "25/05/2024", "30/05/2024"},
            {5L, "Suporte Técnico", "Suporte", "R$ 500,00", "Em Andamento", "Urgente", "05/05/2024", "10/05/2024"}
        };
        
        for (Object[] row : dados) {
            tableModel.addRow(row);
        }
    }
}
