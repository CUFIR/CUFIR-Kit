package com.cfets.cufir.s.xsd.bean;

import java.util.List;

public class Headean {
	
	private String messaage;
	
	private List<TypeBean> types;

	public String getMessaage() {
		return messaage;
	}

	public void setMessaage(String messaage) {
		this.messaage = messaage;
	}

	public List<TypeBean> getTypes() {
		return types;
	}

	public void setTypes(List<TypeBean> types) {
		this.types = types;
	}

}
