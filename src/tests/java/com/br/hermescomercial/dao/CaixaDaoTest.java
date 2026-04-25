package com.br.hermescomercial.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import com.br.hermescomercial.model.Caixa;

import java.util.List;

/**
 * Testes unitários para a classe CaixaDao
 * Testa as funcionalidades de gerenciamento de caixa do sistema
 */
class CaixaDaoTest {

    private CaixaDao caixaDao;
    private Caixa caixaTeste;

    @BeforeEach
    void setUp() {
        caixaDao = new CaixaDao();
        caixaTeste = new Caixa();
        caixaTeste.setTipo("Venda");
        caixaTeste.setValor(1000.0f);
        caixaTeste.setValorCaixa(1500.0f);
        caixaTeste.setImpostoEstadual(50.0f);
        caixaTeste.setImpostoMunicipal(25.0f);
        caixaTeste.setImpostoFederal(75.0f);
    }

    @Test
    @DisplayName("Deve inicializar CaixaDao sem lançar exceção")
    void testInitialize() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            CaixaDao dao = new CaixaDao();
            assertNotNull(dao, "DAO não deve ser nulo");
        });
    }

    @Test
    @DisplayName("Deve salvar caixa válido")
    void testSalvar() {
        // Arrange
        Caixa caixa = new Caixa();
        caixa.setTipo("Entrada");
        caixa.setValor(500.0f);
        caixa.setValorCaixa(750.0f);
        caixa.setImpostoEstadual(25.0f);
        caixa.setImpostoMunicipal(12.5f);
        caixa.setImpostoFederal(37.5f);

        // Act & Assert
        assertDoesNotThrow(() -> {
            caixaDao.salvar(caixa);
        });
        
        // Nota: Como depende de banco de dados, apenas verificamos que não lança exceção
    }

    @Test
    @DisplayName("Deve tentar salvar caixa nulo")
    void testSalvarCaixaNulo() {
        // Arrange
        Caixa caixaNulo = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            caixaDao.salvar(caixaNulo);
        });
    }

    @Test
    @DisplayName("Deve salvar caixa com valores zero")
    void testSalvarCaixaComValoresZero() {
        // Arrange
        Caixa caixaZero = new Caixa();
        caixaZero.setTipo("Zero");
        caixaZero.setValor(0.0f);
        caixaZero.setValorCaixa(0.0f);
        caixaZero.setImpostoEstadual(0.0f);
        caixaZero.setImpostoMunicipal(0.0f);
        caixaZero.setImpostoFederal(0.0f);

        // Act & Assert
        assertDoesNotThrow(() -> {
            caixaDao.salvar(caixaZero);
        });
    }

    @Test
    @DisplayName("Deve salvar caixa com valores negativos")
    void testSalvarCaixaComValoresNegativos() {
        // Arrange
        Caixa caixaNegativo = new Caixa();
        caixaNegativo.setTipo("Negativo");
        caixaNegativo.setValor(-100.0f);
        caixaNegativo.setValorCaixa(-200.0f);
        caixaNegativo.setImpostoEstadual(-10.0f);
        caixaNegativo.setImpostoMunicipal(-5.0f);
        caixaNegativo.setImpostoFederal(-15.0f);

        // Act & Assert
        assertDoesNotThrow(() -> {
            caixaDao.salvar(caixaNegativo);
        });
    }

    @Test
    @DisplayName("Deve salvar caixa com valores altos")
    void testSalvarCaixaComValoresAltos() {
        // Arrange
        Caixa caixaAlto = new Caixa();
        caixaAlto.setTipo("Alto");
        caixaAlto.setValor(99999.99f);
        caixaAlto.setValorCaixa(199999.99f);
        caixaAlto.setImpostoEstadual(5000.0f);
        caixaAlto.setImpostoMunicipal(2500.0f);
        caixaAlto.setImpostoFederal(7500.0f);

        // Act & Assert
        assertDoesNotThrow(() -> {
            caixaDao.salvar(caixaAlto);
        });
    }

    @Test
    @DisplayName("Deve deletar caixa por tipo")
    void testDelete() {
        // Arrange
        String tipoCaixa = "Venda";

        // Act & Assert
        assertDoesNotThrow(() -> {
            caixaDao.delete(tipoCaixa);
        });
        
        // Nota: Como depende de banco de dados, apenas verificamos que não lança exceção
    }

    @Test
    @DisplayName("Deve tentar deletar caixa com tipo nulo")
    void testDeleteComTipoNulo() {
        // Arrange
        String tipoNulo = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            caixaDao.delete(tipoNulo);
        });
    }

    @Test
    @DisplayName("Deve tentar deletar caixa com tipo vazio")
    void testDeleteComTipoVazio() {
        // Arrange
        String tipoVazio = "";

        // Act & Assert
        assertDoesNotThrow(() -> {
            caixaDao.delete(tipoVazio);
        });
    }

    @Test
    @DisplayName("Deve atualizar caixa válido")
    void testUpdate() {
        // Arrange
        Caixa caixaAtualizar = new Caixa();
        caixaAtualizar.setTipo("Atualizado");
        caixaAtualizar.setValor(2000.0f);
        caixaAtualizar.setValorCaixa(3000.0f);
        caixaAtualizar.setImpostoEstadual(100.0f);
        caixaAtualizar.setImpostoMunicipal(50.0f);
        caixaAtualizar.setImpostoFederal(150.0f);

        // Act & Assert
        assertDoesNotThrow(() -> {
            caixaDao.update(caixaAtualizar);
        });
        
        // Nota: Como depende de banco de dados, apenas verificamos que não lança exceção
    }

    @Test
    @DisplayName("Deve tentar atualizar caixa nulo")
    void testUpdateComCaixaNulo() {
        // Arrange
        Caixa caixaNulo = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            caixaDao.update(caixaNulo);
        });
    }

    @Test
    @DisplayName("Deve listar todos os caixas")
    void testListar() {
        // Act
        List<Caixa> resultado = caixaDao.listar();

        // Assert
        assertNotNull(resultado, "Resultado não deve ser nulo");
        // Nota: Como depende de banco de dados, a lista pode estar vazia
        
        // Verifica que é uma lista válida
        assertDoesNotThrow(() -> {
            resultado.size(); // Não deve lançar exceção
        });
    }

    @Test
    @DisplayName("Deve buscar caixa por tipo")
    void testBuscar() {
        // Arrange
        String tipoBusca = "Venda";

        // Act
        List<Caixa> resultado = caixaDao.buscar(tipoBusca);

        // Assert
        assertNotNull(resultado, "Resultado não deve ser nulo");
        // Nota: Como depende de banco de dados, a lista pode estar vazia
        
        // Verifica que é uma lista válida
        assertDoesNotThrow(() -> {
            resultado.size(); // Não deve lançar exceção
        });
    }

    @Test
    @DisplayName("Deve buscar caixa com tipo nulo")
    void testBuscarComTipoNulo() {
        // Arrange
        String tipoNulo = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            List<Caixa> resultado = caixaDao.buscar(tipoNulo);
            assertNotNull(resultado);
        });
    }

    @Test
    @DisplayName("Deve buscar caixa com tipo vazio")
    void testBuscarComTipoVazio() {
        // Arrange
        String tipoVazio = "";

        // Act & Assert
        assertDoesNotThrow(() -> {
            List<Caixa> resultado = caixaDao.buscar(tipoVazio);
            assertNotNull(resultado);
        });
    }

    @Test
    @DisplayName("Deve criar caixa com valores válidos")
    void testCriarCaixaValido() {
        // Arrange
        Caixa caixa = new Caixa();

        // Act
        caixa.setTipo("Despesa");
        caixa.setValor(750.0f);
        caixa.setValorCaixa(1250.0f);
        caixa.setImpostoEstadual(37.5f);
        caixa.setImpostoMunicipal(18.75f);
        caixa.setImpostoFederal(56.25f);

        // Assert
        assertEquals("Despesa", caixa.getTipo());
        assertEquals(750.0f, caixa.getValor());
        assertEquals(1250.0f, caixa.getValorCaixa());
        assertEquals(37.5f, caixa.getImpostoEstadual());
        assertEquals(18.75f, caixa.getImpostoMunicipal());
        assertEquals(56.25f, caixa.getImpostoFederal());
    }

    @Test
    @DisplayName("Deve validar valores padrão do caixa")
    void testValoresPadraoCaixa() {
        // Arrange
        Caixa caixa = new Caixa();

        // Act & Assert
        assertEquals(0.0f, caixa.getValor());
        assertEquals(0.0f, caixa.getValorCaixa());
        assertNull(caixa.getTipo());
        assertNull(caixa.getImpostoEstadual());
        assertNull(caixa.getImpostoMunicipal());
        assertNull(caixa.getImpostoFederal());
    }

    @Test
    @DisplayName("Deve atualizar tipo do caixa")
    void testAtualizarTipoCaixa() {
        // Arrange
        Caixa caixa = new Caixa();
        caixa.setTipo("Entrada");

        // Act
        caixa.setTipo("Saída");

        // Assert
        assertEquals("Saída", caixa.getTipo());
    }

    @Test
    @DisplayName("Deve atualizar valor do caixa")
    void testAtualizarValorCaixa() {
        // Arrange
        Caixa caixa = new Caixa();
        caixa.setValor(500.0f);

        // Act
        caixa.setValor(1500.0f);

        // Assert
        assertEquals(1500.0f, caixa.getValor());
    }

    @Test
    @DisplayName("Deve atualizar valor caixa")
    void testAtualizarValorCaixaTotal() {
        // Arrange
        Caixa caixa = new Caixa();
        caixa.setValorCaixa(1000.0f);

        // Act
        caixa.setValorCaixa(2000.0f);

        // Assert
        assertEquals(2000.0f, caixa.getValorCaixa());
    }

    @Test
    @DisplayName("Deve atualizar imposto estadual")
    void testAtualizarImpostoEstadual() {
        // Arrange
        Caixa caixa = new Caixa();
        caixa.setImpostoEstadual(25.0f);

        // Act
        caixa.setImpostoEstadual(75.0f);

        // Assert
        assertEquals(75.0f, caixa.getImpostoEstadual());
    }

    @Test
    @DisplayName("Deve atualizar imposto municipal")
    void testAtualizarImpostoMunicipal() {
        // Arrange
        Caixa caixa = new Caixa();
        caixa.setImpostoMunicipal(12.5f);

        // Act
        caixa.setImpostoMunicipal(37.5f);

        // Assert
        assertEquals(37.5f, caixa.getImpostoMunicipal());
    }

    @Test
    @DisplayName("Deve atualizar imposto federal")
    void testAtualizarImpostoFederal() {
        // Arrange
        Caixa caixa = new Caixa();
        caixa.setImpostoFederal(50.0f);

        // Act
        caixa.setImpostoFederal(150.0f);

        // Assert
        assertEquals(150.0f, caixa.getImpostoFederal());
    }

    @Test
    @DisplayName("Deve criar múltiplas instâncias do DAO")
    void testCriarMultiplasInstancias() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            CaixaDao dao1 = new CaixaDao();
            CaixaDao dao2 = new CaixaDao();
            CaixaDao dao3 = new CaixaDao();
            
            assertNotNull(dao1);
            assertNotNull(dao2);
            assertNotNull(dao3);
            
            // Verifica que são instâncias diferentes
            assertNotSame(dao1, dao2);
            assertNotSame(dao2, dao3);
        });
    }

    @Test
    @DisplayName("Deve testar combinação de operações")
    void testCombinacaoOperacoes() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            caixaDao.salvar(caixaTeste);
            List<Caixa> lista = caixaDao.listar();
            caixaDao.update(caixaTeste);
            List<Caixa> resultado = caixaDao.buscar("Teste");
            caixaDao.delete("Teste");
            
            assertNotNull(lista);
            assertNotNull(resultado);
        });
    }

    @Test
    @DisplayName("Deve testar diferentes tipos de caixa")
    void testDiferentesTiposCaixa() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Caixa caixa1 = new Caixa();
            caixa1.setTipo("Venda");
            assertEquals("Venda", caixa1.getTipo());

            Caixa caixa2 = new Caixa();
            caixa2.setTipo("Compra");
            assertEquals("Compra", caixa2.getTipo());

            Caixa caixa3 = new Caixa();
            caixa3.setTipo("Transferência");
            assertEquals("Transferência", caixa3.getTipo());

            Caixa caixa4 = new Caixa();
            caixa4.setTipo("Sangria");
            assertEquals("Sangria", caixa4.getTipo());
        });
    }

    @Test
    @DisplayName("Deve testar diferentes valores de caixa")
    void testDiferentesValoresCaixa() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Caixa caixa1 = new Caixa();
            caixa1.setValor(100.0f);
            assertEquals(100.0f, caixa1.getValor());

            Caixa caixa2 = new Caixa();
            caixa2.setValor(1000.0f);
            assertEquals(1000.0f, caixa2.getValor());

            Caixa caixa3 = new Caixa();
            caixa3.setValor(10000.0f);
            assertEquals(10000.0f, caixa3.getValor());
        });
    }

    @Test
    @DisplayName("Deve testar diferentes valores caixa total")
    void testDiferentesValoresCaixaTotal() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Caixa caixa1 = new Caixa();
            caixa1.setValorCaixa(500.0f);
            assertEquals(500.0f, caixa1.getValorCaixa());

            Caixa caixa2 = new Caixa();
            caixa2.setValorCaixa(5000.0f);
            assertEquals(5000.0f, caixa2.getValorCaixa());

            Caixa caixa3 = new Caixa();
            caixa3.setValorCaixa(50000.0f);
            assertEquals(50000.0f, caixa3.getValorCaixa());
        });
    }

    @Test
    @DisplayName("Deve testar caixa com valores decimais")
    void testCaixaComValoresDecimais() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Caixa caixa = new Caixa();
            caixa.setValor(123.456f);
            caixa.setValorCaixa(789.012f);
            caixa.setImpostoEstadual(12.345f);
            caixa.setImpostoMunicipal(6.789f);
            caixa.setImpostoFederal(18.963f);
            
            assertEquals(123.456f, caixa.getValor());
            assertEquals(789.012f, caixa.getValorCaixa());
            assertEquals(12.345f, caixa.getImpostoEstadual());
            assertEquals(6.789f, caixa.getImpostoMunicipal());
            assertEquals(18.963f, caixa.getImpostoFederal());
        });
    }

    @Test
    @DisplayName("Deve testar sequência de operações")
    void testSequenciaOperacoes() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 5; i++) {
                Caixa caixa = new Caixa();
                caixa.setTipo("Operação" + i);
                caixa.setValor(100.0f * (i + 1));
                caixa.setValorCaixa(200.0f * (i + 1));
                caixa.setImpostoEstadual(10.0f * (i + 1));
                caixa.setImpostoMunicipal(5.0f * (i + 1));
                caixa.setImpostoFederal(15.0f * (i + 1));
                
                caixaDao.salvar(caixa);
                
                List<Caixa> lista = caixaDao.listar();
                assertNotNull(lista);
            }
        });
    }

    @Test
    @DisplayName("Deve testar diferentes cenários de busca")
    void testDiferentesCenariosBusca() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            List<Caixa> resultado1 = caixaDao.buscar("Caixa Inexistente");
            List<Caixa> resultado2 = caixaDao.buscar("");
            List<Caixa> resultado3 = caixaDao.buscar(" ");
            List<Caixa> resultado4 = caixaDao.buscar("Venda");
            List<Caixa> resultado5 = caixaDao.buscar("Compra");
            
            assertNotNull(resultado1);
            assertNotNull(resultado2);
            assertNotNull(resultado3);
            assertNotNull(resultado4);
            assertNotNull(resultado5);
        });
    }

    @Test
    @DisplayName("Deve testar estado do DAO após múltiplas operações")
    void testEstadoDaoMultiplasOperacoes() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            // Operação 1
            caixaDao.salvar(caixaTeste);
            
            // Operação 2
            List<Caixa> lista1 = caixaDao.listar();
            assertNotNull(lista1);
            
            // Operação 3
            caixaDao.update(caixaTeste);
            
            // Operação 4
            List<Caixa> resultado = caixaDao.buscar("Teste");
            assertNotNull(resultado);
            
            // Operação 5
            caixaDao.delete("Teste");
            
            // Operação final
            List<Caixa> listaFinal = caixaDao.listar();
            assertNotNull(listaFinal);
        });
    }

    @Test
    @DisplayName("Deve testar caixa com valores extremos")
    void testCaixaComValoresExtremos() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Caixa caixaMinimo = new Caixa();
            caixaMinimo.setValor(Float.MIN_VALUE);
            caixaMinimo.setValorCaixa(Float.MIN_VALUE);
            caixaMinimo.setImpostoEstadual(Float.MIN_VALUE);
            caixaMinimo.setImpostoMunicipal(Float.MIN_VALUE);
            caixaMinimo.setImpostoFederal(Float.MIN_VALUE);
            assertEquals(Float.MIN_VALUE, caixaMinimo.getValor());

            Caixa caixaMaximo = new Caixa();
            caixaMaximo.setValor(Float.MAX_VALUE);
            caixaMaximo.setValorCaixa(Float.MAX_VALUE);
            caixaMaximo.setImpostoEstadual(Float.MAX_VALUE);
            caixaMaximo.setImpostoMunicipal(Float.MAX_VALUE);
            caixaMaximo.setImpostoFederal(Float.MAX_VALUE);
            assertEquals(Float.MAX_VALUE, caixaMaximo.getValor());
        });
    }

    @Test
    @DisplayName("Deve testar persistência do DAO")
    void testPersistenciaDao() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            // Operações iniciais
            caixaDao.salvar(caixaTeste);
            caixaDao.listar();
            
            // Verifica que DAO ainda está funcional
            caixaDao.buscar("Teste");
            caixaDao.update(caixaTeste);
            caixaDao.delete("Teste");
            
            // Mais operações
            caixaDao.salvar(caixaTeste);
            caixaDao.listar();
        });
    }

    @Test
    @DisplayName("Deve testar caixa com valores especiais")
    void testCaixaComValoresEspeciais() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Caixa caixa1 = new Caixa();
            caixa1.setValor(Float.NaN);
            caixa1.setValorCaixa(Float.POSITIVE_INFINITY);
            caixa1.setImpostoEstadual(Float.NEGATIVE_INFINITY);
            caixa1.setImpostoMunicipal(Float.NaN);
            caixa1.setImpostoFederal(Float.POSITIVE_INFINITY);
            
            assertTrue(Float.isNaN(caixa1.getValor()));
            assertTrue(Float.isInfinite(caixa1.getValorCaixa()));
            assertTrue(Float.isInfinite(caixa1.getImpostoEstadual()));
            assertTrue(Float.isNaN(caixa1.getImpostoMunicipal()));
            assertTrue(Float.isInfinite(caixa1.getImpostoFederal()));
        });
    }

    @Test
    @DisplayName("Deve testar diferentes combinações de valores")
    void testDiferentesCombinacoesValores() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Caixa caixa1 = new Caixa();
            caixa1.setValor(100.0f);
            caixa1.setValorCaixa(200.0f);
            caixa1.setImpostoEstadual(10.0f);
            caixa1.setImpostoMunicipal(5.0f);
            caixa1.setImpostoFederal(15.0f);
            assertEquals(100.0f, caixa1.getValor());

            Caixa caixa2 = new Caixa();
            caixa2.setValor(500.0f);
            caixa2.setValorCaixa(1000.0f);
            caixa2.setImpostoEstadual(50.0f);
            caixa2.setImpostoMunicipal(25.0f);
            caixa2.setImpostoFederal(75.0f);
            assertEquals(500.0f, caixa2.getValor());

            Caixa caixa3 = new Caixa();
            caixa3.setValor(1000.0f);
            caixa3.setValorCaixa(2000.0f);
            caixa3.setImpostoEstadual(100.0f);
            caixa3.setImpostoMunicipal(50.0f);
            caixa3.setImpostoFederal(150.0f);
            assertEquals(1000.0f, caixa3.getValor());
        });
    }

    @Test
    @DisplayName("Deve testar caixa com impostos nulos")
    void testCaixaComImpostosNulos() {
        // Arrange
        Caixa caixa = new Caixa();

        // Act
        caixa.setImpostoEstadual(null);
        caixa.setImpostoMunicipal(null);
        caixa.setImpostoFederal(null);

        // Assert
        assertNull(caixa.getImpostoEstadual());
        assertNull(caixa.getImpostoMunicipal());
        assertNull(caixa.getImpostoFederal());
    }

    @Test
    @DisplayName("Deve testar caixa com tipo especial")
    void testCaixaComTipoEspecial() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Caixa caixa1 = new Caixa();
            caixa1.setTipo("Venda@Online");
            assertEquals("Venda@Online", caixa1.getTipo());

            Caixa caixa2 = new Caixa();
            caixa2.setTipo("Compra-Loja");
            assertEquals("Compra-Loja", caixa2.getTipo());

            Caixa caixa3 = new Caixa();
            caixa3.setTipo("Transferência_Conta");
            assertEquals("Transferência_Conta", caixa3.getTipo());
        });
    }

    @Test
    @DisplayName("Deve testar caixa com tipo vazio")
    void testCaixaComTipoVazio() {
        // Arrange
        Caixa caixa = new Caixa();

        // Act
        caixa.setTipo("");

        // Assert
        assertEquals("", caixa.getTipo());
    }
}
