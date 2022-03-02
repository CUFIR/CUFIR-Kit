package org.cufir.s.xsd.bean;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * xsd复杂类型元素
 */
@Data
public class ComplexBean {
	
	/**
	 * 元素名
	 */
	private String name;
	
	/**
	 * 类型名
	 */
	private String type;
	
	/**
	 * 最小出现次数
	 */
	private String minOccurs;
	
	/**
	 * 最大出现次数
	 */
	private String maxOccurs;
	
	/**
	 * 数据类型Id
	 */
	private String TypeId;
	
	/**
	 * 数据类型
	 */
	private String DataType;
	
	/**
	 * 子元素列表（复杂类型元素）
	 */
	private List<ComplexBean> complexes = new ArrayList<>();
	
	/**
	 * 子元素列表（简单类型元素）
	 */
	private List<SimpleBean> simples = new ArrayList<>();
}
