package com.cfets.cufir.s.data.bean;

import java.util.Date;

/**
 * 自定义外部数据类型
 * @author tangmaoquan_ntt
 * @since 3.2.1.0
 * @Date 2021年9月18日
 */
public class EcoreExternalSchema {
	
	private String id;
	private String name;
	private String definition;
	private String registrationStatus;
	private Date removalDate;
	private String objectIdentifier;
	private String processContent;
	private String technical;
	private String previousVersion;
	private String isfromiso20022;
	private Date createTime;
	private Date updateTime;
	private String createUser;
	private String updateUser;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDefinition() {
		return definition;
	}
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	public String getRegistrationStatus() {
		return registrationStatus;
	}
	public void setRegistrationStatus(String registrationStatus) {
		this.registrationStatus = registrationStatus;
	}
	public Date getRemovalDate() {
		return removalDate;
	}
	public void setRemovalDate(Date removalDate) {
		this.removalDate = removalDate;
	}
	public String getObjectIdentifier() {
		return objectIdentifier;
	}
	public void setObjectIdentifier(String objectIdentifier) {
		this.objectIdentifier = objectIdentifier;
	}
	public String getProcessContent() {
		return processContent;
	}
	public void setProcessContent(String processContent) {
		this.processContent = processContent;
	}
	public String getTechnical() {
		return technical;
	}
	public void setTechnical(String technical) {
		this.technical = technical;
	}
	public String getIsfromiso20022() {
		return isfromiso20022;
	}
	public void setIsfromiso20022(String isfromiso20022) {
		this.isfromiso20022 = isfromiso20022;
	}
	public String getPreviousVersion() {
		return previousVersion;
	}
	public void setPreviousVersion(String previousVersion) {
		this.previousVersion = previousVersion;
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

