package org.cufir.s.xsd.bean;
import java.util.ArrayList;
import java.util.List;


import lombok.Data;

/**
 * xsd简单类型元素
 */
@Data
public class SimpleBean {
	
	/**
	 * 元素名
	 */
	private String name;
	
	/**
	 * 元素值
	 */
	private String value;
	
	/**
	 * code名
	 */
	private String codeName;
	
	/**
	 * 子元素属性列表
	 */
	private List<AttributeBean> attributes=new ArrayList<>();
}
