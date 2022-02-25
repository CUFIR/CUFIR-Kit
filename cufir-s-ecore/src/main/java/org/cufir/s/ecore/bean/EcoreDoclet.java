package org.cufir.s.ecore.bean;

import java.util.Date;

import lombok.Data;

@Data
public class EcoreDoclet {

	private String id;
	
	/**
	 * 数据集id
	 */
	private String obj_id;
	private String obj_type;
	private String type;
	private String content;
	private String isfromiso20022;
	private Date createTime;
	private Date updateTime;
	private String createUser;
	private String updateUser;
}
