package org.cufir.s.ide.db;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注释注解，用来规范代码注释
 */
@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Table {

	/**
	 * Table:支持Bean对象的增/删/改/查等操作，Bean字段类型和表列属性一致
	 */
	String CATELOG_TABLE = "T";
	/**
	 * View:支持Bean对象的查寻等操作，Bean字段类型统一为字符串
	 */
	String CATELOG_VIEW = "V";

	/**
	 * 数据库表描述
	 * 
	 * @return
	 */
	public String note() default "";

	/**
	 * 数据库表名称
	 * 
	 * @return
	 */
	public String name();

	/**
	 * 数据库类型:T-Table|V-View
	 * 
	 * @return
	 */
	public String type() default CATELOG_TABLE;
}