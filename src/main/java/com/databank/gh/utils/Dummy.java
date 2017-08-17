package com.databank.gh.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.databank.gh.model.CustDetails;
import com.databank.gh.model.ProductXPrice;
import com.databank.gh.model.TransDetails;

@Service
public class Dummy implements IBalanceCheckerMethods {

	@Override
	public ArrayList<CustDetails> getCustDetails(String searchString) {
		ArrayList<CustDetails>list = new ArrayList<CustDetails>();
		CustDetails a= new CustDetails();
		a.setIssAcc("12345678910");
		a.setDocRef("1350");
		a.setName("Bernard Osei - Aning");
		a.setMaidenName("Nuamah");
		a.setMilesAcc("135894589");
		a.setMobilePhone("0244599114");
		a.setEmail("bernard.oseianing@gmail.com");
		CustDetails b= new CustDetails();
		b.setIssAcc("12345678910");
		b.setName("Sarah Cecelia Osei - Aning");
		b.setMaidenName("Nuamah");
		b.setMilesAcc("15987");
		b.setDocRef("136987");
		b.setMobilePhone("024430334");
		b.setEmail("sarah.oseianing.oseianing@gmail.com");
		list.add(a);
		list.add(b);
		return list;
		
		
		
		
	}

	@Override
	public ArrayList<TransDetails> getAccountTransactions(String accountNumber) {
		// TODO Auto-generated method stub
		ArrayList<TransDetails>list = new ArrayList<TransDetails>();
		
		TransDetails a= new TransDetails();
		a.setMutualFund("EPACK");
		a.setUnits(new BigDecimal(1258.63));
		a.setBalance(new BigDecimal(1000.00));
		
		TransDetails b= new TransDetails();
		b.setMutualFund("MFUND");
		b.setUnits(new BigDecimal(1589));
		b.setBalance(new BigDecimal(988.26));
		
		list.add(a);
		list.add(b);
		
		return list;
	}

	@Override
	public HashMap<String, ProductXPrice> getCurrentPriceList() {
		// TODO Auto-generated method stub
		return null;
	}

}
