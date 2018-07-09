package br.com.donadel.stock;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.donadel.def.entity.Account;
import br.com.donadel.def.entity.StockMonitor;
import br.com.donadel.exception.account.AccountInsufficientBalanceException;
import br.com.donadel.exception.stock.StockFieldNotInformedException;
import br.com.donadel.exception.stock.StockInvalidValueException;
import br.com.donadel.exception.stock.StockLowSellValueException;
import br.com.donadel.exception.stock.StockUniqueMonitorException;
import br.com.donadel.repository.StockMonitorRepository;
import br.com.donadel.service.account.IAccountService;
import br.com.donadel.service.stock.IStockMonitorService;
import br.com.donadel.service.stock.StockKeeper;
import br.com.donadel.service.stock.StockMonitorService;
import br.com.donadel.service.stock.StockPriceGenerator;
import br.com.donadel.service.transaction.ITransactionService;

@RunWith(SpringRunner.class)
public class StockServiceTest {

	@TestConfiguration
	static class StockBeanLoaderConfig {

		@Bean
		public IStockMonitorService stockMonitorService() {
			return new StockMonitorService();
		}

	}

	@Autowired
	private IStockMonitorService stockService;

	@MockBean
	private StockMonitorRepository stockMonitorRepository;

	@MockBean
	private IAccountService accountService;

	@MockBean
	private StockKeeper stockKeeper;

	@MockBean
	private StockPriceGenerator stockPriceGenerator;
	
	@MockBean
	private ITransactionService transactionService;

	@Before
	public void setup() {
		Account account = new Account("Thiago", 1500.0);
		account.setId(1l);

		StockMonitor defaultStock = createBaseStock();
		defaultStock.setId(1l);
		defaultStock.setAccount(account);

		Mockito.when(accountService.getAccountByName(account.getName())).thenReturn(account);
		Mockito.when(stockMonitorRepository.findByName(account.getName(), defaultStock.getName())).thenReturn(defaultStock);

		Collection<StockMonitor> stockCollection = new ArrayList<>();
		stockCollection.add(defaultStock);

		Mockito.when(stockKeeper.getSystemStocks()).thenReturn(stockCollection);
	}
	
	private StockMonitor createBaseStock() {
		StockMonitor stock = new StockMonitor();
		stock.setName("DSA");
		stock.setBuyValue(10.0);
		stock.setSellValue(12.0);
		stock.setQuantity(100);
		stock.setDisponibleBalance(1000.0);

		return stock;
	}

	@Test(expected = StockFieldNotInformedException.class)
	public void stockNullInformationTest() {
		StockMonitor stock = createBaseStock();
		stock.setName(null);

		stockService.createNewStockMonitor("Thiago", stock);
	}

	@Test(expected = StockInvalidValueException.class)
	public void stockInvalidValue() {
		StockMonitor stock = createBaseStock();
		stock.setBuyValue(-5.0);

		stockService.createNewStockMonitor("Thiago", stock);
	}

	@Test(expected = StockLowSellValueException.class)
	public void lowSellValueTest() {
		StockMonitor stock = createBaseStock();
		stock.setSellValue(7.0);

		stockService.createNewStockMonitor("Thiago", stock);
	}

	@Test(expected = AccountInsufficientBalanceException.class)
	public void insuficientBalanceTest() {
		StockMonitor stock = createBaseStock();
		stock.setDisponibleBalance(2000.0);
		stockService.createNewStockMonitor("Thiago", stock);
	}

	@Test(expected = StockUniqueMonitorException.class)
	public void uniqueStockTest() {
		stockService.createNewStockMonitor("Thiago", createBaseStock());
	}

	@Test
	public void buyTest() {
		Mockito.when(stockPriceGenerator.getCurrentPrice(1L)).thenReturn(9.0);
		Mockito.when(stockMonitorRepository.save(ArgumentMatchers.any(StockMonitor.class))).thenAnswer(mock -> {
			StockMonitor stock = mock.getArgument(0);
			Integer quantity = 211; 
			assertEquals(quantity, stock.getQuantity());
			Double balance = 1.0;
			assertEquals(balance, stock.getDisponibleBalance());
			return stock;
		});

		stockService.reactToMarket();
		Mockito.verify(stockMonitorRepository, Mockito.times(1)).save(ArgumentMatchers.any());
	}

	@Test
	public void sellTest() {
		Mockito.when(stockPriceGenerator.getCurrentPrice(1L)).thenReturn(14.0);
		Mockito.when(stockMonitorRepository.save(ArgumentMatchers.any(StockMonitor.class))).thenAnswer(mock -> {
			StockMonitor stock = mock.getArgument(0);
			Integer quantity = 0;
			assertEquals(quantity, stock.getQuantity());
			Double balance = 2400.0;
			assertEquals(balance, stock.getDisponibleBalance());
			return stock;
		});
		
		stockService.reactToMarket();
		Mockito.verify(stockMonitorRepository, Mockito.times(1)).save(ArgumentMatchers.any());
	}
	
	@Test
	public void doNothingTest() {
		Mockito.when(stockPriceGenerator.getCurrentPrice(1L)).thenReturn(11.0);
		stockService.reactToMarket();
		Mockito.verify(stockMonitorRepository, Mockito.never()).save(ArgumentMatchers.any());
	}

}
