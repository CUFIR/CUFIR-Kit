package org.cufir.s.ecore.bean;

import java.util.Date;

import org.cufir.s.ide.db.Table;
import org.cufir.s.ide.db.TableColumn;

import lombok.Data;

/**
 * 混合类型元素（组件关联组件或元素）
 */
@Data
@Table(name = "ECORE_MESSAGE_ELEMENT", note = "混合类型元素")
public class EcoreMessageElement {

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
	
	@TableColumn(name = "XML_TAG", note = "简称")
	private String xmlTag;
	
	@TableColumn(name = "TYPE")
	private String type;
	
	@TableColumn(name = "TYPE_ID")
	private String typeId;
	
	@TableColumn(name = "MAX_OCCURS")
	private Integer maxOccurs;
	
	@TableColumn(name = "MIN_OCCURS")
	private Integer minOccurs;
	
	@TableColumn(name = "MESSAGE_COMPONENT_ID")
	private String messageComponentId;
	
	@TableColumn(name = "VERSION")
	private String version;
	
	@TableColumn(name = "IS_FROM_ISO20022", note = "是否ISO20022，1是")
	private String isfromiso20022;
	
	@TableColumn(name = "IS_MESSAGE_ASSOCIATION_END")
	private String isMessageAssociationEnd;
	
	@TableColumn(name = "TRACE")
	private String trace = "";
	
	@TableColumn(name = "TRACE_TYPE")
	private String traceType;
	
	@TableColumn(name = "IS_DERIVED")
	private String isDerived;
	
	@TableColumn(name = "CREATE_TIME")
	private Date createTime;
	
	@TableColumn(name = "UPDATE_TIME")
	private Date updateTime;
	
	@TableColumn(name = "CREATE_USER")
	private String createUser;
	
	@TableColumn(name = "UPDATE_USER")
	private String updateUser;
	
	@TableColumn(name = "TRACES_TO")
	private String tracesTo;
	
	@TableColumn(name = "TECHNICAL")
	private String technical;
	
	@TableColumn(name = "TYPE_OF_TRACES_TO")
	private String typeOfTracesTo = "";
	
	@TableColumn(name = "TRACE_PATH")
	private String tracePath;
	
	private String typeName;
}

