package br.com.donadel.service.account;

import br.com.donadel.def.entity.Account;
import br.com.donadel.exception.account.AccountException;

public interface IAccountService {

	/**
	 * Criação de uma nova conta no sistema
	 * 
	 * @param name
	 *            nome da conta
	 * @param initialBalance
	 *            saldo inicial disponivel para a conta
	 * @return Id da nova conta criada
	 * @throws AccountException
	 * 
	 */
	Long createAccount(String name, Double initialBalance) throws AccountException;

	/**
	 * Adiciona um valor a "carteira" da conta.
	 * 
	 * @param accountName
	 *            nome da conta
	 * @param value
	 *            valor a ser adicionado
	 * @return o novo saldo da "carteira".
	 * @throws AccountException
	 */
	Double addBalance(String accountName, Double value) throws AccountException;

	/**
	 * Adiciona um valor a "carteira" da conta.
	 * 
	 * @param accountName
	 *            objeto da conta
	 * @param value
	 *            valor a ser adicionado
	 * @return o novo saldo da "carteira".
	 * @throws AccountException
	 */
	Double addBalance(Account account, Double value) throws AccountException;

	/**
	 * Retira um valor da "carteira" da conta.
	 * 
	 * @param accountName
	 *            nome da conta
	 * @param value
	 *            valor a ser retirado
	 * @return o novo saldo da conta
	 * @throws AccountException
	 */
	Double withdrawBalance(String accountName, Double value) throws AccountException;

	/**
	 * Retira um valor da "carteira" da conta.
	 * 
	 * @param accountName
	 *            objeto da conta
	 * @param value
	 *            valor a ser retirado
	 * @return o novo saldo da conta
	 * @throws AccountException
	 */
	Double withdrawBalance(Account account, Double value) throws AccountException;

	/**
	 * Encontra uma conta pelo nome da mesma
	 * 
	 * @param accountName
	 *            nome da conta a ser encontrada
	 * @return conta encontrada
	 * @throws AccountException
	 *             caso nenhuma conta seja encontrada
	 */
	Account getAccountByName(String accountName) throws AccountException;

}
