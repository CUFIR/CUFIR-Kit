package org.cufir.s.ide.utils.i18n;

import org.apache.log4j.Logger;

/**
 * 国际化API
 * 
 * @author HERONGJIE
 * @since 1.0.0
 */
public class I18nApi {
	
	protected static Logger logger = Logger.getLogger(I18nApi.class);
	
	/**
	 * 初始化
	 * @param path
	 * @since 1.0.0
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
	 * @since 1.0.0
	 */
	public static void switchLan(String lan) {
		I18nManager.get().setCurrentLan(lan);
	}
	
	/**
	 * 获取当前的语言环境
	 * 
	 * @return
	 * @since 1.0.0
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
	 * @since 1.0.0
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
	 * @since 1.0.0
	 */
	public static String get(String key,String lan) {
		return I18nManager.get().getValue(key,lan);
	}
	
	public static void main(String[] args) {
		I18nApi.init("D:\\workspace\\config");
		
		System.out.println("当前语言环境："+I18nApi.getCurrentLan());
		System.out.println("当前语言环境编码集："+I18nManager.get().getCharset());
		System.out.println("key=a.k1,val="+I18nApi.get("tree.bm.name"));

//		I18nApi.switchLan(I18nConstant.I18N_LAN_EN);
		System.out.println("当前语言环境："+I18nApi.getCurrentLan());
		System.out.println("key=a.k1,val="+I18nApi.get("a.k1"));
		
		System.out.println("当前语言环境："+I18nApi.getCurrentLan());
		System.out.println("key=b,val="+I18nApi.get("b"));
		System.out.println("key=b,lan=cn,val="+I18nApi.get("b", I18nConstant.I18N_LAN_CN));
		System.out.println("key=b,lan=en,val="+I18nApi.get("b", I18nConstant.I18N_LAN_EN));
		System.out.println("key=b,lan=aaa,val="+I18nApi.get("b", "aaa"));
	}

}
