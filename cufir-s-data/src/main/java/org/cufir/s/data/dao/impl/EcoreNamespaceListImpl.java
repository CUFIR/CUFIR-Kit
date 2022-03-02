package org.cufir.s.data.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.cufir.s.ecore.bean.EcoreNamespaceList;
import org.cufir.s.ide.db.DerbyUtil;

/**
 * EcoreNamespaceList数据库操作
 */
public class EcoreNamespaceListImpl{

	private static Logger logger = Logger.getLogger(EcoreNamespaceListImpl.class);

	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	public void addEcoreNamespaceListList(List<EcoreNamespaceList> ecoreNamespaceLists) throws Exception{
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			
			String sql = "insert into ECORE_NAMESPACE_LIST(id,obj_id,namespace_list,create_user,create_time,is_from_iso20022) "
					+ "values(?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			for (EcoreNamespaceList po : ecoreNamespaceLists) {
				pstmt.setString(1, po.getId());
				pstmt.setString(2, po.getObj_id());
				pstmt.setString(3, po.getNamespaceList());
				pstmt.setString(4, po.getCreateUser());
				pstmt.setTimestamp(5, new Timestamp(new java.util.Date().getTime()));
				pstmt.setString(6, po.getIsfromiso20022());
				pstmt.addBatch();
			}
			int[] result = pstmt.executeBatch();// 执行更新语句
			conn.commit(); // 提交
			if (result != null) {
				int count = 0;
				for (int i = 0; i < result.length; i++) {
					count = count + result[i];
				}
				logger.info(count + "件数据更新成功 SQL=" + sql);
			} else {
				logger.info("没有数据被更新 SQL=" + sql);
			}
		} catch (Exception e) {
			logger.error("更新数据失败!");
			conn.rollback();
			throw e;
		} finally {
			DerbyUtil.close(conn, pstmt, rs);
		}
	}	
}
