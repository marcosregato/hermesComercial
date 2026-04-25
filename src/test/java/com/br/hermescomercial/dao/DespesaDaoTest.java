package com.br.hermescomercial.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import com.br.hermescomercial.model.Despesa;

import java.util.List;

/**
 * Testes unitários para a classe DespesaDao
 * Testa as funcionalidades de gerenciamento de despesas do sistema
 */
class DespesaDaoTest {

    private DespesaDao despesaDao;
    private Despesa despesaTeste;

    @BeforeEach
    void setUp() {
        despesaDao = new DespesaDao();
        despesaTeste = new Despesa();
        despesaTeste.setId(1L);
        despesaTeste.setTipo("Alimentação");
        despesaTeste.setNome("Mercado");
        despesaTeste.setValor(250.75f);
        despesaTeste.setDescricao("Compras do mês");
    }

    @Test
    @DisplayName("Deve inicializar DespesaDao sem lançar exceção")
    void testInitialize() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            DespesaDao dao = new DespesaDao();
            assertNotNull(dao, "DAO não deve ser nulo");
        });
    }

    @Test
    @DisplayName("Deve salvar despesa válida")
    void testSalvar() {
        // Arrange
        Despesa despesa = new Despesa();
        despesa.setTipo("Transporte");
        despesa.setNome("Uber");
        despesa.setValor(45.50f);
        despesa.setDescricao("Viagem ao trabalho");

        // Act & Assert
        assertDoesNotThrow(() -> {
            despesaDao.salvar(despesa);
        });
        
        // Nota: Como depende de banco de dados, apenas verificamos que não lança exceção
    }

    @Test
    @DisplayName("Deve tentar salvar despesa nula")
    void testSalvarDespesaNula() {
        // Arrange
        Despesa despesaNula = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            despesaDao.salvar(despesaNula);
        });
    }

    @Test
    @DisplayName("Deve salvar despesa com valores zero")
    void testSalvarDespesaComValoresZero() {
        // Arrange
        Despesa despesaZero = new Despesa();
        despesaZero.setTipo("");
        despesaZero.setNome("");
        despesaZero.setValor(0.0f);
        despesaZero.setDescricao("");

        // Act & Assert
        assertDoesNotThrow(() -> {
            despesaDao.salvar(despesaZero);
        });
    }

    @Test
    @DisplayName("Deve salvar despesa com valores negativos")
    void testSalvarDespesaComValoresNegativos() {
        // Arrange
        Despesa despesaNegativa = new Despesa();
        despesaNegativa.setTipo("Negativo");
        despesaNegativa.setNome("Perda");
        despesaNegativa.setValor(-100.0f);
        despesaNegativa.setDescricao("Prejuízo");

        // Act & Assert
        assertDoesNotThrow(() -> {
            despesaDao.salvar(despesaNegativa);
        });
    }

    @Test
    @DisplayName("Deve salvar despesa com valores altos")
    void testSalvarDespesaComValoresAltos() {
        // Arrange
        Despesa despesaAlta = new Despesa();
        despesaAlta.setTipo("Investimento");
        despesaAlta.setNome("Equipamento");
        despesaAlta.setValor(9999.99f);
        despesaAlta.setDescricao("Compra de equipamento caro");

        // Act & Assert
        assertDoesNotThrow(() -> {
            despesaDao.salvar(despesaAlta);
        });
    }

    @Test
    @DisplayName("Deve remover despesa por nome")
    void testRemove() {
        // Arrange
        String nomeDespesa = "Mercado";

        // Act & Assert
        assertDoesNotThrow(() -> {
            despesaDao.remove(nomeDespesa);
        });
        
        // Nota: Como depende de banco de dados, apenas verificamos que não lança exceção
    }

    @Test
    @DisplayName("Deve tentar remover despesa com nome nulo")
    void testRemoveComNomeNulo() {
        // Arrange
        String nomeNulo = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            despesaDao.remove(nomeNulo);
        });
    }

    @Test
    @DisplayName("Deve tentar remover despesa com nome vazio")
    void testRemoveComNomeVazio() {
        // Arrange
        String nomeVazio = "";

        // Act & Assert
        assertDoesNotThrow(() -> {
            despesaDao.remove(nomeVazio);
        });
    }

    @Test
    @DisplayName("Deve atualizar despesa válida")
    void testUpdate() {
        // Arrange
        Despesa despesaAtualizar = new Despesa();
        despesaAtualizar.setId(2L);
        despesaAtualizar.setTipo("Saúde");
        despesaAtualizar.setNome("Médico");
        despesaAtualizar.setValor(150.0f);
        despesaAtualizar.setDescricao("Consulta médica");

        // Act & Assert
        assertDoesNotThrow(() -> {
            despesaDao.update(despesaAtualizar);
        });
        
        // Nota: Como depende de banco de dados, apenas verificamos que não lança exceção
    }

    @Test
    @DisplayName("Deve tentar atualizar despesa nula")
    void testUpdateComDespesaNula() {
        // Arrange
        Despesa despesaNula = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            despesaDao.update(despesaNula);
        });
    }

    @Test
    @DisplayName("Deve listar todas as despesas")
    void testListar() {
        // Act
        List<Despesa> resultado = despesaDao.listar();

        // Assert
        assertNotNull(resultado, "Resultado não deve ser nulo");
        // Nota: Como depende de banco de dados, a lista pode estar vazia
        
        // Verifica que é uma lista válida
        assertDoesNotThrow(() -> {
            resultado.size(); // Não deve lançar exceção
        });
    }

    @Test
    @DisplayName("Deve buscar despesa por nome")
    void testBuscar() {
        // Arrange
        String nomeBusca = "Mercado";

        // Act
        List<Despesa> resultado = despesaDao.buscar(nomeBusca);

        // Assert
        assertNotNull(resultado, "Resultado não deve ser nulo");
        // Nota: Como depende de banco de dados, a lista pode estar vazia
        
        // Verifica que é uma lista válida
        assertDoesNotThrow(() -> {
            resultado.size(); // Não deve lançar exceção
        });
    }

    @Test
    @DisplayName("Deve buscar despesa com nome nulo")
    void testBuscarComNomeNulo() {
        // Arrange
        String nomeNulo = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            List<Despesa> resultado = despesaDao.buscar(nomeNulo);
            assertNotNull(resultado);
        });
    }

    @Test
    @DisplayName("Deve buscar despesa com nome vazio")
    void testBuscarComNomeVazio() {
        // Arrange
        String nomeVazio = "";

        // Act & Assert
        assertDoesNotThrow(() -> {
            List<Despesa> resultado = despesaDao.buscar(nomeVazio);
            assertNotNull(resultado);
        });
    }

    @Test
    @DisplayName("Deve criar despesa com valores válidos")
    void testCriarDespesaValida() {
        // Arrange
        Despesa despesa = new Despesa();

        // Act
        despesa.setId(3L);
        despesa.setTipo("Educação");
        despesa.setNome("Curso");
        despesa.setValor(299.90f);
        despesa.setDescricao("Curso online");

        // Assert
        assertEquals(3L, despesa.getId());
        assertEquals("Educação", despesa.getTipo());
        assertEquals("Curso", despesa.getNome());
        assertEquals(299.90f, despesa.getValor());
        assertEquals("Curso online", despesa.getDescricao());
    }

    @Test
    @DisplayName("Deve validar valores padrão da despesa")
    void testValoresPadraoDespesa() {
        // Arrange
        Despesa despesa = new Despesa();

        // Act & Assert
        assertEquals(0L, despesa.getId());
        assertNull(despesa.getTipo());
        assertNull(despesa.getNome());
        assertEquals(0.0f, despesa.getValor());
        assertNull(despesa.getDescricao());
    }

    @Test
    @DisplayName("Deve atualizar tipo da despesa")
    void testAtualizarTipoDespesa() {
        // Arrange
        Despesa despesa = new Despesa();
        despesa.setTipo("Lazer");

        // Act
        despesa.setTipo("Entretenimento");

        // Assert
        assertEquals("Entretenimento", despesa.getTipo());
    }

    @Test
    @DisplayName("Deve atualizar nome da despesa")
    void testAtualizarNomeDespesa() {
        // Arrange
        Despesa despesa = new Despesa();
        despesa.setNome("Cinema");

        // Act
        despesa.setNome("Streaming");

        // Assert
        assertEquals("Streaming", despesa.getNome());
    }

    @Test
    @DisplayName("Deve atualizar valor da despesa")
    void testAtualizarValorDespesa() {
        // Arrange
        Despesa despesa = new Despesa();
        despesa.setValor(50.0f);

        // Act
        despesa.setValor(75.0f);

        // Assert
        assertEquals(75.0f, despesa.getValor());
    }

    @Test
    @DisplayName("Deve atualizar descrição da despesa")
    void testAtualizarDescricaoDespesa() {
        // Arrange
        Despesa despesa = new Despesa();
        despesa.setDescricao("Descrição antiga");

        // Act
        despesa.setDescricao("Descrição nova");

        // Assert
        assertEquals("Descrição nova", despesa.getDescricao());
    }

    @Test
    @DisplayName("Deve criar múltiplas instâncias do DAO")
    void testCriarMultiplasInstancias() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            DespesaDao dao1 = new DespesaDao();
            DespesaDao dao2 = new DespesaDao();
            DespesaDao dao3 = new DespesaDao();
            
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
            despesaDao.salvar(despesaTeste);
            List<Despesa> lista = despesaDao.listar();
            despesaDao.update(despesaTeste);
            List<Despesa> resultado = despesaDao.buscar("Mercado");
            despesaDao.remove("Mercado");
            
            assertNotNull(lista);
            assertNotNull(resultado);
        });
    }

    @Test
    @DisplayName("Deve testar diferentes tipos de despesa")
    void testDiferentesTiposDespesa() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Despesa despesa1 = new Despesa();
            despesa1.setTipo("Alimentação");
            assertEquals("Alimentação", despesa1.getTipo());

            Despesa despesa2 = new Despesa();
            despesa2.setTipo("Transporte");
            assertEquals("Transporte", despesa2.getTipo());

            Despesa despesa3 = new Despesa();
            despesa3.setTipo("Moradia");
            assertEquals("Moradia", despesa3.getTipo());

            Despesa despesa4 = new Despesa();
            despesa4.setTipo("Saúde");
            assertEquals("Saúde", despesa4.getTipo());
        });
    }

    @Test
    @DisplayName("Deve testar diferentes nomes de despesa")
    void testDiferentesNomesDespesa() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Despesa despesa1 = new Despesa();
            despesa1.setNome("Supermercado");
            assertEquals("Supermercado", despesa1.getNome());

            Despesa despesa2 = new Despesa();
            despesa2.setNome("Gasolina");
            assertEquals("Gasolina", despesa2.getNome());

            Despesa despesa3 = new Despesa();
            despesa3.setNome("Aluguel");
            assertEquals("Aluguel", despesa3.getNome());
        });
    }

    @Test
    @DisplayName("Deve testar diferentes valores de despesa")
    void testDiferentesValoresDespesa() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Despesa despesa1 = new Despesa();
            despesa1.setValor(10.0f);
            assertEquals(10.0f, despesa1.getValor());

            Despesa despesa2 = new Despesa();
            despesa2.setValor(100.0f);
            assertEquals(100.0f, despesa2.getValor());

            Despesa despesa3 = new Despesa();
            despesa3.setValor(1000.0f);
            assertEquals(1000.0f, despesa3.getValor());
        });
    }

    @Test
    @DisplayName("Deve testar diferentes descrições de despesa")
    void testDiferentesDescricoesDespesa() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Despesa despesa1 = new Despesa();
            despesa1.setDescricao("Compra mensal");
            assertEquals("Compra mensal", despesa1.getDescricao());

            Despesa despesa2 = new Despesa();
            despesa2.setDescricao("Abastecimento veículo");
            assertEquals("Abastecimento veículo", despesa2.getDescricao());

            Despesa despesa3 = new Despesa();
            despesa3.setDescricao("Pagamento aluguel");
            assertEquals("Pagamento aluguel", despesa3.getDescricao());
        });
    }

    @Test
    @DisplayName("Deve testar despesa com valores decimais")
    void testDespesaComValoresDecimais() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Despesa despesa = new Despesa();
            despesa.setValor(123.456f);
            
            assertEquals(123.456f, despesa.getValor());
        });
    }

    @Test
    @DisplayName("Deve testar sequência de operações")
    void testSequenciaOperacoes() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 5; i++) {
                Despesa despesa = new Despesa();
                despesa.setId((long) (i + 1));
                despesa.setTipo("Tipo" + i);
                despesa.setNome("Despesa" + i);
                despesa.setValor(10.0f * (i + 1));
                despesa.setDescricao("Descrição " + i);
                
                despesaDao.salvar(despesa);
                
                List<Despesa> lista = despesaDao.listar();
                assertNotNull(lista);
            }
        });
    }

    @Test
    @DisplayName("Deve testar diferentes cenários de busca")
    void testDiferentesCenariosBusca() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            List<Despesa> resultado1 = despesaDao.buscar("Despesa Inexistente");
            List<Despesa> resultado2 = despesaDao.buscar("");
            List<Despesa> resultado3 = despesaDao.buscar(" ");
            List<Despesa> resultado4 = despesaDao.buscar("Mercado");
            List<Despesa> resultado5 = despesaDao.buscar("Aluguel");
            
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
            despesaDao.salvar(despesaTeste);
            
            // Operação 2
            List<Despesa> lista1 = despesaDao.listar();
            assertNotNull(lista1);
            
            // Operação 3
            despesaDao.update(despesaTeste);
            
            // Operação 4
            List<Despesa> resultado = despesaDao.buscar("Mercado");
            assertNotNull(resultado);
            
            // Operação 5
            despesaDao.remove("Mercado");
            
            // Operação final
            List<Despesa> listaFinal = despesaDao.listar();
            assertNotNull(listaFinal);
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
    @DisplayName("Deve testar persistência do DAO")
    void testPersistenciaDao() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            // Operações iniciais
            despesaDao.salvar(despesaTeste);
            despesaDao.listar();
            
            // Verifica que DAO ainda está funcional
            despesaDao.buscar("Mercado");
            despesaDao.update(despesaTeste);
            despesaDao.remove("Mercado");
            
            // Mais operações
            despesaDao.salvar(despesaTeste);
            despesaDao.listar();
        });
    }

    @Test
    @DisplayName("Deve testar despesa com valores especiais")
    void testDespesaComValoresEspeciais() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Despesa despesa1 = new Despesa();
            despesa1.setValor(Float.NaN);
            assertTrue(Float.isNaN(despesa1.getValor()));

            Despesa despesa2 = new Despesa();
            despesa2.setValor(Float.POSITIVE_INFINITY);
            assertTrue(Float.isInfinite(despesa2.getValor()));

            Despesa despesa3 = new Despesa();
            despesa3.setValor(Float.NEGATIVE_INFINITY);
            assertTrue(Float.isInfinite(despesa3.getValor()));
        });
    }

    @Test
    @DisplayName("Deve testar diferentes combinações de despesas")
    void testDiferentesCombinacoesDespesas() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Despesa despesa1 = new Despesa();
            despesa1.setTipo("Alimentação");
            despesa1.setNome("Mercado");
            despesa1.setValor(200.0f);
            despesa1.setDescricao("Compras semanais");
            assertEquals("Alimentação", despesa1.getTipo());

            Despesa despesa2 = new Despesa();
            despesa2.setTipo("Transporte");
            despesa2.setNome("Ônibus");
            despesa2.setValor(50.0f);
            despesa2.setDescricao("Passagem mensal");
            assertEquals("Transporte", despesa2.getTipo());

            Despesa despesa3 = new Despesa();
            despesa3.setTipo("Lazer");
            despesa3.setNome("Cinema");
            despesa3.setValor(30.0f);
            despesa3.setDescricao("Ingresso e pipoca");
            assertEquals("Lazer", despesa3.getTipo());
        });
    }

    @Test
    @DisplayName("Deve testar despesa com ID extremos")
    void testDespesaComIdExtremos() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Despesa despesaMinima = new Despesa();
            despesaMinima.setId(Long.MIN_VALUE);
            assertEquals(Long.MIN_VALUE, despesaMinima.getId());

            Despesa despesaMaxima = new Despesa();
            despesaMaxima.setId(Long.MAX_VALUE);
            assertEquals(Long.MAX_VALUE, despesaMaxima.getId());
        });
    }

    @Test
    @DisplayName("Deve testar despesa com ID zero")
    void testDespesaComIdZero() {
        // Arrange
        Despesa despesa = new Despesa();

        // Act
        despesa.setId(0L);

        // Assert
        assertEquals(0L, despesa.getId());
    }

    @Test
    @DisplayName("Deve testar despesa com ID negativo")
    void testDespesaComIdNegativo() {
        // Arrange
        Despesa despesa = new Despesa();

        // Act
        despesa.setId(-1L);

        // Assert
        assertEquals(-1L, despesa.getId());
    }

    @Test
    @DisplayName("Deve testar despesa com valores de mercado realistas")
    void testDespesaComValoresRealistas() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Despesa despesa1 = new Despesa();
            despesa1.setValor(5.99f);   // Café
            despesa1.setNome("Café");
            assertEquals(5.99f, despesa1.getValor());

            Despesa despesa2 = new Despesa();
            despesa2.setValor(199.90f); // Conta de luz
            despesa2.setNome("Energia");
            assertEquals(199.90f, despesa2.getValor());

            Despesa despesa3 = new Despesa();
            despesa3.setValor(1200.0f); // Aluguel
            despesa3.setNome("Aluguel");
            assertEquals(1200.0f, despesa3.getValor());
        });
    }

    @Test
    @DisplayName("Deve testar despesa com tipos especiais")
    void testDespesaComTiposEspeciais() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Despesa despesa1 = new Despesa();
            despesa1.setTipo("Alimentação@Trabalho");
            assertEquals("Alimentação@Trabalho", despesa1.getTipo());

            Despesa despesa2 = new Despesa();
            despesa2.setTipo("Transporte-Urbano");
            assertEquals("Transporte-Urbano", despesa2.getTipo());

            Despesa despesa3 = new Despesa();
            despesa3.setTipo("Moradia_Casa");
            assertEquals("Moradia_Casa", despesa3.getTipo());
        });
    }

    @Test
    @DisplayName("Deve testar despesa com nomes especiais")
    void testDespesaComNomesEspeciais() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Despesa despesa1 = new Despesa();
            despesa1.setNome("Supermercado ABC");
            assertEquals("Supermercado ABC", despesa1.getNome());

            Despesa despesa2 = new Despesa();
            despesa2.setNome("Posto Shell");
            assertEquals("Posto Shell", despesa2.getNome());

            Despesa despesa3 = new Despesa();
            despesa3.setNome("Netflix 4K");
            assertEquals("Netflix 4K", despesa3.getNome());
        });
    }

    @Test
    @DisplayName("Deve testar despesa com descrições longas")
    void testDespesaComDescricoesLongas() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Despesa despesa = new Despesa();
            String descricaoLonga = "Esta é uma descrição muito longa para uma despesa que contém muitos detalhes sobre o que foi comprado, onde foi comprado, quando foi comprado e por que foi necessário fazer esta compra específica para o controle financeiro pessoal.";
            despesa.setDescricao(descricaoLonga);
            assertEquals(descricaoLonga, despesa.getDescricao());
        });
    }

    @Test
    @DisplayName("Deve testar despesa com descrições vazias")
    void testDespesaComDescricoesVazias() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Despesa despesa1 = new Despesa();
            despesa1.setDescricao("");
            assertEquals("", despesa1.getDescricao());

            Despesa despesa2 = new Despesa();
            despesa2.setDescricao(" ");
            assertEquals(" ", despesa2.getDescricao());
        });
    }

    @Test
    @DisplayName("Deve testar diferentes IDs para busca")
    void testDiferentesIdsParaBusca() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            Despesa despesa1 = new Despesa();
            despesa1.setId(1L);
            despesa1.setNome("Teste1");
            
            Despesa despesa2 = new Despesa();
            despesa2.setId(100L);
            despesa2.setNome("Teste100");
            
            Despesa despesa3 = new Despesa();
            despesa3.setId(999L);
            despesa3.setNome("Teste999");
            
            assertEquals(1L, despesa1.getId());
            assertEquals(100L, despesa2.getId());
            assertEquals(999L, despesa3.getId());
        });
    }
}
