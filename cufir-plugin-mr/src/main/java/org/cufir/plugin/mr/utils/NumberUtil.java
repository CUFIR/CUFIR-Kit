package org.cufir.plugin.mr.utils;

/**
 * 数字格式化
 * @author tangmaoquan
 * @Date 2021年10月15日
 */
public class NumberUtil {

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
	
	/**
	 * 验证字符串不为空
	 * 
	 * @param cs
	 * @return
	 */
	public static boolean isNotBlank(CharSequence cs) {
		return !isBlank(cs);
	}
	
	/**
	 * 验证字符串为空
	 * StringUtils.isBlank(null) = true 
	 * StringUtils.isBlank("") = true
	 * StringUtils.isBlank(" ") = true 
	 * StringUtils.isBlank("bob") = false
	 * StringUtils.isBlank(" bob ") = false
	 **/
	public static boolean isBlank(CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}
}
