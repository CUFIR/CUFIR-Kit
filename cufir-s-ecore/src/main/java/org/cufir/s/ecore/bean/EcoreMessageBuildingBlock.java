package org.cufir.s.ecore.bean;

import java.util.Date;

import org.cufir.s.ide.db.Table;
import org.cufir.s.ide.db.TableColumn;

import lombok.Data;

/**
 * 报文关联信息（元素、组件）
 */
@Data
@Table(name = "ECORE_MESSAGE_BUILDING_BLOCK")
public class EcoreMessageBuildingBlock {

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
	
	@TableColumn(name = "XML_TAG")
	private String xmlTag;
	
	@TableColumn(name = "DATA_TYPE")
	private String dataType;
	
	@TableColumn(name = "DATA_TYPE_ID")
	private String dataTypeId;
	
	@TableColumn(name = "MAX_OCCURS")
	private Integer maxOccurs;
	
	@TableColumn(name = "MIN_OCCURS")
	private Integer minOccurs;
	
	@TableColumn(name = "MESSAGE_ID")
	private String messageId;
	
	@TableColumn(name = "VERSION")
	private String version;
	
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

