package org.cufir.plugin.mr.bean;

/**
 * 树展示层数枚举
 */
public enum TreeLevelEnum {
	LEVEL_1("1"),
	LEVEL_2("2"),
	LEVEL_3("3"),
	LEVEL_4("4"),
	LEVEL_5("5"),
	;
	
	private String level;
	
	private TreeLevelEnum(String level) {
		this.level = level;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

}
