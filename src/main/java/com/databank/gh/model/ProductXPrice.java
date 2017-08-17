package com.databank.gh.model;

import java.math.BigDecimal;
import java.util.Date;

public class ProductXPrice {
	String fundId;
	BigDecimal price;
	Date navDate;
	public String getFundId() {
		return fundId;
	}
	public void setFundId(String fundId) {
		this.fundId = fundId;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Date getNavDate() {
		return navDate;
	}
	public void setNavDate(Date navFate) {
		this.navDate = navFate;
	}

}
