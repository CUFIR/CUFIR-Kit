package org.cufir.s.ecore.bean;

import java.util.Date;

import lombok.Data;

/**
 * 报文集和报文关系映射表
 * @author tangmaoquan_ntt
 * @since 1.0.0
 */
@Data
@Table(name = "ECORE_MESSAGE_SET_DEFINITION_RL", note = "报文集和报文关系映射表")
public class EcoreMessageSetDefinitionRL {

	@TableColumn(name = "MESSAGE_ID", note = "报文ID")
	private String messageId;
	
	@TableColumn(name = "SET_ID", note = "报文集ID")
	private String setId;
	
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
