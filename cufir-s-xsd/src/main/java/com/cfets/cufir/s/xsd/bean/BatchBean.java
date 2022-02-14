package com.cfets.cufir.s.xsd.bean;

import java.util.List;

public class BatchBean {
	
	private String transaction;
	
	private List<TypeBean> types;

	public String getTransaction() {
		return transaction;
	}

	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}

	public List<TypeBean> getTypes() {
		return types;
	}

	public void setTypes(List<TypeBean> types) {
		this.types = types;
	}

}
