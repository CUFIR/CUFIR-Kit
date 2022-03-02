package org.cufir.s.xsd.bean;
import java.util.List;

import lombok.Data;

/**
 * 要素
 */
@Data
public class ElementBean {
	
	/**
	 * 元素名
	 */
	private String name;
	
	/**
	 * 类型名
	 */
	private String type;
	
	/**
	 * 数据类型
	 */
	private String dataType;
	
	/**
	 * 标签
	 */
	private String xmlTag;
	
	/**
	 * 数据类型Id
	 */
	private String TypeId;
	
	/**
	 * 数据类型Id
	 */
	private String dataTypeId;
	
	/**
	 * 复合表Type
	 */
	private String ComponentType;
	
	/**
	 * 复合表id
	 */
	private String ComponentId;
	
	/**
	 * code名
	 */
	private String codeName;
	
	/**
	 * 最大出现次数
	 */
	private String maxOccurs;
	
	/**
	 * 最小出现次数
	 */
	private String minOccurs;
	
	/**
	 * 使用正则表达式约束可以出现的字符
	 */
	private String pattern;
	
	/**
	 * 小数位数
	 */
	private String fractionDigits;
	
	/**
	 * 最大位数
	 */
	private String totalDigits;
	
	/**
	 * 允许数值的下限，可等于下限值
	 */
	private String minInclusive;
	
	/**
	 * 允许数值的上限，可等于上限值
	 */
	private String maxInclusive;
	
	/**
	 * 允许数值的下限，不能等于下限值
	 */
	private String minExclusive;
	
	/**
	 * 允许数值的上限，不能等于上限值
	 */
	private String maxExclusive;
	
	/**
	 * 最小长度
	 */
	private String minLength;
	
	/**
	 * 最大长度
	 */
	private String maxLength;
	
	/**
	 * 一般元素
	 */
	private List<ElementBean>conmmonlyElements;
	
	/**
	 * code元素
	 */
	private List<ElementBean> codeElements;
	
	public ElementBean(String name) {
		this.name=name;
	}
	
	public ElementBean() {
	}
	
	public ElementBean(String name,String type) {
		super();
		this.name=name;
		this.type=type;
	}
	
	public ElementBean(String name,String type,String maxOccurs,String minOccurs) {
		super();
		this.name=name;
		this.type=type;
		this.maxOccurs=maxOccurs;
		this.minOccurs=minOccurs;
	}
	
	
}
