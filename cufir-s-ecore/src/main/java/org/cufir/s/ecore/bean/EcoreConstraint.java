package org.cufir.s.ecore.bean;

import java.util.Date;

import org.cufir.s.ide.db.Table;
import org.cufir.s.ide.db.TableColumn;

import lombok.Data;

/**
 * 约束表
 */
@Data
@Table(name = "ECORE_CONSTRAINT", note = "约束表")
public class EcoreConstraint {
	
	@TableColumn(name = "ID", note = "ID")
	private String id;
	
	@TableColumn(name = "OBJ_ID", note = "对应类型表id")
	private String obj_id;
	
	@TableColumn(name = "OBJ_TYPE", note = "关联表，1:dateType、8:ExternalSchema、9:MessageComponent、10:MessageElement、11:MessageDefinition")
	private String obj_type;
	
	@TableColumn(name = "EXPRESSION", note = "表达关联等")
	private String expression;
	
	@TableColumn(name = "EXPRESSION_LANGUAGE")
	private String expressionlanguage;
	
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
