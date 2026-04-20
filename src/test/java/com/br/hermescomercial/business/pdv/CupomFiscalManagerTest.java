package com.br.hermescomercial.business.pdv;

import com.br.hermescomercial.model.Cliente;
import com.br.hermescomercial.model.ItemVenda;
import com.br.hermescomercial.model.Pagamento;
import com.br.hermescomercial.model.Usuario;
import com.br.hermescomercial.model.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CupomFiscalManagerTest {

    private CupomFiscalManager cupomManager;
    private List<ItemVenda> itensVenda;
    private Cliente cliente;
    private Usuario operador;
    private Pagamento pagamento;

    @BeforeEach
    void setUp() {
        cupomManager = CupomFiscalManager.getInstance();
        itensVenda = criarItensVendaMock();
        cliente = criarClienteMock();
        operador = criarOperadorMock();
        pagamento = criarPagamentoMock();
    }

    @Nested
    @DisplayName("Testes de Geração de Cupom Fiscal")
    class GeracaoCupomFiscal {

        @Test
        @DisplayName("Deve gerar cupom fiscal completo com todos os dados")
        void testGerarCupomFiscal_ComDadosCompletos() {
            // Act
            String cupom = cupomManager.gerarCupomFiscal(itensVenda, pagamento, cliente, operador, "000001");

            // Assert
            assertNotNull(cupom);
            assertTrue(cupom.contains("CUPOM FISCAL ELETRÔNICO"));
            assertTrue(cupom.contains("Nº: 000001"));
            assertTrue(cupom.contains("Hermes Comercial Ltda"));
            assertTrue(cupom.contains("12.345.678/0001-95"));
            assertTrue(cupom.contains("João Silva"));
            assertTrue(cupom.contains("123.456.789-00"));
            assertTrue(cupom.contains("Produto Teste 1"));
            assertTrue(cupom.contains("Produto Teste 2"));
            assertTrue(cupom.contains("DINHEIRO"));
            assertTrue(cupom.contains("Operador: João Silva"));
        }

        @Test
        @DisplayName("Deve gerar cupom fiscal sem cliente")
        void testGerarCupomFiscal_SemCliente() {
            // Act
            String cupom = cupomManager.gerarCupomFiscal(itensVenda, pagamento, null, operador, "000002");

            // Assert
            assertNotNull(cupom);
            assertTrue(cupom.contains("CUPOM FISCAL ELETRÔNICO"));
            assertTrue(cupom.contains("Nº: 000002"));
            assertFalse(cupom.contains("DADOS DO CLIENTE"));
        }

        @Test
        @DisplayName("Deve gerar cupom fiscal com cliente PJ")
        void testGerarCupomFiscal_ClientePessoaJuridica() {
            // Arrange
            Cliente clientePJ = new Cliente();
            clientePJ.setNome("Empresa Teste Ltda");
            clientePJ.setCpfCnpj("12.345.678/0001-95");
            clientePJ.setTipoPessoa("JURIDICA");

            // Act
            String cupom = cupomManager.gerarCupomFiscal(itensVenda, pagamento, clientePJ, operador, "000003");

            // Assert
            assertNotNull(cupom);
            assertTrue(cupom.contains("Empresa Teste Ltda"));
            assertTrue(cupom.contains("CNPJ: 12.345.678/0001-95"));
        }

        @Test
        @DisplayName("Deve gerar cupom fiscal com múltiplos itens")
        void testGerarCupomFiscal_MultiplosItens() {
            // Arrange
            List<ItemVenda> muitosItens = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                ItemVenda item = criarItemVenda("Produto " + i, new BigDecimal(10.0 * i), i);
                muitosItens.add(item);
            }

            // Act
            String cupom = cupomManager.gerarCupomFiscal(muitosItens, pagamento, cliente, operador, "000004");

            // Assert
            assertNotNull(cupom);
            assertTrue(cupom.contains("Produto 1"));
            assertTrue(cupom.contains("Produto 2"));
            assertTrue(cupom.contains("Produto 3"));
            assertTrue(cupom.contains("Produto 4"));
            assertTrue(cupom.contains("Produto 5"));
        }
    }

    @Nested
    @DisplayName("Testes de Resumo de Cupom")
    class ResumoCupom {

        @Test
        @DisplayName("Deve gerar resumo simplificado do cupom")
        void testGerarResumoCupom() {
            // Act
            String resumo = cupomManager.gerarResumoCupom(itensVenda, new BigDecimal("150.75"), "000005");

            // Assert
            assertNotNull(resumo);
            assertTrue(resumo.contains("CUPOM NÃO FISCAL - 000005"));
            assertTrue(resumo.contains("Qtd Itens: 2"));
            assertTrue(resumo.contains("Valor Total: R$ 150,75"));
        }

        @Test
        @DisplayName("Deve gerar resumo com valor zero")
        void testGerarResumoCupom_ValorZero() {
            // Act
            String resumo = cupomManager.gerarResumoCupom(new ArrayList<>(), BigDecimal.ZERO, "000006");

            // Assert
            assertNotNull(resumo);
            assertTrue(resumo.contains("Qtd Itens: 0"));
            assertTrue(resumo.contains("Valor Total: R$ 0,00"));
        }
    }

    @Nested
    @DisplayName("Testes de Cupom de Cancelamento")
    class CupomCancelamento {

        @Test
        @DisplayName("Deve gerar cupom de cancelamento completo")
        void testGerarCupomCancelamento() {
            // Act
            String cupom = cupomManager.gerarCupomCancelamento("000001", new BigDecimal("150.75"), 
                "Cliente desistiu da compra", operador);

            // Assert
            assertNotNull(cupom);
            assertTrue(cupom.contains("CUPOM DE CANCELAMENTO"));
            assertTrue(cupom.contains("CANCELAMENTO DO CUPOM: 000001"));
            assertTrue(cupom.contains("Valor Cancelado: R$ 150,75"));
            assertTrue(cupom.contains("Motivo: Cliente desistiu da compra"));
            assertTrue(cupom.contains("Operador: João Silva"));
        }

        @Test
        @DisplayName("Deve gerar cupom de cancelamento sem motivo")
        void testGerarCupomCancelamento_SemMotivo() {
            // Act
            String cupom = cupomManager.gerarCupomCancelamento("000002", new BigDecimal("50.00"), null, operador);

            // Assert
            assertNotNull(cupom);
            assertTrue(cupom.contains("Motivo: Não informado"));
        }

        @Test
        @DisplayName("Deve gerar cupom de cancelamento com valor zero")
        void testGerarCupomCancelamento_ValorZero() {
            // Act
            String cupom = cupomManager.gerarCupomCancelamento("000003", BigDecimal.ZERO, "Erro no sistema", operador);

            // Assert
            assertNotNull(cupom);
            assertTrue(cupom.contains("Valor Cancelado: R$ 0,00"));
        }
    }

    @Nested
    @DisplayName("Testes de Configuração da Empresa")
    class ConfiguracaoEmpresa {

        @Test
        @DisplayName("Deve manter nome da empresa padrão")
        void testGetNomeEmpresa_Padrao() {
            // Assert
            assertEquals("Hermes Comercial Ltda", cupomManager.getNomeEmpresa());
        }

        @Test
        @DisplayName("Deve alterar nome da empresa")
        void testSetNomeEmpresa() {
            // Act
            cupomManager.setNomeEmpresa("Nova Empresa Ltda");

            // Assert
            assertEquals("Nova Empresa Ltda", cupomManager.getNomeEmpresa());
        }

        @Test
        @DisplayName("Deve manter CNPJ da empresa padrão")
        void testGetCnpjEmpresa_Padrao() {
            // Assert
            assertEquals("12.345.678/0001-95", cupomManager.getCnpjEmpresa());
        }

        @Test
        @DisplayName("Deve alterar CNPJ da empresa")
        void testSetCnpjEmpresa() {
            // Act
            cupomManager.setCnpjEmpresa("98.765.432/0001-10");

            // Assert
            assertEquals("98.765.432/0001-10", cupomManager.getCnpjEmpresa());
        }

        @Test
        @DisplayName("Deve manter endereço da empresa padrão")
        void testGetEnderecoEmpresa_Padrao() {
            // Assert
            assertEquals("Rua Comercial, 123 - Centro - São Paulo/SP", cupomManager.getEnderecoEmpresa());
        }

        @Test
        @DisplayName("Deve alterar endereço da empresa")
        void testSetEnderecoEmpresa() {
            // Act
            cupomManager.setEnderecoEmpresa("Av. Principal, 456 - Centro - Rio de Janeiro/RJ");

            // Assert
            assertEquals("Av. Principal, 456 - Centro - Rio de Janeiro/RJ", cupomManager.getEnderecoEmpresa());
        }
    }

    @Nested
    @DisplayName("Testes de Tratamento de Erros")
    class TratamentoErros {

        @Test
        @DisplayName("Deve tratar erro na geração de cupom fiscal")
        void testGerarCupomFiscal_Erro() {
            // Act
            String cupom = cupomManager.gerarCupomFiscal(null, null, null, null, null);

            // Assert
            assertEquals("ERRO AO GERAR CUPOM FISCAL", cupom);
        }

        @Test
        @DisplayName("Deve tratar erro na geração de resumo")
        void testGerarResumoCupom_Erro() {
            // Act
            String resumo = cupomManager.gerarResumoCupom(null, null, null);

            // Assert
            assertEquals("ERRO AO GERAR RESUMO", resumo);
        }

        @Test
        @DisplayName("Deve tratar erro na geração de cupom de cancelamento")
        void testGerarCupomCancelamento_Erro() {
            // Act
            String cupom = cupomManager.gerarCupomCancelamento(null, null, null, null);

            // Assert
            assertEquals("ERRO AO GERAR CUPOM DE CANCELAMENTO", cupom);
        }
    }

    @Nested
    @DisplayName("Testes de Formatação de Valores")
    class FormatacaoValores {

        @Test
        @DisplayName("Deve formatar valor monetário corretamente")
        void testFormatarValor() {
            // Este teste usa reflexão para acessar método privado
            try {
                var method = CupomFiscalManager.class.getDeclaredMethod("formatarValor", BigDecimal.class);
                method.setAccessible(true);
                
                // Act
                String resultado = (String) method.invoke(cupomManager, new BigDecimal("123.456"));
                
                // Assert
                assertEquals("123,46", resultado);
            } catch (Exception e) {
                fail("Não deveria lançar exceção: " + e.getMessage());
            }
        }

        @Test
        @DisplayName("Deve formatar valor nulo")
        void testFormatarValor_Nulo() {
            try {
                var method = CupomFiscalManager.class.getDeclaredMethod("formatarValor", BigDecimal.class);
                method.setAccessible(true);
                
                // Act
                String resultado = (String) method.invoke(cupomManager, (BigDecimal) null);
                
                // Assert
                assertEquals("0,00", resultado);
            } catch (Exception e) {
                fail("Não deveria lançar exceção: " + e.getMessage());
            }
        }
    }

    // Métodos auxiliares
    private List<ItemVenda> criarItensVendaMock() {
        List<ItemVenda> itens = new ArrayList<>();
        itens.add(criarItemVenda("Produto Teste 1", new BigDecimal("50.00"), 1));
        itens.add(criarItemVenda("Produto Teste 2", new BigDecimal("100.75"), 2));
        return itens;
    }

    private ItemVenda criarItemVenda(String nome, BigDecimal valor, int quantidade) {
        ItemVenda item = new ItemVenda();
        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setPrecoVenda(valor);
        item.setProduto(produto);
        item.setValorUnitario(valor);
        item.setQuantidade(quantidade);
        item.setValorFinal(valor.multiply(new BigDecimal(quantidade)));
        return item;
    }

    private Cliente criarClienteMock() {
        Cliente cliente = new Cliente();
        cliente.setNome("João Silva");
        cliente.setCpf("123.456.789-00");
        cliente.setTipoPessoa("FISICA");
        cliente.setEndereco("Rua das Flores");
        cliente.setNumero("456");
        cliente.setBairro("Centro");
        cliente.setCidade("São Paulo");
        cliente.setEstado("SP");
        cliente.setCep("01234-567");
        return cliente;
    }

    private Usuario criarOperadorMock() {
        Usuario operador = new Usuario();
        operador.setNome("João Silva");
        operador.setId(1L);
        return operador;
    }

    private Pagamento criarPagamentoMock() {
        Pagamento pagamento = new Pagamento();
        pagamento.setTipoPagamento("DINHEIRO");
        pagamento.setValorPago(new BigDecimal("150.75"));
        pagamento.setValorTroco(new BigDecimal("0.00"));
        pagamento.setDataPagamento(LocalDateTime.now());
        pagamento.aprovar();
        return pagamento;
    }
}
