package com.cfets.cufir.s.data.bean;

import java.util.Date;

/**
 * 元素和组件
 * @author tangmaoquan_ntt
 * @since 3.2.1.0
 * @Date 2021年9月18日
 */
public class EcoreMessageComponent {

	private String id;
	private String definition;
	private String name;
	private String registrationStatus;
	private Date removalDate;
	private String objectIdentifier;
	private String previousVersion;
	private String componentType;
	private String version;
	/**
	 * true/false 
	 */
	private String technical;
	private String trace;
	private String traceName;
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
	public String getDefinition() {
		return definition;
	}
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getPreviousVersion() {
		return previousVersion;
	}
	public void setPreviousVersion(String previousVersion) {
		this.previousVersion = previousVersion;
	}
	public String getComponentType() {
		return componentType;
	}
	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getIsfromiso20022() {
		return isfromiso20022;
	}
	public void setIsfromiso20022(String isfromiso20022) {
		this.isfromiso20022 = isfromiso20022;
	}
	public String getTrace() {
		return trace;
	}
	public void setTrace(String trace) {
		this.trace = trace;
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
	public String getTechnical() {
		return technical;
	}
	public void setTechnical(String technical) {
		this.technical = technical;
	}
	public String getTraceName() {
		return traceName;
	}
	public void setTraceName(String traceName) {
		this.traceName = traceName;
	}
	
}

