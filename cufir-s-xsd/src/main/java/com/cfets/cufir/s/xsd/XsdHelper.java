package com.cfets.cufir.s.xsd;

import org.apache.log4j.Logger;

import com.cfets.cwap.s.daemon.SpiFactory;
import com.cfets.cwap.s.util.db.JdbcManager;

/**
 * 服务帮助类,demo
 * 
 * @author
 * @since 0.7.0
 */

public final class XsdHelper {

	protected static Logger logger = Logger.getLogger(XsdHelper.class);

	// 具体构件常量
	public final static String PLUGIN_ID = "cufir-s-xsd";

	// 发布路径
	public final static String PLUGIN_MAPPING = "/rest/" + PLUGIN_ID;

	private static JdbcManager jdbcManager = null;

	public synchronized static JdbcManager getJdbcManager() {
		if (jdbcManager == null) {
			// JdbcProperty jdbcProperty = new JdbcProperty(
			//		JdbcProperty.DRIVER_MYSQL,
			//		"jdbc:mysql://localhost:3306/mct",
			//		"username", "password");
			// jdbcManager = new JdbcManager(jdbcProperty.buildDS());
			jdbcManager = new JdbcManager(SpiFactory.getDataSource());
		}
		return jdbcManager;
	}

	
	
}
