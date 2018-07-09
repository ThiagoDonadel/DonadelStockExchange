package br.com.donadel.exception.account;

public class AccountNegativeBalanceException extends AccountException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccountNegativeBalanceException() {
		super("Saldo Negativo Informado");
	}

}
