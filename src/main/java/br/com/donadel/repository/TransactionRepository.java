package br.com.donadel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.donadel.def.entity.Transaction;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

	@Query("Select t from Transaction t where t.account.name = :accountName and t.stockName = :stockName order by date")
	List<Transaction> findAllByName(@Param("accountName") String accountName, @Param("stockName") String stockName);
	
	@Query("Select t from Transaction t where t.account.name = :accountName order by date")
	List<Transaction> findAllByAccount(@Param("accountName") String accountName);

}
