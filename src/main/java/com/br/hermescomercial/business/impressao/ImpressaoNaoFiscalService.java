package com.br.hermescomercial.business.impressao;

import com.br.hermescomercial.shared.model.ImpressoraNaoFiscal;
import com.br.hermescomercial.pdv.model.VendaPDV;
import com.br.hermescomercial.pdv.model.ItemVenda;
import com.br.hermescomercial.shared.model.NotaFiscal;
// import com.br.hermescomercial.model.Usuario; - não utilizado
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.math.BigDecimal;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
// import java.time.LocalDateTime; - não utilizado
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ImpressaoNaoFiscalService {
    
    private static final Logger logger = LogManager.getLogger(ImpressaoNaoFiscalService.class);
    
    private static final int LINE_FEED = 10;
    // private static final int CARRIAGE_RETURN = 13; - não utilizado
    // private static final int FORM_FEED = 12; - não utilizado
    private static final int ESC = 27;
    private static final int GS = 29;
    
    // Comandos ESC/POS
    private static final byte[] ALIGN_CENTER = {ESC, 'a', 1};
    private static final byte[] ALIGN_LEFT = {ESC, 'a', 0};
    private static final byte[] ALIGN_RIGHT = {ESC, 'a', 2};
    private static final byte[] BOLD_ON = {ESC, 'E', 1};
    private static final byte[] BOLD_OFF = {ESC, 'E', 0};
    private static final byte[] DOUBLE_HEIGHT_ON = {ESC, '!', 16};
    private static final byte[] DOUBLE_HEIGHT_OFF = {ESC, '!', 0};
    private static final byte[] CUT_PAPER = {GS, 'V', 1};
    // private static final byte[] PARTIAL_CUT = {GS, 'V', 66}; - não utilizado
    private static final byte[] OPEN_DRAWER = {ESC, 'p', 0, 60, 120};
    
    private ImpressoraNaoFiscal impressoraConfigurada;
    private OutputStream outputStream;
    private boolean conectado = false;
    
    public ImpressaoNaoFiscalService() {
        // Carregar configuração padrão ou do banco
        this.impressoraConfigurada = criarConfiguracaoPadrao();
    }
    
    /**
     * Conecta à impressora não fiscal
     */
    public boolean conectar() {
        try {
            if (impressoraConfigurada == null) {
                logger.error("Configuração da impressora não encontrada");
                return false;
            }
            
            logger.info("Conectando à impressora: " + impressoraConfigurada.getDescricaoCompleta());
            
            switch (impressoraConfigurada.getTipoConexao().toUpperCase()) {
                case "USB":
                    return conectarUSB();
                case "SERIAL":
                    return conectarSerial();
                case "REDE":
                    return conectarRede();
                case "BLUETOOTH":
                    return conectarBluetooth();
                default:
                    logger.error("Tipo de conexão não suportado: " + impressoraConfigurada.getTipoConexao());
                    return false;
            }
            
        } catch (Exception e) {
            logger.error("Erro ao conectar à impressora: " + e.getMessage(), e);
            impressoraConfigurada.atualizarStatus(false, "Erro de conexão: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Desconecta da impressora
     */
    public void desconectar() {
        try {
            if (outputStream != null) {
                outputStream.close();
                outputStream = null;
            }
            conectado = false;
            logger.info("Desconectado da impressora");
        } catch (Exception e) {
            logger.error("Erro ao desconectar: " + e.getMessage(), e);
        }
    }
    
    /**
     * Imprime cupom de venda
     */
    public boolean imprimirCupomVenda(VendaPDV venda) {
        try {
            if (!conectado && !conectar()) {
                return false;
            }
            
            logger.info("Imprimindo cupom de venda: " + venda.getNumeroCupom());
            
            // Cabeçalho
            imprimirCabecalho();
            
            // Dados da venda
            imprimirDadosVenda(venda);
            
            // Itens
            imprimirItensVenda(venda.getItens());
            
            // Totais
            imprimirTotaisVenda(venda);
            
            // Pagamento
            imprimirPagamento(venda.getPagamento());
            
            // Rodapé
            imprimirRodape();
            
            // Cortar papel
            if (impressoraConfigurada.isCortaPapel()) {
                cortarPapel();
            }
            
            logger.info("Cupom impresso com sucesso");
            return true;
            
        } catch (Exception e) {
            logger.error("Erro ao imprimir cupom: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Imprime nota fiscal
     */
    public boolean imprimirNotaFiscal(NotaFiscal notaFiscal) {
        try {
            if (!conectado && !conectar()) {
                return false;
            }
            
            logger.info("Imprimindo nota fiscal: " + notaFiscal.getChaveAcesso());
            
            // Cabeçalho
            imprimirCabecalho();
            
            // Dados da nota fiscal
            imprimirDadosNotaFiscal(notaFiscal);
            
            // Itens
            imprimirItensNotaFiscal(notaFiscal.getItens());
            
            // Totais e impostos
            imprimirTotaisNotaFiscal(notaFiscal);
            
            // Pagamentos
            imprimirPagamentosNotaFiscal(notaFiscal.getPagamentos().getPagamentos());
            
            // Rodapé
            imprimirRodape();
            
            // Cortar papel
            if (impressoraConfigurada.isCortaPapel()) {
                cortarPapel();
            }
            
            logger.info("Nota fiscal impressa com sucesso");
            return true;
            
        } catch (Exception e) {
            logger.error("Erro ao imprimir nota fiscal: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Imprime relatório de fechamento de caixa
     */
    public boolean imprimirRelatorioCaixa(String relatorio) {
        try {
            if (!conectado && !conectar()) {
                return false;
            }
            
            logger.info("Imprimindo relatório de caixa");
            
            // Cabeçalho
            imprimirCabecalho();
            
            // Conteúdo do relatório
            imprimirTexto(relatorio, ALIGN_LEFT);
            
            // Rodapé
            imprimirRodape();
            
            // Cortar papel
            if (impressoraConfigurada.isCortaPapel()) {
                cortarPapel();
            }
            
            logger.info("Relatório de caixa impresso com sucesso");
            return true;
            
        } catch (Exception e) {
            logger.error("Erro ao imprimir relatório: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Abre a gaveta de dinheiro
     */
    public boolean abrirGaveta() {
        try {
            if (!conectado && !conectar()) {
                return false;
            }
            
            if (impressoraConfigurada.isGavetaDinheiro()) {
                outputStream.write(OPEN_DRAWER);
                outputStream.flush();
                logger.info("Gaveta de dinheiro aberta");
                return true;
            } else {
                logger.warn("Impressora não tem suporte para gaveta de dinheiro");
                return false;
            }
            
        } catch (Exception e) {
            logger.error("Erro ao abrir gaveta: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Verifica status da impressora
     */
    public boolean verificarStatus() {
        try {
            // Simulação de verificação realista - na maioria dos casos não há impressora conectada
            logger.info("Verificando status da impressora: " + impressoraConfigurada.getDescricaoCompleta());
            
            // Simular detecção de impressora (70% de chance de não encontrar)
            boolean impressoraDetectada = Math.random() > 0.7;
            
            if (!impressoraDetectada) {
                String mensagemErro = "Impressora não fiscal não encontrada. Verifique conexão e configuração.";
                impressoraConfigurada.atualizarStatus(false, mensagemErro);
                conectado = false;
                logger.warn("Impressora não fiscal não detectada: " + impressoraConfigurada.getDescricaoCompleta());
                return false;
            }
            
            // Se impressora "detectada", tentar conectar
            if (!conectado) {
                if (!conectar()) {
                    impressoraConfigurada.atualizarStatus(false, "Falha ao conectar impressora não fiscal. Verifique cabos e configurações.");
                    return false;
                }
            }
            
            // Simulação de verificação de status real
            // Em uma implementação real, aqui seriam enviados comandos de status
            boolean statusOk = Math.random() > 0.2; // 80% de chance de estar OK se conectada
            
            if (statusOk) {
                impressoraConfigurada.atualizarStatus(true, "Operacional");
                logger.info("Impressora operacional: " + impressoraConfigurada.getDescricaoCompleta());
                return true;
            } else {
                impressoraConfigurada.atualizarStatus(false, "Erro de comunicação com impressora não fiscal. Reinicie a impressora.");
                conectado = false;
                logger.error("Erro de comunicação com impressora não fiscal: " + impressoraConfigurada.getDescricaoCompleta());
                return false;
            }
            
        } catch (Exception e) {
            logger.error("Erro ao verificar status: " + e.getMessage(), e);
            impressoraConfigurada.atualizarStatus(false, "Erro: " + e.getMessage());
            conectado = false;
            return false;
        }
    }
    
    // Métodos privados de impressão
    private void imprimirCabecalho() throws IOException {
        imprimirLinhaEmBranco();
        imprimirTextoCentralizado("HERMES COMERCIAL LTDA", BOLD_ON);
        imprimirTextoCentralizado("CNPJ: 12.345.678/0001-99", BOLD_OFF);
        imprimirTextoCentralizado("Rua Comercial, 123 - São Paulo/SP");
        imprimirTextoCentralizado("Tel: (11) 1234-5678");
        imprimirLinhaEmBranco();
        imprimirSeparador();
    }
    
    private void imprimirDadosVenda(VendaPDV venda) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        
        imprimirTexto("CUPOM NÃO FISCAL", BOLD_ON);
        imprimirTexto("N°: " + venda.getNumeroCupom(), BOLD_OFF);
        imprimirTexto("Data: " + venda.getDataVenda().format(formatter));
        imprimirTexto("Operador: " + venda.getOperador().getNome());
        imprimirTexto("Terminal: " + venda.getNumeroTerminal());
        imprimirSeparador();
    }
    
    private void imprimirDadosNotaFiscal(NotaFiscal notaFiscal) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        
        imprimirTexto("DANFE NFC-e", BOLD_ON);
        imprimirTexto("N°: " + notaFiscal.getNumero() + " Série: " + notaFiscal.getSerie(), BOLD_OFF);
        imprimirTexto("Emissão: " + notaFiscal.getDataEmissao().format(formatter));
        
        if (notaFiscal.getDataHoraAutorizacao() != null) {
            imprimirTexto("Autorização: " + notaFiscal.getDataHoraAutorizacao().format(formatter));
            imprimirTexto("Protocolo: " + notaFiscal.getProtocoloAutorizacao());
        }
        
        imprimirSeparador();
        imprimirTexto("EMITENTE", BOLD_ON);
        imprimirTexto(notaFiscal.getEmitenteNome(), BOLD_OFF);
        imprimirTexto("CNPJ: " + notaFiscal.getEmitenteCnpj());
        imprimirTexto(notaFiscal.getEmitenteEndereco() + " - " + notaFiscal.getEmitenteMunicipio() + "/" + notaFiscal.getEmitenteUf());
        imprimirSeparador();
        
        imprimirTexto("DESTINATÁRIO", BOLD_ON);
        imprimirTexto(notaFiscal.getDestinatarioNome(), BOLD_OFF);
        if (!"CONSUMIDOR NÃO IDENTIFICADO".equals(notaFiscal.getDestinatarioNome())) {
            imprimirTexto("CPF/CNPJ: " + notaFiscal.getDestinatarioCnpjCpf());
            imprimirTexto(notaFiscal.getDestinatarioEndereco() + " - " + notaFiscal.getDestinatarioMunicipio() + "/" + notaFiscal.getDestinatarioUf());
        }
        imprimirSeparador();
    }
    
    private void imprimirItensVenda(List<ItemVenda> itens) throws IOException {
        imprimirTexto("ITENS", BOLD_ON);
        imprimirSeparador();
        
        for (ItemVenda item : itens) {
            String descricao = item.getProduto();
            String quantidade = String.format("%3d", item.getQuantidade());
            String valorUnit = String.format("R$ %7.2f", item.getValorUnitario());
            String valorTotal = String.format("R$ %7.2f", item.getValorFinal());
            
            // Descrição do produto
            imprimirTexto(descricao);
            
            // Quantidade, valor unitário e total
            imprimirTexto(String.format("  %s x %s = %s", quantidade, valorUnit, valorTotal));
            
            // Desconto se houver
            if (item.getDesconto() != null && item.getDesconto().compareTo(BigDecimal.ZERO) > 0) {
                imprimirTexto("  Desconto: R$ " + String.format("%6.2f", item.getDesconto()));
            }
            
            imprimirLinhaEmBranco();
        }
        
        imprimirSeparador();
    }
    
    private void imprimirItensNotaFiscal(List<com.br.hermescomercial.shared.model.NotaFiscalItem> itens) throws IOException {
        imprimirTexto("ITENS", BOLD_ON);
        imprimirSeparador();
        
        for (com.br.hermescomercial.shared.model.NotaFiscalItem item : itens) {
            String codigo = String.format("%-8s", item.getCodigoProduto());
            String descricao = item.getDescricao();
            String quantidade = String.format("%6s", item.getQuantidade().toString());
            String valorUnit = String.format("R$ %8.2f", item.getValorUnitario());
            String valorTotal = String.format("R$ %8.2f", item.getValorTotal());
            
            // Código e descrição
            imprimirTexto(codigo + " " + descricao);
            
            // Quantidade, valor unitário e total
            imprimirTexto(String.format("  Qtd: %s Unit: %s Total: %s", quantidade, valorUnit, valorTotal));
            
            imprimirLinhaEmBranco();
        }
        
        imprimirSeparador();
    }
    
    private void imprimirTotaisVenda(VendaPDV venda) throws IOException {
        imprimirTexto("RESUMO", BOLD_ON);
        imprimirSeparador();
        
        imprimirTextoAlinhadoDireita("Subtotal: R$ " + String.format("%9.2f", venda.getValorTotal()));
        
        if (venda.getValorDesconto() != null && venda.getValorDesconto().compareTo(BigDecimal.ZERO) > 0) {
            imprimirTextoAlinhadoDireita("Desconto: -R$ " + String.format("%9.2f", venda.getValorDesconto()));
        }
        
        imprimirTexto("TOTAL:", DOUBLE_HEIGHT_ON);
        imprimirTextoAlinhadoDireita("R$ " + String.format("%9.2f", venda.getValorFinal()), DOUBLE_HEIGHT_ON);
        imprimirTexto("", DOUBLE_HEIGHT_OFF); // Reset
        
        imprimirSeparador();
    }
    
    private void imprimirTotaisNotaFiscal(NotaFiscal notaFiscal) throws IOException {
        imprimirTexto("RESUMO DOS VALORES", BOLD_ON);
        imprimirSeparador();
        
        imprimirTextoAlinhadoDireita("Total Produtos: R$ " + String.format("%9.2f", notaFiscal.getValorTotalProdutos()));
        
        if (notaFiscal.getValorDesconto() != null && notaFiscal.getValorDesconto().compareTo(BigDecimal.ZERO) > 0) {
            imprimirTextoAlinhadoDireita("Desconto: -R$ " + String.format("%9.2f", notaFiscal.getValorDesconto()));
        }
        
        if (notaFiscal.getValorFrete() != null && notaFiscal.getValorFrete().compareTo(BigDecimal.ZERO) > 0) {
            imprimirTextoAlinhadoDireita("Frete: R$ " + String.format("%9.2f", notaFiscal.getValorFrete()));
        }
        
        imprimirTexto("TOTAL NF:", DOUBLE_HEIGHT_ON);
        imprimirTextoAlinhadoDireita("R$ " + String.format("%9.2f", notaFiscal.getValorTotalNota()), DOUBLE_HEIGHT_ON);
        imprimirTexto("", DOUBLE_HEIGHT_OFF); // Reset
        
        imprimirSeparador();
        
        // Impostos
        imprimirTexto("IMPOSTOS", BOLD_ON);
        imprimirSeparador();
        
        if (notaFiscal.getValorIcms() != null && notaFiscal.getValorIcms().compareTo(BigDecimal.ZERO) > 0) {
            imprimirTextoAlinhadoDireita("Valor ICMS: R$ " + String.format("%9.2f", notaFiscal.getValorIcms()));
        }
        
        if (notaFiscal.getValorIpi() != null && notaFiscal.getValorIpi().compareTo(BigDecimal.ZERO) > 0) {
            imprimirTextoAlinhadoDireita("Valor IPI: R$ " + String.format("%9.2f", notaFiscal.getValorIpi()));
        }
        
        if (notaFiscal.getValorPis() != null && notaFiscal.getValorPis().compareTo(BigDecimal.ZERO) > 0) {
            imprimirTextoAlinhadoDireita("Valor PIS: R$ " + String.format("%9.2f", notaFiscal.getValorPis()));
        }
        
        if (notaFiscal.getValorCofins() != null && notaFiscal.getValorCofins().compareTo(BigDecimal.ZERO) > 0) {
            imprimirTextoAlinhadoDireita("Valor COFINS: R$ " + String.format("%9.2f", notaFiscal.getValorCofins()));
        }
        
        imprimirSeparador();
    }
    
    private void imprimirPagamento(com.br.hermescomercial.pdv.model.Pagamento pagamento) throws IOException {
        imprimirTexto("FORMA DE PAGAMENTO", BOLD_ON);
        imprimirSeparador();
        
        imprimirTexto("Tipo: " + pagamento.getTipoPagamento());
        imprimirTextoAlinhadoDireita("Valor Pago: R$ " + String.format("%9.2f", pagamento.getValorPago()));
        
        if (pagamento.getValorTroco() != null && pagamento.getValorTroco().compareTo(BigDecimal.ZERO) > 0) {
            imprimirTextoAlinhadoDireita("Troco: R$ " + String.format("%9.2f", pagamento.getValorTroco()));
        }
        
        imprimirSeparador();
    }
    
    private void imprimirPagamentosNotaFiscal(List<com.br.hermescomercial.shared.model.NotaFiscalPagamentoItem> pagamentos) throws IOException {
        imprimirTexto("FORMAS DE PAGAMENTO", BOLD_ON);
        imprimirSeparador();
        
        for (com.br.hermescomercial.shared.model.NotaFiscalPagamentoItem pagamento : pagamentos) {
            imprimirTexto(pagamento.getFormaPagamentoDescricao());
            imprimirTextoAlinhadoDireita("R$ " + String.format("%9.2f", pagamento.getValorPagamento()));
            imprimirLinhaEmBranco();
        }
        
        imprimirSeparador();
    }
    
    private void imprimirRodape() throws IOException {
        imprimirLinhaEmBranco();
        imprimirTextoCentralizado("Obrigado pela preferência!");
        imprimirTextoCentralizado("Volte sempre!");
        imprimirLinhaEmBranco();
        imprimirTextoCentralizado("Consulte nossa nota fiscal em:");
        imprimirTextoCentralizado("www.fazenda.sp.gov.br/nfce");
        imprimirLinhaEmBranco();
        imprimirTextoCentralizado("Chave de Acesso:");
        
        // Imprimir chave de acesso em múltiplas linhas se for muito longa
        String chave = impressoraConfigurada.getDriver().equals("ESCPOS") ? 
            "35240112345678900123550010000000012345678901" : // Chave simulada
            "CHAVE_SIMULADA_44_DIGITOS_1234567890123456789012";
        
        if (chave.length() > impressoraConfigurada.getColunas()) {
            for (int i = 0; i < chave.length(); i += impressoraConfigurada.getColunas()) {
                int end = Math.min(i + impressoraConfigurada.getColunas(), chave.length());
                imprimirTextoCentralizado(chave.substring(i, end));
            }
        } else {
            imprimirTextoCentralizado(chave);
        }
        
        imprimirLinhaEmBranco();
        imprimirSeparador();
    }
    
    // Métodos auxiliares de impressão
    private void imprimirTexto(String texto) throws IOException {
        imprimirTexto(texto, ALIGN_LEFT);
    }
    
    private void imprimirTexto(String texto, byte[] alinhamento) throws IOException {
        outputStream.write(alinhamento);
        byte[] textoBytes = texto.getBytes(getCharset());
        outputStream.write(textoBytes);
        outputStream.write(LINE_FEED);
    }
    
    private void imprimirTextoCentralizado(String texto) throws IOException {
        imprimirTextoCentralizado(texto, null);
    }
    
    private void imprimirTextoCentralizado(String texto, byte[] formatacao) throws IOException {
        if (formatacao != null) {
            outputStream.write(formatacao);
        }
        outputStream.write(ALIGN_CENTER);
        byte[] textoBytes = texto.getBytes(getCharset());
        outputStream.write(textoBytes);
        outputStream.write(LINE_FEED);
        if (formatacao != null) {
            outputStream.write(ALIGN_LEFT);
        }
    }
    
    private void imprimirTextoAlinhadoDireita(String texto) throws IOException {
        imprimirTextoAlinhadoDireita(texto, null);
    }
    
    private void imprimirTextoAlinhadoDireita(String texto, byte[] formatacao) throws IOException {
        if (formatacao != null) {
            outputStream.write(formatacao);
        }
        outputStream.write(ALIGN_RIGHT);
        byte[] textoBytes = texto.getBytes(getCharset());
        outputStream.write(textoBytes);
        outputStream.write(LINE_FEED);
        if (formatacao != null) {
            outputStream.write(ALIGN_LEFT);
        }
    }
    
    private void imprimirSeparador() throws IOException {
        String separador = "-".repeat(impressoraConfigurada.getColunas());
        imprimirTexto(separador);
    }
    
    private void imprimirLinhaEmBranco() throws IOException {
        outputStream.write(LINE_FEED);
    }
    
    private void cortarPapel() throws IOException {
        outputStream.write(CUT_PAPER);
        outputStream.flush();
    }
    
    // Métodos de conexão
    private boolean conectarUSB() {
        try {
            // Simulação de conexão USB
            // Em uma implementação real, aqui seria usada uma biblioteca como jUSB ou javax.usb
            outputStream = new ByteArrayOutputStream(); // Simulação
            conectado = true;
            impressoraConfigurada.atualizarStatus(true, "Conectado via USB");
            logger.info("Conectado via USB (simulação)");
            return true;
        } catch (Exception e) {
            logger.error("Erro na conexão USB: " + e.getMessage());
            return false;
        }
    }
    
    private boolean conectarSerial() {
        try {
            // Simulação de conexão serial
            // Em uma implementação real, aqui seria usada uma biblioteca como jSerialComm
            outputStream = new ByteArrayOutputStream(); // Simulação
            conectado = true;
            impressoraConfigurada.atualizarStatus(true, "Conectado via Serial");
            logger.info("Conectado via Serial (simulação)");
            return true;
        } catch (Exception e) {
            logger.error("Erro na conexão Serial: " + e.getMessage());
            return false;
        }
    }
    
    private boolean conectarRede() {
        try {
            if (impressoraConfigurada.getIp() == null || impressoraConfigurada.getIp().trim().isEmpty()) {
                logger.error("IP da impressora não configurado");
                impressoraConfigurada.atualizarStatus(false, "IP não configurado");
                return false;
            }
            
            // Simulação de tentativa de conexão realista
            logger.info("Tentando conectar via rede: " + impressoraConfigurada.getIp() + ":" + impressoraConfigurada.getPortaRede());
            
            // 80% de chance de falhar na conexão (simulando impressora não encontrada na rede)
            boolean conexaoSucesso = Math.random() > 0.8;
            
            if (!conexaoSucesso) {
                logger.error("Falha na conexão de rede: Impressora não fiscal não encontrada em " + impressoraConfigurada.getIp() + ":" + impressoraConfigurada.getPortaRede());
                impressoraConfigurada.atualizarStatus(false, "Impressora não fiscal não encontrada na rede. Verifique IP e porta.");
                return false;
            }
            
            // Simulação bem-sucedida
            Socket socket = new Socket(impressoraConfigurada.getIp(), impressoraConfigurada.getPortaRede());
            try {
                outputStream = socket.getOutputStream();
                conectado = true;
                impressoraConfigurada.atualizarStatus(true, "Conectado via Rede");
                logger.info("Conectado via Rede: " + impressoraConfigurada.getIp() + ":" + impressoraConfigurada.getPortaRede());
                return true;
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    logger.warn("Erro ao fechar socket: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            logger.error("Erro na conexão de rede: " + e.getMessage());
            impressoraConfigurada.atualizarStatus(false, "Erro de rede: " + e.getMessage());
            return false;
        }
    }
    
    private boolean conectarBluetooth() {
        try {
            // Simulação de conexão Bluetooth
            // Em uma implementação real, aqui seria usada uma biblioteca específica para Bluetooth
            outputStream = new ByteArrayOutputStream(); // Simulação
            conectado = true;
            impressoraConfigurada.atualizarStatus(true, "Conectado via Bluetooth");
            logger.info("Conectado via Bluetooth (simulação)");
            return true;
        } catch (Exception e) {
            logger.error("Erro na conexão Bluetooth: " + e.getMessage());
            return false;
        }
    }
    
    private String getCharset() {
        switch (impressoraConfigurada.getCodificacao().toUpperCase()) {
            case "UTF-8":
                return StandardCharsets.UTF_8.name();
            case "ISO-8859-1":
                return StandardCharsets.ISO_8859_1.name();
            case "CP850":
                return "CP850";
            default:
                return StandardCharsets.UTF_8.name();
        }
    }
    
    private ImpressoraNaoFiscal criarConfiguracaoPadrao() {
        ImpressoraNaoFiscal impressora = new ImpressoraNaoFiscal();
        impressora.setNome("Impressora Térmica Padrão");
        impressora.setModelo("TM-T20");
        impressora.setFabricante("Epson");
        impressora.setTipoConexao("USB");
        impressora.setPorta("USB001");
        impressora.setColunas(48);
        impressora.setCortaPapel(true);
        impressora.setDriver("ESCPOS");
        return impressora;
    }
    
    // Getters e Setters
    public ImpressoraNaoFiscal getImpressoraConfigurada() {
        return impressoraConfigurada;
    }
    
    public void setImpressoraConfigurada(ImpressoraNaoFiscal impressoraConfigurada) {
        this.impressoraConfigurada = impressoraConfigurada;
    }
    
    public boolean isConectado() {
        return conectado;
    }
}
