package br.com.donadel.service.stock;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import br.com.donadel.def.TransactionType;
import br.com.donadel.def.entity.Account;
import br.com.donadel.def.entity.StockMonitor;
import br.com.donadel.exception.account.AccountInsufficientBalanceException;
import br.com.donadel.exception.stock.StockFieldNotInformedException;
import br.com.donadel.exception.stock.StockInvalidValueException;
import br.com.donadel.exception.stock.StockLowSellValueException;
import br.com.donadel.exception.stock.StockNotMonitoredException;
import br.com.donadel.exception.stock.StockUniqueMonitorException;
import br.com.donadel.repository.StockMonitorRepository;
import br.com.donadel.service.account.IAccountService;
import br.com.donadel.service.transaction.ITransactionService;
import br.com.donadel.util.RoundUtil;

@Service
public class StockMonitorService implements IStockMonitorService {

	@Autowired
	private StockMonitorRepository stockMonitorRepository;

	@Autowired
	private IAccountService accountService;

	@Autowired
	private StockKeeper stockKeeper;

	@Autowired
	private StockPriceGenerator stockPriceGenerator;

	@Autowired
	private ITransactionService transactionService;

	@Override
	public void createNewStockMonitor(String accountName, StockMonitor newStockMonitor) {
		Account account = accountService.getAccountByName(accountName);

		if (validateStock(newStockMonitor, account)) {
			newStockMonitor.setAccount(account);
			newStockMonitor = stockMonitorRepository.save(newStockMonitor);
			accountService.withdrawBalance(account, newStockMonitor.getDisponibleBalance());
			stockKeeper.keep(newStockMonitor);
		}

	}

	/**
	 * Válida se todos os campos de um novo monitoramento estão de acordo com as
	 * regras de negocio
	 * 
	 * @param stock
	 *            dados do monitoramento
	 * @param account
	 *            conta na qual o monitoramento será criado
	 * @return true se estiver tudo correto
	 */
	private boolean validateStock(StockMonitor stock, Account account) {
		if (stock.getName() == null) {
			throw new StockFieldNotInformedException("Nome");
		}

		if (stock.getBuyValue() == null) {
			throw new StockFieldNotInformedException("Valor Compra");
		}

		if (stock.getBuyValue() <= 0) {
			throw new StockInvalidValueException("Valor Compra");
		}

		if (stock.getSellValue() == null) {
			throw new StockFieldNotInformedException("Valor Venda");
		}

		if (stock.getSellValue() <= 0) {
			throw new StockInvalidValueException("Valor Venda");
		}

		if (stock.getSellValue() <= stock.getBuyValue()) {
			throw new StockLowSellValueException();
		}

		if (stock.getDisponibleBalance() == null) {
			throw new StockFieldNotInformedException("Saldo Disponivel");
		}

		if (stock.getDisponibleBalance() <= 0) {
			throw new StockInvalidValueException("Saldo Disponivel");
		}

		if (account.getBalance() < stock.getDisponibleBalance()) {
			throw new AccountInsufficientBalanceException(account.getBalance());
		}

		if (stockMonitorRepository.findByName(account.getName(), stock.getName()) != null) {
			throw new StockUniqueMonitorException();
		}

		return true;
	}

	@Override
	public List<StockMonitor> getAllStockByAccount(String accountName) {
		return stockMonitorRepository.findAllByAccountName(accountName);
	}

	@Override
	public boolean stopMonitoring(String accountName, String stockName) {
		StockMonitor stock = stockMonitorRepository.findByName(accountName, stockName);
		if (stock == null) {
			throw new StockNotMonitoredException();
		}

		if (stock.getQuantity() > 0) {
			sell(stock, stockPriceGenerator.getCurrentPrice(stock.getId()));
			accountService.addBalance(stock.getAccount(), stock.getDisponibleBalance());
			stockKeeper.release(stock.getId());
			stockMonitorRepository.delete(stock);
		}
		return true;
	}

	@JmsListener(destination = "reactToMarket")
	@Override
	public void reactToMarket() {
		/*
		 * Feito por meio de JMS para que as verificações só fossem feitas apos termido
		 * da atualização dos valores.. evitando que o sistema fique verificando toda
		 * hora mesmo sem mudanças
		 */
		stockKeeper.getSystemStocks().iterator().forEachRemaining(stock -> {
			Double currentPrice = stockPriceGenerator.getCurrentPrice(stock.getId());
			if (currentPrice <= stock.getBuyValue()) {
				buy(stock, currentPrice);
			} else if (currentPrice >= stock.getSellValue()) {
				sell(stock, currentPrice);
			}
		});
	}

	/**
	 * Método responsavel por realizar compras de uma ação
	 * 
	 * @param stock
	 *            ação a ser comprada
	 * @param price
	 *            preço atual da ação
	 */
	private void buy(StockMonitor stock, Double price) {
		int quantity = (int) (stock.getDisponibleBalance() / price);
		if (quantity > 0) {
			Double transactionValue = quantity * price;
			Double newBalance = stock.getDisponibleBalance() - transactionValue;
			// garantir 2 casas decimais para o saldo
			newBalance = RoundUtil.currencyTruncate(newBalance);
			// caso n tenha vendido tudo antes de comprar novamente
			int newQuantity = quantity + stock.getQuantity();
			stock.setQuantity(newQuantity);
			stock.setDisponibleBalance(newBalance);
			stockMonitorRepository.save(stock);
			transactionService.registerTransaction(stock.getAccount(), stock.getName(), price, quantity, newBalance, TransactionType.BUY);
		}

	}

	/**
	 * Método responsavel por vendar uma ação
	 * 
	 * @param stock
	 *            ação a ser vendida
	 * @param price
	 *            preço de venda
	 */
	private void sell(StockMonitor stock, Double price) {
		if (stock.getQuantity() > 0) {
			Integer quantity = stock.getQuantity();
			Double transactionValue = quantity * price;
			Double newBalance = stock.getDisponibleBalance() + transactionValue;
			stock.setQuantity(0);
			stock.setDisponibleBalance(newBalance);
			stockMonitorRepository.save(stock);
			transactionService.registerTransaction(stock.getAccount(), stock.getName(), price, quantity, newBalance, TransactionType.SELL);
		}
	}

}
