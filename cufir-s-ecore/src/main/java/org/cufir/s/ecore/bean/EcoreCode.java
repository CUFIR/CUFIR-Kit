package org.cufir.s.ecore.bean;

import java.util.Date;

import org.cufir.s.ide.db.Table;
import org.cufir.s.ide.db.TableColumn;

import lombok.Data;

/**
 * 简单数据（Code Sets的基本CODE）
 */
@Data
@Table(name = "ECORE_CODE", note = "简单数据")
public class EcoreCode {

	@TableColumn(name = "ID", note = "ID")
	private String id;
	
	@TableColumn(name = "NAME", note = "名称")
	private String name;
	
	@TableColumn(name = "CODE_NAME", note = "简称")
	private String codeName;
	
	@TableColumn(name = "DEFINITION", note = "描述")
	private String definition;
	
	@TableColumn(name = "CODE_SET_ID", note = "关联dateType表id，多对一关系")
	private String codesetid;
	
	@TableColumn(name = "REGISTRATION_STATUS", note = "登记状态（Registered登记、 Provisionally Registered暂时登记、Added Registered新增、Obsolete过时）")
	private String registrationStatus;
	
	@TableColumn(name = "REMOVAL_DATE", note = "删除时间")
	private Date removalDate;
	
	@TableColumn(name = "OBJECT_IDENTIFIER")
	private String objectIdentifier;
	
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
