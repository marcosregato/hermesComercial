package com.br.hermescomercial.business.alerta;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Date;

import com.br.hermescomercial.dao.EstoqueDao;
import com.br.hermescomercial.util.ConvertDado;

public class MensagemAlertaBusiness {


	EstoqueDao dao;

	/**
	 * comparar a data da estuque com a data atual
	 * 
	 * */
	public String produtoEncalhado(String data) {

		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

			String date = simpleDateFormat.format(new Date());
			Date dataAtual = simpleDateFormat.parse(date);

			Date dataDoBanco = simpleDateFormat.parse(dao.getDataCompraEstoque());

            ConvertDado convertDado = new ConvertDado();
            if(dataDoBanco.after(convertDado.convertData(data))){
                return dao.getDataCompraEstoque();
            }

			long days = ChronoUnit.DAYS.between((Temporal) dataDoBanco, (Temporal) dataAtual);

		} catch (Exception e) {
			// TODO: handle exception
		}
        return null;


	}

}
