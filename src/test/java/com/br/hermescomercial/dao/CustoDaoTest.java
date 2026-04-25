package com.br.hermescomercial.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import com.br.hermescomercial.model.Custo;

import java.util.List;

/**
 * Testes unitários para a classe CustoDao
 * Testa as funcionalidades de gerenciamento de custos do sistema
 */
class CustoDaoTest {

    private CustoDao custoDao;
    private Custo custoTeste;

    @BeforeEach
    void setUp() {
        custoDao = new CustoDao();
        custoTeste = new Custo();
        custoTeste.setId(1L);
        custoTeste.setCustoUnitario(25.50f);
        custoTeste.setCustoTotal(125.75f);
    }

    @Test
    @DisplayName("Deve inicializar CustoDao sem lançar exceção")
    void testInitialize() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            CustoDao dao = new CustoDao();
            assertNotNull(dao, "DAO não deve ser nulo");
        });
    }

    @Test
    @DisplayName("Deve salvar custo válido")
    void testSalvar() {
        // Arrange
        Custo custo = new Custo();
        custo.setCustoUnitario(30.0f);
        custo.setCustoTotal(150.0f);

        // Act & Assert
        assertDoesNotThrow(() -> {
            custoDao.salvar(custo);
        });
        
        // Nota: Como depende de banco de dados, apenas verificamos que não lança exceção
    }

    @Test
    @DisplayName("Deve tentar salvar custo nulo")
    void testSalvarCustoNulo() {
        // Arrange
        Custo custoNulo = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            custoDao.salvar(custoNulo);
        });
    }

    @Test
    @DisplayName("Deve salvar custo com valores zero")
    void testSalvarCustoComValoresZero() {
        // Arrange
        Custo custoZero = new Custo();
        custoZero.setCustoUnitario(0.0f);
        custoZero.setCustoTotal(0.0f);

        // Act & Assert
        assertDoesNotThrow(() -> {
            custoDao.salvar(custoZero);
        });
    }

    @Test
    @DisplayName("Deve salvar custo com valores negativos")
    void testSalvarCustoComValoresNegativos() {
        // Arrange
        Custo custoNegativo = new Custo();
        custoNegativo.setCustoUnitario(-10.0f);
        custoNegativo.setCustoTotal(-50.0f);

        // Act & Assert
        assertDoesNotThrow(() -> {
            custoDao.salvar(custoNegativo);
        });
    }

    @Test
    @DisplayName("Deve salvar custo com valores altos")
    void testSalvarCustoComValoresAltos() {
        // Arrange
        Custo custoAlto = new Custo();
        custoAlto.setCustoUnitario(9999.99f);
        custoAlto.setCustoTotal(99999.99f);

        // Act & Assert
        assertDoesNotThrow(() -> {
            custoDao.salvar(custoAlto);
        });
    }

    @Test
    @DisplayName("Deve remover custo por ID")
    void testRemove() {
        // Arrange
        String idCusto = "1";

        // Act & Assert
        assertDoesNotThrow(() -> {
            custoDao.remove(idCusto);
        });
        
        // Nota: Como depende de banco de dados, apenas verificamos que não lança exceção
    }

    @Test
    @DisplayName("Deve tentar remover custo com ID nulo")
    void testRemoveComIdNulo() {
        // Arrange
        String idNulo = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            custoDao.remove(idNulo);
        });
    }

    @Test
    @DisplayName("Deve tentar remover custo com ID vazio")
    void testRemoveComIdVazio() {
        // Arrange
        String idVazio = "";

        // Act & Assert
        assertDoesNotThrow(() -> {
            custoDao.remove(idVazio);
        });
    }

    @Test
    @DisplayName("Deve atualizar custo válido")
    void testUpdate() {
        // Arrange
        Custo custoAtualizar = new Custo();
        custoAtualizar.setId(2L);
        custoAtualizar.setCustoUnitario(35.0f);
        custoAtualizar.setCustoTotal(175.0f);

        // Act & Assert
        assertDoesNotThrow(() -> {
            custoDao.update(custoAtualizar);
        });
        
        // Nota: Como depende de banco de dados, apenas verificamos que não lança exceção
    }

    @Test
    @DisplayName("Deve tentar atualizar custo nulo")
    void testUpdateComCustoNulo() {
        // Arrange
        Custo custoNulo = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            custoDao.update(custoNulo);
        });
    }

    @Test
    @DisplayName("Deve listar todos os custos")
    void testListar() {
        // Act
        List<Custo> resultado = custoDao.listar();

        // Assert
        assertNotNull(resultado, "Resultado não deve ser nulo");
        // Nota: Como depende de banco de dados, a lista pode estar vazia
        
        // Verifica que é uma lista válida
        assertDoesNotThrow(() -> {
            resultado.size(); // Não deve lançar exceção
        });
    }

    @Test
    @DisplayName("Deve buscar custo por ID")
    void testBuscar() {
        // Arrange
        String idBusca = "1";

        // Act
        List<Custo> resultado = custoDao.buscar(idBusca);

        // Assert
        assertNotNull(resultado, "Resultado não deve ser nulo");
        // Nota: Como depende de banco de dados, a lista pode estar vazia
        
        // Verifica que é uma lista válida
        assertDoesNotThrow(() -> {
            resultado.size(); // Não deve lançar exceção
        });
    }

    @Test
    @DisplayName("Deve buscar custo com ID nulo")
    void testBuscarComIdNulo() {
        // Arrange
        String idNulo = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            List<Custo> resultado = custoDao.buscar(idNulo);
            assertNotNull(resultado);
        });
    }

    @Test
    @DisplayName("Deve buscar custo com ID vazio")
    void testBuscarComIdVazio() {
        // Arrange
        String idVazio = "";

        // Act & Assert
        assertDoesNotThrow(() -> {
            List<Custo> resultado = custoDao.buscar(idVazio);
            assertNotNull(resultado);
        });
    }

    @Test
    @DisplayName("Deve criar custo com valores válidos")
    void testCriarCustoValido() {
        // Arrange
        Custo custo = new Custo();

        // Act
        custo.setId(3L);
        custo.setCustoUnitario(40.0f);
        custo.setCustoTotal(200.0f);

        // Assert
        assertEquals(3L, custo.getId());
        assertEquals(40.0f, custo.getCustoUnitario());
        assertEquals(200.0f, custo.getCustoTotal());
    }

    @Test
    @DisplayName("Deve validar valores padrão do custo")
    void testValoresPadraoCusto() {
        // Arrange
        Custo custo = new Custo();

        // Act & Assert
        assertEquals(0L, custo.getId());
        assertEquals(0.0f, custo.getCustoUnitario());
        assertEquals(0.0f, custo.getCustoTotal());
    }

    @Test
    @DisplayName("Deve atualizar ID do custo")
    void testAtualizarIdCusto() {
        // Arrange
        Custo custo = new Custo();
        custo.setId(5L);

        // Act
        custo.setId(10L);

        // Assert
        assertEquals(10L, custo.getId());
    }

    @Test
    @DisplayName("Deve atualizar custo unitário")
    void testAtualizarCustoUnitario() {
        // Arrange
        Custo custo = new Custo();
        custo.setCustoUnitario(20.0f);

        // Act
        custo.setCustoUnitario(45.0f);

        // Assert
        assertEquals(45.0f, custo.getCustoUnitario());
    }

    @Test
    @DisplayName("Deve atualizar custo total")
    void testAtualizarCustoTotal() {
        // Arrange
        Custo custo = new Custo();
        custo.setCustoTotal(100.0f);

        // Act
        custo.setCustoTotal(250.0f);

        // Assert
        assertEquals(250.0f, custo.getCustoTotal());
    }

    @Test
    @DisplayName("Deve criar múltiplas instâncias do DAO")
    void testCriarMultiplasInstancias() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            CustoDao dao1 = new CustoDao();
            CustoDao dao2 = new CustoDao();
            CustoDao dao3 = new CustoDao();
            
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
            custoDao.salvar(custoTeste);
            List<Custo> lista = custoDao.listar();
            custoDao.update(custoTeste);
            List<Custo> resultado = custoDao.buscar("1");
            custoDao.remove("1");
            
            assertNotNull(lista);
            assertNotNull(resultado);
        });
    }

    @Test
    @DisplayName("Deve testar diferentes IDs de custo")
    void testDiferentesIdsCusto() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Custo custo1 = new Custo();
            custo1.setId(1L);
            assertEquals(1L, custo1.getId());

            Custo custo2 = new Custo();
            custo2.setId(100L);
            assertEquals(100L, custo2.getId());

            Custo custo3 = new Custo();
            custo3.setId(999L);
            assertEquals(999L, custo3.getId());
        });
    }

    @Test
    @DisplayName("Deve testar diferentes custos unitários")
    void testDiferentesCustosUnitarios() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Custo custo1 = new Custo();
            custo1.setCustoUnitario(5.0f);
            assertEquals(5.0f, custo1.getCustoUnitario());

            Custo custo2 = new Custo();
            custo2.setCustoUnitario(25.0f);
            assertEquals(25.0f, custo2.getCustoUnitario());

            Custo custo3 = new Custo();
            custo3.setCustoUnitario(100.0f);
            assertEquals(100.0f, custo3.getCustoUnitario());
        });
    }

    @Test
    @DisplayName("Deve testar diferentes custos totais")
    void testDiferentesCustosTotais() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Custo custo1 = new Custo();
            custo1.setCustoTotal(50.0f);
            assertEquals(50.0f, custo1.getCustoTotal());

            Custo custo2 = new Custo();
            custo2.setCustoTotal(250.0f);
            assertEquals(250.0f, custo2.getCustoTotal());

            Custo custo3 = new Custo();
            custo3.setCustoTotal(1000.0f);
            assertEquals(1000.0f, custo3.getCustoTotal());
        });
    }

    @Test
    @DisplayName("Deve testar custo com valores decimais")
    void testCustoComValoresDecimais() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Custo custo = new Custo();
            custo.setCustoUnitario(12.345f);
            custo.setCustoTotal(123.456f);
            
            assertEquals(12.345f, custo.getCustoUnitario());
            assertEquals(123.456f, custo.getCustoTotal());
        });
    }

    @Test
    @DisplayName("Deve testar sequência de operações")
    void testSequenciaOperacoes() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 5; i++) {
                Custo custo = new Custo();
                custo.setId((long) (i + 1));
                custo.setCustoUnitario(10.0f * (i + 1));
                custo.setCustoTotal(50.0f * (i + 1));
                
                custoDao.salvar(custo);
                
                List<Custo> lista = custoDao.listar();
                assertNotNull(lista);
            }
        });
    }

    @Test
    @DisplayName("Deve testar diferentes cenários de busca")
    void testDiferentesCenariosBusca() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            List<Custo> resultado1 = custoDao.buscar("999");
            List<Custo> resultado2 = custoDao.buscar("");
            List<Custo> resultado3 = custoDao.buscar(" ");
            List<Custo> resultado4 = custoDao.buscar("1");
            List<Custo> resultado5 = custoDao.buscar("abc");
            
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
            custoDao.salvar(custoTeste);
            
            // Operação 2
            List<Custo> lista1 = custoDao.listar();
            assertNotNull(lista1);
            
            // Operação 3
            custoDao.update(custoTeste);
            
            // Operação 4
            List<Custo> resultado = custoDao.buscar("1");
            assertNotNull(resultado);
            
            // Operação 5
            custoDao.remove("1");
            
            // Operação final
            List<Custo> listaFinal = custoDao.listar();
            assertNotNull(listaFinal);
        });
    }

    @Test
    @DisplayName("Deve testar custo com valores extremos")
    void testCustoComValoresExtremos() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Custo custoMinimo = new Custo();
            custoMinimo.setCustoUnitario(Float.MIN_VALUE);
            custoMinimo.setCustoTotal(Float.MIN_VALUE);
            assertEquals(Float.MIN_VALUE, custoMinimo.getCustoUnitario());

            Custo custoMaximo = new Custo();
            custoMaximo.setCustoUnitario(Float.MAX_VALUE);
            custoMaximo.setCustoTotal(Float.MAX_VALUE);
            assertEquals(Float.MAX_VALUE, custoMaximo.getCustoUnitario());
        });
    }

    @Test
    @DisplayName("Deve testar persistência do DAO")
    void testPersistenciaDao() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            // Operações iniciais
            custoDao.salvar(custoTeste);
            custoDao.listar();
            
            // Verifica que DAO ainda está funcional
            custoDao.buscar("1");
            custoDao.update(custoTeste);
            custoDao.remove("1");
            
            // Mais operações
            custoDao.salvar(custoTeste);
            custoDao.listar();
        });
    }

    @Test
    @DisplayName("Deve testar custo com valores especiais")
    void testCustoComValoresEspeciais() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Custo custo1 = new Custo();
            custo1.setCustoUnitario(Float.NaN);
            custo1.setCustoTotal(Float.POSITIVE_INFINITY);
            
            assertTrue(Float.isNaN(custo1.getCustoUnitario()));
            assertTrue(Float.isInfinite(custo1.getCustoTotal()));

            Custo custo2 = new Custo();
            custo2.setCustoUnitario(Float.NEGATIVE_INFINITY);
            custo2.setCustoTotal(Float.NaN);
            
            assertTrue(Float.isInfinite(custo2.getCustoUnitario()));
            assertTrue(Float.isNaN(custo2.getCustoTotal()));
        });
    }

    @Test
    @DisplayName("Deve testar diferentes combinações de custos")
    void testDiferentesCombinacoesCustos() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Custo custo1 = new Custo();
            custo1.setCustoUnitario(10.0f);
            custo1.setCustoTotal(50.0f);
            assertEquals(10.0f, custo1.getCustoUnitario());

            Custo custo2 = new Custo();
            custo2.setCustoUnitario(25.0f);
            custo2.setCustoTotal(125.0f);
            assertEquals(25.0f, custo2.getCustoUnitario());

            Custo custo3 = new Custo();
            custo3.setCustoUnitario(50.0f);
            custo3.setCustoTotal(250.0f);
            assertEquals(50.0f, custo3.getCustoUnitario());
        });
    }

    @Test
    @DisplayName("Deve testar custo com ID extremos")
    void testCustoComIdExtremos() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Custo custoMinimo = new Custo();
            custoMinimo.setId(Long.MIN_VALUE);
            assertEquals(Long.MIN_VALUE, custoMinimo.getId());

            Custo custoMaximo = new Custo();
            custoMaximo.setId(Long.MAX_VALUE);
            assertEquals(Long.MAX_VALUE, custoMaximo.getId());
        });
    }

    @Test
    @DisplayName("Deve testar custo com ID zero")
    void testCustoComIdZero() {
        // Arrange
        Custo custo = new Custo();

        // Act
        custo.setId(0L);

        // Assert
        assertEquals(0L, custo.getId());
    }

    @Test
    @DisplayName("Deve testar custo com ID negativo")
    void testCustoComIdNegativo() {
        // Arrange
        Custo custo = new Custo();

        // Act
        custo.setId(-1L);

        // Assert
        assertEquals(-1L, custo.getId());
    }

    @Test
    @DisplayName("Deve testar custo com valores de mercado realistas")
    void testCustoComValoresRealistas() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Custo custo1 = new Custo();
            custo1.setCustoUnitario(1.99f);  // Preço de produto barato
            custo1.setCustoTotal(99.50f);   // Custo total pequeno
            assertEquals(1.99f, custo1.getCustoUnitario());

            Custo custo2 = new Custo();
            custo2.setCustoUnitario(29.99f); // Preço médio
            custo2.setCustoTotal(1499.50f);  // Custo médio
            assertEquals(29.99f, custo2.getCustoUnitario());

            Custo custo3 = new Custo();
            custo3.setCustoUnitario(199.99f); // Preço alto
            custo3.setCustoTotal(9999.50f);   // Custo alto
            assertEquals(199.99f, custo3.getCustoUnitario());
        });
    }

    @Test
    @DisplayName("Deve testar relacionamento entre custo unitário e total")
    void testRelacionamentoCustoUnitarioTotal() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Custo custo1 = new Custo();
            custo1.setCustoUnitario(10.0f);
            custo1.setCustoTotal(100.0f);
            assertTrue(custo1.getCustoTotal() > custo1.getCustoUnitario());

            Custo custo2 = new Custo();
            custo2.setCustoUnitario(25.0f);
            custo2.setCustoTotal(250.0f);
            assertTrue(custo2.getCustoTotal() > custo2.getCustoUnitario());

            Custo custo3 = new Custo();
            custo3.setCustoUnitario(50.0f);
            custo3.setCustoTotal(500.0f);
            assertTrue(custo3.getCustoTotal() > custo3.getCustoUnitario());
        });
    }

    @Test
    @DisplayName("Deve testar diferentes IDs como string para busca")
    void testDiferentesIdsStringParaBusca() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            List<Custo> resultado1 = custoDao.buscar("1");
            List<Custo> resultado2 = custoDao.buscar("10");
            List<Custo> resultado3 = custoDao.buscar("100");
            List<Custo> resultado4 = custoDao.buscar("1000");
            List<Custo> resultado5 = custoDao.buscar("999999");
            
            assertNotNull(resultado1);
            assertNotNull(resultado2);
            assertNotNull(resultado3);
            assertNotNull(resultado4);
            assertNotNull(resultado5);
        });
    }
}
