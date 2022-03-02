package org.cufir.plugin.mr.bean;

import org.cufir.s.ide.i18n.I18nApi;

/**
 * 数据类型枚举
 */
public enum DataTypesEnum {
	/**
	 * 编辑集
	 */
	CODE_SETS("1", I18nApi.get("tree.bm.dt.cs"), "icons/list_icon_16.png"),
	/**
	 * 文本
	 */
	TEXT("2", I18nApi.get("tree.bm.dt.tt"), "icons/text_16.png"),
	/**
	 * 布尔
	 */
	BOOLEAN("3", I18nApi.get("tree.bm.dt.bn"), "icons/yes_no_16.png"),
	/**
	 * 标识符
	 */
	INDICATOR("4", I18nApi.get("tree.bm.dt.ir"), "icons/indicator_16.png"),
	/**
	 * 十进制
	 */
	DECIMAL("5", I18nApi.get("tree.bm.dt.dl"), "icons/decimal_16.png"),
	/**
	 * 利率
	 */
	RATE("6", I18nApi.get("tree.bm.dt.re"), "icons/rate_16.png"),
	/**
	 * 金额
	 */
	AMOUNT("7", I18nApi.get("tree.bm.dt.at"), "icons/coins_16.png"),
	/**
	 * 数量
	 */
	QUANTITY("8", I18nApi.get("tree.bm.dt.qy"), "icons/balance-icon_16.png"),
	/**
	 * 时间
	 */
	TIME("9", I18nApi.get("tree.bm.dt.te"), "icons/Calendar-icon_16.png"),
	/**
	 * 二进制
	 */
	BINARY("10", I18nApi.get("tree.bm.dt.by"), "icons/binary_16.png"),
	/**
	 * 模式
	 */
	SCHEMA_TYPES("11", I18nApi.get("tree.bm.dt.st"), "icons/library-icon_16.png"),
	/**
	 * 用户自定义
	 */
	USER_DEFINED("12", I18nApi.get("tree.bm.dt.ud"), "icons/FAQ-icon_16.png"),
	/**
	 * 类型
	 */
	CODE("13", "Code", "icons/MXMessageSet.gif")
	;

	private String type;
	
	private String name;
	
	private String imgPath;

	private DataTypesEnum(String type, String name, String imgPath) {
		this.type = type;
		this.name = name;
		this.imgPath = imgPath;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
	
	public static boolean checkTypeName(String typeName) {
		DataTypesEnum[] values = DataTypesEnum.values();
		for (int i = 0; i < values.length; i++) {
			if(values[i].name.equals(typeName)) {
				return true;
			}
		}
		return false;
	}
	
	public static void main(String[] args) {
		System.out.println(checkTypeName("User Defined"));
	}
}
