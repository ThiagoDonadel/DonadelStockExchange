package br.com.donadel.exception.transaction;

public class TransactionDataNotFound extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TransactionDataNotFound(String target) {
		super("Nenhum dado encontrado para " + target +"!");
	}

}
