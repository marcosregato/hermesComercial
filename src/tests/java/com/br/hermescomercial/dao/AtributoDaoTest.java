package com.br.hermescomercial.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import com.br.hermescomercial.model.Atributo;

import java.util.List;

/**
 * Testes unitários para a classe AtributoDao
 * Testa as funcionalidades de gerenciamento de atributos do sistema
 */
class AtributoDaoTest {

    private AtributoDao atributoDao;
    private Atributo atributoTeste;

    @BeforeEach
    void setUp() {
        atributoDao = new AtributoDao();
        atributoTeste = new Atributo();
        atributoTeste.setImpostoFederal(10.5f);
        atributoTeste.setImpostoEstadual(5.25f);
        atributoTeste.setImpostoMunicipal(2.75f);
    }

    @Test
    @DisplayName("Deve inicializar AtributoDao sem lançar exceção")
    void testInitialize() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            AtributoDao dao = new AtributoDao();
            assertNotNull(dao, "DAO não deve ser nulo");
        });
    }

    @Test
    @DisplayName("Deve salvar atributo válido")
    void testSalvar() {
        // Arrange
        Atributo atributo = new Atributo();
        atributo.setImpostoFederal(15.0f);
        atributo.setImpostoEstadual(7.5f);
        atributo.setImpostoMunicipal(3.25f);

        // Act & Assert
        assertDoesNotThrow(() -> {
            atributoDao.salvar(atributo);
        });
        
        // Nota: Como depende de banco de dados, apenas verificamos que não lança exceção
    }

    @Test
    @DisplayName("Deve tentar salvar atributo nulo")
    void testSalvarAtributoNulo() {
        // Arrange
        Atributo atributoNulo = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            atributoDao.salvar(atributoNulo);
        });
    }

    @Test
    @DisplayName("Deve salvar atributo com valores zero")
    void testSalvarAtributoComValoresZero() {
        // Arrange
        Atributo atributoZero = new Atributo();
        atributoZero.setImpostoFederal(0.0f);
        atributoZero.setImpostoEstadual(0.0f);
        atributoZero.setImpostoMunicipal(0.0f);

        // Act & Assert
        assertDoesNotThrow(() -> {
            atributoDao.salvar(atributoZero);
        });
    }

    @Test
    @DisplayName("Deve salvar atributo com valores negativos")
    void testSalvarAtributoComValoresNegativos() {
        // Arrange
        Atributo atributoNegativo = new Atributo();
        atributoNegativo.setImpostoFederal(-5.0f);
        atributoNegativo.setImpostoEstadual(-2.5f);
        atributoNegativo.setImpostoMunicipal(-1.25f);

        // Act & Assert
        assertDoesNotThrow(() -> {
            atributoDao.salvar(atributoNegativo);
        });
    }

    @Test
    @DisplayName("Deve salvar atributo com valores altos")
    void testSalvarAtributoComValoresAltos() {
        // Arrange
        Atributo atributoAlto = new Atributo();
        atributoAlto.setImpostoFederal(99.99f);
        atributoAlto.setImpostoEstadual(49.99f);
        atributoAlto.setImpostoMunicipal(25.99f);

        // Act & Assert
        assertDoesNotThrow(() -> {
            atributoDao.salvar(atributoAlto);
        });
    }

    @Test
    @DisplayName("Deve remover atributo por nome")
    void testRemove() {
        // Arrange
        String nomeAtributo = "Atributo Teste";

        // Act & Assert
        assertDoesNotThrow(() -> {
            atributoDao.remove(nomeAtributo);
        });
        
        // Nota: Como depende de banco de dados, apenas verificamos que não lança exceção
    }

    @Test
    @DisplayName("Deve tentar remover atributo com nome nulo")
    void testRemoveComNomeNulo() {
        // Arrange
        String nomeNulo = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            atributoDao.remove(nomeNulo);
        });
    }

    @Test
    @DisplayName("Deve tentar remover atributo com nome vazio")
    void testRemoveComNomeVazio() {
        // Arrange
        String nomeVazio = "";

        // Act & Assert
        assertDoesNotThrow(() -> {
            atributoDao.remove(nomeVazio);
        });
    }

    @Test
    @DisplayName("Deve atualizar atributo válido")
    void testUpdate() {
        // Arrange
        Atributo atributoAtualizar = new Atributo();
        atributoAtualizar.setImpostoFederal(20.0f);
        atributoAtualizar.setImpostoEstadual(10.0f);
        atributoAtualizar.setImpostoMunicipal(5.0f);

        // Act & Assert
        assertDoesNotThrow(() -> {
            atributoDao.update(atributoAtualizar);
        });
        
        // Nota: Como depende de banco de dados, apenas verificamos que não lança exceção
    }

    @Test
    @DisplayName("Deve tentar atualizar atributo nulo")
    void testUpdateComAtributoNulo() {
        // Arrange
        Atributo atributoNulo = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            atributoDao.update(atributoNulo);
        });
    }

    @Test
    @DisplayName("Deve listar todos os atributos")
    void testListar() {
        // Act
        List<Atributo> resultado = atributoDao.listar();

        // Assert
        assertNotNull(resultado, "Resultado não deve ser nulo");
        // Nota: Como depende de banco de dados, a lista pode estar vazia
        
        // Verifica que é uma lista válida
        assertDoesNotThrow(() -> {
            resultado.size(); // Não deve lançar exceção
        });
    }

    @Test
    @DisplayName("Deve buscar atributo por nome")
    void testBuscar() {
        // Arrange
        String nomeBusca = "Atributo Teste";

        // Act
        List<Atributo> resultado = atributoDao.buscar(nomeBusca);

        // Assert
        assertNotNull(resultado, "Resultado não deve ser nulo");
        // Nota: Como depende de banco de dados, a lista pode estar vazia
        
        // Verifica que é uma lista válida
        assertDoesNotThrow(() -> {
            resultado.size(); // Não deve lançar exceção
        });
    }

    @Test
    @DisplayName("Deve buscar atributo com nome nulo")
    void testBuscarComNomeNulo() {
        // Arrange
        String nomeNulo = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            List<Atributo> resultado = atributoDao.buscar(nomeNulo);
            assertNotNull(resultado);
        });
    }

    @Test
    @DisplayName("Deve buscar atributo com nome vazio")
    void testBuscarComNomeVazio() {
        // Arrange
        String nomeVazio = "";

        // Act & Assert
        assertDoesNotThrow(() -> {
            List<Atributo> resultado = atributoDao.buscar(nomeVazio);
            assertNotNull(resultado);
        });
    }

    @Test
    @DisplayName("Deve criar atributo com valores válidos")
    void testCriarAtributoValido() {
        // Arrange
        Atributo atributo = new Atributo();

        // Act
        atributo.setImpostoFederal(12.5f);
        atributo.setImpostoEstadual(6.25f);
        atributo.setImpostoMunicipal(3.125f);

        // Assert
        assertEquals(12.5f, atributo.getImpostoFederal());
        assertEquals(6.25f, atributo.getImpostoEstadual());
        assertEquals(3.125f, atributo.getImpostoMunicipal());
    }

    @Test
    @DisplayName("Deve validar valores padrão do atributo")
    void testValoresPadraoAtributo() {
        // Arrange
        Atributo atributo = new Atributo();

        // Act & Assert
        assertEquals(0.0f, atributo.getImpostoFederal());
        assertEquals(0.0f, atributo.getImpostoEstadual());
        assertEquals(0.0f, atributo.getImpostoMunicipal());
    }

    @Test
    @DisplayName("Deve atualizar imposto federal")
    void testAtualizarImpostoFederal() {
        // Arrange
        Atributo atributo = new Atributo();
        atributo.setImpostoFederal(10.0f);

        // Act
        atributo.setImpostoFederal(20.0f);

        // Assert
        assertEquals(20.0f, atributo.getImpostoFederal());
    }

    @Test
    @DisplayName("Deve atualizar imposto estadual")
    void testAtualizarImpostoEstadual() {
        // Arrange
        Atributo atributo = new Atributo();
        atributo.setImpostoEstadual(5.0f);

        // Act
        atributo.setImpostoEstadual(15.0f);

        // Assert
        assertEquals(15.0f, atributo.getImpostoEstadual());
    }

    @Test
    @DisplayName("Deve atualizar imposto municipal")
    void testAtualizarImpostoMunicipal() {
        // Arrange
        Atributo atributo = new Atributo();
        atributo.setImpostoMunicipal(2.5f);

        // Act
        atributo.setImpostoMunicipal(7.5f);

        // Assert
        assertEquals(7.5f, atributo.getImpostoMunicipal());
    }

    @Test
    @DisplayName("Deve criar múltiplas instâncias do DAO")
    void testCriarMultiplasInstancias() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            AtributoDao dao1 = new AtributoDao();
            AtributoDao dao2 = new AtributoDao();
            AtributoDao dao3 = new AtributoDao();
            
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
            atributoDao.salvar(atributoTeste);
            List<Atributo> lista = atributoDao.listar();
            atributoDao.update(atributoTeste);
            List<Atributo> resultado = atributoDao.buscar("Teste");
            atributoDao.remove("Teste");
            
            assertNotNull(lista);
            assertNotNull(resultado);
        });
    }

    @Test
    @DisplayName("Deve testar diferentes valores de imposto federal")
    void testDiferentesValoresImpostoFederal() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Atributo atributo1 = new Atributo();
            atributo1.setImpostoFederal(5.0f);
            assertEquals(5.0f, atributo1.getImpostoFederal());

            Atributo atributo2 = new Atributo();
            atributo2.setImpostoFederal(15.0f);
            assertEquals(15.0f, atributo2.getImpostoFederal());

            Atributo atributo3 = new Atributo();
            atributo3.setImpostoFederal(25.0f);
            assertEquals(25.0f, atributo3.getImpostoFederal());
        });
    }

    @Test
    @DisplayName("Deve testar diferentes valores de imposto estadual")
    void testDiferentesValoresImpostoEstadual() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Atributo atributo1 = new Atributo();
            atributo1.setImpostoEstadual(2.5f);
            assertEquals(2.5f, atributo1.getImpostoEstadual());

            Atributo atributo2 = new Atributo();
            atributo2.setImpostoEstadual(7.5f);
            assertEquals(7.5f, atributo2.getImpostoEstadual());

            Atributo atributo3 = new Atributo();
            atributo3.setImpostoEstadual(12.5f);
            assertEquals(12.5f, atributo3.getImpostoEstadual());
        });
    }

    @Test
    @DisplayName("Deve testar diferentes valores de imposto municipal")
    void testDiferentesValoresImpostoMunicipal() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Atributo atributo1 = new Atributo();
            atributo1.setImpostoMunicipal(1.25f);
            assertEquals(1.25f, atributo1.getImpostoMunicipal());

            Atributo atributo2 = new Atributo();
            atributo2.setImpostoMunicipal(3.75f);
            assertEquals(3.75f, atributo2.getImpostoMunicipal());

            Atributo atributo3 = new Atributo();
            atributo3.setImpostoMunicipal(6.25f);
            assertEquals(6.25f, atributo3.getImpostoMunicipal());
        });
    }

    @Test
    @DisplayName("Deve testar atributo com valores decimais")
    void testAtributoComValoresDecimais() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Atributo atributo = new Atributo();
            atributo.setImpostoFederal(12.345f);
            atributo.setImpostoEstadual(6.789f);
            atributo.setImpostoMunicipal(3.141f);
            
            assertEquals(12.345f, atributo.getImpostoFederal());
            assertEquals(6.789f, atributo.getImpostoEstadual());
            assertEquals(3.141f, atributo.getImpostoMunicipal());
        });
    }

    @Test
    @DisplayName("Deve testar sequência de operações")
    void testSequenciaOperacoes() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 5; i++) {
                Atributo atributo = new Atributo();
                atributo.setImpostoFederal(10.0f + i);
                atributo.setImpostoEstadual(5.0f + i);
                atributo.setImpostoMunicipal(2.5f + i);
                
                atributoDao.salvar(atributo);
                
                List<Atributo> lista = atributoDao.listar();
                assertNotNull(lista);
            }
        });
    }

    @Test
    @DisplayName("Deve testar diferentes cenários de busca")
    void testDiferentesCenariosBusca() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            List<Atributo> resultado1 = atributoDao.buscar("Atributo Inexistente");
            List<Atributo> resultado2 = atributoDao.buscar("");
            List<Atributo> resultado3 = atributoDao.buscar(" ");
            List<Atributo> resultado4 = atributoDao.buscar("Imposto");
            List<Atributo> resultado5 = atributoDao.buscar("Federal");
            
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
            atributoDao.salvar(atributoTeste);
            
            // Operação 2
            List<Atributo> lista1 = atributoDao.listar();
            assertNotNull(lista1);
            
            // Operação 3
            atributoDao.update(atributoTeste);
            
            // Operação 4
            List<Atributo> resultado = atributoDao.buscar("Teste");
            assertNotNull(resultado);
            
            // Operação 5
            atributoDao.remove("Teste");
            
            // Operação final
            List<Atributo> listaFinal = atributoDao.listar();
            assertNotNull(listaFinal);
        });
    }

    @Test
    @DisplayName("Deve testar atributo com valores extremos")
    void testAtributoComValoresExtremos() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Atributo atributoMinimo = new Atributo();
            atributoMinimo.setImpostoFederal(Float.MIN_VALUE);
            atributoMinimo.setImpostoEstadual(Float.MIN_VALUE);
            atributoMinimo.setImpostoMunicipal(Float.MIN_VALUE);
            assertEquals(Float.MIN_VALUE, atributoMinimo.getImpostoFederal());

            Atributo atributoMaximo = new Atributo();
            atributoMaximo.setImpostoFederal(Float.MAX_VALUE);
            atributoMaximo.setImpostoEstadual(Float.MAX_VALUE);
            atributoMaximo.setImpostoMunicipal(Float.MAX_VALUE);
            assertEquals(Float.MAX_VALUE, atributoMaximo.getImpostoFederal());
        });
    }

    @Test
    @DisplayName("Deve testar persistência do DAO")
    void testPersistenciaDao() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            // Operações iniciais
            atributoDao.salvar(atributoTeste);
            atributoDao.listar();
            
            // Verifica que DAO ainda está funcional
            atributoDao.buscar("Teste");
            atributoDao.update(atributoTeste);
            atributoDao.remove("Teste");
            
            // Mais operações
            atributoDao.salvar(atributoTeste);
            atributoDao.listar();
        });
    }

    @Test
    @DisplayName("Deve testar atributo com valores especiais")
    void testAtributoComValoresEspeciais() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Atributo atributo1 = new Atributo();
            atributo1.setImpostoFederal(Float.NaN);
            atributo1.setImpostoEstadual(Float.POSITIVE_INFINITY);
            atributo1.setImpostoMunicipal(Float.NEGATIVE_INFINITY);
            
            assertTrue(Float.isNaN(atributo1.getImpostoFederal()));
            assertTrue(Float.isInfinite(atributo1.getImpostoEstadual()));
            assertTrue(Float.isInfinite(atributo1.getImpostoMunicipal()));
        });
    }

    @Test
    @DisplayName("Deve testar diferentes combinações de impostos")
    void testDiferentesCombinacoesImpostos() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Atributo atributo1 = new Atributo();
            atributo1.setImpostoFederal(10.0f);
            atributo1.setImpostoEstadual(5.0f);
            atributo1.setImpostoMunicipal(2.5f);
            assertEquals(10.0f, atributo1.getImpostoFederal());

            Atributo atributo2 = new Atributo();
            atributo2.setImpostoFederal(15.0f);
            atributo2.setImpostoEstadual(7.5f);
            atributo2.setImpostoMunicipal(3.75f);
            assertEquals(15.0f, atributo2.getImpostoFederal());

            Atributo atributo3 = new Atributo();
            atributo3.setImpostoFederal(20.0f);
            atributo3.setImpostoEstadual(10.0f);
            atributo3.setImpostoMunicipal(5.0f);
            assertEquals(20.0f, atributo3.getImpostoFederal());
        });
    }
}
