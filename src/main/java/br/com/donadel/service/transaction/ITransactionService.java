package br.com.donadel.service.transaction;

import br.com.donadel.def.TransactionType;
import br.com.donadel.def.entity.Account;

public interface ITransactionService {

	/**
	 * Registra uma nova transação no banco
	 * 
	 * @param account
	 *            conta da transação
	 * @param stockName
	 *            noma da ação
	 * @param price
	 *            valor da transação
	 * @param ammount
	 *            quantidade de ações
	 * @param balance
	 *            saldo ao final da transação
	 * @param type
	 *            tipo da transação
	 */
	void registerTransaction(Account account, String stockName, Double price, Integer ammount, Double balance, TransactionType type);

	/**
	 * Gera um relatorio completo das transações de todas as ações da conta
	 * 
	 * @param accountName
	 *            noma da conta
	 * @return nome do arquivo gerado
	 */
	String generateReport(String accountName);

	/**
	 * Gera um relatorio completo das transações de uma ação
	 * 
	 * @param accountName
	 *            noma da conta
	 * @param stockName
	 *            nome da ação
	 * @return nome do arquivo gerado
	 */
	String generateReportByStock(String accountName, String stockName);
}
