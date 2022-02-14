package org.cufir.plugin.mr.bean;

/**
 * iso20022元数据解析数据库类型枚举（对应EcoreExample表obj_type字段）
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
