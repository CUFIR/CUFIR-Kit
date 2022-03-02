package org.cufir.s.ecore.bean;

import java.util.Date;

import org.cufir.s.ide.db.Table;
import org.cufir.s.ide.db.TableColumn;

import lombok.Data;

/**
 * 业务要素
 */
@Data
@Table(name = "ECORE_BUSINESS_ELEMENT", note = "业务要素")
public class EcoreBusinessElement {

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
	
	@TableColumn(name = "PREVIOUS_VERSION")
	private String previousVersion;
	
	@TableColumn(name = "TYPE", note = "1:dataType表类型、2BusinessComponentRL表类型")
	private String type;
	
	@TableColumn(name = "TYPE_ID", note = "一对一关系，1:dataType表ID、2BusinessComponentRL表ID")
	private String typeId;
	
	@TableColumn(name = "MAX_OCCURS", note = "最大出现次数")
	private Integer maxOccurs;
	
	@TableColumn(name = "MIN_OCCURS", note = "最小出现次数")
	private Integer minOccurs;
	
	@TableColumn(name = "BUSINESS_COMPONENT_ID", note = "一对一关系，业务组件id")
	private String businessComponentId;
	
	@TableColumn(name = "IS_DERIVED")
	private String isDerived;
	
	@TableColumn(name = "VERSION")
	private String version;
	
	@TableColumn(name = "IS_FROM_ISO20022", note = "是否ISO20022，1是")
	private String isfromiso20022;
	
	@TableColumn(name = "IS_MESSAGE_ASSOCIATION_END", note = "是否关联结束")
	private String isMessageAssociationEnd;
	
	@TableColumn(name = "CREATE_TIME")
	private Date createTime;
	
	@TableColumn(name = "UPDATE_TIME")
	private Date updateTime;
	
	@TableColumn(name = "CREATE_USER")
	private String createUser;
	
	@TableColumn(name = "UPDATE_USER")
	private String updateUser;
	
	private String typeName;
}
