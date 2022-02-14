package com.cfets.cufir.s.data.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EcoreTreeNode implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6106874791901271158L;
	private String id;
	private String name;
	private String type;
	private String level;
	private String objType;
	private String status;
	private String registrationStatus;
	private String imgPath;
	private String pid;
	
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
	public String getObjType() {
		return objType;
	}
	public void setObjType(String objType) {
		this.objType = objType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRegistrationStatus() {
		return registrationStatus;
	}
	public void setRegistrationStatus(String registrationStatus) {
		this.registrationStatus = registrationStatus;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	
}
