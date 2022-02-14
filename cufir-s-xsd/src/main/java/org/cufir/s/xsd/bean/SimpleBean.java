package org.cufir.s.xsd.bean;
import java.util.ArrayList;
import java.util.List;


import lombok.Data;

/**
 * xsd简单类型元素
 * @author tangmaoquan
 */
@Data
public class SimpleBean {
	private String name;//元素名
	private String value;//元素值
	private String codeName;//code名
	
	private List<AttributeBean> attributes=new ArrayList<>();//子元素属性列表

	
	
}
