package business.aterta;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
//			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",Locale.);

			// parse string to date
//			Date dateBefore = formatter.parse("2019-12-05");
//			Date dateAfter = formatter.parse("2020-01-05");
			
			
			LocalDate dateAfter = LocalDate.now();
			System.out.println(dateAfter);
			
			//long days = ChronoUnit.DAYS.between(dateBefore, dateAfter);
		} catch (Exception e) {
			// TODO: handle exception
		}


	}
	
	
	public static void main(String[] asd) {
		MensagemAlertaBusiness msd = new MensagemAlertaBusiness();
		msd.produtoEncalhado();
	}

}
