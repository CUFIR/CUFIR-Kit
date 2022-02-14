package com.cfets.cufir.s.xsd.bean;

import java.util.List;

public class SecurityBean {

	private String content;

	private List<TypeBean> types;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<TypeBean> getTypes() {
		return types;
	}

	public void setTypes(List<TypeBean> types) {
		this.types = types;
	}

}
