package com.br.hermescomercial.shared.model;


import java.time.LocalDateTime;

public class Cliente {
    
    private Long id;
    private String nome;
    private String cpfCnpj;
    private String tipoPessoa; // FISICA ou JURIDICA
    private String rg;
    private String dataNascimento;
    private String telefone;
    private String celular;
    private String email;
    private String cep;
    private String endereco;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataUltimaAtualizacao;
    private boolean ativo;
    private String observacao;
    private LocalDateTime dataAtualizacao;
    
    // Campos adicionais para compatibilidade
    private String cpf;
    private String cnpj;
    private String nomeFantasia;
    private String inscricaoEstadual;
    private String observacoes;

    public Cliente() {
        this.dataCadastro = LocalDateTime.now();
        this.dataUltimaAtualizacao = LocalDateTime.now();
        this.ativo = true;
    }

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

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(String tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LocalDateTime getDataUltimaAtualizacao() {
        return dataUltimaAtualizacao;
    }

    public void setDataUltimaAtualizacao(LocalDateTime dataUltimaAtualizacao) {
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getEnderecoCompleto() {
        StringBuilder enderecoCompleto = new StringBuilder();
        
        if (endereco != null && !endereco.trim().isEmpty()) {
            enderecoCompleto.append(endereco.trim());
        }
        
        if (numero != null && !numero.trim().isEmpty()) {
            enderecoCompleto.append(", ").append(numero.trim());
        }
        
        if (complemento != null && !complemento.trim().isEmpty()) {
            enderecoCompleto.append(" - ").append(complemento.trim());
        }
        
        if (bairro != null && !bairro.trim().isEmpty()) {
            enderecoCompleto.append(", ").append(bairro.trim());
        }
        
        if (cidade != null && !cidade.trim().isEmpty()) {
            enderecoCompleto.append(" - ").append(cidade.trim());
        }
        
        if (estado != null && !estado.trim().isEmpty()) {
            enderecoCompleto.append("/").append(estado.trim());
        }
        
        if (cep != null && !cep.trim().isEmpty()) {
            enderecoCompleto.append(" - CEP: ").append(cep.trim());
        }
        
        return enderecoCompleto.toString();
    }

    public boolean isPessoaFisica() {
        return "FISICA".equals(tipoPessoa);
    }

    public boolean isPessoaJuridica() {
        return "JURIDICA".equals(tipoPessoa);
    }

    // Métodos adicionais para compatibilidade
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    // Enum para TipoPessoa
    public enum TipoPessoa {
        FISICA, JURIDICA
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cpfCnpj='" + cpfCnpj + '\'' +
                ", tipoPessoa='" + tipoPessoa + '\'' +
                ", telefone='" + telefone + '\'' +
                ", email='" + email + '\'' +
                ", dataCadastro=" + dataCadastro +
                ", ativo=" + ativo +
                '}';
    }
}
