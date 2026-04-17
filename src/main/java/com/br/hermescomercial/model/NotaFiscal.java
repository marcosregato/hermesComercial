package com.br.hermescomercial.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class NotaFiscal {
    
    private Long id;
    private String numero;
    private String serie;
    private Integer modelo; // 55 = NF-e, 65 = NFC-e
    private LocalDateTime dataEmissao;
    private LocalDateTime dataHoraAutorizacao;
    private String chaveAcesso;
    private String protocoloAutorizacao;
    private String status; // EM_ELABORACAO, AUTORIZADA, CANCELADA, REJEITADA
    private String naturezaOperacao; // VENDA, DEVOLUCAO, etc.
    
    // Dados do Emitente
    private String emitenteCnpj;
    private String emitenteNome;
    private String emitenteNomeFantasia;
    private String emitenteEndereco;
    private String emitenteMunicipio;
    private String emitenteUf;
    private String emitenteCep;
    private String emitenteTelefone;
    private String emitenteInscricaoEstadual;
    
    // Dados do Destinatário
    private String destinatarioCnpjCpf;
    private String destinatarioNome;
    private String destinatarioEndereco;
    private String destinatarioMunicipio;
    private String destinatarioUf;
    private String destinatarioCep;
    private String destinatarioTelefone;
    private String destinatarioInscricaoEstadual;
    private String destinatarioEmail;
    
    // Valores
    private BigDecimal baseCalculoIcms;
    private BigDecimal valorIcms;
    private BigDecimal baseCalculoIcmsSt;
    private BigDecimal valorIcmsSt;
    private BigDecimal valorTotalProdutos;
    private BigDecimal valorFrete;
    private BigDecimal valorSeguro;
    private BigDecimal valorDesconto;
    private BigDecimal valorImpostoImportacao;
    private BigDecimal valorIpi;
    private BigDecimal valorPis;
    private BigDecimal valorCofins;
    private BigDecimal valorOutrasDespesas;
    private BigDecimal valorTotalNota;
    
    // Impostos
    private String aliquotaIcms;
    private String aliquotaIpi;
    private String aliquotaPis;
    private String aliquotaCofins;
    
    // Relacionamentos
    private VendaPDV venda;
    private List<NotaFiscalItem> itens;
    private NotaFiscalTransporte transporte;
    private NotaFiscalPagamento pagamentos;
    
    // Controle
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private Usuario criadoPor;
    private Usuario atualizadoPor;
    private String observacao;
    private String informacoesAdicionais;
    
    public NotaFiscal() {
        this.dataEmissao = LocalDateTime.now();
        this.dataCriacao = LocalDateTime.now();
        this.status = "EM_ELABORACAO";
        this.modelo = 65; // NFC-e por padrão para PDV
        this.naturezaOperacao = "VENDA";
        this.valorTotalNota = BigDecimal.ZERO;
        this.valorTotalProdutos = BigDecimal.ZERO;
        this.valorDesconto = BigDecimal.ZERO;
        this.baseCalculoIcms = BigDecimal.ZERO;
        this.valorIcms = BigDecimal.ZERO;
    }
    
    // Métodos de negócio
    public void calcularTotais() {
        if (itens != null && !itens.isEmpty()) {
            valorTotalProdutos = itens.stream()
                .map(NotaFiscalItem::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            valorTotalNota = valorTotalProdutos
                .add(valorFrete != null ? valorFrete : BigDecimal.ZERO)
                .add(valorSeguro != null ? valorSeguro : BigDecimal.ZERO)
                .add(valorOutrasDespesas != null ? valorOutrasDespesas : BigDecimal.ZERO)
                .subtract(valorDesconto != null ? valorDesconto : BigDecimal.ZERO);
        }
    }
    
    public void gerarChaveAcesso() {
        // Simulação de geração de chave de acesso (44 dígitos)
        StringBuilder chave = new StringBuilder();
        chave.append("35"); // UF
        chave.append(String.format("%02d", LocalDateTime.now().getYear() % 100)); // Ano
        chave.append(String.format("%02d", LocalDateTime.now().getMonthValue())); // Mês
        chave.append("23"); // CNPJ emitente (simulado)
        chave.append("55"); // Modelo
        chave.append("001"); // Série
        chave.append(String.format("%09d", System.currentTimeMillis() % 1000000000)); // Número
        chave.append("1"); // Forma de emissão
        chave.append(String.format("%08d", System.currentTimeMillis() % 100000000)); // Código numérico
        chave.append("4"); // DV (simulado)
        
        this.chaveAcesso = chave.toString();
    }
    
    public boolean isAutorizada() {
        return "AUTORIZADA".equals(status);
    }
    
    public boolean isCancelada() {
        return "CANCELADA".equals(status);
    }
    
    public boolean podeCancelar() {
        return isAutorizada() && dataEmissao.plusHours(24).isAfter(LocalDateTime.now());
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNumero() {
        return numero;
    }
    
    public void setNumero(String numero) {
        this.numero = numero;
    }
    
    public String getSerie() {
        return serie;
    }
    
    public void setSerie(String serie) {
        this.serie = serie;
    }
    
    public Integer getModelo() {
        return modelo;
    }
    
    public void setModelo(Integer modelo) {
        this.modelo = modelo;
    }
    
    public LocalDateTime getDataEmissao() {
        return dataEmissao;
    }
    
    public void setDataEmissao(LocalDateTime dataEmissao) {
        this.dataEmissao = dataEmissao;
    }
    
    public LocalDateTime getDataHoraAutorizacao() {
        return dataHoraAutorizacao;
    }
    
    public void setDataHoraAutorizacao(LocalDateTime dataHoraAutorizacao) {
        this.dataHoraAutorizacao = dataHoraAutorizacao;
    }
    
    public String getChaveAcesso() {
        return chaveAcesso;
    }
    
    public void setChaveAcesso(String chaveAcesso) {
        this.chaveAcesso = chaveAcesso;
    }
    
    public String getProtocoloAutorizacao() {
        return protocoloAutorizacao;
    }
    
    public void setProtocoloAutorizacao(String protocoloAutorizacao) {
        this.protocoloAutorizacao = protocoloAutorizacao;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getNaturezaOperacao() {
        return naturezaOperacao;
    }
    
    public void setNaturezaOperacao(String naturezaOperacao) {
        this.naturezaOperacao = naturezaOperacao;
    }
    
    public String getEmitenteCnpj() {
        return emitenteCnpj;
    }
    
    public void setEmitenteCnpj(String emitenteCnpj) {
        this.emitenteCnpj = emitenteCnpj;
    }
    
    public String getEmitenteNome() {
        return emitenteNome;
    }
    
    public void setEmitenteNome(String emitenteNome) {
        this.emitenteNome = emitenteNome;
    }
    
    public String getEmitenteNomeFantasia() {
        return emitenteNomeFantasia;
    }
    
    public void setEmitenteNomeFantasia(String emitenteNomeFantasia) {
        this.emitenteNomeFantasia = emitenteNomeFantasia;
    }
    
    public String getEmitenteEndereco() {
        return emitenteEndereco;
    }
    
    public void setEmitenteEndereco(String emitenteEndereco) {
        this.emitenteEndereco = emitenteEndereco;
    }
    
    public String getEmitenteMunicipio() {
        return emitenteMunicipio;
    }
    
    public void setEmitenteMunicipio(String emitenteMunicipio) {
        this.emitenteMunicipio = emitenteMunicipio;
    }
    
    public String getEmitenteUf() {
        return emitenteUf;
    }
    
    public void setEmitenteUf(String emitenteUf) {
        this.emitenteUf = emitenteUf;
    }
    
    public String getEmitenteCep() {
        return emitenteCep;
    }
    
    public void setEmitenteCep(String emitenteCep) {
        this.emitenteCep = emitenteCep;
    }
    
    public String getEmitenteTelefone() {
        return emitenteTelefone;
    }
    
    public void setEmitenteTelefone(String emitenteTelefone) {
        this.emitenteTelefone = emitenteTelefone;
    }
    
    public String getEmitenteInscricaoEstadual() {
        return emitenteInscricaoEstadual;
    }
    
    public void setEmitenteInscricaoEstadual(String emitenteInscricaoEstadual) {
        this.emitenteInscricaoEstadual = emitenteInscricaoEstadual;
    }
    
    public String getDestinatarioCnpjCpf() {
        return destinatarioCnpjCpf;
    }
    
    public void setDestinatarioCnpjCpf(String destinatarioCnpjCpf) {
        this.destinatarioCnpjCpf = destinatarioCnpjCpf;
    }
    
    public String getDestinatarioNome() {
        return destinatarioNome;
    }
    
    public void setDestinatarioNome(String destinatarioNome) {
        this.destinatarioNome = destinatarioNome;
    }
    
    public String getDestinatarioEndereco() {
        return destinatarioEndereco;
    }
    
    public void setDestinatarioEndereco(String destinatarioEndereco) {
        this.destinatarioEndereco = destinatarioEndereco;
    }
    
    public String getDestinatarioMunicipio() {
        return destinatarioMunicipio;
    }
    
    public void setDestinatarioMunicipio(String destinatarioMunicipio) {
        this.destinatarioMunicipio = destinatarioMunicipio;
    }
    
    public String getDestinatarioUf() {
        return destinatarioUf;
    }
    
    public void setDestinatarioUf(String destinatarioUf) {
        this.destinatarioUf = destinatarioUf;
    }
    
    public String getDestinatarioCep() {
        return destinatarioCep;
    }
    
    public void setDestinatarioCep(String destinatarioCep) {
        this.destinatarioCep = destinatarioCep;
    }
    
    public String getDestinatarioTelefone() {
        return destinatarioTelefone;
    }
    
    public void setDestinatarioTelefone(String destinatarioTelefone) {
        this.destinatarioTelefone = destinatarioTelefone;
    }
    
    public String getDestinatarioInscricaoEstadual() {
        return destinatarioInscricaoEstadual;
    }
    
    public void setDestinatarioInscricaoEstadual(String destinatarioInscricaoEstadual) {
        this.destinatarioInscricaoEstadual = destinatarioInscricaoEstadual;
    }
    
    public String getDestinatarioEmail() {
        return destinatarioEmail;
    }
    
    public void setDestinatarioEmail(String destinatarioEmail) {
        this.destinatarioEmail = destinatarioEmail;
    }
    
    public BigDecimal getBaseCalculoIcms() {
        return baseCalculoIcms;
    }
    
    public void setBaseCalculoIcms(BigDecimal baseCalculoIcms) {
        this.baseCalculoIcms = baseCalculoIcms;
    }
    
    public BigDecimal getValorIcms() {
        return valorIcms;
    }
    
    public void setValorIcms(BigDecimal valorIcms) {
        this.valorIcms = valorIcms;
    }
    
    public BigDecimal getBaseCalculoIcmsSt() {
        return baseCalculoIcmsSt;
    }
    
    public void setBaseCalculoIcmsSt(BigDecimal baseCalculoIcmsSt) {
        this.baseCalculoIcmsSt = baseCalculoIcmsSt;
    }
    
    public BigDecimal getValorIcmsSt() {
        return valorIcmsSt;
    }
    
    public void setValorIcmsSt(BigDecimal valorIcmsSt) {
        this.valorIcmsSt = valorIcmsSt;
    }
    
    public BigDecimal getValorTotalProdutos() {
        return valorTotalProdutos;
    }
    
    public void setValorTotalProdutos(BigDecimal valorTotalProdutos) {
        this.valorTotalProdutos = valorTotalProdutos;
    }
    
    public BigDecimal getValorFrete() {
        return valorFrete;
    }
    
    public void setValorFrete(BigDecimal valorFrete) {
        this.valorFrete = valorFrete;
    }
    
    public BigDecimal getValorSeguro() {
        return valorSeguro;
    }
    
    public void setValorSeguro(BigDecimal valorSeguro) {
        this.valorSeguro = valorSeguro;
    }
    
    public BigDecimal getValorDesconto() {
        return valorDesconto;
    }
    
    public void setValorDesconto(BigDecimal valorDesconto) {
        this.valorDesconto = valorDesconto;
    }
    
    public BigDecimal getValorImpostoImportacao() {
        return valorImpostoImportacao;
    }
    
    public void setValorImpostoImportacao(BigDecimal valorImpostoImportacao) {
        this.valorImpostoImportacao = valorImpostoImportacao;
    }
    
    public BigDecimal getValorIpi() {
        return valorIpi;
    }
    
    public void setValorIpi(BigDecimal valorIpi) {
        this.valorIpi = valorIpi;
    }
    
    public BigDecimal getValorPis() {
        return valorPis;
    }
    
    public void setValorPis(BigDecimal valorPis) {
        this.valorPis = valorPis;
    }
    
    public BigDecimal getValorCofins() {
        return valorCofins;
    }
    
    public void setValorCofins(BigDecimal valorCofins) {
        this.valorCofins = valorCofins;
    }
    
    public BigDecimal getValorOutrasDespesas() {
        return valorOutrasDespesas;
    }
    
    public void setValorOutrasDespesas(BigDecimal valorOutrasDespesas) {
        this.valorOutrasDespesas = valorOutrasDespesas;
    }
    
    public BigDecimal getValorTotalNota() {
        return valorTotalNota;
    }
    
    public void setValorTotalNota(BigDecimal valorTotalNota) {
        this.valorTotalNota = valorTotalNota;
    }
    
    public String getAliquotaIcms() {
        return aliquotaIcms;
    }
    
    public void setAliquotaIcms(String aliquotaIcms) {
        this.aliquotaIcms = aliquotaIcms;
    }
    
    public String getAliquotaIpi() {
        return aliquotaIpi;
    }
    
    public void setAliquotaIpi(String aliquotaIpi) {
        this.aliquotaIpi = aliquotaIpi;
    }
    
    public String getAliquotaPis() {
        return aliquotaPis;
    }
    
    public void setAliquotaPis(String aliquotaPis) {
        this.aliquotaPis = aliquotaPis;
    }
    
    public String getAliquotaCofins() {
        return aliquotaCofins;
    }
    
    public void setAliquotaCofins(String aliquotaCofins) {
        this.aliquotaCofins = aliquotaCofins;
    }
    
    public VendaPDV getVenda() {
        return venda;
    }
    
    public void setVenda(VendaPDV venda) {
        this.venda = venda;
    }
    
    public List<NotaFiscalItem> getItens() {
        return itens;
    }
    
    public void setItens(List<NotaFiscalItem> itens) {
        this.itens = itens;
    }
    
    public NotaFiscalTransporte getTransporte() {
        return transporte;
    }
    
    public void setTransporte(NotaFiscalTransporte transporte) {
        this.transporte = transporte;
    }
    
    public NotaFiscalPagamento getPagamentos() {
        return pagamentos;
    }
    
    public void setPagamentos(NotaFiscalPagamento pagamentos) {
        this.pagamentos = pagamentos;
    }
    
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    
    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }
    
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
    
    public Usuario getCriadoPor() {
        return criadoPor;
    }
    
    public void setCriadoPor(Usuario criadoPor) {
        this.criadoPor = criadoPor;
    }
    
    public Usuario getAtualizadoPor() {
        return atualizadoPor;
    }
    
    public void setAtualizadoPor(Usuario atualizadoPor) {
        this.atualizadoPor = atualizadoPor;
    }
    
    public String getObservacao() {
        return observacao;
    }
    
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
    
    public String getInformacoesAdicionais() {
        return informacoesAdicionais;
    }
    
    public void setInformacoesAdicionais(String informacoesAdicionais) {
        this.informacoesAdicionais = informacoesAdicionais;
    }
    
    @Override
    public String toString() {
        return "NotaFiscal{" +
                "id=" + id +
                ", numero='" + numero + '\'' +
                ", serie='" + serie + '\'' +
                ", chaveAcesso='" + chaveAcesso + '\'' +
                ", status='" + status + '\'' +
                ", valorTotalNota=" + valorTotalNota +
                '}';
    }
}
