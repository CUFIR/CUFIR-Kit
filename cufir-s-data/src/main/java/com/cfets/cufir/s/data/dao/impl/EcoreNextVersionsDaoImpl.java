package com.cfets.cufir.s.data.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;

import com.cfets.cufir.s.data.bean.EcoreNextVersions;
import com.cfets.cufir.s.data.dao.EcoreNextVersionsDao;
import com.cfets.cufir.s.data.util.DerbyUtil;

/**
 * EcoreNextVersions数据库操作
 * @author zqh
 *
 */
public class EcoreNextVersionsDaoImpl implements EcoreNextVersionsDao{

	private static Logger logger = Logger.getLogger(EcoreNextVersionsDaoImpl.class);

	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	@Override
	public void addEcoreNextVersionsList(List<EcoreNextVersions> ecoreNextVersionss) throws Exception{
		try {
			conn = DerbyUtil.getConnection(); // 获得数据库连接
			
			String sql = "insert into ECORE_NEXT_VERSIONS(id,obj_type,next_version_id,create_user,create_time,is_from_iso20022) "
					+ "values(?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			for (EcoreNextVersions po : ecoreNextVersionss) {
				pstmt.setString(1, po.getId());
				pstmt.setString(2, po.getObjType());
				pstmt.setString(3, po.getNextVersionId());
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
