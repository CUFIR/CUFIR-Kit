package com.cfets.cufir.s.data.bean;

import java.util.Date;

public class EcoreMessageDefinition {

	private String id;
	private String definition;
	private String name;
	private String registrationStatus;
	private Date removalDate;
	private String objectIdentifier;
	private String previousVersion;
	
	/**
	 * 业务域id
	 */
	private String businessAreaId;
	private String rootElement;
	private String flavour;
	private String messageFunctionality;
	private String xmlTag;
	private String xmlName;
	private String version;
	private String isfromiso20022;
	
	/**
	 * 业务域
	 */
	private String businessArea;
	private Date createTime;
	private Date updateTime;
	private String createUser;
	private String updateUser;
	private String messageDefinitionIdentifier;
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
	public String getBusinessAreaId() {
		return businessAreaId;
	}
	public void setBusinessAreaId(String businessAreaId) {
		this.businessAreaId = businessAreaId;
	}
	public String getRootElement() {
		return rootElement;
	}
	public void setRootElement(String rootElement) {
		this.rootElement = rootElement;
	}
	public String getFlavour() {
		return flavour;
	}
	public void setFlavour(String flavour) {
		this.flavour = flavour;
	}
	public String getMessageFunctionality() {
		return messageFunctionality;
	}
	public void setMessageFunctionality(String messageFunctionality) {
		this.messageFunctionality = messageFunctionality;
	}
	public String getXmlTag() {
		return xmlTag;
	}
	public void setXmlTag(String xmlTag) {
		this.xmlTag = xmlTag;
	}
	public String getXmlName() {
		return xmlName;
	}
	public void setXmlName(String xmlName) {
		this.xmlName = xmlName;
	}
	public String getBusinessArea() {
		return businessArea;
	}
	public void setBusinessArea(String businessArea) {
		this.businessArea = businessArea;
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
	public String getMessageDefinitionIdentifier() {
		return messageDefinitionIdentifier;
	}
	public void setMessageDefinitionIdentifier(String messageDefinitionIdentifier) {
		this.messageDefinitionIdentifier = messageDefinitionIdentifier;
	}
	
}
