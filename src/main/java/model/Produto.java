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
    private String tipo;
    private String subTipo;
    private String codigo;
    private String dataCompra;
    private String codigoNcm;

    private float valorUnitario;

    private float valorTotal;

    private float valorVenda;

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
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the produto to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the subProduto
     */
    public String getSubTipo() {
        return subTipo;
    }

    /**
     * @param subProduto the subProduto to set
     */
    public void setSubTipo(String subProduto) {
        this.subTipo = subTipo;
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


    public float getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(float valorUnitario) {
        this.valorUnitario = valorUnitario;
    }
}
