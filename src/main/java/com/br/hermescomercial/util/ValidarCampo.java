/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.br.hermescomercial.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;

import com.br.hermescomercial.controller.LoginController;
import javafx.scene.control.Tooltip;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author marcos
 */
public class ValidarCampo {


	// https://lincolnminto.wordpress.com/2015/03/09/validacao-de-campos-javafx-validation-fields-javafx/

	Tooltip toolTip = new Tooltip("This field is requested.");
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(ValidarCampo.class);

	public String campoVazio(String valorCPF){
		if(!valorCPF.isEmpty()){
			//toolTip.setStyle("-fx-background-color: linear-gradient(#FF6B6B , #FFA6A6 );"
			//		+ " -fx-font-weight: bold;");
			return valorCPF;
		}
		return null;

	}

	public String campoData(String valorCPF){
		try {
			if(!valorCPF.isEmpty()){
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				LocalDate data = LocalDate.parse(valorCPF, formatter);    
				return String.valueOf(data);
			}
		} catch (DateTimeParseException e) {
            logger.info(e.getMessage());
		} 
		return null;
	}

	// https://www.devmedia.com.br/validando-o-cnpj-em-uma-aplicacao-java/22374
	public static boolean isCNPJ(String valorCPF) {
		if (valorCPF.equals("00000000000000") || valorCPF.equals("11111111111111") ||
                valorCPF.equals("22222222222222") || valorCPF.equals("33333333333333") ||
                valorCPF.equals("44444444444444") || valorCPF.equals("55555555555555") ||
                valorCPF.equals("66666666666666") || valorCPF.equals("77777777777777") ||
                valorCPF.equals("88888888888888") || valorCPF.equals("99999999999999") ||
				(valorCPF.length() != 14))
			return(false);

		char dig13;
        char dig14;
		int sm;
        int i;
        int r;
        int num;
        int peso;

		try {
			sm = 0;
			peso = 2;
			for (i=11; i>=0; i--) {
				num = valorCPF.charAt(i) - 48;
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}

			r = sm % 11;
			if ((r == 0) || (r == 1))
				dig13 = '0';
			else dig13 = (char)((11-r) + 48);

			sm = 0;
			peso = 2;
			for (i=12; i>=0; i--) {
				num = valorCPF.charAt(i)- 48;
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}

			r = sm % 11;
			if ((r == 0) || (r == 1))
				dig14 = '0';
			else dig14 = (char)((11-r) + 48);

			return (dig13 == valorCPF.charAt(12)) && (dig14 == valorCPF.charAt(13));
		} catch (InputMismatchException erro) {
			return(false);
		}
	}

	private String removeCaracteresEspeciais(String doc) {
		if (doc.contains(".")) {
			doc = doc.replace(".", "");
		}
		if (doc.contains("-")) {
			doc = doc.replace("-", "");
		}
		if (doc.contains("/")) {
			doc = doc.replace("/", "");
		}
		return doc;
	}

	public boolean isCPF(String valorCPF) {

		String cpf = removeCaracteresEspeciais(valorCPF);

		// considera-se erro CPF's formados por uma sequencia de numeros iguais
		if (cpf.equals("00000000000") || valorCPF.equals("11111111111") || cpf.equals("22222222222") || cpf.equals("33333333333") || cpf.equals("44444444444") || cpf.equals("55555555555") || cpf.equals("66666666666") || cpf.equals("77777777777") || cpf.equals("88888888888") || cpf.equals("99999999999") || (cpf.length() != 11))
			return (false);

		char dig10;
        char dig11;
		int sm;
        int i;
        int r;
        int num;
        int peso;

		// "try" - protege o codigo para eventuais erros de conversao de tipo (int)
		try {
			// Calculo do 1o. Digito Verificador
			sm = 0;
			peso = 10;
			for (i = 0; i < 9; i++) {
				// converte o i-esimo caractere do CPF em um numero:
				// por exemplo, transforma o caractere '0' no inteiro 0
				// (48 eh a posicao de '0' na tabela ASCII)
				num = cpf.charAt(i) - 48;
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11))
				dig10 = '0';
			else
				dig10 = (char) (r + 48); // converte no respectivo caractere numerico

			// Calculo do 2o. Digito Verificador
			sm = 0;
			peso = 11;
			for (i = 0; i < 10; i++) {
				num = cpf.charAt(i) - 48;
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11))
				dig11 = '0';
			else
				dig11 = (char) (r + 48);

			// Verifica se os digitos calculados conferem com os digitos informados.
			return (dig10 == cpf.charAt(9)) && (dig11 == cpf.charAt(10));
		} catch (InputMismatchException erro) {
			return (false);
		}
	}






}
