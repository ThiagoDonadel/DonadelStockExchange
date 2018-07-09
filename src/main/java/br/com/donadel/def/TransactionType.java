package br.com.donadel.def;

public enum TransactionType {

	BUY("COMPRA"), SELL("VENDA");

	private String caption;

	private TransactionType(String caption) {
		this.caption = caption;
	}

	public String getCaption() {
		return this.caption;
	}

}
