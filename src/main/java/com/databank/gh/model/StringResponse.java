package com.databank.gh.model;

public class StringResponse {

	 private String response;

	    public StringResponse(String s) { 
	       this.setResponse(s);
	    }

		public String getResponse() {
			return response;
		}

		public void setResponse(String response) {
			this.response = response;
		}
}
