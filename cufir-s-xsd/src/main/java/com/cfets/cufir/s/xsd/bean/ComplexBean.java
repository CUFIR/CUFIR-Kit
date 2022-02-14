package com.cfets.cufir.s.xsd.bean;

import java.util.List;

public class ComplexBean {
	private String name;
	private String type;
	private String minOccurs;
	private String maxOccurs;
	private String TypeId;
	private String DataType;
	private List<ComplexBean> list;
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
	public String getMinOccurs() {
		return minOccurs;
	}
	public void setMinOccurs(String minOccurs) {
		this.minOccurs = minOccurs;
	}
	public String getMaxOccurs() {
		return maxOccurs;
	}
	public void setMaxOccurs(String maxOccurs) {
		this.maxOccurs = maxOccurs;
	}
	
	public String getTypeId() {
		return TypeId;
	}
	public void setTypeId(String typeId) {
		TypeId = typeId;
	}
	public String getDataType() {
		return DataType;
	}
	public void setDataType(String dataType) {
		DataType = dataType;
	}
	public List<ComplexBean> getList() {
		return list;
	}
	public void setList(List<ComplexBean> list) {
		this.list = list;
	}

}
