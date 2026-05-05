package com.br.hermescomercial.examples;

import com.br.hermescomercial.ui.components.FormPanel;
import com.br.hermescomercial.ui.components.DataTable;
import com.br.hermescomercial.ui.components.DialogManager;
import com.br.hermescomercial.ui.layout.LayoutPadrao;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Exemplo de uso dos Componentes UI Refatorados
 * Demonstra como utilizar FormPanel, DataTable e DialogManager
 * Versão 2.0.0 - UI Components Usage Example
 */
public class UIComponentsExample {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(UIComponentsExample::createAndShowGUI);
    }
    
    public static void createAndShowGUI() {
        JFrame frame = new JFrame("🎨 Componentes UI Refatorados - Exemplo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        
        // Criar painel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        
        // Criar aba de formulário
        JPanel formPanel = createFormExample();
        
        // Criar aba de tabela
        JPanel tablePanel = createTableExample();
        
        // Criar aba de exemplos
        JPanel examplesPanel = createExamplesPanel();
        
        // Criar tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(LayoutPadrao.FONTE_SUBTITULO);
        tabbedPane.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        tabbedPane.setForeground(LayoutPadrao.COR_TEXTO);
        
        tabbedPane.addTab("📝 Formulário", formPanel);
        tabbedPane.addTab("📊 Tabela", tablePanel);
        tabbedPane.addTab("🎯 Exemplos", examplesPanel);
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        frame.add(mainPanel);
        frame.setVisible(true);
    }
    
    /**
     * Cria exemplo de formulário
     */
    private static JPanel createFormExample() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        // Criar formulário
        FormPanel form = new FormPanel(2);
        
        // Adicionar campos
        form.addRequiredField("Nome:", "nome", FormPanel.FieldType.TEXT);
        form.addField("Email:", "email", FormPanel.FieldType.EMAIL);
        form.addField("Telefone:", "telefone", FormPanel.FieldType.PHONE);
        form.addRequiredField("Idade:", "idade", FormPanel.FieldType.NUMBER);
        form.addField("Salário:", "salario", FormPanel.FieldType.CURRENCY);
        form.addField("Data Nascimento:", "dataNasc", FormPanel.FieldType.DATE);
        form.addSeparator("Endereço");
        form.addField("Rua:", "rua", FormPanel.FieldType.TEXT);
        form.addField("Cidade:", "cidade", FormPanel.FieldType.TEXT);
        form.addComboBox("Estado:", "estado", new String[]{"SP", "RJ", "MG", "BA", "RS", "SC", "PR"});
        form.addField("CEP:", "cep", FormPanel.FieldType.TEXT);
        form.addSeparator("Observações");
        form.addField("Observações:", "obs", FormPanel.FieldType.TEXTAREA);
        
        // Adicionar botões
        form.addButton("💾 Salvar", "success");
        form.addButton("🧹 Limpar", "secondary");
        form.addButton("❌ Cancelar", "danger");
        
        // Configurar ações dos botões
        JButton saveButton = (JButton) form.getButtonPanel().getComponent(0);
        JButton clearButton = (JButton) form.getButtonPanel().getComponent(1);
        JButton cancelButton = (JButton) form.getButtonPanel().getComponent(2);
        
        saveButton.addActionListener(e -> {
            List<String> errors = form.validateRequired();
            if (errors.isEmpty()) {
                Map<String, Object> data = new java.util.HashMap<>();
                for (Map.Entry<String, JComponent> entry : form.getFields().entrySet()) {
                    data.put(entry.getKey(), form.getFieldValue(entry.getKey()));
                }
                
                DialogManager.showSuccess("Dados Salvos", "Formulário salvo com sucesso!\n\n" + data);
            } else {
                DialogManager.showError("Erros de Validação", String.join("\n", errors));
            }
        });
        
        clearButton.addActionListener(e -> {
            form.clearFields();
            DialogManager.showInfo("Formulário Limpo", "Todos os campos foram limpos.");
        });
        
        cancelButton.addActionListener(e -> {
            if (DialogManager.showQuestion("Deseja realmente cancelar?")) {
                form.clearFields();
            }
        });
        
        panel.add(form, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Cria exemplo de tabela
     */
    private static JPanel createTableExample() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        // Criar tabela
        DataTable table = new DataTable();
        
        // Adicionar colunas
        table.addColumn("ID", "id", 50);
        table.addColumn("Nome", "nome", 200);
        table.addColumn("Email", "email", 250);
        table.addColumn("Telefone", "telefone", 150);
        table.addColumn("Status", "status", 100);
        table.addColumn("Salário", "salario", 120);
        
        // Adicionar dados de exemplo
        table.addRow(1, "João Silva", "joao@email.com", "(11) 98765-4321", "Ativo", "R$ 5.000,00");
        table.addRow(2, "Maria Santos", "maria@email.com", "(21) 91234-5678", "Ativo", "R$ 6.500,00");
        table.addRow(3, "Pedro Costa", "pedro@email.com", "(31) 87654-3210", "Inativo", "R$ 4.200,00");
        table.addRow(4, "Ana Oliveira", "ana@email.com", "(41) 99876-5432", "Ativo", "R$ 7.800,00");
        table.addRow(5, "Carlos Ferreira", "carlos@email.com", "(51) 92345-6789", "Ativo", "R$ 5.900,00");
        table.addRow(6, "Lucia Pereira", "lucia@email.com", "(61) 87654-2109", "Ativo", "R$ 8.200,00");
        table.addRow(7, "Roberto Almeida", "roberto@email.com", "(71) 94567-8901", "Inativo", "R$ 4.800,00");
        table.addRow(8, "Fernanda Lima", "fernanda@email.com", "(81) 91234-5678", "Ativo", "R$ 6.100,00");
        
        // Adicionar listeners
        table.addSelectionListener(row -> {
            if (row >= 0) {
                String nome = (String) table.getValueAt(row, 1);
                String status = (String) table.getValueAt(row, 4);
                table.getStatusLabel().setText("👤 Selecionado: " + nome + " - " + status);
            }
        });
        
        table.addDoubleClickListener(row -> {
            if (row >= 0) {
                String nome = (String) table.getValueAt(row, 1);
                DialogManager.showInfo("Detalhes do Funcionário", 
                    "Nome: " + nome + "\n" +
                    "Linha: " + (row + 1) + "\n" +
                    "ID: " + table.getValueAt(row, 0));
            }
        });
        
        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        
        JButton addButton = LayoutPadrao.criarBotaoSucesso("➕ Adicionar");
        JButton editButton = LayoutPadrao.criarBotaoPrimario("✏️ Editar");
        JButton deleteButton = LayoutPadrao.criarBotaoPerigo("🗑️ Excluir");
        JButton refreshButton = LayoutPadrao.criarBotaoSecundario("🔄 Atualizar");
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        // Configurar ações
        addButton.addActionListener(e -> {
            // Simular adição
            int newId = table.getModel().getRowCount() + 1;
            table.addRow(newId, "Novo Funcionário", "novo@email.com", "(00) 00000-0000", "Ativo", "R$ 0,00");
            DialogManager.showSuccess("Adicionado", "Novo funcionário adicionado com sucesso!");
        });
        
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String nome = (String) table.getValueAt(selectedRow, 1);
                String newName = DialogManager.showInputDialog("Editar Nome", "Nome:", nome);
                if (newName != null && !newName.trim().isEmpty()) {
                    table.setValueAt(newName, selectedRow, 1);
                    DialogManager.showSuccess("Atualizado", "Nome atualizado com sucesso!");
                }
            } else {
                DialogManager.showWarning("Seleção", "Selecione um funcionário para editar.");
            }
        });
        
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String nome = (String) table.getValueAt(selectedRow, 1);
                if (DialogManager.showConfirmation("Excluir", "Deseja excluir o funcionário " + nome + "?") == JOptionPane.YES_OPTION) {
                    table.removeRow(selectedRow);
                    DialogManager.showSuccess("Excluído", "Funcionário excluído com sucesso!");
                }
            } else {
                DialogManager.showWarning("Seleção", "Selecione um funcionário para excluir.");
            }
        });
        
        refreshButton.addActionListener(e -> {
            table.refresh();
            DialogManager.showInfo("Atualizado", "Tabela atualizada!");
        });
        
        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(table, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Cria painel de exemplos
     */
    private static JPanel createExamplesPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        
        // Título
        JLabel titleLabel = new JLabel("🎯 Exemplos de Diálogos");
        titleLabel.setFont(LayoutPadrao.FONTE_TITULO);
        titleLabel.setForeground(LayoutPadrao.COR_TEXTO);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        // Botões de exemplo
        JButton infoButton = LayoutPadrao.criarBotaoPrimario("ℹ️ Informação");
        JButton successButton = LayoutPadrao.criarBotaoSucesso("✅ Sucesso");
        JButton warningButton = LayoutPadrao.criarBotaoSecundario("⚠️ Aviso");
        JButton errorButton = LayoutPadrao.criarBotaoPerigo("❌ Erro");
        
        JButton questionButton = LayoutPadrao.criarBotaoPrimario("❓ Pergunta");
        JButton confirmationButton = LayoutPadrao.criarBotaoSecundario("🔒 Confirmação");
        JButton inputButton = LayoutPadrao.criarBotaoPrimario("📝 Entrada");
        JButton passwordButton = LayoutPadrao.criarBotaoSecundario("🔐 Senha");
        
        JButton progressButton = LayoutPadrao.criarBotaoPrimario("⏳ Progresso");
        JButton formButton = LayoutPadrao.criarBotaoSucesso("📋 Formulário");
        JButton customButton = LayoutPadrao.criarBotaoSecundario("🎨 Customizado");
        
        // Primeira linha
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(infoButton, gbc);
        
        gbc.gridx = 1;
        panel.add(successButton, gbc);
        
        // Segunda linha
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(warningButton, gbc);
        
        gbc.gridx = 1;
        panel.add(errorButton, gbc);
        
        // Terceira linha
        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(questionButton, gbc);
        
        gbc.gridx = 1;
        panel.add(confirmationButton, gbc);
        
        // Quarta linha
        gbc.gridy = 4;
        gbc.gridx = 0;
        panel.add(inputButton, gbc);
        
        gbc.gridx = 1;
        panel.add(passwordButton, gbc);
        
        // Quinta linha
        gbc.gridy = 5;
        gbc.gridx = 0;
        panel.add(progressButton, gbc);
        
        gbc.gridx = 1;
        panel.add(formButton, gbc);
        
        // Sexta linha
        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(customButton, gbc);
        
        // Configurar ações
        infoButton.addActionListener(e -> {
            DialogManager.showInfo("Informação", "Esta é uma mensagem informativa.\n\nO sistema está funcionando corretamente!");
        });
        
        successButton.addActionListener(e -> {
            DialogManager.showSuccess("Sucesso", "Operação concluída com sucesso!\n\nTodos os dados foram salvos.");
        });
        
        warningButton.addActionListener(e -> {
            DialogManager.showWarning("Aviso", "Atenção: Esta é uma mensagem de aviso.\n\nVerifique os dados antes de continuar.");
        });
        
        errorButton.addActionListener(e -> {
            DialogManager.showError("Erro", "Ocorreu um erro inesperado.\n\nPor favor, tente novamente mais tarde.");
        });
        
        questionButton.addActionListener(e -> {
            boolean result = DialogManager.showQuestion("Deseja continuar?", "Esta ação não pode ser desfeita.");
            DialogManager.showInfo("Resposta", result ? "Você clicou em SIM" : "Você clicou em NÃO");
        });
        
        confirmationButton.addActionListener(e -> {
            int result = DialogManager.showConfirmation("Confirmação", "Escolha uma opção:");
            String[] options = {"SIM", "NÃO", "CANCELAR"};
            String[] messages = {"Você confirmou a ação.", "Você negou a ação.", "Você cancelou a ação."};
            
            String message = result >= 0 && result < messages.length ? messages[result] : "Opção desconhecida";
            DialogManager.showInfo("Resultado", message);
        });
        
        inputButton.addActionListener(e -> {
            String input = DialogManager.showInputDialog("Entrada de Dados", "Digite seu nome:");
            if (input != null && !input.trim().isEmpty()) {
                DialogManager.showSuccess("Bem-vindo", "Olá, " + input + "!");
            }
        });
        
        passwordButton.addActionListener(e -> {
            String password = DialogManager.showPasswordDialog("Autenticação", "Digite sua senha:");
            if (password != null) {
                DialogManager.showInfo("Senha", "Senha digitada: " + "*".repeat(password.length()));
            }
        });
        
        progressButton.addActionListener(e -> {
            DialogManager.showProgressDialog("Processando Dados", () -> {
                try {
                    Thread.sleep(3000); // Simular processamento
                    return true;
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            });
        });
        
        formButton.addActionListener(e -> {
            FormPanel form = new FormPanel(1);
            form.addRequiredField("Nome:", "nome", FormPanel.FieldType.TEXT);
            form.addField("Email:", "email", FormPanel.FieldType.EMAIL);
            form.addRequiredField("Idade:", "idade", FormPanel.FieldType.NUMBER);
            
            form.addButton("OK", "success");
            form.addButton("Cancelar", "secondary");
            
            Map<String, Object> result = DialogManager.showFormDialog("Cadastro", form);
            if (result != null) {
                DialogManager.showSuccess("Dados Recebidos", "Dados do formulário:\n" + result);
            }
        });
        
        customButton.addActionListener(e -> {
            DialogManager.DialogResult result = DialogManager.builder("Diálogo Customizado", "Este é um diálogo customizado com múltiplas opções.", DialogManager.DialogType.QUESTION)
                .options("Opção 1", "Opção 2", "Opção 3", "Cancelar")
                .size(400, 200)
                .show();
            
            String message = "Você selecionou: " + result.name();
            DialogManager.showInfo("Resultado", message);
        });
        
        return panel;
    }
}
