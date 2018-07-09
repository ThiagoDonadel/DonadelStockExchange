package br.com.donadel.stock;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.donadel.def.entity.Account;
import br.com.donadel.def.entity.StockMonitor;
import br.com.donadel.repository.AccountRepository;
import br.com.donadel.repository.StockMonitorRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class StockRepositoryTest {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private StockMonitorRepository stockRepository;

	@Test
	public void simpleRepositoryTest() {
		Account account = new Account("Thiago", 10000.0);
		account = accountRepository.save(account);

		StockMonitor stock = new StockMonitor("Donadel S.A", 6.0, 9.5, 2000.0);
		stock.setAccount(account);
		stock = stockRepository.save(stock);

		assertNotNull(stock.getId());

		stockRepository.delete(stock);
		Optional<StockMonitor> deletedStock = stockRepository.findById(stock.getId());
		assertFalse(deletedStock.isPresent());

	}

}
