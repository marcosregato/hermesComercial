package com.br.hermescomercial.business.pdv;

import com.br.hermescomercial.model.Cliente;
import com.br.hermescomercial.model.ItemVenda;
import com.br.hermescomercial.model.Pagamento;
import com.br.hermescomercial.model.Usuario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CupomFiscalManager {
    
    private static final Logger logger = LogManager.getLogger(CupomFiscalManager.class);
    
    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DateTimeFormatter FORMATADOR_DATA_CURTA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    private String nomeEmpresa;
    private String cnpjEmpresa;
    private String enderecoEmpresa;
    private String telefoneEmpresa;
    private String numeroTerminal;

    public CupomFiscalManager() {
        this.nomeEmpresa = "Hermes Comercial Ltda";
        this.cnpjEmpresa = "12.345.678/0001-95";
        this.enderecoEmpresa = "Rua Comercial, 123 - Centro - São Paulo/SP";
        this.telefoneEmpresa = "(11) 1234-5678";
        this.numeroTerminal = "001";
    }

    /**
     * Gera cupom fiscal completo
     * @param itens Lista de itens da venda
     * @param pagamento Pagamento realizado
     * @param cliente Cliente (opcional)
     * @param operador Operador do caixa
     * @param numeroCupom Número do cupom
     * @return String com cupom formatado
     */
    public String gerarCupomFiscal(List<ItemVenda> itens, Pagamento pagamento, Cliente cliente, 
                                  Usuario operador, String numeroCupom) {
        try {
            StringBuilder cupom = new StringBuilder();
            
            // Cabeçalho
            adicionarCabecalho(cupom, numeroCupom);
            
            // Dados do cliente (se houver)
            if (cliente != null) {
                adicionarDadosCliente(cupom, cliente);
            }
            
            // Itens da venda
            adicionarItens(cupom, itens);
            
            // Totais
            adicionarTotais(cupom, itens);
            
            // Pagamento
            adicionarPagamento(cupom, pagamento);
            
            // Rodapé
            adicionarRodape(cupom, operador);
            
            return cupom.toString();
            
        } catch (Exception e) {
            logger.error("Erro ao gerar cupom fiscal: " + e.getMessage(), e);
            return "ERRO AO GERAR CUPOM FISCAL";
        }
    }

    /**
     * Gera resumo simplificado do cupom
     */
    public String gerarResumoCupom(List<ItemVenda> itens, BigDecimal valorTotal, String numeroCupom) {
        try {
            StringBuilder resumo = new StringBuilder();
            
            resumo.append("CUPOM NÃO FISCAL - ").append(numeroCupom).append("\n");
            resumo.append("Data: ").append(LocalDateTime.now().format(FORMATADOR_DATA_CURTA)).append("\n");
            resumo.append("Qtd Itens: ").append(itens.size()).append("\n");
            resumo.append("Valor Total: R$ ").append(formatarValor(valorTotal)).append("\n");
            
            return resumo.toString();
            
        } catch (Exception e) {
            logger.error("Erro ao gerar resumo: " + e.getMessage(), e);
            return "ERRO AO GERAR RESUMO";
        }
    }

    /**
     * Gera cupom de cancelamento
     */
    public String gerarCupomCancelamento(String numeroCupomOriginal, BigDecimal valorCancelado, 
                                        String motivo, Usuario operador) {
        try {
            StringBuilder cupom = new StringBuilder();
            
            cupom.append("CUPOM DE CANCELAMENTO\n");
            cupom.append("================================\n");
            cupom.append("CANCELAMENTO DO CUPOM: ").append(numeroCupomOriginal).append("\n");
            cupom.append("Data: ").append(LocalDateTime.now().format(FORMATADOR_DATA)).append("\n");
            cupom.append("Valor Cancelado: R$ ").append(formatarValor(valorCancelado)).append("\n");
            cupom.append("Motivo: ").append(motivo != null ? motivo : "Não informado").append("\n");
            cupom.append("Operador: ").append(operador != null ? operador.getNome() : "N/A").append("\n");
            cupom.append("================================\n");
            
            return cupom.toString();
            
        } catch (Exception e) {
            logger.error("Erro ao gerar cupom de cancelamento: " + e.getMessage(), e);
            return "ERRO AO GERAR CUPOM DE CANCELAMENTO";
        }
    }

    private void adicionarCabecalho(StringBuilder cupom, String numeroCupom) {
        cupom.append(nomeEmpresa).append("\n");
        cupom.append("CNPJ: ").append(cnpjEmpresa).append("\n");
        cupom.append(enderecoEmpresa).append("\n");
        cupom.append("Tel: ").append(telefoneEmpresa).append("\n");
        cupom.append("================================\n");
        cupom.append("CUPOM FISCAL ELETRÔNICO\n");
        cupom.append("Nº: ").append(numeroCupom).append("\n");
        cupom.append("Data: ").append(LocalDateTime.now().format(FORMATADOR_DATA)).append("\n");
        cupom.append("Terminal: ").append(numeroTerminal).append("\n");
        cupom.append("================================\n");
    }

    private void adicionarDadosCliente(StringBuilder cupom, Cliente cliente) {
        cupom.append("DADOS DO CLIENTE\n");
        cupom.append("Nome: ").append(cliente.getNome()).append("\n");
        
        if (cliente.getCpfCnpj() != null) {
            String documento = cliente.isPessoaFisica() ? "CPF" : "CNPJ";
            cupom.append(documento).append(": ").append(formatarDocumento(cliente.getCpfCnpj())).append("\n");
        }
        
        if (cliente.getEnderecoCompleto() != null && !cliente.getEnderecoCompleto().trim().isEmpty()) {
            cupom.append("Endereço: ").append(cliente.getEnderecoCompleto()).append("\n");
        }
        
        cupom.append("--------------------------------\n");
    }

    private void adicionarItens(StringBuilder cupom, List<ItemVenda> itens) {
        cupom.append("ITEM  CODIGO  DESCRIÇÃO                     QTD   UN   VL.UNIT   VL.TOTAL\n");
        cupom.append("--------------------------------\n");
        
        int contador = 1;
        for (ItemVenda item : itens) {
            String nomeProduto = item.getProduto().getNome();
            String codigo = item.getProduto().getId() != null ? item.getProduto().getId().toString() : "000";
            String unidade = item.getProduto().getUnidade() != null ? item.getProduto().getUnidade() : "UN";
            
            cupom.append(String.format("%02d    %-6s  %-29s %3d  %-2s %9s %9s\n",
                contador,
                codigo,
                limitarString(nomeProduto, 29),
                item.getQuantidade(),
                unidade,
                formatarValor(item.getValorUnitario()),
                formatarValor(item.getValorFinal())
            ));
            
            // Adicionar desconto se houver
            if (item.getDesconto() != null && item.getDesconto().compareTo(BigDecimal.ZERO) > 0) {
                cupom.append(String.format("      Desconto: %46s\n", formatarValor(item.getDesconto())));
            }
            
            contador++;
        }
        
        cupom.append("--------------------------------\n");
    }

    private void adicionarTotais(StringBuilder cupom, List<ItemVenda> itens) {
        BigDecimal subtotal = itens.stream()
                .map(ItemVenda::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalDesconto = itens.stream()
                .map(ItemVenda::getDesconto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalFinal = itens.stream()
                .map(ItemVenda::getValorFinal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        cupom.append("SUBTOTAL: ").append(String.format("%47s", formatarValor(subtotal))).append("\n");
        
        if (totalDesconto.compareTo(BigDecimal.ZERO) > 0) {
            cupom.append("DESCONTO: ").append(String.format("%47s", formatarValor(totalDesconto))).append("\n");
        }
        
        cupom.append("TOTAL:    ").append(String.format("%47s", formatarValor(totalFinal))).append("\n");
        cupom.append("================================\n");
    }

    private void adicionarPagamento(StringBuilder cupom, Pagamento pagamento) {
        cupom.append("FORMA PAGAMENTO: ").append(pagamento.getTipoPagamento()).append("\n");
        cupom.append("VALOR PAGO: ").append(formatarValor(pagamento.getValorPago())).append("\n");
        
        if (pagamento.getValorTroco() != null && pagamento.getValorTroco().compareTo(BigDecimal.ZERO) > 0) {
            cupom.append("TROCO: ").append(formatarValor(pagamento.getValorTroco())).append("\n");
        }
        
        if (pagamento.getNumeroParcelas() != null && !"1".equals(pagamento.getNumeroParcelas())) {
            cupom.append("PARCELAS: ").append(pagamento.getNumeroParcelas()).append("x\n");
        }
        
        cupom.append("================================\n");
    }

    private void adicionarRodape(StringBuilder cupom, Usuario operador) {
        cupom.append("Operador: ").append(operador != null ? operador.getNome() : "N/A").append("\n");
        cupom.append("Obrigado pela preferência!\n");
        cupom.append("Volte sempre!\n");
        cupom.append("================================\n");
        cupom.append("CUPOM NÃO FISCAL - SEM VALOR FISCAL\n");
    }

    private String formatarValor(BigDecimal valor) {
        if (valor == null) return "0,00";
        return String.format("%,.2f", valor).replace('.', ',');
    }

    private String formatarDocumento(String documento) {
        if (documento == null) return "";
        
        // Formatar CPF
        if (documento.length() == 11) {
            return documento.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
        }
        
        // Formatar CNPJ
        if (documento.length() == 14) {
            return documento.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
        }
        
        return documento;
    }

    private String limitarString(String str, int tamanho) {
        if (str == null) return "";
        return str.length() > tamanho ? str.substring(0, tamanho) : str;
    }

    // Getters e Setters
    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getCnpjEmpresa() {
        return cnpjEmpresa;
    }

    public void setCnpjEmpresa(String cnpjEmpresa) {
        this.cnpjEmpresa = cnpjEmpresa;
    }

    public String getEnderecoEmpresa() {
        return enderecoEmpresa;
    }

    public void setEnderecoEmpresa(String enderecoEmpresa) {
        this.enderecoEmpresa = enderecoEmpresa;
    }

    public String getTelefoneEmpresa() {
        return telefoneEmpresa;
    }

    public void setTelefoneEmpresa(String telefoneEmpresa) {
        this.telefoneEmpresa = telefoneEmpresa;
    }

    public String getNumeroTerminal() {
        return numeroTerminal;
    }

    public void setNumeroTerminal(String numeroTerminal) {
        this.numeroTerminal = numeroTerminal;
    }
}
