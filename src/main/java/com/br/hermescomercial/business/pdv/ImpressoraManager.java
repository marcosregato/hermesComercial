package com.br.hermescomercial.business.pdv;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ImpressoraManager {
    
    private static final Logger logger = LogManager.getLogger(ImpressoraManager.class);
    
    // Configurações da impressora
    private String nomeImpressora;
    private int colunasPorLinha;
    private int caracteresPorLinha;
    private boolean modoDebug;
    
    // Sequências de controle ESC/POS
    private static final byte[] ESC = new byte[]{0x1B};
    private static final byte[] GS = new byte[]{0x1D};
    private static final byte[] LF = new byte[]{0x0A};
    private static final byte[] CR = new byte[]{0x0D};
    private static final byte[] INIT = new byte[]{0x1B, 0x40};
    private static final byte[] CUT_PAPER = new byte[]{0x1B, 0x69};
    private static final byte[] BOLD_ON = new byte[]{0x1B, 0x45, 0x01};
    private static final byte[] BOLD_OFF = new byte[]{0x1B, 0x45, 0x00};
    private static final byte[] ALIGN_CENTER = new byte[]{0x1B, 0x61, 0x01};
    private static final byte[] ALIGN_LEFT = new byte[]{0x1B, 0x61, 0x00};
    private static final byte[] ALIGN_RIGHT = new byte[]{0x1B, 0x61, 0x02};

    public ImpressoraManager() {
        this.nomeImpressora = "Default";
        this.colunasPorLinha = 48;
        this.caracteresPorLinha = 48;
        this.modoDebug = false;
    }

    /**
     * Imprime cupom fiscal
     * @param cupom Cupom a ser impresso
     * @return true se impresso com sucesso
     */
    public boolean imprimirCupom(String cupom) {
        try {
            if (cupom == null || cupom.trim().isEmpty()) {
                logger.error("Cupom vazio para impressão");
                return false;
            }

            if (modoDebug) {
                logger.info("MODO DEBUG - Cupom a imprimir:\n" + cupom);
                return true;
            }

            byte[] dadosImpressao = formatarDadosImpressao(cupom);
            
            return enviarParaImpressora(dadosImpressao);

        } catch (Exception e) {
            logger.error("Erro ao imprimir cupom: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Imprime texto simples
     * @param texto Texto a ser impresso
     * @return true se impresso com sucesso
     */
    public boolean imprimirTexto(String texto) {
        try {
            if (modoDebug) {
                logger.info("MODO DEBUG - Texto a imprimir:\n" + texto);
                return true;
            }

            byte[] dados = texto.getBytes(StandardCharsets.UTF_8);
            return enviarParaImpressora(dados);

        } catch (Exception e) {
            logger.error("Erro ao imprimir texto: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Imprime relatório de vendas do dia
     * @param dadosRelatorio Dados do relatório
     * @return true se impresso com sucesso
     */
    public boolean imprimirRelatorioVendas(String dadosRelatorio) {
        try {
            StringBuilder relatorio = new StringBuilder();
            
            // Adicionar cabeçalho do relatório
            relatorio.append("RELATÓRIO DE VENDAS DO DIA\n");
            relatorio.append("================================\n");
            relatorio.append(dadosRelatorio);
            relatorio.append("================================\n");
            relatorio.append("FIM DO RELATÓRIO\n");

            return imprimirCupom(relatorio.toString());

        } catch (Exception e) {
            logger.error("Erro ao imprimir relatório: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Imprime teste de impressora
     * @return true se impresso com sucesso
     */
    public boolean imprimirTeste() {
        try {
            StringBuilder teste = new StringBuilder();
            
            teste.append("TESTE DE IMPRESSORA\n");
            teste.append("================================\n");
            teste.append("Data: ").append(java.time.LocalDateTime.now()).append("\n");
            teste.append("Terminal: ").append(nomeImpressora).append("\n");
            teste.append("Caracteres por linha: ").append(caracteresPorLinha).append("\n");
            teste.append("123456789012345678901234567890123456789012345678901234567890\n");
            teste.append("ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ\n");
            teste.append("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz\n");
            teste.append("!@#$%^&*()_+-=[]{}|;':\",./<>?`~\n");
            teste.append("================================\n");
            teste.append("IMPRESSÃO OK\n");
            teste.append("================================\n");

            return imprimirCupom(teste.toString());

        } catch (Exception e) {
            logger.error("Erro ao imprimir teste: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Abre gaveta de dinheiro
     * @return true se aberta com sucesso
     */
    public boolean abrirGaveta() {
        try {
            if (modoDebug) {
                logger.info("MODO DEBUG - Abrindo gaveta de dinheiro");
                return true;
            }

            // Comando ESC/POS para abrir gaveta (pin 2)
            byte[] comandoGaveta = new byte[]{0x1B, 0x70, 0x00, 0x19, (byte) 0xFA};
            
            return enviarParaImpressora(comandoGaveta);

        } catch (Exception e) {
            logger.error("Erro ao abrir gaveta: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Formata dados para impressão ESC/POS
     */
    private byte[] formatarDadosImpressao(String cupom) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            
            // Inicializar impressora
            baos.write(INIT);
            
            // Processar cada linha do cupom
            String[] linhas = cupom.split("\n");
            
            for (String linha : linhas) {
                linha = processarLinha(linha);
                
                // Verificar se é linha de cabeçalho (centralizada)
                if (linha.contains("CUPOM") || linha.contains("FISCAL") || 
                    linha.contains("CANCELAMENTO") || linha.contains("RELATÓRIO")) {
                    baos.write(ALIGN_CENTER);
                } else if (linha.contains("TOTAL:") || linha.contains("PAGAMENTO")) {
                    baos.write(BOLD_ON);
                } else {
                    baos.write(ALIGN_LEFT);
                }
                
                // Adicionar linha
                baos.write(linha.getBytes(StandardCharsets.UTF_8));
                baos.write(LF);
                
                // Desativar negrito se estava ativo
                if (linha.contains("TOTAL:") || linha.contains("PAGAMENTO")) {
                    baos.write(BOLD_OFF);
                }
            }
            
            // Cortar papel
            baos.write(CUT_PAPER);
            
            return baos.toByteArray();

        } catch (Exception e) {
            logger.error("Erro ao formatar dados: " + e.getMessage(), e);
            return cupom.getBytes(StandardCharsets.UTF_8);
        }
    }

    /**
     * Processa linha para formatação
     */
    private String processarLinha(String linha) {
        if (linha == null) return "";
        
        // Limitar tamanho da linha
        if (linha.length() > caracteresPorLinha) {
            linha = linha.substring(0, caracteresPorLinha);
        }
        
        // Remover caracteres especiais que podem causar problemas
        linha = linha.replace("ç", "c").replace("Ç", "C");
        linha = linha.replace("ã", "a").replace("Ã", "A");
        linha = linha.replace("õ", "o").replace("Õ", "O");
        linha = linha.replace("á", "a").replace("Á", "A");
        linha = linha.replace("é", "e").replace("É", "E");
        linha = linha.replace("í", "i").replace("Í", "I");
        linha = linha.replace("ó", "o").replace("Ó", "O");
        linha = linha.replace("ú", "u").replace("Ú", "U");
        
        return linha;
    }

    /**
     * Envia dados para impressora
     */
    private boolean enviarParaImpressora(byte[] dados) {
        try {
            // Tentar imprimir usando comando do sistema (para Windows/Linux)
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                return imprimirWindows(dados);
            } else {
                return imprimirLinux(dados);
            }

        } catch (Exception e) {
            logger.error("Erro ao enviar para impressora: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Imprime no Windows
     */
    private boolean imprimirWindows(byte[] dados) {
        try {
            // Usar comando COPY para enviar para porta LPT1 ou USB
            String comando = "cmd /c echo " + new String(dados) + " > LPT1";
            Process process = Runtime.getRuntime().exec(comando);
            
            return process.waitFor() == 0;

        } catch (Exception e) {
            logger.error("Erro ao imprimir no Windows: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Imprime no Linux
     */
    private boolean imprimirLinux(byte[] dados) {
        try {
            // Usar lp para enviar para impressora
            String comando = "lp -d " + nomeImpressora;
            Process process = Runtime.getRuntime().exec(comando);
            
            try (OutputStream os = process.getOutputStream()) {
                os.write(dados);
                os.flush();
            }
            
            return process.waitFor() == 0;

        } catch (Exception e) {
            logger.error("Erro ao imprimir no Linux: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Verifica status da impressora
     */
    public boolean verificarStatusImpressora() {
        try {
            if (modoDebug) {
                logger.info("MODO DEBUG - Impressora OK");
                return true;
            }

            // Simulação de verificação - em sistema real, implementar comunicação com impressora
            return true;

        } catch (Exception e) {
            logger.error("Erro ao verificar status da impressora: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Lista impressoras disponíveis
     */
    public List<String> listarImpressorasDisponiveis() {
        // Implementar listagem de impressoras conforme sistema operacional
        return List.of("Default", "USB001", "LPT1", "PDF");
    }

    // Getters e Setters
    public String getNomeImpressora() {
        return nomeImpressora;
    }

    public void setNomeImpressora(String nomeImpressora) {
        this.nomeImpressora = nomeImpressora;
    }

    public int getColunasPorLinha() {
        return colunasPorLinha;
    }

    public void setColunasPorLinha(int colunasPorLinha) {
        this.colunasPorLinha = colunasPorLinha;
        this.caracteresPorLinha = colunasPorLinha;
    }

    public boolean isModoDebug() {
        return modoDebug;
    }

    public void setModoDebug(boolean modoDebug) {
        this.modoDebug = modoDebug;
    }
}
