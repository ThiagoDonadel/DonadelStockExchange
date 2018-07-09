package br.com.donadel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.donadel.def.entity.StockMonitor;
import br.com.donadel.service.account.IAccountService;
import br.com.donadel.service.stock.IStockMonitorService;

@RestController
public class DefaultController {
	
	@Autowired 
	private IAccountService accountService;
	
	@Autowired
	private IStockMonitorService stockService;
	
	@PostMapping("/default/start") 
	public void startDemo(){
		
		accountService.createAccount("Thiago", 10000.0);
		
		StockMonitor stock1 = new StockMonitor("DSA", 9.35, 11.15, 3000.0);
		stockService.createNewStockMonitor("Thiago", stock1);
		
		StockMonitor stock2 = new StockMonitor("TSA", 7.44, 8.79, 4000.0);
		stockService.createNewStockMonitor("Thiago", stock2);
	}

}
