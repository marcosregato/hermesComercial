package com.br.hermescomercial.service;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Serviço de backup automático de dados
 * @author Hermes Comercial
 * @version 2.8.0
 */
public class BackupService {
    
    private static BackupService instance;
    private final String diretorioBackup;
    private final String diretorioDados;
    private final int maxBackups;
    
    public enum TipoBackup {
        COMPLETO,        // Backup completo de todos os dados
        INCREMENTAL,     // Backup apenas das alterações
        DIFERENCIAL,    // Backup apenas dos dados alterados desde o último backup completo
        AUTOMATICO       // Backup agendado automaticamente
    }
    
    public enum StatusBackup {
        PENDENTE,        // Aguardando execução
        EM_ANDAMENTO,    // Backup em execução
        CONCLUIDO,       // Backup concluído com sucesso
        ERRO,            // Erro durante o backup
        CANCELADO        // Backup cancelado
    }
    
    public static class RegistroBackup {
        private TipoBackup tipo;
        private LocalDateTime dataHora;
        private StatusBackup status;
        private String caminhoArquivo;
        private long tamanhoArquivo;
        private int quantidadeArquivos;
        private String mensagem;
        private long duracaoMilissegundos;
        
        public RegistroBackup(TipoBackup tipo) {
            this.id = generateId();
            this.tipo = tipo;
            this.dataHora = LocalDateTime.now();
            this.status = StatusBackup.PENDENTE;
        }
        
        private String generateId() {
            return "BK_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
        }
        
        // ID gerado mas não utilizado - mantido para referência futura
        @SuppressWarnings("unused")
        private String id = generateId();
        
        // Getters e setters
        public TipoBackup getTipo() { return tipo; }
        public void setTipo(TipoBackup tipo) { this.tipo = tipo; }
        public LocalDateTime getDataHora() { return dataHora; }
        public StatusBackup getStatus() { return status; }
        public void setStatus(StatusBackup status) { this.status = status; }
        public String getCaminhoArquivo() { return caminhoArquivo; }
        public void setCaminhoArquivo(String caminhoArquivo) { this.caminhoArquivo = caminhoArquivo; }
        public long getTamanhoArquivo() { return tamanhoArquivo; }
        public void setTamanhoArquivo(long tamanhoArquivo) { this.tamanhoArquivo = tamanhoArquivo; }
        public int getQuantidadeArquivos() { return quantidadeArquivos; }
        public void setQuantidadeArquivos(int quantidadeArquivos) { this.quantidadeArquivos = quantidadeArquivos; }
        public String getMensagem() { return mensagem; }
        public void setMensagem(String mensagem) { this.mensagem = mensagem; }
        public long getDuracaoMilissegundos() { return duracaoMilissegundos; }
        public void setDuracaoMilissegundos(long duracaoMilissegundos) { this.duracaoMilissegundos = duracaoMilissegundos; }
    }
    
    private BackupService() {
        this.diretorioBackup = System.getProperty("user.home") + "/hermes-backup";
        this.diretorioDados = System.getProperty("user.home") + "/hermes-dados";
        this.maxBackups = 10;
        
        // Criar diretórios se não existirem
        criarDiretorios();
    }
    
    public static synchronized BackupService getInstance() {
        if (instance == null) {
            instance = new BackupService();
        }
        return instance;
    }
    
    /**
     * Executa backup completo
     */
    public RegistroBackup executarBackupCompleto() {
        RegistroBackup registro = new RegistroBackup(TipoBackup.COMPLETO);
        registro.setStatus(StatusBackup.EM_ANDAMENTO);
        
        long inicio = System.currentTimeMillis();
        
        try {
            String nomeArquivo = "backup_completo_" + 
                              LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".zip";
            String caminhoCompleto = diretorioBackup + File.separator + nomeArquivo;
            
            // Criar arquivo ZIP
            try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(caminhoCompleto))) {
                int arquivosProcessados = 0;
                
                // Adicionar arquivos do diretório de dados
                arquivosProcessados += adicionarDiretorioAoZip(zipOut, Paths.get(diretorioDados), "");
                
                // Adicionar arquivos de configuração
                Path dirConfig = Paths.get(System.getProperty("user.home") + "/hermes-config");
                if (Files.exists(dirConfig)) {
                    arquivosProcessados += adicionarDiretorioAoZip(zipOut, dirConfig, "config/");
                }
                
                registro.setQuantidadeArquivos(arquivosProcessados);
            }
            
            // Verificar tamanho do arquivo
            File arquivoBackup = new File(caminhoCompleto);
            registro.setTamanhoArquivo(arquivoBackup.length());
            registro.setCaminhoArquivo(caminhoCompleto);
            registro.setStatus(StatusBackup.CONCLUIDO);
            registro.setMensagem("Backup completo executado com sucesso");
            
            // Limpar backups antigos
            limparBackupsAntigos();
            
        } catch (Exception e) {
            registro.setStatus(StatusBackup.ERRO);
            registro.setMensagem("Erro no backup: " + e.getMessage());
        }
        
        registro.setDuracaoMilissegundos(System.currentTimeMillis() - inicio);
        
        return registro;
    }
    
    /**
     * Executa backup incremental
     */
    public RegistroBackup executarBackupIncremental() {
        RegistroBackup registro = new RegistroBackup(TipoBackup.INCREMENTAL);
        registro.setStatus(StatusBackup.EM_ANDAMENTO);
        
        long inicio = System.currentTimeMillis();
        
        try {
            String nomeArquivo = "backup_incremental_" + 
                              LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".zip";
            String caminhoCompleto = diretorioBackup + File.separator + nomeArquivo;
            
            // Obter data do último backup completo
            LocalDateTime dataUltimoBackup = getDataUltimoBackupCompleto();
            
            try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(caminhoCompleto))) {
                int arquivosProcessados = 0;
                
                // Adicionar apenas arquivos modificados desde o último backup
                arquivosProcessados += adicionarArquivosModificados(zipOut, Paths.get(diretorioDados), "", dataUltimoBackup);
                
                registro.setQuantidadeArquivos(arquivosProcessados);
            }
            
            File arquivoBackup = new File(caminhoCompleto);
            registro.setTamanhoArquivo(arquivoBackup.length());
            registro.setCaminhoArquivo(caminhoCompleto);
            registro.setStatus(StatusBackup.CONCLUIDO);
            registro.setMensagem("Backup incremental executado com sucesso");
            
        } catch (Exception e) {
            registro.setStatus(StatusBackup.ERRO);
            registro.setMensagem("Erro no backup incremental: " + e.getMessage());
        }
        
        registro.setDuracaoMilissegundos(System.currentTimeMillis() - inicio);
        
        return registro;
    }
    
    /**
     * Restaura backup
     */
    public boolean restaurarBackup(String caminhoBackup, String diretorioDestino) {
        try {
            File arquivoBackup = new File(caminhoBackup);
            
            if (!arquivoBackup.exists()) {
                throw new FileNotFoundException("Arquivo de backup não encontrado: " + caminhoBackup);
            }
            
            // Verificar se é um arquivo ZIP válido
            if (!caminhoBackup.toLowerCase().endsWith(".zip")) {
                throw new IllegalArgumentException("Arquivo de backup deve estar no formato ZIP");
            }
            
            // Descompactar arquivo
            descompactarZip(caminhoBackup, diretorioDestino);
            
            return true;
            
        } catch (Exception e) {
            System.err.println("Erro na restauração: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Lista backups existentes
     */
    public List<RegistroBackup> listarBackups() {
        List<RegistroBackup> backups = new ArrayList<>();
        
        File diretorio = new File(diretorioBackup);
        if (!diretorio.exists()) {
            return backups;
        }
        
        File[] arquivos = diretorio.listFiles((dir, name) -> name.toLowerCase().endsWith(".zip"));
        
        if (arquivos != null) {
            for (File arquivo : arquivos) {
                RegistroBackup registro = new RegistroBackup(TipoBackup.COMPLETO);
                registro.setCaminhoArquivo(arquivo.getAbsolutePath());
                registro.setTamanhoArquivo(arquivo.length());
                registro.setStatus(StatusBackup.CONCLUIDO);
                
                // Extrair data do nome do arquivo
                String nome = arquivo.getName();
                if (nome.contains("completo")) {
                    registro.setTipo(TipoBackup.COMPLETO);
                } else if (nome.contains("incremental")) {
                    registro.setTipo(TipoBackup.INCREMENTAL);
                }
                
                backups.add(registro);
            }
        }
        
        // Ordenar por data (mais recente primeiro)
        backups.sort((b1, b2) -> b2.getDataHora().compareTo(b1.getDataHora()));
        
        return backups;
    }
    
    /**
     * Obtém estatísticas dos backups
     */
    public Map<String, Object> getEstatisticasBackup() {
        Map<String, Object> estatisticas = new HashMap<>();
        
        List<RegistroBackup> backups = listarBackups();
        
        int totalBackups = backups.size();
        long espacoTotalOcupado = 0;
        int backupsCompletos = 0;
        int backupsIncrementais = 0;
        
        for (RegistroBackup backup : backups) {
            espacoTotalOcupado += backup.getTamanhoArquivo();
            
            if (backup.getTipo() == TipoBackup.COMPLETO) {
                backupsCompletos++;
            } else if (backup.getTipo() == TipoBackup.INCREMENTAL) {
                backupsIncrementais++;
            }
        }
        
        estatisticas.put("totalBackups", totalBackups);
        estatisticas.put("espacoTotalOcupado", formatarTamanho(espacoTotalOcupado));
        estatisticas.put("backupsCompletos", backupsCompletos);
        estatisticas.put("backupsIncrementais", backupsIncrementais);
        estatisticas.put("diretorioBackup", diretorioBackup);
        estatisticas.put("maxBackups", maxBackups);
        
        // Último backup
        if (!backups.isEmpty()) {
            RegistroBackup ultimo = backups.get(0);
            estatisticas.put("dataUltimoBackup", ultimo.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            estatisticas.put("tamanhoUltimoBackup", formatarTamanho(ultimo.getTamanhoArquivo()));
            estatisticas.put("statusUltimoBackup", ultimo.getStatus());
        }
        
        return estatisticas;
    }
    
    /**
     * Agenda backup automático
     */
    public void agendarBackupAutomatico(int hora, int minuto, TipoBackup tipo) {
        // Simulação - em implementação real, usaria um scheduler como Quartz
        System.out.println("Backup automático agendado para " + String.format("%02d:%02d", hora, minuto) + 
                          " - Tipo: " + tipo);
    }
    
    /**
     * Verifica integridade dos backups
     */
    public Map<String, Object> verificarIntegridadeBackups() {
        Map<String, Object> verificacao = new HashMap<>();
        
        List<RegistroBackup> backups = listarBackups();
        List<String> backupsCorrompidos = new ArrayList<>();
        List<String> backupsValidos = new ArrayList<>();
        
        for (RegistroBackup backup : backups) {
            try {
                File arquivo = new File(backup.getCaminhoArquivo());
                
                // Verificar se arquivo existe e pode ser lido
                if (arquivo.exists() && arquivo.canRead()) {
                    // Verificar se é um arquivo ZIP válido
                    if (isZipValido(backup.getCaminhoArquivo())) {
                        backupsValidos.add(arquivo.getName());
                    } else {
                        backupsCorrompidos.add(arquivo.getName());
                    }
                } else {
                    backupsCorrompidos.add(arquivo.getName());
                }
            } catch (Exception e) {
                backupsCorrompidos.add(backup.getCaminhoArquivo());
            }
        }
        
        verificacao.put("totalVerificados", backups.size());
        verificacao.put("backupsValidos", backupsValidos.size());
        verificacao.put("backupsCorrompidos", backupsCorrompidos.size());
        verificacao.put("listaCorrompidos", backupsCorrompidos);
        verificacao.put("percentualValidos", backups.size() > 0 ? 
                        (backupsValidos.size() * 100.0) / backups.size() : 0);
        
        return verificacao;
    }
    
    // Métodos auxiliares
    private void criarDiretorios() {
        try {
            Files.createDirectories(Paths.get(diretorioBackup));
            Files.createDirectories(Paths.get(diretorioDados));
        } catch (IOException e) {
            System.err.println("Erro ao criar diretórios: " + e.getMessage());
        }
    }
    
    private int adicionarDiretorioAoZip(ZipOutputStream zipOut, Path diretorio, String prefixo) throws IOException {
        int arquivosProcessados = 0;
        
        if (Files.exists(diretorio)) {
            Files.walk(diretorio)
                .filter(path -> !Files.isDirectory(path))
                .forEach(path -> {
                    try {
                        String entryName = prefixo + diretorio.relativize(path).toString();
                        zipOut.putNextEntry(new ZipEntry(entryName));
                        
                        Files.copy(path, zipOut);
                        zipOut.closeEntry();
                        
                    } catch (IOException e) {
                        System.err.println("Erro ao adicionar arquivo ao ZIP: " + path);
                    }
                });
            
            // Contar arquivos
            arquivosProcessados = (int) Files.walk(diretorio).filter(Files::isRegularFile).count();
        }
        
        return arquivosProcessados;
    }
    
    private int adicionarArquivosModificados(ZipOutputStream zipOut, Path diretorio, String prefixo, 
                                          LocalDateTime dataLimite) throws IOException {
        int arquivosProcessados = 0;
        
        if (Files.exists(diretorio)) {
            Files.walk(diretorio)
                .filter(path -> !Files.isDirectory(path))
                .filter(path -> {
                    try {
                        LocalDateTime dataModificacao = Files.getLastModifiedTime(path)
                                .toInstant()
                                .atZone(java.time.ZoneId.systemDefault())
                                .toLocalDateTime();
                        return dataModificacao.isAfter(dataLimite);
                    } catch (IOException e) {
                        return false;
                    }
                })
                .forEach(path -> {
                    try {
                        String entryName = prefixo + diretorio.relativize(path).toString();
                        zipOut.putNextEntry(new ZipEntry(entryName));
                        
                        Files.copy(path, zipOut);
                        zipOut.closeEntry();
                        
                    } catch (IOException e) {
                        System.err.println("Erro ao adicionar arquivo modificado ao ZIP: " + path);
                    }
                });
            
            arquivosProcessados = (int) Files.walk(diretorio)
                .filter(Files::isRegularFile)
                .filter(path -> {
                    try {
                        LocalDateTime dataModificacao = Files.getLastModifiedTime(path)
                                .toInstant()
                                .atZone(java.time.ZoneId.systemDefault())
                                .toLocalDateTime();
                        return dataModificacao.isAfter(dataLimite);
                    } catch (IOException e) {
                        return false;
                    }
                })
                .count();
        }
        
        return arquivosProcessados;
    }
    
    private void limparBackupsAntigos() {
        try {
            List<RegistroBackup> backups = listarBackups();
            
            if (backups.size() > maxBackups) {
                // Manter apenas os mais recentes
                for (int i = maxBackups; i < backups.size(); i++) {
                    File arquivoAntigo = new File(backups.get(i).getCaminhoArquivo());
                    if (arquivoAntigo.delete()) {
                        System.out.println("Backup antigo removido: " + arquivoAntigo.getName());
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao limpar backups antigos: " + e.getMessage());
        }
    }
    
    private LocalDateTime getDataUltimoBackupCompleto() {
        // Simulação - em implementação real, buscaria no banco ou logs
        return LocalDateTime.now().minusDays(7);
    }
    
    private void descompactarZip(String caminhoZip, String diretorioDestino) throws IOException {
        try (java.util.zip.ZipInputStream zipIn = new java.util.zip.ZipInputStream(new FileInputStream(caminhoZip))) {
            java.util.zip.ZipEntry entry = zipIn.getNextEntry();
            
            while (entry != null) {
                Path filePath = Paths.get(diretorioDestino, entry.getName());
                
                if (!entry.isDirectory()) {
                    Files.createDirectories(filePath.getParent());
                    Files.copy(zipIn, filePath, StandardCopyOption.REPLACE_EXISTING);
                }
                
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        }
    }
    
    private boolean isZipValido(String caminhoZip) {
        try (java.util.zip.ZipFile zipFile = new java.util.zip.ZipFile(caminhoZip)) {
            return true; // Se conseguir abrir, está válido
        } catch (IOException e) {
            return false;
        }
    }
    
    private String formatarTamanho(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
        return String.format("%.1f GB", bytes / (1024.0 * 1024.0 * 1024.0));
    }
}
