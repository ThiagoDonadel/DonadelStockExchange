package br.com.donadel.exception.stock;

public class StockUniqueMonitorException extends StockException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StockUniqueMonitorException() {
		super("Está ação já esta sendo monitorada!");
	}

}
