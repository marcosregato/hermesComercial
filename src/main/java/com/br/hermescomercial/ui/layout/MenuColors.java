package com.br.hermescomercial.ui.layout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Paleta de cores específica para headers de menus por setor
 * Implementa benefícios de UX com cores diferenciadas para facilitar navegação
 */
public class MenuColors {
    
    // ===== CORES PRIMÁRIAS =====
    public static final Color COR_HEADER_PDV = new Color(41, 128, 185);       // Azul turquesa (PDV)
    public static final Color COR_HEADER_ERP = new Color(142, 68, 173);        // Roxo (ERP)
    public static final Color COR_HEADER_FINANCEIRO = new Color(34, 139, 34);  // Verde (Financeiro)
    public static final Color COR_HEADER_CLIENTES = new Color(230, 126, 34);   // Laranja (Clientes)
    public static final Color COR_HEADER_PRODUTOS = new Color(33, 150, 243);   // Azul claro (Produtos)
    public static final Color COR_HEADER_VENDAS = new Color(156, 39, 176);   // Roxo escuro (Vendas)
    public static final Color COR_HEADER_RELATORIOS = new Color(108, 117, 125); // Cinza (Relatórios)
    public static final Color COR_HEADER_CONFIG = new Color(33, 37, 41);    // Preto (Configurações)
    
    // ===== CORES DE TEXTO =====
    public static final Color COR_TEXTO_HEADER = Color.WHITE;                  // Texto branco para headers
    public static final Color COR_TEXTO_MENU = new Color(52, 73, 94);     // Cinza escuro para menus
    public static final Color COR_TEXTO_HOVER = new Color(230, 240, 250);   // Hover para interação
    
    // ===== CORES DE DESTAQUE =====
    public static final Color COR_DESTAQUE_ATIVO = new Color(255, 235, 59);  // Amarelo suave
    public static final Color COR_DESTAQUE_INATIVO = new Color(200, 200, 200); // Cinza claro
    
    // ===== CORES DE ÍCONES =====
    public static final Color COR_ICONE_FINANCEIRO = new Color(46, 204, 113); // Verde
    public static final Color COR_ICONE_PRODUTOS = new Color(33, 150, 243);   // Azul
    public static final Color COR_ICONE_CLIENTES = new Color(230, 126, 34);   // Laranja
    public static final Color COR_ICONE_VENDAS = new Color(156, 39, 176);   // Roxo
    public static final Color COR_ICONE_RELATORIOS = new Color(108, 117, 125); // Cinza
    public static final Color COR_ICONE_CONFIG = new Color(33, 37, 41);    // Preto
    
    /**
     * Obtém cor do header baseada no setor
     * @param setor Nome do setor (PDV, ERP, FINANCEIRO, etc.)
     * @return Cor específica para o header do setor
     */
    public static Color getCorHeader(String setor) {
        switch (setor.toLowerCase()) {
            case "pdv":
                return COR_HEADER_PDV;
            case "erp":
                return COR_HEADER_ERP;
            case "financeiro":
                return COR_HEADER_FINANCEIRO;
            case "clientes":
                return COR_HEADER_CLIENTES;
            case "produtos":
                return COR_HEADER_PRODUTOS;
            case "vendas":
                return COR_HEADER_VENDAS;
            case "relatórios":
                return COR_HEADER_RELATORIOS;
            case "config":
                return COR_HEADER_CONFIG;
            default:
                return COR_HEADER_ERP; // Default
        }
    }
    
    /**
     * Obtém cor do ícone baseada no setor
     * @param setor Nome do setor
     * @return Cor específica para o ícone do setor
     */
    public static Color getCorIcone(String setor) {
        switch (setor.toLowerCase()) {
            case "financeiro":
                return COR_ICONE_FINANCEIRO;
            case "produtos":
                return COR_ICONE_PRODUTOS;
            case "clientes":
                return COR_ICONE_CLIENTES;
            case "vendas":
                return COR_ICONE_VENDAS;
            case "relatórios":
                return COR_ICONE_RELATORIOS;
            case "config":
                return COR_ICONE_CONFIG;
            default:
                return COR_ICONE_FINANCEIRO; // Default
        }
    }
    
    /**
     * Obtém nome formatado do setor para exibição
     * @param setor Nome do setor
     * @return Nome formatado com ícone
     */
    public static String getNomeSetorFormatado(String setor) {
        String nomeSetor = setor.substring(0, 1).toUpperCase() + setor.substring(1).toLowerCase();
        
        switch (setor.toLowerCase()) {
            case "financeiro":
                return "💰 " + nomeSetor;
            case "produtos":
                return "📦 " + nomeSetor;
            case "clientes":
                return "👥 " + nomeSetor;
            case "vendas":
                return "💵 " + nomeSetor;
            case "relatórios":
                return "📊 " + nomeSetor;
            case "config":
                return "⚙️ " + nomeSetor;
            default:
                return "🏢 " + nomeSetor;
        }
    }
    
    /**
     * Cria um JLabel com estilo de header para menu
     * @param setor Nome do setor
     * @param texto Texto do header
     * @return JLabel formatado com cores do setor
     */
    public static JLabel criarHeaderMenu(String setor, String texto) {
        JLabel header = new JLabel(texto);
        header.setFont(LayoutPadrao.FONTE_TITULO);
        header.setForeground(COR_TEXTO_HEADER);
        header.setBackground(getCorHeader(setor));
        header.setOpaque(true);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10),
            BorderFactory.createLineBorder(LayoutPadrao.COR_BORDA, 1)
        ));
        header.setPreferredSize(new Dimension(600, 40));
        
        // Adicionar ícone do setor
        String textoComIcone = getNomeSetorFormatado(setor) + " " + texto;
        header.setText(textoComIcone);
        
        return header;
    }
    
    /**
     * Cria um painel de menu com cores específicas do setor
     * @param setor Nome do setor
     * @return JPanel formatado com cores do setor
     */
    public static JPanel criarPainelMenuSetor(String setor) {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(LayoutPadrao.COR_FUNDO_ESCURO);
        painel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 5, 5, 5),
            BorderFactory.createLineBorder(getCorHeader(setor), 2)
        ));
        
        return painel;
    }
    
    /**
     * Cria um botão com cores específicas do setor
     * @param setor Nome do setor
     * @param texto Texto do botão
     * @return JButton formatado com cores do setor
     */
    public static JButton criarBotaoSetor(String setor, String texto) {
        JButton botao = new JButton(texto);
        botao.setFont(LayoutPadrao.FONTE_BOTAO);
        botao.setForeground(COR_TEXTO_MENU);
        botao.setBackground(getCorHeader(setor));
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(getCorHeader(setor), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Efeito hover
        botao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                botao.setBackground(COR_DESTAQUE_ATIVO);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                botao.setBackground(getCorHeader(setor));
            }
        });
        
        return botao;
    }
}
