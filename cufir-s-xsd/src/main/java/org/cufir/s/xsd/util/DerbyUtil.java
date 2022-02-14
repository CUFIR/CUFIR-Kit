package org.cufir.s.xsd.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;

/**
 * derby数据库操作工具类
 * @author tangmaoquan
 */
public class DerbyUtil {
	
	private static Logger logger = Logger.getLogger(DerbyUtil.class);
	
	private static final String DB_PATH = Constant.CONFIG_PATH + Constant.DOUBLE_SLASH + Constant.DERBY_DB + Constant.SEMICOLON;
	
	private static final String URL_SHUTDOWN = Constant.DERBY + DB_PATH + Constant.SHUTDOWN;
	
	// 测试入口
	public static String TEST_URL = "";
	
	/**
	 * 数据库连接   搬到DerbyUtil中
	 */
	public static Connection getJdbcConnection(String url,String username,String password,
			String driverClassName) {
		Connection connection=null;
		try {
			Properties props=new Properties();
			props.put("user", username);
			props.put("password", password);
			props.put("remarksReporting", "true");
			
			try {
				Class.forName(driverClassName).newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			connection=DriverManager.getConnection(url,props);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	/**
	 * 获取数据库连接的方法     
	 * 
	 * @return 返回数据库的连接对象    失败返回null  
	 * @throws Exception 
	 */
	public static Connection getConnection() {
		try {
//			String url = "C://Users//tangmaoquan_ntt//Desktop//cide-2.0.0//";
			String url = Platform.getInstallLocation().getURL().getPath().replaceFirst("/", "").replace("/", "//");
			String dburl= "jdbc:derby:" + url + "config\\db;create=false";
			Connection conn = null;
			Class.forName(Constant.DRIVER);
			conn = DriverManager.getConnection(dburl);
			conn.setAutoCommit(true);
			logger.info("数据库连接成功");
			return conn;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			logger.error("数据库连接失败");
		}
		return null;
	}

	/**
	 * 关闭数据库连接的方法     
	 * 
	 * @param Connection con,Statement stm,ResultSet rs     
	 * 
	 */
	public static void close(Connection con, Statement stm, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
				}
			if (stm != null){
				stm.close();
				}
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				DriverManager.getConnection(URL_SHUTDOWN);
			} catch (SQLException e) {
				logger.info("数据库已关闭");
			}
			
		}
	}
}
