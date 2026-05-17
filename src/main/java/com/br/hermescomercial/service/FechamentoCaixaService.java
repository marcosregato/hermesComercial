package com.br.hermescomercial.service;

import com.br.hermescomercial.dao.VendaDao;
import com.br.hermescomercial.model.VendaPDV;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Serviço de fechamento e validação de caixa
 * @author Hermes Comercial
 * @version 2.8.0
 */
public class FechamentoCaixaService {
    
    private final VendaDao vendaDao;
    
    // Configurações de negócio
    private static final BigDecimal TOLERANCIA_DIVERGENCIA = BigDecimal.valueOf(10.00); // R$ 10,00
    private static final BigDecimal VALOR_MINIMO_CAIXA = BigDecimal.valueOf(100.00); // R$ 100,00
    private static final int HORAS_LIMITE_FECHAMENTO = 2; // 2 horas após o fim do expediente
    
    public enum StatusCaixa {
        ABERTO,         // Caixa aberto
        EM_PROCESSO,    // Em processo de fechamento
        FECHADO,        // Caixa fechado
        BLOQUEADO,      // Caixa bloqueado por divergência
        PENDENTE        // Fechamento pendente de validação
    }
    
    public enum TipoPagamento {
        DINHEIRO, CARTAO_CREDITO, CARTAO_DEBITO, PIX, OUTROS
    }
    
    public enum ResultadoValidacao {
        OK,                     // Validação aprovada
        DIVERGENCIA_VALOR,      // Divergência de valores
        CAIXA_NAO_FECHADO,      // Caixa ainda não fechado
        FORA_DO_PRAZO,          // Fora do prazo para fechamento
        VALORES_ZERADOS,        // Valores zerados ou inválidos
        VENDAS_PENDENTES,       // Existem vendas não processadas
        AUTORIZACAO_NECESSARIA  // Precisa de autorização do gerente
    }
    
    public FechamentoCaixaService() {
        this.vendaDao = new VendaDao();
    }
    
    /**
     * Obtém resumo do caixa para uma data
     */
    public Map<String, Object> getResumoCaixa(LocalDate data) {
        Map<String, Object> resumo = new HashMap<>();
        
        try {
            LocalDateTime inicioDia = data.atStartOfDay();
            LocalDateTime fimDia = data.atTime(23, 59, 59);
            
            List<VendaPDV> vendas = vendaDao.listarPorPeriodo(inicioDia, fimDia);
            
            // Valores totais
            BigDecimal totalVendas = BigDecimal.ZERO;
            BigDecimal totalDinheiro = BigDecimal.ZERO;
            BigDecimal totalCartaoCredito = BigDecimal.ZERO;
            BigDecimal totalCartaoDebito = BigDecimal.ZERO;
            BigDecimal totalPix = BigDecimal.ZERO;
            BigDecimal totalOutros = BigDecimal.ZERO;
            
            int quantidadeVendas = 0;
            BigDecimal ticketMedio = BigDecimal.ZERO;
            
            for (VendaPDV venda : vendas) {
                BigDecimal valor = venda.getValorTotal();
                totalVendas = totalVendas.add(valor);
                quantidadeVendas++;
                
                // Simulação de tipos de pagamento (em implementação real, viria do banco)
                String tipoPagamento = getTipoPagamentoVenda(venda);
                
                switch (tipoPagamento) {
                    case "DINHEIRO":
                        totalDinheiro = totalDinheiro.add(valor);
                        break;
                    case "CARTAO_CREDITO":
                        totalCartaoCredito = totalCartaoCredito.add(valor);
                        break;
                    case "CARTAO_DEBITO":
                        totalCartaoDebito = totalCartaoDebito.add(valor);
                        break;
                    case "PIX":
                        totalPix = totalPix.add(valor);
                        break;
                    default:
                        totalOutros = totalOutros.add(valor);
                        break;
                }
            }
            
            if (quantidadeVendas > 0) {
                ticketMedio = totalVendas.divide(BigDecimal.valueOf(quantidadeVendas), 2, RoundingMode.HALF_UP);
            }
            
            // Preencher resumo
            resumo.put("data", data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            resumo.put("dataCompleta", data.format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy")));
            resumo.put("totalVendas", totalVendas);
            resumo.put("quantidadeVendas", quantidadeVendas);
            resumo.put("ticketMedio", ticketMedio);
            
            // Valores por tipo de pagamento
            resumo.put("totalDinheiro", totalDinheiro);
            resumo.put("totalCartaoCredito", totalCartaoCredito);
            resumo.put("totalCartaoDebito", totalCartaoDebito);
            resumo.put("totalPix", totalPix);
            resumo.put("totalOutros", totalOutros);
            
            // Percentuais
            if (totalVendas.compareTo(BigDecimal.ZERO) > 0) {
                resumo.put("percentualDinheiro", totalDinheiro.divide(totalVendas, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)));
                resumo.put("percentualCartaoCredito", totalCartaoCredito.divide(totalVendas, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)));
                resumo.put("percentualCartaoDebito", totalCartaoDebito.divide(totalVendas, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)));
                resumo.put("percentualPix", totalPix.divide(totalVendas, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)));
                resumo.put("percentualOutros", totalOutros.divide(totalVendas, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)));
            }
            
            resumo.put("status", getStatusCaixa(data));
            resumo.put("horaGeracao", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            
        } catch (Exception e) {
            // Valores padrão em caso de erro
            resumo.put("erro", "Não foi possível obter resumo do caixa");
            resumo.put("totalVendas", BigDecimal.ZERO);
            resumo.put("quantidadeVendas", 0);
        }
        
        return resumo;
    }
    
    /**
     * Valida fechamento de caixa
     */
    public Map<String, Object> validarFechamentoCaixa(LocalDate data, Map<TipoPagamento, BigDecimal> valoresInformados) {
        Map<String, Object> validacao = new HashMap<>();
        
        try {
            // Obter resumo do caixa
            Map<String, Object> resumo = getResumoCaixa(data);
            BigDecimal totalSistema = (BigDecimal) resumo.get("totalVendas");
            
            // Calcular total informado
            BigDecimal totalInformado = BigDecimal.ZERO;
            for (BigDecimal valor : valoresInformados.values()) {
                totalInformado = totalInformado.add(valor);
            }
            
            // Validar valores mínimos
            if (totalSistema.compareTo(VALOR_MINIMO_CAIXA) < 0) {
                validacao.put("resultado", ResultadoValidacao.VALORES_ZERADOS);
                validacao.put("mensagem", "Valor total do caixa abaixo do mínimo esperado (R$ " + VALOR_MINIMO_CAIXA + ")");
                return validacao;
            }
            
            // Validar prazo de fechamento
            if (!estaDentroPrazoFechamento(data)) {
                validacao.put("resultado", ResultadoValidacao.FORA_DO_PRAZO);
                validacao.put("mensagem", "Fechamento fora do prazo permitido (" + HORAS_LIMITE_FECHAMENTO + " horas após o expediente)");
                return validacao;
            }
            
            // Calcular divergência
            BigDecimal divergencia = totalInformado.subtract(totalSistema);
            BigDecimal percentualDivergencia = BigDecimal.ZERO;
            
            if (totalSistema.compareTo(BigDecimal.ZERO) > 0) {
                percentualDivergencia = divergencia.divide(totalSistema, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
            }
            
            // Validar divergência
            ResultadoValidacao resultado;
            String mensagem;
            
            if (divergencia.abs().compareTo(TOLERANCIA_DIVERGENCIA) <= 0) {
                resultado = ResultadoValidacao.OK;
                mensagem = "Fechamento validado com sucesso!";
            } else if (divergencia.abs().compareTo(TOLERANCIA_DIVERGENCIA.multiply(BigDecimal.valueOf(3))) <= 0) {
                resultado = ResultadoValidacao.AUTORIZACAO_NECESSARIA;
                mensagem = "Divergência detectada. Necessária autorização do gerente.";
            } else {
                resultado = ResultadoValidacao.DIVERGENCIA_VALOR;
                mensagem = "Divergência significativa detectada. Fechamento bloqueado.";
            }
            
            // Preencher validação
            validacao.put("resultado", resultado);
            validacao.put("mensagem", mensagem);
            validacao.put("totalSistema", totalSistema);
            validacao.put("totalInformado", totalInformado);
            validacao.put("divergencia", divergencia);
            validacao.put("percentualDivergencia", percentualDivergencia);
            validacao.put("tolerancia", TOLERANCIA_DIVERGENCIA);
            
            // Valores por tipo
            validacao.put("valoresSistema", Map.of(
                "DINHEIRO", resumo.get("totalDinheiro"),
                "CARTAO_CREDITO", resumo.get("totalCartaoCredito"),
                "CARTAO_DEBITO", resumo.get("totalCartaoDebito"),
                "PIX", resumo.get("totalPix"),
                "OUTROS", resumo.get("totalOutros")
            ));
            
            validacao.put("valoresInformados", valoresInformados);
            
            // Detalhes das divergências por tipo
            Map<String, BigDecimal> divergenciasPorTipo = new HashMap<>();
            for (Map.Entry<TipoPagamento, BigDecimal> entry : valoresInformados.entrySet()) {
                BigDecimal valorSistema = getValorPorTipo(resumo, entry.getKey());
                BigDecimal divergenciaTipo = entry.getValue().subtract(valorSistema);
                if (divergenciaTipo.compareTo(BigDecimal.ZERO) != 0) {
                    divergenciasPorTipo.put(entry.getKey().toString(), divergenciaTipo);
                }
            }
            validacao.put("divergenciasPorTipo", divergenciasPorTipo);
            
        } catch (Exception e) {
            validacao.put("resultado", ResultadoValidacao.VALORES_ZERADOS);
            validacao.put("mensagem", "Erro ao validar fechamento: " + e.getMessage());
        }
        
        return validacao;
    }
    
    /**
     * Finaliza fechamento de caixa
     */
    public boolean finalizarFechamentoCaixa(LocalDate data, Map<TipoPagamento, BigDecimal> valoresInformados, String operador, String observacoes) {
        try {
            // Validar fechamento
            Map<String, Object> validacao = validarFechamentoCaixa(data, valoresInformados);
            ResultadoValidacao resultado = (ResultadoValidacao) validacao.get("resultado");
            
            if (resultado == ResultadoValidacao.DIVERGENCIA_VALOR || resultado == ResultadoValidacao.VALORES_ZERADOS) {
                return false;
            }
            
            // Registrar fechamento (em implementação real, salvaria no banco)
            registrarFechamento(data, valoresInformados, operador, observacoes, validacao);
            
            // Atualizar status do caixa
            atualizarStatusCaixa(data, StatusCaixa.FECHADO);
            
            return true;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Obtém histórico de fechamentos
     */
    public List<Map<String, Object>> getHistoricoFechamentos(LocalDate dataInicio, LocalDate dataFim) {
        List<Map<String, Object>> historico = new ArrayList<>();
        
        try {
            LocalDate dataAtual = dataInicio;
            
            while (!dataAtual.isAfter(dataFim)) {
                Map<String, Object> resumo = getResumoCaixa(dataAtual);
                
                if (((BigDecimal) resumo.get("totalVendas")).compareTo(BigDecimal.ZERO) > 0) {
                    Map<String, Object> registro = new HashMap<>();
                    registro.put("data", dataAtual.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    registro.put("totalVendas", resumo.get("totalVendas"));
                    registro.put("quantidadeVendas", resumo.get("quantidadeVendas"));
                    registro.put("ticketMedio", resumo.get("ticketMedio"));
                    registro.put("status", resumo.get("status"));
                    
                    historico.add(registro);
                }
                
                dataAtual = dataAtual.plusDays(1);
            }
            
        } catch (Exception e) {
            // Retornar lista vazia em caso de erro
        }
        
        return historico;
    }
    
    /**
     * Obtém estatísticas do período
     */
    public Map<String, Object> getEstatisticasPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        Map<String, Object> estatisticas = new HashMap<>();
        
        try {
            List<Map<String, Object>> historico = getHistoricoFechamentos(dataInicio, dataFim);
            
            if (historico.isEmpty()) {
                estatisticas.put("mensagem", "Não há dados para o período selecionado");
                return estatisticas;
            }
            
            BigDecimal totalFaturamento = BigDecimal.ZERO;
            int totalVendas = 0;
            int diasComVendas = historico.size();
            
            BigDecimal maiorFaturamento = BigDecimal.ZERO;
            BigDecimal menorFaturamento = BigDecimal.valueOf(Double.MAX_VALUE);
            
            for (Map<String, Object> dia : historico) {
                BigDecimal faturamentoDia = (BigDecimal) dia.get("totalVendas");
                int vendasDia = (Integer) dia.get("quantidadeVendas");
                
                totalFaturamento = totalFaturamento.add(faturamentoDia);
                totalVendas += vendasDia;
                
                if (faturamentoDia.compareTo(maiorFaturamento) > 0) {
                    maiorFaturamento = faturamentoDia;
                }
                
                if (faturamentoDia.compareTo(menorFaturamento) < 0) {
                    menorFaturamento = faturamentoDia;
                }
            }
            
            BigDecimal mediaDiaria = totalFaturamento.divide(BigDecimal.valueOf(diasComVendas), 2, RoundingMode.HALF_UP);
            BigDecimal ticketMedioGeral = totalVendas > 0 ? 
                totalFaturamento.divide(BigDecimal.valueOf(totalVendas), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            
            estatisticas.put("periodo", dataInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + 
                              " a " + dataFim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            estatisticas.put("totalFaturamento", totalFaturamento);
            estatisticas.put("totalVendas", totalVendas);
            estatisticas.put("diasComVendas", diasComVendas);
            estatisticas.put("mediaDiaria", mediaDiaria);
            estatisticas.put("ticketMedioGeral", ticketMedioGeral);
            estatisticas.put("maiorFaturamento", maiorFaturamento);
            estatisticas.put("menorFaturamento", menorFaturamento);
            
        } catch (Exception e) {
            estatisticas.put("erro", "Não foi possível calcular estatísticas");
        }
        
        return estatisticas;
    }
    
    // Métodos auxiliares
    private String getTipoPagamentoVenda(VendaPDV venda) {
        // Simulação - em implementação real, viria do banco
        String[] tipos = {"DINHEIRO", "CARTAO_CREDITO", "CARTAO_DEBITO", "PIX", "OUTROS"};
        return tipos[(int)(Math.random() * tipos.length)];
    }
    
    private StatusCaixa getStatusCaixa(LocalDate data) {
        // Simulação - em implementação real, viria do banco
        if (data.isBefore(LocalDate.now())) {
            return StatusCaixa.FECHADO;
        } else if (data.isEqual(LocalDate.now())) {
            return StatusCaixa.ABERTO;
        } else {
            return StatusCaixa.EM_PROCESSO;
        }
    }
    
    private boolean estaDentroPrazoFechamento(LocalDate data) {
        if (data.isBefore(LocalDate.now())) {
            return false; // Não pode fechar dias anteriores
        }
        
        if (data.isEqual(LocalDate.now())) {
            LocalTime agora = LocalTime.now();
            LocalTime limite = LocalTime.of(22 + HORAS_LIMITE_FECHAMENTO, 0); // 22:00 + 2 horas = 00:00
            return agora.isBefore(limite);
        }
        
        return true;
    }
    
    private BigDecimal getValorPorTipo(Map<String, Object> resumo, TipoPagamento tipo) {
        switch (tipo) {
            case DINHEIRO:
                return (BigDecimal) resumo.get("totalDinheiro");
            case CARTAO_CREDITO:
                return (BigDecimal) resumo.get("totalCartaoCredito");
            case CARTAO_DEBITO:
                return (BigDecimal) resumo.get("totalCartaoDebito");
            case PIX:
                return (BigDecimal) resumo.get("totalPix");
            case OUTROS:
                return (BigDecimal) resumo.get("totalOutros");
            default:
                return BigDecimal.ZERO;
        }
    }
    
    private void registrarFechamento(LocalDate data, Map<TipoPagamento, BigDecimal> valores, 
                                    String operador, String observacoes, Map<String, Object> validacao) {
        // Simulação - registraria fechamento no banco
        System.out.println("Registrando fechamento - Data: " + data + 
                          ", Operador: " + operador + 
                          ", Divergência: " + validacao.get("divergencia"));
    }
    
    private void atualizarStatusCaixa(LocalDate data, StatusCaixa status) {
        // Simulação - atualizaria status no banco
        System.out.println("Atualizando status do caixa - Data: " + data + ", Status: " + status);
    }
}
