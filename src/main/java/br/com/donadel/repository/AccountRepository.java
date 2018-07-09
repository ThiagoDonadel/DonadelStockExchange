package br.com.donadel.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.donadel.def.entity.Account;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

	Account findByName(String name);
	
}
