package br.com.donadel.exception.stock;

public class StockInvalidValueException extends StockException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StockInvalidValueException(String field) {
		super("O campo [" + field + "] deve ser maior que 0!");
	}

}
