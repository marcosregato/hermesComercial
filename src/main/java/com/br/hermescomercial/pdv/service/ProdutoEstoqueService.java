package com.br.hermescomercial.pdv.service;

import com.br.hermescomercial.pdv.model.ProdutoEstoque;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Serviço para gerenciar dados de consulta de estoque
 * Extraído de PDVFormularioConsultarEstoque para melhor organização
 */
public class ProdutoEstoqueService {
    
    /**
     * Adiciona dados de exemplo à tabela
     */
    public List<ProdutoEstoque> adicionarDadosExemplo(DefaultTableModel modelTabela) {
        List<ProdutoEstoque> produtos = new ArrayList<>();
        
        // Limpar tabela atual
        modelTabela.setRowCount(0);
        
        // Dados de exemplo
        Object[][] dadosExemplo = {
            {"001", "Arroz Integral 5kg", "Alimentos", "Fornecedor A", "50", "R$ 25,00", "Disponível", "A1"},
            {"002", "Feijão Preto 1kg", "Alimentos", "Fornecedor A", "100", "R$ 8,50", "Disponível", "A1"},
            {"003", "Leite Integral 1L", "Laticínios", "Fornecedor B", "30", "R$ 4,50", "Baixo", "B2"},
            {"004", "Pão Francês kg", "Padaria", "Fornecedor C", "5", "R$ 12,00", "Baixo", "C1"},
            {"005", "Sabão em Pó 1kg", "Limpeza", "Fornecedor D", "25", "R$ 15,00", "Disponível", "D3"},
            {"006", "Detergente 500ml", "Limpeza", "Fornecedor D", "40", "R$ 2,50", "Disponível", "D3"},
            {"007", "Refrigerante 2L", "Bebidas", "Fornecedor E", "20", "R$ 7,00", "Disponível", "E1"}
        };
        
        for (Object[] dados : dadosExemplo) {
            modelTabela.addRow(dados);
            
            // Adicionar à lista de produtos
            ProdutoEstoque produto = new ProdutoEstoque();
            produto.setCodigo((String) dados[0]);
            produto.setProduto((String) dados[1]);
            produto.setCategoria((String) dados[2]);
            produto.setFornecedor((String) dados[3]);
            produto.setQuantidade((String) dados[4]);
            produto.setPreco((String) dados[5]);
            produto.setStatus((String) dados[6]);
            produto.setLocalizacao((String) dados[7]);
            produtos.add(produto);
        }
        
        return produtos;
    }
    
    /**
     * Realiza busca rápida de produtos
     */
    public List<ProdutoEstoque> buscarProdutosRapida(String termo, List<ProdutoEstoque> produtosEncontrados) {
        if (termo.isEmpty()) {
            return produtosEncontrados;
        }
        
        List<ProdutoEstoque> resultado = new ArrayList<>();
        String termoLower = termo.toLowerCase();
        
        for (ProdutoEstoque produto : produtosEncontrados) {
            if (produto.getCodigo().toLowerCase().contains(termoLower) ||
                produto.getProduto().toLowerCase().contains(termoLower) ||
                produto.getCategoria().toLowerCase().contains(termoLower)) {
                resultado.add(produto);
            }
        }
        
        return resultado;
    }
}
