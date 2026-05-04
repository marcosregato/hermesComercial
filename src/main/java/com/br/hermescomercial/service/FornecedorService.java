package com.br.hermescomercial.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Serviço de gestão de fornecedores
 * @author Hermes Comercial
 * @version 2.8.0
 */
public class FornecedorService {
    
    public enum StatusFornecedor {
        ATIVO,          // Fornecedor ativo
        INATIVO,        // Fornecedor inativo temporariamente
        BLOQUEADO,      // Fornecedor bloqueado
        EM_ANALISE      // Em análise para aprovação
    }
    
    public enum TipoProduto {
        MERCADORIA,     // Produtos para revenda
        SERVICO,        // Serviços contratados
        MATERIAL,       // Matéria-prima
        EQUIPAMENTO     // Equipamentos
    }
    
    public static class Fornecedor {
        private String id;
        private String nome;
        private String cnpj;
        private String razaoSocial;
        private String nomeFantasia;
        private String email;
        private String telefone;
        private String celular;
        private String endereco;
        private String cidade;
        private String estado;
        private String cep;
        private StatusFornecedor status;
        private LocalDate dataCadastro;
        private LocalDate dataUltimaCompra;
        private BigDecimal valorUltimaCompra;
        private BigDecimal totalCompras;
        private int quantidadeCompras;
        private List<String> produtosFornecidos;
        private List<Pedido> historicoPedidos;
        private Map<String, Object> dadosAdicionais;
        
        public Fornecedor() {
            this.id = generateId();
            this.status = StatusFornecedor.EM_ANALISE;
            this.dataCadastro = LocalDate.now();
            this.totalCompras = BigDecimal.ZERO;
            this.quantidadeCompras = 0;
            this.produtosFornecidos = new ArrayList<>();
            this.historicoPedidos = new ArrayList<>();
            this.dadosAdicionais = new HashMap<>();
        }
        
        private String generateId() {
            return "FOR_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
        }
        
        // Getters e setters
        public String getId() { return id; }
        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
        public String getCnpj() { return cnpj; }
        public void setCnpj(String cnpj) { this.cnpj = cnpj; }
        public String getRazaoSocial() { return razaoSocial; }
        public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }
        public String getNomeFantasia() { return nomeFantasia; }
        public void setNomeFantasia(String nomeFantasia) { this.nomeFantasia = nomeFantasia; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getTelefone() { return telefone; }
        public void setTelefone(String telefone) { this.telefone = telefone; }
        public String getCelular() { return celular; }
        public void setCelular(String celular) { this.celular = celular; }
        public String getEndereco() { return endereco; }
        public void setEndereco(String endereco) { this.endereco = endereco; }
        public String getCidade() { return cidade; }
        public void setCidade(String cidade) { this.cidade = cidade; }
        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
        public String getCep() { return cep; }
        public void setCep(String cep) { this.cep = cep; }
        public StatusFornecedor getStatus() { return status; }
        public void setStatus(StatusFornecedor status) { this.status = status; }
        public LocalDate getDataCadastro() { return dataCadastro; }
        public LocalDate getDataUltimaCompra() { return dataUltimaCompra; }
        public void setDataUltimaCompra(LocalDate dataUltimaCompra) { this.dataUltimaCompra = dataUltimaCompra; }
        public BigDecimal getValorUltimaCompra() { return valorUltimaCompra; }
        public void setValorUltimaCompra(BigDecimal valorUltimaCompra) { this.valorUltimaCompra = valorUltimaCompra; }
        public BigDecimal getTotalCompras() { return totalCompras; }
        public void setTotalCompras(BigDecimal totalCompras) { this.totalCompras = totalCompras; }
        public int getQuantidadeCompras() { return quantidadeCompras; }
        public void setQuantidadeCompras(int quantidadeCompras) { this.quantidadeCompras = quantidadeCompras; }
        public List<String> getProdutosFornecidos() { return produtosFornecidos; }
        public List<Pedido> getHistoricoPedidos() { return historicoPedidos; }
        public Map<String, Object> getDadosAdicionais() { return dadosAdicionais; }
        
        public void adicionarProduto(String produto) {
            if (!produtosFornecidos.contains(produto)) {
                produtosFornecidos.add(produto);
            }
        }
        
        public void adicionarPedido(Pedido pedido) {
            historicoPedidos.add(pedido);
            quantidadeCompras++;
            totalCompras = totalCompras.add(pedido.getValorTotal());
            dataUltimaCompra = pedido.getDataPedido();
            valorUltimaCompra = pedido.getValorTotal();
        }
    }
    
    public static class Pedido {
        private String id;
        private String idFornecedor;
        private LocalDate dataPedido;
        private LocalDate dataEntrega;
        private LocalDate dataPrevistaEntrega;
        private String status;
        private BigDecimal valorTotal;
        private List<ItemPedido> itens;
        private String observacoes;
        private String responsavel;
        
        public Pedido() {
            this.id = generateId();
            this.dataPedido = LocalDate.now();
            this.status = "PENDENTE";
            this.itens = new ArrayList<>();
        }
        
        private String generateId() {
            return "PED_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
        }
        
        // Getters e setters
        public String getId() { return id; }
        public String getIdFornecedor() { return idFornecedor; }
        public void setIdFornecedor(String idFornecedor) { this.idFornecedor = idFornecedor; }
        public LocalDate getDataPedido() { return dataPedido; }
        public void setDataPedido(LocalDate dataPedido) { this.dataPedido = dataPedido; }
        public LocalDate getDataEntrega() { return dataEntrega; }
        public void setDataEntrega(LocalDate dataEntrega) { this.dataEntrega = dataEntrega; }
        public LocalDate getDataPrevistaEntrega() { return dataPrevistaEntrega; }
        public void setDataPrevistaEntrega(LocalDate dataPrevistaEntrega) { this.dataPrevistaEntrega = dataPrevistaEntrega; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public BigDecimal getValorTotal() { return valorTotal; }
        public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
        public List<ItemPedido> getItens() { return itens; }
        public String getObservacoes() { return observacoes; }
        public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
        public String getResponsavel() { return responsavel; }
        public void setResponsavel(String responsavel) { this.responsavel = responsavel; }
        
        public void adicionarItem(ItemPedido item) {
            itens.add(item);
            calcularValorTotal();
        }
        
        private void calcularValorTotal() {
            BigDecimal total = BigDecimal.ZERO;
            for (ItemPedido item : itens) {
                total = total.add(item.getValorTotal());
            }
            this.valorTotal = total;
        }
    }
    
    public static class ItemPedido {
        private String id;
        private String codigoProduto;
        private String nomeProduto;
        private int quantidade;
        private BigDecimal precoUnitario;
        private BigDecimal valorTotal;
        private TipoProduto tipo;
        
        public ItemPedido() {
            this.id = generateId();
        }
        
        private String generateId() {
            return "ITM_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
        }
        
        // Getters e setters
        public String getId() { return id; }
        public String getCodigoProduto() { return codigoProduto; }
        public void setCodigoProduto(String codigoProduto) { this.codigoProduto = codigoProduto; }
        public String getNomeProduto() { return nomeProduto; }
        public void setNomeProduto(String nomeProduto) { this.nomeProduto = nomeProduto; }
        public int getQuantidade() { return quantidade; }
        public void setQuantidade(int quantidade) { 
            this.quantidade = quantidade;
            calcularValorTotal();
        }
        public BigDecimal getPrecoUnitario() { return precoUnitario; }
        public void setPrecoUnitario(BigDecimal precoUnitario) { 
            this.precoUnitario = precoUnitario;
            calcularValorTotal();
        }
        public BigDecimal getValorTotal() { return valorTotal; }
        public TipoProduto getTipo() { return tipo; }
        public void setTipo(TipoProduto tipo) { this.tipo = tipo; }
        
        private void calcularValorTotal() {
            if (quantidade > 0 && precoUnitario != null) {
                this.valorTotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
            }
        }
    }
    
    // Simulação de banco de dados
    private final List<Fornecedor> fornecedores;
    private final List<Pedido> pedidos;
    
    public FornecedorService() {
        this.fornecedores = new ArrayList<>();
        this.pedidos = new ArrayList<>();
        inicializarDadosMock();
    }
    
    /**
     * Cadastra novo fornecedor
     */
    public Fornecedor cadastrarFornecedor(String nome, String cnpj, String razaoSocial, 
                                          String email, String telefone, String endereco) {
        // Validar CNPJ
        if (!validarCNPJ(cnpj)) {
            throw new IllegalArgumentException("CNPJ inválido");
        }
        
        // Verificar se já existe
        if (buscarFornecedorPorCNPJ(cnpj) != null) {
            throw new IllegalArgumentException("Fornecedor com este CNPJ já cadastrado");
        }
        
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome(nome);
        fornecedor.setCnpj(cnpj);
        fornecedor.setRazaoSocial(razaoSocial);
        fornecedor.setEmail(email);
        fornecedor.setTelefone(telefone);
        fornecedor.setEndereco(endereco);
        
        fornecedores.add(fornecedor);
        
        return fornecedor;
    }
    
    /**
     * Busca fornecedor por ID
     */
    public Fornecedor buscarFornecedorPorId(String id) {
        for (Fornecedor fornecedor : fornecedores) {
            if (fornecedor.getId().equals(id)) {
                return fornecedor;
            }
        }
        return null;
    }
    
    /**
     * Busca fornecedor por CNPJ
     */
    public Fornecedor buscarFornecedorPorCNPJ(String cnpj) {
        for (Fornecedor fornecedor : fornecedores) {
            if (fornecedor.getCnpj() != null && fornecedor.getCnpj().equals(cnpj)) {
                return fornecedor;
            }
        }
        return null;
    }
    
    /**
     * Busca fornecedores por nome
     */
    public List<Fornecedor> buscarFornecedoresPorNome(String nome) {
        List<Fornecedor> resultado = new ArrayList<>();
        
        for (Fornecedor fornecedor : fornecedores) {
            if (fornecedor.getNome() != null && 
                fornecedor.getNome().toLowerCase().contains(nome.toLowerCase())) {
                resultado.add(fornecedor);
            }
        }
        
        return resultado;
    }
    
    /**
     * Lista todos os fornecedores
     */
    public List<Fornecedor> listarTodosFornecedores() {
        return new ArrayList<>(fornecedores);
    }
    
    /**
     * Lista fornecedores por status
     */
    public List<Fornecedor> listarFornecedoresPorStatus(StatusFornecedor status) {
        List<Fornecedor> resultado = new ArrayList<>();
        
        for (Fornecedor fornecedor : fornecedores) {
            if (fornecedor.getStatus() == status) {
                resultado.add(fornecedor);
            }
        }
        
        return resultado;
    }
    
    /**
     * Atualiza dados do fornecedor
     */
    public boolean atualizarFornecedor(Fornecedor fornecedor) {
        for (int i = 0; i < fornecedores.size(); i++) {
            if (fornecedores.get(i).getId().equals(fornecedor.getId())) {
                fornecedores.set(i, fornecedor);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Altera status do fornecedor
     */
    public boolean alterarStatusFornecedor(String idFornecedor, StatusFornecedor novoStatus) {
        Fornecedor fornecedor = buscarFornecedorPorId(idFornecedor);
        if (fornecedor != null) {
            fornecedor.setStatus(novoStatus);
            return true;
        }
        return false;
    }
    
    /**
     * Cria novo pedido
     */
    public Pedido criarPedido(String idFornecedor, LocalDate dataPrevistaEntrega, String responsavel) {
        Fornecedor fornecedor = buscarFornecedorPorId(idFornecedor);
        if (fornecedor == null) {
            throw new IllegalArgumentException("Fornecedor não encontrado");
        }
        
        if (fornecedor.getStatus() != StatusFornecedor.ATIVO) {
            throw new IllegalArgumentException("Fornecedor não está ativo");
        }
        
        Pedido pedido = new Pedido();
        pedido.setIdFornecedor(idFornecedor);
        pedido.setDataPrevistaEntrega(dataPrevistaEntrega);
        pedido.setResponsavel(responsavel);
        
        pedidos.add(pedido);
        fornecedor.adicionarPedido(pedido);
        
        return pedido;
    }
    
    /**
     * Adiciona item ao pedido
     */
    public boolean adicionarItemPedido(String idPedido, String codigoProduto, String nomeProduto, 
                                      int quantidade, BigDecimal precoUnitario, TipoProduto tipo) {
        Pedido pedido = buscarPedidoPorId(idPedido);
        if (pedido == null) {
            return false;
        }
        
        ItemPedido item = new ItemPedido();
        item.setCodigoProduto(codigoProduto);
        item.setNomeProduto(nomeProduto);
        item.setQuantidade(quantidade);
        item.setPrecoUnitario(precoUnitario);
        item.setTipo(tipo);
        
        pedido.adicionarItem(item);
        
        // Adicionar produto à lista do fornecedor
        Fornecedor fornecedor = buscarFornecedorPorId(pedido.getIdFornecedor());
        if (fornecedor != null) {
            fornecedor.adicionarProduto(codigoProduto);
        }
        
        return true;
    }
    
    /**
     * Busca pedido por ID
     */
    public Pedido buscarPedidoPorId(String id) {
        for (Pedido pedido : pedidos) {
            if (pedido.getId().equals(id)) {
                return pedido;
            }
        }
        return null;
    }
    
    /**
     * Lista pedidos do fornecedor
     */
    public List<Pedido> listarPedidosFornecedor(String idFornecedor) {
        List<Pedido> resultado = new ArrayList<>();
        
        for (Pedido pedido : pedidos) {
            if (pedido.getIdFornecedor().equals(idFornecedor)) {
                resultado.add(pedido);
            }
        }
        
        return resultado;
    }
    
    /**
     * Atualiza status do pedido
     */
    public boolean atualizarStatusPedido(String idPedido, String novoStatus) {
        Pedido pedido = buscarPedidoPorId(idPedido);
        if (pedido != null) {
            pedido.setStatus(novoStatus);
            
            if ("ENTREGUE".equals(novoStatus)) {
                pedido.setDataEntrega(LocalDate.now());
            }
            
            return true;
        }
        return false;
    }
    
    /**
     * Obtém estatísticas dos fornecedores
     */
    public Map<String, Object> getEstatisticasFornecedores() {
        Map<String, Object> estatisticas = new HashMap<>();
        
        int totalFornecedores = fornecedores.size();
        int fornecedoresAtivos = 0;
        int fornecedoresInativos = 0;
        int fornecedoresBloqueados = 0;
        int fornecedoresEmAnalise = 0;
        
        BigDecimal totalCompras = BigDecimal.ZERO;
        BigDecimal mediaCompras = BigDecimal.ZERO;
        int totalPedidos = 0;
        
        for (Fornecedor fornecedor : fornecedores) {
            switch (fornecedor.getStatus()) {
                case ATIVO:
                    fornecedoresAtivos++;
                    break;
                case INATIVO:
                    fornecedoresInativos++;
                    break;
                case BLOQUEADO:
                    fornecedoresBloqueados++;
                    break;
                case EM_ANALISE:
                    fornecedoresEmAnalise++;
                    break;
            }
            
            totalCompras = totalCompras.add(fornecedor.getTotalCompras());
            totalPedidos += fornecedor.getQuantidadeCompras();
        }
        
        if (totalFornecedores > 0) {
            mediaCompras = totalCompras.divide(BigDecimal.valueOf(totalFornecedores), 2, RoundingMode.HALF_UP);
        }
        
        estatisticas.put("totalFornecedores", totalFornecedores);
        estatisticas.put("fornecedoresAtivos", fornecedoresAtivos);
        estatisticas.put("fornecedoresInativos", fornecedoresInativos);
        estatisticas.put("fornecedoresBloqueados", fornecedoresBloqueados);
        estatisticas.put("fornecedoresEmAnalise", fornecedoresEmAnalise);
        estatisticas.put("totalCompras", totalCompras);
        estatisticas.put("mediaComprasPorFornecedor", mediaCompras);
        estatisticas.put("totalPedidos", totalPedidos);
        
        // Melhores fornecedores
        List<Fornecedor> melhoresFornecedores = new ArrayList<>(fornecedores);
        melhoresFornecedores.sort((f1, f2) -> f2.getTotalCompras().compareTo(f1.getTotalCompras()));
        
        List<Map<String, Object>> top5 = new ArrayList<>();
        for (int i = 0; i < Math.min(5, melhoresFornecedores.size()); i++) {
            Fornecedor f = melhoresFornecedores.get(i);
            Map<String, Object> dados = new HashMap<>();
            dados.put("nome", f.getNome());
            dados.put("totalCompras", f.getTotalCompras());
            dados.put("quantidadePedidos", f.getQuantidadeCompras());
            top5.add(dados);
        }
        estatisticas.put("top5Fornecedores", top5);
        
        return estatisticas;
    }
    
    /**
     * Obtém fornecedores que precisam de atenção
     */
    public List<Fornecedor> getFornecedoresAtencao() {
        List<Fornecedor> atencao = new ArrayList<>();
        
        for (Fornecedor fornecedor : fornecedores) {
            // Fornecedores sem compras há mais de 90 dias
            if (fornecedor.getDataUltimaCompra() != null) {
                long diasSemCompra = java.time.temporal.ChronoUnit.DAYS.between(
                    fornecedor.getDataUltimaCompra(), LocalDate.now());
                
                if (diasSemCompra > 90 && fornecedor.getStatus() == StatusFornecedor.ATIVO) {
                    atencao.add(fornecedor);
                }
            }
        }
        
        return atencao;
    }
    
    /**
     * Valida CNPJ (simplificado)
     */
    private boolean validarCNPJ(String cnpj) {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            return false;
        }
        
        cnpj = cnpj.replaceAll("[^0-9]", "");
        
        if (cnpj.length() != 14) {
            return false;
        }
        
        // Validação básica - em produção, implementar algoritmo completo
        return !cnpj.equals("00000000000000") && 
               !cnpj.equals("11111111111111") && 
               !cnpj.equals("22222222222222") && 
               !cnpj.equals("33333333333333") && 
               !cnpj.equals("44444444444444") && 
               !cnpj.equals("55555555555555") && 
               !cnpj.equals("66666666666666") && 
               !cnpj.equals("77777777777777") && 
               !cnpj.equals("88888888888888") && 
               !cnpj.equals("99999999999999");
    }
    
    /**
     * Inicializa dados mock para teste
     */
    private void inicializarDadosMock() {
        // Criar fornecedores de exemplo
        Fornecedor f1 = new Fornecedor();
        f1.setNome("Distribuidora ABC Ltda");
        f1.setCnpj("12345678901234");
        f1.setRazaoSocial("Distribuidora ABC Comercial Ltda");
        f1.setEmail("contato@abc.com.br");
        f1.setTelefone("(11) 3333-4444");
        f1.setEndereco("Rua das Mercadorias, 123");
        f1.setCidade("São Paulo");
        f1.setEstado("SP");
        f1.setStatus(StatusFornecedor.ATIVO);
        fornecedores.add(f1);
        
        Fornecedor f2 = new Fornecedor();
        f2.setNome("Fornecedor Premium SA");
        f2.setCnpj("56789012345678");
        f2.setRazaoSocial("Fornecedor Premium S.A.");
        f2.setEmail("vendas@premium.com");
        f2.setTelefone("(21) 5555-6666");
        f2.setEndereco("Av. Central, 456");
        f2.setCidade("Rio de Janeiro");
        f2.setEstado("RJ");
        f2.setStatus(StatusFornecedor.ATIVO);
        fornecedores.add(f2);
    }
}
