package com.br.hermescomercial.pdv.controller;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Controller principal do sistema PDV (versão simplificada)
 * Versão 2.0 - Interface principal do sistema
 * NOTA: Este controller foi substituído por PDVMenuLateralElegante
 */
public class PDVPrincipalSwingController {
    
    private JFrame mainFrame;
    private JLabel statusLabel;
    
    public PDVPrincipalSwingController() {
        initializeUI();
    }
    
    private void initializeUI() {
        mainFrame = new JFrame("Hermes Comercial PDV - Versão Antiga");
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainFrame.setSize(800, 600);
        mainFrame.setLocationRelativeTo(null);
        
        JPanel panel = new JPanel();
        panel.add(new JLabel("Este controller foi substituído por PDVMenuLateralElegante"));
        
        mainFrame.add(panel);
    }
    
    // Métodos de compatibilidade para referências antigas
    private void abrirSistemaConfig(ActionEvent e) {
        JOptionPane.showMessageDialog(mainFrame, 
            "Funcionalidade de Configurações migrada para o novo sistema!\n" +
            "Por favor, use o menu lateral: ⚙️ Configurações → ⚙️ Configurações Principais", 
            "Sistema Atualizado", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void abrirFornecedores(ActionEvent e) {
        JOptionPane.showMessageDialog(mainFrame, 
            "Funcionalidade de Fornecedores migrada para o novo sistema!\n" +
            "Por favor, use o menu lateral: 📦 Produtos → 🏭 Gestão Fornecedores", 
            "Sistema Atualizado", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void show() {
        mainFrame.setVisible(true);
    }
    
    public JFrame getFrame() {
        return mainFrame;
    }
}
