package com.br.hermescomercial.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Serviço de regras de negócio para vendas
 * @author Hermes Comercial
 * @version 2.8.0
 */
public class VendaService {
    
    // Configurações de negócio
    private static final BigDecimal DESCONTO_MAXIMO = BigDecimal.valueOf(0.20); // 20%
    private static final BigDecimal VALOR_MINIMO_VENDA = BigDecimal.valueOf(5.00); // R$ 5,00
    private static final int QUANTIDADE_MAXIMA_ITENS = 50;
    private static final int TEMPO_LIMITE_ALTERACAO_MINUTOS = 30; // 30 minutos
    private static final BigDecimal VALOR_LIMITE_CANCELAMENTO = BigDecimal.valueOf(1000.00); // R$ 1.000,00
    
    public enum ResultadoValidacao {
        OK,                     // Venda permitida
        DESCONTO_EXCEDIDO,     // Desconto acima do limite
        VALOR_ABAIXO_MINIMO,    // Valor abaixo do mínimo
        ITENS_EXCEDIDOS,        // Quantidade de itens acima do limite
        HORARIO_INVALIDO,       // Fora do horário comercial
        CANCELAMENTO_RESTRITO,  // Cancelamento precisa de autorização
        VENDA_MUITO_ANTIGA,    // Alteração não permitida
        LIMITE_VALOR_EXCEDIDO   // Valor acima do limite para cancelamento
    }
    
    public enum TipoDesconto {
        PERCENTUAL,    // Desconto em percentual
        VALOR_FIXO    // Desconto em valor fixo
    }
    
    /**
     * Valida se o desconto é permitido
     */
    public ResultadoValidacao validarDesconto(BigDecimal valorTotal, BigDecimal valorDesconto, TipoDesconto tipo) {
        if (valorDesconto.compareTo(BigDecimal.ZERO) <= 0) {
            return ResultadoValidacao.OK; // Sem desconto é permitido
        }
        
        BigDecimal percentualDesconto;
        
        if (tipo == TipoDesconto.PERCENTUAL) {
            percentualDesconto = valorDesconto;
        } else {
            // Converter valor fixo para percentual
            percentualDesconto = valorDesconto.divide(valorTotal, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
        }
        
        if (percentualDesconto.compareTo(DESCONTO_MAXIMO.multiply(BigDecimal.valueOf(100))) > 0) {
            return ResultadoValidacao.DESCONTO_EXCEDIDO;
        }
        
        BigDecimal valorComDesconto = valorTotal.subtract(valorDesconto);
        if (valorComDesconto.compareTo(VALOR_MINIMO_VENDA) < 0) {
            return ResultadoValidacao.VALOR_ABAIXO_MINIMO;
        }
        
        return ResultadoValidacao.OK;
    }
    
    /**
     * Valida se o valor mínimo da venda é respeitado
     */
    public ResultadoValidacao validarValorMinimo(BigDecimal valorTotal) {
        if (valorTotal.compareTo(VALOR_MINIMO_VENDA) < 0) {
            return ResultadoValidacao.VALOR_ABAIXO_MINIMO;
        }
        return ResultadoValidacao.OK;
    }
    
    /**
     * Valida se a quantidade de itens é permitida
     */
    public ResultadoValidacao validarQuantidadeItens(int quantidadeItens) {
        if (quantidadeItens > QUANTIDADE_MAXIMA_ITENS) {
            return ResultadoValidacao.ITENS_EXCEDIDOS;
        }
        return ResultadoValidacao.OK;
    }
    
    /**
     * Valida se o horário permite vendas
     */
    public ResultadoValidacao validarHorarioVenda() {
        LocalTime agora = LocalTime.now();
        
        // Horário comercial: 08:00 às 22:00
        LocalTime inicioComercial = LocalTime.of(8, 0);
        LocalTime fimComercial = LocalTime.of(22, 0);
        
        if (agora.isBefore(inicioComercial) || agora.isAfter(fimComercial)) {
            return ResultadoValidacao.HORARIO_INVALIDO;
        }
        
        return ResultadoValidacao.OK;
    }
    
    /**
     * Valida se o cancelamento é permitido
     */
    public ResultadoValidacao validarCancelamento(BigDecimal valorTotal, LocalDateTime dataVenda, String motivo) {
        // Validar valor limite para cancelamento
        if (valorTotal.compareTo(VALOR_LIMITE_CANCELAMENTO) > 0) {
            return ResultadoValidacao.CANCELAMENTO_RESTRITO;
        }
        
        // Validar tempo desde a venda
        LocalDateTime agora = LocalDateTime.now();
        long minutosDesdeVenda = java.time.temporal.ChronoUnit.MINUTES.between(dataVenda, agora);
        
        if (minutosDesdeVenda > TEMPO_LIMITE_ALTERACAO_MINUTOS) {
            return ResultadoValidacao.VENDA_MUITO_ANTIGA;
        }
        
        // Validar motivo do cancelamento
        if (motivo == null || motivo.trim().isEmpty()) {
            return ResultadoValidacao.CANCELAMENTO_RESTRITO;
        }
        
        return ResultadoValidacao.OK;
    }
    
    /**
     * Valida alteração de venda
     */
    public ResultadoValidacao validarAlteracaoVenda(LocalDateTime dataVenda) {
        LocalDateTime agora = LocalDateTime.now();
        long minutosDesdeVenda = java.time.temporal.ChronoUnit.MINUTES.between(dataVenda, agora);
        
        if (minutosDesdeVenda > TEMPO_LIMITE_ALTERACAO_MINUTOS) {
            return ResultadoValidacao.VENDA_MUITO_ANTIGA;
        }
        
        return ResultadoValidacao.OK;
    }
    
    /**
     * Calcula o desconto máximo permitido
     */
    public BigDecimal calcularDescontoMaximo(BigDecimal valorTotal, TipoDesconto tipo) {
        if (tipo == TipoDesconto.PERCENTUAL) {
            return DESCONTO_MAXIMO.multiply(BigDecimal.valueOf(100));
        } else {
            return valorTotal.multiply(DESCONTO_MAXIMO);
        }
    }
    
    /**
     * Obtém mensagem de erro para validação
     */
    public String getMensagemErro(ResultadoValidacao resultado) {
        switch (resultado) {
            case DESCONTO_EXCEDIDO:
                return "🚫 DESCONTO EXCEDIDO!\n\n" +
                       "Desconto máximo permitido: " + DESCONTO_MAXIMO.multiply(BigDecimal.valueOf(100)) + "%\n" +
                       "Para descontos maiores, consulte o gerente.";
                       
            case VALOR_ABAIXO_MINIMO:
                return "💰 VALOR ABAIXO DO MÍNIMO!\n\n" +
                       "Valor mínimo por venda: R$ " + VALOR_MINIMO_VENDA + "\n" +
                       "Adicione mais itens para atingir o valor mínimo.";
                       
            case ITENS_EXCEDIDOS:
                return "📦 QUANTIDADE DE ITENS EXCEDIDA!\n\n" +
                       "Máximo de itens por venda: " + QUANTIDADE_MAXIMA_ITENS + "\n" +
                       "Para vendas maiores, consulte o gerente.";
                       
            case HORARIO_INVALIDO:
                return "🕐 FORA DO HORÁRIO COMERCIAL!\n\n" +
                       "Horário de funcionamento: 08:00 às 22:00\n" +
                       "Vendas fora do horário requerem autorização.";
                       
            case CANCELAMENTO_RESTRITO:
                return "🚫 CANCELAMENTO RESTRITO!\n\n" +
                       "Cancelamentos acima de R$ " + VALOR_LIMITE_CANCELAMENTO + " precisam de autorização do gerente.\n" +
                       "Forneça um motivo detalhado para análise.";
                       
            case VENDA_MUITO_ANTIGA:
                return "⏰ ALTERAÇÃO NÃO PERMITIDA!\n\n" +
                       "Alterações só são permitidas em até " + TEMPO_LIMITE_ALTERACAO_MINUTOS + " minutos após a venda.\n" +
                       "Contate o gerente para alterações posteriores.";
                       
            case LIMITE_VALOR_EXCEDIDO:
                return "💸 VALOR ACIMA DO LIMITE!\n\n" +
                       "Operações acima de R$ " + VALOR_LIMITE_CANCELAMENTO + " precisam de autorização.";
                       
            default:
                return "❓ Erro de validação não identificado.";
        }
    }
    
    /**
     * Verifica se precisa de autorização do gerente
     */
    public boolean precisaAutorizacaoGerente(ResultadoValidacao resultado) {
        return resultado == ResultadoValidacao.DESCONTO_EXCEDIDO ||
               resultado == ResultadoValidacao.CANCELAMENTO_RESTRITO ||
               resultado == ResultadoValidacao.ITENS_EXCEDIDOS ||
               resultado == ResultadoValidacao.LIMITE_VALOR_EXCEDIDO;
    }
    
    /**
     * Obtém configurações atuais do sistema
     */
    public Map<String, Object> getConfiguracoes() {
        Map<String, Object> config = new HashMap<>();
        
        config.put("descontoMaximo", DESCONTO_MAXIMO.multiply(BigDecimal.valueOf(100)) + "%");
        config.put("valorMinimoVenda", "R$ " + VALOR_MINIMO_VENDA);
        config.put("quantidadeMaximaItens", QUANTIDADE_MAXIMA_ITENS);
        config.put("tempoLimiteAlteracao", TEMPO_LIMITE_ALTERACAO_MINUTOS + " minutos");
        config.put("valorLimiteCancelamento", "R$ " + VALOR_LIMITE_CANCELAMENTO);
        config.put("horarioComercial", "08:00 às 22:00");
        
        return config;
    }
    
    /**
     * Validação completa de uma venda
     */
    public Map<String, ResultadoValidacao> validarVendaCompleta(
            BigDecimal valorTotal, 
            BigDecimal valorDesconto, 
            TipoDesconto tipoDesconto,
            int quantidadeItens,
            LocalDateTime dataVenda) {
        
        Map<String, ResultadoValidacao> validacoes = new HashMap<>();
        
        // Validar valor mínimo
        validacoes.put("valorMinimo", validarValorMinimo(valorTotal));
        
        // Validar desconto
        validacoes.put("desconto", validarDesconto(valorTotal, valorDesconto, tipoDesconto));
        
        // Validar quantidade de itens
        validacoes.put("quantidadeItens", validarQuantidadeItens(quantidadeItens));
        
        // Validar horário
        validacoes.put("horario", validarHorarioVenda());
        
        // Validar tempo para alteração
        if (dataVenda != null) {
            validacoes.put("alteracao", validarAlteracaoVenda(dataVenda));
        }
        
        return validacoes;
    }
    
    /**
     * Verifica se todas as validações foram aprovadas
     */
    public boolean todasValidacoesAprovadas(Map<String, ResultadoValidacao> validacoes) {
        for (ResultadoValidacao resultado : validacoes.values()) {
            if (resultado != ResultadoValidacao.OK) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Obtém o nível de risco de uma venda
     */
    public String getNivelRisco(BigDecimal valorTotal, int quantidadeItens) {
        int pontosRisco = 0;
        
        // Valor alto
        if (valorTotal.compareTo(BigDecimal.valueOf(500)) > 0) pontosRisco++;
        if (valorTotal.compareTo(BigDecimal.valueOf(1000)) > 0) pontosRisco++;
        
        // Quantidade de itens
        if (quantidadeItens > 20) pontosRisco++;
        if (quantidadeItens > 30) pontosRisco++;
        
        if (pontosRisco == 0) return "🟢 Baixo";
        if (pontosRisco == 1) return "🟡 Médio";
        if (pontosRisco == 2) return "🟠 Alto";
        return "🔴 Crítico";
    }
}
