package com.cfets.cufir.s.xsd;

import java.util.ArrayList;
import java.util.List;

public class Tree {
	
	private String id;//主键ID
	private String name; //用来显示名称
	private String datatype; //父ID 参照AREA_ID
	private Tree parentObj;//父节点对象
	private List<Tree>childrenList=new ArrayList<Tree>(); //子节点

	
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
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public Tree getParentObj() {
		return parentObj;
	}
	public void setParentObj(Tree parentObj) {
		this.parentObj = parentObj;
	}
	public List<Tree> getChildrenList() {
		return childrenList;
	}
	public void setChildrenList(List<Tree> childrenList) {
		this.childrenList = childrenList;
	}
	

}
