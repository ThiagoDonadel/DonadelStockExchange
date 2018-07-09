package br.com.donadel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.donadel.def.entity.StockMonitor;

@Repository
public interface StockMonitorRepository extends CrudRepository<StockMonitor, Long> {
	
	@Query("select s from StockMonitor s where s.account.name = :accountName")
	List<StockMonitor> findAllByAccountName(@Param("accountName") String accountName);
	
	@Query("select s from StockMonitor s where s.account.name = :accountName and s.name = :stockName")
	StockMonitor findByName(@Param("accountName") String accountName, @Param("stockName") String stockName);
	
}
