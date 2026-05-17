package com.br.hermescomercial.shared.service;

import com.br.hermescomercial.exception.BusinessException;
import com.br.hermescomercial.exception.DataAccessException;
import com.br.hermescomercial.exception.ExceptionHandler;
import com.br.hermescomercial.shared.model.NotaFiscal;
import com.br.hermescomercial.shared.model.NotaFiscalItem;
import com.br.hermescomercial.shared.model.NotaFiscalPagamento;
import com.br.hermescomercial.shared.model.NotaFiscalPagamentoItem;
import com.br.hermescomercial.shared.model.NotaFiscalTransporte;
import com.br.hermescomercial.pdv.model.VendaPDV;
import com.br.hermescomercial.erp.model.Usuario;
import com.br.hermescomercial.shared.model.Cliente;
import com.br.hermescomercial.pdv.model.ItemVenda;
import com.br.hermescomercial.pdv.model.Pagamento;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;


public class NotaFiscalService {
    
    private static final Logger logger = LogManager.getLogger(NotaFiscalService.class);
    
    // Configurações do emitente (simuladas)
    private static final String EMITENTE_CNPJ = "12.345.678/0001-99";
    private static final String EMITENTE_NOME = "Hermes Comercial Ltda";
    private static final String EMITENTE_NOME_FANTASIA = "Hermes PDV";
    private static final String EMITENTE_ENDERECO = "Rua Comercial, 123";
    private static final String EMITENTE_MUNICIPIO = "São Paulo";
    private static final String EMITENTE_UF = "SP";
    private static final String EMITENTE_CEP = "01234-567";
    private static final String EMITENTE_TELEFONE = "(11) 1234-5678";
    private static final String EMITENTE_IE = "123.456.789.123";
    
    /**
     * Gera uma nota fiscal a partir de uma venda
     */
    public NotaFiscal gerarNotaFiscal(VendaPDV venda, Usuario operador) {
        try {
            logger.info("Iniciando geração de nota fiscal para venda: " + venda.getNumeroCupom());
            
            NotaFiscal notaFiscal = new NotaFiscal();
            
            // Dados básicos
            notaFiscal.setVenda(venda);
            notaFiscal.setNaturezaOperacao("VENDA");
            notaFiscal.setModelo(65); // NFC-e
            notaFiscal.setSerie("001");
            notaFiscal.setNumero(gerarNumeroNotaFiscal());
            notaFiscal.setDataEmissao(LocalDateTime.now());
            notaFiscal.setCriadoPor(operador);
            
            // Dados do emitente
            preencherDadosEmitente(notaFiscal);
            
            // Dados do destinatário
            preencherDadosDestinatario(notaFiscal, venda.getCliente());
            
            // Itens da nota fiscal
            preencherItensNotaFiscal(notaFiscal, venda.getItens());
            
            // Pagamentos
            preencherPagamentos(notaFiscal, venda.getPagamento());
            
            // Transporte (para NFC-e geralmente sem frete)
            preencherTransporte(notaFiscal);
            
            // Cálculos
            notaFiscal.calcularTotais();
            
            // Geração da chave de acesso
            notaFiscal.gerarChaveAcesso();
            
            // Status inicial
            notaFiscal.setStatus("EM_ELABORACAO");
            
            logger.info("Nota fiscal gerada com sucesso: " + notaFiscal.getChaveAcesso());
            return notaFiscal;
            
        } catch (BusinessException e) {
            ExceptionHandler.handleBusinessException("gerar nota fiscal", e);
            return null;
        } catch (DataAccessException e) {
            ExceptionHandler.handleDataAccessException("gerar nota fiscal", e);
            return null;
        } catch (Exception e) {
            ExceptionHandler.handleSystemException("gerar nota fiscal", e);
            return null;
        }
    }
    
    /**
     * Envia a nota fiscal para autorização
     */
    public boolean autorizarNotaFiscal(NotaFiscal notaFiscal) {
        try {
            logger.info("Enviando nota fiscal para autorização: " + notaFiscal.getChaveAcesso());
            
            // Simulação de envio para a SEFAZ
            // Em um ambiente real, aqui seria feita a integração com a API da SEFAZ
            
            // Simular tempo de processamento
            Thread.sleep(2000);
            
            // Simular autorização bem-sucedida
            notaFiscal.setStatus("AUTORIZADA");
            notaFiscal.setDataHoraAutorizacao(LocalDateTime.now());
            notaFiscal.setProtocoloAutorizacao("35" + System.currentTimeMillis());
            
            logger.info("Nota fiscal autorizada com sucesso. Protocolo: " + notaFiscal.getProtocoloAutorizacao());
            return true;
            
        } catch (Exception e) {
            logger.error("Erro ao autorizar nota fiscal: " + e.getMessage(), e);
            notaFiscal.setStatus("REJEITADA");
            return false;
        }
    }
    
    /**
     * Cancela uma nota fiscal autorizada
     */
    public boolean cancelarNotaFiscal(NotaFiscal notaFiscal, String motivoCancelamento) {
        try {
            if (!notaFiscal.isAutorizada()) {
                logger.warn("Tentativa de cancelar nota fiscal não autorizada: " + notaFiscal.getChaveAcesso());
                return false;
            }
            
            if (!notaFiscal.podeCancelar()) {
                logger.warn("Nota fiscal não pode ser cancelada (prazo de 24h excedido): " + notaFiscal.getChaveAcesso());
                return false;
            }
            
            logger.info("Cancelando nota fiscal: " + notaFiscal.getChaveAcesso() + " - Motivo: " + motivoCancelamento);
            
            // Simulação de cancelamento
            Thread.sleep(1500);
            
            notaFiscal.setStatus("CANCELADA");
            notaFiscal.setObservacao("CANCELADA: " + motivoCancelamento);
            
            logger.info("Nota fiscal cancelada com sucesso");
            return true;
            
        } catch (Exception e) {
            logger.error("Erro ao cancelar nota fiscal: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Gera o número da nota fiscal (simulado)
     */
    private String gerarNumeroNotaFiscal() {
        // Em um ambiente real, isso viria de um sequencial no banco de dados
        return String.format("%09d", System.currentTimeMillis() % 1000000000);
    }
    
    /**
     * Preenche os dados do emitente
     */
    private void preencherDadosEmitente(NotaFiscal notaFiscal) {
        notaFiscal.setEmitenteCnpj(EMITENTE_CNPJ);
        notaFiscal.setEmitenteNome(EMITENTE_NOME);
        notaFiscal.setEmitenteNomeFantasia(EMITENTE_NOME_FANTASIA);
        notaFiscal.setEmitenteEndereco(EMITENTE_ENDERECO);
        notaFiscal.setEmitenteMunicipio(EMITENTE_MUNICIPIO);
        notaFiscal.setEmitenteUf(EMITENTE_UF);
        notaFiscal.setEmitenteCep(EMITENTE_CEP);
        notaFiscal.setEmitenteTelefone(EMITENTE_TELEFONE);
        notaFiscal.setEmitenteInscricaoEstadual(EMITENTE_IE);
    }
    
    /**
     * Preenche os dados do destinatário
     */
    private void preencherDadosDestinatario(NotaFiscal notaFiscal, Cliente cliente) {
        if (cliente != null) {
            notaFiscal.setDestinatarioCnpjCpf(cliente.getCpfCnpj());
            notaFiscal.setDestinatarioNome(cliente.getNome());
            notaFiscal.setDestinatarioEndereco(cliente.getEndereco());
            notaFiscal.setDestinatarioMunicipio(cliente.getCidade());
            notaFiscal.setDestinatarioUf(cliente.getEstado());
            notaFiscal.setDestinatarioCep(cliente.getCep());
            notaFiscal.setDestinatarioTelefone(cliente.getTelefone());
            notaFiscal.setDestinatarioEmail(cliente.getEmail());
            notaFiscal.setDestinatarioInscricaoEstadual(cliente.getInscricaoEstadual());
        } else {
            // Consumidor não identificado
            notaFiscal.setDestinatarioCnpjCpf("00000000000");
            notaFiscal.setDestinatarioNome("CONSUMIDOR NÃO IDENTIFICADO");
        }
    }
    
    /**
     * Preenche os itens da nota fiscal
     */
    private void preencherItensNotaFiscal(NotaFiscal notaFiscal, List<ItemVenda> itensVenda) {
        List<NotaFiscalItem> itensNota = new ArrayList<>();
        
        if (itensVenda != null) {
            for (int i = 0; i < itensVenda.size(); i++) {
                ItemVenda itemVenda = itensVenda.get(i);
                NotaFiscalItem itemNota = new NotaFiscalItem();
                
                itemNota.setNumeroItem(i + 1);
                itemNota.preencherComItemVenda(itemVenda);
                itemNota.setNotaFiscal(notaFiscal);
                
                itensNota.add(itemNota);
            }
        }
        
        notaFiscal.setItens(itensNota);
    }
    
    /**
     * Preenche os dados de pagamento
     */
    private void preencherPagamentos(NotaFiscal notaFiscal, Pagamento pagamento) {
        NotaFiscalPagamento nfPagamento = new NotaFiscalPagamento();
        nfPagamento.setNotaFiscal(notaFiscal);
        
        List<NotaFiscalPagamentoItem> pagamentos = new ArrayList<>();
        
        if (pagamento != null) {
            NotaFiscalPagamentoItem item = new NotaFiscalPagamentoItem();
            item.preencherComPagamento(pagamento);
            item.setNotaFiscalPagamento(nfPagamento);
            pagamentos.add(item);
        }
        
        nfPagamento.setPagamentos(pagamentos);
        notaFiscal.setPagamentos(nfPagamento);
    }
    
    /**
     * Preenche os dados de transporte (sem frete para NFC-e)
     */
    private void preencherTransporte(NotaFiscal notaFiscal) {
        NotaFiscalTransporte transporte = new NotaFiscalTransporte();
        transporte.setModalidadeFrete("9"); // Sem frete
        transporte.setNotaFiscal(notaFiscal);
        notaFiscal.setTransporte(transporte);
    }
    
    /**
     * Valida se a nota fiscal está pronta para autorização
     */
    public boolean validarNotaFiscal(NotaFiscal notaFiscal) {
        try {
            // Validações básicas
            if (notaFiscal.getItens() == null || notaFiscal.getItens().isEmpty()) {
                logger.error("Nota fiscal sem itens");
                return false;
            }
            
            if (notaFiscal.getDestinatarioNome() == null || notaFiscal.getDestinatarioNome().isEmpty()) {
                logger.error("Nota fiscal sem destinatário");
                return false;
            }
            
            if (notaFiscal.getValorTotalNota() == null || notaFiscal.getValorTotalNota().compareTo(BigDecimal.ZERO) <= 0) {
                logger.error("Nota fiscal com valor total inválido");
                return false;
            }
            
            // Validação dos itens
            for (NotaFiscalItem item : notaFiscal.getItens()) {
                if (item.getQuantidade() == null || item.getQuantidade().compareTo(BigDecimal.ZERO) <= 0) {
                    logger.error("Item com quantidade inválida: " + item.getDescricao());
                    return false;
                }
                
                if (item.getValorUnitario() == null || item.getValorUnitario().compareTo(BigDecimal.ZERO) <= 0) {
                    logger.error("Item com valor unitário inválido: " + item.getDescricao());
                    return false;
                }
            }
            
            logger.info("Validação da nota fiscal concluída com sucesso");
            return true;
            
        } catch (Exception e) {
            logger.error("Erro na validação da nota fiscal: " + e.getMessage(), e);
            return false;
        }
    }
}
