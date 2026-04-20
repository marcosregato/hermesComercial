package com.br.hermescomercial.controller.pdv;

import com.br.hermescomercial.dao.UsuarioDao;
import com.br.hermescomercial.model.Usuario;
import com.br.hermescomercial.model.Cliente;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PDVResultadoBuscaClientesControllerTest {

    private PDVResultadoBuscaClientesController controller;
    
    @Mock
    private Label lblTermoBusca;
    
    @Mock
    private Label lblQuantidadeResultados;
    
    @Mock
    private TableView<Usuario> tblResultados;
    
    @Mock
    private Label lblDetalheId;
    
    @Mock
    private Label lblDetalheNome;
    
    @Mock
    private Label lblDetalheDocumento;
    
    @Mock
    private Label lblDetalheTelefone;
    
    @Mock
    private Label lblDetalheEmail;
    
    @Mock
    private Label lblDetalheEndereco;
    
    @Mock
    private Button btnSelecionar;
    
    @Mock
    private Button btnNovaBusca;
    
    @Mock
    private Button btnFechar;
    
    @Mock
    private UsuarioDao usuarioDao;
    
    private Cliente clienteSelecionado;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new PDVResultadoBuscaClientesController();
        
        // Injetar mocks
        injectMock("lblTermoBusca", lblTermoBusca);
        injectMock("lblQuantidadeResultados", lblQuantidadeResultados);
        injectMock("tblResultados", tblResultados);
        injectMock("lblDetalheId", lblDetalheId);
        injectMock("lblDetalheNome", lblDetalheNome);
        injectMock("lblDetalheDocumento", lblDetalheDocumento);
        injectMock("lblDetalheTelefone", lblDetalheTelefone);
        injectMock("lblDetalheEmail", lblDetalheEmail);
        injectMock("lblDetalheEndereco", lblDetalheEndereco);
        injectMock("btnSelecionar", btnSelecionar);
        injectMock("btnNovaBusca", btnNovaBusca);
        injectMock("btnFechar", btnFechar);
        injectMock("usuarioDao", usuarioDao);
        
        // Configurar callback
        controller.setCallback(cliente -> {
            clienteSelecionado = cliente;
        });
    }

    private void injectMock(String fieldName, Object mock) {
        try {
            java.lang.reflect.Field field = PDVResultadoBuscaClientesController.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(controller, mock);
        } catch (Exception e) {
            fail("Falha ao injetar mock " + fieldName + ": " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Deve carregar resultados de busca por nome")
    void testCarregarResultados_BuscaPorNome() {
        // Configurar mocks
        List<Usuario> clientesMock = criarListaClientesMock();
        when(usuarioDao.buscarClientePorNomeCpfCnpj("João")).thenReturn(clientesMock);
        
        // Executar
        controller.carregarResultados("João", "nome");
        
        // Verificar
        verify(usuarioDao).buscarClientePorNomeCpfCnpj("João");
        verify(lblTermoBusca).setText("Termo da busca: João (por nome)");
        verify(lblQuantidadeResultados).setText("Resultados encontrados: 2");
    }

    @Test
    @DisplayName("Deve carregar resultados de busca por CPF/CNPJ")
    void testCarregarResultados_BuscaPorCpfCnpj() {
        List<Usuario> clientesMock = criarListaClientesMock();
        when(usuarioDao.buscarClientePorNomeCpfCnpj("123")).thenReturn(clientesMock);
        
        controller.carregarResultados("123", "cpf/cnpj");
        
        verify(usuarioDao).buscarClientePorNomeCpfCnpj("123");
        verify(lblTermoBusca).setText("Termo da busca: 123 (por cpf/cnpj)");
        verify(lblQuantidadeResultados).setText("Resultados encontrados: 2");
    }

    @Test
    @DisplayName("Deve tratar busca sem resultados")
    void testCarregarResultados_SemResultados() {
        List<Usuario> clientesVazios = new ArrayList<>();
        when(usuarioDao.buscarClientePorNomeCpfCnpj("XYZ")).thenReturn(clientesVazios);
        
        controller.carregarResultados("XYZ", "nome");
        
        verify(usuarioDao).buscarClientePorNomeCpfCnpj("XYZ");
        verify(lblQuantidadeResultados).setText("Resultados encontrados: 0");
    }

    @Test
    @DisplayName("Deve atualizar detalhes do cliente selecionado")
    void testAtualizarDetalhes() throws Exception {
        Usuario clienteMock = criarClienteMock();
        
        java.lang.reflect.Method method = PDVResultadoBuscaClientesController.class.getDeclaredMethod("atualizarDetalhes", Usuario.class);
        method.setAccessible(true);
        method.invoke(controller, clienteMock);
        
        verify(lblDetalheId).setText("1");
        verify(lblDetalheNome).setText("João Silva");
        verify(lblDetalheDocumento).setText("12345678900");
        verify(lblDetalheTelefone).setText("11999999999");
        verify(lblDetalheEmail).setText("joao@email.com");
    }

    @Test
    @DisplayName("Deve limpar detalhes quando não há cliente")
    void testLimparDetalhes() throws Exception {
        java.lang.reflect.Method method = PDVResultadoBuscaClientesController.class.getDeclaredMethod("limparDetalhes");
        method.setAccessible(true);
        method.invoke(controller);
        
        verify(lblDetalheId).setText("-");
        verify(lblDetalheNome).setText("-");
        verify(lblDetalheDocumento).setText("-");
        verify(lblDetalheTelefone).setText("-");
        verify(lblDetalheEmail).setText("-");
        verify(lblDetalheEndereco).setText("-");
    }

    @Test
    @DisplayName("Deve selecionar cliente via callback")
    void testSelecionarCliente() throws Exception {
        Usuario clienteMock = criarClienteMock();
        
        java.lang.reflect.Method method = PDVResultadoBuscaClientesController.class.getDeclaredMethod("selecionarCliente", Usuario.class);
        method.setAccessible(true);
        method.invoke(controller, clienteMock);
        
        assertNotNull(clienteSelecionado);
        assertEquals("João Silva", clienteSelecionado.getNome());
        assertEquals("12345678900", clienteSelecionado.getCpf());
        assertEquals("joao@email.com", clienteSelecionado.getEmail());
        assertEquals("11999999999", clienteSelecionado.getTelefone());
    }

    @Test
    @DisplayName("Deve tratar cliente com dados nulos nos detalhes")
    void testAtualizarDetalhes_ClienteComDadosNulos() throws Exception {
        Usuario clienteMock = new Usuario();
        clienteMock.setId(1L);
        clienteMock.setNome(null);
        clienteMock.setNumeroDocumeto(null);
        clienteMock.setTelefone(null);
        clienteMock.setEmail(null);
        
        java.lang.reflect.Method method = PDVResultadoBuscaClientesController.class.getDeclaredMethod("atualizarDetalhes", Usuario.class);
        method.setAccessible(true);
        method.invoke(controller, clienteMock);
        
        verify(lblDetalheId).setText("1");
        verify(lblDetalheNome).setText("-");
        verify(lblDetalheDocumento).setText("-");
        verify(lblDetalheTelefone).setText("-");
        verify(lblDetalheEmail).setText("-");
    }

    @Test
    @DisplayName("Deve montar endereço completo")
    void testAtualizarDetalhes_EnderecoCompleto() throws Exception {
        Usuario clienteMock = criarClienteMock();
        clienteMock.setEndereco("Rua das Flores");
        clienteMock.setBairro("Centro");
        clienteMock.setCidade("São Paulo");
        clienteMock.setEstado("SP");
        clienteMock.setCep("01234567");
        
        java.lang.reflect.Method method = PDVResultadoBuscaClientesController.class.getDeclaredMethod("atualizarDetalhes", Usuario.class);
        method.setAccessible(true);
        method.invoke(controller, clienteMock);
        
        verify(lblDetalheEndereco).setText("Rua das Flores - Centro, São Paulo - SP CEP: 01234567");
    }

    private List<Usuario> criarListaClientesMock() {
        List<Usuario> clientes = new ArrayList<>();
        
        Usuario cliente1 = criarClienteMock();
        clientes.add(cliente1);
        
        Usuario cliente2 = new Usuario();
        cliente2.setId(2L);
        cliente2.setNome("Maria Santos");
        cliente2.setNumeroDocumeto("98765432100");
        clientes.add(cliente2);
        
        return clientes;
    }

    private Usuario criarClienteMock() {
        Usuario cliente = new Usuario();
        cliente.setId(1L);
        cliente.setNome("João Silva");
        cliente.setNumeroDocumeto("12345678900");
        cliente.setEmail("joao@email.com");
        cliente.setTelefone("11999999999");
        cliente.setEndereco("Rua das Flores");
        cliente.setBairro("Centro");
        cliente.setCidade("São Paulo");
        cliente.setEstado("SP");
        cliente.setCep("01234567");
        return cliente;
    }
}
