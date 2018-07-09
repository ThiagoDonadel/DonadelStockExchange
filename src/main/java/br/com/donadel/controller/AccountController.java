package br.com.donadel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.donadel.def.entity.Account;
import br.com.donadel.service.account.IAccountService;

@RestController
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private IAccountService accountService;

	@PostMapping(value = "/new")
	public String newAccount(String name, Double initialBalance) {
		accountService.createAccount(name, initialBalance);
		return "Conta" + name + " criada com sucesso";
	}
	
	@PostMapping(value = "/addBalance")
	public String addFunds(String accountName, Double value) {
		Double newBalance = accountService.addBalance(accountName, value);
		return "Foram adicionados R$ " + value + "! Seu novo saldo é de R$ " + newBalance;
	}
	
	@PostMapping(value = "/withdrawBalance")
	public String withdrawFunds(String accountName, Double value) {
		Double newBalance = accountService.withdrawBalance(accountName, value);
		return "Foram retirados R$ " + value + "! Seu novo saldo é de R$ " + newBalance;
	}
	
	@GetMapping(value = "/get")
	public Account getAccount(String accountName) {
		return accountService.getAccountByName(accountName);
	}
	
}
