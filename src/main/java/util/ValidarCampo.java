/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javafx.scene.control.Tooltip;

/**
 *
 * @author marcos
 */
public class ValidarCampo {


	// https://lincolnminto.wordpress.com/2015/03/09/validacao-de-campos-javafx-validation-fields-javafx/

	Tooltip toolTip = new Tooltip("This field is requested.");


	public String campoVazio(String valor){
		if((valor.isEmpty()) && (valor.length() > 0)){
			//toolTip.setStyle("-fx-background-color: linear-gradient(#FF6B6B , #FFA6A6 );"
			//		+ " -fx-font-weight: bold;");
			return valor;
		}
		return null;

	}

	public String campoData(String valor){
		try {
			if((valor.isEmpty()) && (valor.length() > 0)){
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				LocalDate data = LocalDate.parse(valor, formatter);    
				return String.valueOf(data);
			}
		} catch (DateTimeParseException e) {

		} 
		return null;
	}





}
