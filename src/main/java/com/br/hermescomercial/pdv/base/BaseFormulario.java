package com.br.hermescomercial.pdv.base;

import com.br.hermescomercial.util.SystemLogger;
import com.br.hermescomercial.pdv.patterns.ConfigurationManager;
import com.br.hermescomercial.pdv.patterns.NotificationSystem;
import com.br.hermescomercial.pdv.patterns.ValidationStrategy;
import com.br.hermescomercial.pdv.patterns.CommandPattern;
import com.br.hermescomercial.pdv.patterns.DAOPattern;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

/**
 * Classe Base para Formulários do Sistema PDV
 * Contém variáveis e métodos comuns compartilhados entre todos os formulários
 */
public abstract class BaseFormulario {
    
    // Variáveis comuns a todos os formulários
    protected JPanel workArea;
    protected String usuarioAtual;
    protected String nomeUsuario;
    protected JPanel mainPanel;
    protected String formularioNome;
    
    // Componentes de UI comuns
    protected Color corPrimaria = new Color(52, 152, 219);
    protected Color corSecundaria = new Color(46, 204, 113);
    protected Color corErro = new Color(231, 76, 60);
    protected Color corSucesso = new Color(39, 174, 96);
    protected Color corAviso = new Color(241, 196, 15);
    
    // Fontes padrão
    protected Font fontTitulo = new Font("Arial", Font.BOLD, 18);
    protected Font fontSubtitulo = new Font("Arial", Font.BOLD, 14);
    protected Font fontPadrao = new Font("Arial", Font.PLAIN, 12);
    protected Font fontCampo = new Font("Arial", Font.PLAIN, 11);
    
    // Sistemas de patterns
    protected ConfigurationManager configManager;
    protected NotificationSystem notificationSystem;
    protected ValidationStrategy.ValidationContext validationContext;
    protected CommandPattern.CommandInvoker commandInvoker;
    
    /**
     * Construtor base para todos os formulários
     */
    public BaseFormulario(JPanel workArea, String usuarioAtual, String nomeUsuario, String formularioNome) {
        this.workArea = workArea;
        this.usuarioAtual = usuarioAtual;
        this.nomeUsuario = nomeUsuario;
        this.formularioNome = formularioNome;
        
        // Inicializar sistemas
        inicializarSistemas();
        
        // Limpar workArea
        if (workArea != null) {
            workArea.removeAll();
            if (workArea.getLayout() == null) {
                workArea.setLayout(new BorderLayout());
            }
        }
        
        SystemLogger.ui("=== INICIANDO FORMULÁRIO: " + formularioNome + " ===");
        SystemLogger.ui("Usuário: " + usuarioAtual + " | Nome: " + nomeUsuario);
    }
    
    /**
     * Inicializa os sistemas de patterns
     */
    private void inicializarSistemas() {
        this.configManager = ConfigurationManager.getInstance();
        this.notificationSystem = NotificationSystem.getInstance();
        this.commandInvoker = new CommandPattern.CommandInvoker();
        this.validationContext = ValidationStrategy.createValidation();
    }
    
    /**
     * Método abstrato para criar o formulário específico
     */
    public abstract JPanel criarFormulario();
    
    /**
     * Cria painel com título padrão
     */
    protected JPanel criarPainelTitulo(String titulo) {
        JPanel painelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelTitulo.setBackground(Color.WHITE);
        painelTitulo.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(fontTitulo);
        lblTitulo.setForeground(corPrimaria);
        
        painelTitulo.add(lblTitulo);
        return painelTitulo;
    }
    
    /**
     * Cria painel de botões padrão (Salvar, Editar, Excluir, Limpar)
     */
    protected JPanel criarPainelBotoesAcao() {
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JButton btnSalvar = criarBotao("💾 Salvar", corSucesso);
        JButton btnEditar = criarBotao("✏️ Editar", corPrimaria);
        JButton btnExcluir = criarBotao("🗑️ Excluir", corErro);
        JButton btnLimpar = criarBotao("🧹 Limpar", corAviso);
        
        // Adicionar listeners padrão
        btnSalvar.addActionListener(e -> acaoSalvar());
        btnEditar.addActionListener(e -> acaoEditar());
        btnExcluir.addActionListener(e -> acaoExcluir());
        btnLimpar.addActionListener(e -> acaoLimpar());
        
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnLimpar);
        
        return painelBotoes;
    }
    
    /**
     * Cria botão com estilo padrão
     */
    protected JButton criarBotao(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFont(fontPadrao);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efeito hover
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                botao.setBackground(cor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(cor);
            }
        });
        
        return botao;
    }
    
    /**
     * Cria campo de texto com validação padrão
     */
    protected JTextField criarCampoTexto(String placeholder, boolean obrigatorio) {
        JTextField campo = new JTextField();
        campo.setFont(fontCampo);
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        
        if (placeholder != null && !placeholder.isEmpty()) {
            campo.setToolTipText(placeholder + (obrigatorio ? " (Obrigatório)" : ""));
        }
        
        return campo;
    }
    
    /**
     * Cria combo box com validação padrão
     */
    protected <T> JComboBox<T> criarComboBox(T[] itens, String placeholder, boolean obrigatorio) {
        JComboBox<T> combo = new JComboBox<>();
        combo.setFont(fontCampo);
        combo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        
        // Adicionar itens
        combo.addItem(null); // Placeholder
        for (T item : itens) {
            combo.addItem(item);
        }
        
        if (placeholder != null && !placeholder.isEmpty()) {
            combo.setToolTipText(placeholder + (obrigatorio ? " (Obrigatório)" : ""));
        }
        
        return combo;
    }
    
    /**
     * Aplica estilo de erro em campo
     */
    protected void aplicarErroCampo(JComponent campo) {
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(corErro, 2),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
    }
    
    /**
     * Aplica estilo de sucesso em campo
     */
    protected void aplicarSucessoCampo(JComponent campo) {
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(corSucesso, 2),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
    }
    
    /**
     * Reseta estilo de campo para padrão
     */
    protected void resetarEstiloCampo(JComponent campo) {
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
    }
    
    /**
     * Validação de campos obrigatórios
     */
    protected boolean validarCamposObrigatorios(JComponent... campos) {
        List<String> erros = new ArrayList<>();
        
        for (JComponent campo : campos) {
            if (campo instanceof JTextField) {
                JTextField txtCampo = (JTextField) campo;
                if (txtCampo.getText().trim().isEmpty()) {
                    erros.add("Campo " + txtCampo.getToolTipText() + " é obrigatório");
                    aplicarErroCampo(campo);
                } else {
                    resetarEstiloCampo(campo);
                }
            } else if (campo instanceof JComboBox) {
                JComboBox<?> combo = (JComboBox<?>) campo;
                if (combo.getSelectedItem() == null) {
                    erros.add("Campo " + combo.getToolTipText() + " é obrigatório");
                    aplicarErroCampo(campo);
                } else {
                    resetarEstiloCampo(campo);
                }
            }
        }
        
        if (!erros.isEmpty()) {
            mostrarMensagemErro("Campos obrigatórios não preenchidos:\n\n" + String.join("\n", erros));
            return false;
        }
        
        return true;
    }
    
    /**
     * Mostra mensagem de sucesso
     */
    protected void mostrarMensagemSucesso(String mensagem) {
        JOptionPane.showMessageDialog(workArea, mensagem, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        notificar("success", "Sucesso", mensagem, "INFO");
    }
    
    /**
     * Mostra mensagem de erro
     */
    protected void mostrarMensagemErro(String mensagem) {
        JOptionPane.showMessageDialog(workArea, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
        notificar("error", "Erro", mensagem, "HIGH");
    }
    
    /**
     * Mostra mensagem de aviso
     */
    protected void mostrarMensagemAviso(String mensagem) {
        JOptionPane.showMessageDialog(workArea, mensagem, "Aviso", JOptionPane.WARNING_MESSAGE);
        notificar("warning", "Aviso", mensagem, "MEDIUM");
    }
    
    /**
     * Envia notificação usando o sistema de notificações
     */
    protected void notificar(String tipo, String titulo, String mensagem, String prioridade) {
        notificationSystem.notifyObservers(tipo, titulo, mensagem, prioridade);
    }
    
    /**
     * Obtém configuração do sistema
     */
    protected String getConfiguracao(String chave) {
        return configManager.getProperty(chave);
    }
    
    /**
     * Define configuração do sistema
     */
    protected void setConfiguracao(String chave, String valor) {
        configManager.setProperty(chave, valor);
    }
    
    /**
     * Executa comando usando Command Pattern
     */
    protected void executarComando(CommandPattern.Command comando) {
        commandInvoker.executeCommand(comando);
    }
    
    /**
     * Desfaz último comando
     */
    protected void desfazerComando() {
        commandInvoker.undo();
    }
    
    /**
     * Refaz último comando desfeito
     */
    protected void refazerComando() {
        commandInvoker.redo();
    }
    
    /**
     * Valida campos usando Validation Strategy
     */
    protected boolean validarCampos() {
        return validationContext.validate();
    }
    
    /**
     * Adiciona validador ao contexto
     */
    protected void adicionarValidador(ValidationStrategy.Validator validator) {
        validationContext.addValidator(validator);
    }
    
    /**
     * Métodos de ação padrão (podem ser sobrescritos)
     */
    protected void acaoSalvar() {
        SystemLogger.ui("Ação SALVAR não implementada para: " + formularioNome);
        mostrarMensagemAviso("Funcionalidade de salvar ainda não implementada.");
    }
    
    protected void acaoEditar() {
        SystemLogger.ui("Ação EDITAR não implementada para: " + formularioNome);
        mostrarMensagemAviso("Funcionalidade de editar ainda não implementada.");
    }
    
    protected void acaoExcluir() {
        SystemLogger.ui("Ação EXCLUIR não implementada para: " + formularioNome);
        mostrarMensagemAviso("Funcionalidade de excluir ainda não implementada.");
    }
    
    protected void acaoLimpar() {
        SystemLogger.ui("Ação LIMPAR não implementada para: " + formularioNome);
        mostrarMensagemAviso("Funcionalidade de limpar ainda não implementada.");
    }
    
    /**
     * Obtém DAO genérico para entidades
     */
    protected <T, K> DAOPattern.GenericDAO<T, K> getDAO(Class<T> entityClass) {
        return DAOPattern.DAOFactory.getDAO(entityClass);
    }
    
    /**
     * Limpa o workArea quando o formulário é fechado
     */
    public void limparWorkArea() {
        if (workArea != null) {
            workArea.removeAll();
            workArea.revalidate();
            workArea.repaint();
        }
        SystemLogger.ui("Formulário " + formularioNome + " limpo do workArea");
    }
    
    /**
     * Obtém informações do usuário atual
     */
    public String getUsuarioAtual() {
        return usuarioAtual;
    }
    
    public String getNomeUsuario() {
        return nomeUsuario;
    }
    
    public String getFormularioNome() {
        return formularioNome;
    }
    
    /**
     * Define informações do usuário
     */
    public void setUsuarioInfo(String usuarioAtual, String nomeUsuario) {
        this.usuarioAtual = usuarioAtual;
        this.nomeUsuario = nomeUsuario;
        SystemLogger.ui("Informações de usuário atualizadas para: " + usuarioAtual);
    }
    
    /**
     * Obtém painel de controle de comandos (Undo/Redo)
     */
    public JPanel getPainelControleComandos() {
        // TODO: Implementar quando CommandPattern estiver corrigido
        return new JPanel();
    }
    
    /**
     * Método utilitário para criar separadores visuais
     */
    protected JSeparator criarSeparador() {
        JSeparator separador = new JSeparator();
        separador.setForeground(Color.LIGHT_GRAY);
        return separador;
    }
    
    /**
     * Método utilitário para criar espaçamento
     */
    protected Component criarEspacamento(int largura, int altura) {
        return Box.createRigidArea(new Dimension(largura, altura));
    }
    
    /**
     * Finaliza o formulário
     */
    public void finalizar() {
        SystemLogger.ui("Finalizando formulário: " + formularioNome);
        notificar("system", "Formulário Finalizado", 
                "Formulário " + formularioNome + " foi finalizado", "INFO");
    }
}
