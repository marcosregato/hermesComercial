/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.FornecedorDao;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import model.Fornecedor;

/**
 *
 * @author marcos
 */
public class FornecedorController {

    @FXML
    private Button btBuscar;

    @FXML
    private TableColumn<?, String> colAcao;

    @FXML
    private TableColumn<Fornecedor, String> colDesconto;

    @FXML
    private TableColumn<Fornecedor, String> colNome;

    @FXML
    private TableColumn<Fornecedor, String> colQuantidade;

    @FXML
    private TableColumn<Fornecedor, String> colTipo;

    @FXML
    private TableColumn<Fornecedor, String> colValor;

    @FXML
    private ComboBox<String> comboFornecedor;

    @FXML
    private Pane painel;

    @FXML
    private Pane painel2;

    @FXML
    private TextField txtNome;
    FornecedorDao dao = new FornecedorDao();
    
    public void salvar(Fornecedor fornecedor){
        try {
            dao.salvar(fornecedor);
        } catch (Exception e) {
            e.printStackTrace();
        
        }
    }
    
    public void listar(){
        try {
            dao.listar();
        } catch (Exception e) {
            e.printStackTrace();
        
        }
    }
    
    public void remove(String nome){
        try {
            dao.remove(nome);
        } catch (Exception e) {
            e.printStackTrace();
        
        }
    }
    
    public void update(Fornecedor fornecedor){
        try {
            dao.update(fornecedor);
        } catch (Exception e) {
            e.printStackTrace();
        
        }
    }
    
    public void buscar(String nome){
        try {
            dao.buscar(nome);
        } catch (Exception e) {
            e.printStackTrace();
        
        }
    }
    
}
