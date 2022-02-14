package com.cfets.cufir.s.xsd.bean;

import java.util.List;

public class ThirdBean {

	private String name;
	private String type;
	private int minOccurs;
	private int maxOccurs;
	private List<ThirdBean> list;
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
	public int getMinOccurs() {
		return minOccurs;
	}
	public void setMinOccurs(int minOccurs) {
		this.minOccurs = minOccurs;
	}
	public int getMaxOccurs() {
		return maxOccurs;
	}
	public void setMaxOccurs(int maxOccurs) {
		this.maxOccurs = maxOccurs;
	}
	public List<ThirdBean> getList() {
		return list;
	}
	public void setList(List<ThirdBean> list) {
		this.list = list;
	}
	
	
}
