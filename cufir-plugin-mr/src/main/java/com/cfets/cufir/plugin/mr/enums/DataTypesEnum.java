package com.cfets.cufir.plugin.mr.enums;

/**
 * 数据类型枚举
 */
public enum DataTypesEnum {
	
//	1:CodeSet;2:Text;3:Boolean,4:Indicator,5:Decimal,6:Rate,7:Amount,8:Quantity,9:Time,10:Binary,11:SchemaType,12:UserDefined
	/**
	 * 编辑集
	 */
	CODE_SETS("1", "Code Sets", "icons/list_icon_16.png"),
	/**
	 * 文本
	 */
	TEXT("2", "Text", "icons/text_16.png"),
	/**
	 * 布尔
	 */
	BOOLEAN("3", "Boolean", "icons/yes_no_16.png"),
	/**
	 * 标识符
	 */
	INDICATOR("4", "Indicator", "icons/indicator_16.png"),
	/**
	 * 十进制
	 */
	DECIMAL("5", "Decimal", "icons/decimal_16.png"),
	/**
	 * 利率
	 */
	RATE("6", "Rate", "icons/rate_16.png"),
	/**
	 * 金额
	 */
	AMOUNT("7", "Amount", "icons/coins_16.png"),
	/**
	 * 数量
	 */
	QUANTITY("8", "Quantity", "icons/balance-icon_16.png"),
	/**
	 * 时间
	 */
	TIME("9", "Time", "icons/Calendar-icon_16.png"),
	/**
	 * 二进制
	 */
	BINARY("10", "Binary", "icons/binary_16.png"),
	/**
	 * 模式
	 */
	SCHEMA_TYPES("11", "Schema Types", "icons/library-icon_16.png"),
	/**
	 * 用户自定义
	 */
	USER_DEFINED("12", "User Defined", "icons/FAQ-icon_16.png"),
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
