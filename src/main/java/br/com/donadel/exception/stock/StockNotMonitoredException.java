package br.com.donadel.exception.stock;

public class StockNotMonitoredException extends StockException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StockNotMonitoredException() {
		super("Ação Não Monitorada!");
	}

}
