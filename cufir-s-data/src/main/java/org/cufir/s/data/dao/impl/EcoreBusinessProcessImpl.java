package org.cufir.s.data.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

import org.apache.log4j.Logger;
import org.cufir.s.ecore.bean.EcoreBusinessProcess;
import org.cufir.s.ide.db.DerbyUtil;

/**
 * EcoreBusinessProcess数据库操作
 */
public class EcoreBusinessProcessImpl{

	private static Logger logger = Logger.getLogger(EcoreBusinessProcessImpl.class);

	private static Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	public void addEcoreBusinessProcessList(List<EcoreBusinessProcess> ecoreBusinessProcesss) throws Exception{
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			
			String sql = "insert into ecore_business_process(id,name,definition,registration_status,removal_date,"
					+ "object_identifier,create_user,create_time,is_from_iso20022) "
					+ "values(?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			for (EcoreBusinessProcess po : ecoreBusinessProcesss) {
				pstmt.setString(1, po.getId());
				pstmt.setString(2, po.getName());
				pstmt.setString(3, po.getDefinition());
				pstmt.setString(4, po.getRegistrationStatus());
				if(po.getRemovalDate()!=null) {
					pstmt.setDate(5, new Date(po.getRemovalDate().getTime()));
				} else {
					pstmt.setNull(5, Types.DATE);
				}
				pstmt.setString(6, po.getObjectIdentifier());
				pstmt.setString(7, po.getCreateUser());
				pstmt.setTimestamp(8, new Timestamp(new java.util.Date().getTime()));
				pstmt.setString(9, po.getIsfromiso20022());
				pstmt.addBatch();
			}
			pstmt.executeBatch();// 执行更新语句
			conn.commit(); // 提交
		} catch (Exception e) {
			logger.error("更新数据失败!");
			conn.rollback();
			throw e;
		} finally {
			DerbyUtil.close(conn, pstmt, rs);
		}

	}
}
