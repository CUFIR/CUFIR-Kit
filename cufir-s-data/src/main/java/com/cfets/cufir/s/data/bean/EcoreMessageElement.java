package com.cfets.cufir.s.data.bean;

import java.util.Date;

/**
 * 组件要素
 * @author tangmaoquan_ntt
 * @since 3.2.1.0
 * @Date 2021年9月17日
 */
public class EcoreMessageElement {

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
	
	/**
	 * 上个版本
	 */
	private String previousVersion;
	
	/**
	 * xml标识
	 */
	private String xmlTag;
	
	/**
	 * 类型
	 */
	private String type;
	
	/**
	 * EcoreDataType ID
	 */
	private String typeId;
	
	/**
	 * 最大出现次数
	 */
	private Integer maxOccurs;
	
	/**
	 * 最小出现次数
	 */
	private Integer minOccurs;
	
	/**
	 * 上级组件id
	 */
	private String messageComponentId;
	
	/**
	 * 版本
	 */
	private String version;
	
	/**
	 * 是否是iso20022官方（1是）
	 */
	private String isfromiso20022;
	private String isMessageAssociationEnd;
	
	/**
	 * 元素id或组件id
	 */
	private String trace;
	
	/**
	 * 1元素、2组件
	 */
	private String traceType;
	
	private String isDerived;
	private Date createTime;
	private Date updateTime;
	private String createUser;
	private String updateUser;
	
	private String typeName;
	private String tracesTo;
	
	/**
	 * 是否专业
	 */
	private String technical;
	private String typeOfTracesTo;
	
	/**
	 * 
	 */
	private String tracePath;
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
	public String getXmlTag() {
		return xmlTag;
	}
	public void setXmlTag(String xmlTag) {
		this.xmlTag = xmlTag;
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
	public String getMessageComponentId() {
		return messageComponentId;
	}
	public void setMessageComponentId(String messageComponentId) {
		this.messageComponentId = messageComponentId;
	}
	public String getIsMessageAssociationEnd() {
		return isMessageAssociationEnd;
	}
	public void setIsMessageAssociationEnd(String isMessageAssociationEnd) {
		this.isMessageAssociationEnd = isMessageAssociationEnd;
	}
	public String getTrace() {
		return trace;
	}
	public void setTrace(String trace) {
		this.trace = trace;
	}
	public String getIsDerived() {
		return isDerived;
	}
	public void setIsDerived(String isDerived) {
		this.isDerived = isDerived;
	}
	public String getTraceType() {
		return traceType;
	}
	public void setTraceType(String traceType) {
		this.traceType = traceType;
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
	public String getTracesTo() {
		return tracesTo;
	}
	public void setTracesTo(String tracesTo) {
		this.tracesTo = tracesTo;
	}
	public String getTechnical() {
		return technical;
	}
	public void setTechnical(String technical) {
		this.technical = technical;
	}
	public String getTypeOfTracesTo() {
		return typeOfTracesTo;
	}
	public void setTypeOfTracesTo(String typeOfTracesTo) {
		this.typeOfTracesTo = typeOfTracesTo;
	}
	public String getTracePath() {
		return tracePath;
	}
	public void setTracePath(String tracePath) {
		this.tracePath = tracePath;
	}
	
}

