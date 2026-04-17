package com.br.hermescomercial.controller.pdv;

import com.br.hermescomercial.dao.UsuarioDao;
import com.br.hermescomercial.model.Usuario;
import com.br.hermescomercial.model.Cliente;
import com.br.hermescomercial.business.pdv.PDVManager;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
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

public class PDVPrincipalControllerTest {

    private PDVPrincipalController controller;
    
    @Mock
    private TextField txtBuscarClienteNome;
    
    @Mock
    private TextField txtBuscarClienteCpfCnpj;
    
    @Mock
    private Button btnBuscarClienteNome;
    
    @Mock
    private Button btnBuscarClienteCpfCnpj;
    
    @Mock
    private Label lblClienteSelecionado;
    
    @Mock
    private UsuarioDao usuarioDao;
    
    @Mock
    private PDVManager pdvManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new PDVPrincipalController();
        
        // Injetar mocks (simulando injeção do FXML)
        try {
            java.lang.reflect.Field field = PDVPrincipalController.class.getDeclaredField("txtBuscarClienteNome");
            field.setAccessible(true);
            field.set(controller, txtBuscarClienteNome);
            
            field = PDVPrincipalController.class.getDeclaredField("txtBuscarClienteCpfCnpj");
            field.setAccessible(true);
            field.set(controller, txtBuscarClienteCpfCnpj);
            
            field = PDVPrincipalController.class.getDeclaredField("btnBuscarClienteNome");
            field.setAccessible(true);
            field.set(controller, btnBuscarClienteNome);
            
            field = PDVPrincipalController.class.getDeclaredField("btnBuscarClienteCpfCnpj");
            field.setAccessible(true);
            field.set(controller, btnBuscarClienteCpfCnpj);
            
            field = PDVPrincipalController.class.getDeclaredField("lblClienteSelecionado");
            field.setAccessible(true);
            field.set(controller, lblClienteSelecionado);
            
            field = PDVPrincipalController.class.getDeclaredField("usuarioDao");
            field.setAccessible(true);
            field.set(controller, usuarioDao);
            
            field = PDVPrincipalController.class.getDeclaredField("pdvManager");
            field.setAccessible(true);
            field.set(controller, pdvManager);
            
        } catch (Exception e) {
            fail("Falha ao injetar mocks: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Deve buscar cliente por nome com 2 caracteres")
    void testBuscarClientePorNome_DoisCaracteres() throws Exception {
        // Configurar mocks
        when(txtBuscarClienteNome.getText()).thenReturn("Jo");
        
        List<Usuario> clientesMock = new ArrayList<>();
        Usuario clienteMock = new Usuario();
        clienteMock.setId(1L);
        clienteMock.setNome("João Silva");
        clienteMock.setNumeroDocumeto("12345678900");
        clientesMock.add(clienteMock);
        
        when(usuarioDao.buscarClientePorNomeCpfCnpj("Jo")).thenReturn(clientesMock);
        
        // Executar método privado usando reflection
        java.lang.reflect.Method method = PDVPrincipalController.class.getDeclaredMethod("buscarClientePorNome", String.class);
        method.setAccessible(true);
        method.invoke(controller, "Jo");
        
        // Verificar interações
        verify(usuarioDao).buscarClientePorNomeCpfCnpj("Jo");
        verify(txtBuscarClienteNome).getText();
    }

    @Test
    @DisplayName("Deve buscar cliente por nome com 3 caracteres")
    void testBuscarClientePorNome_TresCaracteres() throws Exception {
        when(txtBuscarClienteNome.getText()).thenReturn("Joa");
        
        List<Usuario> clientesMock = new ArrayList<>();
        Usuario clienteMock = new Usuario();
        clienteMock.setId(1L);
        clienteMock.setNome("João Silva");
        clientesMock.add(clienteMock);
        
        when(usuarioDao.buscarClientePorNomeCpfCnpj("Joa")).thenReturn(clientesMock);
        
        java.lang.reflect.Method method = PDVPrincipalController.class.getDeclaredMethod("buscarClientePorNome", String.class);
        method.setAccessible(true);
        method.invoke(controller, "Joa");
        
        verify(usuarioDao).buscarClientePorNomeCpfCnpj("Joa");
    }

    @Test
    @DisplayName("Não deve buscar cliente com menos de 2 caracteres")
    void testBuscarClientePorNome_MenosDeDoisCaracteres() throws Exception {
        when(txtBuscarClienteNome.getText()).thenReturn("J");
        
        java.lang.reflect.Method method = PDVPrincipalController.class.getDeclaredMethod("buscarClientePorNome", String.class);
        method.setAccessible(true);
        method.invoke(controller, "J");
        
        // Não deve chamar o DAO
        verify(usuarioDao, never()).buscarClientePorNomeCpfCnpj(anyString());
    }

    @Test
    @DisplayName("Deve buscar cliente por CPF/CNPJ com 2 caracteres")
    void testBuscarClientePorCpfCnpj_DoisCaracteres() throws Exception {
        when(txtBuscarClienteCpfCnpj.getText()).thenReturn("12");
        
        List<Usuario> clientesMock = new ArrayList<>();
        Usuario clienteMock = new Usuario();
        clienteMock.setId(1L);
        clienteMock.setNome("Cliente Teste");
        clienteMock.setNumeroDocumeto("12345678900");
        clientesMock.add(clienteMock);
        
        when(usuarioDao.buscarClientePorNomeCpfCnpj("12")).thenReturn(clientesMock);
        
        java.lang.reflect.Method method = PDVPrincipalController.class.getDeclaredMethod("buscarClientePorCpfCnpj", String.class);
        method.setAccessible(true);
        method.invoke(controller, "12");
        
        verify(usuarioDao).buscarClientePorNomeCpfCnpj("12");
    }

    @Test
    @DisplayName("Deve tratar busca vazia corretamente")
    void testBuscarClientePorNome_BuscaVazia() throws Exception {
        when(txtBuscarClienteNome.getText()).thenReturn("");
        
        java.lang.reflect.Method method = PDVPrincipalController.class.getDeclaredMethod("buscarClientePorNome", String.class);
        method.setAccessible(true);
        method.invoke(controller, "");
        
        // Não deve chamar o DAO com string vazia
        verify(usuarioDao, never()).buscarClientePorNomeCpfCnpj("");
    }

    @Test
    @DisplayName("Deve tratar busca nula corretamente")
    void testBuscarClientePorNome_BuscaNula() throws Exception {
        when(txtBuscarClienteNome.getText()).thenReturn(null);
        
        java.lang.reflect.Method method = PDVPrincipalController.class.getDeclaredMethod("buscarClientePorNome", String.class);
        method.setAccessible(true);
        method.invoke(controller, (String) null);
        
        // Não deve chamar o DAO com null
        verify(usuarioDao, never()).buscarClientePorNomeCpfCnpj(any());
    }

    @Test
    @DisplayName("Deve atualizar label quando cliente é encontrado")
    void testBuscarClientePorNome_AtualizarLabel() throws Exception {
        when(txtBuscarClienteNome.getText()).thenReturn("Jo");
        
        List<Usuario> clientesMock = new ArrayList<>();
        Usuario clienteMock = new Usuario();
        clienteMock.setId(1L);
        clienteMock.setNome("João Silva");
        clienteMock.setNumeroDocumeto("12345678900");
        clienteMock.setEmail("joao@email.com");
        clienteMock.setTelefone("11999999999");
        clientesMock.add(clienteMock);
        
        when(usuarioDao.buscarClientePorNomeCpfCnpj("Jo")).thenReturn(clientesMock);
        
        java.lang.reflect.Method method = PDVPrincipalController.class.getDeclaredMethod("buscarClientePorNome", String.class);
        method.setAccessible(true);
        method.invoke(controller, "Jo");
        
        // Verificar se o label foi atualizado
        verify(lblClienteSelecionado).setText("João Silva - 12345678900");
    }

    @Test
    @DisplayName("Deve exibir mensagem quando nenhum cliente é encontrado")
    void testBuscarClientePorNome_NenhumClienteEncontrado() throws Exception {
        when(txtBuscarClienteNome.getText()).thenReturn("XYZ");
        
        List<Usuario> clientesVazios = new ArrayList<>();
        when(usuarioDao.buscarClientePorNomeCpfCnpj("XYZ")).thenReturn(clientesVazios);
        
        java.lang.reflect.Method method = PDVPrincipalController.class.getDeclaredMethod("buscarClientePorNome", String.class);
        method.setAccessible(true);
        method.invoke(controller, "XYZ");
        
        // Verificar se o label foi atualizado com mensagem de não encontrado
        verify(lblClienteSelecionado).setText("Nenhum cliente encontrado");
    }
}
