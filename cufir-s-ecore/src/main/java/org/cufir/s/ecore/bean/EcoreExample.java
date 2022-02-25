package org.cufir.s.ecore.bean;

import java.util.Date;

import lombok.Data;

@Data
@Table(name = "ECORE_EXAMPLE", note = "")
public class EcoreExample {

	@TableColumn(name = "ID", note = "ID")
	private String id;
	
	@TableColumn(name = "OBJ_ID", note = "对应类型表id")
	private String objId;
	
	@TableColumn(name = "OBJ_TYPE", note = "关联表，1:dateType、2:BusinessComponent、3:BusinessComponent、8:ExternalSchema、9:MessageComponent、10:MessageElement、11:MessageDefinition")
	private String objType;
	
	@TableColumn(name = "EXAMPLE")
	private String example;
	
	@TableColumn(name = "IS_FROM_ISO20022", note = "是否ISO20022，1是")
	private String isfromiso20022;
	
	@TableColumn(name = "CREATE_TIME")
	private Date createTime;
	
	@TableColumn(name = "UPDATE_TIME")
	private Date updateTime;
	
	@TableColumn(name = "CREATE_USER")
	private String createUser;
	
	@TableColumn(name = "UPDATE_USER")
	private String updateUser;
}

