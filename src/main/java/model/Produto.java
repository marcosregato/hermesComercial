/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import javax.persistence.Entity;
import javax.persistence.Table;
/**
 *
 * @author marcos
 */
@Entity
@Table(name = "PRODUTO")
public class Produto {

    private Long id;
    private String nome;
    private String subProduto;
    private String codigo;
    private String dataCompra;
    private String codigoNcm;

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
     * @return the produto
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the produto to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the subProduto
     */
    public String getSubProduto() {
        return subProduto;
    }

    /**
     * @param subProduto the subProduto to set
     */
    public void setSubProduto(String subProduto) {
        this.subProduto = subProduto;
    }

    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the dataCompra
     */
    public String getDataCompra() {
        return dataCompra;
    }

    /**
     * @param dataCompra the dataCompra to set
     */
    public void setDataCompra(String dataCompra) {
        this.dataCompra = dataCompra;
    }

    /**
     * @return the codigoNcm
     */
    public String getCodigoNcm() {
        return codigoNcm;
    }

    /**
     * @param codigoNcm the codigoNcm to set
     */
    public void setCodigoNcm(String codigoNcm) {
        this.codigoNcm = codigoNcm;
    }
    
    
}
