package com.databank.gh.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.databank.gh.model.CustDetails;
import com.databank.gh.model.TransDetails;
import com.databank.gh.utils.IBalanceCheckerMethods;
import com.databank.gh.utils.IDocManMethods;

@RestController
public class MainRestController {

	private IBalanceCheckerMethods balanceCheckerMethods;
	
	private IDocManMethods docManMethods;

	private static HashMap<String, String> optionMap = new HashMap<String, String>();
	static {
		optionMap.put("name", "WHERE ACC.CLIENT_FNAME LIKE ");
		optionMap.put("iss-acc", "where CLI.[UCC] LIKE ");
		optionMap.put("miles-acc", "where ACC.[mapcode] LIKE ");
	}

	@Autowired
	@Qualifier("balanceCheckerMethods")
	//@Qualifier("dummy")
	public void setBalanceCheckerMethods(IBalanceCheckerMethods balanceCheckerMethods) {
		this.balanceCheckerMethods = balanceCheckerMethods;
	}
	
	
	public void setDocManMethods(IDocManMethods docManMethods) {
		this.docManMethods = docManMethods;
	}



	public static void setOptionMap(HashMap<String, String> optionMap) {
		MainRestController.optionMap = optionMap;
	}



	@RequestMapping(value = "/getAccountDetails/{milesAcc}", method = RequestMethod.GET)
	public ArrayList<TransDetails> getAccountDetails(@PathVariable("milesAcc") String milesAcc) {

		return balanceCheckerMethods.getAccountTransactions(milesAcc);
	}
	
	@RequestMapping(value = "/getDocRef/{milesAcc}", method = RequestMethod.GET)
	public Map<String,String> getDocRef(@PathVariable("milesAcc") String milesAcc) {

		Map<String,String> a = new HashMap<String,String>();
		a.put("docRef","1");
		return a;
		
	}
	
	//@RequestMapping(path = "/mno/objectKey/{id}/{name}", method = RequestMethod.GET)
	//public Book getBook(@PathVariable int id, @PathVariable String name) {
	    // code here
	//}

	@RequestMapping(value = "/getCustList/{searchOption}/{searchValue}", method = RequestMethod.GET)
	public ArrayList<CustDetails> getData(@PathVariable("searchValue") String searchValue,@PathVariable("searchOption") String searchOption) throws InterruptedException {

		if (!searchOption.contains("name")) {
			// MILES DOES NOT STORE THE LEADING ZEROS FOR ISS NUMBERS SO IF A
			// USER ENTERS 000123 WHICH IS A STRING
			// IT WILL NOT BE FOUND so fix it here

			try {
				searchValue = Long.valueOf(searchValue.trim()).toString();

				return balanceCheckerMethods.getCustDetails(optionMap.get(searchOption) + "'%" + searchValue + "%'");
			} catch (Exception e) {
				return new ArrayList<CustDetails>();   //when an invalid number is entered just catch the exception and return nothing
			}

		} else {

			String dummyString = searchValue.replace(' ', '%');

			return balanceCheckerMethods
					.getCustDetails(optionMap.get(searchOption) + "'%" + dummyString.toUpperCase() + "%'");
		}

	}

}
