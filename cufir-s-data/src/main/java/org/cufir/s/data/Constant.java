package org.cufir.s.data;

import org.cufir.s.data.util.SystemUtil;

/**
 * @author wjq
 *
 */
public class Constant {

	// 数据库常量
	public static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	
	public static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
	
	public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	
	public static final String ECLIPSE_PATH = SystemUtil.getEclipsePath().replace("\\", "//");
	
	public static final String SHUTDOWN = "shutdown=true";
	
	public static final String CREATE_FALSE = "create=false";
	
	public static final String CREATE_TRUE = "create=true";

	public static final String DERBY_DB = "db";
	
	public static final String DERBY = "jdbc:derby:";
	
	public static final String DOUBLE_SLASH = "//";
	
	public static final String SEMICOLON = ";";
	
	public static final String CONFIG_PATH = ECLIPSE_PATH + DOUBLE_SLASH + "config";
	
	public static final boolean DERBY_DEV = true;
	
	// 数据常量
	public static final String PROVISIONALLY_REGISTERED = "Provisionally Registered";
	public static final String OBSOLETE = "Obsolete";
	public static final String REGISTERED = "Registered";
	public static final String ADDED_REGISTERED = "Added Registered";
	
	// XML相关
	public static final String HYPHEN = "-";
	public static final String XML_NODE_PLUGINS = "plugins";
	public static final String XML_NODE_PROPS = "props";
	public static final String HOST = "host";
	public static final String XML_NODE_PLUGIN = "plugin";
	public static final String XML_NODE_URL = "url";
	public static final String XML_NODE_LABEL = "label";
	public static final String XML_ATTIBUTE_NAME = "name";
	public static final String XML_ATTIBUTE_TYPE = "type";
	public static final String XML_ATTIBUTE_VERSION = "version";
	public static final String XML_ATTIBUTE_VALUE = "value";
	public static final String XML_ATTIBUTE_LABEL = "label";
	public static final String SINGLE_SLASH = "/";
	public static final String FIlE_TYPE_JAR = ".jar";
}
