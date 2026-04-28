package com.br.hermescomercial.pdv.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import com.br.hermescomercial.model.Despesa;

/**
 * Testes unitários para a classe DespesaController
 * Testa as funcionalidades de gerenciamento de despesas do sistema
 */
class DespesaControllerTest {

    private com.br.hermescomercial.pdv.controller.DespesaController despesaController;
    private Despesa despesaTeste;

    @BeforeEach
    void setUp() {
        despesaController = new com.br.hermescomercial.pdv.controller.DespesaController();
        despesaTeste = new Despesa();
        despesaTeste.setId(1L);
        despesaTeste.setTipo("Alimentação");
        despesaTeste.setValor(150.75);
        despesaTeste.setNome("Almoço Executivo");
        despesaTeste.setDescricao("Refeição com cliente");
    }

    @Test
    @DisplayName("Deve inicializar DespesaController sem lançar exceção")
    void testInitialize() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            DespesaController controller = new DespesaController();
            assertNotNull(controller, "Controller não deve ser nulo");
        });
    }

    @Test
    @DisplayName("Deve salvar despesa")
    void testSalvarDespesa() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            despesaController.salvar();
        });
        
        // Nota: Como depende de banco de dados, apenas verificamos que não lança exceção
    }

    @Test
    @DisplayName("Deve remover despesa")
    void testDeletarDespesa() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            despesaController.remove();
        });
        
        // Nota: Como depende de banco de dados, apenas verificamos que não lança exceção
    }

    @Test
    @DisplayName("Deve atualizar despesa válida")
    void testAlterarDespesa() {
        // Arrange
        Despesa despesaAtualizar = new Despesa();
        despesaAtualizar.setId(2L);
        despesaAtualizar.setTipo("Transporte");
        despesaAtualizar.setValor(85.50f);
        despesaAtualizar.setNome("Uber");
        despesaAtualizar.setDescricao("Viagem para reunião");

        // Act & Assert
        assertDoesNotThrow(() -> {
            despesaController.update(despesaAtualizar);
        });
        
        // Nota: Como depende de banco de dados, apenas verificamos que não lança exceção
    }

    @Test
    @DisplayName("Deve tentar atualizar despesa nula")
    void testAlterarDespesaNula() {
        // Arrange
        Despesa despesaNula = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            despesaController.update(despesaNula);
        });
    }

    @Test
    @DisplayName("Deve listar todas as despesas")
    void testListarDespesa() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            despesaController.listar();
        });
        
        // Nota: Como depende de banco de dados, apenas verificamos que não lança exceção
    }

    @Test
    @DisplayName("Deve buscar despesa por nome")
    void testBuscarDespesa() {
        // Arrange
        String nomeBusca = "Almoço Executivo";

        // Act & Assert
        assertDoesNotThrow(() -> {
            despesaController.buscar(nomeBusca);
        });
        
        // Nota: Como depende de banco de dados, apenas verificamos que não lança exceção
    }

    @Test
    @DisplayName("Deve buscar despesa com nome nulo")
    void testBuscarDespesaComNomeNulo() {
        // Arrange
        String nomeNulo = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            despesaController.buscar(nomeNulo);
        });
    }

    @Test
    @DisplayName("Deve buscar despesa com nome vazio")
    void testBuscarDespesaComNomeVazio() {
        // Arrange
        String nomeVazio = "";

        // Act & Assert
        assertDoesNotThrow(() -> {
            despesaController.buscar(nomeVazio);
        });
    }

    @Test
    @DisplayName("Deve criar despesa com valores válidos")
    void testCriarDespesaValida() {
        // Arrange
        Despesa despesa = new Despesa();

        // Act
        despesa.setId(3L);
        despesa.setTipo("Material de Escritório");
        despesa.setValor(250.00f);
        despesa.setNome("Canetas e Papel");
        despesa.setDescricao("Compra de material para escritório");

        // Assert
        assertEquals(3L, despesa.getId());
        assertEquals("Material de Escritório", despesa.getTipo());
        assertEquals(250.00f, despesa.getValor());
        assertEquals("Canetas e Papel", despesa.getNome());
        assertEquals("Compra de material para escritório", despesa.getDescricao());
    }

    @Test
    @DisplayName("Deve validar valores padrão da despesa")
    void testValoresPadraoDespesa() {
        // Arrange
        Despesa despesa = new Despesa();

        // Act & Assert
        assertEquals(0L, despesa.getId());
        assertNull(despesa.getTipo());
        assertNull(despesa.getValor());
        assertNull(despesa.getNome());
        assertNull(despesa.getDescricao());
    }

    @Test
    @DisplayName("Deve atualizar tipo da despesa")
    void testAtualizarTipoDespesa() {
        // Arrange
        Despesa despesa = new Despesa();
        despesa.setTipo("Alimentação");

        // Act
        despesa.setTipo("Transporte");

        // Assert
        assertEquals("Transporte", despesa.getTipo());
    }

    @Test
    @DisplayName("Deve atualizar valor da despesa")
    void testAtualizarValorDespesa() {
        // Arrange
        Despesa despesa = new Despesa();
        despesa.setValor(100.00f);

        // Act
        despesa.setValor(200.00f);

        // Assert Assert
        assertEquals(200.00f, despesa.getValor());
    }

    @Test
    @DisplayName("Deve atualizar nome da despesa")
    void testAtualizarNomeDespesa() {
        // Arrange
        Despesa despesa = new Despesa();
        despesa.setNome("Nome Original");

        // Act
        despesa.setNome("Nome Atualizado");

        // Assert
        assertEquals("Nome Atualizado", despesa.getNome());
    }

    @Test
    @DisplayName("Deve atualizar descrição da despesa")
    void testAtualizarDescricaoDespesa() {
        // Arrange
        Despesa despesa = new Despesa();
        despesa.setDescricao("Descrição Original");

        // Act
        despesa.setDescricao("Descrição Atualizada");

        // Assert
        assertEquals("Descrição Atualizada", despesa.getDescricao());
    }

    @Test
    @DisplayName("Deve testar despesa com valor decimal")
    void testDespesaComValorDecimal() {
        // Arrange
        Despesa despesa = new Despesa();

        // Act
        despesa.setValor(123.45f);

        // Assert
        assertEquals(123.45f, despesa.getValor());
    }

    @Test
    @DisplayName("Deve testar despesa com valor zero")
    void testDespesaComValorZero() {
        // Arrange
        Despesa despesa = new Despesa();

        // Act
        despesa.setValor(0.0f);

        // Assert
        assertEquals(0.0f, despesa.getValor());
    }

    @Test
    @DisplayName("Deve testar despesa com valor negativo")
    void testDespesaComValorNegativo() {
        // Arrange
        Despesa despesa = new Despesa();

        // Act
        despesa.setValor(-50.00f);

        // Assert
        assertEquals(-50.00f, despesa.getValor());
    }

    @Test
    @DisplayName("Deve criar múltiplas instâncias do controller")
    void testCriarMultiplasInstancias() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            DespesaController controller1 = new DespesaController();
            DespesaController controller2 = new DespesaController();
            DespesaController controller3 = new DespesaController();
            
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
            despesaController.salvar();
            despesaController.listar();
            despesaController.update(despesaTeste);
            despesaController.buscar("Teste");
            despesaController.remove();
        });
    }

    @Test
    @DisplayName("Deve testar diferentes tipos de despesa")
    void testDiferentesTiposDespesa() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Despesa alimentacao = new Despesa();
            alimentacao.setTipo("Alimentação");
            assertEquals("Alimentação", alimentacao.getTipo());

            Despesa transporte = new Despesa();
            transporte.setTipo("Transporte");
            assertEquals("Transporte", transporte.getTipo());

            Despesa material = new Despesa();
            material.setTipo("Material de Escritório");
            assertEquals("Material de Escritório", material.getTipo());

            Despesa servico = new Despesa();
            servico.setTipo("Serviços");
            assertEquals("Serviços", servico.getTipo());
        });
    }

    @Test
    @DisplayName("Deve testar diferentes valores de despesa")
    void testDiferentesValoresDespesa() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Despesa despesaBaixa = new Despesa();
            despesaBaixa.setValor(5.00f);
            assertEquals(5.00f, despesaBaixa.getValor());

            Despesa despesaMedia = new Despesa();
            despesaMedia.setValor(100.00f);
            assertEquals(100.00f, despesaMedia.getValor());

            Despesa despesaAlta = new Despesa();
            despesaAlta.setValor(5000.00f);
            assertEquals(5000.00f, despesaAlta.getValor());
        });
    }

    @Test
    @DisplayName("Deve testar despesa com valores extremos")
    void testDespesaComValoresExtremos() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Despesa despesaMinima = new Despesa();
            despesaMinima.setValor(Float.MIN_VALUE);
            assertEquals(Float.MIN_VALUE, despesaMinima.getValor());

            Despesa despesaMaxima = new Despesa();
            despesaMaxima.setValor(Float.MAX_VALUE);
            assertEquals(Float.MAX_VALUE, despesaMaxima.getValor());
        });
    }

    @Test
    @DisplayName("Deve testar sequência de operações")
    void testSequenciaOperacoes() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 5; i++) {
                despesaController.salvar();
                despesaController.listar();
                despesaController.buscar("Despesa" + i);
                despesaController.update(despesaTeste);
                despesaController.remove();
            }
        });
    }

    @Test
    @DisplayName("Deve testar diferentes cenários de busca")
    void testDiferentesCenariosBusca() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            despesaController.buscar("Despesa Inexistente");
            despesaController.buscar("");
            despesaController.buscar(" ");
            despesaController.buscar("Despesa Com Espaços");
            despesaController.buscar("Despesa@Especial#123");
        });
    }

    @Test
    @DisplayName("Deve testar despesa com nomes especiais")
    void testDespesaComNomesEspeciais() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Despesa despesa1 = new Despesa();
            despesa1.setNome("Despesa com Acentuação");
            assertEquals("Despesa com Acentuação", despesa1.getNome());

            Despesa despesa2 = new Despesa();
            despesa2.setNome("Despesa@Especial#123");
            assertEquals("Despesa@Especial#123", despesa2.getNome());

            Despesa despesa3 = new Despesa();
            despesa3.setNome("Despesa com Espaços");
            assertEquals("Despesa com Espaços", despesa3.getNome());
        });
    }

    @Test
    @DisplayName("Deve testar despesa com descrições longas")
    void testDespesaComDescricoesLongas() {
        // Arrange
        String descricaoLonga = "Esta é uma descrição muito longa para uma despesa que serve para testar se o sistema consegue lidar com textos extensos sem problemas de memória ou processamento.";

        // Act
        Despesa despesa = new Despesa();
        despesa.setDescricao(descricaoLonga);

        // Assert
        assertEquals(descricaoLonga, despesa.getDescricao());
    }

    @Test
    @DisplayName("Deve testar estado do controller após múltiplas operações")
    void testEstadoControllerMultiplasOperacoes() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            // Operação 1
            despesaController.salvar();
            
            // Operação 2
            despesaController.listar();
            
            // Operação 3
            despesaController.update(despesaTeste);
            
            // Operação 4
            despesaController.buscar("Teste");
            
            // Operação 5
            despesaController.remove();
            
            // Operação final
            despesaController.listar();
        });
    }

    @Test
    @DisplayName("Deve testar despesa com ID")
    void testDespesaComId() {
        // Arrange
        Despesa despesa = new Despesa();

        // Act
        despesa.setId(12345L);

        // Assert
        assertEquals(12345L, despesa.getId());
    }

    @Test
    @DisplayName("Deve testar diferentes IDs de despesa")
    void testDiferentesIdsDespesa() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Despesa despesa1 = new Despesa();
            despesa1.setId(1L);
            assertEquals(1L, despesa1.getId());

            Despesa despesa2 = new Despesa();
            despesa2.setId(999L);
            assertEquals(999L, despesa2.getId());

            Despesa despesa3 = new Despesa();
            despesa3.setId(Long.MAX_VALUE);
            assertEquals(Long.MAX_VALUE, despesa3.getId());
        });
    }

    @Test
    @DisplayName("Deve testar despesa com valores nulos")
    void testDespesaComValoresNulos() {
        // Arrange
        Despesa despesa = new Despesa();

        // Act
        despesa.setTipo(null);
        despesa.setValor(null);
        despesa.setNome(null);
        despesa.setDescricao(null);

        // Assert
        assertNull(despesa.getTipo());
        assertNull(despesa.getValor());
        assertNull(despesa.getNome());
        assertNull(despesa.getDescricao());
    }

    @Test
    @DisplayName("Deve testar despesa com strings vazias")
    void testDespesaComStringsVazias() {
        // Arrange
        Despesa despesa = new Despesa();

        // Act
        despesa.setTipo("");
        despesa.setNome("");
        despesa.setDescricao("");

        // Assert
        assertEquals("", despesa.getTipo());
        assertEquals("", despesa.getNome());
        assertEquals("", despesa.getDescricao());
    }
}
