package org.cufir.s.ecore.bean;

import java.util.Date;

import org.cufir.s.ide.db.Table;
import org.cufir.s.ide.db.TableColumn;

import lombok.Data;

/**
 * 自定义外部数据类型
 */
@Data
@Table(name = "ECORE_EXTERNAL_SCHEMA")
public class EcoreExternalSchema {
	
	@TableColumn(name = "ID", note = "ID")
	private String id;
	
	@TableColumn(name = "DEFINITION", note = "描述")
	private String definition;
	
	@TableColumn(name = "NAME", note = "名称")
	private String name;
	
	@TableColumn(name = "REGISTRATION_STATUS", note = "登记状态（Registered登记、 Provisionally Registered暂时登记、Added Registered新增、Obsolete过时）")
	private String registrationStatus;
	
	@TableColumn(name = "REMOVAL_DATE", note = "删除时间")
	private Date removalDate;
	
	@TableColumn(name = "OBJECT_IDENTIFIER")
	private String objectIdentifier;
	
	@TableColumn(name = "PROCESS_CONTENT")
	private String processContent;
	
	@TableColumn(name = "TECHNICAL")
	private String technical;
	
	@TableColumn(name = "PREVIOUS_VERSION")
	private String previousVersion;
	
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

