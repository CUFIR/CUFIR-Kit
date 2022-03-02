package org.cufir.s.ecore.bean;

import java.util.Date;

import org.cufir.s.ide.db.Table;
import org.cufir.s.ide.db.TableColumn;

import lombok.Data;

/**
 * 版本关联
 */
@Data
@Table(name = "ECORE_NEXT_VERSIONS", note = "版本关联")
public class EcoreNextVersions {

	@TableColumn(name = "ID", note = "当前版本ID")
	private String id;
	
	@TableColumn(name = "NEXT_VERSION_ID", note = "关联版本ID")
	private String nextVersionId;
	
	@TableColumn(name = "IS_FROM_ISO20022", note = "是否ISO20022，1是")
	private String isfromiso20022;
	
	@TableColumn(name = "OBJ_TYPE", note = "关联表，1:dateType、2:BusinessComponent、3:BusinessComponent、8:ExternalSchema、9:MessageComponent、10:MessageElement、11:MessageDefinition")
	private String objType;
	
	@TableColumn(name = "CREATE_TIME")
	private Date createTime;
	
	@TableColumn(name = "UPDATE_TIME")
	private Date updateTime;
	
	@TableColumn(name = "CREATE_USER")
	private String createUser;
	
	@TableColumn(name = "UPDATE_USER")
	private String updateUser;
}
