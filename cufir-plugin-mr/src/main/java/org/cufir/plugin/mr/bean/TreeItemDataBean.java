package org.cufir.plugin.mr.bean;

import lombok.Data;

/**
 * 树节点数据
 * @author tangmaoquan
 * @Date 2021年9月29日
 */
@Data
public class TreeItemDataBean {
	private String id;
	private String name;
	private String image;
	private String tabType;
	private String treeLevel;
	private TreeItemDataBean parentTreeItemDataBean;
	private TreeItemDataBean currentTreeItemDataBean;
	
	public TreeItemDataBean(String id, String name, String image, String tabType, String treeLevel) {
		super();
		this.id = id;
		this.name = name;
		this.image = image;
		this.tabType = tabType;
		this.treeLevel = treeLevel;
	}
}
