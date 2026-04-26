package com.br.hermescomercial;

import javax.swing.*;
import java.awt.*;

/**
 * Classe principal da aplicação Hermes Comercial PDV
 * Versão SWING - Interface nativa Java
 */
public class MainApplication {
    
    public static void main(String[] args) {
        // Usar Look and Feel padrão (sem configuração para evitar erros)
        
        // Executar na thread de UI
        SwingUtilities.invokeLater(() -> {
            try {
                // Criar e exibir a janela principal
                JFrame frame = new JFrame("Hermes Comercial PDV");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(1200, 800);
                frame.setLocationRelativeTo(null);
                
                // Painel principal
                JPanel mainPanel = new JPanel(new BorderLayout());
                mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                
                // Título
                JLabel titleLabel = new JLabel("Hermes Comercial PDV", SwingConstants.CENTER);
                titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
                mainPanel.add(titleLabel, BorderLayout.NORTH);
                
                // Painel central com informações
                JPanel centerPanel = new JPanel(new GridLayout(0, 1, 10, 10));
                centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
                
                JLabel infoLabel = new JLabel("<html><center>" +
                    "<h2>Bem-vindo ao Hermes Comercial PDV</h2>" +
                    "<p>Versão: 1.2.0-SWING</p>" +
                    "<p>Interface: SWING (Java nativo)</p>" +
                    "<p>Status: Sistema 100% SWING - Sem JavaFX</p>" +
                    "<br>" +
                    "<p><b>Funcionalidades Disponíveis:</b></p>" +
                    "<ul>" +
                    "<li>• Gestão de Vendas</li>" +
                    "<li>• Controle de Estoque</li>" +
                    "<li>• Relatórios Financeiros</li>" +
                    "<li>• Configurações do Sistema</li>" +
                    "</ul>" +
                    "</center></html>", SwingConstants.CENTER);
                
                centerPanel.add(infoLabel);
                mainPanel.add(centerPanel, BorderLayout.CENTER);
                
                // Painel de botões
                JPanel buttonPanel = new JPanel(new FlowLayout());
                
                JButton btnVendas = new JButton("Vendas");
                JButton btnEstoque = new JButton("Estoque");
                JButton btnRelatorios = new JButton("Relatórios");
                JButton btnConfig = new JButton("Configurações");
                JButton btnSair = new JButton("Sair");
                
                btnVendas.addActionListener(e -> JOptionPane.showMessageDialog(frame, 
                    "Módulo de Vendas\n\nFuncionalidade em desenvolvimento.", 
                    "Vendas", JOptionPane.INFORMATION_MESSAGE));
                    
                btnEstoque.addActionListener(e -> JOptionPane.showMessageDialog(frame, 
                    "Módulo de Estoque\n\nFuncionalidade em desenvolvimento.", 
                    "Estoque", JOptionPane.INFORMATION_MESSAGE));
                    
                btnRelatorios.addActionListener(e -> JOptionPane.showMessageDialog(frame, 
                    "Módulo de Relatórios\n\nFuncionalidade em desenvolvimento.", 
                    "Relatórios", JOptionPane.INFORMATION_MESSAGE));
                    
                btnConfig.addActionListener(e -> JOptionPane.showMessageDialog(frame, 
                    "Módulo de Configurações\n\nFuncionalidade em desenvolvimento.", 
                    "Configurações", JOptionPane.INFORMATION_MESSAGE));
                    
                btnSair.addActionListener(e -> System.exit(0));
                
                buttonPanel.add(btnVendas);
                buttonPanel.add(btnEstoque);
                buttonPanel.add(btnRelatorios);
                buttonPanel.add(btnConfig);
                buttonPanel.add(btnSair);
                
                mainPanel.add(buttonPanel, BorderLayout.SOUTH);
                
                frame.add(mainPanel);
                frame.setVisible(true);
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Erro ao iniciar a aplicação: " + e.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}
