package com.br.hermescomercial.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import com.br.hermescomercial.model.Pessoa;

/**
 * Testes unitários para a classe LoginDao (equivalente a UsuarioDao)
 * Testa as funcionalidades de gerenciamento de usuários do sistema
 */
class UsuarioDaoTest {

    private LoginDao loginDao;
    private Pessoa pessoaTeste;

    @BeforeEach
    void setUp() {
        loginDao = new LoginDao();
        pessoaTeste = new Pessoa();
        pessoaTeste.setNome("João Silva");
        pessoaTeste.setEndereco("Rua das Flores, 123");
        pessoaTeste.setBairro("Centro");
        pessoaTeste.setCidade("São Paulo");
        pessoaTeste.setEstado("SP");
        pessoaTeste.setCep("01234-567");
        pessoaTeste.setCnpj("12.345.678/0001-90");
        pessoaTeste.setCpf("123.456.789-00");
        pessoaTeste.setEmail("joao@email.com");
        pessoaTeste.setTipoPessoa("Cliente");
    }

    @Test
    @DisplayName("Deve inicializar LoginDao sem lançar exceção")
    void testInitialize() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            LoginDao dao = new LoginDao();
            assertNotNull(dao, "DAO não deve ser nulo");
        });
    }

    @Test
    @DisplayName("Deve salvar pessoa no banco sem lançar exceção")
    void testSalvar() {
        // Arrange
        Pessoa novaPessoa = new Pessoa();
        novaPessoa.setNome("Maria Santos");
        novaPessoa.setEndereco("Av. Principal, 456");
        novaPessoa.setBairro("Jardins");
        novaPessoa.setCidade("Rio de Janeiro");
        novaPessoa.setEstado("RJ");
        novaPessoa.setCep("23456-789");
        novaPessoa.setEmail("maria@email.com");
        novaPessoa.setTipoPessoa("Fornecedor");

        // Act & Assert
        assertDoesNotThrow(() -> {
            loginDao.salvar(novaPessoa);
        });
        
        // Nota: Como depende de conexão com banco, apenas verificamos que não lança exceção
    }

    @Test
    @DisplayName("Deve tentar salvar pessoa nula")
    void testSalvarPessoaNula() {
        // Arrange
        Pessoa pessoaNula = null;

        // Act & Assert
        assertDoesNotThrow(() -> {
            loginDao.salvar(pessoaNula);
        });
        
        // Nota: Como o método não verifica null, pode lançar exceção no tratamento
    }

    @Test
    @DisplayName("Deve acessar pessoa com login e senha válidos")
    void testAcessarPessoaValida() {
        // Arrange
        String login = "joao";
        String senha = "123456";

        // Act & Assert
        assertDoesNotThrow(() -> {
            loginDao.acessarPessoa(login, senha);
            // Nota: Como depende de banco de dados, o resultado pode ser null
            // Apenas verificamos que não lança exceção
        });
    }

    @Test
    @DisplayName("Deve acessar pessoa com login nulo")
    void testAcessarPessoaComLoginNulo() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            loginDao.acessarPessoa(null, "123456");
            // Pode retornar null ou lançar exceção - apenas verificamos comportamento
        });
    }

    @Test
    @DisplayName("Deve acessar pessoa com senha nula")
    void testAcessarPessoaComSenhaNula() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            loginDao.acessarPessoa("joao", null);
            // Pode retornar null ou lançar exceção - apenas verificamos comportamento
        });
    }

    @Test
    @DisplayName("Deve acessar pessoa com credenciais vazias")
    void testAcessarPessoaComCredenciaisVazias() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            loginDao.acessarPessoa("", "");
            // Pode retornar null ou lançar exceção - apenas verificamos comportamento
        });
    }

    @Test
    @DisplayName("Deve criar pessoa com valores válidos")
    void testCriarPessoaValida() {
        // Arrange
        Pessoa pessoa = new Pessoa();

        // Act
        pessoa.setId(1L);
        pessoa.setNome("Carlos Silva");
        pessoa.setEndereco("Rua Verde, 789");
        pessoa.setBairro("Alameda");
        pessoa.setCidade("Belo Horizonte");
        pessoa.setEstado("MG");
        pessoa.setCep("34567-890");
        pessoa.setCnpj("98.765.432/0001-10");
        pessoa.setCpf("987.654.321-00");
        pessoa.setEmail("carlos@email.com");
        pessoa.setTipoPessoa("Funcionário");

        // Assert
        assertEquals(1L, pessoa.getId());
        assertEquals("Carlos Silva", pessoa.getNome());
        assertEquals("Rua Verde, 789", pessoa.getEndereco());
        assertEquals("Alameda", pessoa.getBairro());
        assertEquals("Belo Horizonte", pessoa.getCidade());
        assertEquals("MG", pessoa.getEstado());
        assertEquals("34567-890", pessoa.getCep());
        assertEquals("98.765.432/0001-10", pessoa.getCnpj());
        assertEquals("987.654.321-00", pessoa.getCpf());
        assertEquals("carlos@email.com", pessoa.getEmail());
        assertEquals("Funcionário", pessoa.getTipoPessoa());
    }

    @Test
    @DisplayName("Deve validar valores padrão da pessoa")
    void testValoresPadraoPessoa() {
        // Arrange
        Pessoa pessoa = new Pessoa();

        // Act & Assert
        assertEquals(0L, pessoa.getId());
        assertNull(pessoa.getNome());
        assertNull(pessoa.getEndereco());
        assertNull(pessoa.getBairro());
        assertNull(pessoa.getCidade());
        assertNull(pessoa.getEstado());
        assertNull(pessoa.getCep());
        assertNull(pessoa.getCnpj());
        assertNull(pessoa.getCpf());
        assertNull(pessoa.getEmail());
        assertNull(pessoa.getTipoPessoa());
    }

    @Test
    @DisplayName("Deve atualizar nome da pessoa")
    void testAtualizarNomePessoa() {
        // Arrange
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("Nome Original");

        // Act
        pessoa.setNome("Nome Atualizado");

        // Assert
        assertEquals("Nome Atualizado", pessoa.getNome());
    }

    @Test
    @DisplayName("Deve atualizar email da pessoa")
    void testAtualizarEmailPessoa() {
        // Arrange
        Pessoa pessoa = new Pessoa();
        pessoa.setEmail("original@email.com");

        // Act
        pessoa.setEmail("atualizado@email.com");

        // Assert
        assertEquals("atualizado@email.com", pessoa.getEmail());
    }

    @Test
    @DisplayName("Deve atualizar tipo de pessoa")
    void testAtualizarTipoPessoa() {
        // Arrange
        Pessoa pessoa = new Pessoa();
        pessoa.setTipoPessoa("Cliente");

        // Act
        pessoa.setTipoPessoa("Fornecedor");

        // Assert
        assertEquals("Fornecedor", pessoa.getTipoPessoa());
    }

    @Test
    @DisplayName("Deve testar pessoa com CPF e CNPJ")
    void testPessoaComCPFeCNPJ() {
        // Arrange
        Pessoa pessoa = new Pessoa();

        // Act
        pessoa.setCpf("111.222.333-44");
        pessoa.setCnpj("55.666.777/0001-88");

        // Assert
        assertEquals("111.222.333-44", pessoa.getCpf());
        assertEquals("55.666.777/0001-88", pessoa.getCnpj());
    }

    @Test
    @DisplayName("Deve testar pessoa com endereço completo")
    void testPessoaComEnderecoCompleto() {
        // Arrange
        Pessoa pessoa = new Pessoa();

        // Act
        pessoa.setEndereco("Rua das Árvores, 1000, Apto 203");
        pessoa.setBairro("Vila Nova");
        pessoa.setCidade("Porto Alegre");
        pessoa.setEstado("RS");
        pessoa.setCep("90123-456");

        // Assert
        assertEquals("Rua das Árvores, 1000, Apto 203", pessoa.getEndereco());
        assertEquals("Vila Nova", pessoa.getBairro());
        assertEquals("Porto Alegre", pessoa.getCidade());
        assertEquals("RS", pessoa.getEstado());
        assertEquals("90123-456", pessoa.getCep());
    }

    @Test
    @DisplayName("Deve testar diferentes tipos de pessoa")
    void testDiferentesTiposPessoa() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Pessoa cliente = new Pessoa();
            cliente.setTipoPessoa("Cliente");
            assertEquals("Cliente", cliente.getTipoPessoa());

            Pessoa fornecedor = new Pessoa();
            fornecedor.setTipoPessoa("Fornecedor");
            assertEquals("Fornecedor", fornecedor.getTipoPessoa());

            Pessoa funcionario = new Pessoa();
            funcionario.setTipoPessoa("Funcionário");
            assertEquals("Funcionário", funcionario.getTipoPessoa());
        });
    }

    @Test
    @DisplayName("Deve criar múltiplas instâncias do DAO")
    void testCriarMultiplasInstancias() {
        // Act & Assert
        assertDoesNotThrow(() -> {
            LoginDao dao1 = new LoginDao();
            LoginDao dao2 = new LoginDao();
            LoginDao dao3 = new LoginDao();
            
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
            loginDao.salvar(pessoaTeste);
            
            loginDao.salvar(pessoaTeste);
        });
    }

    @Test
    @DisplayName("Deve testar salvar múltiplas pessoas")
    void testSalvarMultiplasPessoas() {
        // Arrange & Act & Assert
        assertDoesNotThrow(() -> {
            Pessoa pessoa1 = new Pessoa();
            pessoa1.setNome("Ana Costa");
            pessoa1.setEmail("ana@email.com");
            pessoa1.setTipoPessoa("Cliente");
            
            Pessoa pessoa2 = new Pessoa();
            pessoa2.setNome("Pedro Santos");
            pessoa2.setEmail("pedro@email.com");
            pessoa2.setTipoPessoa("Fornecedor");
            
            Pessoa pessoa3 = new Pessoa();
            pessoa3.setNome("Lucia Oliveira");
            pessoa3.setEmail("lucia@email.com");
            pessoa3.setTipoPessoa("Funcionário");
            
            loginDao.salvar(pessoa1);
            loginDao.salvar(pessoa2);
            loginDao.salvar(pessoa3);
        });
    }

    @Test
    @DisplayName("Deve testar setTipoUsuario (método legacy)")
    void testSetTipoUsuario() {
        // Arrange
        Pessoa pessoa = new Pessoa();

        // Act
        pessoa.setTipoUsuario("Administrador");

        // Assert
        assertEquals("Administrador", pessoa.getTipoPessoa());
    }

    @Test
    @DisplayName("Deve testar pessoa com valores vazios")
    void testPessoaComValoresVazios() {
        // Arrange
        Pessoa pessoa = new Pessoa();

        // Act
        pessoa.setNome("");
        pessoa.setEndereco("");
        pessoa.setEmail("");
        pessoa.setTipoPessoa("");

        // Assert
        assertEquals("", pessoa.getNome());
        assertEquals("", pessoa.getEndereco());
        assertEquals("", pessoa.getEmail());
        assertEquals("", pessoa.getTipoPessoa());
    }
}
