/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author marcos
 */
public class Fornecedor {
    
    private Long id;
    private Long fk_produto;
    private String nome;
    private String tipoFornecedor;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the fk_produto
     */
    public Long getFk_produto() {
        return fk_produto;
    }

    /**
     * @param fk_produto the fk_produto to set
     */
    public void setFk_produto(Long fk_produto) {
        this.fk_produto = fk_produto;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the tipoFornecedor
     */
    public String getTipoFornecedor() {
        return tipoFornecedor;
    }

    /**
     * @param tipoFornecedor the tipoFornecedor to set
     */
    public void setTipoFornecedor(String tipoFornecedor) {
        this.tipoFornecedor = tipoFornecedor;
    }
    
    
    
    
}
