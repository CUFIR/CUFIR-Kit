package org.cufir.s.xsd.bean;
import java.util.List;

import lombok.Data;

/**
 * 外部元素
 * @author tangmaoquan
 */
@Data
public class OuterBean {
	private String name;
	private String type;
	private String dataType;

	private List<OuterBean> list;


	
}
