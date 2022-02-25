package org.cufir.rcp.kit;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;

/**
 * 配置文件处理
 * @author hrj、tangmaoquan
 * @Date 2021年10月15日
 */
public final class Configuration {

	private final static String PROP_FILENAME = "cufir.license";

	private final static String PROP_SIGN = "cufir.signature";

	private Properties prop = new Properties();

	private static Configuration instance = null;

	/**
	 * 通过单例获得对象
	 * 
	 * @return
	 */
	public synchronized static Configuration getInstance() {
		if (instance == null) {
			instance = new Configuration();
		}
		return instance;
	}

	public Configuration() {
		Location installLocation = Platform.getInstallLocation();
		if (installLocation != null) {
			String path = installLocation.getURL().getPath();
			prop = readConfigFile(path + "//" + PROP_FILENAME);
		}
	}

	public String getValue(String key) {
		return prop.getProperty(key, "");
	}

	/**
	 * 获得签名
	 * 
	 * @param key
	 * @return
	 */
	public String getSign(String key) {
		Location installLocation = Platform.getInstallLocation();
		String filePath = installLocation.getURL().getPath() + "//" + PROP_FILENAME;
		String str = readStrFromFile(filePath);

		if (str.indexOf(PROP_SIGN) >= 0) {
			String[] split = str.split(PROP_SIGN);
			if (split.length == 2) {
				return split[1].replaceFirst("=", "");
			}
		}

		return prop.getProperty(key, "");
	}

	/**
	 * 读出文件的所有内容字符串
	 * 
	 * @param filePath
	 * @return
	 */
	public static String readStrFromFile(String filePath) {
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		try {
			fis = new FileInputStream(filePath);
			bis = new BufferedInputStream(fis);
			byte[] b = new byte[bis.available()];
			bis.mark(bis.read(b));
			String string = new String(b, "utf-8");

			return string;
		} catch (Exception e) {
			return "";
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
				if (bis != null) {
					bis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 读取指定路径的配置文件
	 * 
	 * @param propertyPath
	 * @return
	 * @throws IOException
	 */
	public Properties readConfigFile(String propertyPath) {
		InputStream inputStream = null;
		Properties properties = new Properties();
		try {
			inputStream = new BufferedInputStream(new FileInputStream(new File(propertyPath)));
			properties.load(inputStream);
		} catch (Exception ioE) {
			ioE.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return properties;
	}
}
