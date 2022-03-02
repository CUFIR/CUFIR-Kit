package org.cufir.s.xsd.bean;

import lombok.Data;

/**
 * xsd简单类型元素子节点属性
 */
@Data
public class AttributeBean {

	/**
	 * 简单类型元素子元素名
	 */
	private String name;
	
	/**
	 * 简单类型元素子元素值
	 */
	private String defaultvalue;
}
