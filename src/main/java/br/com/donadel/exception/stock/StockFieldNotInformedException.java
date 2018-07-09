package br.com.donadel.exception.stock;

public class StockFieldNotInformedException extends StockException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StockFieldNotInformedException(String field) {
		super("Campo [" + field + "] não informado!");
	}

}
