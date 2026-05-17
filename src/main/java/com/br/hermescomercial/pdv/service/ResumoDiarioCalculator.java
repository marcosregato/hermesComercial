package com.br.hermescomercial.pdv.service;

import com.br.hermescomercial.pdv.model.TransacaoDiaria;

import java.util.List;

/**
 * Serviço para cálculos do Resumo Diário
 * Extraído de PDVFormularioResumoDiario para melhor organização
 */
public class ResumoDiarioCalculator {
    
    /**
     * Calcula os totais do resumo diário
     */
    public ResumoDiarioTotais calcularTotais(List<TransacaoDiaria> transacoes) {
        double totalVendas = 0.0;
        double totalDevolucoes = 0.0;
        double totalDespesas = 0.0;
        int totalTransacoes = 0;
        int vendasCount = 0;
        
        for (TransacaoDiaria transacao : transacoes) {
            String valorStr = transacao.getValor().replace("R$ ", "").replace(",", ".");
            double valor = Double.parseDouble(valorStr);
            
            if ("Venda".equals(transacao.getTipo())) {
                totalVendas += valor;
                vendasCount++;
            } else if ("Devolução".equals(transacao.getTipo())) {
                totalDevolucoes += Math.abs(valor);
            } else if ("Despesa".equals(transacao.getTipo())) {
                totalDespesas += Math.abs(valor);
            }
            
            totalTransacoes++;
        }
        
        double saldoLiquido = totalVendas - totalDevolucoes - totalDespesas;
        double ticketMedio = vendasCount > 0 ? totalVendas / vendasCount : 0.0;
        
        return new ResumoDiarioTotais(totalVendas, totalDevolucoes, totalDespesas, 
                                       saldoLiquido, totalTransacoes, ticketMedio);
    }
    
    /**
     * Classe para representar os totais calculados
     */
    public static class ResumoDiarioTotais {
        private final double totalVendas;
        private final double totalDevolucoes;
        private final double totalDespesas;
        private final double saldoLiquido;
        private final int totalTransacoes;
        private final double ticketMedio;
        
        public ResumoDiarioTotais(double totalVendas, double totalDevolucoes, double totalDespesas,
                                  double saldoLiquido, int totalTransacoes, double ticketMedio) {
            this.totalVendas = totalVendas;
            this.totalDevolucoes = totalDevolucoes;
            this.totalDespesas = totalDespesas;
            this.saldoLiquido = saldoLiquido;
            this.totalTransacoes = totalTransacoes;
            this.ticketMedio = ticketMedio;
        }
        
        public double getTotalVendas() { return totalVendas; }
        public double getTotalDevolucoes() { return totalDevolucoes; }
        public double getTotalDespesas() { return totalDespesas; }
        public double getSaldoLiquido() { return saldoLiquido; }
        public int getTotalTransacoes() { return totalTransacoes; }
        public double getTicketMedio() { return ticketMedio; }
    }
}
