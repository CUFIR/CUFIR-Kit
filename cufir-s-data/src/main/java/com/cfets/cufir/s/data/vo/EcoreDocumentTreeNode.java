package com.cfets.cufir.s.data.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EcoreDocumentTreeNode implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String index;
	private int lvl;
	private String name;
	private String xmlTag;
	private String mult;
	private String type;
	private String constraint;
	private String dataTypeId;
	private String dataType;
	private List<EcoreDocumentTreeNode> childNodes=new ArrayList<EcoreDocumentTreeNode>();
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public int getLvl() {
		return lvl;
	}
	public void setLvl(int lvl) {
		this.lvl = lvl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getXmlTag() {
		return xmlTag;
	}
	public void setXmlTag(String xmlTag) {
		this.xmlTag = xmlTag;
	}
	public String getMult() {
		return mult;
	}
	public void setMult(String mult) {
		this.mult = mult;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getConstraint() {
		return constraint;
	}
	public void setConstraint(String constraint) {
		this.constraint = constraint;
	}
	public List<EcoreDocumentTreeNode> getChildNodes() {
		return childNodes;
	}
	public void setChildNodes(List<EcoreDocumentTreeNode> childNodes) {
		this.childNodes = childNodes;
	}
	public String getDataTypeId() {
		return dataTypeId;
	}
	public void setDataTypeId(String dataTypeId) {
		this.dataTypeId = dataTypeId;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
}
