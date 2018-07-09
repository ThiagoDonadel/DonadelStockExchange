package br.com.donadel.def.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class StockMonitor {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column
	private String name;

	@Column
	private Double buyValue;

	@Column
	private Double sellValue;

	@Column
	private Double disponibleBalance;

	@Column
	private Integer quantity;

	@ManyToOne
	private Account account;

	public StockMonitor() {
		super();
		quantity = 0;
	}

	public StockMonitor(String name, Double buyValue, Double sellValue, Double disponibleBalance) {
		this();
		this.name = name;
		this.buyValue = buyValue;
		this.sellValue = sellValue;
		this.disponibleBalance = disponibleBalance;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getBuyValue() {
		return buyValue;
	}

	public void setBuyValue(Double buyValue) {
		this.buyValue = buyValue;
	}

	public Double getSellValue() {
		return sellValue;
	}

	public void setSellValue(Double selValue) {
		this.sellValue = selValue;
	}

	public Double getDisponibleBalance() {
		return disponibleBalance;
	}

	public void setDisponibleBalance(Double disponibleBalance) {
		this.disponibleBalance = disponibleBalance;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String format() {
		String formated = "Ação: %s; PV: %.2f; PC: %.2f; Quantidade: %d; Saldo: %.2f";
		formated = String.format(formated, name, sellValue, buyValue, quantity, disponibleBalance);
		return formated;
	}

}
