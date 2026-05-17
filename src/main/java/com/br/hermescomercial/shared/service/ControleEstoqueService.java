package com.br.hermescomercial.shared.service;

import com.br.hermescomercial.shared.model.MetodoControleEstoque;
import com.br.hermescomercial.shared.model.Produto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Serviço de Controle de Estoque
 * Implementa os métodos PEPS, UEPS e Custo Médio para avaliação de custos
 */
public class ControleEstoqueService {
    
    private static final Logger logger = LogManager.getLogger(ControleEstoqueService.class);
    
    /**
     * Representa um lote de produto com seu custo e data de entrada
     */
    public static class LoteEstoque {
        private String lote;
        private LocalDateTime dataEntrada;
        private int quantidade;
        private BigDecimal custoUnitario;
        private BigDecimal valorTotal;
        
        public LoteEstoque(String lote, LocalDateTime dataEntrada, int quantidade, BigDecimal custoUnitario) {
            this.lote = lote;
            this.dataEntrada = dataEntrada;
            this.quantidade = quantidade;
            this.custoUnitario = custoUnitario;
            this.valorTotal = custoUnitario.multiply(new BigDecimal(quantidade));
        }
        
        // Getters e Setters
        public String getLote() { return lote; }
        public void setLote(String lote) { this.lote = lote; }
        
        public LocalDateTime getDataEntrada() { return dataEntrada; }
        public void setDataEntrada(LocalDateTime dataEntrada) { this.dataEntrada = dataEntrada; }
        
        public int getQuantidade() { return quantidade; }
        public void setQuantidade(int quantidade) { 
            this.quantidade = quantidade;
            this.valorTotal = custoUnitario.multiply(new BigDecimal(quantidade));
        }
        
        public BigDecimal getCustoUnitario() { return custoUnitario; }
        public void setCustoUnitario(BigDecimal custoUnitario) { 
            this.custoUnitario = custoUnitario;
            this.valorTotal = custoUnitario.multiply(new BigDecimal(quantidade));
        }
        
        public BigDecimal getValorTotal() { return valorTotal; }
        
        @Override
        public String toString() {
            return String.format("Lote: %s | Qtd: %d | Custo: R$ %.2f | Data: %s", 
                    lote, quantidade, custoUnitario, dataEntrada);
        }
    }
    
    /**
     * Resultado de uma operação de saída de estoque
     */
    public static class ResultadoSaidaEstoque {
        private BigDecimal custoTotalSaida;
        private BigDecimal custoUnitarioMedio;
        private List<LoteEstoque> lotesUtilizados;
        private String descricaoOperacao;
        
        public ResultadoSaidaEstoque(BigDecimal custoTotalSaida, BigDecimal custoUnitarioMedio, 
                                    List<LoteEstoque> lotesUtilizados, String descricaoOperacao) {
            this.custoTotalSaida = custoTotalSaida;
            this.custoUnitarioMedio = custoUnitarioMedio;
            this.lotesUtilizados = new ArrayList<>(lotesUtilizados);
            this.descricaoOperacao = descricaoOperacao;
        }
        
        // Getters
        public BigDecimal getCustoTotalSaida() { return custoTotalSaida; }
        public BigDecimal getCustoUnitarioMedio() { return custoUnitarioMedio; }
        public List<LoteEstoque> getLotesUtilizados() { return new ArrayList<>(lotesUtilizados); }
        public String getDescricaoOperacao() { return descricaoOperacao; }
    }
    
    /**
     * Adiciona produtos ao estoque usando o método de controle especificado
     * @param produto Produto a ser adicionado
     * @param quantidade Quantidade a ser adicionada
     * @param custoUnitario Custo unitário dos produtos
     * @param lote Número do lote
     * @param lotesExistentes Lista de lotes existentes no estoque
     * @return Lista atualizada de lotes
     */
    public List<LoteEstoque> adicionarAoEstoque(Produto produto, int quantidade, 
                                             BigDecimal custoUnitario, String lote,
                                             List<LoteEstoque> lotesExistentes) {
        logger.info("Adicionando {} unidades do produto {} ao estoque - Método: {}", 
                   quantidade, produto.getNome(), produto.getMetodoControleEstoque());
        
        List<LoteEstoque> lotes = lotesExistentes != null ? new ArrayList<>(lotesExistentes) : new ArrayList<>();
        
        // Criar novo lote
        LoteEstoque novoLote = new LoteEstoque(
            lote != null ? lote : "LOT" + System.currentTimeMillis(),
            LocalDateTime.now(),
            quantidade,
            custoUnitario
        );
        
        lotes.add(novoLote);
        
        logger.info("Lote adicionado: {}", novoLote);
        return lotes;
    }
    
    /**
     * Remove produtos do estoque usando o método PEPS (Primeiro que Entra, Primeiro que Sai)
     * @param produto Produto a ser removido
     * @param quantidade Quantidade a ser removida
     * @param lotes Lista de lotes disponíveis
     * @return Resultado da operação com custo calculado
     */
    public ResultadoSaidaEstoque removerDoEstoquePEPS(Produto produto, int quantidade, List<LoteEstoque> lotes) {
        logger.info("Removendo {} unidades do produto {} usando método PEPS", quantidade, produto.getNome());
        
        if (lotes == null || lotes.isEmpty()) {
            throw new RuntimeException("Não há lotes disponíveis no estoque");
        }
        
        // Ordenar lotes por data de entrada (mais antigos primeiro)
        List<LoteEstoque> lotesOrdenados = new ArrayList<>(lotes);
        lotesOrdenados.sort(Comparator.comparing(LoteEstoque::getDataEntrada));
        
        BigDecimal custoTotal = BigDecimal.ZERO;
        List<LoteEstoque> lotesUtilizados = new ArrayList<>();
        int quantidadeRemovida = 0;
        
        for (LoteEstoque lote : lotesOrdenados) {
            if (quantidadeRemovida >= quantidade) break;
            
            int quantidadeDoLote = Math.min(lote.getQuantidade(), quantidade - quantidadeRemovida);
            
            BigDecimal custoDoLote = lote.getCustoUnitario().multiply(new BigDecimal(quantidadeDoLote));
            custoTotal = custoTotal.add(custoDoLote);
            
            // Atualizar quantidade do lote
            lote.setQuantidade(lote.getQuantidade() - quantidadeDoLote);
            
            // Adicionar lote utilizado ao resultado
            LoteEstoque loteUtilizado = new LoteEstoque(
                lote.getLote(), lote.getDataEntrada(), quantidadeDoLote, lote.getCustoUnitario()
            );
            lotesUtilizados.add(loteUtilizado);
            
            quantidadeRemovida += quantidadeDoLote;
            
            logger.debug("PEPS: Removido {} do lote {} - Custo: R$ {}", 
                        quantidadeDoLote, lote.getLote(), custoDoLote);
        }
        
        if (quantidadeRemovida < quantidade) {
            throw new RuntimeException(String.format(
                "Estoque insuficiente. Disponível: %d, Solicitado: %d", quantidadeRemovida, quantidade));
        }
        
        BigDecimal custoUnitarioMedio = custoTotal.divide(new BigDecimal(quantidade), 4, RoundingMode.HALF_UP);
        
        // Remover lotes com quantidade zero
        lotes.removeIf(lote -> lote.getQuantidade() <= 0);
        
        ResultadoSaidaEstoque resultado = new ResultadoSaidaEstoque(
            custoTotal, custoUnitarioMedio, lotesUtilizados, "PEPS - Primeiro que Entra, Primeiro que Sai"
        );
        
        logger.info("PEPS: Custo total da saída: R$ {} | Custo unitário médio: R$ {}", 
                   custoTotal, custoUnitarioMedio);
        
        return resultado;
    }
    
    /**
     * Remove produtos do estoque usando o método UEPS (Último que Entra, Primeiro que Sai)
     * @param produto Produto a ser removido
     * @param quantidade Quantidade a ser removida
     * @param lotes Lista de lotes disponíveis
     * @return Resultado da operação com custo calculado
     */
    public ResultadoSaidaEstoque removerDoEstoqueUEPS(Produto produto, int quantidade, List<LoteEstoque> lotes) {
        logger.info("Removendo {} unidades do produto {} usando método UEPS", quantidade, produto.getNome());
        
        if (lotes == null || lotes.isEmpty()) {
            throw new RuntimeException("Não há lotes disponíveis no estoque");
        }
        
        // Ordenar lotes por data de entrada (mais recentes primeiro)
        List<LoteEstoque> lotesOrdenados = new ArrayList<>(lotes);
        lotesOrdenados.sort((l1, l2) -> l2.getDataEntrada().compareTo(l1.getDataEntrada()));
        
        BigDecimal custoTotal = BigDecimal.ZERO;
        List<LoteEstoque> lotesUtilizados = new ArrayList<>();
        int quantidadeRemovida = 0;
        
        for (LoteEstoque lote : lotesOrdenados) {
            if (quantidadeRemovida >= quantidade) break;
            
            int quantidadeDoLote = Math.min(lote.getQuantidade(), quantidade - quantidadeRemovida);
            
            BigDecimal custoDoLote = lote.getCustoUnitario().multiply(new BigDecimal(quantidadeDoLote));
            custoTotal = custoTotal.add(custoDoLote);
            
            // Atualizar quantidade do lote
            lote.setQuantidade(lote.getQuantidade() - quantidadeDoLote);
            
            // Adicionar lote utilizado ao resultado
            LoteEstoque loteUtilizado = new LoteEstoque(
                lote.getLote(), lote.getDataEntrada(), quantidadeDoLote, lote.getCustoUnitario()
            );
            lotesUtilizados.add(loteUtilizado);
            
            quantidadeRemovida += quantidadeDoLote;
            
            logger.debug("UEPS: Removido {} do lote {} - Custo: R$ {}", 
                        quantidadeDoLote, lote.getLote(), custoDoLote);
        }
        
        if (quantidadeRemovida < quantidade) {
            throw new RuntimeException(String.format(
                "Estoque insuficiente. Disponível: %d, Solicitado: %d", quantidadeRemovida, quantidade));
        }
        
        BigDecimal custoUnitarioMedio = custoTotal.divide(new BigDecimal(quantidade), 4, RoundingMode.HALF_UP);
        
        // Remover lotes com quantidade zero
        lotes.removeIf(lote -> lote.getQuantidade() <= 0);
        
        ResultadoSaidaEstoque resultado = new ResultadoSaidaEstoque(
            custoTotal, custoUnitarioMedio, lotesUtilizados, "UEPS - Último que Entra, Primeiro que Sai"
        );
        
        logger.info("UEPS: Custo total da saída: R$ {} | Custo unitário médio: R$ {}", 
                   custoTotal, custoUnitarioMedio);
        
        return resultado;
    }
    
    /**
     * Remove produtos do estoque usando o método Custo Médio
     * @param produto Produto a ser removido
     * @param quantidade Quantidade a ser removida
     * @param lotes Lista de lotes disponíveis
     * @return Resultado da operação com custo calculado
     */
    public ResultadoSaidaEstoque removerDoEstoqueCustoMedio(Produto produto, int quantidade, List<LoteEstoque> lotes) {
        logger.info("Removendo {} unidades do produto {} usando método Custo Médio", quantidade, produto.getNome());
        
        if (lotes == null || lotes.isEmpty()) {
            throw new RuntimeException("Não há lotes disponíveis no estoque");
        }
        
        // Calcular custo médio ponderado
        BigDecimal valorTotalEstoque = BigDecimal.ZERO;
        int quantidadeTotalEstoque = 0;
        
        for (LoteEstoque lote : lotes) {
            valorTotalEstoque = valorTotalEstoque.add(lote.getValorTotal());
            quantidadeTotalEstoque += lote.getQuantidade();
        }
        
        if (quantidadeTotalEstoque <= 0) {
            throw new RuntimeException("Estoque vazio");
        }
        
        BigDecimal custoMedioUnitario = valorTotalEstoque.divide(
            new BigDecimal(quantidadeTotalEstoque), 4, RoundingMode.HALF_UP);
        
        BigDecimal custoTotalSaida = custoMedioUnitario.multiply(new BigDecimal(quantidade));
        
        // Remover quantidades dos lotes (ordem não importa para custo médio)
        List<LoteEstoque> lotesUtilizados = new ArrayList<>();
        int quantidadeRemovida = 0;
        
        for (LoteEstoque lote : lotes) {
            if (quantidadeRemovida >= quantidade) break;
            
            int quantidadeDoLote = Math.min(lote.getQuantidade(), quantidade - quantidadeRemovida);
            
            // Atualizar quantidade do lote
            lote.setQuantidade(lote.getQuantidade() - quantidadeDoLote);
            
            // Adicionar lote utilizado ao resultado
            LoteEstoque loteUtilizado = new LoteEstoque(
                lote.getLote(), lote.getDataEntrada(), quantidadeDoLote, custoMedioUnitario
            );
            lotesUtilizados.add(loteUtilizado);
            
            quantidadeRemovida += quantidadeDoLote;
            
            logger.debug("Custo Médio: Removido {} do lote {} - Custo médio: R$ {}", 
                        quantidadeDoLote, lote.getLote(), custoMedioUnitario);
        }
        
        if (quantidadeRemovida < quantidade) {
            throw new RuntimeException(String.format(
                "Estoque insuficiente. Disponível: %d, Solicitado: %d", quantidadeRemovida, quantidade));
        }
        
        // Remover lotes com quantidade zero
        lotes.removeIf(lote -> lote.getQuantidade() <= 0);
        
        ResultadoSaidaEstoque resultado = new ResultadoSaidaEstoque(
            custoTotalSaida, custoMedioUnitario, lotesUtilizados, "Custo Médio Ponderado"
        );
        
        logger.info("Custo Médio: Custo total da saída: R$ {} | Custo unitário médio: R$ {}", 
                   custoTotalSaida, custoMedioUnitario);
        
        return resultado;
    }
    
    /**
     * Remove produtos do estoque usando o método configurado no produto
     * @param produto Produto a ser removido
     * @param quantidade Quantidade a ser removida
     * @param lotes Lista de lotes disponíveis
     * @return Resultado da operação com custo calculado
     */
    public ResultadoSaidaEstoque removerDoEstoque(Produto produto, int quantidade, List<LoteEstoque> lotes) {
        MetodoControleEstoque metodo = produto.getMetodoControleEstoque();
        
        switch (metodo) {
            case PEPS:
                return removerDoEstoquePEPS(produto, quantidade, lotes);
            case UEPS:
                return removerDoEstoqueUEPS(produto, quantidade, lotes);
            case CUSTO_MEDIO:
                return removerDoEstoqueCustoMedio(produto, quantidade, lotes);
            default:
                logger.warn("Método de controle de estoque não reconhecido: {}. Usando Custo Médio como padrão.", metodo);
                return removerDoEstoqueCustoMedio(produto, quantidade, lotes);
        }
    }
    
    /**
     * Calcula o valor total do estoque atual
     * @param lotes Lista de lotes
     * @return Valor total do estoque
     */
    public BigDecimal calcularValorTotalEstoque(List<LoteEstoque> lotes) {
        if (lotes == null || lotes.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal valorTotal = BigDecimal.ZERO;
        for (LoteEstoque lote : lotes) {
            valorTotal = valorTotal.add(lote.getValorTotal());
        }
        
        return valorTotal;
    }
    
    /**
     * Calcula o custo médio atual do estoque
     * @param lotes Lista de lotes
     * @return Custo médio unitário
     */
    public BigDecimal calcularCustoMedioAtual(List<LoteEstoque> lotes) {
        if (lotes == null || lotes.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal valorTotal = calcularValorTotalEstoque(lotes);
        int quantidadeTotal = lotes.stream().mapToInt(LoteEstoque::getQuantidade).sum();
        
        if (quantidadeTotal == 0) {
            return BigDecimal.ZERO;
        }
        
        return valorTotal.divide(new BigDecimal(quantidadeTotal), 4, RoundingMode.HALF_UP);
    }
    
    /**
     * Obtém resumo do estoque para exibição
     * @param lotes Lista de lotes
     * @return String com resumo formatado
     */
    public String getResumoEstoque(List<LoteEstoque> lotes) {
        if (lotes == null || lotes.isEmpty()) {
            return "Estoque vazio";
        }
        
        int quantidadeTotal = lotes.stream().mapToInt(LoteEstoque::getQuantidade).sum();
        BigDecimal valorTotal = calcularValorTotalEstoque(lotes);
        BigDecimal custoMedio = calcularCustoMedioAtual(lotes);
        
        StringBuilder resumo = new StringBuilder();
        resumo.append(String.format("📦 RESUMO DO ESTOQUE\n\n"));
        resumo.append(String.format("🔢 Total de itens: %d\n", quantidadeTotal));
        resumo.append(String.format("💰 Valor total: R$ %.2f\n", valorTotal));
        resumo.append(String.format("📊 Custo médio: R$ %.2f\n", custoMedio));
        resumo.append(String.format("📋 Número de lotes: %d\n\n", lotes.size()));
        
        resumo.append("📋 LOTES DETALHADOS:\n");
        for (LoteEstoque lote : lotes) {
            resumo.append(String.format("  • %s | Qtd: %d | Custo: R$ %.2f | Total: R$ %.2f | Data: %s\n",
                lote.getLote(), lote.getQuantidade(), lote.getCustoUnitario(), 
                lote.getValorTotal(), lote.getDataEntrada().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        }
        
        return resumo.toString();
    }
}
