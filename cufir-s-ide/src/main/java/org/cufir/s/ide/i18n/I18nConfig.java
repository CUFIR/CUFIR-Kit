package org.cufir.s.ide.i18n;

import java.io.File;
import java.util.Properties;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 国际化配置
 */
@Data
@Accessors(chain=true,fluent=true)
public class I18nConfig {

	//配置项的key
	private final static String CONFIG_KEY_LAN = "cufir.kit.i18n.lan";
	private final static String CONFIG_KEY_CHARSET = "cufir.kit.i18n.charset";
	private final static String CONFIG_KEY_PROPS_PATH = "cufir.kit.i18n.props.path";
		
	/**
	 * 当前的语言环境
	 */
	private String currentLan = I18nConstant.I18N_LAN_CN;
	
	/**
	 * 当前的语言环境编码集
	 */
	private String charset = I18nConstant.CHARSET_UTF_8;
	
	/**
	 * 国际化语言文件目录（绝对路径）
	 */
	private String propDirPath = "";
	
	
	private Properties configProperties;
	
	public static I18nConfig build() {
		return new I18nConfig();
	}
	
	/**
	 * 加载配置文件
	 * 
	 * @param configPath
	 * @return
	 */
	public I18nConfig load(String configPath) {
		try {
			this.propDirPath=configPath;
			//
			File file = new File(configPath);
			if (file.exists() && file.isDirectory()) {
				File[] listFiles = file.listFiles();
				for (File file2 : listFiles) {
					// i18n-config.properties文件加载至内存
					if (file2.getName().toLowerCase().equals(I18nConstant.I18N_LAN_CONFIG)) {
						this.configProperties = I18nHelper.readProperties(file2, I18nConstant.CHARSET_UTF_8);
						break;
					}
				}
			}
			
			//初始化字段内容
			initFieldValue();
			return this;
		} catch (Exception e) {
		}
		return null;
	}


	/**
	 * 初始化字段内容
	 */
	private void initFieldValue() {
		if(configProperties != null) {
			String lanStr = configProperties.getProperty(CONFIG_KEY_LAN);
			String charsetStr = configProperties.getProperty(CONFIG_KEY_CHARSET);
			String propsStr = configProperties.getProperty(CONFIG_KEY_PROPS_PATH);
			
			if(!I18nHelper.isEmpty(lanStr)) {
				this.currentLan = lanStr;
			}
			if(!I18nHelper.isEmpty(charsetStr)) {
				this.charset = charsetStr;
			}
			if(!I18nHelper.isEmpty(propsStr)) {
				this.propDirPath = propsStr;
			}
		}
	}

	/**
	 * 检查配置是否正确
	 * @return
	 */
	public boolean validate() {
		if(I18nHelper.isEmpty(propDirPath)) {
			return false;
		}
		if(I18nHelper.isEmpty(charset)) {
			return false;
		}
		if(I18nHelper.isEmpty(currentLan)) {
			return false;
		}
		return true;
	}
	
}
