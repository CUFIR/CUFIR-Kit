package com.cfets.cufir.s.xsd.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Properties;

public class CreateBean {
	//包名
	public static String strpackage;
	//数据库表名
	public static String tableName;
	
	public static StringBuffer sb=new StringBuffer();
	
	public static Connection con=null;
	
	public static ResultSet res=null;
	
	public static ResultSetMetaData rsmd=null;
	
	//加载配置文件
	static {
	loadConfig();	
	}

	public static void loadConfig() {
		//InputStream stream=ConnectionDatebase.class.getClassLoader().getResourceAsStream("db.properties");
		Properties pro=new Properties();
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	
	

}
