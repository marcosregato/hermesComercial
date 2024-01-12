/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author marcos
 */
public class Custo {
    
    private Long id;
    private Long fk_fornecedor;
    private float custoUnitario;
    private float custoTotal;

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
     * @return the fk_fornecedor
     */
    public Long getFk_fornecedor() {
        return fk_fornecedor;
    }

    /**
     * @param fk_fornecedor the fk_fornecedor to set
     */
    public void setFk_fornecedor(Long fk_fornecedor) {
        this.fk_fornecedor = fk_fornecedor;
    }

    /**
     * @return the custoUnitario
     */
    public float getCustoUnitario() {
        return custoUnitario;
    }

    /**
     * @param custoUnitario the custoUnitario to set
     */
    public void setCustoUnitario(float custoUnitario) {
        this.custoUnitario = custoUnitario;
    }

    /**
     * @return the custoTotal
     */
    public float getCustoTotal() {
        return custoTotal;
    }

    /**
     * @param custoTotal the custoTotal to set
     */
    public void setCustoTotal(float custoTotal) {
        this.custoTotal = custoTotal;
    }
    
    
    
}
