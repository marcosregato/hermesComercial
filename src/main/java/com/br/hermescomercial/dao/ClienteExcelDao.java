package com.br.hermescomercial.dao;

import com.br.hermescomercial.excel.ExcelConnectionBD;
import com.br.hermescomercial.model.Cliente;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operações com clientes usando Excel como banco de dados
 * @author marcos
 */
public class ClienteExcelDao {
    private static final Logger logger = LogManager.getLogger(ClienteExcelDao.class);
    private final ExcelConnectionBD excelConnection;
    
    public ClienteExcelDao() {
        this.excelConnection = ExcelConnectionBD.getInstance();
    }
    
    private Cliente mapRowToCliente(Row row) {
        Cliente cliente = new Cliente();
        
        Cell idCell = row.getCell(0);
        if (idCell != null) {
            cliente.setId((long) idCell.getNumericCellValue());
        }
        
        Cell nomeCell = row.getCell(1);
        if (nomeCell != null) {
            cliente.setNome(nomeCell.getStringCellValue());
        }
        
        Cell cpfCell = row.getCell(2);
        if (cpfCell != null) {
            cliente.setCpf(cpfCell.getStringCellValue());
        }
        
        Cell emailCell = row.getCell(3);
        if (emailCell != null) {
            cliente.setEmail(emailCell.getStringCellValue());
        }
        
        Cell telefoneCell = row.getCell(4);
        if (telefoneCell != null) {
            cliente.setTelefone(telefoneCell.getStringCellValue());
        }
        
        Cell enderecoCell = row.getCell(5);
        if (enderecoCell != null) {
            cliente.setEndereco(enderecoCell.getStringCellValue());
        }
        
        Cell cidadeCell = row.getCell(6);
        if (cidadeCell != null) {
            cliente.setCidade(cidadeCell.getStringCellValue());
        }
        
        Cell estadoCell = row.getCell(7);
        if (estadoCell != null) {
            cliente.setEstado(estadoCell.getStringCellValue());
        }
        
        Cell cepCell = row.getCell(8);
        if (cepCell != null) {
            cliente.setCep(cepCell.getStringCellValue());
        }
        
        return cliente;
    }
    
    private void mapClienteToRow(Cliente cliente, Row row) {
        // ID
        Cell idCell = row.createCell(0);
        if (cliente.getId() != null) {
            idCell.setCellValue(cliente.getId());
        } else {
            idCell.setCellValue(getNextId());
        }
        
        // Nome
        Cell nomeCell = row.createCell(1);
        nomeCell.setCellValue(cliente.getNome() != null ? cliente.getNome() : "");
        
        // CPF
        Cell cpfCell = row.createCell(2);
        cpfCell.setCellValue(cliente.getCpf() != null ? cliente.getCpf() : "");
        
        // Email
        Cell emailCell = row.createCell(3);
        emailCell.setCellValue(cliente.getEmail() != null ? cliente.getEmail() : "");
        
        // Telefone
        Cell telefoneCell = row.createCell(4);
        telefoneCell.setCellValue(cliente.getTelefone() != null ? cliente.getTelefone() : "");
        
        // Endereço
        Cell enderecoCell = row.createCell(5);
        enderecoCell.setCellValue(cliente.getEndereco() != null ? cliente.getEndereco() : "");
        
        // Cidade
        Cell cidadeCell = row.createCell(6);
        cidadeCell.setCellValue(cliente.getCidade() != null ? cliente.getCidade() : "");
        
        // Estado
        Cell estadoCell = row.createCell(7);
        estadoCell.setCellValue(cliente.getEstado() != null ? cliente.getEstado() : "");
        
        // CEP
        Cell cepCell = row.createCell(8);
        cepCell.setCellValue(cliente.getCep() != null ? cliente.getCep() : "");
    }
    
    private long getNextId() {
        try {
            Workbook workbook = excelConnection.openWorkbook(excelConnection.getClientesFile());
            Sheet sheet = workbook.getSheet("Dados");
            
            long maxId = 0;
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell idCell = row.getCell(0);
                    if (idCell != null) {
                        long id = (long) idCell.getNumericCellValue();
                        if (id > maxId) {
                            maxId = id;
                        }
                    }
                }
            }
            
            workbook.close();
            return maxId + 1;
        } catch (IOException e) {
            logger.error("Erro ao obter próximo ID: " + e.getMessage(), e);
            return 1;
        }
    }
    
    public boolean salvar(Cliente cliente) {
        try {
            Workbook workbook = excelConnection.openWorkbook(excelConnection.getClientesFile());
            Sheet sheet = workbook.getSheet("Dados");
            
            int newRowNum = sheet.getLastRowNum() + 1;
            Row row = sheet.createRow(newRowNum);
            
            mapClienteToRow(cliente, row);
            
            // Auto-sizing columns
            for (int i = 0; i < 9; i++) {
                sheet.autoSizeColumn(i);
            }
            
            excelConnection.saveWorkbook(workbook, excelConnection.getClientesFile());
            workbook.close();
            
            logger.info("Cliente salvo com sucesso: " + cliente.getNome());
            return true;
            
        } catch (IOException e) {
            logger.error("Erro ao salvar cliente: " + e.getMessage(), e);
            return false;
        }
    }
    
    public boolean remove(String nome) {
        try {
            Workbook workbook = excelConnection.openWorkbook(excelConnection.getClientesFile());
            Sheet sheet = workbook.getSheet("Dados");
            
            boolean found = false;
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell nomeCell = row.getCell(1);
                    if (nomeCell != null && nomeCell.getStringCellValue().equals(nome)) {
                        sheet.removeRow(row);
                        // Shift rows up
                        if (i < sheet.getLastRowNum()) {
                            sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
                        }
                        found = true;
                        break;
                    }
                }
            }
            
            if (found) {
                excelConnection.saveWorkbook(workbook, excelConnection.getClientesFile());
                logger.info("Cliente removido com sucesso: " + nome);
            }
            
            workbook.close();
            return found;
            
        } catch (IOException e) {
            logger.error("Erro ao remover cliente: " + e.getMessage(), e);
            return false;
        }
    }
    
    public boolean update(Cliente cliente) {
        try {
            Workbook workbook = excelConnection.openWorkbook(excelConnection.getClientesFile());
            Sheet sheet = workbook.getSheet("Dados");
            
            boolean found = false;
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell nomeCell = row.getCell(1);
                    if (nomeCell != null && nomeCell.getStringCellValue().equals(cliente.getNome())) {
                        mapClienteToRow(cliente, row);
                        found = true;
                        break;
                    }
                }
            }
            
            if (found) {
                // Auto-sizing columns
                for (int i = 0; i < 9; i++) {
                    sheet.autoSizeColumn(i);
                }
                
                excelConnection.saveWorkbook(workbook, excelConnection.getClientesFile());
                logger.info("Cliente atualizado com sucesso: " + cliente.getNome());
            }
            
            workbook.close();
            return found;
            
        } catch (IOException e) {
            logger.error("Erro ao atualizar cliente: " + e.getMessage(), e);
            return false;
        }
    }
    
    public List<Cliente> buscar(String nome) {
        List<Cliente> lista = new ArrayList<>();
        
        try {
            Workbook workbook = excelConnection.openWorkbook(excelConnection.getClientesFile());
            Sheet sheet = workbook.getSheet("Dados");
            
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell nomeCell = row.getCell(1);
                    if (nomeCell != null && 
                        nomeCell.getStringCellValue().toLowerCase().contains(nome.toLowerCase())) {
                        lista.add(mapRowToCliente(row));
                    }
                }
            }
            
            workbook.close();
            
        } catch (IOException e) {
            logger.error("Erro ao buscar clientes: " + e.getMessage(), e);
        }
        
        return lista;
    }
    
    public List<Cliente> listar() {
        List<Cliente> lista = new ArrayList<>();
        
        try {
            Workbook workbook = excelConnection.openWorkbook(excelConnection.getClientesFile());
            Sheet sheet = workbook.getSheet("Dados");
            
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    lista.add(mapRowToCliente(row));
                }
            }
            
            workbook.close();
            
        } catch (IOException e) {
            logger.error("Erro ao listar clientes: " + e.getMessage(), e);
        }
        
        return lista;
    }
    
    public Cliente buscarPorCpf(String cpf) {
        try {
            Workbook workbook = excelConnection.openWorkbook(excelConnection.getClientesFile());
            Sheet sheet = workbook.getSheet("Dados");
            
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell cpfCell = row.getCell(2);
                    if (cpfCell != null && cpfCell.getStringCellValue().equals(cpf)) {
                        Cliente cliente = mapRowToCliente(row);
                        workbook.close();
                        return cliente;
                    }
                }
            }
            
            workbook.close();
            
        } catch (IOException e) {
            logger.error("Erro ao buscar cliente por CPF: " + e.getMessage(), e);
        }
        
        return null;
    }
}
