package org.cufir.s.ecore.bean;

import java.util.Date;

import lombok.Data;

/**
 * 业务域
 */
@Data
public class EcoreBusinessArea {

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
	 * 登记状态（Registered登记、 Provisionally Registered暂时登记、Added Registered新增、Obsolete过时）
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
	private String code;
	
	/**
	 * 是否是iso20022官方（1是）
	 */
	private String isfromiso20022;
	private Date createTime;
	private Date updateTime;
	private String createUser;
	private String updateUser;
}
