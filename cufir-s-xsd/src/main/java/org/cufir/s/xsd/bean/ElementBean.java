package org.cufir.s.xsd.bean;
import java.util.List;

import lombok.Data;

/**
 * 要素
 * @author tangmaoquan
 */
@Data
public class ElementBean {
	
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
	private List<ElementBean>list;
	private List<ComplexBean> lists;
	private List<ElementBean> codes;
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
