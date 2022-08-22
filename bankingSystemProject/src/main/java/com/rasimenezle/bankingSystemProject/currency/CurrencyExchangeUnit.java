package com.rasimenezle.bankingSystemProject.currency;

public interface CurrencyExchangeUnit {
	public double currencyChange(double balance, String accountType, String accountType2);

	public double buyingGramGAU(double balance, String accountType, String accountType2);

	public double sellingGramGAU(double balance, String accountType, String accountType2);

}
