package com.cfets.cufir.s.xsd.bean;

import java.util.List;

public class ElementNode {
	
	private String name;//元素名
	private String type;//类型名
	private String dataType;//数据类型
	private String xmlTag; //标签
	private String TypeId;//数据类型Id
	private String dataTypeId;//数据类型Id
	private String ComponentType;//复合表Type
	private String ComponentId;//复合表id
	private String codeName;//code名
	private String maxOccurs;//最大出现次数
	private String minOccurs;//最小出现次数
	private String pattern;//使用正则表达式约束可以出现的字符
	private String fractionDigits;//小数位数
	private String totalDigits;//最大位数
	private String minInclusive;//允许数值的下限，可等于下限值
	private String maxInclusive;//允许数值的上限，可等于上限值
	private String minExclusive;//允许数值的下限，不能等于下限值
	private String maxExclusive;//允许数值的上限，不能等于上限值
	private String minLength;//最小长度
	private String maxLength;//最大长度
	private List<ElementNode>list;
	private List<ComplexBean> lists;
	private List<ElementNode> codes;
	public ElementNode(String name) {
		this.name=name;
	}
	public ElementNode() {
	}
	public ElementNode(String name,String type) {
		super();
		this.name=name;
		this.type=type;
	}
	
	public ElementNode(String name,String type,String maxOccurs,String minOccurs) {
		super();
		this.name=name;
		this.type=type;
		this.maxOccurs=maxOccurs;
		this.minOccurs=minOccurs;
	}
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
	
	public String getTypeId() {
		return TypeId;
	}
	public void setTypeId(String typeId) {
		TypeId = typeId;
	}
	public String getXmlTag() {
		return xmlTag;
	}
	public void setXmlTag(String xmlTag) {
		this.xmlTag = xmlTag;
	}
	public String getMaxOccurs() {
		return maxOccurs;
	}
	public void setMaxOccurs(String maxOccurs) {
		this.maxOccurs = maxOccurs;
	}
	public String getMinOccurs() {
		return minOccurs;
	}
	public void setMinOccurs(String minOccurs) {
		this.minOccurs = minOccurs;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getDataTypeId() {
		return dataTypeId;
	}
	public void setDataTypeId(String dataTypeId) {
		this.dataTypeId = dataTypeId;
	}
	
	public String getComponentType() {
		return ComponentType;
	}
	public void setComponentType(String componentType) {
		ComponentType = componentType;
	}
	public String getComponentId() {
		return ComponentId;
	}
	public void setComponentId(String componentId) {
		ComponentId = componentId;
	}
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	public String getFractionDigits() {
		return fractionDigits;
	}
	public void setFractionDigits(String fractionDigits) {
		this.fractionDigits = fractionDigits;
	}
	public String getTotalDigits() {
		return totalDigits;
	}
	public void setTotalDigits(String totalDigits) {
		this.totalDigits = totalDigits;
	}
	public String getMinInclusive() {
		return minInclusive;
	}
	public void setMinInclusive(String minInclusive) {
		this.minInclusive = minInclusive;
	}
	public String getMinLength() {
		return minLength;
	}
	public void setMinLength(String minLength) {
		this.minLength = minLength;
	}
	public String getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(String maxLength) {
		this.maxLength = maxLength;
	}
	public String getMaxInclusive() {
		return maxInclusive;
	}
	public void setMaxInclusive(String maxInclusive) {
		this.maxInclusive = maxInclusive;
	}
	public String getMinExclusive() {
		return minExclusive;
	}
	public void setMinExclusive(String minExclusive) {
		this.minExclusive = minExclusive;
	}
	public String getMaxExclusive() {
		return maxExclusive;
	}
	public void setMaxExclusive(String maxExclusive) {
		this.maxExclusive = maxExclusive;
	}
	public List<ElementNode> getList() {
		return list;
	}
	public void setList(List<ElementNode> list) {
		this.list = list;
	}
	public List<ElementNode> getCodes() {
		return codes;
	}
	public void setCodes(List<ElementNode> codes) {
		this.codes = codes;
	}
	public List<ComplexBean> getLists() {
		return lists;
	}
	public void setLists(List<ComplexBean> lists) {
		this.lists = lists;
	}
	
	
}
