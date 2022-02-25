package org.cufir.s.ecore.bean;

import java.util.Date;

import org.eclipse.emf.common.util.EList;

import lombok.Data;

/**
 * 数据类型
 * @author tangmaoquan_ntt
 * @since 1.0.0
 */
@Data
@Table(name = "ECORE_DATA_TYPE", note = "数据类型")
public class EcoreDataType {

	@TableColumn(name = "ID", note = "ID")
	private String id;
	
	@TableColumn(name = "DEFINITION", note = "描述")
	private String definition = "";
	
	@TableColumn(name = "NAME", note = "名称")
	private String name = "";
	
	@TableColumn(name = "REGISTRATION_STATUS", note = "登记状态（Registered登记、 Provisionally Registered暂时登记、Added Registered新增、Obsolete过时）")
	private String registrationStatus = "";
	
	@TableColumn(name = "REMOVAL_DATE", note = "删除时间")
	private Date removalDate;
	
	@TableColumn(name = "OBJECT_IDENTIFIER")
	private String objectIdentifier = "";
	
	@TableColumn(name = "MIN_INCLUSIVE", note = "最小包容")
	private String minInclusive = "";
	
	@TableColumn(name = "MIN_EXCLUSIVE", note = "最小专有")
	private String minExclusive = "";
	
	@TableColumn(name = "MAX_INCLUSIVE", note = "最大包容")
	private String maxInclusive = "";
	
	@TableColumn(name = "MAX_EXCLUSIVE", note = "最大专有")
	private String maxExclusive = "";
	
	@TableColumn(name = "PATTERN", note = "模式")
	private String pattern = "";
	
	@TableColumn(name = "PREVIOUS_VERSION")
	private String previousVersion = "";
	
	@TableColumn(name = "FRACTION_DIGITS", note = "小数数位")
	private Integer fractionDigits;
	
	@TableColumn(name = "TOTAL_DIGITS", note = "总数位")
	private Integer totalDigits;
	
	@TableColumn(name = "UNIT_CODE")
	private String unitCode = "";
	
	@TableColumn(name = "BASE_VALUE")
	private Double baseValue;
	
	@TableColumn(name = "BASE_UNIT_CODE")
	private String baseUnitCode = "";

	@TableColumn(name = "MIN_LENGTH", note = "最小长度")
	private Integer minLength;

	@TableColumn(name = "MAX_LENGTH", note = "最大长度")
	private Integer maxLength;

	@TableColumn(name = "LENGTH", note = "长度")
	private Integer length;
	
	@TableColumn(name = "MEANING_WHEN_TRUE")
	private String meaningWhenTrue = "";
	
	@TableColumn(name = "MEANING_WHEN_FALSE")
	private String meaningWhenFalse = "";
	
	@TableColumn(name = "IDENTIFICATION_SCHEME")
	private String identificationScheme = "";
	
	@TableColumn(name = "VALUE")
	private String value = "";
	
	@TableColumn(name = "LITERAL")
	private String literal = "";
	
	@TableColumn(name = "TYPE")
	private String type = "";
	
	@TableColumn(name = "VERSION")
	private String version = "";
	
	@TableColumn(name = "TRACE")
	private String trace;
	
	@TableColumn(name = "IS_FROM_ISO20022", note = "是否ISO20022，1是")
	private String isfromiso20022;
	
	@TableColumn(name = "PROCESS_CONTENTS")
	private String processContents;
	
	@TableColumn(name = "NAMESPACE")
	private String namespace;
	
	@TableColumn(name = "NAMESPACE_LIST")
	private String namespaceList;
	
	@TableColumn(name = "CREATE_TIME")
	private Date createTime;
	
	@TableColumn(name = "UPDATE_TIME")
	private Date updateTime;
	
	@TableColumn(name = "CREATE_USER")
	private String createUser;
	
	@TableColumn(name = "UPDATE_USER")
	private String updateUser;
	
	private EList<String> example;
}
