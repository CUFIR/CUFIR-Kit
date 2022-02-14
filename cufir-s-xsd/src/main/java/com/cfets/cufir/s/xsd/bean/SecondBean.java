package com.cfets.cufir.s.xsd.bean;

import java.util.List;

public class SecondBean {
	private String name;
	private String type;
	private String xmlTag;
	private String dataType;
	private String minOccurs;
	private String maxOccurs;
	private List<SecondBean> list;
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
	public String getXmlTag() {
		return xmlTag;
	}
	public void setXmlTag(String xmlTag) {
		this.xmlTag = xmlTag;
	}
	public List<SecondBean> getList() {
		return list;
	}
	public void setList(List<SecondBean> list) {
		this.list = list;
	}
	
	
}
