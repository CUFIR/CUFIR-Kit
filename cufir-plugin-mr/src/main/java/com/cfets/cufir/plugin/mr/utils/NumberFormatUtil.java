package com.cfets.cufir.plugin.mr.utils;

/**
 * 数字格式化
 * @author jin.c.li
 *
 */
public class NumberFormatUtil {

	public static Integer getInt(String str) {
		Integer i = 0;
		if(str != null && str != "") {
			try {
				i = Integer.valueOf(str);
			} catch (NumberFormatException e) {
				i = 0;
			}
		}
		return i;
	}
	
	public static Double getDouble(String str) {
		Double i = 0.0;
		if(str != null && str != "") {
			try {
				i = Double.valueOf(str);
			} catch (NumberFormatException e) {
				i = 0.0;
			}
		}
		return i;
	}
}
