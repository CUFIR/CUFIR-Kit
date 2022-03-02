package org.cufir.s.xsd.bean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import lombok.Data;

/**
 * xsd文件各个元素列表
 */
@Data
public class TreeBean {
	
	/**
	 * 复杂类型元素bean列表
	 */
	private List<ComplexBean> complexBeans;
	
	/**
	 * 简单类型元素bean列表
	 */
	private List<SimpleBean> simpleBeans;

	/**
	 * xsd文件元素名字和元素对应map
	 */
	private Map<String, Object> xmlTypeMap = new HashMap<String, Object>();
	
	/**
	 * xsd元素和子元素列表对应map
	 */
	private Map<Object, Object> xmlCompTypeMap = new HashMap<Object, Object>();
	
	
}
