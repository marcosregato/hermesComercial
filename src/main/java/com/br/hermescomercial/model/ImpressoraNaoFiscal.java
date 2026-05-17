package com.br.hermescomercial.model;


import java.time.LocalDateTime;

public class ImpressoraNaoFiscal {
    
    private Long id;
    private String nome;
    private String modelo;
    private String fabricante;
    private String tipoConexao; // USB, SERIAL, REDE, BLUETOOTH
    private String porta; // COM1, USB001, IP:9100, etc.
    private String ip;
    private Integer portaRede;
    private Integer baudRate; // Para conexão serial
    private Integer dataBits;
    private Integer stopBits;
    private String paridade; // NONE, EVEN, ODD
    private Integer colunas; // 40, 48, 80 colunas
    private String codificacao; // UTF-8, ISO-8859-1, CP850
    private boolean impressoraCupom; // true se for impressora de cupom fiscal
    private boolean cortaPapel;
    private boolean gavetaDinheiro;
    private String displayCliente; // NONE, LCD_16x2, LCD_20x4
    private boolean statusOnline;
    private LocalDateTime ultimaVerificacao;
    private String mensagemStatus;
    private String driver; // ESCPOS, EPSON, CUSTOM
    private String configuracaoAvancada;
    private boolean ativo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    
    public ImpressoraNaoFiscal() {
        this.tipoConexao = "USB";
        this.porta = "USB001";
        this.colunas = 48;
        this.codificacao = "UTF-8";
        this.impressoraCupom = false;
        this.cortaPapel = true;
        this.gavetaDinheiro = false;
        this.displayCliente = "NONE";
        this.statusOnline = false;
        this.driver = "ESCPOS";
        this.ativo = true;
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
        this.baudRate = 9600;
        this.dataBits = 8;
        this.stopBits = 1;
        this.paridade = "NONE";
        this.portaRede = 9100;
    }
    
    // Métodos de negócio
    public boolean isConexaoUSB() {
        return "USB".equalsIgnoreCase(tipoConexao);
    }
    
    public boolean isConexaoSerial() {
        return "SERIAL".equalsIgnoreCase(tipoConexao);
    }
    
    public boolean isConexaoRede() {
        return "REDE".equalsIgnoreCase(tipoConexao);
    }
    
    public boolean isConexaoBluetooth() {
        return "BLUETOOTH".equalsIgnoreCase(tipoConexao);
    }
    
    public String getDescricaoCompleta() {
        return String.format("%s %s (%s %s)", 
            fabricante != null ? fabricante : "", 
            nome != null ? nome : "",
            tipoConexao != null ? tipoConexao : "",
            isConexaoRede() ? (ip + ":" + portaRede) : porta);
    }
    
    public void atualizarStatus(boolean online, String mensagem) {
        this.statusOnline = online;
        this.mensagemStatus = mensagem;
        this.ultimaVerificacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }
    
    public String getStatusDescricao() {
        if (statusOnline) {
            return "Online";
        } else {
            return "Offline";
        }
    }
    
    public boolean precisaConfiguracaoRede() {
        return isConexaoRede() && (ip == null || ip.trim().isEmpty());
    }
    
    public boolean precisaConfiguracaoSerial() {
        return isConexaoSerial() && (porta == null || porta.trim().isEmpty());
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getModelo() {
        return modelo;
    }
    
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
    
    public String getFabricante() {
        return fabricante;
    }
    
    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }
    
    public String getTipoConexao() {
        return tipoConexao;
    }
    
    public void setTipoConexao(String tipoConexao) {
        this.tipoConexao = tipoConexao;
    }
    
    public String getPorta() {
        return porta;
    }
    
    public void setPorta(String porta) {
        this.porta = porta;
    }
    
    public String getIp() {
        return ip;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public Integer getPortaRede() {
        return portaRede;
    }
    
    public void setPortaRede(Integer portaRede) {
        this.portaRede = portaRede;
    }
    
    public Integer getBaudRate() {
        return baudRate;
    }
    
    public void setBaudRate(Integer baudRate) {
        this.baudRate = baudRate;
    }
    
    public Integer getDataBits() {
        return dataBits;
    }
    
    public void setDataBits(Integer dataBits) {
        this.dataBits = dataBits;
    }
    
    public Integer getStopBits() {
        return stopBits;
    }
    
    public void setStopBits(Integer stopBits) {
        this.stopBits = stopBits;
    }
    
    public String getParidade() {
        return paridade;
    }
    
    public void setParidade(String paridade) {
        this.paridade = paridade;
    }
    
    public Integer getColunas() {
        return colunas;
    }
    
    public void setColunas(Integer colunas) {
        this.colunas = colunas;
    }
    
    public String getCodificacao() {
        return codificacao;
    }
    
    public void setCodificacao(String codificacao) {
        this.codificacao = codificacao;
    }
    
    public boolean isImpressoraCupom() {
        return impressoraCupom;
    }
    
    public void setImpressoraCupom(boolean impressoraCupom) {
        this.impressoraCupom = impressoraCupom;
    }
    
    public boolean isCortaPapel() {
        return cortaPapel;
    }
    
    public void setCortaPapel(boolean cortaPapel) {
        this.cortaPapel = cortaPapel;
    }
    
    public boolean isGavetaDinheiro() {
        return gavetaDinheiro;
    }
    
    public void setGavetaDinheiro(boolean gavetaDinheiro) {
        this.gavetaDinheiro = gavetaDinheiro;
    }
    
    public String getDisplayCliente() {
        return displayCliente;
    }
    
    public void setDisplayCliente(String displayCliente) {
        this.displayCliente = displayCliente;
    }
    
    public boolean isStatusOnline() {
        return statusOnline;
    }
    
    public void setStatusOnline(boolean statusOnline) {
        this.statusOnline = statusOnline;
    }
    
    public LocalDateTime getUltimaVerificacao() {
        return ultimaVerificacao;
    }
    
    public void setUltimaVerificacao(LocalDateTime ultimaVerificacao) {
        this.ultimaVerificacao = ultimaVerificacao;
    }
    
    public String getMensagemStatus() {
        return mensagemStatus;
    }
    
    public void setMensagemStatus(String mensagemStatus) {
        this.mensagemStatus = mensagemStatus;
    }
    
    public String getDriver() {
        return driver;
    }
    
    public void setDriver(String driver) {
        this.driver = driver;
    }
    
    public String getConfiguracaoAvancada() {
        return configuracaoAvancada;
    }
    
    public void setConfiguracaoAvancada(String configuracaoAvancada) {
        this.configuracaoAvancada = configuracaoAvancada;
    }
    
    public boolean isAtivo() {
        return ativo;
    }
    
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
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
    
    @Override
    public String toString() {
        return "ImpressoraNaoFiscal{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", modelo='" + modelo + '\'' +
                ", tipoConexao='" + tipoConexao + '\'' +
                ", porta='" + porta + '\'' +
                ", statusOnline=" + statusOnline +
                '}';
    }
}
