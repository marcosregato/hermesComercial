package com.br.hermescomercial.pdv.service;

import com.br.hermescomercial.pdv.model.FluxoCaixa;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Serviço para gerenciar dados de fluxo de caixa
 * Extraído de PDVFormularioFluxoCaixa para melhor organização
 */
public class FluxoCaixaService {
    
    /**
     * Adiciona dados de exemplo à tabela
     */
    public List<FluxoCaixa> adicionarDadosExemplo(DefaultTableModel modelTabela) {
        List<FluxoCaixa> fluxos = new ArrayList<>();
        
        // Limpar tabela atual
        modelTabela.setRowCount(0);
        
        // Dados de exemplo
        Object[][] dadosExemplo = {
            {"09/05/2026", "Venda #001", "Vendas", "Entrada", "R$ 150,00", "Dinheiro", "Concluído"},
            {"09/05/2026", "Pagamento Fornecedor A", "Compras", "Saída", "R$ 120,00", "Transferência", "Concluído"},
            {"09/05/2026", "Venda #002", "Vendas", "Entrada", "R$ 89,50", "Cartão", "Concluído"},
            {"08/05/2026", "Despesa Energia", "Despesas", "Saída", "R$ 450,00", "Boleto", "Pendente"},
            {"08/05/2026", "Venda #003", "Vendas", "Entrada", "R$ 234,75", "PIX", "Concluído"},
            {"07/05/2026", "Pagamento Salários", "Folha", "Saída", "R$ 5.000,00", "Transferência", "Concluído"},
            {"07/05/2026", "Venda #004", "Vendas", "Entrada", "R$ 67,80", "Dinheiro", "Concluído"}
        };
        
        for (Object[] dados : dadosExemplo) {
            modelTabela.addRow(dados);
            
            // Adicionar à lista de fluxos
            FluxoCaixa fluxo = new FluxoCaixa();
            fluxo.setData((String) dados[0]);
            fluxo.setDescricao((String) dados[1]);
            fluxo.setCategoria((String) dados[2]);
            fluxo.setTipo((String) dados[3]);
            fluxo.setValor((String) dados[4]);
            fluxo.setFormaPagamento((String) dados[5]);
            fluxo.setStatus((String) dados[6]);
            fluxos.add(fluxo);
        }
        
        return fluxos;
    }
    
    /**
     * Calcula os totais do fluxo de caixa
     */
    public FluxoCaixaTotais calcularTotais(List<FluxoCaixa> fluxos) {
        double totalEntradas = 0.0;
        double totalSaidas = 0.0;
        
        for (FluxoCaixa fluxo : fluxos) {
            String valorStr = fluxo.getValor().replace("R$ ", "").replace(".", "").replace(",", ".");
            double valor = Double.parseDouble(valorStr);
            
            if ("Entrada".equals(fluxo.getTipo())) {
                totalEntradas += valor;
            } else if ("Saída".equals(fluxo.getTipo())) {
                totalSaidas += valor;
            }
        }
        
        double saldoFinal = totalEntradas - totalSaidas;
        
        return new FluxoCaixaTotais(totalEntradas, totalSaidas, saldoFinal);
    }
    
    /**
     * Classe para representar os totais calculados
     */
    public static class FluxoCaixaTotais {
        private final double totalEntradas;
        private final double totalSaidas;
        private final double saldoFinal;
        
        public FluxoCaixaTotais(double totalEntradas, double totalSaidas, double saldoFinal) {
            this.totalEntradas = totalEntradas;
            this.totalSaidas = totalSaidas;
            this.saldoFinal = saldoFinal;
        }
        
        public double getTotalEntradas() { return totalEntradas; }
        public double getTotalSaidas() { return totalSaidas; }
        public double getSaldoFinal() { return saldoFinal; }
    }
}
