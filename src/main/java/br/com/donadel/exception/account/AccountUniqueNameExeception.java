package br.com.donadel.exception.account;

public class AccountUniqueNameExeception extends AccountException {

	public AccountUniqueNameExeception() {
		super("Nome da conta já utilizado");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
