package com.databank.gh.utils;

import java.util.ArrayList;
import java.util.HashMap;

import com.databank.gh.model.CustDetails;
import com.databank.gh.model.ProductXPrice;
import com.databank.gh.model.TransDetails;

public interface IBalanceCheckerMethods {
	
public ArrayList<CustDetails> getCustDetails(String searchString);
	
	public ArrayList<TransDetails>getAccountTransactions(String accountNumber);	
	
	
	public HashMap<String,ProductXPrice>getCurrentPriceList();

}
