package com.cfets.cufir.s.data.bean;

import java.util.Date;

import org.eclipse.emf.common.util.EList;

/**
 * 数据类型
 * @author tangmaoquan_ntt
 * @since 3.2.1.0
 * @Date 2021年9月18日
 */
public class EcoreDataType {

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
	private String minInclusive;
	private String minExclusive;
	private String maxInclusive;
	private String maxExclusive;
	private String pattern;
	private String previousVersion;
	private Integer fractionDigits;
	private Integer totalDigits;
	private String unitCode;
	private Double baseValue;
	private String baseUnitCode;

	/**
	 * 限制最小长度
	 */
	private Integer minLength;

	/**
	 * 限制最大长度
	 */
	private Integer maxLength;

	/**
	 * 长度
	 */
	private Integer length;
	private String meaningWhenTrue;
	private String meaningWhenFalse;
	private String identificationScheme;
	private String value;
	private String literal;
	private String type;
	private String version;
	private String trace;
	
	/**
	 * 是否是iso20022官方（1是）
	 */
	private String isfromiso20022;
	private String processContents;
	private String namespace;
	private String namespaceList;
	private Date createTime;
	private Date updateTime;
	private String createUser;
	private String updateUser;
	//样例
	private EList<String> example;
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
	public String getMinInclusive() {
		return minInclusive;
	}
	public void setMinInclusive(String minInclusive) {
		this.minInclusive = minInclusive;
	}
	public String getMinExclusive() {
		return minExclusive;
	}
	public void setMinExclusive(String minExclusive) {
		this.minExclusive = minExclusive;
	}
	public String getMaxInclusive() {
		return maxInclusive;
	}
	public void setMaxInclusive(String maxInclusive) {
		this.maxInclusive = maxInclusive;
	}
	public String getMaxExclusive() {
		return maxExclusive;
	}
	public void setMaxExclusive(String maxExclusive) {
		this.maxExclusive = maxExclusive;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public String getPreviousVersion() {
		return previousVersion;
	}
	public void setPreviousVersion(String previousVersion) {
		this.previousVersion = previousVersion;
	}
	public Integer getFractionDigits() {
		return fractionDigits;
	}
	public void setFractionDigits(Integer fractionDigits) {
		this.fractionDigits = fractionDigits;
	}
	public Integer getTotalDigits() {
		return totalDigits;
	}
	public void setTotalDigits(Integer totalDigits) {
		this.totalDigits = totalDigits;
	}
	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	public Double getBaseValue() {
		return baseValue;
	}
	public void setBaseValue(Double baseValue) {
		this.baseValue = baseValue;
	}
	public String getBaseUnitCode() {
		return baseUnitCode;
	}
	public void setBaseUnitCode(String baseUnitCode) {
		this.baseUnitCode = baseUnitCode;
	}
	public Integer getMinLength() {
		return minLength;
	}
	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}
	public Integer getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public String getMeaningWhenTrue() {
		return meaningWhenTrue;
	}
	public void setMeaningWhenTrue(String meaningWhenTrue) {
		this.meaningWhenTrue = meaningWhenTrue;
	}
	public String getMeaningWhenFalse() {
		return meaningWhenFalse;
	}
	public void setMeaningWhenFalse(String meaningWhenFalse) {
		this.meaningWhenFalse = meaningWhenFalse;
	}
	public String getIdentificationScheme() {
		return identificationScheme;
	}
	public void setIdentificationScheme(String identificationScheme) {
		this.identificationScheme = identificationScheme;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getLiteral() {
		return literal;
	}
	public void setLiteral(String literal) {
		this.literal = literal;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public EList<String> getExample() {
		return example;
	}
	public void setExample(EList<String> example) {
		this.example = example;
	}
	public String getTrace() {
		return trace;
	}
	public void setTrace(String trace) {
		this.trace = trace;
	}
	public String getProcessContents() {
		return processContents;
	}
	public void setProcessContents(String processContents) {
		this.processContents = processContents;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public String getNamespaceList() {
		return namespaceList;
	}
	public void setNamespaceList(String namespaceList) {
		this.namespaceList = namespaceList;
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

}
