package org.cufir.s.ecore.bean;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注释注解，用来规范代码注释
 * 
 * @author boqing.shen
 */

@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface TableColumn {

	/**
	 * 数据库列表描述
	 * 
	 */
	public String note() default "";

	/**
	 * 数据库列表名
	 * 
	 */
	public String name() default "";

	/**
	 * 表字段是否自增
	 * 
	 */
	public boolean auto() default false;

	/**
	 * 指定字段获取之后显示的格式
	 * 
	 */
	public String format() default "";

	/**
	 * 数学运算符号
	 * 
	 * @return
	 */
	public int math() default -1;

	/**
	 * 数学运算符号之后的数值：ColValue(mathOp)mathSv
	 * 
	 * @return
	 */
	public double mathsv() default 0.0;

	// ///////////////////////////////////////////////////////

	/**
	 * format数字格式:小数点后保持2位精度
	 * 
	 */
	String NUMBER_P2 = "0.00";
	/**
	 * format数字格式:小数点后保持4位精度
	 * 
	 */
	String NUMBER_P4 = "0.0000";
	/**
	 * format数字格式:小数点后保持6位精度
	 * 
	 */
	String NUMBER_P6 = "0.000000";

	/**
	 * 货币2位精度:##.####
	 * 
	 */
	String CURRENCY_P2 = "##.00";
	/**
	 * 货币四位精度:##.####
	 * 
	 */
	String CURRENCY_P4 = "##.0000";
	/**
	 * 货币六位精度:##.######
	 * 
	 */
	String CURRENCY_P6 = "##.000000";
	/**
	 * 货币分节符，千分位:#,###
	 * 
	 */
	String CURRENCY_SECTION = "#,###";
	/**
	 * 货币分节符，千分位,保留两位分数:#,###.00
	 * 
	 */
	String CURRENCY_SECTION_P2 = "#,###.00";

	/**
	 * dbType(用于View)/format日期格式(用于Bean):date
	 * 
	 */
	String DATE_DATE = "date";
	/**
	 * dbType(用于View)/format日期格式(用于Bean):timestamp
	 * 
	 */
	String DATE_TIMESTAMP = "timestamp";

	/**
	 * 运算符号:加法
	 * 
	 */
	int MATH_PLUS = 1;
	/**
	 * 运算符号:减法
	 * 
	 */
	int MATH_SUB = 2;
	/**
	 * 运算符号:乘法
	 * 
	 */
	int MATH_MULTIPLY = 3;
	/**
	 * 运算符号:除法
	 * 
	 */
	int MATH_DIV = 4;

	/**
	 * dbType格式:一般货币、数量等
	 * 
	 */
	String DB_CURRENCY = "CURRENCY";
	/**
	 * dbType格式:一般Number
	 * 
	 */
	String DB_NUMBER = "NUMBER";

	/**
	 * dbType(用于View)/format日期格式(用于Bean):date
	 * 
	 */
	String DB_DATE = "DATE";
	/**
	 * dbType(用于View)/format日期格式(用于Bean):timestamp
	 * 
	 */
	String DB_TIMESTAMP = "TIMESTAMP";

	/**
	 * 表明字段对应数据库属性为BLOB
	 * 
	 */
	int LOB_BLOB = 2;

	/**
	 * 表明字段对应数据库属性为CLOB
	 * 
	 */
	int LOB_CLOB = 1;
	/**
	 * 表明字段非LOB类型
	 * 
	 */
	int LOB_NONE = 0;

}