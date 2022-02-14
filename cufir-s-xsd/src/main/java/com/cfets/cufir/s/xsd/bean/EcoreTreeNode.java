package com.cfets.cufir.s.xsd.bean;

import java.util.ArrayList;
import java.util.List;

public class EcoreTreeNode {

	private String id;
	private String name;
	private String type;
	private String level;
	private List<EcoreTreeNode> childNodes=new ArrayList<EcoreTreeNode>();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public List<EcoreTreeNode> getChildNodes() {
		return childNodes;
	}
	public void setChildNodes(List<EcoreTreeNode> childNodes) {
		this.childNodes = childNodes;
	}
	
}
