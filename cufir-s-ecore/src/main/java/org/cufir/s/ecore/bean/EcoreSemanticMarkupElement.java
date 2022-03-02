package org.cufir.s.ecore.bean;

import java.util.Date;

import org.cufir.s.ide.db.Table;
import org.cufir.s.ide.db.TableColumn;

import lombok.Data;

/**
 * 语义标记要素
 */
@Data
@Table(name = "ECORE_SEMANTIC_MARKUP_ELEMENT", note = "语义标记要素")
public class EcoreSemanticMarkupElement {

	@TableColumn(name = "ID", note = "ID")
	private String id;
	
	@TableColumn(name = "NAME", note = "名称")
	private String name;
	
	@TableColumn(name = "VALUE", note = "标记值")
	private String value;
	
	@TableColumn(name = "SEMANTIC_MARKUP_ID", note = "多对一关系")
	private String semanticMarkupId;
	
	@TableColumn(name = "IS_FROM_ISO20022", note = "是否ISO20022，1是")
	private String isfromiso20022;
	
	@TableColumn(name = "CREATE_TIME")
	private Date createTime;
	
	@TableColumn(name = "UPDATE_TIME")
	private Date updateTime;
	
	@TableColumn(name = "CREATE_USER")
	private String createUser;
	
	@TableColumn(name = "UPDATE_USER")
	private String updateUser;
}
