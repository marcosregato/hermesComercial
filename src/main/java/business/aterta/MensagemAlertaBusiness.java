package business.aterta;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.Locale;

import dao.EstoqueDao;

public class MensagemAlertaBusiness {


	EstoqueDao dao;

	/**
	 * comparar a data da estuque com a data atual
	 * 
	 * */
	public void produtoEncalhado() {

		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

			String date = simpleDateFormat.format(new Date());
			Date dataAtual = simpleDateFormat.parse(date);

			Date dataDoBanco = simpleDateFormat.parse(dao.getDataCompra());

			long days = ChronoUnit.DAYS.between((Temporal) dataDoBanco, (Temporal) dataAtual);

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
