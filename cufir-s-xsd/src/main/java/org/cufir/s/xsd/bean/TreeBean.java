package org.cufir.s.xsd.bean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import lombok.Data;

/**
 * xsd文件各个元素列表
 * @author tangmaoquan
 * @Date 2021年10月15日
 */
@Data
public class TreeBean {
	
	private List<ComplexBean> complexBeans;//复杂类型元素bean列表
	private List<SimpleBean> simpleBeans;//简单类型元素bean列表

	
	private Map<String, Object> xmlTypeMap = new HashMap<String, Object>();//xsd文件元素名字和元素对应map
	private Map<Object, Object> xmlCompTypeMap = new HashMap<Object, Object>();//xsd元素和子元素列表对应map
	//private Map<Object, Object> xmlSimpleTypeMap = new HashMap<Object, Object>();
	
	
}
