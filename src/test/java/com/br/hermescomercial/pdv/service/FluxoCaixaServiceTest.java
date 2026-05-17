package com.br.hermescomercial.pdv.service;

import com.br.hermescomercial.pdv.model.FluxoCaixa;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.table.DefaultTableModel;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para FluxoCaixaService
 */
public class FluxoCaixaServiceTest {
    
    private FluxoCaixaService fluxoService;
    private DefaultTableModel modelTabela;
    
    @BeforeEach
    public void setUp() {
        fluxoService = new FluxoCaixaService();
        modelTabela = new DefaultTableModel(
            new Object[]{"Data", "Descrição", "Categoria", "Tipo", "Valor", "Forma Pagamento", "Status"},
            0
        );
    }
    
    @Test
    public void testAdicionarDadosExemplo() {
        List<FluxoCaixa> fluxos = fluxoService.adicionarDadosExemplo(modelTabela);
        
        assertNotNull(fluxos);
        assertEquals(7, fluxos.size());
        assertEquals(7, modelTabela.getRowCount());
        
        FluxoCaixa primeiroFluxo = fluxos.get(0);
        assertEquals("09/05/2026", primeiroFluxo.getData());
        assertEquals("Venda #001", primeiroFluxo.getDescricao());
        assertEquals("Entrada", primeiroFluxo.getTipo());
    }
    
    @Test
    public void testCalcularTotais() {
        List<FluxoCaixa> fluxos = fluxoService.adicionarDadosExemplo(modelTabela);
        
        FluxoCaixaService.FluxoCaixaTotais totais = fluxoService.calcularTotais(fluxos);
        
        assertNotNull(totais);
        assertTrue(totais.getTotalEntradas() > 0);
        assertTrue(totais.getTotalSaidas() > 0);
    }
    
    @Test
    public void testCalcularTotaisSaldoFinal() {
        List<FluxoCaixa> fluxos = fluxoService.adicionarDadosExemplo(modelTabela);
        
        FluxoCaixaService.FluxoCaixaTotais totais = fluxoService.calcularTotais(fluxos);
        
        double saldoEsperado = totais.getTotalEntradas() - totais.getTotalSaidas();
        assertEquals(saldoEsperado, totais.getSaldoFinal(), 0.01);
    }
}
