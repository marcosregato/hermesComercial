package com.br.hermescomercial.ui.layout;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Classe LayoutPadrao - Centralização de estilos visuais do sistema
 * Contém toda configuração de botões, campos, fontes e cores
 * @author Hermes Comercial
 * @version 2.8.3
 */
public class LayoutPadrao {
    
    // ===== CORES DO TEMA OCEANO =====
    public static final Color COR_PRIMARIA = new Color(41, 128, 185);        // Azul turquesa
    public static final Color COR_SECUNDARIA = new Color(52, 152, 219);      // Azul claro
    public static final Color COR_ACENTO = new Color(46, 204, 113);          // Verde
    public static final Color COR_PERIGO = new Color(231, 76, 60);           // Vermelho
    public static final Color COR_ALERTA = new Color(241, 196, 15);         // Amarelo
    public static final Color COR_SUCESSO = new Color(39, 174, 96);          // Verde escuro
    public static final Color COR_INFO = new Color(52, 152, 219);           // Azul info
    public static final Color COR_FUNDO = new Color(255, 255, 255);          // Branco
    public static final Color COR_FUNDO_ESCURO = new Color(245, 246, 250);   // Cinza claro
    public static final Color COR_TEXTO = new Color(52, 73, 94);             // Cinza escuro
    public static final Color COR_TEXTO_CLARO = new Color(149, 165, 166);    // Cinza médio
    public static final Color COR_BORDA = new Color(200, 200, 200);         // Cinza borda
    public static final Color COR_SOMBRA = new Color(0, 0, 0, 50);           // Sombra
    public static final Color COR_HOVER = new Color(230, 240, 250);          // Hover azul
    public static final Color COR_PRESSED = new Color(35, 115, 165);         // Pressed azul
    
    // ===== FONTES =====
    public static final Font FONTE_TITULO = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONTE_SUBTITULO = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONTE_TEXTO = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONTE_CAMPO = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONTE_BOTAO = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font FONTE_ROTULO = new Font("Segoe UI", Font.BOLD, 11);
    public static final Font FONTE_PEQUENA = new Font("Segoe UI", Font.PLAIN, 10);
    
    // ===== BORDAS =====
    public static final Border BORDA_PADRAO = BorderFactory.createLineBorder(COR_BORDA, 1);
    public static final Border BORDA_ARREDONDADA = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(COR_BORDA, 1),
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
    );
    public static final Border BORDA_CAMPO = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(COR_BORDA, 1),
        BorderFactory.createEmptyBorder(8, 12, 8, 12)
    );
    public static final Border BORDA_PAINEL = BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(COR_BORDA, 1),
            "",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            FONTE_ROTULO,
            COR_TEXTO
        ),
        BorderFactory.createEmptyBorder(15, 15, 15, 15)
    );
    
    // ===== MÉTODOS PARA CRIAÇÃO DE BOTÕES =====
    
    /**
     * Cria um botão primário com estilo padrão
     */
    public static JButton criarBotaoPrimario(String texto) {
        JButton botao = new JButton(texto);
        configurarBotao(botao, COR_PRIMARIA, COR_FUNDO, FONTE_BOTAO);
        return botao;
    }
    
    /**
     * Cria um botão secundário com estilo padrão
     */
    public static JButton criarBotaoSecundario(String texto) {
        JButton botao = new JButton(texto);
        configurarBotao(botao, COR_SECUNDARIA, COR_FUNDO, FONTE_BOTAO);
        return botao;
    }
    
    /**
     * Cria um botão de sucesso (verde)
     */
    public static JButton criarBotaoSucesso(String texto) {
        JButton botao = new JButton(texto);
        configurarBotao(botao, COR_SUCESSO, COR_FUNDO, FONTE_BOTAO);
        return botao;
    }
    
    /**
     * Cria um botão de perigo (vermelho)
     */
    public static JButton criarBotaoPerigo(String texto) {
        JButton botao = new JButton(texto);
        configurarBotao(botao, COR_PERIGO, COR_FUNDO, FONTE_BOTAO);
        return botao;
    }
    
    /**
     * Cria um botão de alerta (amarelo)
     */
    public static JButton criarBotaoAlerta(String texto) {
        JButton botao = new JButton(texto);
        configurarBotao(botao, COR_ALERTA, Color.BLACK, FONTE_BOTAO);
        return botao;
    }
    
    /**
     * Configura um botão com cores e efeitos
     */
    private static void configurarBotao(JButton botao, Color corFundo, Color corTexto, Font fonte) {
        botao.setFont(fonte);
        botao.setBackground(corFundo);
        botao.setForeground(corTexto);
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
        botao.setOpaque(true);
        botao.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botao.setBorder(BORDA_ARREDONDADA);
        
        // Efeito hover
        botao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botao.setBackground(corFundo.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                botao.setBackground(corFundo);
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                botao.setBackground(corFundo.darker());
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                botao.setBackground(corFundo.brighter());
            }
        });
    }
    
    // ===== MÉTODOS PARA CRIAÇÃO DE CAMPOS =====
    
    /**
     * Cria um campo de texto com estilo padrão
     */
    public static JTextField criarCampoTexto(int colunas) {
        JTextField campo = new JTextField(colunas);
        configurarCampoTexto(campo);
        return campo;
    }
    
    /**
     * Cria um campo de senha com estilo padrão
     */
    public static JPasswordField criarCampoSenha(int colunas) {
        JPasswordField campo = new JPasswordField(colunas);
        configurarCampoTexto(campo);
        return campo;
    }
    
    /**
     * Cria uma área de texto com estilo padrão
     */
    public static JTextArea criarAreaTexto(int linhas, int colunas) {
        JTextArea area = new JTextArea(linhas, colunas);
        area.setFont(FONTE_CAMPO);
        area.setForeground(COR_TEXTO);
        area.setBackground(COR_FUNDO);
        area.setBorder(BORDA_CAMPO);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        return area;
    }
    
    /**
     * Configura campo de texto com estilo padrão
     */
    private static void configurarCampoTexto(JTextField campo) {
        campo.setFont(FONTE_CAMPO);
        campo.setForeground(COR_TEXTO);
        campo.setBackground(COR_FUNDO);
        campo.setBorder(BORDA_CAMPO);
        campo.setCaretColor(COR_TEXTO);
        campo.setSelectionColor(COR_PRIMARIA);
        campo.setSelectedTextColor(COR_FUNDO);
    }
    
    // ===== MÉTODOS PARA CRIAÇÃO DE RÓTULOS =====
    
    /**
     * Cria um rótulo de título
     */
    public static JLabel criarRotuloTitulo(String texto) {
        JLabel rotulo = new JLabel(texto);
        rotulo.setFont(FONTE_TITULO);
        rotulo.setForeground(COR_PRIMARIA);
        return rotulo;
    }
    
    /**
     * Cria um rótulo de subtítulo
     */
    public static JLabel criarRotuloSubtitulo(String texto) {
        JLabel rotulo = new JLabel(texto);
        rotulo.setFont(FONTE_SUBTITULO);
        rotulo.setForeground(COR_TEXTO);
        return rotulo;
    }
    
    /**
     * Cria um rótulo de texto normal
     */
    public static JLabel criarRotuloTexto(String texto) {
        JLabel rotulo = new JLabel(texto);
        rotulo.setFont(FONTE_TEXTO);
        rotulo.setForeground(COR_TEXTO);
        return rotulo;
    }
    
    /**
     * Cria um rótulo de campo
     */
    public static JLabel criarRotuloCampo(String texto) {
        JLabel rotulo = new JLabel(texto);
        rotulo.setFont(FONTE_ROTULO);
        rotulo.setForeground(COR_TEXTO);
        return rotulo;
    }
    
    // ===== MÉTODOS PARA CRIAÇÃO DE PAINÉIS =====
    
    /**
     * Cria um painel com fundo branco
     */
    public static JPanel criarPainelBranco() {
        JPanel painel = new JPanel();
        painel.setBackground(COR_FUNDO);
        return painel;
    }
    
    /**
     * Cria um painel com fundo cinza claro
     */
    public static JPanel criarPainelCinza() {
        JPanel painel = new JPanel();
        painel.setBackground(COR_FUNDO_ESCURO);
        return painel;
    }
    
    /**
     * Cria um painel com borda e título
     */
    public static JPanel criarPainelComBorda(String titulo) {
        JPanel painel = new JPanel();
        painel.setBackground(COR_FUNDO);
        painel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COR_BORDA, 1),
                titulo,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                FONTE_ROTULO,
                COR_TEXTO
            ),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        return painel;
    }
    
    // ===== MÉTODOS PARA CRIAÇÃO DE TABELAS =====
    
    /**
     * Cria uma tabela com estilo padrão
     */
    public static JTable criarTabela() {
        JTable tabela = new JTable();
        configurarTabela(tabela);
        return tabela;
    }
    
    /**
     * Configura tabela com estilo padrão
     */
    private static void configurarTabela(JTable tabela) {
        tabela.setFont(FONTE_TEXTO);
        tabela.setForeground(COR_TEXTO);
        tabela.setBackground(COR_FUNDO);
        tabela.setGridColor(COR_BORDA);
        tabela.setSelectionBackground(COR_HOVER);
        tabela.setSelectionForeground(COR_TEXTO);
        tabela.setRowHeight(25);
        tabela.getTableHeader().setFont(FONTE_ROTULO);
        tabela.getTableHeader().setBackground(COR_FUNDO_ESCURO);
        tabela.getTableHeader().setForeground(COR_TEXTO);
        tabela.getTableHeader().setBorder(BORDA_PADRAO);
    }
    
    // ===== MÉTODOS PARA CRIAÇÃO DE COMBO BOX =====
    
    /**
     * Cria um combo box com estilo padrão
     */
    public static <T> JComboBox<T> criarComboBox(T[] itens) {
        JComboBox<T> combo = new JComboBox<>(itens);
        combo.setFont(FONTE_CAMPO);
        combo.setForeground(COR_TEXTO);
        combo.setBackground(COR_FUNDO);
        combo.setBorder(BORDA_CAMPO);
        return combo;
    }
    
    // ===== MÉTODOS PARA CRIAÇÃO DE CHECK BOX =====
    
    /**
     * Cria um check box com estilo padrão
     */
    public static JCheckBox criarCheckBox(String texto) {
        JCheckBox check = new JCheckBox(texto);
        check.setFont(FONTE_TEXTO);
        check.setForeground(COR_TEXTO);
        check.setBackground(COR_FUNDO);
        check.setOpaque(true);
        return check;
    }
    
    // ===== MÉTODOS PARA CRIAÇÃO DE RADIO BUTTON =====
    
    /**
     * Cria um radio button com estilo padrão
     */
    public static JRadioButton criarRadioButton(String texto) {
        JRadioButton radio = new JRadioButton(texto);
        radio.setFont(FONTE_TEXTO);
        radio.setForeground(COR_TEXTO);
        radio.setBackground(COR_FUNDO);
        radio.setOpaque(true);
        return radio;
    }
    
    // ===== MÉTODOS PARA CRIAÇÃO DE BARRAS DE ROLAGEM =====
    
    /**
     * Cria uma barra de rolagem com estilo padrão
     */
    public static JScrollPane criarBarraRolagem(Component componente) {
        JScrollPane scroll = new JScrollPane(componente);
        scroll.setBackground(COR_FUNDO);
        scroll.setBorder(BORDA_PADRAO);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getHorizontalScrollBar().setUnitIncrement(16);
        return scroll;
    }
    
    // ===== MÉTODOS PARA CRIAÇÃO DE SEPARADORES =====
    
    /**
     * Cria um separador horizontal
     */
    public static JSeparator criarSeparadorHorizontal() {
        JSeparator separador = new JSeparator(SwingConstants.HORIZONTAL);
        separador.setForeground(COR_BORDA);
        separador.setBackground(COR_BORDA);
        return separador;
    }
    
    /**
     * Cria um separador vertical
     */
    public static JSeparator criarSeparadorVertical() {
        JSeparator separador = new JSeparator(SwingConstants.VERTICAL);
        separador.setForeground(COR_BORDA);
        separador.setBackground(COR_BORDA);
        return separador;
    }
    
    // ===== MÉTODOS DE LAYOUT =====
    
    /**
     * Cria um painel com espaçamento padrão
     */
    public static JPanel criarPainelComEspacamento(int espacamento) {
        JPanel painel = new JPanel(new BorderLayout(espacimento, espacimento));
        painel.setBackground(COR_FUNDO);
        return painel;
    }
    
    /**
     * Cria um painel com margem padrão
     */
    public static JPanel criarPainelComMargem(int margem) {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(COR_FUNDO);
        painel.setBorder(BorderFactory.createEmptyBorder(margem, margem, margem, margem));
        return painel;
    }
    
    // ===== MÉTODOS DE VALIDAÇÃO VISUAL =====
    
    /**
     * Aplica estilo de erro a um campo
     */
    public static void aplicarEstiloErro(JComponent componente) {
        componente.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COR_PERIGO, 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }
    
    /**
     * Aplica estilo de sucesso a um campo
     */
    public static void aplicarEstiloSucesso(JComponent componente) {
        componente.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COR_SUCESSO, 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }
    
    /**
     * Remove estilos de validação de um campo
     */
    public static void removerEstiloValidacao(JComponent componente) {
        if (componente instanceof JTextField || componente instanceof JPasswordField) {
            componente.setBorder(BORDA_CAMPO);
        } else {
            componente.setBorder(BORDA_PADRAO);
        }
    }
}
