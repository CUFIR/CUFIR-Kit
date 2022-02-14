package com.cfets.cufir.plugin.mr.bean;

public class TreeItemDataBean {
	private String id;
	private String name;
	private String image;
	private String tabType;
	private String treeLevel;
	
	private TreeItemDataBean parentTreeItemDataBean;
	
	private TreeItemDataBean currentTreeItemDataBean;

	public TreeItemDataBean getParentTreeItemDataBean() {
		return parentTreeItemDataBean;
	}

	public void setParentTreeItemDataBean(TreeItemDataBean parentTreeItemDataBean) {
		this.parentTreeItemDataBean = parentTreeItemDataBean;
	}

	public TreeItemDataBean getCurrentTreeItemDataBean() {
		return currentTreeItemDataBean;
	}

	public void setCurrentTreeItemDataBean(TreeItemDataBean currentTreeItemDataBean) {
		this.currentTreeItemDataBean = currentTreeItemDataBean;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getTabType() {
		return tabType;
	}
	public void setTabType(String tabType) {
		this.tabType = tabType;
	}
	public String getTreeLevel() {
		return treeLevel;
	}
	public void setTreeLevel(String treeLevel) {
		this.treeLevel = treeLevel;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public TreeItemDataBean(String id, String name, String image, String tabType, String treeLevel) {
		super();
		this.id = id;
		this.name = name;
		this.image = image;
		this.tabType = tabType;
		this.treeLevel = treeLevel;
	}
	
}
