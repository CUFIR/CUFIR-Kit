package org.cufir.s.ecore.bean;

import java.util.Date;

import lombok.Data;

@Data
public class EcoreNamespaceList {

	private String id;
	
	/**
	 * ExternalSchemaId
	 */
	private String obj_id;
	private String namespaceList;
	private String isfromiso20022;
	private Date createTime;
	private Date updateTime;
	private String createUser;
	private String updateUser;
}

