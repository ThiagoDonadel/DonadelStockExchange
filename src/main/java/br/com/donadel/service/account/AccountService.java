package br.com.donadel.service.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.donadel.def.entity.Account;
import br.com.donadel.exception.account.AccountException;
import br.com.donadel.exception.account.AccountInsufficientBalanceException;
import br.com.donadel.exception.account.AccountNegativeBalanceException;
import br.com.donadel.exception.account.AccountNotFoundException;
import br.com.donadel.exception.account.AccountUniqueNameExeception;
import br.com.donadel.repository.AccountRepository;

@Service
public class AccountService implements IAccountService {

	@Autowired
	private AccountRepository accountRepository;

	@Override
	public Long createAccount(String name, Double initialBalance) throws AccountException {

		checkValueNegative(initialBalance);

		if (accountRepository.findByName(name) != null) {
			throw new AccountUniqueNameExeception();
		}

		Account newAccount = new Account(name, initialBalance);
		newAccount = accountRepository.save(newAccount);
		return newAccount.getId();
	}

	@Override
	public Double addBalance(String accountName, Double value) throws AccountException {
		Account account = getAccountByName(accountName);
		return this.addBalance(account, value);
	}

	@Override
	public Double addBalance(Account account, Double value) throws AccountException {
		checkValueNegative(value);
		return applyBalance(account, value);
	}

	@Override
	public Double withdrawBalance(String accountName, Double value) throws AccountException {

		Account account = getAccountByName(accountName);
		return this.withdrawBalance(account, value);

	}

	@Override
	public Double withdrawBalance(Account account, Double value) throws AccountException {
		checkValueNegative(value);
		if (account.getBalance() < value) {
			throw new AccountInsufficientBalanceException(account.getBalance());
		}
		return applyBalance(account, value * -1);
	}

	/**
	 * Atualiza o saldo da conta
	 * 
	 * @param account
	 *            conta
	 * @param value
	 *            valor a ser somado (valor positivo) ou diminuido (valor negativo);
	 * @return
	 */
	private Double applyBalance(Account account, Double value) {

		Double newBalance = account.getBalance() + value;
		account.setBalance(newBalance);
		accountRepository.save(account);

		return newBalance;

	}

	private boolean checkValueNegative(Double value) throws AccountNegativeBalanceException {
		if (value < 0) {
			throw new AccountNegativeBalanceException();
		}

		return true;
	}

	@Override
	public Account getAccountByName(String accountName) throws AccountException {
		Account account = accountRepository.findByName(accountName);

		if (account == null) {
			throw new AccountNotFoundException();
		}

		return account;
	}

}
