package org.cufir.s.ecore.bean;

import java.util.Date;

import org.cufir.s.ide.db.Table;
import org.cufir.s.ide.db.TableColumn;

import lombok.Data;

/**
 * EcoreBusinessComponent扩展
 */
@Data
@Table(name = "ECORE_BUSINESS_COMPONENT_RL", note = "业务组件扩展")
public class EcoreBusinessComponentRL {

	@TableColumn(name = "ID", note = "ID")
	private String id;
	
	@TableColumn(name = "P_ID", note = "多对一关联BusinessComponent表ID")
	private String pId;
	
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
