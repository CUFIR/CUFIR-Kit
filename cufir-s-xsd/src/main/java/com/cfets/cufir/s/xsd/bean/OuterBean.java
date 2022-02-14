package com.cfets.cufir.s.xsd.bean;

import java.util.List;

public class OuterBean {
	private String name;
	private String type;
	private String dataType;

	private List<OuterBean> list;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public List<OuterBean> getList() {
		return list;
	}

	public void setList(List<OuterBean> list) {
		this.list = list;
	}
	
}
