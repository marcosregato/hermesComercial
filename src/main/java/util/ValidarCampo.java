/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import javafx.scene.control.Alert;
import javafx.scene.control.Tooltip;
import javafx.stage.Window;

/**
 *
 * @author marcos
 */
public class ValidarCampo {
    
    
    // https://lincolnminto.wordpress.com/2015/03/09/validacao-de-campos-javafx-validation-fields-javafx/
    
    Tooltip toolTip = new Tooltip("This field is requested.");
 
    
    public String campoVazio(String valor){
        if((valor.isEmpty()) && (valor.length() > 0)){
            toolTip.setStyle("-fx-background-color: linear-gradient(#FF6B6B , #FFA6A6 );"
                            + " -fx-font-weight: bold;");
        }
        return valor;
        
    }


    
}
