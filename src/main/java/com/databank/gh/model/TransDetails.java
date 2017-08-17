package com.databank.gh.model;

import java.math.BigDecimal;

public class TransDetails {
	
	private String mutualFund;
	
	private BigDecimal units;
	
	private BigDecimal balance;
	
	private BigDecimal currentPrice;
	
	
	

	public BigDecimal getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(BigDecimal currentPrice) {
		this.currentPrice = currentPrice;
	}

	public String getMutualFund() {
		return mutualFund;
	}

	public void setMutualFund(String mutualFund) {
		this.mutualFund = mutualFund;
	}

	public BigDecimal getUnits() {
		return units;
	}

	public void setUnits(BigDecimal units) {
		this.units = units;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	

}
