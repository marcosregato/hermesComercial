package com.br.hermescomercial.excel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe para gerenciar conexão com arquivos Excel como banco de dados
 * @author marcos
 */
public class ExcelConnectionBD {
    private static final Logger logger = LogManager.getLogger(ExcelConnectionBD.class);
    
    private static final String EXCEL_DATA_PATH = "./data/excel/";
    private static final String PRODUTOS_FILE = "produtos.xlsx";
    private static final String CLIENTES_FILE = "clientes.xlsx";
    private static final String VENDAS_FILE = "vendas.xlsx";
    private static final String USUARIOS_FILE = "usuarios.xlsx";
    
    private static ExcelConnectionBD instance;
    private Workbook workbook;
    
    private ExcelConnectionBD() {
        initializeDataDirectory();
    }
    
    public static synchronized ExcelConnectionBD getInstance() {
        if (instance == null) {
            instance = new ExcelConnectionBD();
        }
        return instance;
    }
    
    private void initializeDataDirectory() {
        try {
            Path dataPath = Paths.get(EXCEL_DATA_PATH);
            if (!Files.exists(dataPath)) {
                Files.createDirectories(dataPath);
                logger.info("Diretório de dados Excel criado: " + EXCEL_DATA_PATH);
            }
            
            // Criar arquivos Excel se não existirem
            createExcelFileIfNotExists(PRODUTOS_FILE, getProdutosHeaders());
            createExcelFileIfNotExists(CLIENTES_FILE, getClientesHeaders());
            createExcelFileIfNotExists(VENDAS_FILE, getVendasHeaders());
            createExcelFileIfNotExists(USUARIOS_FILE, getUsuariosHeaders());
            
        } catch (IOException e) {
            logger.error("Erro ao inicializar diretório de dados Excel: " + e.getMessage(), e);
            throw new RuntimeException("Não foi possível inicializar o diretório de dados", e);
        }
    }
    
    private void createExcelFileIfNotExists(String fileName, List<String> headers) throws IOException {
        Path filePath = Paths.get(EXCEL_DATA_PATH + fileName);
        if (!Files.exists(filePath)) {
            try (Workbook wb = new XSSFWorkbook();
                 FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
                
                Sheet sheet = wb.createSheet("Dados");
                Row headerRow = sheet.createRow(0);
                
                for (int i = 0; i < headers.size(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers.get(i));
                }
                
                wb.write(fos);
                logger.info("Arquivo Excel criado: " + fileName);
            }
        }
    }
    
    public Workbook getWorkbook() throws IOException {
        if (workbook == null) {
            // Para operações de leitura, vamos abrir o workbook conforme necessário
            // Isso evita problemas de concorrência
        }
        return workbook;
    }
    
    public Workbook openWorkbook(String fileName) throws IOException {
        String filePath = EXCEL_DATA_PATH + fileName;
        return new XSSFWorkbook(new FileInputStream(filePath));
    }
    
    public void saveWorkbook(Workbook workbook, String fileName) throws IOException {
        String filePath = EXCEL_DATA_PATH + fileName;
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        }
    }
    
    public Sheet getSheet(String fileName, String sheetName) throws IOException {
        try (Workbook wb = openWorkbook(fileName)) {
            Sheet sheet = wb.getSheet(sheetName);
            if (sheet == null) {
                sheet = wb.createSheet(sheetName);
            }
            return sheet;
        }
    }
    
    public Sheet getOrCreateSheet(String fileName, String sheetName) throws IOException {
        String filePath = EXCEL_DATA_PATH + fileName;
        Workbook wb;
        boolean needsSave = false;
        
        try {
            if (Files.exists(Paths.get(filePath))) {
                wb = new XSSFWorkbook(new FileInputStream(filePath));
            } else {
                wb = new XSSFWorkbook();
                needsSave = true;
            }
            
            Sheet sheet = wb.getSheet(sheetName);
            if (sheet == null) {
                sheet = wb.createSheet(sheetName);
                needsSave = true;
                
                // Adicionar cabeçalhos
                List<String> headers = getHeadersForFile(fileName);
                if (headers != null) {
                    Row headerRow = sheet.createRow(0);
                    for (int i = 0; i < headers.size(); i++) {
                        Cell cell = headerRow.createCell(i);
                        cell.setCellValue(headers.get(i));
                    }
                }
            }
            
            if (needsSave) {
                saveWorkbook(wb, fileName);
            }
            
            return sheet;
        } catch (IOException e) {
            logger.error("Erro ao obter planilha: " + e.getMessage(), e);
            throw e;
        }
    }
    
    private List<String> getHeadersForFile(String fileName) {
        switch (fileName) {
            case PRODUTOS_FILE:
                return getProdutosHeaders();
            case CLIENTES_FILE:
                return getClientesHeaders();
            case VENDAS_FILE:
                return getVendasHeaders();
            case USUARIOS_FILE:
                return getUsuariosHeaders();
            default:
                return null;
        }
    }
    
    private List<String> getProdutosHeaders() {
        List<String> headers = new ArrayList<>();
        headers.add("id");
        headers.add("nome");
        headers.add("categoria");
        headers.add("subCategoria");
        headers.add("codigo");
        headers.add("marca");
        headers.add("dataCompra");
        headers.add("codigoBarras");
        headers.add("unidade");
        headers.add("precoVenda");
        headers.add("estoque");
        return headers;
    }
    
    private List<String> getClientesHeaders() {
        List<String> headers = new ArrayList<>();
        headers.add("id");
        headers.add("nome");
        headers.add("cpf");
        headers.add("email");
        headers.add("telefone");
        headers.add("endereco");
        headers.add("cidade");
        headers.add("estado");
        headers.add("cep");
        return headers;
    }
    
    private List<String> getVendasHeaders() {
        List<String> headers = new ArrayList<>();
        headers.add("id");
        headers.add("dataVenda");
        headers.add("idCliente");
        headers.add("valorTotal");
        headers.add("formaPagamento");
        headers.add("status");
        return headers;
    }
    
    private List<String> getUsuariosHeaders() {
        List<String> headers = new ArrayList<>();
        headers.add("id");
        headers.add("nome");
        headers.add("usuario");
        headers.add("senha");
        headers.add("perfil");
        headers.add("ativo");
        return headers;
    }
    
    public String getProdutosFile() {
        return PRODUTOS_FILE;
    }
    
    public String getClientesFile() {
        return CLIENTES_FILE;
    }
    
    public String getVendasFile() {
        return VENDAS_FILE;
    }
    
    public String getUsuariosFile() {
        return USUARIOS_FILE;
    }
    
    public String getExcelDataPath() {
        return EXCEL_DATA_PATH;
    }
    
    public void testConnection() {
        try {
            Path dataPath = Paths.get(EXCEL_DATA_PATH);
            if (Files.exists(dataPath) && Files.isDirectory(dataPath)) {
                logger.info("Conexão com Excel funcionando corretamente!");
                logger.info("Diretório de dados: " + dataPath.toAbsolutePath());
                
                // Verificar arquivos
                String[] files = {PRODUTOS_FILE, CLIENTES_FILE, VENDAS_FILE, USUARIOS_FILE};
                for (String file : files) {
                    Path filePath = Paths.get(EXCEL_DATA_PATH + file);
                    if (Files.exists(filePath)) {
                        logger.info("Arquivo encontrado: " + file);
                    } else {
                        logger.warn("Arquivo não encontrado: " + file);
                    }
                }
            } else {
                logger.error("Diretório de dados Excel não encontrado: " + EXCEL_DATA_PATH);
            }
        } catch (Exception e) {
            logger.error("Erro ao testar conexão Excel: " + e.getMessage(), e);
        }
    }
}
