package com.cfets.cufir.s.data.bean;

import java.util.Date;

/**
 * 要素
 * @since 1.0.0
 */
public class EcoreBusinessElement {

	private String id;
	/**
	 * 描述
	 */
	private String definition;

	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 登记状态（Registered登记、 Provisionally Registered暂时登记、Added Registered新增）
	 */
	private String registrationStatus;
	
	/**
	 * 删除时间
	 */
	private Date removalDate;
	
	/**
	 * 标识符
	 */
	private String objectIdentifier;
	private String previousVersion;
	private String type;
	
	/**
	 * 数据类型id
	 */
	private String typeId;
	private Integer maxOccurs;
	private Integer minOccurs;
	
	/**
	 * 组件id
	 */
	private String businessComponentId;
	private String isDerived;
	
	private String version;
	
	/**
	 * 是否是iso20022官方（1是）
	 */
	private String isfromiso20022;
	private String isMessageAssociationEnd;
	private Date createTime;
	private Date updateTime;
	private String createUser;
	private String updateUser;
	private String typeName;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public Integer getMaxOccurs() {
		return maxOccurs;
	}
	public void setMaxOccurs(Integer maxOccurs) {
		this.maxOccurs = maxOccurs;
	}
	public Integer getMinOccurs() {
		return minOccurs;
	}
	public void setMinOccurs(Integer minOccurs) {
		this.minOccurs = minOccurs;
	}
	public String getBusinessComponentId() {
		return businessComponentId;
	}
	public void setBusinessComponentId(String businessComponentId) {
		this.businessComponentId = businessComponentId;
	}
	public String getIsDerived() {
		return isDerived;
	}
	public void setIsDerived(String isDerived) {
		this.isDerived = isDerived;
	}
	public String getIsMessageAssociationEnd() {
		return isMessageAssociationEnd;
	}
	public void setIsMessageAssociationEnd(String isMessageAssociationEnd) {
		this.isMessageAssociationEnd = isMessageAssociationEnd;
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
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
}
