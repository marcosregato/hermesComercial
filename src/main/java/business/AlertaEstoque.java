package business;

import dao.EstoqueDao;

public class AlertaEstoque {

	EstoqueDao dao;

	/**
	 * Aviso quando o produto está acabando
	 * 
	 * */
	public void limiteProduto(int limite) {
		try {
			for(int i=0; i < dao.listaQuantidadeEstoque().size();i++) {
//				if(limite < dao.listaQuantidadeEstoque().get(i)) {
					
//				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		



	}

	/**
	 * Aviso quando o produto está encalhando
	 * 
	 * */
	public void produtoEncalhado() {
		
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}



	}





}
