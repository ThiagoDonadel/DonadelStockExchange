package br.com.donadel.account;

import static org.junit.Assert.assertEquals;

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
import br.com.donadel.exception.account.AccountInsufficientBalanceException;
import br.com.donadel.exception.account.AccountNegativeBalanceException;
import br.com.donadel.exception.account.AccountNotFoundException;
import br.com.donadel.exception.account.AccountUniqueNameExeception;
import br.com.donadel.repository.AccountRepository;
import br.com.donadel.service.account.AccountService;
import br.com.donadel.service.account.IAccountService;

@RunWith(SpringRunner.class)
public class AccountServiceTests {

	@TestConfiguration
	static class AccountBeanLoaderConfig {
		@Bean
		public IAccountService accountService() {
			return new AccountService();
		}
	}

	@Autowired
	private IAccountService accountService;

	@MockBean
	private AccountRepository accountRepository;

	@Before
	public void setup() {
		Account existingAccount = new Account("Donadel", 2000d);
		Mockito.when(accountRepository.findByName(existingAccount.getName())).thenReturn(existingAccount);
		Mockito.when(accountRepository.findByName("Thiago")).thenReturn(null);
	}

	@Test
	public void testNewAccount() {
		Long expected = 1l;
		Mockito.when(accountRepository.save(ArgumentMatchers.any(Account.class))).thenAnswer(mock -> {
			Account account = mock.getArgument(0);
			account.setId(1l);
			return account;
		});

		Long id = accountService.createAccount("Teste", 3000d);

		assertEquals(expected, id);
	}

	@Test(expected = AccountNegativeBalanceException.class)
	public void testNegativeBalance() {
		accountService.createAccount("Teste", -50000d);
	}

	@Test(expected = AccountUniqueNameExeception.class)
	public void testUniqueName() {
		accountService.createAccount("Donadel", 10000d);
	}

	@Test(expected = AccountNotFoundException.class)
	public void testAccountNotFound() {
		accountService.getAccountByName("Thiago");
	}

	@Test
	public void testAddBalance() {
		Double expected = 3000d;
		Double newBalance = accountService.addBalance("Donadel", 1000d);
		assertEquals(expected, newBalance);
	}

	@Test
	public void testWithdrawBalance() {
		Double expected = 1000d;
		Double newBalance = accountService.withdrawBalance("Donadel", 1000d);
		assertEquals(expected, newBalance);
	}

	@Test(expected = AccountInsufficientBalanceException.class)
	public void testInsufficientBalance() {
		accountService.withdrawBalance("Donadel", 5000d);
	}

}
