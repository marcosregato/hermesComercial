package com.br.hermescomercial.dao;

import com.br.hermescomercial.config.DatabaseConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Factory para criar instâncias de DAOs baseado na configuração do banco de dados
 * @author marcos
 */
public class DatabaseFactory {
    private static final Logger logger = LogManager.getLogger(DatabaseFactory.class);
    
    private static DatabaseConfig config;
    private static ProdutoDao produtoDao;
    private static ClienteDao clienteDao;
    private static VendaDao vendaDao;
    private static UsuarioDao usuarioDao;
    private static PagamentoDao pagamentoDao;
    private static ItemVendaDao itemVendaDao;
    
    // Excel DAOs
    private static ProdutoExcelDao produtoExcelDao;
    private static ClienteExcelDao clienteExcelDao;
    
    static {
        config = DatabaseConfig.getInstance();
        logger.info("DatabaseFactory inicializado com: " + config.getDatabaseName());
    }
    
    public static synchronized ProdutoDao getProdutoDao() {
        if (config.isExcel()) {
            if (produtoExcelDao == null) {
                produtoExcelDao = new ProdutoExcelDao();
                logger.info("ProdutoExcelDao criado");
            }
            // Retorna um adaptador que implementa a interface ProdutoDao
            return new ProdutoDaoAdapter(produtoExcelDao);
        } else {
            if (produtoDao == null) {
                produtoDao = new ProdutoDao();
                logger.info("ProdutoDao criado");
            }
            return produtoDao;
        }
    }
    
    public static synchronized ClienteDao getClienteDao() {
        if (config.isExcel()) {
            if (clienteExcelDao == null) {
                clienteExcelDao = new ClienteExcelDao();
                logger.info("ClienteExcelDao criado");
            }
            // Retorna um adaptador que implementa a interface ClienteDao
            return new ClienteDaoAdapter(clienteExcelDao);
        } else {
            if (clienteDao == null) {
                clienteDao = new ClienteDao();
                logger.info("ClienteDao criado");
            }
            return clienteDao;
        }
    }
    
    public static synchronized VendaDao getVendaDao() {
        if (vendaDao == null) {
            vendaDao = new VendaDao();
            logger.info("VendaDao criado");
        }
        return vendaDao;
    }
    
    public static synchronized UsuarioDao getUsuarioDao() {
        if (usuarioDao == null) {
            usuarioDao = new UsuarioDao();
            logger.info("UsuarioDao criado");
        }
        return usuarioDao;
    }
    
    public static synchronized PagamentoDao getPagamentoDao() {
        if (pagamentoDao == null) {
            pagamentoDao = new PagamentoDao();
            logger.info("PagamentoDao criado");
        }
        return pagamentoDao;
    }
    
    public static synchronized ItemVendaDao getItemVendaDao() {
        if (itemVendaDao == null) {
            itemVendaDao = new ItemVendaDao();
            logger.info("ItemVendaDao criado");
        }
        return itemVendaDao;
    }
    
    /**
     * Método para recriar todas as instâncias quando o tipo de banco é alterado
     */
    public static synchronized void resetConnections() {
        logger.info("Resetando conexões do banco de dados...");
        
        produtoDao = null;
        clienteDao = null;
        vendaDao = null;
        usuarioDao = null;
        pagamentoDao = null;
        itemVendaDao = null;
        
        produtoExcelDao = null;
        clienteExcelDao = null;
        
        logger.info("Conexões resetadas. Novo tipo: " + config.getDatabaseName());
    }
    
    /**
     * Testa a conexão com o banco de dados configurado
     */
    public static boolean testConnection() {
        try {
            if (config.isExcel()) {
                // Testar conexão Excel
                com.br.hermescomercial.excel.ExcelConnectionBD.getInstance().testConnection();
                return true;
            } else {
                // Testar conexão PostgreSQL
                com.br.hermescomercial.connectionBD.ConnectionBD.testConnection();
                return true;
            }
        } catch (Exception e) {
            logger.error("Erro ao testar conexão: " + e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Obtém informações sobre o banco de dados atual
     */
    public static String getDatabaseInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Tipo de Banco: ").append(config.getDatabaseName()).append("\n");
        info.append("Descrição: ").append(config.getDatabaseDescription()).append("\n");
        
        if (config.isExcel()) {
            info.append("Localização: ./data/excel/\n");
            info.append("Arquivos: produtos.xlsx, clientes.xlsx, vendas.xlsx, usuarios.xlsx\n");
        } else if (config.isPostgreSQL()) {
            info.append("Host: localhost:5432\n");
            info.append("Banco: hermescomercialdb\n");
            info.append("Usuário: hermesuser\n");
        }
        
        return info.toString();
    }
}
