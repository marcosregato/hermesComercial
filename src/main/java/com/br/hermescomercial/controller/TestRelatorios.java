package com.br.hermescomercial.controller;

import com.br.hermescomercial.util.LoggerUtil;
import javax.swing.SwingUtilities;

/**
 * Classe de teste para o PDVRelatoriosSwingController
 * Usada para diagnosticar erros na inicialização
 */
public class TestRelatorios {
    
    public static void main(String[] args) {
        LoggerUtil.initialize();
        
        SwingUtilities.invokeLater(() -> {
            try {
                LoggerUtil.info("Iniciando teste do PDVRelatoriosSwingController");
                PDVRelatoriosSwingController controller = new PDVRelatoriosSwingController();
                controller.show();
                LoggerUtil.info("PDVRelatoriosSwingController iniciado com sucesso no teste");
            } catch (Exception e) {
                LoggerUtil.error("Erro no teste do PDVRelatoriosSwingController", e);
                e.printStackTrace();
            }
        });
    }
}
