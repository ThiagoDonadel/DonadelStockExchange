package br.com.donadel.account;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.donadel.def.entity.Account;
import br.com.donadel.repository.AccountRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryTest {
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Test
	public void simpleRepositoryTest() {
		Account newAccount = new Account("Thiago", 1000d);
		newAccount = accountRepository.save(newAccount);
		
		assertNotNull(newAccount.getId());		
		Account foundAccount = accountRepository.findByName(newAccount.getName());
		assertSame(newAccount.getName(), foundAccount.getName());
		assertSame(newAccount.getBalance(), foundAccount.getBalance());
		
		accountRepository.delete(foundAccount);
		
		Optional<Account> deletedAccount = accountRepository.findById(foundAccount.getId());
		assertFalse(deletedAccount.isPresent());
		
	}

}
