package br.com.donadel.exception.stock;

public class StockLowSellValueException extends StockException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StockLowSellValueException() {
		super("O Valor de Venda deve ser maior que o Valor de Compra!");
	}

}
