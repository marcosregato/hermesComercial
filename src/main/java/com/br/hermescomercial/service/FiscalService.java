package com.br.hermescomercial.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Serviço de validações fiscais e conformidade legal
 * @author Hermes Comercial
 * @version 2.8.0
 */
public class FiscalService {
    
    // Configurações fiscais brasileiras
    private static final BigDecimal LIMITE_NFCE_ANUAL = BigDecimal.valueOf(200000.00); // R$ 200.000 por ano
    private static final BigDecimal LIMITE_NFCe_DIA = BigDecimal.valueOf(10000.00); // R$ 10.000 por dia
    private static final BigDecimal ALIQUOTA_ICMS_PADRAO = BigDecimal.valueOf(0.18); // 18%
    private static final BigDecimal ALIQUOTA_PIS = BigDecimal.valueOf(0.0065); // 0,65%
    private static final BigDecimal ALIQUOTA_COFINS = BigDecimal.valueOf(0.03); // 3%
        
    // CFOPs mais comuns
    public static final String CFOP_VENDA_INTERNA = "5102"; // Venda de mercadoria adquirida ou recebida de terceiros
    public static final String CFOP_VENDA_FORA_ESTADO = "6102"; // Venda de mercadoria adquirida ou recebida de terceiros
    public static final String CFOP_VENDA_CONTRIBUINTE = "5405"; // Venda de mercadoria adquirida ou recebida de terceiros
    public static final String CFOP_DEVOLUCAO_VENDA = "1202"; // Devolução de venda
    
    public enum TipoOperacao {
        VENDA_CONTRIBUINTE,    // Venda para contribuinte do ICMS
        VENDA_NAO_CONTRIBUINTE, // Venda para não contribuinte
        VENDA_FORA_ESTADO,     // Venda para fora do estado
        DEVOLUCAO,            // Devolução de venda
        SIMPLES_NACIONAL      // Regime Simples Nacional
    }
    
    public enum RegimeTributario {
        SIMPLES_NACIONAL,     // Simples Nacional
        LUCRO_PRESUMIDO,      // Lucro Presumido
        LUCRO_REAL           // Lucro Real
    }
    
    public enum StatusValidacao {
        APROVADO,             // Documento fiscal válido
        REPROVADO_VALOR,     // Valor acima do limite
        REPROVADO_CFOP,       // CFOP inadequado
        REPROVADO_CAMPOS,     // Campos obrigatórios faltando
        REPROVADO_ALIQUOTA,   // Alíquota incorreta
        ATENCAO_NECESSARIA    // Requer atenção/manual
    }
    
    /**
     * Valida se NFCe pode ser emitida
     */
    public Map<String, Object> validarEmissaoNFCe(BigDecimal valorTotal, String cpfCnpj, TipoOperacao tipoOperacao) {
        Map<String, Object> validacao = new HashMap<>();
        
        try {
            // Validar valor total
            if (valorTotal.compareTo(BigDecimal.ZERO) <= 0) {
                validacao.put("status", StatusValidacao.REPROVADO_VALOR);
                validacao.put("mensagem", "Valor total deve ser maior que zero");
                return validacao;
            }
            
            // Validar limite diário
            BigDecimal totalDiario = getTotalNFCeHoje();
            BigDecimal totalComVenda = totalDiario.add(valorTotal);
            
            if (totalComVenda.compareTo(LIMITE_NFCe_DIA) > 0) {
                validacao.put("status", StatusValidacao.REPROVADO_VALOR);
                validacao.put("mensagem", "Valor excede limite diário de NFCe (R$ " + LIMITE_NFCe_DIA + ")");
                validacao.put("limiteDiario", LIMITE_NFCe_DIA);
                validacao.put("totalDiario", totalDiario);
                return validacao;
            }
            
            // Validar limite anual
            BigDecimal totalAnual = getTotalNFCeAno();
            BigDecimal totalAnualComVenda = totalAnual.add(valorTotal);
            
            if (totalAnualComVenda.compareTo(LIMITE_NFCE_ANUAL) > 0) {
                validacao.put("status", StatusValidacao.ATENCAO_NECESSARIA);
                validacao.put("mensagem", "Valor se aproxima do limite anual de NFCe (R$ " + LIMITE_NFCE_ANUAL + ")");
                validacao.put("limiteAnual", LIMITE_NFCE_ANUAL);
                validacao.put("totalAnual", totalAnual);
                validacao.put("sugestao", "Considere emitir NF-e para vendas de alto valor");
                return validacao;
            }
            
            // Validar CPF/CNPJ
            if (!validarCPFCNPJ(cpfCnpj)) {
                validacao.put("status", StatusValidacao.REPROVADO_CAMPOS);
                validacao.put("mensagem", "CPF/CNPJ inválido ou não informado");
                return validacao;
            }
            
            // Calcular impostos
            Map<String, BigDecimal> impostos = calcularImpostos(valorTotal, tipoOperacao);
            
            // Validar CFOP adequado
            String cfopSugerido = sugerirCFOP(tipoOperacao);
            
            validacao.put("status", StatusValidacao.APROVADO);
            validacao.put("mensagem", "Emissão de NFCe autorizada");
            validacao.put("cfopSugerido", cfopSugerido);
            validacao.put("impostos", impostos);
            validacao.put("totalComImpostos", valorTotal.add(impostos.get("totalImpostos")));
            
        } catch (Exception e) {
            validacao.put("status", StatusValidacao.REPROVADO_CAMPOS);
            validacao.put("mensagem", "Erro na validação: " + e.getMessage());
        }
        
        return validacao;
    }
    
    /**
     * Calcula impostos para a operação
     */
    public Map<String, BigDecimal> calcularImpostos(BigDecimal valorBase, TipoOperacao tipoOperacao) {
        Map<String, BigDecimal> impostos = new HashMap<>();
        
        try {
            BigDecimal icms = BigDecimal.ZERO;
            BigDecimal pis = BigDecimal.ZERO;
            BigDecimal cofins = BigDecimal.ZERO;
            BigDecimal iss = BigDecimal.ZERO;
            
            switch (tipoOperacao) {
                case VENDA_CONTRIBUINTE:
                    icms = valorBase.multiply(ALIQUOTA_ICMS_PADRAO).setScale(2, RoundingMode.HALF_UP);
                    pis = valorBase.multiply(ALIQUOTA_PIS).setScale(2, RoundingMode.HALF_UP);
                    cofins = valorBase.multiply(ALIQUOTA_COFINS).setScale(2, RoundingMode.HALF_UP);
                    break;
                    
                case VENDA_NAO_CONTRIBUINTE:
                    // Para não contribuinte, ICMS é substituído
                    icms = valorBase.multiply(ALIQUOTA_ICMS_PADRAO).setScale(2, RoundingMode.HALF_UP);
                    pis = valorBase.multiply(ALIQUOTA_PIS).setScale(2, RoundingMode.HALF_UP);
                    cofins = valorBase.multiply(ALIQUOTA_COFINS).setScale(2, RoundingMode.HALF_UP);
                    break;
                    
                case SIMPLES_NACIONAL:
                    // Simples Nacional tem tratamento diferenciado
                    BigDecimal aliquotaSimples = getAliquotaSimples(valorBase);
                    BigDecimal totalSimples = valorBase.multiply(aliquotaSimples).setScale(2, RoundingMode.HALF_UP);
                    impostos.put("simplesNacional", totalSimples);
                    break;
                    
                case DEVOLUCAO:
                    // Devolução não gera impostos novos (estorna)
                    break;
                    
                case VENDA_FORA_ESTADO:
                    // Para fora do estado, ICMS interestadual
                    icms = valorBase.multiply(ALIQUOTA_ICMS_PADRAO.multiply(BigDecimal.valueOf(1.2))).setScale(2, RoundingMode.HALF_UP); // 12% + 20% diferença
                    pis = valorBase.multiply(ALIQUOTA_PIS).setScale(2, RoundingMode.HALF_UP);
                    cofins = valorBase.multiply(ALIQUOTA_COFINS).setScale(2, RoundingMode.HALF_UP);
                    break;
            }
            
            impostos.put("icms", icms);
            impostos.put("pis", pis);
            impostos.put("cofins", cofins);
            impostos.put("iss", iss);
            
            BigDecimal totalImpostos = icms.add(pis).add(cofins).add(iss);
            impostos.put("totalImpostos", totalImpostos);
            
        } catch (Exception e) {
            // Valores zerados em caso de erro
            impostos.put("icms", BigDecimal.ZERO);
            impostos.put("pis", BigDecimal.ZERO);
            impostos.put("cofins", BigDecimal.ZERO);
            impostos.put("iss", BigDecimal.ZERO);
            impostos.put("totalImpostos", BigDecimal.ZERO);
        }
        
        return impostos;
    }
    
    /**
     * Sugere CFOP adequado para a operação
     */
    public String sugerirCFOP(TipoOperacao tipoOperacao) {
        switch (tipoOperacao) {
            case VENDA_CONTRIBUINTE:
                return CFOP_VENDA_CONTRIBUINTE;
            case VENDA_NAO_CONTRIBUINTE:
                return CFOP_VENDA_INTERNA;
            case VENDA_FORA_ESTADO:
                return CFOP_VENDA_FORA_ESTADO;
            case DEVOLUCAO:
                return CFOP_DEVOLUCAO_VENDA;
            case SIMPLES_NACIONAL:
                return CFOP_VENDA_INTERNA; // Pode variar conforme o caso
            default:
                return CFOP_VENDA_INTERNA;
        }
    }
    
    /**
     * Valida se CPF/CNPJ está no formato correto
     */
    private boolean validarCPFCNPJ(String cpfCnpj) {
        if (cpfCnpj == null || cpfCnpj.trim().isEmpty()) {
            return false;
        }
        
        cpfCnpj = cpfCnpj.replaceAll("[^0-9]", "");
        
        if (cpfCnpj.length() == 11) {
            return validarCPF(cpfCnpj);
        } else if (cpfCnpj.length() == 14) {
            return validarCNPJ(cpfCnpj);
        }
        
        return false;
    }
    
    /**
     * Valida CPF (algoritmo simplificado)
     */
    private boolean validarCPF(String cpf) {
        if (cpf.equals("00000000000") || cpf.equals("11111111111") || 
            cpf.equals("22222222222") || cpf.equals("33333333333") || 
            cpf.equals("44444444444") || cpf.equals("55555555555") || 
            cpf.equals("66666666666") || cpf.equals("77777777777") || 
            cpf.equals("88888888888") || cpf.equals("99999999999")) {
            return false;
        }
        
        // Validação simplificada - em produção, implementar algoritmo completo
        return cpf.length() == 11;
    }
    
    /**
     * Valida CNPJ (algoritmo simplificado)
     */
    private boolean validarCNPJ(String cnpj) {
        if (cnpj.equals("00000000000000") || cnpj.equals("11111111111111") || 
            cnpj.equals("22222222222222") || cnpj.equals("33333333333333") || 
            cnpj.equals("44444444444444") || cnpj.equals("55555555555555") || 
            cnpj.equals("66666666666666") || cnpj.equals("77777777777777") || 
            cnpj.equals("88888888888888") || cnpj.equals("99999999999999")) {
            return false;
        }
        
        // Validação simplificada - em produção, implementar algoritmo completo
        return cnpj.length() == 14;
    }
    
    /**
     * Obtém alíquota do Simples Nacional
     */
    private BigDecimal getAliquotaSimples(BigDecimal faturamentoAnual) {
        // Tabela Simples Nacional 2024 (simplificada)
        if (faturamentoAnual.compareTo(BigDecimal.valueOf(180000)) <= 0) {
            return BigDecimal.valueOf(0.045); // 4,5%
        } else if (faturamentoAnual.compareTo(BigDecimal.valueOf(360000)) <= 0) {
            return BigDecimal.valueOf(0.073); // 7,3%
        } else if (faturamentoAnual.compareTo(BigDecimal.valueOf(720000)) <= 0) {
            return BigDecimal.valueOf(0.095); // 9,5%
        } else if (faturamentoAnual.compareTo(BigDecimal.valueOf(1800000)) <= 0) {
            return BigDecimal.valueOf(0.107); // 10,7%
        } else if (faturamentoAnual.compareTo(BigDecimal.valueOf(3600000)) <= 0) {
            return BigDecimal.valueOf(0.143); // 14,3%
        } else if (faturamentoAnual.compareTo(BigDecimal.valueOf(4800000)) <= 0) {
            return BigDecimal.valueOf(0.19); // 19%
        } else {
            return BigDecimal.valueOf(0.225); // 22,5%
        }
    }
    
    /**
     * Obtém total de NFCe emitidas hoje
     */
    private BigDecimal getTotalNFCeHoje() {
        // Simulação - em implementação real, consultaria o banco
        return BigDecimal.valueOf(Math.random() * 5000); // Valor aleatório até R$ 5.000
    }
    
    /**
     * Obtém total de NFCe emitidas no ano
     */
    private BigDecimal getTotalNFCeAno() {
        // Simulação - em implementação real, consultaria o banco
        int diaDoAno = LocalDate.now().getDayOfYear();
        BigDecimal mediaDiaria = BigDecimal.valueOf(1000 + Math.random() * 2000); // R$ 1.000 a 3.000 por dia
        return mediaDiaria.multiply(BigDecimal.valueOf(diaDoAno));
    }
    
    /**
     * Gera número de série para NFCe
     */
    public String gerarNumeroSerieNFCe() {
        // Formato: AAAA + CNPJ (últimos 8 dígitos) + Modelo + Série + Número aleatório
        String ano = String.valueOf(LocalDate.now().getYear());
        String cnpjUltimos8 = "12345678"; // Simulação - viria da configuração
        String modelo = "65"; // NFCe
        String serie = "001";
        String numero = String.format("%06d", (int)(Math.random() * 999999));
        
        return ano + cnpjUltimos8 + modelo + serie + numero;
    }
    
    /**
     * Gera chave de acesso para NFCe
     */
    public String gerarChaveAcessoNFCe(String estado, String cnpj, String modelo, String serie, 
                                     String numero, String tpEmis, String codigoNumerico) {
        // Simulação - em implementação real, seguiria layout oficial da NFCe
        StringBuilder chave = new StringBuilder();
        
        // UF (2 dígitos)
        chave.append(String.format("%02d", Integer.parseInt(estado)));
        
        // AAMM (ano e mês)
        chave.append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyMM")));
        
        // CNPJ (14 dígitos)
        chave.append(cnpj.replaceAll("[^0-9]", ""));
        
        // Modelo (2 dígitos)
        chave.append(modelo);
        
        // Série (3 dígitos)
        chave.append(String.format("%03d", Integer.parseInt(serie)));
        
        // Número (9 dígitos)
        chave.append(String.format("%09d", Integer.parseInt(numero)));
        
        // Forma de emissão (1 dígito)
        chave.append(tpEmis);
        
        // Código numérico (8 dígitos)
        chave.append(String.format("%08d", Integer.parseInt(codigoNumerico)));
        
        // DV (1 dígito - calculado)
        chave.append(calcularDV(chave.toString()));
        
        return chave.toString();
    }
    
    /**
     * Calcula dígito verificador (simplificado)
     */
    private String calcularDV(String chave) {
        // Simulação - em implementação real, seguiria algoritmo oficial do Módulo 11
        int soma = 0;
        int peso = 2;
        
        for (int i = chave.length() - 1; i >= 0; i--) {
            soma += Character.getNumericValue(chave.charAt(i)) * peso;
            peso = peso == 9 ? 2 : peso + 1;
        }
        
        int resto = soma % 11;
        int dv = resto < 2 ? 0 : 11 - resto;
        
        return String.valueOf(dv);
    }
    
    /**
     * Obtém resumo fiscal do período
     */
    public Map<String, Object> getResumoFiscalPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        Map<String, Object> resumo = new HashMap<>();
        
        try {
            // Simulação de dados fiscais
            int dias = (int) java.time.temporal.ChronoUnit.DAYS.between(dataInicio, dataFim) + 1;
            
            BigDecimal totalFaturado = BigDecimal.valueOf(10000 + Math.random() * 50000).multiply(BigDecimal.valueOf(dias));
            BigDecimal totalImpostos = totalFaturado.multiply(BigDecimal.valueOf(0.15)); // 15% em média
            int totalDocumentos = (int)(50 + Math.random() * 200) * dias;
            
            resumo.put("periodo", dataInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + 
                              " a " + dataFim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            resumo.put("totalFaturado", totalFaturado);
            resumo.put("totalImpostos", totalImpostos);
            resumo.put("totalDocumentos", totalDocumentos);
            resumo.put("mediaDiaria", totalFaturado.divide(BigDecimal.valueOf(dias), 2, RoundingMode.HALF_UP));
            resumo.put("percentualImpostos", totalImpostos.divide(totalFaturado, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)));
            
            // Verificar proximidade dos limites
            BigDecimal totalAnual = getTotalNFCeAno();
            BigDecimal percentualLimiteAnual = totalAnual.divide(LIMITE_NFCE_ANUAL, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
            
            if (percentualLimiteAnual.compareTo(BigDecimal.valueOf(80)) > 0) {
                resumo.put("alertaLimiteAnual", "Atenção: " + percentualLimiteAnual.setScale(1, RoundingMode.HALF_UP) + 
                                           "% do limite anual utilizado");
            }
            
        } catch (Exception e) {
            resumo.put("erro", "Não foi possível obter resumo fiscal");
        }
        
        return resumo;
    }
}
