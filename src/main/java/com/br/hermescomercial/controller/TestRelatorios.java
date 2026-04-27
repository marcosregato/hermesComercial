package com.br.hermescomercial.controller;

import javax.swing.SwingUtilities;

/**
 * Classe de teste para o PDVRelatoriosSwingController
 * Usada para diagnosticar erros na inicialização
 */
public class TestRelatorios {
    
    public static void main(String[] args) {
        // System.out.initialize(); // Método não existe
        
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("Iniciando teste do PDVRelatoriosSwingController");
                PDVRelatoriosSwingController controller = new PDVRelatoriosSwingController();
                controller.show();
                System.out.println("PDVRelatoriosSwingController iniciado com sucesso no teste");
            } catch (Exception e) {
                System.err.println("Erro no teste do PDVRelatoriosSwingController: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
