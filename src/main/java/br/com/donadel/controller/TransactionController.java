package br.com.donadel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.donadel.service.transaction.ITransactionService;

@RestController
@RequestMapping("transaction")
public class TransactionController {

	@Autowired
	private ITransactionService transactionService;

	@PostMapping("/reportStock")
	public String generateReportByStock(String accountName, String stockName) {
		String report = transactionService.generateReportByStock(accountName, stockName);
		return "Novo relatorio gerado em: " + report;
	}
	
	@PostMapping("/report")
	public String generateReport(String accountName) {
		String report = transactionService.generateReport(accountName);
		return "Novo relatorio gerado em: " + report;
	}
}
