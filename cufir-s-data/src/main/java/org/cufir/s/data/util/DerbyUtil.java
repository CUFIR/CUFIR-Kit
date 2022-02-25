package org.cufir.s.data.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.cufir.s.data.Constant;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;

/**
 * derby数据库操作工具类
 * 
 * @author wjq
 *
 */
public class DerbyUtil {
	
	private static Logger logger = Logger.getLogger(DerbyUtil.class);
	// 测试入口
	public static boolean TEST = false;
	public static String TEST_URL = "";
	
	public static String URL_SHUTDOWN="";
	
	/**
	 * 获取数据库连接的方法     
	 * 
	 * @return 返回数据库的连接对象    失败返回null  
	 * @throws Exception 
	 */
	public static Connection getConnection() {
		try {
			Location installLocation = Platform.getInstallLocation();
			String dburl="";
			if (installLocation != null) {
				dburl= "jdbc:derby:"
						+ Platform.getInstallLocation().getURL().getPath().replaceFirst("/", "").replace("/", "//")
						+ "config\\db;create=false";
				URL_SHUTDOWN="jdbc:derby:"
						+ Platform.getInstallLocation().getURL().getPath().replaceFirst("/", "").replace("/", "//")
						+ "config\\db;shutdown=true";
			}
			logger.info("数据库URL=" + dburl);
			System.out.println("数据库URL=" + dburl);
			Class.forName(Constant.DRIVER);
			Connection conn = DriverManager.getConnection(dburl);
			conn.setAutoCommit(false);
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
