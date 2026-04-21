package com.br.hermescomercial.dao;

import com.br.hermescomercial.excel.ExcelConnectionBD;
import com.br.hermescomercial.model.Produto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operações com produtos usando Excel como banco de dados
 * @author marcos
 */
public class ProdutoExcelDao {
    private static final Logger logger = LogManager.getLogger(ProdutoExcelDao.class);
    private final ExcelConnectionBD excelConnection;
    
    public ProdutoExcelDao() {
        this.excelConnection = ExcelConnectionBD.getInstance();
    }
    
    private Produto mapRowToProduto(Row row) {
        Produto produto = new Produto();
        
        Cell idCell = row.getCell(0);
        if (idCell != null) {
            produto.setId((long) idCell.getNumericCellValue());
        }
        
        Cell nomeCell = row.getCell(1);
        if (nomeCell != null) {
            produto.setNome(nomeCell.getStringCellValue());
        }
        
        Cell categoriaCell = row.getCell(2);
        if (categoriaCell != null) {
            produto.setCategoria(categoriaCell.getStringCellValue());
        }
        
        Cell subCategoriaCell = row.getCell(3);
        if (subCategoriaCell != null) {
            produto.setSubCategoria(subCategoriaCell.getStringCellValue());
        }
        
        Cell codigoCell = row.getCell(4);
        if (codigoCell != null) {
            produto.setCodigo(codigoCell.getStringCellValue());
        }
        
        Cell marcaCell = row.getCell(5);
        if (marcaCell != null) {
            produto.setMarca(marcaCell.getStringCellValue());
        }
        
        Cell dataCompraCell = row.getCell(6);
        if (dataCompraCell != null) {
            produto.setDataCompra(dataCompraCell.getStringCellValue());
        }
        
        Cell codigoBarrasCell = row.getCell(7);
        if (codigoBarrasCell != null) {
            produto.setCodigoBarras(codigoBarrasCell.getStringCellValue());
        }
        
        Cell unidadeCell = row.getCell(8);
        if (unidadeCell != null) {
            produto.setUnidade(unidadeCell.getStringCellValue());
        }
        
        Cell precoVendaCell = row.getCell(9);
        if (precoVendaCell != null) {
            produto.setPrecoVenda(BigDecimal.valueOf(precoVendaCell.getNumericCellValue()));
        }
        
        Cell estoqueCell = row.getCell(10);
        if (estoqueCell != null) {
            produto.setEstoque((int) estoqueCell.getNumericCellValue());
        }
        
        return produto;
    }
    
    private void mapProdutoToRow(Produto produto, Row row) {
        // ID
        Cell idCell = row.createCell(0);
        if (produto.getId() != null) {
            idCell.setCellValue(produto.getId());
        } else {
            idCell.setCellValue(getNextId());
        }
        
        // Nome
        Cell nomeCell = row.createCell(1);
        nomeCell.setCellValue(produto.getNome() != null ? produto.getNome() : "");
        
        // Categoria
        Cell categoriaCell = row.createCell(2);
        categoriaCell.setCellValue(produto.getCategoria() != null ? produto.getCategoria() : "");
        
        // SubCategoria
        Cell subCategoriaCell = row.createCell(3);
        subCategoriaCell.setCellValue(produto.getSubCategoria() != null ? produto.getSubCategoria() : "");
        
        // Código
        Cell codigoCell = row.createCell(4);
        codigoCell.setCellValue(produto.getCodigo() != null ? produto.getCodigo() : "");
        
        // Marca
        Cell marcaCell = row.createCell(5);
        marcaCell.setCellValue(produto.getMarca() != null ? produto.getMarca() : "");
        
        // Data Compra
        Cell dataCompraCell = row.createCell(6);
        dataCompraCell.setCellValue(produto.getDataCompra() != null ? produto.getDataCompra() : "");
        
        // Código de Barras
        Cell codigoBarrasCell = row.createCell(7);
        codigoBarrasCell.setCellValue(produto.getCodigoBarras() != null ? produto.getCodigoBarras() : "");
        
        // Unidade
        Cell unidadeCell = row.createCell(8);
        unidadeCell.setCellValue(produto.getUnidade() != null ? produto.getUnidade() : "");
        
        // Preço Venda
        Cell precoVendaCell = row.createCell(9);
        if (produto.getPrecoVenda() != null) {
            precoVendaCell.setCellValue(produto.getPrecoVenda().doubleValue());
        } else {
            precoVendaCell.setCellValue(0.0);
        }
        
        // Estoque
        Cell estoqueCell = row.createCell(10);
        estoqueCell.setCellValue(produto.getEstoque());
    }
    
    private long getNextId() {
        try {
            Workbook workbook = excelConnection.openWorkbook(excelConnection.getProdutosFile());
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
    
    public boolean salvar(Produto produto) {
        try {
            Workbook workbook = excelConnection.openWorkbook(excelConnection.getProdutosFile());
            Sheet sheet = workbook.getSheet("Dados");
            
            int newRowNum = sheet.getLastRowNum() + 1;
            Row row = sheet.createRow(newRowNum);
            
            mapProdutoToRow(produto, row);
            
            // Auto-sizing columns
            for (int i = 0; i < 11; i++) {
                sheet.autoSizeColumn(i);
            }
            
            excelConnection.saveWorkbook(workbook, excelConnection.getProdutosFile());
            workbook.close();
            
            logger.info("Produto salvo com sucesso: " + produto.getNome());
            return true;
            
        } catch (IOException e) {
            logger.error("Erro ao salvar produto: " + e.getMessage(), e);
            return false;
        }
    }
    
    public boolean remove(String nome) {
        try {
            Workbook workbook = excelConnection.openWorkbook(excelConnection.getProdutosFile());
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
                excelConnection.saveWorkbook(workbook, excelConnection.getProdutosFile());
                logger.info("Produto removido com sucesso: " + nome);
            }
            
            workbook.close();
            return found;
            
        } catch (IOException e) {
            logger.error("Erro ao remover produto: " + e.getMessage(), e);
            return false;
        }
    }
    
    public boolean update(Produto produto) {
        try {
            Workbook workbook = excelConnection.openWorkbook(excelConnection.getProdutosFile());
            Sheet sheet = workbook.getSheet("Dados");
            
            boolean found = false;
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell nomeCell = row.getCell(1);
                    if (nomeCell != null && nomeCell.getStringCellValue().equals(produto.getNome())) {
                        mapProdutoToRow(produto, row);
                        found = true;
                        break;
                    }
                }
            }
            
            if (found) {
                // Auto-sizing columns
                for (int i = 0; i < 11; i++) {
                    sheet.autoSizeColumn(i);
                }
                
                excelConnection.saveWorkbook(workbook, excelConnection.getProdutosFile());
                logger.info("Produto atualizado com sucesso: " + produto.getNome());
            }
            
            workbook.close();
            return found;
            
        } catch (IOException e) {
            logger.error("Erro ao atualizar produto: " + e.getMessage(), e);
            return false;
        }
    }
    
    public List<Produto> buscar(String nome) {
        List<Produto> lista = new ArrayList<>();
        
        try {
            Workbook workbook = excelConnection.openWorkbook(excelConnection.getProdutosFile());
            Sheet sheet = workbook.getSheet("Dados");
            
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell nomeCell = row.getCell(1);
                    if (nomeCell != null && 
                        nomeCell.getStringCellValue().toLowerCase().contains(nome.toLowerCase())) {
                        lista.add(mapRowToProduto(row));
                    }
                }
            }
            
            workbook.close();
            
        } catch (IOException e) {
            logger.error("Erro ao buscar produtos: " + e.getMessage(), e);
        }
        
        return lista;
    }
    
    public List<Produto> listar() {
        List<Produto> lista = new ArrayList<>();
        
        try {
            Workbook workbook = excelConnection.openWorkbook(excelConnection.getProdutosFile());
            Sheet sheet = workbook.getSheet("Dados");
            
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    lista.add(mapRowToProduto(row));
                }
            }
            
            workbook.close();
            
        } catch (IOException e) {
            logger.error("Erro ao listar produtos: " + e.getMessage(), e);
        }
        
        return lista;
    }
    
    public Produto buscarPorCodigoBarras(String codigoBarras) {
        try {
            Workbook workbook = excelConnection.openWorkbook(excelConnection.getProdutosFile());
            Sheet sheet = workbook.getSheet("Dados");
            
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell codigoCell = row.getCell(4); // código
                    Cell codigoBarrasCell = row.getCell(7); // código de barras
                    
                    boolean match = false;
                    if (codigoCell != null && codigoCell.getStringCellValue().equals(codigoBarras)) {
                        match = true;
                    }
                    if (codigoBarrasCell != null && codigoBarrasCell.getStringCellValue().equals(codigoBarras)) {
                        match = true;
                    }
                    
                    if (match) {
                        Produto produto = mapRowToProduto(row);
                        workbook.close();
                        return produto;
                    }
                }
            }
            
            workbook.close();
            
        } catch (IOException e) {
            logger.error("Erro ao buscar produto por código de barras: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    public List<Produto> buscarComFiltros(String nome, String categoria, String subCategoria, 
                                         String codigoBarras, BigDecimal precoMin, BigDecimal precoMax, 
                                         Integer estoqueMin, boolean ativos, boolean inativos) {
        List<Produto> lista = new ArrayList<>();
        
        try {
            Workbook workbook = excelConnection.openWorkbook(excelConnection.getProdutosFile());
            Sheet sheet = workbook.getSheet("Dados");
            
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Produto produto = mapRowToProduto(row);
                    
                    boolean matches = true;
                    
                    // Filtro por nome
                    if (nome != null && !nome.trim().isEmpty()) {
                        if (produto.getNome() == null || 
                            !produto.getNome().toLowerCase().contains(nome.toLowerCase())) {
                            matches = false;
                        }
                    }
                    
                    // Filtro por categoria
                    if (categoria != null && !categoria.trim().isEmpty()) {
                        if (produto.getCategoria() == null || 
                            !produto.getCategoria().toLowerCase().contains(categoria.toLowerCase())) {
                            matches = false;
                        }
                    }
                    
                    // Filtro por código
                    if (codigoBarras != null && !codigoBarras.trim().isEmpty()) {
                        if (produto.getCodigo() == null || 
                            !produto.getCodigo().toLowerCase().contains(codigoBarras.toLowerCase())) {
                            matches = false;
                        }
                    }
                    
                    // Filtro por preço mínimo
                    if (precoMin != null) {
                        if (produto.getPrecoVenda() == null || 
                            produto.getPrecoVenda().compareTo(precoMin) < 0) {
                            matches = false;
                        }
                    }
                    
                    // Filtro por preço máximo
                    if (precoMax != null) {
                        if (produto.getPrecoVenda() == null || 
                            produto.getPrecoVenda().compareTo(precoMax) > 0) {
                            matches = false;
                        }
                    }
                    
                    // Filtro por estoque mínimo
                    if (estoqueMin != null) {
                        if (produto.getEstoque() < estoqueMin) {
                            matches = false;
                        }
                    }
                    
                    if (matches) {
                        lista.add(produto);
                    }
                }
            }
            
            workbook.close();
            
        } catch (IOException e) {
            logger.error("Erro ao buscar produtos com filtros: " + e.getMessage(), e);
        }
        
        return lista;
    }
}
