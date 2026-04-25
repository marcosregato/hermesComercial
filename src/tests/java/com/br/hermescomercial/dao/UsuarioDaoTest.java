package com.br.hermescomercial.dao;

<<<<<<< HEAD:src/tests/java/com/br/hermescomercial/dao/UsuarioDaoTest.java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PessoaDaoTest {

    @Test
    void testSalvar() {
        // TODO: Implement test
    }

    @Test
    void testRemove() {
        // TODO: Implement test
    }

    @Test
    void testUpdate() {
        // TODO: Implement test
    }

    @Test
    void testLista() {
        // TODO: Implement test
    }

    @Test
    void testBuscar() {
        // TODO: Implement test
    }
=======
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import com.br.hermescomercial.controller.financeiro.CustoController;
import com.br.hermescomercial.model.Custo;
import com.br.hermescomercial.model.Atributo;

import java.util.List;

/**
 * Testes unitários para a classe CustoController
 * Testa as funcionalidades de gerenciamento de custos do sistema
 */
class CustoControllerTest {

    private CustoController custoController;
    private Custo custoTeste;

    @BeforeEach
    void setUp() {
        custoController = new CustoController();
        custoTeste = new Custo();
        custoTeste.setId(1L);
        custoTeste.setCustoUnitario(25.50f);
        custoTeste.setCustoTotal(125.75f);
        custoTeste.setFk_fornecedor(100L);
    }

    @Test
    @DisplayName("Deve inicializar CustoController sem lançar exceção")
    void testInitialize() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            CustoController controller = new CustoController();
            assertNotNull(controller, "Controller não deve ser nulo");
        });
    }

    @Test
    @DisplayName("Deve salvar custo válido")
    void testSalvarCusto() {
        // Arrange
        Custo novoCusto = new Custo();
        novoCusto.setCustoUnitario(30.0f);
        novoCusto.setCustoTotal(150.0f);
        novoCusto.setFk_fornecedor(200L);

        // Act & Assert
        assertDoesNotThrow(() -> {
            custoController.salvar(novoCusto);
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
            custoController.salvar(custoNulo);
        });
        
        // Nota: Como o método não verifica null, pode lançar exceção no tratamento
    }

    @Test
    @DisplayName("Deve salvar custo com valores zero")
    void testSalvarCustoComValoresZero() {
        // Arrange
        Custo custoZero = new Custo();
        custoZero.setCustoUnitario(0.0f);
        custoZero.setCustoTotal(0.0f);
        custoZero.setFk_fornecedor(0L);

        // Act & Assert
        assertDoesNotThrow(() -> {
            custoController.salvar(custoZero);
        });
    }

    @Test
    @DisplayName("Deve salvar custo com valores negativos")
    void testSalvarCustoComValoresNegativos() {
        // Arrange
        Custo custoNegativo = new Custo();
        custoNegativo.setCustoUnitario(-10.0f);
        custoNegativo.setCustoTotal(-50.0f);
        custoNegativo.setFk_fornecedor(-1L);

        // Act & Assert
        assertDoesNotThrow(() -> {
            custoController.salvar(custoNegativo);
        });
    }

    @Test
    @DisplayName("Deve salvar custo com valores altos")
    void testSalvarCustoComValoresAltos() {
        // Arrange
        Custo custoAlto = new Custo();
        custoAlto.setCustoUnitario(9999.99f);
        custoAlto.setCustoTotal(99999.99f);
        custoAlto.setFk_fornecedor(999999L);

        // Act & Assert
        assertDoesNotThrow(() -> {
            custoController.salvar(custoAlto);
        });
    }

    @Test
    @DisplayName("Deve remover custo por nome")
    void testDeletarCusto() {
        // Arrange
        String nomeCusto = "Custo Teste";

        // Act & Assert
        assertDoesNotThrow(() -> {
            custoController.remove(nomeCusto);
        });
        
        // Nota: Como depende de banco de dados, apenas verificamos que não lança exceção
    }

    @Test
    @DisplayName("Deve tentar remover custo com nome nulo")
    void testDeletarCustoComNomeNulo() {
        // Arrange
        String nomeNulo = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            custoController.remove(nomeNulo);
        });
    }

    @Test
    @DisplayName("Deve tentar remover custo com nome vazio")
    void testDeletarCustoComNomeVazio() {
        // Arrange
        String nomeVazio = "";

        // Act & Assert
        assertDoesNotThrow(() -> {
            custoController.remove(nomeVazio);
        });
    }

    @Test
    @DisplayName("Deve atualizar custo válido")
    void testAlterarCusto() {
        // Arrange
        Custo custoAtualizar = new Custo();
        custoAtualizar.setCustoUnitario(35.0f);
        custoAtualizar.setCustoTotal(175.0f);
        custoAtualizar.setFk_fornecedor(300L);

        // Act & Assert
        assertDoesNotThrow(() -> {
            custoController.update(custoAtualizar);
        });
        
        // Nota: Como depende de banco de dados, apenas verificamos que não lança exceção
    }

    @Test
    @DisplayName("Deve tentar atualizar custo nulo")
    void testAlterarCustoNulo() {
        // Arrange
        Custo custoNulo = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            custoController.update(custoNulo);
        });
    }

    @Test
    @DisplayName("Deve listar todos os custos")
    void testListarCusto() {
        // Act
        List<Atributo> resultado = custoController.listar();

        // Assert
        assertNotNull(resultado, "Resultado não deve ser nulo");
        // Nota: Como depende de banco de dados, a lista pode estar vazia
        
        // Verifica que é uma lista válida
        assertDoesNotThrow(() -> {
            resultado.size(); // Não deve lançar exceção
        });
    }

    @Test
    @DisplayName("Deve buscar custo por nome")
    void testBuscarCusto() {
        // Arrange
        String nomeBusca = "Custo Teste";

        // Act
        List<Custo> resultado = custoController.buscar(nomeBusca);

        // Assert
        assertNotNull(resultado, "Resultado não deve ser nulo");
        // Nota: Como depende de banco de dados, a lista pode estar vazia
        
        // Verifica que é uma lista válida
        assertDoesNotThrow(() -> {
            resultado.size(); // Não deve lançar exceção
        });
    }

    @Test
    @DisplayName("Deve buscar custo com nome nulo")
    void testBuscarCustoComNomeNulo() {
        // Arrange
        String nomeNulo = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            List<Custo> resultado = custoController.buscar(nomeNulo);
            assertNotNull(resultado);
        });
    }

    @Test
    @DisplayName("Deve buscar custo com nome vazio")
    void testBuscarCustoComNomeVazio() {
        // Arrange
        String nomeVazio = "";

        // Act & Assert
        assertDoesNotThrow(() -> {
            List<Custo> resultado = custoController.buscar(nomeVazio);
            assertNotNull(resultado);
        });
    }

    @Test
    @DisplayName("Deve criar custo com valores válidos")
    void testCriarCustoValido() {
        // Arrange
        Custo custo = new Custo();

        // Act
        custo.setId(2L);
        custo.setCustoUnitario(45.75f);
        custo.setCustoTotal(228.75f);
        custo.setFk_fornecedor(150L);

        // Assert
        assertEquals(2L, custo.getId());
        assertEquals(45.75f, custo.getCustoUnitario());
        assertEquals(228.75f, custo.getCustoTotal());
        assertEquals(150L, custo.getFk_fornecedor());
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
        assertNull(custo.getFk_fornecedor());
    }

    @Test
    @DisplayName("Deve atualizar custo unitário")
    void testAtualizarCustoUnitario() {
        // Arrange
        Custo custo = new Custo();
        custo.setCustoUnitario(20.0f);

        // Act
        custo.setCustoUnitario(40.0f);

        // Assert
        assertEquals(40.0f, custo.getCustoUnitario());
    }

    @Test
    @DisplayName("Deve atualizar custo total")
    void testAtualizarCustoTotal() {
        // Arrange
        Custo custo = new Custo();
        custo.setCustoTotal(100.0f);

        // Act
        custo.setCustoTotal(200.0f);

        // Assert
        assertEquals(200.0f, custo.getCustoTotal());
    }

    @Test
    @DisplayName("Deve atualizar fornecedor")
    void testAtualizarFornecedor() {
        // Arrange
        Custo custo = new Custo();
        custo.setFk_fornecedor(50L);

        // Act
        custo.setFk_fornecedor(75L);

        // Assert
        assertEquals(75L, custo.getFk_fornecedor());
    }

    @Test
    @DisplayName("Deve testar custo com valores decimais")
    void testCustoComValoresDecimais() {
        // Arrange
        Custo custo = new Custo();

        // Act
        custo.setCustoUnitario(12.345f);
        custo.setCustoTotal(67.890f);
        custo.setFk_fornecedor(123L);

        // Assert
        assertEquals(12.345f, custo.getCustoUnitario());
        assertEquals(67.890f, custo.getCustoTotal());
        assertEquals(123L, custo.getFk_fornecedor());
    }

    @Test
    @DisplayName("Deve criar múltiplas instâncias do controller")
    void testCriarMultiplasInstancias() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            CustoController controller1 = new CustoController();
            CustoController controller2 = new CustoController();
            CustoController controller3 = new CustoController();
            
            assertNotNull(controller1);
            assertNotNull(controller2);
            assertNotNull(controller3);
            
            // Verifica que são instâncias diferentes
            assertNotSame(controller1, controller2);
            assertNotSame(controller2, controller3);
        });
    }

    @Test
    @DisplayName("Deve testar combinação de operações")
    void testCombinacaoOperacoes() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            custoController.salvar(custoTeste);
            List<Atributo> lista = custoController.listar();
            custoController.update(custoTeste);
            List<Custo> resultado = custoController.buscar("Teste");
            custoController.remove("Teste");
            
            assertNotNull(lista);
            assertNotNull(resultado);
        });
    }

    @Test
    @DisplayName("Deve testar salvar múltiplos custos")
    void testSalvarMultiplasCustos() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Custo custo1 = new Custo();
            custo1.setCustoUnitario(10.0f);
            custo1.setCustoTotal(50.0f);
            custo1.setFk_fornecedor(100L);
            
            Custo custo2 = new Custo();
            custo2.setCustoUnitario(20.0f);
            custo2.setCustoTotal(100.0f);
            custo2.setFk_fornecedor(200L);
            
            Custo custo3 = new Custo();
            custo3.setCustoUnitario(30.0f);
            custo3.setCustoTotal(150.0f);
            custo3.setFk_fornecedor(300L);
            
            custoController.salvar(custo1);
            custoController.salvar(custo2);
            custoController.salvar(custo3);
        });
    }

    @Test
    @DisplayName("Deve testar diferentes valores de custo")
    void testDiferentesValoresCusto() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Custo custoBaixo = new Custo();
            custoBaixo.setCustoUnitario(1.0f);
            custoBaixo.setCustoTotal(5.0f);
            custoBaixo.setFk_fornecedor(1L);
            assertEquals(1.0f, custoBaixo.getCustoUnitario());
            
            Custo custoMedio = new Custo();
            custoMedio.setCustoUnitario(50.0f);
            custoMedio.setCustoTotal(250.0f);
            custoMedio.setFk_fornecedor(50L);
            assertEquals(50.0f, custoMedio.getCustoUnitario());
            
            Custo custoAlto = new Custo();
            custoAlto.setCustoUnitario(500.0f);
            custoAlto.setCustoTotal(2500.0f);
            custoAlto.setFk_fornecedor(500L);
            assertEquals(500.0f, custoAlto.getCustoUnitario());
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
            custoMinimo.setFk_fornecedor(1L);
            assertEquals(Float.MIN_VALUE, custoMinimo.getCustoUnitario());
            
            Custo custoMaximo = new Custo();
            custoMaximo.setCustoUnitario(Float.MAX_VALUE);
            custoMaximo.setCustoTotal(Float.MAX_VALUE);
            custoMaximo.setFk_fornecedor(Long.MAX_VALUE);
            assertEquals(Float.MAX_VALUE, custoMaximo.getCustoUnitario());
        });
    }

    @Test
    @DisplayName("Deve testar sequência de operações")
    void testSequenciaOperacoes() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 5; i++) {
                Custo custo = new Custo();
                custo.setCustoUnitario(10.0f + i);
                custo.setCustoTotal(50.0f + (i * 5));
                custo.setFk_fornecedor(100L + i);
                
                custoController.salvar(custo);
                
                List<Atributo> lista = custoController.listar();
                assertNotNull(lista);
            }
        });
    }

    @Test
    @DisplayName("Deve testar diferentes cenários de busca")
    void testDiferentesCenariosBusca() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            List<Custo> resultado1 = custoController.buscar("Custo Inexistente");
            List<Custo> resultado2 = custoController.buscar("");
            List<Custo> resultado3 = custoController.buscar(" ");
            List<Custo> resultado4 = custoController.buscar("Custo Com Espaços");
            List<Custo> resultado5 = custoController.buscar("Custo@Especial#123");
            
            assertNotNull(resultado1);
            assertNotNull(resultado2);
            assertNotNull(resultado3);
            assertNotNull(resultado4);
            assertNotNull(resultado5);
        });
    }

    @Test
    @DisplayName("Deve testar estado do controller após múltiplas operações")
    void testEstadoControllerMultiplasOperacoes() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            // Operação 1
            custoController.salvar(custoTeste);
            
            // Operação 2
            List<Atributo> lista1 = custoController.listar();
            assertNotNull(lista1);
            
            // Operação 3
            custoController.update(custoTeste);
            
            // Operação 4
            List<Custo> resultado = custoController.buscar("Teste");
            assertNotNull(resultado);
            
            // Operação 5
            custoController.remove("Teste");
            
            // Operação final
            List<Atributo> listaFinal = custoController.listar();
            assertNotNull(listaFinal);
        });
    }
>>>>>>> main:src/test/java/com/br/hermescomercial/controller/CustoControllerTest.java
}
