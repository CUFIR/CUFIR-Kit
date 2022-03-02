package org.cufir.s.ecore.bean;

import java.util.Date;

import lombok.Data;

/**
 * 进程
 */
@Data
public class EcoreBusinessProcess {

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
	private String isfromiso20022;
	private Date createTime;
	private Date updateTime;
	private String createUser;
	private String updateUser;
}

