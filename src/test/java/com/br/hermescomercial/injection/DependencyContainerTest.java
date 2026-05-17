package com.br.hermescomercial.injection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import com.br.hermescomercial.repository.ClienteRepository;
import com.br.hermescomercial.repository.ProdutoRepository;
import com.br.hermescomercial.repository.UsuarioRepository;

/**
 * Testes para DependencyContainer
 * Verifica funcionamento do container de injeção de dependências
 */
public class DependencyContainerTest {
    
    private DependencyContainer container;
    
    @BeforeEach
    void setUp() {
        container = DependencyContainer.getInstance();
    }
    
    @AfterEach
    void tearDown() {
        container.clear();
    }
    
    @Test
    @DisplayName("Deve obter instância singleton do container")
    void testGetInstance() {
        DependencyContainer instance1 = DependencyContainer.getInstance();
        DependencyContainer instance2 = DependencyContainer.getInstance();
        
        assertSame(instance1, instance2, "Deve retornar a mesma instância");
    }
    
    @Test
    @DisplayName("Deve registrar dependência singleton")
    void testRegisterSingleton() {
        assertDoesNotThrow(() -> {
            container.registerSingleton(TestInterface.class, TestImplementation.class);
        }, "Deve registrar dependência singleton sem exceções");
    }
    
    @Test
    @DisplayName("Deve registrar dependência transient")
    void testRegisterTransient() {
        assertDoesNotThrow(() -> {
            container.registerTransient(TestInterface.class, TestImplementation.class);
        }, "Deve registrar dependência transient sem exceções");
    }
    
    @Test
    @DisplayName("Deve resolver dependência registrada")
    void testResolveDependency() {
        container.registerSingleton(TestInterface.class, TestImplementation.class);
        
        TestInterface dependency = container.get(TestInterface.class);
        
        assertNotNull(dependency, "Dependência deve ser resolvida");
        assertTrue(dependency instanceof TestImplementation, "Deve ser instância correta");
    }
    
    @Test
    @DisplayName("Deve lançar exceção quando dependência não registrada")
    void testUnregisteredDependency() {
        assertThrows(DependencyContainer.DependencyNotFoundException.class, () -> {
            container.get(TestInterface.class);
        }, "Deve lançar DependencyNotFoundException");
    }
    
    @Test
    @DisplayName("Deve verificar se dependência está registrada")
    void testIsRegistered() {
        assertFalse(container.isRegistered(TestInterface.class), "Não deve estar registrado inicialmente");
        
        container.registerSingleton(TestInterface.class, TestImplementation.class);
        
        assertTrue(container.isRegistered(TestInterface.class), "Deve estar registrado após registro");
    }
    
    @Test
    @DisplayName("Deve retornar Optional vazio para dependência não registrada")
    void testGetOptionalUnregistered() {
        java.util.Optional<TestInterface> optional = container.getOptional(TestInterface.class);
        
        assertFalse(optional.isPresent(), "Optional deve estar vazio");
    }
    
    @Test
    @DisplayName("Deve retornar Optional com valor para dependência registrada")
    void testGetOptionalRegistered() {
        container.registerSingleton(TestInterface.class, TestImplementation.class);
        
        java.util.Optional<TestInterface> optional = container.getOptional(TestInterface.class);
        
        assertTrue(optional.isPresent(), "Optional deve ter valor");
    }
    
    @Test
    @DisplayName("Deve limpar todas as dependências")
    void testClear() {
        container.registerSingleton(TestInterface.class, TestImplementation.class);
        assertTrue(container.isRegistered(TestInterface.class), "Deve estar registrado");
        
        container.clear();
        
        assertFalse(container.isRegistered(TestInterface.class), "Não deve estar registrado após clear");
    }
    
    @Test
    @DisplayName("Deve listar todas as dependências registradas")
    void testGetRegisteredDependencies() {
        container.registerSingleton(TestInterface.class, TestImplementation.class);
        container.registerSingleton(AnotherInterface.class, AnotherImplementation.class);
        
        java.util.Set<Class<?>> dependencies = container.getRegisteredDependencies();
        
        assertEquals(2, dependencies.size(), "Deve ter 2 dependências registradas");
        assertTrue(dependencies.contains(TestInterface.class), "Deve conter TestInterface");
        assertTrue(dependencies.contains(AnotherInterface.class), "Deve conter AnotherInterface");
    }
    
    @Test
    @DisplayName("Deve resolver repositories core configurados")
    void testResolveCoreRepositories() {
        assertDoesNotThrow(() -> {
            ClienteRepository clienteRepo = container.get(ClienteRepository.class);
            assertNotNull(clienteRepo, "ClienteRepository deve ser resolvido");
            
            ProdutoRepository produtoRepo = container.get(ProdutoRepository.class);
            assertNotNull(produtoRepo, "ProdutoRepository deve ser resolvido");
            
            UsuarioRepository usuarioRepo = container.get(UsuarioRepository.class);
            assertNotNull(usuarioRepo, "UsuarioRepository deve ser resolvido");
        }, "Repositories core devem ser resolvidos");
    }
    
    @Test
    @DisplayName("Deve lançar DependencyCreationException ao falhar criação")
    void testDependencyCreationException() {
        container.registerSingleton(TestInterface.class, InvalidImplementation.class);
        
        assertThrows(DependencyContainer.DependencyCreationException.class, () -> {
            container.get(TestInterface.class);
        }, "Deve lançar DependencyCreationException");
    }
    
    // Interfaces e classes de teste
    interface TestInterface {
        String test();
    }
    
    static class TestImplementation implements TestInterface {
        @Override
        public String test() {
            return "test";
        }
    }
    
    interface AnotherInterface {
        void another();
    }
    
    static class AnotherImplementation implements AnotherInterface {
        @Override
        public void another() {
            // Implementação vazia
        }
    }
    
    static class InvalidImplementation implements TestInterface {
        public InvalidImplementation(String required) {
            // Construtor que requer parâmetro - vai falhar
        }
        
        @Override
        public String test() {
            return "invalid";
        }
    }
}
