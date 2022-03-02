package org.cufir.s.ide.i18n;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import lombok.Getter;
import lombok.Setter;

/**
 * 国际化内容管理
 */
public class I18nManager {
	protected static Logger logger = Logger.getLogger(I18nManager.class);
	
	/**
	 * 当前语言环境
	 */
	@Getter
	@Setter
	private String currentLan = I18nConstant.I18N_LAN_EN;
	
	//编码集
	@Getter
	private String charset = I18nConstant.CHARSET_UTF_8;

	//国际化内容 
	private Map<String, Properties> lanProperties = new HashMap<String, Properties>();

	/**
	 * 私有构造函数
	 */
	private I18nManager() {
	}

	/**
	 * 通过单例进行获得
	 */
	private static I18nManager manager = null;

	public synchronized static I18nManager get() {
		if (manager == null) {
			manager = new I18nManager();
		}
		return manager;
	}

	/**
	 * 加载语言环境
	 * 
	 * @param path
	 */
	public void load(I18nConfig config) {
		this.currentLan = config.currentLan();
		this.charset = config.charset();

		// 读取该目录下的配置文件，以文件名为语言类型唯一标识
		File file = new File(config.propDirPath());
		if (file.exists() && file.isDirectory()) {
			File[] listFiles = file.listFiles();
			for (File file2 : listFiles) {
				// 文件是properties文件，加载至内存
				if (file2.getName().endsWith(I18nConstant.PROP_FILE_TYPE)) {
					readProp(file2);
				}
			}
		} else {
			// 语言配置文件所在目录不存在或者不是一个目录
			logger.error("I18n配置文件所在目录不存在或者不是一个目录");
		}

	}

	/**
	 * 读取语言配置文件
	 * 
	 * @param file2
	 */
	private void readProp(File file2) {
		String name = file2.getName();
		String key = name.substring(0, name.indexOf(I18nConstant.KEY_SEPARATOR));
		Properties properties = I18nHelper.readProperties(file2, charset);
		if(properties != null) {
			lanProperties.put(key.toLowerCase(), properties);
		}
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public String getValue(String key) {
		return getValue(key, currentLan);
	}

	/**
	 * 
	 * @param key
	 * @param lan
	 * @return
	 */
	public String getValue(String key, String lan) {
		try {
			Properties properties = lanProperties.get(lan);
			if (properties != null) {
				return properties.getProperty(key);
			} else {
				// 当前语言环境尚未加载
			}
		} catch (Exception e) {
		}
		return "";
	}

}
