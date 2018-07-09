package br.com.donadel.def.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import br.com.donadel.def.TransactionType;

@Entity
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column
	private String stockName;

	@Column
	@Enumerated(EnumType.STRING)
	private TransactionType type;

	@Column
	private Double stockPrice;

	@Column
	private Integer stockAmount;

	@Column
	private Double balance;

	@Column
	private LocalDateTime date;

	@ManyToOne
	private Account account;

	public Transaction() {

	}

	public Transaction(String stockName, TransactionType type, Double stockPrice, Integer stockAmount, Double balance, Account account) {
		super();
		this.stockName = stockName;
		this.type = type;
		this.stockPrice = stockPrice;
		this.stockAmount = stockAmount;
		this.balance = balance;
		this.date = LocalDateTime.now();
		this.account = account;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public Double getStockPrice() {
		return stockPrice;
	}

	public void setStockPrice(Double stockPrice) {
		this.stockPrice = stockPrice;
	}

	public Integer getStockAmount() {
		return stockAmount;
	}

	public void setStockAmount(Integer stockAmount) {
		this.stockAmount = stockAmount;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	@PrePersist
	public void setCurrentTime() {
		this.date = LocalDateTime.now();
	}

	public String toString() {
		String formated = "[%s] %s - Ação: %s;Preço: %.2f;Quantidade: %d; Saldo: %.2f";
		formated = String.format(formated, date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), type.getCaption(), stockName, stockPrice, stockAmount, balance);
		return formated;
	}

}
