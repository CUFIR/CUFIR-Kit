package org.cufir.s.ide.utils.i18n;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 国际化辅助类
 * 
 * @author HERONGJIE
 * @since 1.0.0
 */
public class I18nHelper {

	/**
	 * 将properties文件读到Properties对象中
	 * @param file
	 * @param charset
	 * @return
	 * @since 1.0.0
	 */
	public static Properties readProperties(File file, String charset) {
		Properties properties = new Properties();

		FileInputStream fis = null;
		InputStreamReader isr = null;

		try {
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, charset);
			properties.load(isr);
		} catch (Exception e) {
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
				if (isr != null) {
					isr.close();
				}
			} catch (IOException e) {
			}
		}
		return properties;
	}

	/**
	 * 字符串为空？
	 * 
	 * @param str
	 * @return
	 * @since 1.0.0
	 */
	public static boolean isEmpty(String str) {
		return str== null || "".equals(str);
	}
}
