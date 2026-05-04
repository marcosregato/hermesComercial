package com.br.hermescomercial.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Serviço de gestão financeira - contas a pagar/receber e fluxo de caixa
 * @author Hermes Comercial
 * @version 2.8.0
 */
public class FinanceiroService {
    
    public enum TipoTransacao {
        RECEITA,        // Entrada de dinheiro
        DESPESA,        // Saída de dinheiro
        PAGAMENTO,      // Pagamento de conta
        RECEBIMENTO,    // Recebimento de conta
        TRANSFERENCIA   // Transferência entre contas
    }
    
    public enum StatusTransacao {
        PENDENTE,       // Aguardando pagamento/recebimento
        PAGO,           // Pago/Recebido
        VENCIDO,        // Data de vencimento passou
        CANCELADO,      // Transação cancelada
        PARCIAL         // Pago parcialmente
    }
    
    public enum CategoriaFinanceira {
        VENDAS,         // Receitas de vendas
        SERVICOS,       // Receitas de serviços
        COMPRAS,        // Despesas de compras
        ALUGUEL,        // Aluguel
        SALARIOS,       // Salários
        IMPOSTOS,       // Impostos e taxas
        ENERGIA,        // Energia elétrica
        AGUA,           // Água
        TELEFONE,       // Telefone/internet
        MARKETING,      // Marketing e propaganda
        MANUTENCAO,     // Manutenção
        OUTROS          // Outras despesas/receitas
    }
    
    public static class TransacaoFinanceira {
        private String id;
        private String descricao;
        private TipoTransacao tipo;
        private CategoriaFinanceira categoria;
        private BigDecimal valor;
        private LocalDate dataVencimento;
        private LocalDate dataPagamento;
        private LocalDate dataLancamento;
        private StatusTransacao status;
        private String numeroDocumento;
        private String fornecedorCliente;
        private String conta;
        private String formaPagamento;
        private BigDecimal valorPago;
        private String observacoes;
        
        public TransacaoFinanceira() {
            this.id = generateId();
            this.dataLancamento = LocalDate.now();
            this.status = StatusTransacao.PENDENTE;
            this.valorPago = BigDecimal.ZERO;
        }
        
        private String generateId() {
            return "FIN_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
        }
        
        // Getters e setters
        public String getId() { return id; }
        public String getDescricao() { return descricao; }
        public void setDescricao(String descricao) { this.descricao = descricao; }
        public TipoTransacao getTipo() { return tipo; }
        public void setTipo(TipoTransacao tipo) { this.tipo = tipo; }
        public CategoriaFinanceira getCategoria() { return categoria; }
        public void setCategoria(CategoriaFinanceira categoria) { this.categoria = categoria; }
        public BigDecimal getValor() { return valor; }
        public void setValor(BigDecimal valor) { this.valor = valor; }
        public LocalDate getDataVencimento() { return dataVencimento; }
        public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }
        public LocalDate getDataPagamento() { return dataPagamento; }
        public void setDataPagamento(LocalDate dataPagamento) { this.dataPagamento = dataPagamento; }
        public LocalDate getDataLancamento() { return dataLancamento; }
        public StatusTransacao getStatus() { return status; }
        public void setStatus(StatusTransacao status) { this.status = status; }
        public String getNumeroDocumento() { return numeroDocumento; }
        public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }
        public String getFornecedorCliente() { return fornecedorCliente; }
        public void setFornecedorCliente(String fornecedorCliente) { this.fornecedorCliente = fornecedorCliente; }
        public String getConta() { return conta; }
        public void setConta(String conta) { this.conta = conta; }
        public String getFormaPagamento() { return formaPagamento; }
        public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }
        public BigDecimal getValorPago() { return valorPago; }
        public void setValorPago(BigDecimal valorPago) { this.valorPago = valorPago; }
        public String getObservacoes() { return observacoes; }
        public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
        
        public boolean isVencida() {
            return dataVencimento != null && dataVencimento.isBefore(LocalDate.now()) && status == StatusTransacao.PENDENTE;
        }
        
        public boolean isProximoVencimento() {
            if (dataVencimento == null) return false;
            long dias = ChronoUnit.DAYS.between(LocalDate.now(), dataVencimento);
            return dias >= 0 && dias <= 7 && status == StatusTransacao.PENDENTE;
        }
        
        public BigDecimal getSaldoRestante() {
            return valor.subtract(valorPago);
        }
        
        public boolean isPagaParcialmente() {
            return valorPago.compareTo(BigDecimal.ZERO) > 0 && valorPago.compareTo(valor) < 0;
        }
    }
    
    public static class FluxoCaixa {
        private LocalDate data;
        private BigDecimal saldoInicial;
        private BigDecimal totalReceitas;
        private BigDecimal totalDespesas;
        private BigDecimal saldoFinal;
        private List<TransacaoFinanceira> transacoes;
        
        public FluxoCaixa(LocalDate data) {
            this.data = data;
            this.saldoInicial = BigDecimal.ZERO;
            this.totalReceitas = BigDecimal.ZERO;
            this.totalDespesas = BigDecimal.ZERO;
            this.transacoes = new ArrayList<>();
        }
        
        // Getters e setters
        public LocalDate getData() { return data; }
        public BigDecimal getSaldoInicial() { return saldoInicial; }
        public void setSaldoInicial(BigDecimal saldoInicial) { this.saldoInicial = saldoInicial; }
        public BigDecimal getTotalReceitas() { return totalReceitas; }
        public void setTotalReceitas(BigDecimal totalReceitas) { this.totalReceitas = totalReceitas; }
        public BigDecimal getTotalDespesas() { return totalDespesas; }
        public void setTotalDespesas(BigDecimal totalDespesas) { this.totalDespesas = totalDespesas; }
        public BigDecimal getSaldoFinal() { return saldoFinal; }
        public void setSaldoFinal(BigDecimal saldoFinal) { this.saldoFinal = saldoFinal; }
        public List<TransacaoFinanceira> getTransacoes() { return transacoes; }
        
        public void calcularSaldoFinal() {
            this.saldoFinal = saldoInicial.add(totalReceitas).subtract(totalDespesas);
        }
    }
    
    // Simulação de banco de dados
    private final List<TransacaoFinanceira> transacoes;
    
    public FinanceiroService() {
        this.transacoes = new ArrayList<>();
        inicializarDadosMock();
    }
    
    /**
     * Cria nova conta a pagar
     */
    public TransacaoFinanceira criarContaPagar(String descricao, BigDecimal valor, LocalDate dataVencimento,
                                               CategoriaFinanceira categoria, String fornecedor) {
        TransacaoFinanceira transacao = new TransacaoFinanceira();
        transacao.setDescricao(descricao);
        transacao.setTipo(TipoTransacao.DESPESA);
        transacao.setCategoria(categoria);
        transacao.setValor(valor);
        transacao.setDataVencimento(dataVencimento);
        transacao.setFornecedorCliente(fornecedor);
        
        transacoes.add(transacao);
        
        return transacao;
    }
    
    /**
     * Cria nova conta a receber
     */
    public TransacaoFinanceira criarContaReceber(String descricao, BigDecimal valor, LocalDate dataVencimento,
                                                 CategoriaFinanceira categoria, String cliente) {
        TransacaoFinanceira transacao = new TransacaoFinanceira();
        transacao.setDescricao(descricao);
        transacao.setTipo(TipoTransacao.RECEITA);
        transacao.setCategoria(categoria);
        transacao.setValor(valor);
        transacao.setDataVencimento(dataVencimento);
        transacao.setFornecedorCliente(cliente);
        
        transacoes.add(transacao);
        
        return transacao;
    }
    
    /**
     * Registra pagamento de conta
     */
    public boolean registrarPagamento(String idTransacao, BigDecimal valorPago, String formaPagamento) {
        TransacaoFinanceira transacao = buscarTransacaoPorId(idTransacao);
        if (transacao == null) {
            return false;
        }
        
        transacao.setDataPagamento(LocalDate.now());
        transacao.setFormaPagamento(formaPagamento);
        transacao.setValorPago(transacao.getValorPago().add(valorPago));
        
        // Atualizar status
        if (transacao.getValorPago().compareTo(transacao.getValor()) >= 0) {
            transacao.setStatus(StatusTransacao.PAGO);
            transacao.setValorPago(transacao.getValor());
        } else {
            transacao.setStatus(StatusTransacao.PARCIAL);
        }
        
        return true;
    }
    
    /**
     * Busca transação por ID
     */
    public TransacaoFinanceira buscarTransacaoPorId(String id) {
        for (TransacaoFinanceira transacao : transacoes) {
            if (transacao.getId().equals(id)) {
                return transacao;
            }
        }
        return null;
    }
    
    /**
     * Lista contas a pagar
     */
    public List<TransacaoFinanceira> listarContasPagar() {
        List<TransacaoFinanceira> contasPagar = new ArrayList<>();
        
        for (TransacaoFinanceira transacao : transacoes) {
            if (transacao.getTipo() == TipoTransacao.DESPESA || transacao.getTipo() == TipoTransacao.PAGAMENTO) {
                contasPagar.add(transacao);
            }
        }
        
        return contasPagar;
    }
    
    /**
     * Lista contas a receber
     */
    public List<TransacaoFinanceira> listarContasReceber() {
        List<TransacaoFinanceira> contasReceber = new ArrayList<>();
        
        for (TransacaoFinanceira transacao : transacoes) {
            if (transacao.getTipo() == TipoTransacao.RECEITA || transacao.getTipo() == TipoTransacao.RECEBIMENTO) {
                contasReceber.add(transacao);
            }
        }
        
        return contasReceber;
    }
    
    /**
     * Lista contas vencidas
     */
    public List<TransacaoFinanceira> listarContasVencidas() {
        List<TransacaoFinanceira> vencidas = new ArrayList<>();
        
        for (TransacaoFinanceira transacao : transacoes) {
            if (transacao.isVencida()) {
                vencidas.add(transacao);
            }
        }
        
        return vencidas;
    }
    
    /**
     * Lista contas com vencimento próximo (7 dias)
     */
    public List<TransacaoFinanceira> listarContasVencimentoProximo() {
        List<TransacaoFinanceira> proximas = new ArrayList<>();
        
        for (TransacaoFinanceira transacao : transacoes) {
            if (transacao.isProximoVencimento()) {
                proximas.add(transacao);
            }
        }
        
        return proximas;
    }
    
    /**
     * Gera fluxo de caixa do período
     */
    public FluxoCaixa gerarFluxoCaixa(LocalDate dataInicio, LocalDate dataFim) {
        FluxoCaixa fluxo = new FluxoCaixa(dataInicio);
        
        BigDecimal totalReceitas = BigDecimal.ZERO;
        BigDecimal totalDespesas = BigDecimal.ZERO;
        
        for (LocalDate data = dataInicio; !data.isAfter(dataFim); data = data.plusDays(1)) {
            List<TransacaoFinanceira> transacoesDia = getTransacoesPorData(data);
            
            for (TransacaoFinanceira transacao : transacoesDia) {
                fluxo.getTransacoes().add(transacao);
                
                if (transacao.getTipo() == TipoTransacao.RECEITA || transacao.getTipo() == TipoTransacao.RECEBIMENTO) {
                    totalReceitas = totalReceitas.add(transacao.getValor());
                } else {
                    totalDespesas = totalDespesas.add(transacao.getValor());
                }
            }
        }
        
        fluxo.setTotalReceitas(totalReceitas);
        fluxo.setTotalDespesas(totalDespesas);
        fluxo.calcularSaldoFinal();
        
        return fluxo;
    }
    
    /**
     * Obtém resumo financeiro do mês
     */
    public Map<String, Object> getResumoFinanceiroMes(LocalDate mes) {
        Map<String, Object> resumo = new HashMap<>();
        
        LocalDate inicio = mes.withDayOfMonth(1);
        LocalDate fim = mes.withDayOfMonth(mes.lengthOfMonth());
        
        List<TransacaoFinanceira> transacoesMes = getTransacoesPorPeriodo(inicio, fim);
        
        BigDecimal totalReceitas = BigDecimal.ZERO;
        BigDecimal totalDespesas = BigDecimal.ZERO;
        BigDecimal receitasRecebidas = BigDecimal.ZERO;
        BigDecimal despesasPagas = BigDecimal.ZERO;
        
        Map<CategoriaFinanceira, BigDecimal> receitasPorCategoria = new HashMap<>();
        Map<CategoriaFinanceira, BigDecimal> despesasPorCategoria = new HashMap<>();
        
        for (TransacaoFinanceira transacao : transacoesMes) {
            if (transacao.getTipo() == TipoTransacao.RECEITA || transacao.getTipo() == TipoTransacao.RECEBIMENTO) {
                totalReceitas = totalReceitas.add(transacao.getValor());
                if (transacao.getStatus() == StatusTransacao.PAGO) {
                    receitasRecebidas = receitasRecebidas.add(transacao.getValor());
                }
                
                receitasPorCategoria.merge(transacao.getCategoria(), transacao.getValor(), BigDecimal::add);
            } else {
                totalDespesas = totalDespesas.add(transacao.getValor());
                if (transacao.getStatus() == StatusTransacao.PAGO) {
                    despesasPagas = despesasPagas.add(transacao.getValor());
                }
                
                despesasPorCategoria.merge(transacao.getCategoria(), transacao.getValor(), BigDecimal::add);
            }
        }
        
        BigDecimal saldoLiquido = totalReceitas.subtract(totalDespesas);
        BigDecimal saldoCaixa = receitasRecebidas.subtract(despesasPagas);
        
        resumo.put("mes", mes.format(DateTimeFormatter.ofPattern("MMMM/yyyy")));
        resumo.put("totalReceitas", totalReceitas);
        resumo.put("totalDespesas", totalDespesas);
        resumo.put("saldoLiquido", saldoLiquido);
        resumo.put("receitasRecebidas", receitasRecebidas);
        resumo.put("despesasPagas", despesasPagas);
        resumo.put("saldoCaixa", saldoCaixa);
        resumo.put("receitasPorCategoria", receitasPorCategoria);
        resumo.put("despesasPorCategoria", despesasPorCategoria);
        
        // Percentuais
        if (totalReceitas.compareTo(BigDecimal.ZERO) > 0) {
            resumo.put("percentualRecebidas", receitasRecebidas.divide(totalReceitas, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)));
        }
        
        if (totalDespesas.compareTo(BigDecimal.ZERO) > 0) {
            resumo.put("percentualPagas", despesasPagas.divide(totalDespesas, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)));
        }
        
        return resumo;
    }
    
    /**
     * Obtém projeção de fluxo de caixa
     */
    public Map<String, Object> getProjecaoFluxoCaixa(int dias) {
        Map<String, Object> projecao = new HashMap<>();
        
        LocalDate hoje = LocalDate.now();
        LocalDate dataFim = hoje.plusDays(dias);
        
        BigDecimal projetadoReceber = BigDecimal.ZERO;
        BigDecimal projetadoPagar = BigDecimal.ZERO;
        
        for (LocalDate data = hoje; !data.isAfter(dataFim); data = data.plusDays(1)) {
            List<TransacaoFinanceira> transacoesDia = getTransacoesPorData(data);
            
            for (TransacaoFinanceira transacao : transacoesDia) {
                if (transacao.getStatus() == StatusTransacao.PENDENTE) {
                    if (transacao.getTipo() == TipoTransacao.RECEITA || transacao.getTipo() == TipoTransacao.RECEBIMENTO) {
                        projetadoReceber = projetadoReceber.add(transacao.getValor());
                    } else {
                        projetadoPagar = projetadoPagar.add(transacao.getValor());
                    }
                }
            }
        }
        
        BigDecimal saldoProjetado = projetadoReceber.subtract(projetadoPagar);
        
        projecao.put("periodo", hoje.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " a " + dataFim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        projecao.put("dias", dias);
        projecao.put("projetadoReceber", projetadoReceber);
        projecao.put("projetadoPagar", projetadoPagar);
        projecao.put("saldoProjetado", saldoProjetado);
        
        // Alertas
        List<String> alertas = new ArrayList<>();
        
        if (saldoProjetado.compareTo(BigDecimal.ZERO) < 0) {
            alertas.add("⚠️ Saldo projetado negativo: necessidade de capital de giro");
        }
        
        List<TransacaoFinanceira> vencidas = listarContasVencidas();
        if (!vencidas.isEmpty()) {
            BigDecimal totalVencidas = BigDecimal.ZERO;
            for (TransacaoFinanceira v : vencidas) {
                totalVencidas = totalVencidas.add(v.getSaldoRestante());
            }
            alertas.add("🔴 " + vencidas.size() + " contas vencidas no valor de R$ " + totalVencidas);
        }
        
        projecao.put("alertas", alertas);
        
        return projecao;
    }
    
    /**
     * Obtém estatísticas financeiras
     */
    public Map<String, Object> getEstatisticasFinanceiras() {
        Map<String, Object> estatisticas = new HashMap<>();
        
        // Totais
        BigDecimal totalReceitas = BigDecimal.ZERO;
        BigDecimal totalDespesas = BigDecimal.ZERO;
        BigDecimal totalVencidas = BigDecimal.ZERO;
        BigDecimal totalProximoVencimento = BigDecimal.ZERO;
        
        int contasPagar = 0;
        int contasReceber = 0;
        int contasVencidas = 0;
        int contasProximoVencimento = 0;
        
        Map<CategoriaFinanceira, BigDecimal> despesasPorCategoria = new HashMap<>();
        
        for (TransacaoFinanceira transacao : transacoes) {
            if (transacao.getTipo() == TipoTransacao.RECEITA || transacao.getTipo() == TipoTransacao.RECEBIMENTO) {
                totalReceitas = totalReceitas.add(transacao.getValor());
                contasReceber++;
            } else {
                totalDespesas = totalDespesas.add(transacao.getValor());
                contasPagar++;
                
                despesasPorCategoria.merge(transacao.getCategoria(), transacao.getValor(), BigDecimal::add);
            }
            
            if (transacao.isVencida()) {
                totalVencidas = totalVencidas.add(transacao.getSaldoRestante());
                contasVencidas++;
            }
            
            if (transacao.isProximoVencimento()) {
                totalProximoVencimento = totalProximoVencimento.add(transacao.getSaldoRestante());
                contasProximoVencimento++;
            }
        }
        
        BigDecimal saldoAtual = totalReceitas.subtract(totalDespesas);
        
        estatisticas.put("totalReceitas", totalReceitas);
        estatisticas.put("totalDespesas", totalDespesas);
        estatisticas.put("saldoAtual", saldoAtual);
        estatisticas.put("contasPagar", contasPagar);
        estatisticas.put("contasReceber", contasReceber);
        estatisticas.put("contasVencidas", contasVencidas);
        estatisticas.put("totalVencidas", totalVencidas);
        estatisticas.put("contasProximoVencimento", contasProximoVencimento);
        estatisticas.put("totalProximoVencimento", totalProximoVencimento);
        estatisticas.put("despesasPorCategoria", despesasPorCategoria);
        
        // Maior categoria de despesas
        String maiorCategoria = "Nenhuma";
        BigDecimal maiorValor = BigDecimal.ZERO;
        
        for (Map.Entry<CategoriaFinanceira, BigDecimal> entry : despesasPorCategoria.entrySet()) {
            if (entry.getValue().compareTo(maiorValor) > 0) {
                maiorValor = entry.getValue();
                maiorCategoria = entry.getKey().toString();
            }
        }
        
        estatisticas.put("maiorCategoriaDespesas", maiorCategoria);
        estatisticas.put("valorMaiorCategoria", maiorValor);
        
        return estatisticas;
    }
    
    // Métodos auxiliares
    private List<TransacaoFinanceira> getTransacoesPorData(LocalDate data) {
        List<TransacaoFinanceira> transacoesDia = new ArrayList<>();
        
        for (TransacaoFinanceira transacao : transacoes) {
            if ((transacao.getDataLancamento().isEqual(data) || 
                 (transacao.getDataVencimento() != null && transacao.getDataVencimento().isEqual(data)) ||
                 (transacao.getDataPagamento() != null && transacao.getDataPagamento().isEqual(data)))) {
                transacoesDia.add(transacao);
            }
        }
        
        return transacoesDia;
    }
    
    private List<TransacaoFinanceira> getTransacoesPorPeriodo(LocalDate inicio, LocalDate fim) {
        List<TransacaoFinanceira> transacoesPeriodo = new ArrayList<>();
        
        for (TransacaoFinanceira transacao : transacoes) {
            if (!transacao.getDataLancamento().isBefore(inicio) && !transacao.getDataLancamento().isAfter(fim)) {
                transacoesPeriodo.add(transacao);
            }
        }
        
        return transacoesPeriodo;
    }
    
    /**
     * Inicializa dados mock para teste
     */
    private void inicializarDadosMock() {
        // Contas a pagar de exemplo
        TransacaoFinanceira conta1 = new TransacaoFinanceira();
        conta1.setDescricao("Aluguel do comércio");
        conta1.setTipo(TipoTransacao.DESPESA);
        conta1.setCategoria(CategoriaFinanceira.ALUGUEL);
        conta1.setValor(BigDecimal.valueOf(3500.00));
        conta1.setDataVencimento(LocalDate.now().plusDays(5));
        conta1.setFornecedorCliente("Imobiliária Central");
        transacoes.add(conta1);
        
        TransacaoFinanceira conta2 = new TransacaoFinanceira();
        conta2.setDescricao("Compra de mercadorias");
        conta2.setTipo(TipoTransacao.DESPESA);
        conta2.setCategoria(CategoriaFinanceira.COMPRAS);
        conta2.setValor(BigDecimal.valueOf(8500.00));
        conta2.setDataVencimento(LocalDate.now().plusDays(10));
        conta2.setFornecedorCliente("Distribuidora ABC");
        transacoes.add(conta2);
        
        // Contas a receber de exemplo
        TransacaoFinanceira receita1 = new TransacaoFinanceira();
        receita1.setDescricao("Venda para Cliente A");
        receita1.setTipo(TipoTransacao.RECEITA);
        receita1.setCategoria(CategoriaFinanceira.VENDAS);
        receita1.setValor(BigDecimal.valueOf(12500.00));
        receita1.setDataVencimento(LocalDate.now().plusDays(3));
        receita1.setFornecedorCliente("João Silva");
        transacoes.add(receita1);
    }
}
