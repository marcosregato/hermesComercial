package com.br.hermescomercial.pdv.service;

import com.br.hermescomercial.pdv.model.VendaConsulta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.table.DefaultTableModel;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para VendaConsultaService
 */
public class VendaConsultaServiceTest {
    
    private VendaConsultaService vendaService;
    private DefaultTableModel modelTabela;
    
    @BeforeEach
    public void setUp() {
        vendaService = new VendaConsultaService();
        modelTabela = new DefaultTableModel(
            new Object[]{"Número", "Data", "Cliente", "CPF", "Vendedor", "Valor", "Status", "Forma Pagamento"},
            0
        );
    }
    
    @Test
    public void testAdicionarDadosExemplo() {
        List<VendaConsulta> vendas = vendaService.adicionarDadosExemplo(modelTabela);
        
        assertNotNull(vendas);
        assertEquals(5, vendas.size());
        assertEquals(5, modelTabela.getRowCount());
        
        VendaConsulta primeiraVenda = vendas.get(0);
        assertEquals("001", primeiraVenda.getNumero());
        assertEquals("João Silva", primeiraVenda.getCliente());
    }
    
    @Test
    public void testBuscarVendasRapidaComTermo() {
        List<VendaConsulta> vendas = vendaService.adicionarDadosExemplo(modelTabela);
        
        List<VendaConsulta> resultado = vendaService.buscarVendasRapida("João", vendas);
        
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals("João Silva", resultado.get(0).getCliente());
    }
    
    @Test
    public void testBuscarVendasRapidaComTermoVazio() {
        List<VendaConsulta> vendas = vendaService.adicionarDadosExemplo(modelTabela);
        
        List<VendaConsulta> resultado = vendaService.buscarVendasRapida("", vendas);
        
        assertNotNull(resultado);
        assertEquals(vendas.size(), resultado.size());
    }
    
    @Test
    public void testBuscarVendasRapidaComTermoInexistente() {
        List<VendaConsulta> vendas = vendaService.adicionarDadosExemplo(modelTabela);
        
        List<VendaConsulta> resultado = vendaService.buscarVendasRapida("Inexistente", vendas);
        
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}
