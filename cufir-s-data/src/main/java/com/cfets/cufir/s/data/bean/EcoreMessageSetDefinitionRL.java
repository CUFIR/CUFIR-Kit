package com.cfets.cufir.s.data.bean;

import java.util.Date;

/**
 * 报文集和报文关系映射
 * @author tangmaoquan_ntt
 * @since 3.2.1.0
 * @Date 2021年9月17日
 */
public class EcoreMessageSetDefinitionRL {

	/**
	 * 报文id
	 */
	private String messageId;
	
	/**
	 * 报文集id
	 */
	private String setId;
	
	/**
	 * 是否是iso20022官方（1是）
	 */
	private String isfromiso20022;
	private Date createTime;
	private Date updateTime;
	private String createUser;
	private String updateUser;
	public String getIsfromiso20022() {
		return isfromiso20022;
	}
	public void setIsfromiso20022(String isfromiso20022) {
		this.isfromiso20022 = isfromiso20022;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getSetId() {
		return setId;
	}
	public void setSetId(String setId) {
		this.setId = setId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	
}
