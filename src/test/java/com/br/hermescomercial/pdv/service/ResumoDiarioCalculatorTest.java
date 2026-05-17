package com.br.hermescomercial.pdv.service;

import com.br.hermescomercial.pdv.model.TransacaoDiaria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para ResumoDiarioCalculator
 */
public class ResumoDiarioCalculatorTest {
    
    private ResumoDiarioCalculator calculator;
    
    @BeforeEach
    public void setUp() {
        calculator = new ResumoDiarioCalculator();
    }
    
    @Test
    public void testCalcularTotaisComTransacoes() {
        List<TransacaoDiaria> transacoes = new ArrayList<>();
        
        TransacaoDiaria venda1 = new TransacaoDiaria();
        venda1.setTipo("Venda");
        venda1.setValor("R$ 150,00");
        transacoes.add(venda1);
        
        TransacaoDiaria venda2 = new TransacaoDiaria();
        venda2.setTipo("Venda");
        venda2.setValor("R$ 89,50");
        transacoes.add(venda2);
        
        TransacaoDiaria devolucao = new TransacaoDiaria();
        devolucao.setTipo("Devolução");
        devolucao.setValor("R$ 50,00");
        transacoes.add(devolucao);
        
        ResumoDiarioCalculator.ResumoDiarioTotais totais = calculator.calcularTotais(transacoes);
        
        assertNotNull(totais);
        assertEquals(239.50, totais.getTotalVendas(), 0.01);
        assertEquals(50.00, totais.getTotalDevolucoes(), 0.01);
        assertEquals(3, totais.getTotalTransacoes());
    }
    
    @Test
    public void testCalcularTotaisComListaVazia() {
        List<TransacaoDiaria> transacoes = new ArrayList<>();
        
        ResumoDiarioCalculator.ResumoDiarioTotais totais = calculator.calcularTotais(transacoes);
        
        assertNotNull(totais);
        assertEquals(0.0, totais.getTotalVendas(), 0.01);
        assertEquals(0.0, totais.getTotalDevolucoes(), 0.01);
        assertEquals(0, totais.getTotalTransacoes());
    }
    
    @Test
    public void testCalcularTotaisSaldoLiquido() {
        List<TransacaoDiaria> transacoes = new ArrayList<>();
        
        TransacaoDiaria venda = new TransacaoDiaria();
        venda.setTipo("Venda");
        venda.setValor("R$ 100,00");
        transacoes.add(venda);
        
        TransacaoDiaria despesa = new TransacaoDiaria();
        despesa.setTipo("Despesa");
        despesa.setValor("R$ 30,00");
        transacoes.add(despesa);
        
        ResumoDiarioCalculator.ResumoDiarioTotais totais = calculator.calcularTotais(transacoes);
        
        assertEquals(70.00, totais.getSaldoLiquido(), 0.01);
    }
    
    @Test
    public void testCalcularTotaisTicketMedio() {
        List<TransacaoDiaria> transacoes = new ArrayList<>();
        
        TransacaoDiaria venda1 = new TransacaoDiaria();
        venda1.setTipo("Venda");
        venda1.setValor("R$ 100,00");
        transacoes.add(venda1);
        
        TransacaoDiaria venda2 = new TransacaoDiaria();
        venda2.setTipo("Venda");
        venda2.setValor("R$ 200,00");
        transacoes.add(venda2);
        
        ResumoDiarioCalculator.ResumoDiarioTotais totais = calculator.calcularTotais(transacoes);
        
        assertEquals(150.00, totais.getTicketMedio(), 0.01);
    }
}
