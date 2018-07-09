package br.com.donadel.transaction;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.donadel.def.TransactionType;
import br.com.donadel.def.entity.Account;
import br.com.donadel.def.entity.Transaction;
import br.com.donadel.repository.AccountRepository;
import br.com.donadel.repository.TransactionRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionRepositoryTest {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Test
	public void simpleDatabaseTest() {
		Account account = new Account("Thiago", 1000.0);
		accountRepository.save(account);

		Transaction transaction = new Transaction("DSA", TransactionType.SELL, 10.0, 100, 1000.0, account);
		transaction = transactionRepository.save(transaction);

		assertNotNull(transaction.getId());

	}
}
