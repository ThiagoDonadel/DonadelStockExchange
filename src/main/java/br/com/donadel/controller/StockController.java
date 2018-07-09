package br.com.donadel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.donadel.def.entity.StockMonitor;
import br.com.donadel.service.stock.IStockMonitorService;

@RestController
@RequestMapping("/stock")
public class StockController {

	@Autowired
	private IStockMonitorService monitorService;

	@PostMapping("/createMonitor")
	public String createNewMonitor(String accountName, String stockName, Double buy, Double sell, Double balance) {
		StockMonitor newStock = new StockMonitor(stockName, buy, sell, balance);
		monitorService.createNewStockMonitor(accountName, newStock);
		return "Ação " + stockName + "está sendo monitorada";
	}

	@GetMapping("/monitorStatus")
	public void printCurrentMonitorState(String accountName) {
		System.out.println("Status atual das Ações Monitoradas por [" + accountName + "]");
		monitorService.getAllStockByAccount(accountName).iterator().forEachRemaining(stock -> System.out.println(stock.format()));
	}

	@PostMapping("/stopMonitoring")
	public String stopMonitoring(String accountName, String stockName) {
		monitorService.stopMonitoring(accountName, stockName);
		return "A ação " + stockName + " parou de ser monitorada e seu saldo disponivel foi transferido para a sua conta";
	}

}
