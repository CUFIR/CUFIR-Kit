package org.cufir.plugin.mr.bean;

import org.cufir.plugin.mr.utils.ImgUtil;

/**
 * 树菜单枚举
 * @author tangmaoquan
 * @Date 2021年9月29日
 */
public enum TreeMenuEnum {

	DATA_TYPES("Data Types", ImgUtil.DATA_TYPES, "1"),
	BUSINESS_COMPONENTS("Business Components", ImgUtil.BUSINESS_COMPONENTS, "2"),
	EXTERNAL_SCHEMAS("External Schemas", ImgUtil.EXTERNAL_SCHEMAS, "3"),
	MESSAGE_COMPONENTS("Message Components", ImgUtil.MESSAGE_COMPONENTS, "4"),
	BUSINESS_AREAS("Business Areas", ImgUtil.BUSINESS_AREAS, "5"),
	MESSAGE_SETS("Message Sets", ImgUtil.MESSAGE_SETS, "6"),
	MESSAGE_DEFINITIONS("Message Definitions", ImgUtil.MESSAGE_DEFINITIONS, "7"),
	VIEW_IDENTICAL_COMPONENTS("View Identical Components", ImgUtil.VIEW_IDENTICAL_COMPONENTS, "8"),
	MESSAGE_DEFINITIONS_CHILD("Message Building Block", ImgUtil.ME, "9");
	private String name;
	private String imgPath;
	private String tabType;
	private TreeMenuEnum(String name, String imgPath, String tabType) {
		this.name = name;
		this.imgPath = imgPath;
		this.tabType = tabType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public String getTabType() {
		return tabType;
	}
	public void setTabType(String tabType) {
		this.tabType = tabType;
	}
	
}
