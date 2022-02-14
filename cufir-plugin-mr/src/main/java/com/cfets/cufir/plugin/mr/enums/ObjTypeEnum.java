package com.cfets.cufir.plugin.mr.enums;

/**
 * objType的类型
 * @author jin.c.li
 *
 */
public enum ObjTypeEnum {
	DataType("1"),
	BusinessComponent("2"),
	BusinessElement("3"),
	BusinessProcess("4"),
	BusinessRole("5"),
	BusinessArea("6"),
	MessageSet("7"),
	ExternalSchema("8"),
	MessageComponent("9"),
	MessageElement("10"),
	MessageDefinition("11"),
	MessageBlock("12"),
	DataType_CodeSet("13");

	private String type;

	private ObjTypeEnum(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
