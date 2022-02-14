package org.cufir.s.xsd.bean;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * xsd复杂类型元素
 * @author tangmaoquan
 */
@Data
public class ComplexBean {
	
	private String name;//元素名
	private String type;//类型名
	private String minOccurs;//最小出现次数
	private String maxOccurs;//最大出现次数
	private String TypeId;//数据类型Id
	private String DataType;//数据类型
	
	private List<ComplexBean> complexes = new ArrayList<>();//子元素列表（复杂类型元素）
	private List<SimpleBean> simples = new ArrayList<>();//子元素列表（简单类型元素）
	
	


}
