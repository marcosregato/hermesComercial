package com.br.hermescomercial.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Serviço de log e auditoria de ações do usuário
 * @author Hermes Comercial
 * @version 2.8.0
 */
public class AuditoriaService {
    
    private static AuditoriaService instance;
    private final List<RegistroAuditoria> registros;
    
    public enum TipoAcao {
        LOGIN,           // Login no sistema
        LOGOUT,          // Logout do sistema
        CRIACAO,         // Criação de registro
        ATUALIZACAO,     // Atualização de registro
        EXCLUSAO,        // Exclusão de registro
        VENDA,           // Venda realizada
        CANCELAMENTO,    // Cancelamento de venda
        EXPORTACAO,      // Exportação de dados
        IMPORTACAO,      // Importação de dados
        RELATORIO,       // Geração de relatório
        CONFIGURACAO,    // Alteração de configuração
        ERRO,            // Erro do sistema
        ACESSO_NEGADO    // Acesso não autorizado
    }
    
    public enum NivelAuditoria {
        INFO,     // Informação geral
        WARNING,  // Alerta
        ERROR,    // Erro
        CRITICAL  // Erro crítico
    }
    
    public static class RegistroAuditoria {
        private String id;
        private LocalDateTime dataHora;
        private String usuario;
        private TipoAcao tipoAcao;
        private NivelAuditoria nivel;
        private String modulo;
        private String descricao;
        private String detalhes;
        private String enderecoIP;
        private String estacaoTrabalho;
        private Map<String, Object> dadosAdicionais;
        
        public RegistroAuditoria(String usuario, TipoAcao tipoAcao, NivelAuditoria nivel, 
                                String modulo, String descricao) {
            this.id = generateId();
            this.dataHora = LocalDateTime.now();
            this.usuario = usuario;
            this.tipoAcao = tipoAcao;
            this.nivel = nivel;
            this.modulo = modulo;
            this.descricao = descricao;
            this.dadosAdicionais = new HashMap<>();
        }
        
        private String generateId() {
            return "AUD_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
        }
        
        // Getters e setters
        public String getId() { return id; }
        public LocalDateTime getDataHora() { return dataHora; }
        public String getUsuario() { return usuario; }
        public TipoAcao getTipoAcao() { return tipoAcao; }
        public NivelAuditoria getNivel() { return nivel; }
        public String getModulo() { return modulo; }
        public String getDescricao() { return descricao; }
        public String getDetalhes() { return detalhes; }
        public void setDetalhes(String detalhes) { this.detalhes = detalhes; }
        public String getEnderecoIP() { return enderecoIP; }
        public void setEnderecoIP(String enderecoIP) { this.enderecoIP = enderecoIP; }
        public String getEstacaoTrabalho() { return estacaoTrabalho; }
        public void setEstacaoTrabalho(String estacaoTrabalho) { this.estacaoTrabalho = estacaoTrabalho; }
        public Map<String, Object> getDadosAdicionais() { return dadosAdicionais; }
        
        public void adicionarDado(String chave, Object valor) {
            dadosAdicionais.put(chave, valor);
        }
    }
    
    private AuditoriaService() {
        this.registros = new ArrayList<>();
    }
    
    public static synchronized AuditoriaService getInstance() {
        if (instance == null) {
            instance = new AuditoriaService();
        }
        return instance;
    }
    
    /**
     * Registra ação do usuário
     */
    public void registrarAcao(String usuario, TipoAcao tipoAcao, NivelAuditoria nivel, 
                            String modulo, String descricao) {
        RegistroAuditoria registro = new RegistroAuditoria(usuario, tipoAcao, nivel, modulo, descricao);
        
        // Adicionar informações do sistema
        registro.setEnderecoIP(getEnderecoIPAtual());
        registro.setEstacaoTrabalho(getNomeEstacaoTrabalho());
        
        synchronized (registros) {
            registros.add(registro);
            
            // Manter apenas os últimos 10.000 registros
            if (registros.size() > 10000) {
                registros.remove(0);
            }
        }
        
        // Para ações críticas, registrar em log persistente (simulação)
        if (nivel == NivelAuditoria.CRITICAL || nivel == NivelAuditoria.ERROR) {
            registrarLogPersistente(registro);
        }
    }
    
    /**
     * Registra login de usuário
     */
    public void registrarLogin(String usuario, String enderecoIP) {
        RegistroAuditoria registro = new RegistroAuditoria(usuario, TipoAcao.LOGIN, NivelAuditoria.INFO, 
                                                         "AUTENTICACAO", "Usuário fez login no sistema");
        registro.setEnderecoIP(enderecoIP);
        registro.setEstacaoTrabalho(getNomeEstacaoTrabalho());
        
        synchronized (registros) {
            registros.add(registro);
        }
    }
    
    /**
     * Registra logout de usuário
     */
    public void registrarLogout(String usuario) {
        registrarAcao(usuario, TipoAcao.LOGOUT, NivelAuditoria.INFO, "AUTENTICACAO", "Usuário fez logout do sistema");
    }
    
    /**
     * Registra venda realizada
     */
    public void registrarVenda(String usuario, String idVenda, BigDecimal valor, String cliente) {
        RegistroAuditoria registro = new RegistroAuditoria(usuario, TipoAcao.VENDA, NivelAuditoria.INFO, 
                                                         "PDV", "Venda realizada");
        registro.setDetalhes("ID: " + idVenda + ", Valor: R$ " + valor + ", Cliente: " + cliente);
        registro.adicionarDado("idVenda", idVenda);
        registro.adicionarDado("valor", valor);
        registro.adicionarDado("cliente", cliente);
        
        synchronized (registros) {
            registros.add(registro);
        }
    }
    
    /**
     * Registra cancelamento de venda
     */
    public void registrarCancelamentoVenda(String usuario, String idVenda, String motivo) {
        RegistroAuditoria registro = new RegistroAuditoria(usuario, TipoAcao.CANCELAMENTO, NivelAuditoria.WARNING, 
                                                         "PDV", "Venda cancelada");
        registro.setDetalhes("ID: " + idVenda + ", Motivo: " + motivo);
        registro.adicionarDado("idVenda", idVenda);
        registro.adicionarDado("motivo", motivo);
        
        synchronized (registros) {
            registros.add(registro);
        }
    }
    
    /**
     * Registra alteração de dados
     */
    public void registrarAlteracao(String usuario, String modulo, String tipoRegistro, String idRegistro, 
                                 Map<String, Object> valoresAntigos, Map<String, Object> valoresNovos) {
        RegistroAuditoria registro = new RegistroAuditoria(usuario, TipoAcao.ATUALIZACAO, NivelAuditoria.INFO, 
                                                         modulo, "Alteração de " + tipoRegistro);
        registro.setDetalhes("Registro: " + idRegistro);
        registro.adicionarDado("tipoRegistro", tipoRegistro);
        registro.adicionarDado("idRegistro", idRegistro);
        registro.adicionarDado("valoresAntigos", valoresAntigos);
        registro.adicionarDado("valoresNovos", valoresNovos);
        
        synchronized (registros) {
            registros.add(registro);
        }
    }
    
    /**
     * Registra erro do sistema
     */
    public void registrarErro(String usuario, String modulo, String erro, String detalhes) {
        RegistroAuditoria registro = new RegistroAuditoria(usuario, TipoAcao.ERRO, NivelAuditoria.ERROR, 
                                                         modulo, "Erro do sistema");
        registro.setDetalhes("Erro: " + erro + "\nDetalhes: " + detalhes);
        registro.adicionarDado("erro", erro);
        registro.adicionarDado("detalhes", detalhes);
        
        synchronized (registros) {
            registros.add(registro);
        }
    }
    
    /**
     * Registra acesso negado
     */
    public void registrarAcessoNegado(String usuario, String modulo, String recurso) {
        RegistroAuditoria registro = new RegistroAuditoria(usuario, TipoAcao.ACESSO_NEGADO, NivelAuditoria.WARNING, 
                                                         modulo, "Tentativa de acesso não autorizado");
        registro.setDetalhes("Recurso: " + recurso);
        registro.adicionarDado("recurso", recurso);
        
        synchronized (registros) {
            registros.add(registro);
        }
    }
    
    /**
     * Busca registros por período
     */
    public List<RegistroAuditoria> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        List<RegistroAuditoria> resultado = new ArrayList<>();
        
        synchronized (registros) {
            for (RegistroAuditoria registro : registros) {
                if (!registro.getDataHora().isBefore(inicio) && !registro.getDataHora().isAfter(fim)) {
                    resultado.add(registro);
                }
            }
        }
        
        return resultado;
    }
    
    /**
     * Busca registros por usuário
     */
    public List<RegistroAuditoria> buscarPorUsuario(String usuario) {
        List<RegistroAuditoria> resultado = new ArrayList<>();
        
        synchronized (registros) {
            for (RegistroAuditoria registro : registros) {
                if (registro.getUsuario().equals(usuario)) {
                    resultado.add(registro);
                }
            }
        }
        
        return resultado;
    }
    
    /**
     * Busca registros por tipo de ação
     */
    public List<RegistroAuditoria> buscarPorTipoAcao(TipoAcao tipoAcao) {
        List<RegistroAuditoria> resultado = new ArrayList<>();
        
        synchronized (registros) {
            for (RegistroAuditoria registro : registros) {
                if (registro.getTipoAcao() == tipoAcao) {
                    resultado.add(registro);
                }
            }
        }
        
        return resultado;
    }
    
    /**
     * Busca registros por nível
     */
    public List<RegistroAuditoria> buscarPorNivel(NivelAuditoria nivel) {
        List<RegistroAuditoria> resultado = new ArrayList<>();
        
        synchronized (registros) {
            for (RegistroAuditoria registro : registros) {
                if (registro.getNivel() == nivel) {
                    resultado.add(registro);
                }
            }
        }
        
        return resultado;
    }
    
    /**
     * Obtém estatísticas de auditoria
     */
    public Map<String, Object> getEstatisticasAuditoria() {
        Map<String, Object> estatisticas = new HashMap<>();
        
        synchronized (registros) {
            int totalRegistros = registros.size();
            Map<TipoAcao, Integer> contagemPorAcao = new HashMap<>();
            Map<NivelAuditoria, Integer> contagemPorNivel = new HashMap<>();
            Map<String, Integer> contagemPorUsuario = new HashMap<>();
            Map<String, Integer> contagemPorModulo = new HashMap<>();
            
            for (RegistroAuditoria registro : registros) {
                contagemPorAcao.merge(registro.getTipoAcao(), 1, Integer::sum);
                contagemPorNivel.merge(registro.getNivel(), 1, Integer::sum);
                contagemPorUsuario.merge(registro.getUsuario(), 1, Integer::sum);
                contagemPorModulo.merge(registro.getModulo(), 1, Integer::sum);
            }
            
            estatisticas.put("totalRegistros", totalRegistros);
            estatisticas.put("contagemPorAcao", contagemPorAcao);
            estatisticas.put("contagemPorNivel", contagemPorNivel);
            estatisticas.put("contagemPorUsuario", contagemPorUsuario);
            estatisticas.put("contagemPorModulo", contagemPorModulo);
            
            // Últimas 24 horas
            LocalDateTime limite = LocalDateTime.now().minusHours(24);
            long ultimas24Horas = registros.stream()
                .filter(r -> r.getDataHora().isAfter(limite))
                .count();
            estatisticas.put("ultimas24Horas", ultimas24Horas);
            
            // Última atividade
            if (!registros.isEmpty()) {
                RegistroAuditoria ultimo = registros.get(registros.size() - 1);
                estatisticas.put("ultimaAtividade", ultimo.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
                estatisticas.put("ultimoUsuario", ultimo.getUsuario());
                estatisticas.put("ultimaAcao", ultimo.getDescricao());
            }
        }
        
        return estatisticas;
    }
    
    /**
     * Obtém atividades suspeitas
     */
    public List<RegistroAuditoria> getAtividadesSuspeitas() {
        List<RegistroAuditoria> suspeitas = new ArrayList<>();
        
        synchronized (registros) {
            for (RegistroAuditoria registro : registros) {
                // Acessos negados
                if (registro.getTipoAcao() == TipoAcao.ACESSO_NEGADO) {
                    suspeitas.add(registro);
                }
                
                // Múltiplogins em curto período
                if (registro.getTipoAcao() == TipoAcao.LOGIN) {
                    // Verificar se houve múltiplos logs recentes
                    long logsRecentes = registros.stream()
                        .filter(r -> r.getUsuario().equals(registro.getUsuario()) &&
                                   r.getTipoAcao() == TipoAcao.LOGIN &&
                                   r.getDataHora().isAfter(registro.getDataHora().minusMinutes(5)))
                        .count();
                    
                    if (logsRecentes > 3) {
                        suspeitas.add(registro);
                    }
                }
                
                // Cancelamentos frequentes
                if (registro.getTipoAcao() == TipoAcao.CANCELAMENTO) {
                    long cancelamentosRecentes = registros.stream()
                        .filter(r -> r.getUsuario().equals(registro.getUsuario()) &&
                                   r.getTipoAcao() == TipoAcao.CANCELAMENTO &&
                                   r.getDataHora().isAfter(registro.getDataHora().minusHours(1)))
                        .count();
                    
                    if (cancelamentosRecentes > 5) {
                        suspeitas.add(registro);
                    }
                }
            }
        }
        
        return suspeitas;
    }
    
    /**
     * Limpa registros antigos
     */
    public void limparRegistrosAntigos(int dias) {
        LocalDateTime limite = LocalDateTime.now().minusDays(dias);
        
        synchronized (registros) {
            registros.removeIf(registro -> registro.getDataHora().isBefore(limite));
        }
    }
    
    /**
     * Gera relatório de auditoria
     */
    public String gerarRelatorioAuditoria(LocalDateTime inicio, LocalDateTime fim) {
        StringBuilder relatorio = new StringBuilder();
        
        relatorio.append("RELATÓRIO DE AUDITORIA\n");
        relatorio.append("Período: ").append(inicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                .append(" a ").append(fim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n");
        relatorio.append("Data: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))).append("\n");
        relatorio.append("\n" + "=".repeat(80) + "\n\n");
        
        List<RegistroAuditoria> registrosPeriodo = buscarPorPeriodo(inicio, fim);
        
        if (registrosPeriodo.isEmpty()) {
            relatorio.append("Nenhum registro encontrado no período.\n");
        } else {
            relatorio.append("Total de registros: ").append(registrosPeriodo.size()).append("\n\n");
            
            // Agrupar por tipo
            Map<TipoAcao, List<RegistroAuditoria>> agrupado = new HashMap<>();
            for (RegistroAuditoria registro : registrosPeriodo) {
                agrupado.computeIfAbsent(registro.getTipoAcao(), k -> new ArrayList<>()).add(registro);
            }
            
            for (Map.Entry<TipoAcao, List<RegistroAuditoria>> entry : agrupado.entrySet()) {
                relatorio.append("=== ").append(entry.getKey()).append(" (").append(entry.getValue().size()).append(") ===\n");
                
                for (RegistroAuditoria registro : entry.getValue()) {
                    relatorio.append(registro.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
                           .append(" - ").append(registro.getUsuario())
                           .append(" - ").append(registro.getDescricao());
                    
                    if (registro.getDetalhes() != null) {
                        relatorio.append(" [").append(registro.getDetalhes()).append("]");
                    }
                    
                    relatorio.append("\n");
                }
                
                relatorio.append("\n");
            }
        }
        
        relatorio.append("\n" + "=".repeat(80));
        relatorio.append("\nFIM DO RELATÓRIO\n");
        
        return relatorio.toString();
    }
    
    // Métodos auxiliares
    private String getEnderecoIPAtual() {
        // Simulação - em implementação real, obteria o IP real
        return "192.168.1.100";
    }
    
    private String getNomeEstacaoTrabalho() {
        // Simulação - em implementação real, obteria o nome da máquina
        return "ESTACAO_" + System.getProperty("user.name", "USER");
    }
    
    private void registrarLogPersistente(RegistroAuditoria registro) {
        // Simulação - em implementação real, salvaria em arquivo ou banco persistente
        System.out.println("[AUDITORIA PERSISTENTE] " + registro.getDataHora() + 
                          " - " + registro.getUsuario() + " - " + registro.getDescricao());
    }
}
