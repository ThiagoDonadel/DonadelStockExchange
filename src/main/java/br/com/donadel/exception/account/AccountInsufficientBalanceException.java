package br.com.donadel.exception.account;

public class AccountInsufficientBalanceException extends AccountException {

	public AccountInsufficientBalanceException(Double currentBalance) {
		super("Saldo da Conta Insuficiente! (Saldo Atual: " + currentBalance + ")");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
