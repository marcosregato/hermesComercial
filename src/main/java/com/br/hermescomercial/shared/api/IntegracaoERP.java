package com.br.hermescomercial.shared.api;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface de integração entre PDV e ERP
 * Define contratos para comunicação entre sistemas
 * Versão 2.3.0 - Arquitetura Modular
 */
public interface IntegracaoERP {
    
    // ==================== PRODUTOS ====================
    
    /**
     * Sincroniza produtos do ERP para o PDV
     */
    void sincronizarProdutos();
    
    /**
     * Envia venda do PDV para o ERP
     */
    void registrarVenda(VendaDTO venda);
    
    /**
     * Atualiza estoque no ERP após venda
     */
    void atualizarEstoque(String produtoCodigo, int quantidade);
    
    // ==================== CLIENTES ====================
    
    /**
     * Busca cliente no ERP
     */
    ClienteDTO buscarCliente(String cpfCnpj);
    
    /**
     * Cadastra novo cliente no ERP
     */
    ClienteDTO cadastrarCliente(ClienteDTO cliente);
    
    /**
     * Sincroniza clientes do ERP para o PDV
     */
    void sincronizarClientes();
    
    // ==================== FINANCEIRO ====================
    
    /**
     * Registra pagamento no ERP
     */
    void registrarPagamento(PagamentoDTO pagamento);
    
    /**
     * Busca contas a pagar/receber
     */
    List<ContasDTO> buscarContas(String tipo);
    
    // ==================== ESTOQUE ====================
    
    /**
     * Verifica disponibilidade de estoque
     */
    boolean verificarEstoque(String produtoCodigo, int quantidade);
    
    /**
     * Gera alerta de estoque baixo
     */
    void gerarAlertaEstoque(String produtoCodigo, int quantidadeAtual);
    
    // ==================== RELATÓRIOS ====================
    
    /**
     * Envia dados de vendas para relatório do ERP
     */
    void enviarDadosVendas(RelatorioVendasDTO dados);
    
    /**
     * Solicita relatório financeiro do ERP
     */
    RelatorioFinanceiroDTO gerarRelatorioFinanceiro(LocalDateTime inicio, LocalDateTime fim);
    
    // ==================== NOTIFICAÇÕES ====================
    
    /**
     * Envia notificação do PDV para o ERP
     */
    void enviarNotificacao(NotificacaoDTO notificacao);
    
    /**
     * Busca notificações do ERP
     */
    List<NotificacaoDTO> buscarNotificacao(String usuarioDestino);
    
    // ==================== DTOs ====================
    
    /**
     * DTO para transferência de dados de venda
     */
    class VendaDTO {
        private Long id;
        private LocalDateTime dataVenda;
        private String clienteCpfCnpj;
        private BigDecimal valorTotal;
        private String formaPagamento;
        private List<ItemVendaDTO> itens;
        
        // Getters e Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public LocalDateTime getDataVenda() { return dataVenda; }
        public void setDataVenda(LocalDateTime dataVenda) { this.dataVenda = dataVenda; }
        
        public String getClienteCpfCnpj() { return clienteCpfCnpj; }
        public void setClienteCpfCnpj(String clienteCpfCnpj) { this.clienteCpfCnpj = clienteCpfCnpj; }
        
        public BigDecimal getValorTotal() { return valorTotal; }
        public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
        
        public String getFormaPagamento() { return formaPagamento; }
        public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }
        
        public List<ItemVendaDTO> getItens() { return itens; }
        public void setItens(List<ItemVendaDTO> itens) { this.itens = itens; }
    }
    
    /**
     * DTO para itens de venda
     */
    class ItemVendaDTO {
        private String produtoCodigo;
        private String produtoDescricao;
        private int quantidade;
        private BigDecimal valorUnitario;
        private BigDecimal valorTotal;
        
        // Getters e Setters
        public String getProdutoCodigo() { return produtoCodigo; }
        public void setProdutoCodigo(String produtoCodigo) { this.produtoCodigo = produtoCodigo; }
        
        public String getProdutoDescricao() { return produtoDescricao; }
        public void setProdutoDescricao(String produtoDescricao) { this.produtoDescricao = produtoDescricao; }
        
        public int getQuantidade() { return quantidade; }
        public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
        
        public BigDecimal getValorUnitario() { return valorUnitario; }
        public void setValorUnitario(BigDecimal valorUnitario) { this.valorUnitario = valorUnitario; }
        
        public BigDecimal getValorTotal() { return valorTotal; }
        public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    }
    
    /**
     * DTO para dados de cliente
     */
    class ClienteDTO {
        private Long id;
        private String nome;
        private String cpfCnpj;
        private String telefone;
        private String email;
        private String endereco;
        
        // Getters e Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        
        public String getCpfCnpj() { return cpfCnpj; }
        public void setCpfCnpj(String cpfCnpj) { this.cpfCnpj = cpfCnpj; }
        
        public String getTelefone() { return telefone; }
        public void setTelefone(String telefone) { this.telefone = telefone; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getEndereco() { return endereco; }
        public void setEndereco(String endereco) { this.endereco = endereco; }
    }
    
    /**
     * DTO para dados de pagamento
     */
    class PagamentoDTO {
        private Long id;
        private BigDecimal valor;
        private String tipo; // ENTRADA, SAIDA
        private String formaPagamento;
        private String descricao;
        private LocalDateTime dataPagamento;
        private String categoria;
        
        // Getters e Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public BigDecimal getValor() { return valor; }
        public void setValor(BigDecimal valor) { this.valor = valor; }
        
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        
        public String getFormaPagamento() { return formaPagamento; }
        public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }
        
        public String getDescricao() { return descricao; }
        public void setDescricao(String descricao) { this.descricao = descricao; }
        
        public LocalDateTime getDataPagamento() { return dataPagamento; }
        public void setDataPagamento(LocalDateTime dataPagamento) { this.dataPagamento = dataPagamento; }
        
        public String getCategoria() { return categoria; }
        public void setCategoria(String categoria) { this.categoria = categoria; }
    }
    
    /**
     * DTO para contas a pagar/receber
     */
    class ContasDTO {
        private Long id;
        private String descricao;
        private BigDecimal valor;
        private String tipo; // PAGAR, RECEBER
        private LocalDateTime dataVencimento;
        private LocalDateTime dataPagamento;
        private String status; // PENDENTE, PAGO, ATRASADO
        
        // Getters e Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getDescricao() { return descricao; }
        public void setDescricao(String descricao) { this.descricao = descricao; }
        
        public BigDecimal getValor() { return valor; }
        public void setValor(BigDecimal valor) { this.valor = valor; }
        
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        
        public LocalDateTime getDataVencimento() { return dataVencimento; }
        public void setDataVencimento(LocalDateTime dataVencimento) { this.dataVencimento = dataVencimento; }
        
        public LocalDateTime getDataPagamento() { return dataPagamento; }
        public void setDataPagamento(LocalDateTime dataPagamento) { this.dataPagamento = dataPagamento; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
    
    /**
     * DTO para relatório de vendas
     */
    class RelatorioVendasDTO {
        private LocalDateTime periodoInicio;
        private LocalDateTime periodoFim;
        private BigDecimal totalVendas;
        private int totalTransacoes;
        private BigDecimal ticketMedio;
        private List<VendaDTO> vendas;
        
        // Getters e Setters
        public LocalDateTime getPeriodoInicio() { return periodoInicio; }
        public void setPeriodoInicio(LocalDateTime periodoInicio) { this.periodoInicio = periodoInicio; }
        
        public LocalDateTime getPeriodoFim() { return periodoFim; }
        public void setPeriodoFim(LocalDateTime periodoFim) { this.periodoFim = periodoFim; }
        
        public BigDecimal getTotalVendas() { return totalVendas; }
        public void setTotalVendas(BigDecimal totalVendas) { this.totalVendas = totalVendas; }
        
        public int getTotalTransacoes() { return totalTransacoes; }
        public void setTotalTransacoes(int totalTransacoes) { this.totalTransacoes = totalTransacoes; }
        
        public BigDecimal getTicketMedio() { return ticketMedio; }
        public void setTicketMedio(BigDecimal ticketMedio) { this.ticketMedio = ticketMedio; }
        
        public List<VendaDTO> getVendas() { return vendas; }
        public void setVendas(List<VendaDTO> vendas) { this.vendas = vendas; }
    }
    
    /**
     * DTO para relatório financeiro
     */
    class RelatorioFinanceiroDTO {
        private LocalDateTime periodoInicio;
        private LocalDateTime periodoFim;
        private BigDecimal totalEntradas;
        private BigDecimal totalSaidas;
        private BigDecimal saldo;
        private List<ContasDTO> contas;
        
        // Getters e Setters
        public LocalDateTime getPeriodoInicio() { return periodoInicio; }
        public void setPeriodoInicio(LocalDateTime periodoInicio) { this.periodoInicio = periodoInicio; }
        
        public LocalDateTime getPeriodoFim() { return periodoFim; }
        public void setPeriodoFim(LocalDateTime periodoFim) { this.periodoFim = periodoFim; }
        
        public BigDecimal getTotalEntradas() { return totalEntradas; }
        public void setTotalEntradas(BigDecimal totalEntradas) { this.totalEntradas = totalEntradas; }
        
        public BigDecimal getTotalSaidas() { return totalSaidas; }
        public void setTotalSaidas(BigDecimal totalSaidas) { this.totalSaidas = totalSaidas; }
        
        public BigDecimal getSaldo() { return saldo; }
        public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }
        
        public List<ContasDTO> getContas() { return contas; }
        public void setContas(List<ContasDTO> contas) { this.contas = contas; }
    }
    
    /**
     * DTO para notificações
     */
    class NotificacaoDTO {
        private Long id;
        private String titulo;
        private String mensagem;
        private String tipo; // SISTEMA, VENDA, ESTOQUE, CLIENTE, FINANCEIRO
        private LocalDateTime dataCriacao;
        private boolean lida;
        private String usuarioDestino;
        private String prioridade; // BAIXA, MEDIA, ALTA, URGENTE
        
        // Getters e Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getTitulo() { return titulo; }
        public void setTitulo(String titulo) { this.titulo = titulo; }
        
        public String getMensagem() { return mensagem; }
        public void setMensagem(String mensagem) { this.mensagem = mensagem; }
        
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        
        public LocalDateTime getDataCriacao() { return dataCriacao; }
        public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
        
        public boolean isLida() { return lida; }
        public void setLida(boolean lida) { this.lida = lida; }
        
        public String getUsuarioDestino() { return usuarioDestino; }
        public void setUsuarioDestino(String usuarioDestino) { this.usuarioDestino = usuarioDestino; }
        
        public String getPrioridade() { return prioridade; }
        public void setPrioridade(String prioridade) { this.prioridade = prioridade; }
    }
}
