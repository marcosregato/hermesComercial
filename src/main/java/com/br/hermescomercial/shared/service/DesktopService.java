package com.br.hermescomercial.shared.service;

import com.br.hermescomercial.config.DesktopConfig;
import com.br.hermescomercial.config.DatabaseConfig;
import com.br.hermescomercial.connectionBD.ConnectionBD;
import com.br.hermescomercial.exception.SystemException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Serviço para gerenciamento de funcionalidades desktop
 * Inclui backup, configurações, inicialização e manutenção
 */
public class DesktopService {
    private static final Logger logger = LogManager.getLogger(DesktopService.class);
    
    private final DesktopConfig desktopConfig;
    private final DatabaseConfig databaseConfig;
    
    public DesktopService() {
        this.desktopConfig = DesktopConfig.getInstance();
        this.databaseConfig = DatabaseConfig.getInstance();
    }
    
    /**
     * Inicializa o ambiente desktop
     */
    public void initializeDesktop() {
        try {
            logger.info("Inicializando ambiente desktop...");
            
            // Verificar se é primeira execução
            if (desktopConfig.isFirstRun()) {
                setupFirstRun();
            }
            
            // Configurar banco de dados embedded
            setupEmbeddedDatabase();
            
            // Configurar logging para desktop
            setupDesktopLogging();
            
            // Verificar atualizações
            checkForUpdates();
            
            logger.info("Ambiente desktop inicializado com sucesso");
            
        } catch (Exception e) {
            logger.error("Erro na inicialização do desktop: {}", e.getMessage(), e);
            throw new SystemException("Falha na inicialização do ambiente desktop", e);
        }
    }
    
    /**
     * Configurações para primeira execução
     */
    private void setupFirstRun() {
        logger.info("Primeira execução detectada. Configurando ambiente...");
        
        try {
            // Criar banco de dados inicial
            createInitialDatabase();
            
            // Salvar configurações padrão
            desktopConfig.saveConfig();
            
            // Criar diretórios de relatórios
            createReportDirectories();
            
            logger.info("Configuração inicial concluída");
            
        } catch (Exception e) {
            logger.error("Erro na configuração inicial: {}", e.getMessage(), e);
            throw new SystemException("Falha na configuração inicial", e);
        }
    }
    
    /**
     * Configura banco de dados embedded SQLite
     */
    private void setupEmbeddedDatabase() {
        if (desktopConfig.isEmbeddedDbEnabled()) {
            try {
                // Atualizar configuração para SQLite
                databaseConfig.setDatabaseType(DatabaseConfig.DatabaseType.SQLITE);
                
                // Verificar se o banco existe
                String dbPath = desktopConfig.getEmbeddedDbPath();
                if (!Files.exists(Paths.get(dbPath))) {
                    logger.info("Criando banco de dados embedded: {}", dbPath);
                    createInitialDatabase();
                } else {
                    logger.info("Banco de dados embedded encontrado: {}", dbPath);
                }
                
                // Testar conexão
                if (ConnectionBD.testConnection()) {
                    logger.info("Conexão com banco embedded testada com sucesso");
                } else {
                    throw new SystemException("Falha no teste de conexão com banco embedded");
                }
                
            } catch (Exception e) {
                logger.error("Erro na configuração do banco embedded: {}", e.getMessage(), e);
                throw new SystemException("Falha na configuração do banco embedded", e);
            }
        }
    }
    
    /**
     * Cria banco de dados inicial
     */
    private void createInitialDatabase() {
        try (Connection conn = ConnectionBD.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Aqui você pode executar scripts SQL iniciais
            // Por enquanto, apenas verificamos a conexão
            logger.info("Banco de dados inicial verificado com sucesso");
            
        } catch (Exception e) {
            logger.error("Erro ao criar banco inicial: {}", e.getMessage(), e);
            throw new SystemException("Falha na criação do banco inicial", e);
        }
    }
    
    /**
     * Configura logging para ambiente desktop
     */
    private void setupDesktopLogging() {
        try {
            // Configurar Log4j para usar diretório de logs do desktop
            System.setProperty("log4j.configurationFile", 
                desktopConfig.getConfigDir() + File.separator + "log4j2.xml");
            
            logger.info("Logging desktop configurado");
            logger.debug("Diretório de logs: {}", desktopConfig.getLogDir());
            
        } catch (Exception e) {
            logger.warn("Erro na configuração do logging: {}", e.getMessage());
        }
    }
    
    /**
     * Verifica se há atualizações disponíveis
     */
    private void checkForUpdates() {
        // Implementar verificação de atualizações
        logger.info("Verificação de atualizações desativada (implementação futura)");
    }
    
    /**
     * Realiza backup automático
     */
    public void performAutoBackup() {
        if (!desktopConfig.isAutoBackupEnabled()) {
            return;
        }
        
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String backupPath = desktopConfig.getBackupDir() + File.separator + 
                              "backup_" + timestamp + ".db";
            
            // Copiar banco de dados atual
            Path source = Paths.get(desktopConfig.getEmbeddedDbPath());
            Path target = Paths.get(backupPath);
            
            if (Files.exists(source)) {
                Files.copy(source, target);
                logger.info("Backup automático realizado: {}", backupPath);
                
                // Limpar backups antigos
                cleanupOldBackups();
            } else {
                logger.warn("Arquivo de banco não encontrado para backup: {}", source);
            }
            
        } catch (Exception e) {
            logger.error("Erro no backup automático: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Limpa backups antigos
     */
    private void cleanupOldBackups() {
        try {
            File backupDir = new File(desktopConfig.getBackupDir());
            File[] backups = backupDir.listFiles((dir, name) -> name.startsWith("backup_") && name.endsWith(".db"));
            
            if (backups != null && backups.length > desktopConfig.getMaxBackupFiles()) {
                // Ordenar por data (mais antigos primeiro)
                java.util.Arrays.sort(backups, (f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()));
                
                // Remover excesso
                int toDelete = backups.length - desktopConfig.getMaxBackupFiles();
                for (int i = 0; i < toDelete; i++) {
                    if (backups[i].delete()) {
                        logger.debug("Backup antigo removido: {}", backups[i].getName());
                    }
                }
            }
            
        } catch (Exception e) {
            logger.error("Erro na limpeza de backups: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Cria diretórios de relatórios
     */
    private void createReportDirectories() {
        try {
            String reportsDir = desktopConfig.getReportsDir();
            String[] subDirs = {"vendas", "clientes", "produtos", "financeiro"};
            
            for (String subDir : subDirs) {
                Path dirPath = Paths.get(reportsDir, subDir);
                if (!Files.exists(dirPath)) {
                    Files.createDirectories(dirPath);
                    logger.debug("Diretório de relatório criado: {}", dirPath);
                }
            }
            
        } catch (Exception e) {
            logger.error("Erro na criação de diretórios de relatórios: {}", e.getMessage(), e);
        }
    }
    
    /**
     * Retorna informações do sistema desktop
     */
    public String getSystemInfo() {
        StringBuilder info = new StringBuilder();
        info.append(desktopConfig.getEnvironmentInfo()).append("\n\n");
        info.append("=== Status do Sistema ===\n");
        info.append("Primeira Execução: ").append(desktopConfig.isFirstRun() ? "Sim" : "Não").append("\n");
        info.append("Banco Embedded: ").append(desktopConfig.isEmbeddedDbEnabled() ? "Ativado" : "Desativado").append("\n");
        info.append("Conexão BD: ").append(ConnectionBD.testConnection() ? "OK" : "Falha").append("\n");
        info.append("Cache: ").append(desktopConfig.isCacheEnabled() ? "Ativado" : "Desativado").append("\n");
        info.append("Backup Auto: ").append(desktopConfig.isAutoBackupEnabled() ? "Ativado" : "Desativado").append("\n");
        info.append("========================");
        
        return info.toString();
    }
    
    /**
     * Prepara o sistema para desligamento
     */
    public void shutdown() {
        try {
            logger.info("Desligando sistema desktop...");
            
            // Realizar backup final
            performAutoBackup();
            
            // Salvar configurações
            desktopConfig.saveConfig();
            
            // Fechar conexões
            // ConnectionBD.closeAll(); // se implementado
            
            logger.info("Sistema desligado com sucesso");
            
        } catch (Exception e) {
            logger.error("Erro no desligamento: {}", e.getMessage(), e);
        }
    }
}
