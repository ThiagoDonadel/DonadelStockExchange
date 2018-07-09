package br.com.donadel.transaction;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.donadel.def.TransactionType;
import br.com.donadel.def.entity.Transaction;
import br.com.donadel.exception.transaction.TransactionDataNotFound;
import br.com.donadel.repository.TransactionRepository;
import br.com.donadel.service.transaction.ITransactionService;
import br.com.donadel.service.transaction.TransactionService;

@RunWith(SpringRunner.class)
public class TransactionServiceTest {

	@TestConfiguration
	static class TransactionServiceLoaderConfig {

		@Bean
		public ITransactionService transactionService() {
			return new TransactionService();
		}

	}

	@Autowired
	private ITransactionService transactionService;

	@MockBean
	private TransactionRepository transactionRepository;

	@Before
	public void setup() {
		Transaction transaction1 = new Transaction("DSA", TransactionType.SELL, 10.0, 100, 1000.0, null);
		Transaction transaction2 = new Transaction("DSA", TransactionType.BUY, 8.0, 125, 0.0, null);

		List<Transaction> tList = new ArrayList<>();
		tList.add(transaction1);
		tList.add(transaction2);

		Mockito.when(transactionRepository.findAllByName("Thiago", "DSA")).thenReturn(tList);

	}

	@Test
	public void generateReportTest() throws IOException {
		String sPath = transactionService.generateReportByStock("Thiago", "DSA");
		Path path = Paths.get(sPath);
		assertTrue(Files.exists(path));
		Files.delete(path);
	}
	
	@Test(expected = TransactionDataNotFound.class)
	public void noDataFoundTest() {
		transactionService.generateReportByStock("Thiago", "DSAA");
	}

}
