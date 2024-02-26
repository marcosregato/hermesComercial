package business.aterta;

import java.time.LocalDate;
<<<<<<< HEAD
=======
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.Locale;
>>>>>>> 3cc664b2af729e79049f37d798ee18c3921aac71

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
