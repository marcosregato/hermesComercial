package com.br.hermescomercial.pdv.service;

import com.br.hermescomercial.pdv.model.VendaConsulta;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Serviço para gerenciar dados de consulta de vendas
 * Extraído de PDVFormularioConsultarVendas para melhor organização
 */
public class VendaConsultaService {
    
    /**
     * Adiciona dados de exemplo à tabela
     */
    public List<VendaConsulta> adicionarDadosExemplo(DefaultTableModel modelTabela) {
        List<VendaConsulta> vendas = new ArrayList<>();
        
        // Limpar tabela atual
        modelTabela.setRowCount(0);
        
        // Dados de exemplo
        Object[][] dadosExemplo = {
            {"001", "09/05/2026", "14:30", "João Silva", "123.456.789-00", "Maria Santos", "Balcão", "3 itens", "R$ 150,00", "Ativa", "Dinheiro", "👁️"},
            {"002", "09/05/2026", "10:15", "Maria Oliveira", "987.654.321-00", "João Pedro", "Delivery", "2 itens", "R$ 89,50", "Ativa", "Cartão", "👁️"},
            {"003", "08/05/2026", "16:45", "Carlos Alberto", "456.789.123-00", "Ana Maria", "Televendas", "5 itens", "R$ 234,75", "Finalizada", "PIX", "👁️"},
            {"004", "08/05/2026", "09:20", "Fernanda Costa", "789.123.456-00", "Pedro Henrique", "E-commerce", "1 item", "R$ 67,80", "Cancelada", "Boleto", "👁️"},
            {"005", "07/05/2026", "13:55", "Roberto Dias", "321.654.987-00", "Luciana Silva", "Atacado", "8 itens", "R$ 445,00", "Finalizada", "Cartão", "👁️"}
        };
        
        for (Object[] dados : dadosExemplo) {
            modelTabela.addRow(dados);
            
            // Adicionar à lista de vendas
            VendaConsulta venda = new VendaConsulta();
            venda.setNumero((String) dados[0]);
            venda.setData((String) dados[1]);
            venda.setCliente((String) dados[2]);
            venda.setCpf((String) dados[3]);
            venda.setVendedor((String) dados[4]);
            venda.setValorTotal((String) dados[5]);
            venda.setStatus((String) dados[6]);
            venda.setFormaPagamento((String) dados[7]);
            vendas.add(venda);
        }
        
        return vendas;
    }
    
    /**
     * Realiza busca rápida de vendas
     */
    public List<VendaConsulta> buscarVendasRapida(String termo, List<VendaConsulta> vendasEncontradas) {
        if (termo.isEmpty()) {
            return vendasEncontradas;
        }
        
        List<VendaConsulta> resultado = new ArrayList<>();
        String termoLower = termo.toLowerCase();
        
        for (VendaConsulta venda : vendasEncontradas) {
            if (venda.getNumero().toLowerCase().contains(termoLower) ||
                venda.getCliente().toLowerCase().contains(termoLower) ||
                venda.getCpf().toLowerCase().contains(termoLower)) {
                resultado.add(venda);
            }
        }
        
        return resultado;
    }
}
