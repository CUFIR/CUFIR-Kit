package org.cufir.s.ide.i18n;

import org.apache.log4j.Logger;

/**
 * 国际化API
 */
public class I18nApi {
	
	protected static Logger logger = Logger.getLogger(I18nApi.class);
	
	/**
	 * 初始化
	 * @param path
	 */
	public static void init(String configPath) {
		//检查
		//
		I18nConfig config = I18nConfig.build().load(configPath);
		if(config != null && config.validate()) {
			I18nManager.get().load(config);
		}else {
			//配置异常
			logger.error("i18n 配置异常...");
		}
	}
	
	
	/**
	 * 切换当前的语言环境
	 * 
	 * @param lan
	 */
	public static void switchLan(String lan) {
		I18nManager.get().setCurrentLan(lan);
	}
	
	/**
	 * 获取当前的语言环境
	 * 
	 * @return
	 */
	public static String getCurrentLan() {
		return I18nManager.get().getCurrentLan();
	}
	
	/**
	 * 获取当前语言环境的指定key的内容
	 * 
	 * @param category
	 * @param key
	 * @return
	 */
	public static String get(String key) {
		return I18nManager.get().getValue(key);
	}
	
	/**
	 * 获取当前语言环境的指定key的内容
	 * 
	 * @param category
	 * @param key
	 * @return
	 */
	public static String get(String key,String lan) {
		return I18nManager.get().getValue(key,lan);
	}

}
