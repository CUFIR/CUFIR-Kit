package com.cfets.cufir.s.xsd.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;


import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;

/**
 * derby数据库操作工具类
 * 
 * @author wjq
 *
 */
public class DerbyUtil2 {
	//static String url ="jdbc:derby:D:\\软件\\db;create=false";
	
	private static Logger logger = Logger.getLogger(DerbyUtil2.class);
	
	
	private static final String DB_PATH = Constant.CFETS_PATH + Constant.DOUBLE_SLASH + Constant.DERBY_DB + Constant.SEMICOLON;
	
	private static final String URL_SHUTDOWN = Constant.DERBY + DB_PATH + Constant.SHUTDOWN;
	
	// 测试入口
	public static boolean TEST = false;
	public static String TEST_URL = "";
	
	/**
	 * 获取数据库连接的方法     
	 * 
	 * @return 返回数据库的连接对象    失败返回null  
	 * @throws Exception 
	 */
	public static Connection getConnection() {
		try {
//			Location installLocation = Platform.getInstallLocation();
//			String dburl="";
			String dburl="jdbc:derby:D:\\workspace_baowen\\cufir-s-xsd\\cfets\\db;create=false";
//			if (installLocation != null) {
//				dburl= "jdbc:derby:"
//						+ Platform.getInstallLocation().getURL().getPath().replaceFirst("/", "").replace("/", "//")
//						+ "cfets\\db;create=false";
//			}
			Connection conn = null;
			if(TEST) {
				// 测试用derby
				//logger.info("数据库URL=" + URL_CREATE);
				//System.out.println("数据库URL=" + URL_CREATE);
				//conn = DriverManager.getConnection(URL_CREATE);
			} else {
				//oracle
				//Class.forName(Constant.ORACLE_DRIVER);
				//String url ="jdbc:oracle:thin:@200.31.156.74:1521/MDMDEV";
				//conn=DriverManager.getConnection(url,"rdt","rdt");
				//derby
				logger.info("数据库URL=" + dburl);
				System.out.println("数据库URL=" + dburl);
				Class.forName(Constant.DRIVER);
				conn = DriverManager.getConnection(dburl);
				conn.setAutoCommit(true);
			}
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
	
	
	public static void main(String[] args) {
//		PluginVersionDaoImpl p = new PluginVersionDaoImpl();
//		p.findAll();
//		p.findAll();
		Connection conn1 = DerbyUtil2.getConnection();
		Connection conn2 = DerbyUtil2.getConnection();
		Connection conn3 = DerbyUtil2.getConnection();
		
//		Connection conn= null; 
//		PreparedStatement pstmt=null;
//		ResultSet rs=null;
//		List<PluginVersionPo> pos = new ArrayList<PluginVersionPo>();
//        try {
//        	conn = DerbyUtil.getConnection();
//        	String sql = "select * from CIDE_PLUGIN_INFO";
//        	
//            pstmt = conn.prepareStatement(sql);
//            logger.debug(sql);
//            rs = pstmt.executeQuery();// 获取查询结果�?
//            
//            // 查询结果放入List
//            while(rs.next()) {
//            	PluginVersionPo po =new PluginVersionPo();
//            	po.setPluginId(rs.getString(1));
//            	po.setPluginVersion(rs.getString(2));
//            	po.setLabel(rs.getString(3));
//            	po.setAuthor(rs.getString(4));
//            	po.setDescription(rs.getString(5));
//            	po.setUpdateTs(rs.getTimestamp(6));
//            	po.setCreateTs(rs.getTimestamp(7));
//            	pos.add(po);
//            }
//            if (!pos.isEmpty()) {
//            	logger.info(pos.size()+"件插件注册信�?");
//            } else {
//            	logger.info("没有注册的插件信�?");
//            }
//        } catch (SQLException e) {
//        	logger.error("查询数据失败");
//        	e.printStackTrace();
//        } catch (Exception e) {
//        	logger.error("查询数据失败");
//			e.printStackTrace();
//		}finally {
//			DerbyUtil.close(conn,pstmt,rs);
//		}
	}
}
