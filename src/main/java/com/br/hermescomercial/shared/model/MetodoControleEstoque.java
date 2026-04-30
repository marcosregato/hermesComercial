package com.br.hermescomercial.shared.model;

/**
 * Enum para métodos de controle de estoque
 * Define os diferentes métodos de avaliação de custos e controle de saída de produtos
 */
public enum MetodoControleEstoque {
    
    /**
     * PEPS (Primeiro que Entra, Primeiro que Sai)
     * Os primeiros itens comprados são os primeiros a serem vendidos
     * Ideal para produtos perecíveis e com validade
     */
    PEPS("PEPS", "Primeiro que Entra, Primeiro que Sai", "FIFO"),
    
    /**
     * UEPS (Último que Entra, Primeiro que Sai)
     * Os últimos itens comprados são os primeiros a serem vendidos
     * Pode ser vantajoso em períodos de inflação
     */
    UEPS("UEPS", "Último que Entra, Primeiro que Sai", "LIFO"),
    
    /**
     * Custo Médio
     * O valor do produto é calculado pela média dos custos de compra
     * Suaviza variações de preço ao longo do tempo
     */
    CUSTO_MEDIO("Custo Médio", "Custo Médio Ponderado", "Average Cost");
    
    private final String sigla;
    private final String descricao;
    private final String nomeIngles;
    
    MetodoControleEstoque(String sigla, String descricao, String nomeIngles) {
        this.sigla = sigla;
        this.descricao = descricao;
        this.nomeIngles = nomeIngles;
    }
    
    public String getSigla() {
        return sigla;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public String getNomeIngles() {
        return nomeIngles;
    }
    
    @Override
    public String toString() {
        return sigla + " - " + descricao;
    }
    
    /**
     * Obtém o método a partir da sigla
     * @param sigla Sigla do método (PEPS, UEPS, CUSTO_MEDIO)
     * @return Método correspondente ou null se não encontrado
     */
    public static MetodoControleEstoque fromSigla(String sigla) {
        for (MetodoControleEstoque metodo : values()) {
            if (metodo.sigla.equalsIgnoreCase(sigla)) {
                return metodo;
            }
        }
        return null;
    }
    
    /**
     * Verifica se o método é PEPS
     * @return true se for PEPS
     */
    public boolean isPEPS() {
        return this == PEPS;
    }
    
    /**
     * Verifica se o método é UEPS
     * @return true se for UEPS
     */
    public boolean isUEPS() {
        return this == UEPS;
    }
    
    /**
     * Verifica se o método é Custo Médio
     * @return true se for Custo Médio
     */
    public boolean isCustoMedio() {
        return this == CUSTO_MEDIO;
    }
}
